/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author macgb
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// ContentFilter

public class Main {
    
    public static void main(String[] args) {
        // Example usage:
        List<File> entries = new ArrayList<>();
        entries.add(new File("file1 1.txt"));
        entries.add(new File("file2 2.txt"));
        String key = "key";
        List<File> output = filterEntries(entries, key);
        for (File file : output) {
            System.out.println("List of entries where key is present : " + file.getName());
        }
    }
    
    public static List<File> filterEntries(List<File> entries, String key) {
        List<File> output = new ArrayList<>();
        for (File entry : entries) {
            if (entry.isFile() && containsKey(entry, key)) {
                output.add(entry);
            }
        }
        return output;
    }
    
    public static boolean containsKey(File file, String key) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(key)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + file.getAbsolutePath() + ": " + e.getMessage());
        }
        return false;
    }

    public static void filterFilesByKeyword(List<File> entries, String key) {
        List<File> output = filterEntries(entries, key);
        for (File file : output) {
            System.out.println("List of entries where key is present : " + file.getName());
        }
    }
}
