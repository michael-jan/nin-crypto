import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class IrrationalNumberCipher {

    private String ptFileName;
    private String ctFileName;
    private String keyFileName;
    private static final double CONVERSION_SCALE_FACTOR = 0.30102999567; // log base 10 of 2
    private static final String keyFormat = "Format for nth root of r:\nn\nr";

    /**
     * Default constructor for IrrationalNumberCipher. Automatically sets
     * plaintext file name to "plaintext.txt", ciphertext file name to "ciphertext.txt",
     * and key file name to "key.txt".
     */
    public IrrationalNumberCipher() {
        this.ptFileName = "plaintext.txt";
        this.ctFileName = "ciphertext.txt";
        this.keyFileName = "key.txt";
    }

    /**
     * Main method for user to use IrrationalNumberCipher.
     *
     * @param plainTextFileName  the file name of the plaintext, including file extension.
     * @param cipherTextFileName the file name of the ciphertext, including file extension.
     * @param keyFileName        the file name of the key, including file extension.
     */
    public IrrationalNumberCipher(String plainTextFileName, String cipherTextFileName, String keyFileName) {
        this.ptFileName = plainTextFileName;
        this.ctFileName = cipherTextFileName;
        this.keyFileName = keyFileName;
    }

    /**
     * Uses the key from its file to generate a keystream, XORs it with the text in the
     * plaintext's file to encrypt it, and prints it the the ciphertext's file.
     */
    public void encrypt() throws IOException {
        BufferedReader f = new BufferedReader(new FileReader(ptFileName));
        int keyLength = (int) (getTotalChars(ptFileName) * 8 * CONVERSION_SCALE_FACTOR);
        String key = convertKey(keyFileName, keyLength);
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(ctFileName)));

        int i = 0;
        String line = f.readLine();

        while (line != null) {
            out.println((xor(toBinary(line), key.substring(i, i + line.length() * 8))));
            i += line.length() * 8;
            line = f.readLine();
        }

        out.close();
        System.out.println("Successfully encrypted text from [" + ptFileName + "] into binary in [" + ctFileName + "] using key in [" + keyFileName + "].");
    }

    /**
     * Uses the key from its file to generate a keystream, XORs it with the text in the
     * ciphertext's file to decrypt it, and prints it the the plaintext's file.
     */
    public void decrypt() throws IOException {
        BufferedReader f = new BufferedReader(new FileReader(ctFileName));
        int keyLength = (int) (getTotalChars(ctFileName) * CONVERSION_SCALE_FACTOR);
        String key = convertKey(keyFileName, keyLength);
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(ptFileName)));

        int i = 0;
        String line = f.readLine();

        while (line != null) {
            out.println(toText(xor(line, key.substring(i))));
            i += line.length();
            line = f.readLine();
        }
        out.close();
        System.out.println("Successfully decrypted binary in [" + ctFileName + "] into text in [" + ptFileName + "] using key in [" + keyFileName + "].");
    }

    /**
     * Uses the key from its file to generate a keystream, XORs it with the text in the
     * plaintext's file, and prints it the the ciphertext's file.
     *
     * @param r the decimal number outside of the radical symbol (the index)
     * @param n the integer inside the radical symbol (the radicand)
     */
    public void setKey(String r, String n) throws IOException {
        PrintWriter pw = new PrintWriter(new File(keyFileName));
        pw.println(r);
        pw.println(n);
        pw.println();
        pw.println(keyFormat);
        pw.close();
        System.out.println("Successfully set key to " + r + "th root of " + n);
    }

    /**
     * Generates the plaintext, ciphertext, and key files, into default directory,
     * unless they are already present.
     */
    public void generateFiles() throws IOException {
        new File(ptFileName).createNewFile();
        new File(ctFileName).createNewFile();
        new File(keyFileName).createNewFile();
    }

    private int getTotalChars(String fileName) throws IOException {
        BufferedReader f = new BufferedReader(new FileReader(fileName));
        String line = f.readLine();
        int counter = 0;
        while (line != null) {
            counter += line.length();
            line = f.readLine();
        }
        return counter;
    }

    private String convertKey(String fileName, int length) throws IOException {
        BufferedReader f = new BufferedReader(new FileReader(fileName));
        String root = takeRoot(Integer.parseInt(f.readLine()),
                new BigDecimal(f.readLine()),
                length
        ).toString();
        return to8Digits(new BigInteger(removeFirstPoint(root)).toString(2));
    }

    private String xor(String s1, String s2) {
        StringBuilder out = new StringBuilder("");
        for (int i = 0; i < s1.length() && i < s2.length(); i++) {
            if (s1.charAt(i) == s2.charAt(i))
                out.append(0);
            else
                out.append(1);
        }
        return out.toString();
    }

    private String removeSpaces(String in) {
        StringBuilder out = new StringBuilder(in);
        for (int i = 0; i < out.length(); i++) {
            if (out.charAt(i) == ' ') {
                out.deleteCharAt(i);
                i--;
            }
        }
        return out.toString();
    }

    private String removeFirstPoint(String in) {
        if (!in.contains(".")) {
            return in;
        }
        return in.substring(0, in.indexOf(".")) + in.substring(in.indexOf(".") + 1);
    }

    private String toText(String bin) {
        StringBuilder out = new StringBuilder("");
        bin = removeSpaces(bin);
        for (int i = 0; i < bin.length(); i += 8) {
            out.append((char) ((int) Integer.valueOf(bin.substring(i, i + 8), 2)));
        }
        return out.toString();
    }

    private String toBinary(String in) {
        StringBuilder out = new StringBuilder("");
        for (int i = 0; i < in.length(); i++) {
            out.append(toBinary(in.charAt(i)));
        }
        return out.toString();
    }

    private String toBinary(char in) {
        return to8Digits(Integer.toBinaryString(in));
    }

    private String to8Digits(String numStr) {
        switch (numStr.length() % 8) {
            case 0:
                return numStr;
            case 7:
                return "0" + numStr;
            case 6:
                return "00" + numStr;
            case 5:
                return "000" + numStr;
            case 4:
                return "0000" + numStr;
            case 3:
                return "00000" + numStr;
            case 2:
                return "000000" + numStr;
            case 1:
                return "0000000" + numStr;
        }
        return numStr;
    }

    private BigDecimal takeRoot(int root, BigDecimal n, int numDigits) {
        BigDecimal maxError = new BigDecimal("1").scaleByPowerOfTen(-numDigits);
        final int MAXITER = Integer.MAX_VALUE;
        MathContext mc = new MathContext(numDigits);
        BigDecimal x;
        x = new BigDecimal(Math.pow(n.doubleValue(), 1.0 / root), mc);
        BigDecimal prevX = null;
        BigDecimal rootBD = new BigDecimal(root, mc);

        System.out.print("Working... [");
        for (int i = 0; i < MAXITER; ++i) {
            System.out.print("=");
            x = x.subtract(x.pow(root, mc)
                    .subtract(n, mc)
                    .divide(rootBD.multiply(x.pow(root - 1, mc), mc), mc), mc);
            if (prevX != null && prevX.subtract(x).abs().compareTo(maxError) < 0)
                break;
            prevX = x;
        }
        System.out.println("]");

        return x;
    }

    public String getPlainTextFileName() {
        return ptFileName;
    }

    public void setPlainTextFileName(String ptFileName) {
        this.ptFileName = ptFileName;
    }

    public String getCipherTextFileName() {
        return ctFileName;
    }

    public void setCipherTextFileName(String ctFileName) {
        this.ctFileName = ctFileName;
    }

    public String getKeyFileName() {
        return keyFileName;
    }

    public void setKeyFileName(String keyFileName) {
        this.keyFileName = keyFileName;
    }
}
