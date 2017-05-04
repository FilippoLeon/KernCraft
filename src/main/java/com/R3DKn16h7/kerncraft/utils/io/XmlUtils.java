package com.R3DKn16h7.kerncraft.utils.io;

/**
 * Created by Filippo on 21-Apr-17.
 */
public class XmlUtils {
    public static final String parseBBCodeIntoMCFormat(String str) {
        str = str
                .replaceAll("^\n[ ]+", "")
                .replaceAll("\n[ ]+[p/]\n[ ]+", "[p/]")
                .replaceAll("\n", "")
                .replaceAll("[ ]+", " ")
//                .replaceAll("\n[ ]+", " ")
                .replace("[b]", "§l")
                .replace("[b/]", "§r")
                .replace("[s]", "§m")
                .replace("[s/]", "§r")
                .replace("[u]", "§n")
                .replace("[u/]", "§r")
                .replace("[blue]", "§1")
                .replace("[red]", "§4")
                .replace("[reset]", "§r")
                .replace("[yellow]", "§e")
                .replace("[gold]", "§6")
                .replace("[green]", "§2")
                .replace("[magenta]", "§d")
                .replace("[li]", "\u2022")
                .replace("_2", "\u2082")
                .replace("[p/]", "\n\n")
                .replace("[br/]", "\n");

        return str;
    }
}
