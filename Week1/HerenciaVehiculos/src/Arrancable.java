import java.util.ArrayList; 

//Interfaz Arrancable con arrancar() y detener().
//Clase abstracta Vehiculo implements Arrancable: atributos marca, modelo,anio; método abstracto tipoVehiculo();método concreto info().
//Clase Auto extends Vehiculo: atributo extra numPuertas.
//Clase Moto extends Vehiculo: atributo extra cilindrada.
//Clase VehiculoElectrico extends Auto: atributo autonomiaKm, sobrescribe arrancar().
//En main: ArrayList<Vehiculo> con polimorfismo, iterar, contar por tipo<

public interface Arrancable {
	void arrancar();
    void detener();
}
abstract class Vehiculo implements Arrancable {
    protected String marca;
    protected String modelo;
    protected int anio;

    public Vehiculo(String marca, String modelo, int anio) {
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
    }

    public abstract String tipoVehiculo(); //método abstracto 

    public String info() {
        return tipoVehiculo() + ": " + marca + " "
             + modelo + " (" + anio + ")";
    }
}

class Auto extends Vehiculo {
    private int numPuertas;

    public Auto(String marca, String modelo, int anio, int numPuertas) {
        super(marca, modelo, numPuertas);
        this.numPuertas = numPuertas;
    }

    @Override
    public String tipoVehiculo() { return "Auto"; }

    @Override
    public void arrancar() {
        System.out.println("  El auto " + marca + " "
                         + modelo + " arranca el motor...");
    }

    @Override
    public void detener() {
        System.out.println("  El auto " + marca + " "
                         + modelo + " se detiene.");
    }

    @Override
    public String toString() {
        return info() + " - " + numPuertas + " puertas";
    }
}

class Moto extends Vehiculo {
    private int cilindrada;

    public Moto(String marca, String modelo, int anio, int cilindrada) {
        super(marca, modelo, anio);
        this.cilindrada = cilindrada;
    }

    @Override
    public String tipoVehiculo() { return "Moto"; }

    @Override
    public void arrancar() {
        System.out.println("  La moto " + marca + " " + modelo
                         + " ruge con " + cilindrada + "cc...");
    }

    @Override
    public void detener() {
        System.out.println("  La moto " + marca + " "
                         + modelo + " se detiene.");
    }

    @Override
    public String toString() {
        return info() + " - " + cilindrada + "cc";
    }
}

class VehiculoElectrico extends Auto {
    private int autonomiaKm;

    public VehiculoElectrico(String marca, String modelo,
                             int anio, int numPuertas,
                             int autonomiaKm) {
        super(marca, modelo, anio, 2);
        this.autonomiaKm = autonomiaKm;
    }

    @Override
    public String tipoVehiculo() { return "Vehiculo Electrico"; }

    @Override
    public void arrancar() {
        System.out.println("  El vehiculo electrico " + marca
            + " " + modelo + " arranca silenciosamente... "
            + "(autonomia: " + autonomiaKm + " km)");
    }

    @Override
    public String toString() {
        return info() + " - autonomia " + autonomiaKm + " km";
    }
}

class HerenciaVehiculos {
    public static void main(String[] args) {
        ArrayList<Vehiculo> flota = new ArrayList<>();
        flota.add(new Auto("Toyota", "Corolla", 2024, 4));
        flota.add(new Moto("Honda", "CBR600", 2023, 600));
        flota.add(new VehiculoElectrico("Tesla", "Model 3",
                                        2025, 4, 580));
        flota.add(new Auto("Ford", "Mustang", 2024, 2));
        flota.add(new Moto("Yamaha", "MT-07", 2024, 689));

        System.out.println("=== Flota de Vehiculos ===");
        for (Vehiculo v : flota) {
            System.out.println(v);
            v.arrancar();
            v.detener();
            System.out.println();
        }

        // TODO: Contar por tipo usando instanceof
        int autos = 0, motos = 0, electricos = 0;
        for (Vehiculo v : flota) {
            if (v instanceof VehiculoElectrico) electricos++;
            else if (v instanceof Auto) autos++;
            else if (v instanceof Moto) motos++;
        }
        System.out.println("=== Resumen ===");
        System.out.println("Autos: " + autos);
        System.out.println("Motos: " + motos);
        System.out.println("Electricos: " + electricos);
    }
}
