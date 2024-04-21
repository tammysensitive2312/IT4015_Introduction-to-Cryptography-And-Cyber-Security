import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class SHA1 {

    private final int[] H = {
            0x67452301,
            0xEFCDAB89,
            0x98BADCFE,
            0x10325476,
            0xC3D2E1F0
    };
    /** thực hiện phép xoay trái trên số nguyên 32-bit * trộn các bit của dữ liệu, làm tăng độ phức tạp của giá trị băm */
    private int leftRotate(int n, int x) {
        return (x << n) | (x >>> (32-n));
    }

    /** padding data
     * đảm bảo tổng độ dài của dữ liệu sau khi padding là bội của 512bit = 64 byte */
    private byte[] prepareData(byte[] data) {
        //  độ dài ban đầu
        int originalLength = data.length;
        // xác định số byte còn lại sau khi chia dữ liệu thành các khối 64-byte.
        int tailLength = originalLength % 64;
        //  số byte đệm cần thiết
        int paddingLength = (tailLength < 56) ? (56 - tailLength) : (120 - tailLength);
        long bitLength = originalLength * 8L;

        /* khởi tạo mảng data chứa độ dài của data sau khi được padding
        2 tham số là mảng data ban đầu và đ dài mới 8 byte cuối lưu độ dài của dữ liệu ban đầu
         */
        byte[] paddedData = Arrays.copyOf(data, originalLength + paddingLength + 8);
        // Thêm bit '1' vào ngay sau dữ liệu ban đầu
        paddedData[originalLength] = (byte) 0x80;

        /* độ dài của thông điệp  được thêm vào cuối dữ liệu */
        for (int i = 0; i < 8; i++) {
            paddedData[paddedData.length - 8 + i] = (byte) (bitLength >>> (8 * (7 - i)));
        }
        return paddedData;
    }

    private void processBlock(byte[] block) {
        int[] W = new int[80];
        int a, b, c, d, e;

        // Chuyển đổi khối thành 16 từ 32-bit
        for (int i = 0; i < 16; i++) {
            W[i] = ((block[i*4] & 0xFF) << 24) | ((block[i*4 + 1] & 0xFF) << 16) |
                    ((block[i*4 + 2] & 0xFF) << 8) | (block[i*4 + 3] & 0xFF);
        }

        // Mở rộng 16 từ thành 80 từ
        for (int i = 16; i < 80; i++) {
            W[i] = leftRotate(1, W[i-3] ^ W[i-8] ^ W[i-14] ^ W[i-16]);
        }

        // Khởi tạo giá trị hash tạm thời
        a = H[0];
        b = H[1];
        c = H[2];
        d = H[3];
        e = H[4];

        // Lặp chính
        for (int i = 0; i < 80; i++) {
            int f, k;

            if (i < 20) {
                f = (b & c) | ((~b) & d);
                k = 0x5A827999;
            } else if (i < 40) {
                f = b ^ c ^ d;
                k = 0x6ED9EBA1;
            } else if (i < 60) {
                f = (b & c) | (b & d) | (c & d);
                k = 0x8F1BBCDC;
            } else {
                f = b ^ c ^ d;
                k = 0xCA62C1D6;
            }

            int temp = leftRotate(5, a) + f + e + k + W[i];
            e = d;
            d = c;
            c = leftRotate(30, b);
            b = a;
            a = temp;
        }

        // Cộng dồn vào kết quả hash
        H[0] += a;
        H[1] += b;
        H[2] += c;
        H[3] += d;
        H[4] += e;
    }

    /** xử lý đầu vào và cập nhật giá trị băm
     * khi tất cả các khối 512bit đã được xử lý, giá trị băm cuối cùng phản ánh dữ liệu đầu vào ban đầu cùng với mọi đệm đã thêm vào. */
    public void update(byte[] data) {
        byte[] preparedData = prepareData(data);
        // vòng lặp sẽ thực hiện cho mỗi khối 64-byte
        for (int i = 0; i < preparedData.length / 64; i++) {
            // xử lý một phần preparedData
            processBlock(Arrays.copyOfRange(preparedData, i * 64, (i + 1) * 64));
        }
    }

    /** tạo chuỗi biểu diễn giá trị băm ở dạng hệ hexa (16) */
    public String digestHex() {
        StringBuilder hexString = new StringBuilder();
        /*
        hàm thực hiện một vòng lặp qua mảng H, chứa 5 giá trị hash cuối cùng sau khi đã xử lý toàn bộ dữ liệu đầu vào.
        Mỗi giá trị trong H là một số nguyên 32-bit.
         */
        /*
        String.format("%08x", H[i]) sử dụng để chuyển đổi giá trị số nguyên hiện tại trong H sang dạng chuỗi hệ thập lục phân
         */
        for (int i = 0; i < H.length; i++) {
            hexString.append(String.format("%08x", H[i]));
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        SHA1 sha1 = new SHA1();
        File file = new File("D:\\RSA-Digital-Signature\\src\\doc.txt");

        try (FileInputStream fis = new FileInputStream(file)) {
            // khởi tạo một mảng byte với kích thước 1024byte tạm thời chứa dữ liệu đọc tư file
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(dataBuffer)) != -1) {
                sha1.update(Arrays.copyOf(dataBuffer, bytesRead));
            }
            System.out.println("SHA-1 Hash: " + sha1.digestHex());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
