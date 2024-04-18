package RSA;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

public class Encrypt {
    public static BigInteger encrypt(String message, BigInteger n, BigInteger e) {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        BigInteger encodedMessage = new BigInteger(1, messageBytes);

        BigInteger encryptedMessage = encodedMessage.modPow(e, n);
        return encryptedMessage;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter message: ");
        String message = scanner.nextLine();

        HashMap<String, String> keys = Helper.readKey("public");

        String nStr = keys.get("n");
        String eStr = keys.get("e");

        if (nStr == null || eStr == null) {
            System.err.println("Lỗi: Không tìm thấy giá trị khóa n hoặc e trong file.");
            return;
        }
        BigInteger n = new BigInteger(nStr);
        BigInteger e = new BigInteger(eStr);

        System.out.println("\nn: " + n);
        System.out.println("e: " + e);

        BigInteger encryptedMessage = encrypt(message, n, e);

        System.out.println("Encrypted message:");
        System.out.println(encryptedMessage);


        Helper.writeFile("encrypt_", String.valueOf(encryptedMessage));
    }
}
