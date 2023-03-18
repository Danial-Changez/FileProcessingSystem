package com.mycompany.finalproject;

import java.util.ArrayList;

/**
 *
 * @author danial
 */
public class NameLengthFilters
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
            if (entry.get(i).contains("."))
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
}
