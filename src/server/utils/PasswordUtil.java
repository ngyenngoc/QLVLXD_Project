package server.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static void main(String[] args) {
        String maBam = hashPassword("789");
        System.out.println("HÃY COPY ĐOẠN MÃ DƯỚI ĐÂY DÁN VÀO SQL SERVER:");
        System.out.println(maBam);
    }
}