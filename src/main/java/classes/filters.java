package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author danial
 *
 * The filters class provides various file filtering operations based on
 * different criteria such as name, length, content, list, count, split, and
 * rename.
 */
public class filters
{
    public ArrayList<File> Name(ArrayList<File> entries, String Key)
    {
        ArrayList<File> output = new ArrayList<>(); // Create an ArrayList to store output files
        for (File entry : entries) // Loop through each entry in the input list
        {
            if (entry.getName().toLowerCase().contains(Key.toLowerCase())) // Check if entry's name contains the given Key
            {
                output.add(entry); // If yes, add the entry to the output list
            }
        }
        return output; // Return the filtered sub-list of entries with the given Key in their name
    }

    public ArrayList<File> Length(ArrayList<File> entries, long Length, String Operator)
    {
        ArrayList<File> output = new ArrayList<File>(); // Create an ArrayList to store output files
        for (File entry : entries) // Loop through each entry in the input list
        {
            if (entry.isFile()) // Check if entry is a file (not a directory)
            {
                switch (Operator)
                {
                    case "EQ": // If Operator is "EQ" (equal to)
                        if (entry.length() == Length) // Check if entry's length is equal to given Length
                        {
                            output.add(entry); // If yes, add the entry to the output list
                        }
                        break;
                    case "NEQ": // If Operator is "NEQ" (not equal to)
                        if (entry.length() != Length)
                        {
                            output.add(entry);
                        }
                        break;
                    case "GT": // If Operator is "GT" (greater than)
                        if (entry.length() > Length)
                        {
                            output.add(entry);
                        }
                        break;
                    case "GTE": // If Operator is "GTE" (greater than or equal to)
                        if (entry.length() >= Length)
                        {
                            output.add(entry);
                        }
                        break;
                    case "LT": // If Operator is "LT" (less than)
                        if (entry.length() < Length)
                        {
                            output.add(entry);
                        }
                        break;
                    case "LTE": // If Operator is "LTE" (less than or equal to)
                        if (entry.length() <= Length)
                        {
                            output.add(entry);
                        }
                        break;
                }
            }
        }
        return output; // Return the filtered sub-list of entries whose length satisfies the given Operator, with regard to the given Length
    }

    public ArrayList<File> Content(ArrayList<File> entries, String key)
    {
        ArrayList<File> output = new ArrayList<File>();

        // Loop through each entry in the input list of entries
        for (File entry : entries)
        {
            try (BufferedReader br = new BufferedReader(new FileReader(entry)))
            {
                String line;
                // Read the content of the file line by line
                while ((line = br.readLine()) != null)
                {
                    // Check if the line contains the given key
                    if (line.contains(key))
                    {
                        // If yes, add the entry to the output list and break the loop
                        output.add(entry);
                        break;
                    }
                }
            }
            catch (IOException e)
            {
                // Print stack trace in case of any IO exception
                e.printStackTrace();
            }
        }
        // Return the output list containing entries with content containing the given key
        return output;
    }

    public ArrayList<File> List(ArrayList<File> entries, int Max)
    {
        ArrayList<File> output = new ArrayList<File>(); // Create an ArrayList to store the output
        for (File entry : entries)
        { // Loop through each entry in the input list
            if (entry.isDirectory())
            { // Check if the entry is a directory
                int run = Max; // Initialize a variable 'run' to store the value of Max

                List<File> filesList = Arrays.asList(entry.listFiles()); // Get a list of files in the directory
                if (filesList.size() < Max)
                { // If the number of files in the directory is less than Max
                    run = filesList.size(); // Update 'run' to the size of the filesList
                }
                for (int j = 0; j < run; j++)
                { // Loop through 'run' number of files from the filesList
                    output.add(filesList.get(j)); // Add the files to the output ArrayList
                }
            }
        }
        return output; // Return the output list of selected entries from the directories
    }

    public static ArrayList<File> Count(ArrayList<File> entries, String key, int min)
    {
        ArrayList<File> filteredEntries = new ArrayList<>();

        // Loop through each entry in the input list of entries
        for (File entry : entries)
        {
            // Check if the entry is a file
            if (entry.isFile())
            {
                try
                {
                    // Read the content of the file into a string
                    String content = new String(Files.readAllBytes(entry.toPath()));
                    // Count the occurrences of the given key in the content
                    int count = countSubstring(content, key);
                    // Check if the count is greater than or equal to the given min
                    if (count >= min)
                    {
                        // If yes, add the entry to the filteredEntries list
                        filteredEntries.add(entry);
                    }
                }
                catch (IOException e)
                {
                    // Print stack trace in case of any IO exception
                    e.printStackTrace();
                }
            }
        }
        // Return the filteredEntries list containing entries with content containing the given key at least min times
        return filteredEntries;
    }

// Helper method to count the occurrences of a substring in a string
    private static int countSubstring(String str, String subStr)
    {
        int count = 0;
        int index = 0;

        // Loop through the string to find occurrences of the substring
        while ((index = str.indexOf(subStr, index)) != -1)
        {
            count++;
            index += subStr.length();
        }

        return count;
    }

    public ArrayList<File> Split(ArrayList<File> entries, int Lines)
    {

        ArrayList<File> result = new ArrayList<>();
        for (File entry : entries)
        {
            if (entry.isDirectory())
            {
                // ignore directories
                continue;
            }
            try
            {
                BufferedReader reader = new BufferedReader(new FileReader(entry)); // Create a BufferedReader to read from the input file
                int partNumber = 1;
                String line = reader.readLine(); // Read the first line from the input file

                while (line != null)
                {
                    String partName = entry.getName() + ".part" + partNumber + ".txt";
                    FileWriter writer = new FileWriter(partName);

                    // Write Lines number of lines to the current split file
                    for (int i = 0; i < Lines && line != null; i++)
                    {
                        writer.write(line + "\n"); // Write line to the current split file
                        line = reader.readLine(); // Read the next line from the input file
                    }

                    writer.close(); // Close the FileWriter to flush and close the current split file
                    result.add(new File(partName)); // Add the current split file to the result list
                    partNumber++;
                }
                reader.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static ArrayList<File> Rename(ArrayList<File> originalFiles, String suffix)
    {
        ArrayList<File> renamedFiles = new ArrayList<>(); // list for the new file names after appending the suffix string
        for (File originalFile : originalFiles)
        { // loop through each file in the originalFiles list
            File renamedFile; // create a file object to store the renamed file

            if (originalFile.isDirectory())
            { // if the file is a directory
                String newDirectoryName = originalFile.getName() + suffix; // append the suffix to the directory name
                renamedFile = new File(originalFile.getParent(), newDirectoryName); // create a new file object with the new directory name
            }
            else
            { // if the file is a regular file
                String fileName = originalFile.getName(); // get the current file name from the file object
                int dotIndex = fileName.lastIndexOf('.'); // saves the last position of the "." character into the dotIndex variable
                String newFileName = fileName.substring(0, dotIndex) + suffix + fileName.substring(dotIndex); // using the substring() method to concatenate the suffix string before the file's extension
                renamedFile = new File(originalFile.getParent(), newFileName); // create a new file object with the parent directory and the new file name

                try (BufferedReader reader = new BufferedReader(new FileReader(originalFile)); BufferedWriter writer = new BufferedWriter(new FileWriter(renamedFile)))
                { // using try-with-resources statement to auto-close reader and writer
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        writer.write(line);
                        writer.newLine(); // Add a newline character after each line
                    }
                }
                catch (IOException e)
                { // catch IOExceptions that can occur while reading/writing files
                    e.printStackTrace();
                }
            }
            renamedFiles.add(renamedFile); // adding the renamed file/directory to the renamedFiles list
        }
        return renamedFiles; // returning renamedFiles list of type List<File>
    }

    public void print(ArrayList<File> renamedFiles, String inType, int enId)
    {
        // Print information for local entries
        if (inType.equals("local"))
        {
            for (File file : renamedFiles)
            {
                System.out.println("Name: " + file.getName()); // Print name of the entry
                if (file.isDirectory())
                {
                    int size = 0;
                    File[] subEntries = file.listFiles();
                    for (File subEntry : subEntries)
                    {
                        size += subEntry.length();
                    }
                    System.out.println("Length: " + size); // Print total size of sub-entries if the entry is a directory
                }
                else
                {
                    System.out.println("Length: " + file.length()); // Print length of the entry if it's a file
                }
                System.out.println("Absolute Path: " + file.getAbsolutePath() + "\n"); // Print absolute path of the entry
            }
        }
        // Print information for remote entries
        else
        {
            for (File file : renamedFiles)
            {
                System.out.println("Name: " + file.getName()); // Print name of the entry
                System.out.println("EntryId: " + enId); // Print entryId (provided as parameter)
                if (file.isDirectory())
                {
                    int size = 0;
                    File[] subEntries = file.listFiles();
                    for (File subEntry : subEntries)
                    {
                        size += subEntry.length();
                    }
                    System.out.println("Length: " + size); // Print total size of sub-entries if the entry is a directory
                }
                else
                {
                    System.out.println("Length: " + file.length()); // Print length of the entry if it's a file
                }
                System.out.println("Absolute Path: " + file.getPath() + "\n"); // Print path of the entry. Since it's downloaded locally, the relative path is the same as the remote absolute path
            }
        }
    }
}
