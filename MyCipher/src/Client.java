import java.math.BigDecimal;
import java.util.Scanner;
import java.io.*;
/**
 * Created by Michael Jan on 1/29/2016.
 */
public class Client {

    /**
     * Main method for user to use IrrationalNumberCipher.
     *
     * @param  args  an absolute URL giving the base location of the image
     * @see         IrrationalNumberCipher
     */
    public static void main(String[] args) throws IOException{

        println("");
        println("+--------------------------+");
        println("| Irrational Number Cipher |");
        println("|     (by Michael Jan)     |");
        println("+--------------------------+");

        printMenu();
        menu(new IrrationalNumberCipher());

    }

    private static void menu(IrrationalNumberCipher cipher) throws IOException {

        cipher.generateFiles();

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        int selection;
        if(input.equals("m")) {
            printMenu();
            selection = sc.nextInt();
        } else {
            selection = Integer.parseInt(input);
        }

        switch(selection) {
            case 1 :
                cipher.encrypt();
                break;
            case 2 :
                cipher.decrypt();
                break;
            case 3 :
                viewFileNames(cipher);
                break;
            case 4:
                println("Which file name would you like to set?");
                println("[1] Plaintext");
                println("[2] Ciphertext");
                println("[3] Key");
                int selection2 = sc.nextInt();
                switch(selection2) {
                    case 1 :
                        setPlainTextFileName(cipher);
                        break;
                    case 2 :
                        setCipherTextFileName(cipher);
                        break;
                    case 3 :
                        setKeyFileName(cipher);
                        break;
                }
                break;
            case 5:
                println("Key is in the form of: rth root fo n");
                println("Enter r:");
                String r = sc.nextLine();
                println("Enter n:");
                String n = sc.nextLine();
                cipher.setKey(r, n);
                break;
            case 0 :
                println("Are you sure you want to exit? (Enter Y or N)");
                if(sc.nextLine().toUpperCase().equals("Y")) {
                    println("Exiting application...");
                    System.exit(0);
                }
                break;
        }

        println("");
        println("Please make a selection from the menu or type in 'm' to reprint it.");
        menu(cipher);
    }

    private static void printMenu() {
        println("");
        println("Please select an option:");
        println("[1] Encrypt from file.");
        println("[2] Decrypt from file.");
        println("[3] View file names.");
        println("[4] Set file names.");
        println("[5] Set key");
        println("[0] Exit application.");
    }

    private static void viewFileNames(IrrationalNumberCipher cipher) {
        println("Plaintext file name: " + cipher.getPlainTextFileName());
        println("Ciphertext file name: " + cipher.getCipherTextFileName());
        println("Key file name: " + cipher.getKeyFileName());
    }

    private static void setPlainTextFileName(IrrationalNumberCipher cipher) {
        Scanner sc = new Scanner(System.in);
        println("Please type in a new file name for your plaintext. Be sure to include the file extension.");
        cipher.setPlainTextFileName(sc.nextLine());
        println("Successfully changed plaintext file name to: " + cipher.getPlainTextFileName());
    }

    private static void setCipherTextFileName(IrrationalNumberCipher cipher) {
        Scanner sc = new Scanner(System.in);
        println("Please type in a new file name for your ciphertext. Be sure to include the file extension.");
        cipher.setCipherTextFileName(sc.nextLine());
        println("Successfully changed ciphertext file name to: " + cipher.getCipherTextFileName());
    }

    private static void setKeyFileName(IrrationalNumberCipher cipher) {
        Scanner sc = new Scanner(System.in);
        println("Please type in a new file name for your key. Be sure to include the file extension.");
        cipher.setKeyFileName(sc.nextLine());
        println("Successfully changed key file name to: " + cipher.getKeyFileName());
    }

    private static void println(String in) {
        System.out.println(in);
    }
}
