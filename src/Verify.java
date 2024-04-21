import RSA.Helper;
import RSA.Decrypt;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class Verify {

    private static final String RES_DIRECTORY_PATH = "D:\\RSA-Digital-Signature\\res\\";

    public static boolean verifySignature(String filename, String signature, BigInteger n, BigInteger e) {
        try {
            // Đường dẫn đầy đủ tới file chứa nội dung cần xác minh
            String contentFilePath = RES_DIRECTORY_PATH + filename;

            // Đọc nội dung file
            byte[] contents = Files.readAllBytes(Paths.get(contentFilePath));

            // Tạo đối tượng SHA-1 và cập nhật nó với nội dung của file
            SHA1 sha1 = new SHA1();
            sha1.update(contents);
            String hexSha = sha1.digestHex();

            // Giải mã chữ ký sử dụng khóa công khai
            String decryptedSignature = Decrypt.decrypt(signature, n, e);

            // So sánh giá trị băm của nội dung file với giá trị được giải mã từ chữ ký
            return hexSha.equals(decryptedSignature);

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Verify <file> <signature>");
            System.exit(1);
        }

        String filename = args[0];
        String signatureFilename = RES_DIRECTORY_PATH + args[1];

        try {
            // Đọc chữ ký từ file
            String signature = new String(Files.readAllBytes(Paths.get(signatureFilename)));

            // Đọc khóa công khai
            Map<String, String> keys = Helper.readKey("public");
            String nStr = keys.get("n");
            String eStr = keys.get("e");

            BigInteger n = new BigInteger(nStr);
            BigInteger e = new BigInteger(eStr);

            // Thực hiện xác minh chữ ký
            boolean isValid = verifySignature(filename, signature, n, e);

            if (isValid) {
                System.out.println("The signature is valid.");
            } else {
                System.out.println("The signature is NOT valid.");
            }

        } catch (IOException e) {
            System.out.println("ERROR: Could not read file '" + filename + "' or signature file '" + signatureFilename + "'.");
            e.printStackTrace();
        }
    }
}
