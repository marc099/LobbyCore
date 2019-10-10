package me.mralecroyt.Listener.Utils;


import java.util.regex.*;

public class Util
{
    public static boolean containsLink(final String message) {
        return Pattern.compile("^((https?|ftp):\\/\\/|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([\\/?].*)?$").matcher(message).find();
    }

    public static boolean containsIP(final String message) {
        return Pattern.compile("(?i)(((([a-zA-Z0-9-]+\\.)+(de|eu|com|net|to|es|ga|us|nu|tk|io|co|org|gs|xyz|me|info|biz|tv))|([0-9]{1,3}\\.){3}[0-9]{1,3})(\\:[0-9]{2,5})?)").matcher(message).find();
    }
}

