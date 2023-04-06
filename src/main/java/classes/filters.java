package classes;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author danial
 */
public class filters
{
    public ArrayList<File> Name(ArrayList<File> entries, String Key)
    {
        ArrayList<File> output = new ArrayList<File>();
        for (File entry : entries)
        {
//            entry.getName(i).contains(Key)
            if (entry.getName().contains(Key))
            {
                output.add(entry);
            }
        }
        return output;
    }

    public ArrayList<File> Length(ArrayList<File> entries, long Length, String Operator)
    {
        ArrayList<File> output = new ArrayList<File>();
        for (File entry : entries)
        {
            if (entry.isFile())
            {
                switch (Operator)
                {
                    case "EQ":
                        if (entry.length() == Length)
                            output.add(entry);
                        break;
                    case "NEQ":
                        if (entry.length() != Length)
                            output.add(entry);
                        break;
                    case "GT":
                        if (entry.length() > Length)
                            output.add(entry);
                        break;
                    case "GTE":
                        if (entry.length() >= Length)
                            output.add(entry);
                        break;
                    case "LT":
                        if (entry.length() < Length)
                            output.add(entry);
                        break;
                    case "LTE":
                        if (entry.length() <= Length)
                            output.add(entry);
                        break;
                }
            }
        }
        return output;
    }

    public ArrayList<File> Content(ArrayList<File> entries, String key)
    {
        ArrayList<File> output = new ArrayList<File>();

        for (File entry : entries)
        {
            try (BufferedReader br = new BufferedReader(new FileReader(entry)))
            {
                String line;
                while ((line = br.readLine()) != null)
                {
                    if (line.contains(key))
                    {
                        output.add(entry);
                        break;
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return output;
    }

    public ArrayList<File> List(ArrayList<File> entries, int Max)
    {
        ArrayList<File> output = new ArrayList<File>();
        for (File entry : entries)
        {
            if (entry.isDirectory())
            {
                int run = Max;

                List<File> filesList = Arrays.asList(entry.listFiles());
                if (filesList.size() < Max)
                {
                    run = filesList.size();
                }
                for (int j = 0; j < run; j++)
                {
                    output.add(filesList.get(j));
                }
            }
        }
        return output;
    }

    public static ArrayList<File> Count(ArrayList<File> entries, String key, int min)
    {
        ArrayList<File> filteredEntries = new ArrayList<>();
        for (File entry : entries)
        {
            if (entry.isFile())
            {
                try
                {
                    String content = new String(Files.readAllBytes(entry.toPath()));
                    int count = countSubstring(content, key);
                    if (count >= min)
                    {
                        filteredEntries.add(entry);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return filteredEntries;
    }

    private static int countSubstring(String str, String subStr)
    {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(subStr, index)) != -1)
        {
            count++;
            index += subStr.length();
        }
        return count;
    }

    public ArrayList<File> Split(ArrayList<File> entries, int Lines)
    {
        ArrayList<File> result = new ArrayList<File>();
        for (File entry : entries)
        {
            if (entry.isDirectory())
            {
                // ignore directories
                continue;
            }

            try
            {
                BufferedReader reader = new BufferedReader(new FileReader(entry));
                int partNumber = 1;
                String line = reader.readLine();

                while (line != null)
                {
                    String partName = entry.getName() + ".part" + partNumber + ".txt";
                    FileWriter writer = new FileWriter(partName);

                    for (int i = 0; i < Lines && line != null; i++)
                    {
                        writer.write(line + "\n");
                        line = reader.readLine();
                    }
                    writer.close();
                    result.add(new File(partName));
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
        int countFile = originalFiles.size(); // counter for setting maximum bound in the for loop
        ArrayList<File> renamedFiles = new ArrayList<File>(); // list for the new file names after appending the suffix string

        for (int i = 0; i < countFile; i++)
        { // loop to iterate through the list containing the original files
            File file = originalFiles.get(i); // get the current file from the original files list
            if (file.isDirectory())
            {
                String newFileName = file.getAbsolutePath() + suffix; // using the getAbsolutePath() method to get the file's absolute path and concatenate the suffix string
                File newFile = new File(newFileName); // create a new file object with the new file name
                renamedFiles.add(newFile); // adding the renamed file to the renamedFiles list
            }
            else
            {
                String fileName = file.getName(); // get the current file name from the file object
                int dotIndex = fileName.lastIndexOf('.'); // saves the last position of the "." character into the dotIndex variable
                String newFileName = fileName.substring(0, dotIndex) + suffix + fileName.substring(dotIndex); // using the substring() method to concatenate the suffix string before the file's extension
                File newFile = new File(file.getParent(), newFileName); // create a new file object with the parent directory and the new file name
                renamedFiles.add(newFile); // adding the renamed file to the renamedFiles list
            }
        }
        return renamedFiles; // returning renamedFiles list of type List<File>
    }

    public void print(ArrayList<File> renamedFiles, String inType, int enId)
    {
        int countFile = renamedFiles.size(); //counter for setting maximum bound in the for loop
        if (inType.equals("local"))
        {
            for (int i = 0; i < countFile; i++)
            {
                System.out.println("Name: " + renamedFiles.get(i).getName());
                if (renamedFiles.get(i).isDirectory())
                {
                    int size = 0;
                    List<File> subEntries = Arrays.asList(renamedFiles.get(i).listFiles());
                    for (int j = 0; j < subEntries.size(); j++)
                    {
                        size += subEntries.get(j).length();
                    }
                    System.out.println("Length: " + size);
                }
                else
                {
                    System.out.println("Length " + renamedFiles.get(i).length());
                }
                System.out.println("Absolute Path: " + renamedFiles.get(i).getPath());
            }
        }

        else
        {
            for (int i = 0; i < countFile; i++)
            {
                System.out.println("Name: " + renamedFiles.get(i).getName());
                System.out.println("EntryId: " + enId);
                if (renamedFiles.get(i).isDirectory())
                {
                    int size = 0;
                    List<File> subEntries = Arrays.asList(renamedFiles.get(i).listFiles());
                    for (int j = 0; j < subEntries.size(); j++)
                    {
                        size += subEntries.get(j).length();
                    }
                    System.out.println("Length: " + size);
                }
                else
                {
                    System.out.println("Length " + renamedFiles.get(i).length());
                }
                System.out.println("Absolute Path: " + renamedFiles.get(i).getPath());
            }
        }
    }
}
