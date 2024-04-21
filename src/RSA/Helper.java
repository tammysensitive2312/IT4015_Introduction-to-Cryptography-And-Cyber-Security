package RSA;

import java.io.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;

public class Helper {
    private static final String RES_DIRECTORY_PATH = "D:\\RSA-Digital-Signature\\res\\";

    public static void writeKey(String filename, BigInteger n, BigInteger e, BigInteger d) throws IOException {

        File directory = new File(RES_DIRECTORY_PATH);
        if (!directory.exists()){
            directory.mkdir(); // Tạo thư mục nếu nó không tồn tại
        }

        File file = new File(RES_DIRECTORY_PATH,filename + ".key");
        FileWriter fw = new FileWriter(file);
        fw.write("n:" + n + "\n");

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

    public static HashMap<String, String> readKey(String filetype) {
        String filename = filetype + ".key";

        HashMap<String, String> keyMap = new HashMap<>();
        File file = new File(RES_DIRECTORY_PATH, filename);

        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    keyMap.put(parts[0], parts[1].trim());
                }
            }
            sc.close();
            System.out.println("Key " + filename + " read successfully!");
        }  catch (FileNotFoundException e) {
            System.err.println("File not found: " + file.getAbsolutePath());
        }
        return keyMap;
    }

    public static void writeFile(String action, String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String currentTime = LocalDateTime.now().format(formatter);
        String filePath = RES_DIRECTORY_PATH + action + currentTime + ".txt";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(String.valueOf(message));
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
