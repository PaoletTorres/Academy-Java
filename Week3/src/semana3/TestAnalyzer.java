package semana3;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.*; 

/***OBJETIVO***
 * Practicar flatMap, Collectors.groupingBy, counting y operaciones de Streams encadenadas.
 ****REQUISITOS*** 
 * Crear clase TextAnalyzer que reciba List<String> (líneas de texto).
 * Implementar wordCount(): total de palabras usando flatMap + count().
 * Implementar uniqueWords(): palabras únicas en minúsculas, solo alfabéticas.
 * Implementar topN(int n): las N palabras más frecuentes con groupingBy + counting.
 * Implementar averageWordLength(): longitud promedio con mapToInt + average.
 * Implementar wordsByFirstLetter(): agrupar palabras por su primera letra.
 * */

    class TextAnalyzer {
    private final List<String> lines; //¿Por qué final? 

    public TextAnalyzer(List<String> lines) {
        this.lines = lines;
    }

    private Stream<String> words() {
        // TODO: flatMap cada linea en sus palabras (split por espacios)
        return lines.stream() //convierte la lista de frases en líneas en un flujo
            .flatMap(line -> Arrays.stream(line.split("\\s+")))
            .filter(w -> !w.isEmpty());
    }

    private Stream<String> cleanWords() {
        // TODO: palabras en minusculas, solo caracteres alfabeticos
        return words()
            .map(w -> w.replaceAll("[^a-zA-Z]", ""))//eliimna todo lo que NO sea una letra 
            .filter(w -> !w.isEmpty())
            .map(String::toLowerCase);
    }

    public long wordCount() {
        // TODO: contar total de palabras
        return words().count();
    }

    public Set<String> uniqueWords() {
        // TODO: palabras unicas (limpiadas)
        return cleanWords().collect(Collectors.toSet()); //elimina cualquier palabra repetida del Stream
    }

    public List<Map.Entry<String, Long>> topN(int n) {
        // TODO: agrupar por palabra, contar, ordenar desc, limitar a n
        return cleanWords()
            .collect(groupingBy(w -> w, Collectors.counting())) //aquí se usa la identidad de la función, para agrupar la palabra misma. 
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(n)
            .collect(toList());
    }

    public double averageWordLength() {
        // TODO: longitud promedio de palabras limpias
        return cleanWords()
            .mapToInt(w -> w.length())//con lambda estoy pasandole la función que extrae el tamaño de la palabra 
            .average()
            .orElse(0.0);
    }

    public Map<Character, List<String>> wordsByFirstLetter() { //bloque para agrupar las palabaras únicas según con la primera letra
        // TODO: agrupar palabras unicas por primera letra
        return cleanWords()
            .distinct()
            .collect(Collectors.groupingBy(w -> w.charAt(0)));//utilizando el método .charAt(0) para obtener la primera letra del String
    }

    public static void main(String[] args) {
        List<String> text = List.of(
            "Java is a powerful programming language",
            "Java streams make data processing elegant",
            "Lambdas and streams are the heart of modern Java"
        );

        TextAnalyzer analyzer = new TextAnalyzer(text);

        System.out.println("=== Estadisticas de Texto ===");
        System.out.println("Total palabras: " + analyzer.wordCount());
        System.out.println("Palabras unicas: " + analyzer.uniqueWords().size());
        System.out.printf("Longitud promedio: %.2f%n", analyzer.averageWordLength());

        System.out.println("\n=== Top 5 Palabras ===");
        analyzer.topN(5).forEach(e ->
            System.out.printf("  '%s': %d veces%n", e.getKey(), e.getValue()));

        System.out.println("\n=== Palabras por Letra ===");
        analyzer.wordsByFirstLetter().entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(e -> System.out.printf("  %c: %s%n", e.getKey(), e.getValue()));
    }
}

