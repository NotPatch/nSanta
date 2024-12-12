package com.notpatch.nsanta.util;

import com.notpatch.nsanta.NSanta;
import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtil {

    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String hexColor(String message) {
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, "" + ChatColor.of(color));
            matcher = pattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getColored(String path) {
        return hexColor(NSanta.getInstance().getConfig().getString(path));
    }

    public static String get(String path){
        return NSanta.getInstance().getConfig().getString(path);
    }

    public static Object get(Object path){
        return NSanta.getInstance().getConfig().get(path.toString());
    }

    public static List<String> getColoredList(List<String> list) {
        return list.stream()
                .map(StringUtil::hexColor)
                .collect(Collectors.toList());
    }

    public static List<String> getColoredList(List<String> list, String replace, String replacement) {
        return list.stream()
                .map(StringUtil::hexColor)
                .map(s -> s.replace(replace, replacement))
                .collect(Collectors.toList());
    }

}
