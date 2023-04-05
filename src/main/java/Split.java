/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author macgb
 */
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Split {

    public static void main(String[] args) {
        // Example usage:
        List<File> entries = new ArrayList<>();
        entries.add(new File("file1 3.txt"));
        entries.add(new File("file2 3.txt"));
        List<File> output = splitFiles(entries, 10);
        for (File file : output) {
            System.out.println(file.getName());
        }
    }

    public static List<File> splitFiles(List<File> entries, int lines) {
        List<File> output = new ArrayList<>();

        for (File entry : entries) {
            if (entry.isFile()) {
                try {
                    Path path = entry.toPath();
                    List<String> linesList = Files.readAllLines(path);
                    int numParts = (int) Math.ceil((double) linesList.size() / lines);
                    String fileName = entry.getName().substring(0, entry.getName().lastIndexOf("."));

                    for (int i = 0; i < numParts; i++) {
                        int start = i * lines;
                        int end = Math.min(start + lines, linesList.size());
                        List<String> partLines = linesList.subList(start, end);
                        String partFileName = fileName + ".part" + (i + 1) + ".txt";
                        Path partFilePath = Paths.get(entry.getParent() != null ? entry.getParent() : "",
                              partFileName);
                        Files.write(partFilePath, partLines);
                        output.add(partFilePath.toFile());
                    }
                } catch (IOException e) {
                    System.err.println("Error splitting file " + entry.getAbsolutePath() + ": " + e.getMessage());
                }
            }
        }

        return output;
    }
}
