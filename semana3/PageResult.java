package semana3;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

/***OBJETIVO***
 * Integrar CompletableFuture, ExecutorService, Collections y Streams en un sistema concurrente.
 ***REQUISITOS***
 * Crear record PageResult(String url, int statusCode, String title, long responseTimeMs).
 * Crear ConcurrentScraper con ExecutorService de pool fijo.
 * Método fetchPage(String) que simule latencia aleatoria (usar Thread.sleep).
 * Implementar fetchAll(List<String>) usando CompletableFuture.supplyAsync() y allOf().
 * Implementar fetchWithTimeout(List<String>, long) con completeOnTimeout() para fallback.
 * Implementar printReport() con Streams: promedio, más lenta, más rápida, agrupado por status.
 * Siempre cerrar el executor con shutdown().
 * */

record PageResult(String url, int statusCode, String title, long responseTimeMs) {
    @Override
    public String toString() {
        return String.format("[%d] %s (%dms)", statusCode, title, responseTimeMs);
    }
}

    class ConcurrentScraper {
    private final ExecutorService executor;

    public ConcurrentScraper(int threadCount) { //el constructor está recibiendo la variable threadCount
        this.executor = Executors.newFixedThreadPool(threadCount);//pool de hilos está inicializando donde estoy minitando el núm. de hilos
    }

    private PageResult fetchPage(String url) {
        long start = System.currentTimeMillis();
        try {
            // Simula latencia aleatoria entre 200ms y 1500ms
            Thread.sleep((long) (200 + Math.random() * 1300));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long elapsed = System.currentTimeMillis() - start;
        return new PageResult(url, 200, "Page: " + url, elapsed);
    }

    public List<PageResult> fetchAll(List<String> urls) { //este método es la descarga asíncrona del programa
        // TODO: crear un CompletableFuture por cada URL
        List<CompletableFuture<PageResult>> futures = urls.stream()
            .map(url -> CompletableFuture.supplyAsync( //se está tomando cada url de String del stream, -> está llamando al método que inicia la tarea en segundo plano. 
                () -> fetchPage(url), executor)) //la orden para que ejecute el pool de hilos creado arriba 
            .collect(Collectors.toList());

        // TODO: esperar a que todos terminen con allOf
        CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])).join();

        // TODO: recolectar resultados
        return futures.stream()
            .map(CompletableFuture :: join)//Para poder extraer los resultados de cada hilo, una vez que tdos terminaron
            .collect(Collectors.toList());
    }

    public List<PageResult> fetchWithTimeout(List<String> urls, long timeoutMs) {
        List<CompletableFuture<PageResult>> futures = urls.stream() //Se empieza cada tarea con el limite de tiempo asignado
            .map(url -> CompletableFuture.supplyAsync(
                    () -> fetchPage(url), executor)
                // TODO: agregar timeout con fallback
                .completeOnTimeout(
                    new PageResult(url, 408, "TIMEOUT: " + url, timeoutMs),
                    timeoutMs, TimeUnit.MILLISECONDS))
            .collect(Collectors.toList());

        CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])).join();

        return futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());
    }

    public void printReport(List<PageResult> results) {
        System.out.println("--- Resultados ---");
        results.forEach(System.out::println);

        // Promedio de tiempo de respuesta
        double avg = results.stream()
            .mapToLong(PageResult :: responseTimeMs)//Para poder calcular el promedio se necesira cambambiar cada objeto de pageREsult en su valor numérico de tiempo con responseTimeMs 
            .average().orElse(0);
        System.out.printf("\nTiempo promedio: %.0fms%n", avg);

        // Pagina mas rapida
        results.stream()
            .min(Comparator.comparingLong(PageResult::responseTimeMs))
            .ifPresent(p -> System.out.println("Mas rapida: " + p));

        // Pagina mas lenta
        results.stream()
            .max(Comparator.comparingLong(PageResult::responseTimeMs))
            .ifPresent(p -> System.out.println("Mas lenta: " + p));

        // Agrupar por status code
        Map<Integer, Long> byStatus = results.stream()
            .collect(Collectors.groupingBy(
                PageResult::statusCode, Collectors.counting()));//Para poder agrupar cada estado con statusCode 
        System.out.println("Por status: " + byStatus);
    }

    public void shutdown() {
        executor.shutdown();
    }

    public static void main(String[] args) {
        ConcurrentScraper scraper = new ConcurrentScraper(4);

        List<String> urls = List.of(
            "example.com", "google.com", "github.com", "stackoverflow.com"
        );

        System.out.println("=== Fetch All (paralelo) ===");
        long start = System.currentTimeMillis();
        List<PageResult> results = scraper.fetchAll(urls);
        long elapsed = System.currentTimeMillis() - start;
        scraper.printReport(results);
        System.out.printf("Tiempo total (paralelo): %dms%n", elapsed);

        System.out.println("\n=== Fetch con Timeout (2000s) ===");
        List<PageResult> resultsTimeout = scraper.fetchWithTimeout(urls, 2000);//cambie está linea porque no les está dando tiempo de responder por el timeout
        scraper.printReport(resultsTimeout);

        scraper.shutdown();
    }
}

