/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package renameandprint;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hasah
 */
public class RenameAndPrint {
    
    public static List<File> renameFile(List<File> originalFileNames, String suffix){
        
        int countFile  = originalFileNames.size(); //counter for setting maximum bound in the for loop
        List<File> renamedFiles = new ArrayList<>(); //array list for the new file names after appending the suffix string
        String fileName; //string to store the current file name while itterating through the loop
        int dotIndex; //variable used to store the index of the "." in the filename string
        String newFileName; //string used to store the new filer name after appending the suffix string
        
        for (int i = 0; i < countFile - 1; i++) { // loop ro itterate through the array list containing the original file names
            fileName = originalFileNames.get(i).getName();// applying the getName() method in the File library
            dotIndex = fileName.lastIndexOf('.'); //saves the last position of the "." character into the dotindex variable
            newFileName = fileName.substring(0, dotIndex) + suffix + fileName.substring(dotIndex); //using the substring() method to concatenate the suffix string before the ".txt"
            File renamedFile = new File(originalFileNames.get(i).getParent(), newFileName); //using the getParent() method to set the path of the original file to the new renamed file
            renamedFiles.add(renamedFile); // adding the renamed file into the renamedFiles list
        }
        return renamedFiles; // returning renamedFiles list
    }
    
         public static void print(List<File> renamedFiles) {
            
            int countFile  = renamedFiles.size(); //counter for setting maximum bound in the for loop
         
            for (int i = 0; i < countFile - 1; i++){ // loop ro itterate through the array list containing the renamed file names
                
                System.out.println();
                System.out.println("Name: " + renamedFiles.get(i).getName()); //printing the new fileName
                System.out.println("Length: " + renamedFiles.get(i).length()); //printing the length of the file at index i of the  renamedFiles arraylist
                System.out.println("Absolute Path: " + renamedFiles.get(i).getAbsolutePath()); //printing the path of the renamedFiles entity at index i (shoul dbe the same path as the parent file)
                System.out.println();//printing newline to make the output more clear
            }
               
    }
        
        

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      //creating suffix string and initializing it to "_copy" as shown in the example in the assignment instructions
      String suffix = ("_copy");
      
      //creating a list to store the orogial file names before they are concatenated with the suffix 
      List<File> fileNames = new ArrayList<>();
      
      //populating the list contining the original file names
      fileNames.add(new File("file1.txt"));
      fileNames.add(new File("file2.txt"));
      fileNames.add(new File("file3.txt"));
      fileNames.add(new File("file4.txt"));
      fileNames.add(new File("file5.txt"));
      
      
        List<File> renamedFiles = renameFile(fileNames, suffix);
        print(renamedFiles);
      
    }
    
}
