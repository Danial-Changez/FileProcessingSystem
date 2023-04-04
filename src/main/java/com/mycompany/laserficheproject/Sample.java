/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.laserficheproject;

import com.laserfiche.api.client.model.AccessKey;
import com.laserfiche.repository.api.RepositoryApiClient;
import com.laserfiche.repository.api.RepositoryApiClientImpl;
import com.laserfiche.repository.api.clients.impl.model.Entry;
import com.laserfiche.repository.api.clients.impl.model.ODataValueContextOfIListOfEntry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author 
 */
public class Sample {
    
    
    //Method which could be used to read and save files within a specified directory
    /*
    public void saveFile(File fileReceived){
        
        if (fileReceived.isDirectory()){
            File[] files = fileReceived.listFiles();
            if (files != null){
                for (File file: files){
                    
                }
            }
        } else {
            
        }
    } */

    public static void main(String[] args) {
        String servicePrincipalKey = "5V-BSOZ4ZRXSPiPMSOiW";
        String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiNDg2NTYxNWMtMjVlMS00ZmMyLTk4YWYtNzYyZGMzMWRlYTcwIiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogIjAxZUNsTmhaYVB2QWY2LXdRcDh1amVQRnkyRGNybi12NXBDRXVFT3ZzaHciLAoJCSJ4IjogInZqUzBPUFZBTjRWWjlMU3JQbDQtWHVJZFpBcjR2SmZnOEI3ZFhlV3c1Qk0iLAoJCSJ5IjogIkdxaXBGUEFXdzBiX3pQQ3VKWHhyeFZod0FrWW9SVHJzekNBMDcxUWY2b3ciLAoJCSJkIjogIjFjT1JjamJrMmhnUWN0dnVWSGp0M1pzUHJZZHVKejRhYWFOemYwSS1KSXMiLAoJCSJpYXQiOiAxNjc3Mjk3NjM2Cgl9Cn0=";
		String repositoryId = "r-0001d410ba56";
        AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);

        RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(
                servicePrincipalKey, accessKey);

        // Get information about the ROOT entry, i.e. entry with ID=1
        int rootEntryId = 1;
        Entry entry = client.getEntriesClient()
                .getEntry(repositoryId, rootEntryId, null).join();

        System.out.println(
                String.format("Entry ID: %d, Name: %s, EntryType: %s, FullPath: %s",
                        entry.getId(), entry.getName(), entry.getEntryType(), entry.getFullPath()));

        // Get information about the child entries of the Root entry
        ODataValueContextOfIListOfEntry result = client
                .getEntriesClient()
                .getEntryListing(repositoryId, rootEntryId, true, null, null, null, null, null, "name", null, null, null).join();
        List<Entry> entries = result.getValue();
        //ArrayList<String> entriesString = new ArrayList<String>();
        int counter = 1;
        for (Entry childEntry : entries) {
            System.out.println(
                    String.format("Child Entry ID: %d, Name: %s, EntryType: %s, FullPath: %s",
                            childEntry.getId(), childEntry.getName(), childEntry.getEntryType(), childEntry.getFullPath()));
        

        String downloadedFilesPath = "C:\\Users\\hassa\\Documents\\NetBeansProjects\\Project";
        // Download an entry
        int entryIdToDownload = childEntry.getId();
        String FILE_NAME = "DownloadedFile" + counter;
        Consumer<InputStream> consumer = inputStream -> {
            File exportedFile = new File(downloadedFilesPath, FILE_NAME);
            
            
            //Determine whether targed being handled is file or folder
            if (exportedFile.isDirectory()){
                int counterWithin = 1;
                File[] fileWithin = exportedFile.listFiles();
                if (fileWithin != null){
                    for (File file : fileWithin){
                        if (file.isFile()){
                            String fileName = file.getName() + counterWithin;
                            counterWithin++;
                            File outputFile = new File(downloadedFilesPath, fileName);
                            try (FileInputStream inputStreamWithin = new FileInputStream(file);
                                    FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                                byte[] buffer = new byte[1024];
                                int length;
                                while ((length = inputStreamWithin.read(buffer)) > 0) {
                                    outputStream.write(buffer, 0, length);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                //counterWithin++;
            }
            
            try (FileOutputStream outputStream = new FileOutputStream(exportedFile)) {
                byte[] buffer = new byte[1024];
                while (true) {
                    int length = inputStream.read(buffer);
                    if (length == -1) {
                        break;
                    }
                    outputStream.write(buffer, 0, length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        
        client.getEntriesClient()
                .exportDocument(repositoryId, entryIdToDownload, null, consumer)
                .join();
        counter++;
        }
        client.close();
    }
}
