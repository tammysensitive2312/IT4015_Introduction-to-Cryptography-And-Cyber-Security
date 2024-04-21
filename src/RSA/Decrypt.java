package RSA;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

public class Decrypt {
    public static String decrypt(String cipherTextHex, BigInteger n, BigInteger d) {
        // Chuyển đổi cipherText từ dạng chuỗi hex sang BigInteger
        BigInteger cipherText = new BigInteger(cipherTextHex);

        // Thực hiện giải mã
        BigInteger decryptedInt = cipherText.modPow(d, n);

        // Chuyển đổi BigInteger đã giải mã sang một mảng byte
        byte[] decryptedBytes = decryptedInt.toByteArray();

        // BigInteger.toByteArray() có thể thêm một byte dẫn đầu 0 để biểu thị số dương.
        // Nếu byte dẫn đầu là 0, ta cần loại bỏ nó.
        if (decryptedBytes[0] == 0) {
            byte[] temp = new byte[decryptedBytes.length - 1];
            System.arraycopy(decryptedBytes, 1, temp, 0, temp.length);
            decryptedBytes = temp;
        }

        // Chuyển đổi mảng byte đã giải mã sang chuỗi sử dụng UTF-8
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter cipher text: ");
        String cipherText = sc.nextLine();

        HashMap<String, String> keys = Helper.readKey("private");
        BigInteger n = new BigInteger(keys.get("n"));
        BigInteger d = new BigInteger(keys.get("d"));

        System.out.println("\nn: " + n);
        System.out.println("d: " + d);
        System.out.println();

        String decryptedMessage = decrypt(cipherText, n, d);

        System.out.println("Decrypted message:");
        System.out.println(decryptedMessage);

        Helper.writeFile("decrypt_", decryptedMessage);
    }
}
