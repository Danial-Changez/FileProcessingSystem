/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laserficheProjectEntries;

import com.laserfiche.api.client.model.AccessKey;
import com.laserfiche.repository.api.RepositoryApiClient;
import com.laserfiche.repository.api.RepositoryApiClientImpl;
import com.laserfiche.repository.api.clients.impl.model.Entry;
import com.laserfiche.repository.api.clients.impl.model.ODataValueContextOfIListOfEntry;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.*;

/**
 *
 * @author
 */
public class entriesProgram
{

    public static void main(String[] args)
    {
        String path = "remote";

        String servicePrincipalKey = "5V-BSOZ4ZRXSPiPMSOiW";
        String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiNDg2NTYxNWMtMjVlMS00ZmMyLTk4YWYtNzYyZGMzMWRlYTcwIiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogIjAxZUNsTmhaYVB2QWY2LXdRcDh1amVQRnkyRGNybi12NXBDRXVFT3ZzaHciLAoJCSJ4IjogInZqUzBPUFZBTjRWWjlMU3JQbDQtWHVJZFpBcjR2SmZnOEI3ZFhlV3c1Qk0iLAoJCSJ5IjogIkdxaXBGUEFXdzBiX3pQQ3VKWHhyeFZod0FrWW9SVHJzekNBMDcxUWY2b3ciLAoJCSJkIjogIjFjT1JjamJrMmhnUWN0dnVWSGp0M1pzUHJZZHVKejRhYWFOemYwSS1KSXMiLAoJCSJpYXQiOiAxNjc3Mjk3NjM2Cgl9Cn0=";
        String repositoryId = "r-0001d410ba56";
        AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);

        RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(servicePrincipalKey, accessKey);

        // Get information about the ROOT entry, i.e. entry with ID=1
        // Accessed from provided JSON. Need to implement.
        int rootEntryId = 1;
        Entry entry = client.getEntriesClient().getEntry(repositoryId, rootEntryId, null).join();

        // Get information about the child entries of the Root entry
        ODataValueContextOfIListOfEntry result = client
                .getEntriesClient()
                .getEntryListing(repositoryId, rootEntryId, true, null, null, null, null, null, "name", null, null, null).join();

        List<Entry> entries = result.getValue();
        ArrayList<String> entriesString = new ArrayList<String>();

        for (int i = 0; i < entries.size(); i++)
        {
            System.out.println(
                    String.format("Child Entry ID: %d, Name: %s, EntryType: %s, FullPath: %s",
                            entries.get(i).getId(), entries.get(i).getName(), entries.get(i).getEntryType(), entries.get(i).getFullPath()));
            
            String folderName = entries.get(i).getName();
            File outputDirectory = new File(folderName);
            System.out.println(folderName);
            
            if (entries.get(i).getEntryType().toString().equals("Folder"))
            {
                ODataValueContextOfIListOfEntry val = client
                        .getEntriesClient()
                        .getEntryListing(repositoryId, entries.get(i).getId(), true, null, null, null, null, null, "name", null, null, null).join();
                
                List<Entry> subEntries = val.getValue();
                for (int j = 0; j < subEntries.size(); j++)
                {
                    download(subEntries, j, client, repositoryId, outputDirectory);
                    System.out.println("fold");
                }
            } 
            else
            {
                download(entries, i, client, repositoryId, outputDirectory);
                System.out.println("doc");
            }
        }
        client.close();
    }

    public static void download(List<Entry> en, int n, RepositoryApiClient cl, String repID, File outputDirectory)
    {
        int entryIdToDownload = en.get(n).getId();
        String FILE_NAME = outputDirectory + "\\" + en.get(n).getName();
        Consumer<InputStream> consumer = inputStream ->
        {
            File exportedFile = new File(FILE_NAME);
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
