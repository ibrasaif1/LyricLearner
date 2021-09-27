import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class Interpreter {
    public static void main(String[] args)
    {
        String all;
        for(int i = 2; i<3;i++) {
            all="";
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader("src/" + i + ".txt"));
                String line = reader.readLine();
                while (line != null) {
                    all += line;
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println(all);
            String[] words = all.split(" ");
            NLP.init();
            double total = 0;
            int z = 0;
            for(String word:words)
            {
                z++;
                //System.out.println(z);
                total += (double)NLP.findSentiment(word);
            }

            total/=(double)words.length;
            System.out.println("Sentiment for " + i + ": " + total);
        }

    }
}

class NLP {
    static StanfordCoreNLP pipeline;

    public static void init() {
        pipeline = new StanfordCoreNLP("MyPropFile.properties");
    }

    public static int findSentiment(String tweet) {

        int mainSentiment = 0;
        if (tweet != null && tweet.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(tweet);
            for (CoreMap sentence : annotation
                    .get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }

            }
        }
        return mainSentiment;
    }
}