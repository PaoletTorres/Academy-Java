package manipuladorStrings; 
//Invertir 'Hola Mundo': odnuM aloH
//'Anita lava la tina' es palindromo: true
//Vocales en 'Murcielago': 5
//Piramide de 5 niveles

public class Manipulador {
	public static String invertir(String s) {
        // TODO: usar StringBuilder.reverse() 
        return new StringBuilder(s).reverse().toString();
    }

    public static boolean esPalindromo(String s) {
        // TODO: limpiar (toLowerCase, replaceAll espacios) 
    	String limpia = s.toLowerCase().replaceAll(" ", "");
        // TODO: comparar con su version invertida
        return limpia.equals(invertir(limpia));
    }

    public static int contarVocales(String s) {
        int count = 0;
        String vocales = "aeiouAEIOU";
        // TODO: recorrer cada caracter, verificar si es vocal
        for(int i=0; i<s.length(); i++) {
        	if (vocales.indexOf(s.charAt(i)) != -1) {
        		count ++; 
        	}
        }
        return count;
    }

    public static String construirPiramide(int niveles) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= niveles; i++) {
            // TODO: agregar espacios (niveles - i) 
        	for(int o = 0; o<(niveles -i); o++)
        	sb.append(" "); 
            // TODO: agregar asteriscos (2*i - 1)
        	for(int u = 0; u < (2*i -1); u++) 
        	sb.append("*"); 
            // TODO: agregar salto de linea
        	sb.append("\n"); 
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Invertir 'Hola Mundo': "
                         + invertir("Hola Mundo"));
        System.out.println("'Anita lava la tina' es palindromo: "
                         + esPalindromo("Anita lava la tina"));
        System.out.println("Vocales en 'Murcielago': "
                         + contarVocales("Murcielago"));
        System.out.println("Piramide de 5 niveles:");
        System.out.println(construirPiramide(5));
    }
}

