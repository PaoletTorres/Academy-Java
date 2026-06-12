package week2; 

/*
*Salida esperada: 
*Deposito exitoso. Saldo: $1500.00
*Retiro exitoso. Saldo: $1300.00
*[LOG] Retiro de $300.00 de cuenta origen. Saldo: $1000.00
*[LOG] Deposito de $300.00 en cuenta destino. Saldo: $800.00
*[LOG] TransactionLog cerrado.
*Transferencia exitosa. Saldo cuenta1: $1000.00, cuenta2: $800.00

=== Manejo de Errores ===
*Error: Monto invalido: -100.0
*Error: Fondos insuficientes para retirar $999999.00 (deficit: $998999.00)
 */ 

/*
 * OBJETIVO: Practicar excepciones checked y unchecked, try-with-resources y multi-catch. 
 * REQUISITOS: 
 * Crear InvalidAmountException que extienda RuntimeException (unchecked).
 * Crear InsufficientBalanceException que extienda Exception (checked) con atributo deficit y getter.
 * Crear AccountLockedException que extienda Exception.
 * Crear TransactionLog que implemente AutoCloseable, con método log(String) y close()
 * Crear BankAccount con saldo inicial, métodos deposit(double), withdraw(double) y transfer(BankAccount, double)
 * Usar try-with-resources en transfer() y multi-catch en main.
 * */
//--- Excepciones ---
class InvalidAmountException extends RuntimeException {
 // TODO: constructor que reciba mensaje
 public InvalidAmountException(String message) {
     super(message); 
 }
}

class InsufficientBalanceException extends Exception {
 private final double deficit;

 // TODO: constructor con mensaje y deficit
 InsufficientBalanceException(String message, double deficit) {
     super(message);
     this.deficit = deficit;
 }

 public double getDeficit() { return deficit; }
}

class AccountLockedException extends Exception {
 // TODO: constructor que reciba mensaje 
	public AccountLockedException (String message) {
		super(message);
	}
	
	
}

//--- AutoCloseable ---
class TransactionLog implements AutoCloseable {
 private boolean open = true;

 public void log(String message) {
     if (!open) throw new IllegalStateException("Log cerrado");
     System.out.println("[LOG] " + message);
 }

 @Override
 public void close(){
     // TODO: marcar como cerrado e imprimir mensaje
     open = false;
     System.out.println("[LOG] TransactionLog cerrado.");
 }
}

//--- Cuenta Bancaria ---
public class BankAccount {
	private double balance;
    private boolean locked;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
        this.locked = false;
    }

    public void deposit(double amount) {
        // TODO: si amount <= 0 lanzar InvalidAmountException 
    	if(amount <= 0) {
    		throw new InvalidAmountException("Monto invalido: " + amount); 
    	}
        // TODO: sumar al balance 
    	this.balance += amount;
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        if (amount <= 0) throw new InvalidAmountException("Monto invalido: " + amount);
        // TODO: si amount > balance lanzar InsufficientBalanceException con deficit
        if(amount > this.balance) {
        	double deficit = amount - this.balance;
        	throw new InsufficientBalanceException("Error: Fondos insuficientes para retirar $" +amount+" (deficit: $" + deficit + ")", deficit);
        }
        // TODO: restar del balance
        this.balance -= amount; 
    }

    public void transfer(BankAccount target, double amount)
            throws InsufficientBalanceException {
        // TODO: usar try-with-resources con TransactionLog 
    	try (TransactionLog logger = new TransactionLog()){
    		//Esto es dentro de withdraw de la cuenta 
    		this.withdraw(amount);
    		logger.log("Retiro de $ " + amount + " de cuenta origen. Saldo: $" + this.getBalance());
    		
    		//DEntro: target.deposit en la cuenta destino
    		target.deposit(amount);
    		logger.log("Deposito de $" + amount+ " en cuenta destino . Saldo: $" + target.getBalance());
    	}
        //       dentro: withdraw, target.deposit, log ambas operaciones
    }

    public void lock() { this.locked = true; }
    public double getBalance() { return balance; }

    public static void main(String[] args) {
        BankAccount cuenta1 = new BankAccount(1000.00);
        BankAccount cuenta2 = new BankAccount(500.00);

        // Operaciones validas
        try {
            cuenta1.deposit(500);
            System.out.printf("Deposito exitoso. Saldo: $%.2f%n", cuenta1.getBalance());

            cuenta1.withdraw(200);
            System.out.printf("Retiro exitoso. Saldo: $%.2f%n", cuenta1.getBalance());

            cuenta1.transfer(cuenta2, 300);
            System.out.printf("Transferencia exitosa. Saldo cuenta1: $%.2f, cuenta2: $%.2f%n",
                cuenta1.getBalance(), cuenta2.getBalance());
        } catch (InsufficientBalanceException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n=== Manejo de Errores ===");

        // TODO: multi-catch para monto invalido
        try {
            cuenta1.deposit(-100); 
        } catch (InvalidAmountException e) {
            System.out.println(e.getMessage()); 
        }

        // TODO: fondos insuficientes con deficit
        try {
            cuenta1.withdraw(999999);
        } catch (InsufficientBalanceException e) {
            System.out.printf(e.getMessage()); 
        }
    }
}
