package RSA;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;

public class Helper {
    public static HashMap<String, String> readKey(String filetype) {
        String filename = filetype + ".key";
        String filepath = System.getProperty("user.dir") + File.separator + filename;

        HashMap<String, String> keyMap = new HashMap<>();
        File file = new File(filepath);

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
            System.err.println("File not found: " + filepath);
        }
        return keyMap;
    }

    public static void writeFile(String action, String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String currentTime = LocalDateTime.now().format(formatter);
        String filePath = action + currentTime + ".txt";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(String.valueOf(message));
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
