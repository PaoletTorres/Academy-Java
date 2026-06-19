package semana3;
import java.util.*; 

/****OBJETIVO***
 * Practicar Generics, Map, Optional y records.
 ***REQUISITOS***
 *Crear clase genérica ExpiringCache<K, V> con un TTL (time-to-live) por defecto.
 *Usar un record CacheEntry<V> privado con value y expiresAt, y método isExpired().
 *Implementar put(K, V) con TTL por defecto y put(K, V, long) con TTL personalizado.
 *get(K) retorna Optional<V> — vacío si la clave no existe o expiró. 
 *Implementar evictExpired() que elimine todas las entradas expiradas. 
 *Implementar size() que cuente solo entradas NO expiradas. 
 * */ 

public class ExpiringCache<K, V> {
    private final Map<K, CacheEntry<V>> store = new HashMap<>();
    private final long defaultTtlMillis;

    private record CacheEntry<V>(V value, long expiresAt) {
        boolean isExpired() {
            return System.currentTimeMillis() > expiresAt;
        }
    }

    public ExpiringCache(long defaultTtlMillis) {
        this.defaultTtlMillis = defaultTtlMillis;
    }

    public void put(K key, V value) {
        // TODO: almacenar con TTL por defecto
        put(key, value, this.defaultTtlMillis);
    }

    public void put(K key, V value, long ttlMillis) {
        // TODO: calcular expiresAt y crear CacheEntry
        long expiresAt = System.currentTimeMillis() + ttlMillis; //para poder obtener la hora actual
        store.put(key, new CacheEntry<>(value, expiresAt));
    }

    public Optional<V> get(K key) {
        // TODO: buscar en store, verificar que no haya expirado
        CacheEntry<V> entry = store.get(key);
        if (entry == null || entry.isExpired()) { //corroborando si la entrada ya no existe o ya expiró. 
            store.remove(key);  // limpieza lazy
            return Optional.empty();
        }
        return Optional.of(entry.value()); // si pasa el if significa que el elemento existe y sigue siendo válido 
    }

    public void evictExpired() {
        // TODO: remover todas las entradas expiradas
        // Hint: usar removeIf en entrySet
        store.entrySet().removeIf(e -> e.getValue().isExpired());
    }

    public int size() {
        evictExpired();
        return store.size();
    }

    @Override
    public String toString() {
        evictExpired();
        StringBuilder sb = new StringBuilder("Cache{");
        store.forEach((k, v) -> sb.append(k).append("=").append(v.value()).append(", "));
        if (!store.isEmpty()) sb.setLength(sb.length() - 2);
        return sb.append("}").toString();
    }

    public static void main(String[] args) throws InterruptedException {
        ExpiringCache<String, String> cache = new ExpiringCache<>(5000);

        System.out.println("=== Operaciones Basicas ===");
        cache.put("user:1", "Ana");
        cache.put("user:2", "Luis");
        cache.put("user:3", "Maria");
        System.out.println("Cache: " + cache);
        System.out.println("Size: " + cache.size());

        System.out.println("\nget user:1 = " + cache.get("user:1"));
        System.out.println("get noexiste = " + cache.get("noexiste"));

        System.out.println("\n=== TTL Personalizado (200ms) ===");
        cache.put("temp", "dato temporal", 200);
        System.out.println("Antes de esperar: get temp = " + cache.get("temp"));
        Thread.sleep(300);
        System.out.println("Despues de 300ms: get temp = " + cache.get("temp"));

        System.out.println("\n=== Expiracion Total (1s TTL) ===");
        ExpiringCache<Integer, String> cache2 = new ExpiringCache<>(500);
        cache2.put(1, "uno");
        cache2.put(2, "dos");
        cache2.put(3, "tres");
        System.out.println("Antes: size = " + cache2.size());
        Thread.sleep(600);
        cache2.evictExpired();
        System.out.println("Despues de 600ms: size = " + cache2.size());
    }
}