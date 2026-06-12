package week2;
import java.util.*; 

/*****OBJETIVO*****
 * Implementar los patrones Builder y Decorator combinados con enums.
 * 
 * ****REQUISITOS****
 * Crear enum Size con SMALL(8.99), MEDIUM(12.99), LARGE(16.99) y campo basePrice
 * Crear enum Crust con THIN(0), CLASSIC(1.50), STUFFED(3.00) y campo extraCost
 * Crear interfaz PizzaOrder con getDescription() y getPrice()
 * Crear clase Pizza con Builder interno: validar mínimo 1 topping, máximo 5. Cada topping cuesta $1.50.
 * Crear clase abstracta PizzaDecorator y dos decoradores: ExtraCheeseDecorator (+$2.50) y GiftBoxDecorator (+$3.00).
 * Demostrar pizza simple, pizza decorada y validaciones de error.
 * */

/*
 * === Pedido 1: Pizza Simple ===
Pizza MEDIUM, CLASSIC con [Pepperoni, Champiñones]
Precio: $17.49

=== Pedido 2: Pizza con Extras ===
Pizza LARGE, STUFFED con [Pepperoni, Champiñones, Aceitunas] + Extra Queso + Caja Regalo
Precio: $29.99

=== Pedido 3: Validaciones ===
Error: Minimo 1 topping
Error: Maximo 5 toppings
 * */

public enum Size {
	SMALL(8.99), MEDIUM(12.99), LARGE(16.99);
    private final double basePrice;
    Size(double basePrice) { this.basePrice = basePrice; }
    public double getBasePrice() { return basePrice; }
} 

enum Crust {
    THIN(0), CLASSIC(1.50), STUFFED(3.00);
    private final double extraCost;
    Crust(double extraCost) { this.extraCost = extraCost; }
    public double getExtraCost() { return extraCost; }
}

interface PizzaOrder {
    String getDescription();
    double getPrice();
}

class Pizza implements PizzaOrder {
    private final Size size;
    private final Crust crust;
    private final List<String> toppings;

    private Pizza(Builder builder) {
        this.size = builder.size;
        this.crust = builder.crust;
        this.toppings = List.copyOf(builder.toppings);
    }

    @Override
    public String getDescription() {
        return String.format("Pizza %s, %s con %s",
            size, crust, toppings);
    }

    @Override
    public double getPrice() {
        // TODO: size.basePrice + crust.extraCost + toppings * 1.50
        return size.getBasePrice() + crust.getExtraCost() + (toppings.size() * 1.50);  
    }

    // --- Builder ---
    static class Builder {
        private final Size size;
        private final Crust crust;
        private final List<String> toppings = new ArrayList<>();

        public Builder(Size size, Crust crust) {
            this.size = size; 
            this.crust = crust;
        }

        public Builder addTopping(String topping) {
            // TODO: validar maximo 5 toppings
            if (toppings.size() >= 5)
                throw new IllegalStateException("Maximo 5 toppings");
            toppings.add(topping);
            return this;
        }

        public Pizza build() {
            // TODO: validar minimo 1 topping
            if (toppings.isEmpty()) { //Como minímo debe de tener uno, si es lo contrario debe de arrojar que sea el mínimo. 
                throw new IllegalStateException("Minimo 1 topping"); 
            }
            return new Pizza(this);
        }
    }
}

// --- Decorator ---
abstract class PizzaDecorator implements PizzaOrder {
    protected final PizzaOrder wrapped;
    PizzaDecorator(PizzaOrder wrapped) { this.wrapped = wrapped; }
}

class ExtraCheeseDecorator extends PizzaDecorator {
    ExtraCheeseDecorator(PizzaOrder wrapped) { super(wrapped); }

    @Override
    public String getDescription() {
        return wrapped.getDescription() + " + Extra Queso";
    }

    @Override
    public double getPrice() {
        // TODO: precio original + 2.50 
        return wrapped.getPrice() + 2.50;
    }
}

class GiftBoxDecorator extends PizzaDecorator {
    GiftBoxDecorator(PizzaOrder wrapped) { super(wrapped); }

    @Override
    public String getDescription() {
        return wrapped.getDescription() + " + Caja Regalo";
    }

    @Override
    public double getPrice() {
        // TODO: precio original + 3.00
        return wrapped.getPrice() + 3.00;
    }
}

    class PizzaShop {
    public static void main(String[] args) {
        System.out.println("=== Pedido 1: Pizza Simple ===");
        PizzaOrder pizza1 = new Pizza.Builder(Size.MEDIUM, Crust.CLASSIC)
            .addTopping("Pepperoni")
            .addTopping("Champiñones")
            .build();
        System.out.println(pizza1.getDescription());
        System.out.printf("Precio: $%.2f%n", pizza1.getPrice());

        System.out.println("\n=== Pedido 2: Pizza con Extras ===");
        PizzaOrder pizza2 = new Pizza.Builder(Size.LARGE, Crust.STUFFED)
            .addTopping("Pepperoni")
            .addTopping("Champiñones")
            .addTopping("Aceitunas")
            .build();
        // TODO: decorar con ExtraCheese y GiftBox
        PizzaOrder pedido2 = new GiftBoxDecorator(
            new ExtraCheeseDecorator(pizza2));
        System.out.println(pedido2.getDescription());
        System.out.printf("Precio: $%.2f%n", pedido2.getPrice());

        System.out.println("\n=== Pedido 3: Validaciones ===");
        try {
            new Pizza.Builder(Size.SMALL, Crust.THIN).build();
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            Pizza.Builder b = new Pizza.Builder(Size.SMALL, Crust.THIN);
            for (int i = 1; i <= 6; i++) b.addTopping("Topping " + i);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
