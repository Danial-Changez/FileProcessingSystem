/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laserficheProjectEntries;

import classes.filters;
import com.laserfiche.api.client.model.AccessKey;
import com.laserfiche.repository.api.RepositoryApiClient;
import com.laserfiche.repository.api.RepositoryApiClientImpl;
import com.laserfiche.repository.api.clients.impl.model.Entry;
import com.laserfiche.repository.api.clients.impl.model.ODataValueContextOfIListOfEntry;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author danial
 */
public class entriesProgram extends filters
{
    public static void main(String[] args)
    {
        // Declare and initialize variables
        ArrayList<File> entries = new ArrayList<File>();
        entriesProgram filter = new entriesProgram();
        JSONParser parser = new JSONParser();
        JSONArray inputEntries;
        JSONObject inputEntry;
        String repId = "";
        String inputType = "";
        String path = "";
        String Operator = "";
        String suffix = "";
        String Key = "";
        int entryId = 0;
        int len = 0;
        int Lines = 0;
        int Min = 0;
        int Max = 0;

        try
        {
            // Parse the JSON file
            Object obj = parser.parse(new FileReader("C:\\Users\\dania\\OneDrive\\Desktop\\Test Scenario.json"));
            JSONObject jsonObj = (JSONObject) obj;

            // Get the processing elements array
            JSONArray processingElements = (JSONArray) jsonObj.get("processing_elements");

            // Loop through each processing element
            for (int i = 0; i < processingElements.size(); i++)
            {
                JSONObject processingElement = (JSONObject) processingElements.get(i);

                // Get the type of method
                String type = (String) processingElement.get("type");

                // Get the type of input entry
                if (i == 0)
                {
                    // Get the input entries
                    inputEntries = (JSONArray) processingElement.get("input_entries");
                    inputEntry = (JSONObject) inputEntries.get(0);
                    inputType = (String) inputEntry.get("type");

                    // Get the path (if it's a local input)
                    if (inputType.equals("local"))
                    {
                        path = (String) inputEntry.get("path");
                    }
                    //Get repositoryId and entryId (if it's a remote input)
                    else
                    {
                        repId = (String) inputEntry.get("repositoryId");
                        entryId = Integer.parseInt(inputEntry.get("entryId").toString());
                    }
                    // Get the list of entries based on the input type and path
                    entries = listOfEntries(repId, entryId, path, inputType);
                }

                // Get the parameters array
                JSONArray parameters = (JSONArray) processingElement.get("parameters");

                // Loop through each parameter
                for (Object param : parameters)
                {
                    JSONObject parameter = (JSONObject) param;

                    // Get the name and value of the parameter
                    String paramName = (String) parameter.get("name");
                    String paramValue = (String) parameter.get("value");

                    // Use a switch statement to set the value of the appropriate variable
                    switch (paramName)
                    {
                        case "Length":
                            len = Integer.parseInt(paramValue);
                            break;
                        case "Operator":
                            Operator = paramValue;
                            break;
                        case "Key":
                            Key = paramValue;
                            break;
                        case "Min":
                            Min = Integer.parseInt(paramValue);
                            break;
                        case "Max":
                            Max = Integer.parseInt(paramValue);
                        case "Suffix":
                            suffix = paramValue;
                            break;
                        case "Lines":
                            Lines = Integer.parseInt(paramValue);
                            break;
                    }
                }

                // Use a switch statement to call the appropriate method based on the type of processing element
                switch (type)
                {
                    case "Name":
                        entries = filter.Name(entries, Key);
                        break;
                    case "Length":
                        entries = filter.Length(entries, len, Operator);
                        break;
                    case "Content":
                        entries = filter.Content(entries, Key);
                        break;
                    case "Count":
                        entries = filter.Count(entries, Key, Min);
                        break;
                    case "Split":
                        entries = filter.Split(entries, Lines);
                        break;
                    case "List":
                        entries = filter.List(entries, Max);
                        break;
                    case "Rename":
                        entries = filter.Rename(entries, suffix);
                        break;
                    case "Print":
                        filter.print(entries, inputType, entryId);
                        break;
                }
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }

        //Deleting all extra files that were made
        for (File delEn : entries)
        {
            delEn.delete();
        }
    }

    /**
     * Retrieves a list of files from either a local directory or a remote
     * repository.
     *
     * @param repositoryId the ID of the remote repository (ignored if inType is
     * "local")
     * @param rootEntryId the ID of the root entry of the remote repository
     * (ignored if inType is "local")
     * @param path the path of the local directory (ignored if inType is not
     * "local")
     * @param inType the type of input, either "local" or "remote"
     */
    public static ArrayList<File> listOfEntries(String repositoryId, int rootEntryId, String path, String inType)
    {
        ArrayList<File> output = new ArrayList<File>();
        if (inType.equals("local")) // if the input is a local directory
        {
            File root = new File(path);
            if (root.isDirectory()) // if the root file is a directory
            {
                File[] rootContents = root.listFiles(); // get an array of all the files and directories within the root directory
                for (File element : rootContents) // for each file or directory in the root directory
                {
                    output.add(element); // add it to the output ArrayList
                }
            }
            else // if the root file is a single file (not a directory)
            {
                output.add(root); // add it to the output ArrayList
            }
        }
        else // if the input is a remote repository
        {
            // create a new access key for the repository
            String servicePrincipalKey = "5V-BSOZ4ZRXSPiPMSOiW";
            String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiNDg2NTYxNWMtMjVlMS00ZmMyLTk4YWYtNzYyZGMzMWRlYTcwIiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogIjAxZUNsTmhaYVB2QWY2LXdRcDh1amVQRnkyRGNybi12NXBDRXVFT3ZzaHciLAoJCSJ4IjogInZqUzBPUFZBTjRWWjlMU3JQbDQtWHVJZFpBcjR2SmZnOEI3ZFhlV3c1Qk0iLAoJCSJ5IjogIkdxaXBGUEFXdzBiX3pQQ3VKWHhyeFZod0FrWW9SVHJzekNBMDcxUWY2b3ciLAoJCSJkIjogIjFjT1JjamJrMmhnUWN0dnVWSGp0M1pzUHJZZHVKejRhYWFOemYwSS1KSXMiLAoJCSJpYXQiOiAxNjc3Mjk3NjM2Cgl9Cn0=";

            // Create an AccessKey object from the base64-encoded access key string
            AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);

            // Create a RepositoryApiClient object using the service principal key and the AccessKey object
            RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(servicePrincipalKey, accessKey);

            // Get the root entry of the repository using the client object
            Entry entry = client.getEntriesClient().getEntry(repositoryId, rootEntryId, null).join();

            // Get the list of entries in the root folder of the repository using the client object
            ODataValueContextOfIListOfEntry result = client
                    .getEntriesClient()
                    .getEntryListing(repositoryId, rootEntryId, true, null, null, null, null, null, "name", null, null, null).join();

            // Get the list of entries from the ODataValueContextOfIListOfEntry object
            List<Entry> entries = result.getValue();

            // Loop through the list of entries
            for (int i = 0; i < entries.size(); i++)
            {
                //Flag indicating whether the current entry is a folder or not
                boolean check = true;

                // Check if the entry is a document or a folder
                if (entries.get(i).getEntryType().toString().equals("Document"))
                {
                    // If it's a document, add a new file to the output list with the same name as the entry and ".txt" extension
                    output.add(new File(entries.get(i).getName() + ".txt"));
                }
                else
                {
                    // If it's a folder, add a new directory to the output list with the same name as the entry
                    output.add(new File(entries.get(i).getName()));
                }

                // Create a new directory with the same name as the current entry
                String folderName = entries.get(i).getName();
                File outputDirectory = new File(folderName);
                outputDirectory.mkdir();

                // If the entry is a folder, get the list of entries in the folder and download them using a loop
                if (entries.get(i).getEntryType().toString().equals("Folder"))
                {
                    ODataValueContextOfIListOfEntry val = client
                            .getEntriesClient()
                            .getEntryListing(repositoryId, entries.get(i).getId(), true, null, null, null, null, null, "name", null, null, null).join();

                    List<Entry> subEntries = val.getValue();
                    for (int j = 0; j < subEntries.size(); j++)
                    {
                        download(subEntries, j, client, repositoryId, outputDirectory, check);
                    }
                }
                else
                {
                    // If the entry is a document, download it using the download() method
                    check = false;
                    download(entries, i, client, repositoryId, outputDirectory, check);
                }
            }
            client.close();
        }
        return output;
    }

    /**
     * Downloads an entry from a given repository.
     *
     * @param en the list of entries to download
     * @param n the index of the entry to download
     * @param cl the repository API client to use for downloading
     * @param repID the ID of the repository to download from
     * @param outDir the directory to save the downloaded file to
     * @param check whether to check if the entry is a document or not
     */
    public static void download(List<Entry> en, int n, RepositoryApiClient cl, String repID, File outDir, Boolean check)
    {
        // Get the ID and name of the entry to download
        int entryIdToDownload = en.get(n).getId();
        String FILE_NAME = en.get(n).getName() + ".txt";

        // Define the consumer to save the downloaded file
        Consumer<InputStream> consumer = inputStream ->
        {
            // Determine where to save the downloaded file
            File exportedFile;
            if (en.get(n).getEntryType().toString().equals("Document") && check)
            {
                exportedFile = new File(outDir, FILE_NAME);
            }
            else
            {
                exportedFile = new File(FILE_NAME);
            }

            // Save the file
            try (FileOutputStream outputStream = new FileOutputStream(exportedFile))
            {
                byte[] buffer = new byte[1024];
                while (true)
                {
                    int length = inputStream.read(buffer);
                    if (length == -1)
                    {
                        break;
                    }
                    outputStream.write(buffer, 0, length);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };

        // Download the entry
        cl.getEntriesClient().exportDocument(repID, entryIdToDownload, null, consumer).join();
    }
}
