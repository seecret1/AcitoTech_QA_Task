package api.test;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CheckValidate {

    public static boolean isValidUUID(String uuid) {
        if (uuid == null) return false;
        String regex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(uuid);
        return matcher.matches();
    }
}