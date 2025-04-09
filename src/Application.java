/**
 * Bank Account Application Tester
 * @author Hellen Pacheco
 */
public class Application {

    public static void main(String[] args) {

        BankAccount account = new BankAccount(100);
        System.out.println("New Account Balance: " + account.getBalance());
        System.out.println("Withdrawing 200");
        account.withdraw(200);
        System.out.println("New Balance: " + account.getBalance());
        System.out.println("Withdrawing -100");
        account.withdraw(-100);
        System.out.println("New Balance: " + account.getBalance());
        System.out.println("Depositing -100");
        account.deposit(-100);
        System.out.println("New Balance: " + account.getBalance());
        System.out.println("Depositing 1000");
        account.deposit(1000);
        System.out.println("Withdrawing 200");
        account.withdraw(200);
        System.out.println("*************************************");
        System.out.println(account.getStatement());
    }

}
