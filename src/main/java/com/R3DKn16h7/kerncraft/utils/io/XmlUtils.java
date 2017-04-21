package com.R3DKn16h7.kerncraft.utils.io;

/**
 * Created by Filippo on 21-Apr-17.
 */
public class XmlUtils {
    public static final String parseBBCodeIntoMCFormat(String str) {
        str = str
                .replaceAll("\n[ ]+", " ")
                .replace("[b]", "§l")
                .replace("[/b]", "§r")
                .replace("[br/]", "\n\n");

        return str;
    }
}
