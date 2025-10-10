package repository;

import java.util.regex.Pattern;

public class Validaciones {
    private static final Pattern EMAIL_RX =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean emailValido(String email) {
        return email != null && EMAIL_RX.matcher(email.trim()).matches();
    }

    public static boolean passwordMinLen(String pass, int min) {
        return pass != null && pass.length() >= min;
    }

    public static boolean esNumero(String s) {
        if (s == null) return false;
        try {
            Integer.parseInt(s.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String linkOrNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

}
