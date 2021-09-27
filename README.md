# LyricLearner
LyricLearner is a project I worked on with Arun Eswara that generates lyrics and analyzes artists' sentiments throughout their careers. LyricLearner parses artist lyrics via Genius' RESTful API in Python, interprets the lyrics and generates new lyrics via a Java program written within the Maven Framework, then uses Twitter's RESTful API to automatically send out tweets. LyricLearner posts to its Twitter account, @LyricLearner. 

There are two parts to the LyricLearner application: Lyric Generation and Album Sentiment Analysis. For Lyric Generation, the program utilizes Markov Chains to come up with a lyric based on all of the artist's past lyrics, which are parsed through using Python and Genius' RESTful API. A Markov Chain is a concept similar to predictive text on iMessage in which suggested words are chosen based on a weighted average of the words that most commonly follow it. The figure attached below helps with visualizing the idea.

![image](https://user-images.githubusercontent.com/76267788/134994907-a8187391-7330-43c0-91da-23a7d17b8712.png)

Once a lyric is generated, it is deployed to Twitter via the Twitter4j RESTful API. Here is an example of a Tweet LyricLearner generated:

![image](https://user-images.githubusercontent.com/76267788/134994937-529d6333-0888-410f-8267-3454bdd483a1.png)

The second function of LyricLearner is Album Sentiment Analysis. To deduce the "sentiment" (how positive/negative it is) of an album, LyricLearner uses Stanford's Natural Language Processing (NLP) library, which is based around an Artificial Neural Network (ANN) architecture. This Stanford framework functions by assigning arbitrary weights to nodes that designate a certain word's sentiment, then incrementally improving the accuracy by providing training data.

![image](https://user-images.githubusercontent.com/76267788/134994950-60786241-78cc-40e1-8091-9cad05e7e26d.png)

LyricLearner uses the Stanford Library to find the sentiment of each word in an artist's album, then averages those numbers to produce an aggregate sentiment for each album. Finally, I scaled the final value for each album from 0 to 100 to create a graph that shows how the artist's sentiment evolves with time through their discography.

For example, as his career progressed, Lil Wayne’s lyrics slowly transitioned from hard-hitting punchlines to more introspective songs in which he reflects on the extreme levels of fame he’s reached and how it’s impacted his relationships, as can be seen in this graph.

![image](https://user-images.githubusercontent.com/76267788/134994967-42248afb-862c-4017-866f-1f31407b896d.png)

If you have any comments or inquiries, feel free to reach out to ibrahim.saifullah1@gmail.com or arun22@utexas.edu

This article can also be found at https://www.linkedin.com/pulse/lyriclearner-ai-music-generator-interpreter-ibrahim-saifullah/
