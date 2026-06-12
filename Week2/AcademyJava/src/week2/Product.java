package week2; 
import java.util.*;
import java.util.function.*; 

/****OBJETIVO****
 * Practicar Predicate, Function, Consumer, Supplier y method references.
 * 
 * ***REQUISITOS***
 * Crear record Product(String name, double price, String category, boolean inStock) con método isAvailable()
 * Crear clase ProductPipeline con filtro Predicate<Product> encadenable.
 * Métodos fluidos: where(Predicate), transform(Function), forEach(Consumer)
 * Usar Supplier<List<Product>> para generar datos de prueba.
 * Usar al menos 3 method references (ej: Product::isAvailable, System.out::println)
 * Demostrar encadenamiento de predicados con .and() y .or()
 * */

record Product(String name, double price, String category, boolean inStock) {
    boolean isAvailable() { return inStock; }

    String toDisplayString() {
        return String.format("%-15s $%7.2f  %-12s [%s]",
            name, price, category, inStock ? "En stock" : "Agotado");
    }
}

class ProductPipeline {
    private Predicate<Product> filter = p -> true;
    private Function<Product, String> transform = Product::toDisplayString;

    public ProductPipeline where(Predicate<Product> predicate) {
        // TODO: encadenar con .and()
        this.filter = this.filter.and(predicate);
        return this;
    }

    public ProductPipeline transform(Function<Product, String> fn) {
        this.transform = fn;
        return this;
    }

    public void forEach(List<Product> products, Consumer<String> action) {
        // TODO: filtrar productos, aplicar transformacion, ejecutar accion
        for (Product p : products) {
            if (this.filter.test(p)) {
                action.accept(this.transform.apply(p));
            }
        }
    }

    public long count(List<Product> products) {
        // TODO: contar productos que pasan el filtro
        long total = 0;
        for (Product p : products) {
            if (this.filter.test(p)) total++; //aquí el médoto .teste(p) ya devuelve un valor booleano no hace falta ponerle true. 
        }
        return total;
    }
}

    class ProductCatalog {
    public static void main(String[] args) {
        // TODO: Supplier para crear datos de prueba
        Supplier<List<Product>> catalogSupplier = () -> List.of(
            new Product("Laptop", 999.99, "Electronica", true),
            new Product("Mouse", 29.99, "Electronica", true),
            new Product("Teclado", 79.99, "Electronica", false),
            new Product("Camisa", 39.99, "Ropa", true),
            new Product("Java Book", 49.99, "Libros", true),
            new Product("Monitor", 349.99, "Electronica", true)
        );

        List<Product> catalogo = catalogSupplier.get();

        System.out.println("=== Catalogo Completo ===");
        // TODO: usar method reference System.out::println
        catalogo.stream()
            .map(Product::toDisplayString)
            .forEach(System.out::println);

        System.out.println("\n=== Pipeline: Electronica en stock, precio > $50 ===");
        // TODO: crear pipeline con 3 condiciones encadenadas
        new ProductPipeline()
            .where(Product::isAvailable)          // method reference
            .where(p -> p.category().equals("Electronica"))
            .where(p -> p.price() > 50)
            .forEach(catalogo, System.out::println);              // TODO: method reference

        System.out.println("\n=== Pipeline: Disponibles, precio < $100 ===");
        ProductPipeline pipeline = new ProductPipeline()
            .where(Product::isAvailable)
            .where(p -> p.price() < 100)
            .transform(p -> "  * " + p.name().toUpperCase() + " - $" + p.price());
        pipeline.forEach(catalogo, System.out::println);          // TODO: method reference

        System.out.println("\nTotal disponibles: "
            + new ProductPipeline()
                .where(Product::isAvailable)
                .count(catalogo));
    }
}
