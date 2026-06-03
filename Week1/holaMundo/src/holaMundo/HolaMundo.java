package holaMundo;

public class HolaMundo {

	public static void main(String[] args) {
		// TODO: Declaración de variables 
		String name = "Karen Paolet Torres Arrieta"; 
		int age = 31; 
		double high = 1.57; 
		boolean isActive = true;   
		
		String state = "";
		if (isActive) {
			state = "Active"; 
		}else {
			state = "Disabled";
		}
		
		// TODO: Concatenación
		String message1 = "My name is: " + name + ", I am: " + age + " years, I am small: " + high + " m and I am: " + state + "."; 
		System.out.println(message1); 
		
		// TODO: Usando String.format() 
		String message2 = String.format("My name is: %s, I am: %d years, I am small: %.2f m and I am: %s.", name, age, high, state); 
		System.out.println(message2); 
		
	}

}
