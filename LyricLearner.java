import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

public class LyricLearner {

    public static void main(String[] args) {
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

        Random rand = new Random();

        while(true)
        {
            tweetLines(artists.get(rand.nextInt(artists.size())));
            try {
                System.out.println("Sleeping for 1 hour...");
                Thread.sleep(3600000); // every 1.5 minutes
                // Thread.sleep(10000); // every 10 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static void tweetLines(String artist) {
        String line;
        int i = 0;
        Random rand = new Random();
        try {

                    InputStream fis = new FileInputStream(artist.toLowerCase() + "OUT.txt");
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));

                while ((br.readLine()) != null) {
                    i++;
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
        i = rand.nextInt(i);
        int z = 0;
        try {

                    InputStream fis = new FileInputStream(artist.toLowerCase() + "OUT.txt");
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));

                while ((line = br.readLine()) != null) {
                    // Deal with the line
                    z++;
                    if(z==i)
                    {
                        if(line.indexOf('-')!=-1  || line.indexOf('&')!=-1)
                        {
                            i++;
                            continue;
                        }
                        line = line.trim();
                        
                        
                        
                        sendTweet(line, artist);
                        System.out.println("Tweeting: " + artist + " be like \"" + line + "\"");
                        break;
                    }


                }

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private static void sendTweet(String line, String artist) {
        Twitter twitter = TwitterFactory.getSingleton();
        Status status;



        try {
            status = twitter.updateStatus(artist + " be like \"" + line + "\"");
            //System.out.println(artist + " be like \"" + line + "\"");
        } catch (TwitterException e) {;
            e.printStackTrace();
        }
    }

}
