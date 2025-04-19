package ea.foort.litevote.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class Color {
    @NotNull
    public static String color(String from) {
        if (from == null) {
            (new RuntimeException()).printStackTrace();
            return "";
        } else {
            Pattern pattern = Pattern.compile("#[a-fA-F\u0000-9]{6}");

            for(Matcher matcher = pattern.matcher(from); matcher.find(); matcher = pattern.matcher(from)) {
                String hexCode = from.substring(matcher.start(), matcher.end());
                String replaceSharp = hexCode.replace('#', 'x');
                char[] ch = replaceSharp.toCharArray();
                StringBuilder builder = new StringBuilder();
                char[] var7 = ch;
                int var8 = ch.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    char c = var7[var9];
                    builder.append("&").append(c);
                }

                from = from.replace(hexCode, builder.toString());
            }

            String var10000 = ChatColor.translateAlternateColorCodes('&', from);
            if (var10000 == null) {
            }

            return var10000;
        }
    }
}