package week2;  
import java.util.ArrayList;
import java.util.List;
import java.util.EnumMap; 
import java.util.EnumSet; 

/* ******OBJETIVO******
 * Dominar enums con campos, constructores, métodos y usar EnumSet / EnumMap
 * 
 * ******REQUISITOS*******
 * Crear enum Priority con valores LOW(1,48), MEDIUM(2,24), HIGH(3,8), CRITICAL(4,1) — campos: level, responseTimeHours
 * Método getLabel() que retorne ej: "CRITICAL (Nivel 4, Respuesta: 1h)"
 * Crear enum TicketStatus con OPEN, IN_PROGRESS, RESOLVED, CLOSED
 * Método canTransitionTo(TicketStatus) que valide transiciones permitidas.
 * Crear clase Ticket con id, description, priority, status y método transitionTo()
 * En main: usar EnumMap para contar tickets por status y EnumSet para filtrar urgentes.
 * */ 

/******SALIDA ESPERADA*******
 * === Todos los Tickets ===

Ticket{id=1, desc='Login falla', priority=CRITICAL (Nivel 4, Respuesta: 1h), status=OPEN}
Ticket{id=2, desc='Boton desalineado', priority=LOW (Nivel 1, Respuesta: 48h), status=OPEN}
Ticket{id=3, desc='Error en pagos', priority=HIGH (Nivel 3, Respuesta: 8h), status=OPEN}
Ticket{id=4, desc='Mejorar docs', priority=MEDIUM (Nivel 2, Respuesta: 24h), status=OPEN}

=== Transiciones ===
Ticket 1: OPEN -> IN_PROGRESS
Ticket 3: OPEN -> IN_PROGRESS
Ticket 3: IN_PROGRESS -> RESOLVED
Error: No se puede transicionar de RESOLVED a OPEN

=== Estado Actualizado ===
Ticket{id=1, desc='Login falla', priority=CRITICAL (Nivel 4, Respuesta: 1h), status=IN_PROGRESS}
Ticket{id=2, desc='Boton desalineado', priority=LOW (Nivel 1, Respuesta: 48h), status=OPEN}
Ticket{id=3, desc='Error en pagos', priority=HIGH (Nivel 3, Respuesta: 8h), status=RESOLVED}
Ticket{id=4, desc='Mejorar docs', priority=MEDIUM (Nivel 2, Respuesta: 24h), status=OPEN}

=== Dashboard (EnumMap) ===
  OPEN: 2
  IN_PROGRESS: 1
  RESOLVED: 1
  CLOSED: 0

=== Tickets Urgentes (EnumSet) ===
Ticket{id=1, desc='Login falla', priority=CRITICAL (Nivel 4, Respuesta: 1h), status=IN_PROGRESS}
Ticket{id=3, desc='Error en pagos', priority=HIGH (Nivel 3, Respuesta: 8h), status=RESOLVED}
  * */

public enum Priority {
	LOW(1, 48),
    MEDIUM(2, 24),
    HIGH(3, 8),
    CRITICAL(4, 1);

    private final int level;
    private final int responseTimeHours;

    // TODO: constructor. Aquí voy a guardar las constantes del enum
    Priority(int level, int responseTimeHours) {
        this.level = level;
        this.responseTimeHours = responseTimeHours;
    }

    public int getLevel() { return level; }
    public int getResponseTimeHours() { return responseTimeHours; }

    public String getLabel() {
        // TODO: retornar "NOMBRE (Nivel X, Respuesta: Yh)"
        return String.format("%s (Nivel %d, Respuesta: %dh)",
            this.name(), this.level, this.responseTimeHours);
    }
}

    enum TicketStatus {
    OPEN, IN_PROGRESS, RESOLVED, CLOSED;

    public boolean canTransitionTo(TicketStatus target) {
        // TODO: definir transiciones validas 
        // OPEN -> IN_PROGRESS
        // IN_PROGRESS -> RESOLVED o OPEN (reabrir)
        // RESOLVED -> CLOSED o IN_PROGRESS (reabrir)
        // CLOSED -> ninguno
        return switch (this) {
            case OPEN -> target == IN_PROGRESS;
            case IN_PROGRESS -> target == RESOLVED || target == OPEN;
            case RESOLVED -> target == OPEN || target == CLOSED;
            case CLOSED -> false;
        };
    }
}

class Ticket {
    private final int id;
    private final String description;
    private final Priority priority;
    private TicketStatus status;

    public Ticket(int id, String description, Priority priority) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.status = TicketStatus.OPEN;
    }

    public void transitionTo(TicketStatus newStatus) {
        // TODO: validar con canTransitionTo, cambiar o imprimir error
    	if(this.status.canTransitionTo(newStatus)) {
    		this.status = newStatus; 
    	}else {
    		System.out.printf("Error: No se puede transicionar de %s a %s%n", this.status, newStatus); //Error: No se puede transicionar de RESOLVED a OPEN
    	}
    }

    // TODO: getters
    public int getId() { return id; }
    public Priority getPriority() { return priority; }
    public TicketStatus getStatus() { return status; }

    @Override
    public String toString() {
        return String.format("Ticket{id=%d, desc='%s', priority=%s, status=%s}",
            id, description, priority.getLabel(), status);
    }
}

    class TicketSystem { //aquí elimine el public para poder correrlo en un solo programa, sé que no es una buena practica. 
    public static void main(String[] args) {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket(1, "Login falla", Priority.CRITICAL));
        tickets.add(new Ticket(2, "Boton desalineado", Priority.LOW));
        tickets.add(new Ticket(3, "Error en pagos", Priority.HIGH));
        tickets.add(new Ticket(4, "Mejorar docs", Priority.MEDIUM));

        System.out.println("=== Todos los Tickets ===");
        tickets.forEach(System.out::println);

        // Transiciones
        System.out.println("\n=== Transiciones ===");
        tickets.get(0).transitionTo(TicketStatus.IN_PROGRESS);
        tickets.get(2).transitionTo(TicketStatus.IN_PROGRESS);
        tickets.get(2).transitionTo(TicketStatus.RESOLVED);

        // Transicion invalida
        tickets.get(2).transitionTo(TicketStatus.OPEN);

        System.out.println("\n=== Estado Actualizado ===");
        tickets.forEach(System.out::println);

        // TODO: EnumMap para contar tickets por status
        System.out.println("\n=== Dashboard (EnumMap) ===");
        EnumMap<TicketStatus, Integer> conteo = new EnumMap<>(TicketStatus.class);
        for (TicketStatus s : TicketStatus.values()) conteo.put(s, 0);
        // TODO: contar tickets por status
        for (Ticket t : tickets) {
        	TicketStatus estadoActual = t.getStatus();
        	conteo.put(estadoActual, conteo.get(estadoActual) + 1); 
        }
        
        conteo.forEach((status, count) ->
            System.out.printf("  %s: %d%n", status, count));

        // TODO: EnumSet para filtrar tickets urgentes (HIGH + CRITICAL)
        System.out.println("\n=== Tickets Urgentes (EnumSet) ===");
        EnumSet<Priority> urgentes = EnumSet.of(Priority.HIGH, Priority.CRITICAL);
        // TODO: filtrar e imprimir tickets con prioridad urgente 
        for(Ticket t : tickets) {
        	if(urgentes.contains(t.getPriority())) {
        		System.out.println(t);
        	}
        }
    }
}
