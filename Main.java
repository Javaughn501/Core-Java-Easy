package encryptdecrypt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String operation = "enc";
        String word = "";
        String algorithm = "shift";
        int key = 0;
        File readFromFile = new File("");
        File writeToFile = new File("");

        for (int i = 0; i < args.length; i+=2) {
            switch (args[i]) {
                case "-mode":
                    operation = args[i + 1];
                    break;
                case "-key":
                    key = Integer.valueOf(args[i + 1]);
                    break;
                case "-data":
                    word = args[i+1];
                    break;
                case "-in":
                    readFromFile = new File("./" + args[i + 1]);
                    break;
                case "-out":
                    writeToFile = new File("./" + args[i + 1]);
                    break;
                case "-alg":
                    algorithm = args[i+1];
                    break;
            }
        }

        switch (algorithm) {
            case "shift":
                Encryption shiftEncryption = new ShiftEncryption(operation,word,key,readFromFile,writeToFile);
                shiftEncryption.runAlgorithm();
                break;
            case "unicode":
                Encryption unicodeEncryption = new UnicodeEncryption(operation,word,key,readFromFile,writeToFile);
                unicodeEncryption.runAlgorithm();
                break;
            default:
                break;
        }
    }
}

abstract class Encryption {

    protected String word;
    protected String operation;
    protected int key;
    protected File readFromFile;
    protected File writeToFile;

    protected Encryption(String operation, String word, int key, File readFromFile, File writeToFile) {
        this.operation = operation;
        this.word = word;
        this.key = key;
        this.readFromFile = readFromFile;
        this.writeToFile = writeToFile;
    }

    abstract String encrypt();
    abstract String decrypt();

    protected void runAlgorithm() {
        String encryptedWord = "";

        switch (this.operation) {
            case "enc":
                if (!word.isEmpty()) {
                    encryptedWord = encrypt();
                    writeToFile(encryptedWord);
                    return ;
                }

                this.word = readWordFromFile();
                encryptedWord = encrypt();
                writeToFile(encryptedWord);
                return;

            case "dec":
                if(!word.isEmpty()) {
                    encryptedWord = decrypt();
                    writeToFile(encryptedWord);
                    return;
                }

                this.word = readWordFromFile();
                encryptedWord = decrypt();
                writeToFile(encryptedWord);
                return;

            default:
                return;
        }
    }
    protected String readWordFromFile() {

        String word = "";

        try {
            Scanner scanner = new Scanner(this.readFromFile);
            while (scanner.hasNext()) {
                word += scanner.nextLine();
            }

        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return word;
    }

    protected void writeToFile(String encryptedWord) {

        if (this.writeToFile.getName().isEmpty()) {
            System.out.println(encryptedWord);
            return;
        }

        try {
            FileWriter fileWriter = new FileWriter(this.writeToFile,false);
            fileWriter.write(encryptedWord);
            fileWriter.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

class ShiftEncryption extends Encryption {

    private static int MAX_LOWERCASE = 122;
    private static int MIN_LOWERCASE = 97;
    private static int MAX_UPPERCASE = 90;
    private static int MIN_UPPERCASE = 65;
    public ShiftEncryption(String operation, String word, int key, File readFromFile, File writeToFile) {
        super(operation, word, key, readFromFile, writeToFile);
    }

    @Override
    String encrypt() {
        String encryptedWord = "";

        for (char c : this.word.toCharArray()) {
            if (c >= MIN_LOWERCASE && c <= MAX_LOWERCASE) {
                if (c + this.key > MAX_LOWERCASE) {
                    encryptedWord += (char) (((c + this.key) % (MAX_LOWERCASE + 1)) + MIN_LOWERCASE);
                }
                else {
                    encryptedWord += (char) (c + this.key);
                }
            }
            else if (c >= MIN_UPPERCASE && c <= MAX_UPPERCASE) {
                if ((c + this.key) > MAX_UPPERCASE) {
                    encryptedWord += (char) (((c + this.key) % (MAX_UPPERCASE + 1)) + MIN_UPPERCASE);
                }
                else {
                    encryptedWord += (char) (c + this.key);
                }
            }
            else {
                encryptedWord += c;
            }
        }


        return encryptedWord;
    }

    @Override
    String decrypt() {
        String deccryptedWord = "";

        for (char c : this.word.toCharArray()) {
            if (c >= MIN_LOWERCASE && c <= MAX_LOWERCASE) {
                if (c - this.key < MIN_LOWERCASE) {
                    deccryptedWord += (char) ((MAX_LOWERCASE + 1) - (MIN_LOWERCASE - (c - this.key)));
                }
                else {
                    deccryptedWord += (char) (c - this.key);
                }
            }
            else if (c >= MIN_UPPERCASE && c <= MAX_UPPERCASE) {
                if ((c - this.key) < MIN_UPPERCASE) {
                    deccryptedWord += (char) ((MAX_UPPERCASE + 1) - (MIN_UPPERCASE - (c - this.key)));
                }
                else {
                    deccryptedWord += (char) (c - this.key);
                }
            }
            else {
                deccryptedWord += c;
            }
        }

        return deccryptedWord;
    }
}

class UnicodeEncryption extends Encryption {

    protected UnicodeEncryption(String operation, String word, int key, File readFromFile, File writeToFile) {
        super(operation, word, key, readFromFile, writeToFile);
    }

    @Override
    String encrypt() {
        String encryptedWord = "";

        for (char c : this.word.toCharArray()) {
            encryptedWord += (char) (c + this.key);
        }

        return encryptedWord;
    }

    @Override
    String decrypt() {
        String decryptedWord = "";

        for (char c : this.word.toCharArray()) {
            decryptedWord += (char) (c - this.key);
        }

        return decryptedWord;
    }
}