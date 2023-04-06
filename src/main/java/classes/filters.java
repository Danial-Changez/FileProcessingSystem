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
    public static ArrayList<File> renameFile(List<File> originalFileNames, String suffix)
    {
        int countFile = originalFileNames.size(); //counter for setting maximum bound in the for loop
        ArrayList<File> renamedFiles = new ArrayList<>(); //array list for the new file names after appending the suffix string
        String fileName; //string to store the current file name while itterating through the loop
        int dotIndex; //variable used to store the index of the "." in the filename string
        String newFileName; //string used to store the new filer name after appending the suffix string

        for (int i = 0; i < countFile - 1; i++)
        { // loop to itterate through the array list containing the original file names
            fileName = originalFileNames.get(i).getName();// applying the getName() method in the File library
            dotIndex = fileName.lastIndexOf('.'); //saves the last position of the "." character into the dotindex variable
            newFileName = fileName.substring(0, dotIndex) + suffix + fileName.substring(dotIndex); //using the substring() method to concatenate the suffix string before the ".txt"
            File renamedFile = new File(originalFileNames.get(i).getParent(), newFileName); //using the getParent() method to set the path of the original file to the new renamed file
            renamedFiles.add(renamedFile); // adding the renamed file into the renamedFiles list
        }
        return renamedFiles; // returning renamedFiles list
    }

    public ArrayList<String> Name(ArrayList<String> entry, String Key)
    {
        ArrayList<String> output = new ArrayList<String>();
        for (int i = 0; i < entry.size(); i++)
        {
            if (entry.get(i).contains(Key))
            {
                output.add(entry.get(i));
            }
        }
        return output;
    }

    public ArrayList<String> Length(ArrayList<String> entry, long Length, String Operator)
    {
        ArrayList<String> output = new ArrayList<String>();
        for (int i = 0; i < entry.size(); i++)
        {
            File file = new File(entry.get(i));
            if (file.isFile())
            {
                switch (Operator)
                {
                    case "EQ":
                        if (entry.get(i).length() == Length)
                            output.add(entry.get(i));
                        break;
                    case "NEQ":
                        if (entry.get(i).length() != Length)
                            output.add(entry.get(i));
                        break;
                    case "GT":
                        if (entry.get(i).length() > Length)
                            output.add(entry.get(i));
                        break;
                    case "GTE":
                        if (entry.get(i).length() >= Length)
                            output.add(entry.get(i));
                        break;
                    case "LT":
                        if (entry.get(i).length() < Length)
                            output.add(entry.get(i));
                        break;
                    case "LTE":
                        if (entry.get(i).length() <= Length)
                            output.add(entry.get(i));
                        break;
                }
            }
        }
        return output;
    }

    public ArrayList<String> Content(ArrayList<String> entries, String key)
    {
        ArrayList<String> output = new ArrayList<>();
        for (String entry : entries)
        {
            File checkFile = new File(entry);
            try
            {
                Path path = Paths.get(entry);
                String fileContent = Files.readString(path);

                if (checkFile.isFile() && fileContent.contains(key))
                {
                    output.add(entry);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return output;
    }

    public ArrayList<String> List(ArrayList<String> entry, int Max)
    {
        ArrayList<String> output = new ArrayList<String>();
        for (int i = 0; i < entry.size(); i++)
        {
            File directory = new File(entry.get(i));
            if (directory.isDirectory())
            {
                int run = Max;
                List<File> filesList = Arrays.asList(directory.listFiles());

                if (filesList.size() < Max)
                {
                    run = filesList.size();
                }
                for (int j = 0; j < run; j++)
                {
                    output.add(filesList.get(j).toString());
                }
            }
        }
        return output;
    }

    public static ArrayList<String> Count(ArrayList<String> entries, String key, int min)
    {
        ArrayList<String> filteredEntries = new ArrayList<>();
        for (String entry : entries)
        {
            File checkFile = new File(entry);
            if (checkFile.isFile())
            {
                try
                {
                    Path path = Paths.get(entry);
                    String content = new String(Files.readString(path));
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

    public ArrayList<String> Split(ArrayList<String> entry, int Lines)
    {
        ArrayList<String> result = new ArrayList<String>();
        for (String e : entry)
        {
            File file = new File(e);
            if (file.isDirectory())
            {
                // ignore directories
                continue;
            }

            try
            {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                int partNumber = 1;
                String line = reader.readLine();

                while (line != null)
                {
                    String partName = file.getName() + ".part" + partNumber + ".txt";
                    FileWriter writer = new FileWriter(partName);

                    for (int i = 0; i < Lines && line != null; i++)
                    {
                        writer.write(line + "\n");
                        line = reader.readLine();
                    }
                    writer.close();
                    result.add(partName);
                    partNumber++;
                }
                reader.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        // return the list of generated files
        return result;
    }

    public static ArrayList<String> Rename(ArrayList<String> originalFileNames, String suffix)
    {
        int countFile = originalFileNames.size(); // counter for setting maximum bound in the for loop
        ArrayList<String> renamedFiles = new ArrayList<>(); // array list for the new file names after appending the suffix string
        String fileName; // string to store the current file name while iterating through the loop
        int dotIndex; // variable used to store the index of the "." in the filename string
        String newFileName; // string used to store the new file name after appending the suffix string

        for (int i = 0; i < countFile; i++)
        { // loop to iterate through the array list containing the original file names
            fileName = originalFileNames.get(i); // get the current file name from the original file names list
            dotIndex = fileName.lastIndexOf('.'); // saves the last position of the "." character into the dotIndex variable
            newFileName = fileName.substring(0, dotIndex) + suffix + fileName.substring(dotIndex); // using the substring() method to concatenate the suffix string before the ".txt"
            renamedFiles.add(newFileName); // adding the renamed file name into the renamedFiles list
        }
        return renamedFiles; // returning renamedFiles list of type ArrayList<String>
    }

    public void print(ArrayList<String> renamedFiles, String inType, int enId)
    {
        int countFile = renamedFiles.size(); //counter for setting maximum bound in the for loop
        if (inType.equals("local"))
        {
            for (int i = 0; i < countFile; i++)
            {
                File file = new File(renamedFiles.get(i));
                System.out.println("Name: " + file.getName());
                if (file.isDirectory())
                {
                    int size = 0;
                    List<File> subEntries = Arrays.asList(file.listFiles());
                    for (int j = 0; j < subEntries.size(); j++)
                    {
                        size += subEntries.get(i).length();
                    }
                    System.out.println("Length: " + size);
                }
                else
                {
                    System.out.println("Length " + file.length());
                }
                System.out.println("Absolute Path: " + file.getPath());
            }
        }

        else
        {
            for (int i = 0; i < countFile; i++)
            {
                File file = new File(renamedFiles.get(i));
                System.out.println("Name: " + file.getName());
                System.out.println("EntryId: " + enId);
                if (file.isDirectory())
                {
                    int size = 0;
                    List<File> subEntries = Arrays.asList(file.listFiles());
                    for (int j = 0; j < subEntries.size(); j++)
                    {
                        size += subEntries.get(i).length();
                    }
                    System.out.println("Length: " + size);
                }
                else
                {
                    System.out.println("Length " + file.length());
                }
                System.out.println("Absolute Path: " + file.getPath());
            }
        }
    }
}
