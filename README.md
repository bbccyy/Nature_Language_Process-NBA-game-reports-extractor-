Nature Language Processing
========
YIXUN WANG

* TOPIC: Information Extraction on NBA Scores Reports 
* Data Corpus: Yahoo Sports -> NBA -> Scores Reports 
* Programming Language: JAVA 
* External Library: JET 
* Source Code:
``` 
NLP/src/nlp/sportsextraction/Main.java
```
```
NLP/src/nlp/sportsextraction/ReadingFile.java 
```
```
NLP-Measure/src/Measure.java 
```

* JET Files: [Make sure `Jet.dataPath` in `*.jet` ﬁle is set correctly] 
```
jet/props/nbaScores.jet 
jet/props/point.jet 
jet/data/chunkPatternsSP.txt 
jet/data/scoresPatterns.txt 
jet/data/sportConcepts.hrc 
jet/data/pointsPatterns.txt 
jet/data/scoreConcepts.hrc
```

## Input Data and Formation: 
The Data corpus were extracted from Yahoo Sports by hand. There are `57` individual news reports as a whole and most of which contains more than `400` words or `1700` characters on average, that is, for each article it contains the whole report it self, not only a part. However I exclude the report’s title, or else I can play a cheat on it since a title usually reﬂects most of information that I need.
Here is a sample input of two news:
```
<TEXT> 
The content of the ﬁrst NBA report is here.
</TEXT> 
<TEXT> 
The content of another NBA report is here.
</TEXT> 
```

## Training Patterns: 
I use six tenth of the total data corpus to train my events patterns and then place a test on the whole 57 news reports. There are three major training targets of this project:

1) to get the HOME COURT 
Luckily enough, I found Yahoo Sports always put the game location at the very beginning, and I can simply extract the rough location information over there and analysis it by comparing it with my database. However, In case of no such a convenience, I would also ﬁrst get a set of possible location by using location-Pattern, second compare such locations with what I get from TEAM and SCORES Pattern. The TEAM information must be linked with the wanted location so that it’s possible to get it correctly.

2) to get the TEAM and SCORES

This is one of the main purpose of my project, and there are three kind of informations I 
need to obtain: WIN-TEAM, LOSE-TEAM and SCORES since these informations are almost 
always show up together, such as: 
```
TEAM-A win/beat TEAM-B with SCORES xxx-xxx
``` 
or 
```
TEAM-A give TEAM-B a loss with SCORES xxx-xxx
```

However there are also many more complexed combinations which requires a more careful 
Pattern construction: 
```
TEAM-A champions to a SCORES xxx-xxx victory over TEAM-B
``` 
or 
```
TEAM-A went into the All-Star break with their fourth victory in ﬁve games, pounding the TEAM-B SCORES xxx-xxx
``` 

One can deﬁne numerous patterns to cover most combinations, my way to break through this question, on the other hand, is that by: ﬁrst locating the key cWIN words like WIN or VICTORY 
then ﬁnding out the corresponding TEAM tag either on both sides or on the same side, and ﬁnally take the struct of sentence into consideration. I can somehow predict TEAMs and SCOREs correctly and efﬁciently. 
My pattern ﬁle will be attached to the end of this report. 

3) to get the PLAYER WITH HIGHEST POINTS 
This is trying to get the possible highest POINTS in a given game, and the corresponding PLAYER. Again, there are numbers of different ways of saying: 
```
PLAYER scored xx POINTS
``` 
For instance, we can say: 
```
PLAYER was the leading scorer with xx POINTS.
``` 
or 
```
PLAYER scored xx of his season-high xx POINTS.
``` 
This time I’m trying to cover these situations carefully and totally with various of patterns. 

## What do I do with my JAVA code 
* Parsing the text into a set of sentences and store them in proper data structure. This allows me to eliminate sentences that doesn’t contains any KEY word like TEAM name or the concept of WIN and LOSE. Notice that, I can not simple separate the whole context by period, because there are names like “D.J. Augustin” also contains “.” which requires additional processing.


* Applying JET Pattern Matching on each sentence. There are two kind of Patterns, one for ﬁnding out TEAMS and SCORES, the other is for searching a PLAYER and his HIGHEST POINTS. Usually this step will generate a lot of redundant values that leaves to me for further processing.


* Filter redundant informations. for instance, I can make a use of pre-acquired LOCATION to help me choosing the patterns that contains the right TEAMS and SCORES, or I can use my database to eliminate a ENAMEX that doesn’t belong to a Person but belong to a TEAM or a LOCATION when I dealing with PLAYER and POINTS information.


* Measure the output and get the Precision & Recall. The standard output of extraction as well as my hand-made evaluation are listed to the end of the report. Each article contains 6 points of informations[LOCATION, WIN-TEAM, LOSE-TEAM, SCORES, PLAYER, POINTS]so that there are 57*6 = 342 pieces of informations need to be check. The measurement including a totally Precision and Recall, and for each of the three extraction targets, I also calculated their corresponding Precision and Recall values.
