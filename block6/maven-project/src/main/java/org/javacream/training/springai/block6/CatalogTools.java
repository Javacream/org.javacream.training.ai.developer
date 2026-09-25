package org.javacream.training.springai.block6;

import java.util.Map;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class CatalogTools {
    private final Map<String, Product> products = Map.of(
        "monitor", new Product("Monitor", 299.0, 12),
        "keyboard", new Product("Keyboard", 89.0, 30),
        "dock", new Product("Docking Station", 159.0, 8)
    );

    @Tool(description = "Liest Produktinformationen aus dem lokalen read-only Seminarkatalog")
    public Product findProduct(
            @ToolParam(description = "Produktname: monitor, keyboard oder dock") String product) {
        return products.getOrDefault(product.toLowerCase(), new Product("unknown", -1, 0));
    }

    public record Product(String name, double price, int stock) {}
}
