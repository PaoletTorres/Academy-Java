package calculadoraBasica;

public class Calculadora {
	//TODO: sumar dos enteros 
	public static int sumar(int a, int b) {
		return a + b; 
	}
	//TODO: sumar dos dobles
	public static double sumar(double a, double b) {
		return a + b;
	}
	//TODO: sumar tres enteros 
	public static int sumar(int a, int b, int c) {
		return a + b + c; 
	}	
	//TODO: sumar un array de enteros 
	public static int sumar (int[] numeros) {
		int total = 0; 
		//TODO: recorrer el array y acumular 
		for(int nums : numeros) {
			total += nums;
		}
		return total; 
		
	}
 
public static void main(String[] args) {
	System.out.println("2 + 3 = "+ sumar(2, 3));
	System.out.println("2.5 + 3.7 = " + sumar(2.5, 3.7));
	System.out.println("1 + 2 + 3 = " + sumar(1, 2, 3)); 
	
	int[] nums = {10, 20, 30, 40}; 
	System.out.println("Array suma= " + sumar(nums)); 
  }
}
