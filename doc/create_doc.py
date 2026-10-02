import copy
import requests

from bs4 import BeautifulSoup
from urllib.parse import urljoin, urldefrag


START_URL = "https://docs.spring.io/spring-ai/reference/"
MAX_PAGES = 500

OUTPUT_FILE = "spring-ai-reference.html"


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


def find_next_link(
    soup: BeautifulSoup,
    current_url: str
) -> str | None:
    """
    Sucht:

        <span class="next">
            <a href="...">...</a>
        </span>

    und liefert die absolute URL.
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


def normalize_url(url: str) -> str:
    """
    Entfernt Fragment-Identifier wie #section.
    """
    url, _ = urldefrag(url)
    return url


def get_title(soup: BeautifulSoup) -> str:
    """
    Liefert bevorzugt die H1-Überschrift.
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


def make_resources_absolute(
    element,
    base_url: str
) -> None:
    """
    Wandelt relative Ressourcen-URLs innerhalb eines
    HTML-Elements in absolute URLs um.
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


def make_head_resources_absolute(
    head,
    base_url: str
) -> None:
    """
    Wandelt relative URLs im <head> der ersten Seite
    in absolute URLs um.
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


def prefix_ids(
    article,
    page_number: int
) -> None:
    """
    Verhindert doppelte IDs beim Zusammenfügen
    der einzelnen Dokumentationsseiten.
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


def remove_unwanted_elements(article) -> None:
    """
    Entfernt Elemente, die auf den einzelnen
    Dokumentationsseiten benötigt werden, in der
    Gesamtseite aber nicht erscheinen sollen.

    Entfernt werden:

        .breadcrumbs-container
        .pagination
    """

    for element in article.select(
        ".breadcrumbs-container, .pagination"
    ):
        element.decompose()


def extract_article(
    soup: BeautifulSoup,
    page_url: str,
    page_number: int
):
    """
    Extrahiert:

        <article class="doc">

    und bereitet den Inhalt für die Gesamtseite vor.
    """

    article = soup.find(
        "article",
        class_="doc"
    )

    if article is None:
        return None

    # Kopie erstellen, damit das ursprüngliche
    # Dokument nicht verändert wird.
    article = copy.deepcopy(article)

    # Breadcrumbs und Seitennavigation entfernen.
    remove_unwanted_elements(article)

    # Relative Ressourcen auf die ursprüngliche
    # Dokumentationsseite beziehen.
    make_resources_absolute(
        article,
        page_url
    )

    # Doppelte IDs vermeiden.
    prefix_ids(
        article,
        page_number
    )

    return article


def crawl(
    start_url: str,
    max_pages: int = MAX_PAGES
):
    """
    Durchläuft die komplette Dokumentation über die
    jeweiligen Next-Links.

    Gleichzeitig werden die <article class="doc">
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

        # Schutz gegen Schleifen
        if current_url in visited:

            print()
            print(
                "Abbruch: Schleife erkannt."
            )
            print(
                f"Bereits besucht: {current_url}"
            )

            break

        print(
            f"{number:3}. {current_url}"
        )

        visited.add(current_url)

        try:
            soup = load_page(
                current_url
            )

        except requests.RequestException as error:

            print()
            print(
                "Abbruch: Fehler beim Laden."
            )
            print(error)

            break

        # Erste Seite merken.
        # Deren <head> verwenden wir später.
        if first_soup is None:
            first_soup = soup

        title = get_title(soup)

        print(
            f"     Titel: {title}"
        )

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

        # Next-Link aus der ORIGINAL-Seite ermitteln.
        # Die Pagination wurde nur aus der Artikelkopie
        # entfernt und steht hier daher weiterhin zur
        # Verfügung.
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

        if next_url in visited:

            print(
                "Abbruch: Next-Link führt auf "
                "eine bereits besuchte Seite."
            )
            print(next_url)

            break

        current_url = next_url

    else:

        print()
        print(
            f"Sicherheitsabbruch nach "
            f"{max_pages} Seiten."
        )

    return first_soup, pages


def create_combined_html(
    first_soup: BeautifulSoup,
    pages: list[dict],
    output_file: str
) -> None:
    """
    Erstellt das Gesamt-HTML-Dokument.

    Der <head> der ersten Dokumentationsseite wird
    übernommen, damit das Spring-AI-CSS erhalten bleibt.
    """

    output = BeautifulSoup(
        "<!DOCTYPE html>"
        "<html>"
        "<head></head>"
        "<body></body>"
        "</html>",
        "html.parser"
    )

    #
    # HEAD DER ERSTEN SEITE
    #

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

    #
    # Titel anpassen
    #

    if output.title:

        output.title.string = (
            "Spring AI Reference Documentation"
        )

    #
    # BODY
    #

    body = output.body

    main = output.new_tag(
        "main",
        attrs={
            "class": "article"
        }
    )

    body.append(main)

    #
    # Alle Artikel einsetzen
    #

    for page in pages:

        section = output.new_tag(
            "section",
            attrs={
                "class": "combined-page",
                "data-source-url": page["url"],
                "id": f"combined-page-{page['number']}"
            }
        )

        section.append(
            page["article"]
        )

        main.append(section)

    #
    # Ergänzendes CSS
    #

    style = output.new_tag("style")

    style.string = """
    .combined-page {
        margin-bottom: 3rem;
    }

    @media print {
        .combined-page {
            break-before: page;
        }

        .combined-page:first-child {
            break-before: auto;
        }
    }
    """

    output.head.append(style)

    #
    # Datei schreiben
    #

    with open(
        output_file,
        "w",
        encoding="utf-8"
    ) as file:

        file.write(
            str(output)
        )


def main():

    print("=" * 80)
    print(
        "Spring AI Reference Documentation"
    )
    print("=" * 80)

    print(
        f"Start:   {START_URL}"
    )

    print(
        f"Maximum: {MAX_PAGES} Seiten"
    )

    print()

    first_soup, pages = crawl(
        START_URL,
        MAX_PAGES
    )

    if first_soup is None:

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

    print()
    print(
        "Erzeuge Gesamt-HTML ..."
    )

    create_combined_html(
        first_soup,
        pages,
        OUTPUT_FILE
    )

    print()
    print(
        f"Fertig: {OUTPUT_FILE}"
    )


if __name__ == "__main__":
    main()