package RSA;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

public class GenerateKeys {
    private final SecureRandom random = new SecureRandom();

    // hàm sinh số nguyên tố lớn làm p và q
    private BigInteger getRandomPrime(int bitLength) {
        return BigInteger.probablePrime(bitLength, random);
    }

    // hàm tính toán cặp khóa public key e và private key d
    private BigInteger[] getKeys(BigInteger p, BigInteger q) {
        BigInteger n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.valueOf(2);
        while (phi.gcd(e).intValue() > 1) {
            e = e.add(BigInteger.ONE);
        }
        BigInteger d = e.modInverse(phi);
        return new BigInteger[]{n, e, d};
    }

    private void writeKey(String filename, BigInteger n, BigInteger e, BigInteger d) throws IOException {
        File file = new File(filename + ".key");
        FileWriter fw = new FileWriter(file);
        fw.write("n:" + n + "\n");
//        fw.write(filename + ":" + keyComponent + "\n");

        if (e != null) {
            fw.write("e:" + e + "\n"); // Khóa công khai có thành phần e
        }
        if (d != null) {
            fw.write("d:" + d + "\n"); // Khóa riêng tư có thành phần d
        }
        fw.close();
        System.out.println("Successfully generated " + filename + " key. ");
        System.out.println(filename + " key path: " + file.getAbsolutePath() + "\n");
    }

    public void generateKeys(int modulusSize) throws IOException {
        int primeSize = modulusSize / 2;

        BigInteger p = getRandomPrime(primeSize);
        BigInteger q = getRandomPrime(primeSize);

        while (p.equals(q)) {
            q = getRandomPrime(primeSize);
        }

        BigInteger[] keys = getKeys(p, q);
        BigInteger n = keys[0];
        BigInteger e = keys[1];
        BigInteger d = keys[2];

        writeKey("public", n, e, null);
        writeKey("private", n, null, d);
    }

    public static void main(String[] args) {
        GenerateKeys generator = new GenerateKeys();
        try {
            generator.generateKeys(1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
