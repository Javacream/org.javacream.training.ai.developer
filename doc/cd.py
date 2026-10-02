import copy
from pathlib import Path
from urllib.parse import urljoin, urldefrag

import requests
from bs4 import BeautifulSoup
from playwright.sync_api import sync_playwright


# ---------------------------------------------------------------------------
# Konfiguration
# ---------------------------------------------------------------------------

START_URL = "https://docs.spring.io/spring-ai/reference/"
MAX_PAGES = 500

OUTPUT_FILE = "spring-ai-reference.html"
PDF_OUTPUT_FILE = "spring-ai-reference.pdf"


# ---------------------------------------------------------------------------
# HTML laden
# ---------------------------------------------------------------------------

def load_page(url: str) -> BeautifulSoup:
    """
    Lädt eine HTML-Seite und liefert ein BeautifulSoup-Objekt zurück.
    """

    response = requests.get(
        url,
        timeout=30,
        headers={
            "User-Agent": "Mozilla/5.0"
        }
    )

    response.raise_for_status()

    return BeautifulSoup(
        response.text,
        "html.parser"
    )


# ---------------------------------------------------------------------------
# Next-Link ermitteln
# ---------------------------------------------------------------------------

def find_next_link(
    soup: BeautifulSoup,
    current_url: str
) -> str | None:
    """
    Sucht in der Spring-Dokumentation nach:

        <span class="next">
            <a href="...">...</a>
        </span>

    und liefert die absolute URL der nächsten Seite zurück.
    """

    next_span = soup.find(
        "span",
        class_="next"
    )

    if next_span is None:
        return None

    link = next_span.find(
        "a",
        href=True
    )

    if link is None:
        return None

    return urljoin(
        current_url,
        link["href"]
    )


# ---------------------------------------------------------------------------
# URL normalisieren
# ---------------------------------------------------------------------------

def normalize_url(url: str) -> str:
    """
    Entfernt Fragment-Identifier wie #section.

    Dadurch wird verhindert, dass dieselbe HTML-Seite
    aufgrund unterschiedlicher Anker mehrfach besucht wird.
    """

    url, _ = urldefrag(url)

    return url


# ---------------------------------------------------------------------------
# Titel ermitteln
# ---------------------------------------------------------------------------

def get_title(soup: BeautifulSoup) -> str:
    """
    Liefert bevorzugt die erste H1-Überschrift.
    """

    h1 = soup.find("h1")

    if h1:
        return h1.get_text(
            " ",
            strip=True
        )

    if soup.title:
        return soup.title.get_text(
            " ",
            strip=True
        )

    return "(kein Titel)"


# ---------------------------------------------------------------------------
# Relative Ressourcen-URLs absolut machen
# ---------------------------------------------------------------------------

def make_resources_absolute(
    element,
    base_url: str
) -> None:
    """
    Wandelt relative Ressourcen-URLs innerhalb eines
    HTML-Elements in absolute URLs um.

    Behandelt:
        href
        src
        srcset
    """

    # href
    for tag in element.find_all(href=True):

        href = tag["href"]

        if not href.startswith("#"):
            tag["href"] = urljoin(
                base_url,
                href
            )

    # src
    for tag in element.find_all(src=True):

        tag["src"] = urljoin(
            base_url,
            tag["src"]
        )

    # srcset
    for tag in element.find_all(srcset=True):

        entries = []

        for entry in tag["srcset"].split(","):

            parts = entry.strip().split()

            if not parts:
                continue

            parts[0] = urljoin(
                base_url,
                parts[0]
            )

            entries.append(
                " ".join(parts)
            )

        tag["srcset"] = ", ".join(entries)


# ---------------------------------------------------------------------------
# URLs im HEAD absolut machen
# ---------------------------------------------------------------------------

def make_head_resources_absolute(
    head,
    base_url: str
) -> None:
    """
    Wandelt relative URLs im <head> der ersten Seite
    in absolute URLs um.

    Dadurch können insbesondere CSS-Dateien und
    JavaScript-Dateien auch aus der lokal gespeicherten
    Gesamtseite geladen werden.
    """

    for tag in head.find_all(href=True):

        href = tag["href"]

        if not href.startswith("#"):
            tag["href"] = urljoin(
                base_url,
                href
            )

    for tag in head.find_all(src=True):

        tag["src"] = urljoin(
            base_url,
            tag["src"]
        )


# ---------------------------------------------------------------------------
# Unerwünschte Elemente entfernen
# ---------------------------------------------------------------------------

def remove_unwanted_elements(article) -> None:
    """
    Entfernt Elemente der einzelnen Dokumentationsseiten,
    die in der Gesamtseite nicht benötigt werden.

    Entfernt werden:

        .breadcrumbs-container
        .pagination
    """

    for element in article.select(
        ".breadcrumbs-container, .pagination"
    ):
        element.decompose()


# ---------------------------------------------------------------------------
# IDs eindeutig machen
# ---------------------------------------------------------------------------

def prefix_ids(
    article,
    page_number: int
) -> None:
    """
    Verhindert doppelte IDs beim Zusammenfügen der
    Dokumentationsseiten.

    Beispiel:

        id="overview"

    wird auf Seite 12 zu:

        id="page-12-overview"

    Lokale Links werden entsprechend angepasst.
    """

    prefix = f"page-{page_number}-"

    id_mapping = {}

    # IDs ändern
    for element in article.find_all(id=True):

        old_id = element["id"]
        new_id = prefix + old_id

        id_mapping[old_id] = new_id

        element["id"] = new_id

    # Lokale #Links korrigieren
    for link in article.find_all(
        "a",
        href=True
    ):

        href = link["href"]

        if not href.startswith("#"):
            continue

        old_id = href[1:]

        if old_id in id_mapping:

            link["href"] = (
                "#"
                + id_mapping[old_id]
            )


# ---------------------------------------------------------------------------
# Artikel extrahieren
# ---------------------------------------------------------------------------

def extract_article(
    soup: BeautifulSoup,
    page_url: str,
    page_number: int
):
    """
    Extrahiert:

        <article class="doc">

    und bereitet den Artikel für die Gesamtseite vor.
    """

    article = soup.find(
        "article",
        class_="doc"
    )

    if article is None:
        return None

    # Kopie erstellen, damit die Originalseite nicht
    # verändert wird. Das ist insbesondere wichtig,
    # weil wir deren Pagination noch für den Next-Link
    # benötigen.
    article = copy.deepcopy(article)

    # Breadcrumbs und Pagination entfernen
    remove_unwanted_elements(article)

    # Relative URLs absolut machen
    make_resources_absolute(
        article,
        page_url
    )

    # IDs eindeutig machen
    prefix_ids(
        article,
        page_number
    )

    return article


# ---------------------------------------------------------------------------
# Dokumentation crawlen
# ---------------------------------------------------------------------------

def crawl(
    start_url: str,
    max_pages: int = MAX_PAGES
):
    """
    Durchläuft die Spring-AI-Dokumentation über die
    jeweiligen Next-Links.

    Abbruchbedingungen:

        - Kein Next-Link vorhanden
        - Bereits besuchte URL
        - HTTP-Fehler
        - maximal MAX_PAGES Seiten

    Gleichzeitig werden alle <article class="doc">
    Elemente gesammelt.
    """

    visited = set()
    pages = []

    current_url = normalize_url(
        start_url
    )

    first_soup = None

    for number in range(
        1,
        max_pages + 1
    ):

        # ---------------------------------------------------------------
        # Schleifenerkennung
        # ---------------------------------------------------------------

        if current_url in visited:

            print()
            print("Abbruch: Schleife erkannt.")
            print(
                f"Bereits besucht: {current_url}"
            )

            break

        print(
            f"{number:3}. {current_url}"
        )

        visited.add(current_url)

        # ---------------------------------------------------------------
        # Seite laden
        # ---------------------------------------------------------------

        try:

            soup = load_page(
                current_url
            )

        except requests.RequestException as error:

            print()
            print(
                "Abbruch: Fehler beim Laden der Seite."
            )
            print(error)

            break

        # ---------------------------------------------------------------
        # Erste Seite für HEAD/CSS merken
        # ---------------------------------------------------------------

        if first_soup is None:
            first_soup = soup

        # ---------------------------------------------------------------
        # Titel
        # ---------------------------------------------------------------

        title = get_title(soup)

        print(
            f"     Titel: {title}"
        )

        # ---------------------------------------------------------------
        # article.doc extrahieren
        # ---------------------------------------------------------------

        article = extract_article(
            soup,
            current_url,
            number
        )

        if article is None:

            print(
                '     WARNUNG: '
                '<article class="doc"> '
                "nicht gefunden."
            )

        else:

            print(
                "     Artikel gefunden."
            )

            pages.append({
                "number": number,
                "title": title,
                "url": current_url,
                "article": article,
            })

        # ---------------------------------------------------------------
        # Next-Link
        #
        # Wichtig:
        # Wir verwenden hier weiterhin das Original-soup.
        # Die Pagination wurde nur aus der Kopie des
        # Artikels entfernt.
        # ---------------------------------------------------------------

        next_url = find_next_link(
            soup,
            current_url
        )

        if next_url is None:

            print()
            print(
                "Kein Next-Link gefunden."
            )
            print(
                "Ende der Dokumentation erreicht."
            )

            break

        next_url = normalize_url(
            next_url
        )

        print(
            f"     Next: {next_url}"
        )
        print()

        # ---------------------------------------------------------------
        # Noch einmal Schleife prüfen
        # ---------------------------------------------------------------

        if next_url in visited:

            print(
                "Abbruch: Next-Link führt auf eine "
                "bereits besuchte Seite."
            )
            print(next_url)

            break

        current_url = next_url

    else:

        print()
        print(
            f"Sicherheitsabbruch: Maximale Anzahl "
            f"von {max_pages} Seiten erreicht."
        )

    return first_soup, pages


# ---------------------------------------------------------------------------
# Gesamt-HTML erzeugen
# ---------------------------------------------------------------------------

def create_combined_html(
    first_soup: BeautifulSoup,
    pages: list[dict],
    output_file: str
) -> None:
    """
    Erstellt die Gesamt-HTML-Datei.

    Der <head> der ersten Spring-AI-Seite wird übernommen.
    Dadurch wird das CSS der Originaldokumentation verwendet.
    """

    output = BeautifulSoup(
        "<!DOCTYPE html>"
        "<html>"
        "<head></head>"
        "<body></body>"
        "</html>",
        "html.parser"
    )

    # -------------------------------------------------------------------
    # HEAD der ersten Seite übernehmen
    # -------------------------------------------------------------------

    if first_soup.head:

        head = copy.deepcopy(
            first_soup.head
        )

        make_head_resources_absolute(
            head,
            START_URL
        )

        output.html.head.replace_with(
            head
        )

    # -------------------------------------------------------------------
    # Dokumenttitel
    # -------------------------------------------------------------------

    if output.title:

        output.title.string = (
            "Spring AI Reference Documentation"
        )

    else:

        title = output.new_tag("title")

        title.string = (
            "Spring AI Reference Documentation"
        )

        output.head.append(title)

    # -------------------------------------------------------------------
    # BODY
    # -------------------------------------------------------------------

    body = output.body

    main = output.new_tag(
        "main",
        attrs={
            "class": "article"
        }
    )

    body.append(main)

    # -------------------------------------------------------------------
    # Alle Artikel einsetzen
    # -------------------------------------------------------------------

    for page in pages:

        section = output.new_tag(
            "section",
            attrs={
                "class": "combined-page",
                "data-source-url": page["url"],
                "id": (
                    f"combined-page-"
                    f"{page['number']}"
                )
            }
        )

        section.append(
            page["article"]
        )

        main.append(section)

    # -------------------------------------------------------------------
    # Zusätzliches CSS für die Gesamtseite
    # -------------------------------------------------------------------

    style = output.new_tag(
        "style"
    )

    style.string = """
    .combined-page {
        margin-bottom: 3rem;
    }

    @media print {

        .combined-page {
            break-before: page;
            page-break-before: always;
        }

        .combined-page:first-child {
            break-before: auto;
            page-break-before: auto;
        }

    }
    """

    output.head.append(
        style
    )

    # -------------------------------------------------------------------
    # HTML-Datei speichern
    # -------------------------------------------------------------------

    with open(
        output_file,
        "w",
        encoding="utf-8"
    ) as file:

        file.write(
            str(output)
        )


# ---------------------------------------------------------------------------
# PDF erzeugen
# ---------------------------------------------------------------------------

def create_pdf(
    html_file: str,
    pdf_file: str
) -> None:
    """
    Rendert die erzeugte Gesamt-HTML-Datei mit Chromium
    als PDF.

    Dadurch werden CSS, Bilder, SVGs, Fonts usw. durch
    einen echten Browser gerendert.
    """

    html_path = Path(
        html_file
    ).resolve()

    pdf_path = Path(
        pdf_file
    ).resolve()

    print()
    print("Erzeuge PDF ...")

    with sync_playwright() as playwright:

        # ---------------------------------------------------------------
        # Chromium starten
        # ---------------------------------------------------------------

        browser = playwright.chromium.launch(
            headless=True
        )

        try:

            page = browser.new_page()

            # -----------------------------------------------------------
            # Lokale Gesamt-HTML-Datei laden.
            #
            # networkidle sorgt dafür, dass externe CSS-Dateien,
            # Bilder usw. möglichst vollständig geladen wurden.
            # -----------------------------------------------------------

            page.goto(
                html_path.as_uri(),
                wait_until="networkidle",
                timeout=120_000
            )

            # -----------------------------------------------------------
            # Bildschirm-CSS verwenden.
            #
            # Damit orientiert sich die Darstellung möglichst eng
            # an der Darstellung der Spring-AI-Webseite.
            # -----------------------------------------------------------

            page.emulate_media(
                media="screen"
            )

            # -----------------------------------------------------------
            # PDF erzeugen
            # -----------------------------------------------------------

            page.pdf(
                path=str(pdf_path),

                format="A4",

                print_background=True,

                margin={
                    "top": "15mm",
                    "right": "15mm",
                    "bottom": "15mm",
                    "left": "15mm"
                },

                prefer_css_page_size=False
            )

        finally:

            browser.close()

    print(
        f"PDF gespeichert: {pdf_path}"
    )


# ---------------------------------------------------------------------------
# Hauptprogramm
# ---------------------------------------------------------------------------

def main():

    print("=" * 80)
    print(
        "Spring AI Reference Documentation"
    )
    print("=" * 80)

    print(
        f"Start:        {START_URL}"
    )

    print(
        f"Maximum:      {MAX_PAGES} Seiten"
    )

    print(
        f"HTML-Ausgabe: {OUTPUT_FILE}"
    )

    print(
        f"PDF-Ausgabe:  {PDF_OUTPUT_FILE}"
    )

    print()

    # -------------------------------------------------------------------
    # Dokumentation crawlen
    # -------------------------------------------------------------------

    first_soup, pages = crawl(
        START_URL,
        MAX_PAGES
    )

    if first_soup is None:

        print()
        print(
            "Keine Seite konnte geladen werden."
        )

        return

    print()
    print("=" * 80)
    print(
        f"Gefundene Artikel: {len(pages)}"
    )
    print("=" * 80)

    # -------------------------------------------------------------------
    # Gesamt-HTML erzeugen
    # -------------------------------------------------------------------

    print()
    print(
        "Erzeuge Gesamt-HTML ..."
    )

    create_combined_html(
        first_soup,
        pages,
        OUTPUT_FILE
    )

    print(
        f"HTML gespeichert: "
        f"{Path(OUTPUT_FILE).resolve()}"
    )

    # -------------------------------------------------------------------
    # PDF erzeugen
    # -------------------------------------------------------------------

    create_pdf(
        OUTPUT_FILE,
        PDF_OUTPUT_FILE
    )

    # -------------------------------------------------------------------
    # Fertig
    # -------------------------------------------------------------------

    print()
    print("=" * 80)
    print("Fertig")
    print("=" * 80)

    print(
        f"HTML: "
        f"{Path(OUTPUT_FILE).resolve()}"
    )

    print(
        f"PDF:  "
        f"{Path(PDF_OUTPUT_FILE).resolve()}"
    )


# ---------------------------------------------------------------------------
# Programmstart
# ---------------------------------------------------------------------------

if __name__ == "__main__":
    main()