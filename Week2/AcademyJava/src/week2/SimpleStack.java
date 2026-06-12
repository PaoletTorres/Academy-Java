package week2; 

import java.util.Iterator;
import java.util.NoSuchElementException;

/*===OBJETIVO===
 * Practicar clases anidadas (static nested e inner class) implementando un stack con iterador
 * 
 * ====REQUISITOS====
 *Crear clase genérica SimpleStack<T> que implemente Iterable<T>.
 *Crear static nested class Node<T> con campos data y next.
 *Implementar push(T), pop(), peek(), isEmpty() y size().
 *Crear inner class StackIterator que implemente Iterator<T> (recorrido LIFO).
 *pop() en stack vacío debe lanzar NoSuchElementException.
 *Demostrar uso con for-each (posible gracias a Iterable).
 * */ 

/*====SALIDA ESPERADA==== 
 * 
 *=== Stack de Enteros ===
 *Después de push 10, 20, 30: Stack[30 -> 20 -> 10]
 *Peek: 30
 *Size: 3
 *Iterando (LIFO): 30 20 10
 *Pop: 30
 *Pop: 20
 *Peek después de pops: 10
 *Size: 1

=== Stack de Strings ===
For-each: Java Mundo Hola

=== Error: pop en stack vacio ===
Error: Stack vacio
 * 
 * */

public class SimpleStack<T> implements Iterable<T> {
	
	// Static nested class: no necesita acceso a la instancia
    private static class Node<T> {
        private T data; //encapsulamiento
        private Node<T> next;

        Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        } 
      //Método que agregué para que el iterador pueda ver y avanzar los nodos
        public T getData() {return data;}
        public Node<T> getNext(){return next;}
    }

    private Node<T> top;
    private int size;

    public void push(T item) {
        // TODO: crear nuevo nodo que apunte al top actual
    	Node<T> nuevoNodo = new Node<>(item, top);
    	// TODO: actualizar top y size 
    	top = nuevoNodo; 
    	size ++; 
    }

    public T pop() {
        // TODO: si esta vacio lanzar NoSuchElementException
    	if(isEmpty()) throw new NoSuchElementException("Stack vacio");
        // TODO: guardar dato del top, avanzar top, decrementar size
        T dato = top.data; 
    	top = top.next; 
    	size --; 
    	return dato; //tiene que retornar el dato y no el método 
    }

    public T peek() {
        if (isEmpty()) throw new NoSuchElementException("Stack vacio");
        return top.data;
    }

    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Inner class: necesita acceso al top del stack externo (ESTA ES MI CLASE INERNA)
    private class StackIterator implements Iterator<T> {  // EL ITERADOR PUEDE VER EL NODO INICIAL PRIVADO DE LA PILA Y SALTA DE NEXT EN NEXT SIN ROMPER EL ENCAPSULAMIENTO
        private Node<T> current = top;  // TODO: iniciar en top

        @Override
        public boolean hasNext() {
            return current != null; //cambie el retorno al mismo método y sólo si es diferente de null current significaria que hay un nodo por leer. 
        }

        @Override
        public T next() { //método next()
            if (!hasNext()) throw new NoSuchElementException();
            // TODO: guardar dato, avanzar current, retornar dato       
            T data = current.getData();
            current = current.getNext();
            return data;
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<T> iterator() { //método iterator 
        return new StackIterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Stack[");
        Node<T> current = top;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) sb.append(" -> ");
            current = current.next;
        }
        return sb.append("]").toString();
    }

    public static void main(String[] args) {
        SimpleStack<Integer> stack = new SimpleStack<>();

        System.out.println("=== Stack de Enteros ===");
        stack.push(10);
        stack.push(20);
        stack.push(30);
        System.out.println("Despues de push 10, 20, 30: " + stack);
        System.out.println("Peek: " + stack.peek());
        System.out.println("Size: " + stack.size());

        System.out.print("Iterando (LIFO): ");
        for (Integer n : stack) {
            System.out.print(n + " ");
        }
        System.out.println();

        System.out.println("Pop: " + stack.pop());
        System.out.println("Pop: " + stack.pop());
        System.out.println("Peek despues de pops: " + stack.peek());
        System.out.println("Size: " + stack.size());

        System.out.println("\n=== Stack de Strings ===");
        SimpleStack<String> palabras = new SimpleStack<>();
        palabras.push("Hola");
        palabras.push("Mundo");
        palabras.push("Java");

        System.out.print("For-each: ");
        for (String s : palabras) {
            System.out.print(s + " ");
        }
        System.out.println();

        System.out.println("\n=== Error: pop en stack vacio ===");
        SimpleStack<Integer> vacio = new SimpleStack<>();
        try {
            vacio.pop();
        } catch (NoSuchElementException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
