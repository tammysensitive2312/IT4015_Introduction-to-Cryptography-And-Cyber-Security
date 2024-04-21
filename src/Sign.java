import RSA.Helper;
import RSA.Encrypt;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class Sign {
    private static void usage() {
        System.out.println("Usage: java Sign <file> [<file> ...]");
        System.exit(1);
    }
    public static void main(String[] args) {
        if (args.length < 1) {
            usage();
        }

        for (String filename : args) {
            try {
                byte[] contents = Files.readAllBytes(Paths.get(filename));

                SHA1 sha1 = new SHA1();
                sha1.update(contents);
                String hexSha = sha1.digestHex();

                Map<String, String> keys = Helper.readKey("private");

                String nStr = keys.get("n");
                String dStr = keys.get("d");

                BigInteger n = new BigInteger(nStr);
                BigInteger e = new BigInteger(dStr);

                String signature = String.valueOf(Encrypt.encrypt(hexSha, n, e));

                String sendMes = new String(contents) + "*/*/*/*" + signature;

                Helper.writeFile("signed", signature);

                System.out.println("Signed document");
                System.out.println(sendMes);

            } catch (IOException e) {
                System.out.println("ERROR: Input file \"" + filename + "\" cannot be read.");
                e.printStackTrace();
            }
        }


    }
}
