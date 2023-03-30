package com.mycompany.finalproject;

import java.util.ArrayList;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author danial
 */
public class myFilters
{
    public static void main(String[] args)
    {
        System.out.println("Hello World!");
    }

    public ArrayList<String> Name(ArrayList<String> entry, String Key)
    {
        ArrayList<String> output = new ArrayList<String>();
        for (int i = 0; i < entry.size(); i++)
        {
            if (entry.get(i).contains(Key))
                output.add(entry.get(i));
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

    public ArrayList<File> List(ArrayList<String> entry, int Max)
    {
        ArrayList<File> output = new ArrayList<File>();
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
}
