package semana3;
import java.util.*;
import java.util.stream.*;

/****OBJETIVO***
 * Practicar el Collections Framework, Comparable, Comparator y Optional.
 * 
 * ****REQUISITOS****
 * Crear clase Contact con campos name, email, phone
 * Implementar Comparable<Contact> con orden natural por name (alfabético).
 * Sobrescribir equals() y hashCode() basados en email (un email = un contacto único).
 * Crear ContactManager que use TreeSet internamente.
 * Implementar addContact(), findByEmail() (retorna Optional), findByNamePrefix() y getAllSortedBy(Comparator).
 * Demostrar que duplicados por email son rechazados y ordenamiento por distintos criterios.
 * */


public class Contact implements Comparable<Contact> {
    private String name;
    private String email;
    private String phone;

    public Contact(String name, String email, String phone) {
        this.name = name; this.email = email; this.phone = phone;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    @Override
    public int compareTo(Contact other) { //me va a regresar números 
        // TODO: orden natural por name (alfabetico)
        return this.name.compareTo(other.getName());// el compareTo devuelve los valor de 1 o -1 si el nombre antes o después. 
    }

    @Override
    public boolean equals(Object o) {
        // TODO: igualdad basada en email
        if (this == o) return true;
        if (!(o instanceof Contact c)) return false;
        return this.email.equals(c.getEmail());// con equals estoy comparando los dos correos, iguales true, doferentes false. 
    }

    @Override
    public int hashCode() {
        // TODO: hash basado en email
        return Objects.hash(email);//.hash es un método estático derivado de la clase java.util.Objects
    }

    @Override
    public String toString() {
        return String.format("Contact{name='%s', email='%s', phone='%s'}",
            name, email, phone);
    }
}

class ContactManager {
    private final Set<Contact> contacts = new TreeSet<>(); //Aquí se gestiona la colección de contactos con el TreeSet llamado contacts

    public boolean addContact(Contact contact) {
        // TODO: agregar al set, retornar true si se agrego
        return contacts.add(contact);
    }

    public Optional<Contact> findByEmail(String email) {
        // TODO: buscar contacto por email usando stream + filter
        return contacts.stream()
            .filter(c -> c.getEmail().equals(email)) //usando lambda para recorrer uno por uno y comparar su valor con equals. 
            .findFirst();
    }

    public List<Contact> findByNamePrefix(String prefix) {
        // TODO: filtrar contactos cuyo nombre empiece con prefix (case-insensitive)
        return contacts.stream()
            .filter(c -> c.getName().toLowerCase().startsWith(prefix.toLowerCase()))
            .collect(Collectors.toList());
    }

    public List<Contact> getAllSortedBy(Comparator<Contact> comp) {
        // TODO: retornar todos los contactos ordenados por el comparator dado
        return contacts.stream()
            .sorted(comp)
            .collect(Collectors.toList());
    }

    public int size() { return contacts.size(); }

    public static void main(String[] args) {
        ContactManager mgr = new ContactManager();

        System.out.println("=== Agregando Contactos ===");
        System.out.println("Ana: " + mgr.addContact(
            new Contact("Ana Garcia", "ana@mail.com", "555-1111")));
        System.out.println("Luis: " + mgr.addContact(
            new Contact("Luis Lopez", "luis@mail.com", "555-2222")));
        System.out.println("Maria: " + mgr.addContact(
            new Contact("Maria Torres", "maria@mail.com", "555-3333")));
        System.out.println("Ana duplicada: " + mgr.addContact(
            new Contact("Ana Garcia", "ana@mail.com", "555-9999")));
        System.out.println("Carlos: " + mgr.addContact(
            new Contact("Carlos Ruiz", "carlos@mail.com", "555-4444")));
        System.out.println("Total contactos: " + mgr.size());

        System.out.println("\n=== Orden Natural (por nombre) ===");
        mgr.getAllSortedBy(Comparator.naturalOrder())
            .forEach(System.out::println);

        System.out.println("\n=== Ordenados por Email ===");
        mgr.getAllSortedBy(Comparator.comparing(Contact::getEmail))
            .forEach(System.out::println);

        System.out.println("\n=== Buscar por Email ===");
        mgr.findByEmail("maria@mail.com")
            .ifPresentOrElse(
                c -> System.out.println("Encontrado: " + c),
                () -> System.out.println("No encontrado"));
        mgr.findByEmail("noexiste@mail.com")
            .ifPresentOrElse(
                c -> System.out.println("Encontrado: " + c),
                () -> System.out.println("No encontrado"));

        System.out.println("\n=== Buscar por Prefijo 'Ma' ===");
        mgr.findByNamePrefix("Ma").forEach(System.out::println);
    }
}