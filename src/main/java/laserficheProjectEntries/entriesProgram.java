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
 * @author
 */
public class entriesProgram
{
    public static void main(String[] args)
    {
        JSONParser parser = new JSONParser();
        filters filter = new filters();
        String repId = "";
        int entryId = 0;
        String path = "";

        try
        {
            Object obj = parser.parse(new FileReader("C:\\Users\\dania\\OneDrive\\Desktop\\Test Scenario.json"));
            JSONObject jsonObj = (JSONObject) obj;

            // Get the processing elements array
            JSONArray processingElements = (JSONArray) jsonObj.get("processing_elements");

            // Loop through each processing element
            for (Object elem : processingElements)
            {
                JSONObject processingElement = (JSONObject) elem;

                // Get the type of method
                String type = (String) processingElement.get("type");
                System.out.println("Type: " + type);

                // Get the input entries array
                JSONArray inputEntries = (JSONArray) processingElement.get("input_entries");

                // Loop through each input entry
                JSONObject inputEntry = (JSONObject) inputEntries.get(0);

                // Get the type of input entry
                String inputType = (String) inputEntry.get("type");

                // Get the path (if it's a local input)
                if (inputType.equals("local"))
                {
                    path = (String) inputEntry.get("path");
                }

                //Get repositoryId and entryId (if it's a remote input)
                else
                {
                    repId = (String) inputEntry.get("repositoryId");
                    entryId = (int) inputEntry.get("entryId");
                }

                // Get the parameters array
                JSONArray parameters = (JSONArray) processingElement.get("parameters");
                ArrayList<String> entries = listOfEntries(repId, entryId, path, inputType);
                // Loop through each parameter
                for (Object param : parameters)
                {
                    JSONObject parameter = (JSONObject) param;

                    // Get the name and value of the parameter
                    String paramName = (String) parameter.get("name");
                    String paramValue = (String) parameter.get("value");

                    switch (type)
                    {
                        case "Name":
                            String Key = paramValue;
                            entries = filter.Name(entries, Key);
                            break;

                        case "Length":
                            int len = 0;
                            String Operator = "";

                            if (paramName.equals("Length"))
                            {
                                len = Integer.parseInt(paramValue);
                            }
                            else
                            {
                                Operator = paramValue;
                            }

                            entries = filter.Length(entries, len, Operator);
                            break;

                        case "Content":
                            Key = paramValue;
                            entries = filter.Content(entries, Key);
                            break;

                        case "Count":
                            int Min = 0;
                            Key = "";
                            if (paramName.equals("Key"))
                            {
                                Key = paramValue;
                            }

                            else
                            {
                                Min = Integer.parseInt(paramValue);
                            }
                            entries = filter.Count(entries, Key, Min);
                            break;

                        case "Split":
                            int Lines = Integer.parseInt(paramValue);
                            entries = filter.Split(entries, Lines);
                            break;

                        case "List":
                            int Max = Integer.parseInt(paramValue);
                            entries = filter.List(entries, Max);
                            break;

                        case "Rename":
                            String suffix = paramValue;
                            entries = filter.Rename(entries, suffix);

                        case "Print":
                            filter.print(entries, inputType, entryId);
                            break;
                    }
                }
            }
        }

        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> listOfEntries(String repositoryId, int rootEntryId, String path, String inType)
    {
        ArrayList<String> output = new ArrayList<String>();
        if (inType.equals("local"))
        {
            File root = new File(path);
            if (root.isDirectory())
            {
                File[] rootContents = root.listFiles();
                for (File element : rootContents)
                {
                    output.add(element.toString());
                }
            }
            else
            {
                output.add(root.toString());
            }
        }

        else
        {
            String servicePrincipalKey = "5V-BSOZ4ZRXSPiPMSOiW";
            String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiNDg2NTYxNWMtMjVlMS00ZmMyLTk4YWYtNzYyZGMzMWRlYTcwIiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogIjAxZUNsTmhaYVB2QWY2LXdRcDh1amVQRnkyRGNybi12NXBDRXVFT3ZzaHciLAoJCSJ4IjogInZqUzBPUFZBTjRWWjlMU3JQbDQtWHVJZFpBcjR2SmZnOEI3ZFhlV3c1Qk0iLAoJCSJ5IjogIkdxaXBGUEFXdzBiX3pQQ3VKWHhyeFZod0FrWW9SVHJzekNBMDcxUWY2b3ciLAoJCSJkIjogIjFjT1JjamJrMmhnUWN0dnVWSGp0M1pzUHJZZHVKejRhYWFOemYwSS1KSXMiLAoJCSJpYXQiOiAxNjc3Mjk3NjM2Cgl9Cn0=";
            AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);

            RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(servicePrincipalKey, accessKey);

            Entry entry = client.getEntriesClient().getEntry(repositoryId, rootEntryId, null).join();
            ODataValueContextOfIListOfEntry result = client
                    .getEntriesClient()
                    .getEntryListing(repositoryId, rootEntryId, true, null, null, null, null, null, "name", null, null, null).join();

            List<Entry> entries = result.getValue();
            boolean check = true;

            for (int i = 0; i < entries.size(); i++)
            {
                System.out.println(
                        String.format("Child Entry ID: %d, Name: %s, EntryType: %s, FullPath: %s",
                                entries.get(i).getId(), entries.get(i).getName(), entries.get(i).getEntryType(), entries.get(i).getFullPath()));
                output.add(entries.get(i).getName());

                String folderName = entries.get(i).getName();
                File outputDirectory = new File(folderName);
                outputDirectory.mkdir();

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
                    check = false;
                    download(entries, i, client, repositoryId, outputDirectory, check);
                }
            }
            client.close();
        }
        return output;
    }

    public static void download(List<Entry> en, int n, RepositoryApiClient cl, String repID, File outDir, Boolean check)
    {
        int entryIdToDownload = en.get(n).getId();
        String FILE_NAME = en.get(n).getName();

        Consumer<InputStream> consumer = inputStream ->
        {
            File exportedFile;

            if (en.get(n).getEntryType().toString().equals("Document") && check)
            {
                exportedFile = new File(outDir, FILE_NAME);
            }

            else
            {
                exportedFile = new File(FILE_NAME);
            }

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

        cl.getEntriesClient()
                .exportDocument(repID, entryIdToDownload, null, consumer)
                .join();
    }
}
