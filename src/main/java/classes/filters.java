package classes;

import java.util.ArrayList;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author danial
 */
public class filters
{
    public static void main(String[] args)
    {

    }

    public void order()
    {
        String[] options = new String[]
        {
            "Name", "Length", "Content", "Count", "Split", "List", "Rename", "Print"
        };
    }

    public static List<File> renameFile(List<File> originalFileNames, String suffix)
    {
        int countFile = originalFileNames.size(); //counter for setting maximum bound in the for loop
        List<File> renamedFiles = new ArrayList<>(); //array list for the new file names after appending the suffix string
        String fileName; //string to store the current file name while itterating through the loop
        int dotIndex; //variable used to store the index of the "." in the filename string
        String newFileName; //string used to store the new filer name after appending the suffix string

        for (int i = 0; i < countFile - 1; i++)
        { // loop ro itterate through the array list containing the original file names
            fileName = originalFileNames.get(i).getName();// applying the getName() method in the File library
            dotIndex = fileName.lastIndexOf('.'); //saves the last position of the "." character into the dotindex variable
            newFileName = fileName.substring(0, dotIndex) + suffix + fileName.substring(dotIndex); //using the substring() method to concatenate the suffix string before the ".txt"
            File renamedFile = new File(originalFileNames.get(i).getParent(), newFileName); //using the getParent() method to set the path of the original file to the new renamed file
            renamedFiles.add(renamedFile); // adding the renamed file into the renamedFiles list
        }
        return renamedFiles; // returning renamedFiles list
    }

    public List<String> Name(ArrayList<String> entry, String Key)
    {
        List<String> output = new ArrayList<String>();
        for (int i = 0; i < entry.size(); i++)
        {
            if (entry.get(i).contains(Key))
                output.add(entry.get(i));
        }
        return output;
    }

    public List<String> Length(ArrayList<String> entry, long Length, String Operator)
    {
        List<String> output = new ArrayList<String>();
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

    public List<File> List(ArrayList<String> entry, int Max)
    {
        List<File> output = new ArrayList<File>();
        for (int i = 0; i < entry.size(); i++)
        {
            File directory = new File(entry.get(i));
            if (directory.isDirectory())
            {
                List<File> filesList = Arrays.asList(directory.listFiles());
                for (int j = 0; j < Max; j++)
                    output.add(filesList.get(j));
            }
        }
        return output;
    }

    public static void print(List<File> renamedFiles)
    {
        int countFile = renamedFiles.size(); //counter for setting maximum bound in the for loop
        for (int i = 0; i < countFile - 1; i++)
        { // loop ro itterate through the array list containing the renamed file names

            System.out.println();
            System.out.println("Name: " + renamedFiles.get(i).getName()); //printing the new fileName
            System.out.println("Length: " + renamedFiles.get(i).length()); //printing the length of the file at index i of the  renamedFiles arraylist
            System.out.println("Absolute Path: " + renamedFiles.get(i).getAbsolutePath()); //printing the path of the renamedFiles entity at index i (shoul dbe the same path as the parent file)
            System.out.println();//printing newline to make the output more clear
        }
    }
}
