package classes;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author danial
 */
public class filters {

    public static void main(String[] args) {

    }

    public ArrayList<String> Name(ArrayList<String> entry, String Key) {
        ArrayList<String> output = new ArrayList<String>();
        for (int i = 0; i < entry.size(); i++) {
            if (entry.get(i).contains(Key)) {
                output.add(entry.get(i));
            }
        }
        return output;
    }

    public ArrayList<String> Length(ArrayList<String> entry, long Length, String Operator) {
        ArrayList<String> output = new ArrayList<String>();
        for (int i = 0; i < entry.size(); i++) {
            File file = new File(entry.get(i));
            if (file.isFile()) {
                switch (Operator) {
                    case "EQ":
                        if (entry.get(i).length() == Length) {
                            output.add(entry.get(i));
                        }
                        break;
                    case "NEQ":
                        if (entry.get(i).length() != Length) {
                            output.add(entry.get(i));
                        }
                        break;
                    case "GT":
                        if (entry.get(i).length() > Length) {
                            output.add(entry.get(i));
                        }
                        break;
                    case "GTE":
                        if (entry.get(i).length() >= Length) {
                            output.add(entry.get(i));
                        }
                        break;
                    case "LT":
                        if (entry.get(i).length() < Length) {
                            output.add(entry.get(i));
                        }
                        break;
                    case "LTE":
                        if (entry.get(i).length() <= Length) {
                            output.add(entry.get(i));
                        }
                        break;
                }
            }
        }
        return output;
    }

    public ArrayList<String> Split(ArrayList<String> entry, int Lines) {
        ArrayList<String> result = new ArrayList<String>();
        for (String e : entry) {
            File file = new File(e);

            if (file.isDirectory()) {
                // ignore directories
                continue;
            }

            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                int partNumber = 1;
                String line = reader.readLine();

                while (line != null) {
                    String partName = file.getName() + ".part" + partNumber + ".txt";
                    FileWriter writer = new FileWriter(partName);

                    for (int i = 0; i < Lines && line != null; i++) {
                        writer.write(line + "\n");
                        line = reader.readLine();
                    }
                    writer.close();
                    result.add(partName);
                    partNumber++;
                }
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        // return the list of generated files
        return result;
    }
    
//    //Need to adjust
//    public ArrayList<String> List(ArrayList<String> entry, int Max) {
//        ArrayList<String> output = new ArrayList<String>();
//        for (int i = 0; i < entry.size(); i++) {
//            File directory = new File(entry.get(i));
//            if (directory.isDirectory()) {
//                ArrayList<File> filesList = Arrays.asList(directory.listFiles());
//                for (int j = 0; j < Max; j++) {
//                    output.add(filesList.get(j));
//                }
//            }
//        }
//        return output;
//    }

    public static ArrayList<String> rename(ArrayList<String> originalFileNames, String suffix) {
        int countFile = originalFileNames.size(); // counter for setting maximum bound in the for loop
        ArrayList<String> renamedFiles = new ArrayList<>(); // array list for the new file names after appending the suffix string
        String fileName; // string to store the current file name while iterating through the loop
        int dotIndex; // variable used to store the index of the "." in the filename string
        String newFileName; // string used to store the new file name after appending the suffix string

        for (int i = 0; i < countFile; i++) { // loop to iterate through the array list containing the original file names
            fileName = originalFileNames.get(i); // get the current file name from the original file names list
            dotIndex = fileName.lastIndexOf('.'); // saves the last position of the "." character into the dotIndex variable
            newFileName = fileName.substring(0, dotIndex) + suffix + fileName.substring(dotIndex); // using the substring() method to concatenate the suffix string before the ".txt"
            renamedFiles.add(newFileName); // adding the renamed file name into the renamedFiles list
        }
        return renamedFiles; // returning renamedFiles list of type ArrayList<String>
    }
    
    public static void print(List<File> renamedFiles) {
        int countFile = renamedFiles.size(); //counter for setting maximum bound in the for loop
        for (int i = 0; i < countFile - 1; i++) { // loop ro itterate through the array list containing the renamed file names

            System.out.println();
            System.out.println("Name: " + renamedFiles.get(i).getName()); //printing the new fileName
            System.out.println("Length: " + renamedFiles.get(i).length()); //printing the length of the file at index i of the  renamedFiles arraylist
            System.out.println("Absolute Path: " + renamedFiles.get(i).getAbsolutePath()); //printing the path of the renamedFiles entity at index i (shoul dbe the same path as the parent file)
            System.out.println();//printing newline to make the output more clear
        }
    }
}
