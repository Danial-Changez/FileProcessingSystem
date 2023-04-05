/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author macgb
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author HAADI REHAN
 */
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Example usage:
        List<File> entries = Arrays.asList(new File("file1.txt"));
        List<File> filteredEntries = filterByCount(entries, "hello", 2);
        for (File entry : filteredEntries) {
            System.out.println(entry.getAbsolutePath());
        }
    }

    public static List<File> filterByCount(List<File> entries, String key, int min) {
        List<File> filteredEntries = new ArrayList<>();
        for (File entry : entries) {
            if (entry.isFile()) {
                try {
                    String content = new String(Files.readAllBytes(entry.toPath()));
                    int count = countSubstring(content, key);
                    if (count >= min) {
                        filteredEntries.add(entry);
                        long fileSize = entry.length();
                        System.out.println("File size of " + entry.getAbsolutePath() + " is " + fileSize + " bytes");
                    }
                } catch (IOException e) {
                    System.err.println("Error reading file " + entry.getAbsolutePath() + ": " + e.getMessage());
                }
            }
        }
        return filteredEntries;
    }

    private static int countSubstring(String str, String subStr) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(subStr, index)) != -1) {
            count++;
            index += subStr.length();
        }
        return count;
    }
}
