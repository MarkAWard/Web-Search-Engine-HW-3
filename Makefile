JCC = javac
JVM = java
JFLAGS = -classpath "library/*"
INDEXFLAGS = -classpath "src:library/*" 
RUNFLAGS = -classpath "src:library/*" -Xmx512m
MININGFLAGS = -classpath "src:library/*"

.java.class:
	$(JCC) $(JFLAGS) $*.java

CLASSES = \
	src/edu/nyu/cs/cs2580/DocumentFull.java \
	src/edu/nyu/cs/cs2580/IndexerInvertedDoconly.java \
	src/edu/nyu/cs/cs2580/RankerFavorite.java \
	src/edu/nyu/cs/cs2580/DocumentIndexed.java \
	src/edu/nyu/cs/cs2580/IndexerInvertedOccurrence.java \
	src/edu/nyu/cs/cs2580/RankerFullScan.java \
	src/edu/nyu/cs/cs2580/Document.java \
	src/edu/nyu/cs/cs2580/Indexer.java \
	src/edu/nyu/cs/cs2580/Ranker.java \
	src/edu/nyu/cs/cs2580/Evaluator.java \
	src/edu/nyu/cs/cs2580/QueryHandler.java \
	src/edu/nyu/cs/cs2580/ReadCorpus.java \
	src/edu/nyu/cs/cs2580/Grader.java \
	src/edu/nyu/cs/cs2580/Query.java \
	src/edu/nyu/cs/cs2580/ScoredDocument.java \
	src/edu/nyu/cs/cs2580/IndexerFullScan.java \
	src/edu/nyu/cs/cs2580/QueryPhrase.java \
	src/edu/nyu/cs/cs2580/SearchEngine.java \
	src/edu/nyu/cs/cs2580/IndexerInvertedCompressed.java \
	src/edu/nyu/cs/cs2580/RankerConjunctive.java \
	src/edu/nyu/cs/cs2580/Stemmer.java \
	src/edu/nyu/cs/cs2580/Spearman.java \
	src/edu/nyu/cs/cs2580/Bhattacharyya.java \


default:
	$(JCC) $(JFLAGS) src/edu/nyu/cs/cs2580/*.java

mining:
	$(JVM) $(MININGFLAGS) edu.nyu.cs.cs2580.SearchEngine --mode=mining --options=conf/engine.conf

index:
	$(JVM) $(INDEXFLAGS) edu.nyu.cs.cs2580.SearchEngine --mode=index --options=conf/engine.conf

run:
	$(JVM) $(RUNFLAGS) edu.nyu.cs.cs2580.SearchEngine --mode=serve --port=25808 --options=conf/engine.conf 

spearman:
	$(JVM) $(RUNFLAGS) edu.nyu.cs.cs2580.Spearman

bhatta:
	$(JVM) $(RUNFLAGS) edu.nyu.cs.cs2580.Bhattacharyya prf/ outputs/ 	  

clean:
	find . -name '*.class' -exec rm -rf {} \;
	find . -name '*~' -exec rm -rf {} \;




