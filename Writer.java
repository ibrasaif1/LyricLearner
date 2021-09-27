import java.io.*;
import java.lang.*;
import java.util.*;
/*
 * CS 314H Assignment 2 - Random Writing
 *
 * Your task is to implement this RandomWriter class
 */
public class Writer implements TextProcessor {
    //INSTANCE FIELDS
    private int k;
    private HashMap<String, ArrayList<String>> map;
    private String txt;
    private String seed;

    public static void main(String[] args)
    {
        InputStream inputStream = Writer.class.getResourceAsStream("/artists.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ArrayList<String> artists = new ArrayList<String>();

        try {
            while(reader.ready()) {
                String line = reader.readLine();
                System.out.println(line);
                artists.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        artists.remove(0);

        for(String x: artists)
        {
            System.out.println("processing " + x);
            String[] s = new String[4];
            s[0] = "/" + x.toLowerCase() + ".txt";
            s[1] = x.toLowerCase() + "OUT.txt";
            s[2] = "8";
            s[3] = "100000";
            driver(s);
            System.out.println("finished " + x);
        }

    }

    public static void driver(String[] args) {
        //Check if k is not negative
        if (Integer.parseInt(args[2])<0)
        {
            System.out.println("The level of analysis cannot be negative");
            return;
        }
        //Check that length isn't negative
        if (Integer.parseInt(args[3])<0)
        {
            System.out.println("The output length cannot be negative");
            return;
        }
        //read the file and catch potential errors
        TextProcessor writer = createProcessor(Integer.parseInt(args[2]));
        try{
            InputStream inputStream = Writer.class.getResourceAsStream(args[0]);
            BufferedReader testReader = new BufferedReader(new InputStreamReader(inputStream));
            int character = -1;
            StringBuffer test = new StringBuffer("");
            //append each character of the text file to the test stringBuffer
            while ((character = testReader.read()) != -1)
            {
                test.append((char)character);
            }
            //makes sure k is less than the length of the text
            if (test.length()<=Integer.parseInt(args[2]))
            {
                System.out.println("Source is not long enough given the level of analysis");
                return;
            }
            //run readText
            writer.readText(args[0]);
        } catch (FileNotFoundException e)
        {
            //catch the error if the file isn't there
            System.err.println("The input file was not found\n" + e);
            return;
        }
        catch (IOException e)
        {
            //catch any other IO errors
            System.err.println("An IOException occurred in the input file\n" + e);
            return;
        }

        try {
            //test writing to the file with an empty string
            FileWriter testWriter = new FileWriter(args[1]);
            testWriter.write("");
            //run writeText
            writer.writeText(args[1], Integer.parseInt(args[3]));
        }
        catch (IOException e)
        {
            //catches the IO error of the output file
            System.err.println("An IOException occurred in the output file\n" + e);
            return;
        }

    }

    // Unless you need extra logic here, you might not have to touch this method
    public static TextProcessor createProcessor(int level) {
      return new Writer(level);
    }

    //constructor sets all the default values and sets the k value from the parameter
    private Writer(int level) {
      k = level;
      map = new HashMap<String, ArrayList<String>>();
      txt = "";
      seed = "";
    }


    public void readText(String inputFilename) throws IOException {
        //reads in file to a StringBuffer and appends that to the class variable txt
        InputStream inputStream = Writer.class.getResourceAsStream(inputFilename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        int character = -1;
        StringBuffer text = new StringBuffer("");
        while ((character = reader.read()) != -1)
        {
            text.append((char)character);
        }
        txt = text.toString();
        //creates a random seed of length k from the text, the stores that in the class variable
        Random randomGen = new Random();
        int start = randomGen.nextInt(txt.length()-k);
        seed = txt.substring(start,start+k);
    }



    public void writeText(String outputFilename, int length) throws IOException {


        //open the output file to write
        FileWriter writer = new FileWriter(outputFilename);
        String result = "";
        Random randomGen = new Random();
        //exit if the length is 0
        if (length==0)
        {
            writer.write("");
            writer.close();
            return;
        }
        //add the randomly generated seed from readText
        result += seed;
        //for cases where the expected length is less than k, so the seed is already big enough
        if (result.length()>=length)
        {
            //make result a shortened version of seed to match the expected length, then write that
            writer.write(result.substring(0,length));
            //System.out.println(result.substring(0,length));
            writer.close();
            return;
        }
        //special case if the level of analysis is 0
        if (k==0)
        {
            //create an arrayList where each index stores a single character from the text
            ArrayList<String> nextChar = new ArrayList<String>();
            for (int i = 0;i<txt.length();i++)
            {
                nextChar.add(txt.substring(i,i+1));

            }
            //create the result file by appending randomly chosen characters until the desired size has been achieved

            for (int i = 0; i<length;i++)
                result += nextChar.get(randomGen.nextInt(nextChar.size()));
            //once the result is completed, write the file and close
            writer.write(result);
            System.out.println(result);
            writer.close();
            return;
        }
        //k is subtracted from length because the initial seed was already added to the result string
        for (int i = 0; i<length-k;i++)
        {

            //first, check if the map already has the seed as a key and that there are values folllwing it
            if (map.containsKey(seed) && map.get(seed).size()!=0)
            {
                //copy the array of possible next characters
                ArrayList<String> values = map.get(seed);
                //randomly pick a character and add it on
                int nextCharacterIndex = randomGen.nextInt(values.size());
                result += values.get(nextCharacterIndex);
            } else {
                ArrayList<String> values = new ArrayList<String>();
                //if the seed isn't already stored, you have to make the key/value pair in the map
                //this for loop goes through the text file and finds all following characters given the seed
                for (int x = 0; x<txt.length()-k;x++)
                {
                    if(txt.substring(x,x+k).equals(seed))
                    {
                        //this makes sure that the seed isn't the last group of characters
                        if (x+k+1<=txt.length())
                            values.add(txt.substring(x+k,x+k+1));
                    }
                }
                //add the arraylist and seed to the map
                map.put(seed,values);
                //this if clause is if there are no occurrences of the seed within the file
                if (values.size()==0)
                {
                    //increments i to account for a new seed being added
                    i+=k-1;
                    //seed is randomly generated then added to the result String
                    int start = randomGen.nextInt(txt.length()-k);
                    seed = txt.substring(start,start+k);
                    result += seed;
                    //this iteration of the for loop is exited (since the seed has already been added)
                    continue;
                }
                //now that the seed has been added to the map, you can pick a random value from the possible value ArrayList
                int nextCharacterIndex = randomGen.nextInt(values.size());
                //add the random new character to the result String
                result += values.get(nextCharacterIndex);
            }
            //get the new seed
            seed = result.substring(result.length()-k,result.length());

        }
        //write the result and close the file
        writer.write(result);
        //System.out.println(result);
        writer.close();

    }
}
