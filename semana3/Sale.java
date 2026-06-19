package semana3;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.*; 

/***OBJETIVO***
 *Dominar la Streams API con Collectors, agrupaciones y estadísticas.
 ****REQUISITOS*** 
 * Crear record Sale(String product, String category, double amount, String region, LocalDate date).
 * Calcular el ingreso total con mapToDouble + sum().
 * Agrupar ingresos por categoría con groupingBy + summingDouble.
 * Obtener el Top 3 de productos por monto con sorted + limit.
 * Ingresos por región ordenados de mayor a menor.
 * Promedio de venta por categoría con averagingDouble.
 * Producto más caro por región con groupingBy + maxBy
 * */

record Sale(String product, String category, double amount,
            String region, LocalDate date) {}

    class SalesAnalyzer {
    public static void main(String[] args) {
        List<Sale> sales = List.of(
            new Sale("Laptop",  "Tech",   25000, "CDMX", LocalDate.of(2026, 1, 15)),
            new Sale("Phone",   "Tech",   15000, "GDL",  LocalDate.of(2026, 1, 20)),
            new Sale("Desk",    "Office",  8000, "CDMX", LocalDate.of(2026, 2, 1)),
            new Sale("Chair",   "Office",  5000, "MTY",  LocalDate.of(2026, 2, 10)),
            new Sale("Monitor", "Tech",   12000, "CDMX", LocalDate.of(2026, 2, 15)),
            new Sale("Tablet",  "Tech",   10000, "GDL",  LocalDate.of(2026, 1, 25)),
            new Sale("Lamp",    "Office",  2000, "MTY",  LocalDate.of(2026, 1, 30))
        );

        // 1. Ingreso total
        double total = sales.stream() // aquí se abre un flujo stream de datos a partir de sales. 
            .mapToDouble(sale -> sale.amount()) //método para obtener la cantidad de dinero de una venta. 
            .sum();
        System.out.println("=== Ingreso Total ===");
        System.out.printf("$%,.2f%n", total);

        // 2. Ingresos por categoria
        Map<String, Double> byCategory = sales.stream()  
            .collect(groupingBy(Sale::category, summingDouble(Sale::amount)));
        // total vendiddo pero por categoria y de cada grupo formado toma los montos (amount) y se suma.
        System.out.println("\n=== Ingresos por Categoria ===");
        byCategory.entrySet().stream() 
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(e ->System.out.printf("  %s: $%,.2f%n", e.getKey(), e.getValue()));
        /*En las líneas 47 y 48 hice un cambio porque en la salida esperada se espera de mayor a menor 
         *utilizando .sorted y el forEach para agregarle el método */
        // 3. Top 3 productos por monto
        System.out.println("\n=== Top 3 Productos ===");
        sales.stream()
            .sorted(Comparator.comparingDouble(Sale::amount).reversed()) //ordernar los productos de mayor a menor. 
            .limit(3) //aquí estoy indicando que el limite son los tres productos
            .forEach(s -> System.out.printf("  %s: $%,.2f%n", s.product(), s.amount()));

        // 4. Ingresos por region (mayor a menor)
        System.out.println("\n=== Ingresos por Region ===");
        // TODO: groupingBy region, summingDouble, ordenar por valor descendente
        sales.stream()
            .collect(groupingBy(Sale::region, summingDouble(Sale::amount))) //agrupa y suma por cada region 
            .entrySet().stream()     //convierte el resultado de la linea de arriba en un nuevo stream
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())// lo ordena de mayor a menor 
            .forEach(e -> System.out.printf("  %s: $%,.2f%n", e.getKey(), e.getValue()));

        // 5. Promedio por categoria
        Map<String, Double> avgByCategory = sales.stream()
            .collect(groupingBy(Sale::category, averagingDouble(Sale::amount)));
        System.out.println("\n=== Promedio por Categoria ===");
        avgByCategory.forEach((cat, avg) ->
            System.out.printf("  %s: $%,.2f%n", cat, avg));

        // 6. Producto mas caro por region
        System.out.println("\n=== Producto mas Caro por Region ===");
        // TODO: groupingBy region, maxBy comparando amount
        Map<String, Optional<Sale>> bestPerRegion = sales.stream()
            .collect(groupingBy(Sale::region,
                maxBy(Comparator.comparingDouble(Sale::amount))));
        bestPerRegion.forEach((region, sale) ->
            sale.ifPresent(s -> System.out.printf("  %s: %s ($%,.2f)%n",
                region, s.product(), s.amount())));

        // 7. Conteo por mes
        Map<Integer, Long> byMonth = sales.stream()
            .collect(groupingBy(
                s -> s.date().getMonthValue(), counting()));//la cantidad de ventas no el total del dinero 
                                              //pertenece a la clase collectors, es un conteo 
        								      // cuando se groupingBy con dos argumentos, el segundo argumento debe ser un colector
        System.out.println("\n=== Ventas por Mes ===");
        byMonth.forEach((month, count) ->
            System.out.printf("  Mes %d: %d ventas%n", month, count));
    }
}

