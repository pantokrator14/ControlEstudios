package controlestudios.utils;

import java.nio.file.Paths;

public class SystemPaths {
    public static String getDesktopPath() {
        return Paths.get(System.getProperty("user.home"), "Desktop").toString();
    }
}