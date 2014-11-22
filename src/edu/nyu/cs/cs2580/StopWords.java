package edu.nyu.cs.cs2580;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

import edu.nyu.cs.cs2580.SearchEngine.Options;

public class StopWords {

	private HashSet<String> _stopwords = new HashSet<String>();

	public StopWords(Options options) throws IOException {

		ReadCorpus PorterStemmer = new ReadCorpus();
		BufferedReader br = new BufferedReader(new FileReader(options._stopWordsList));
		String line;
		while ((line = br.readLine()) != null) 
   			_stopwords.add( PorterStemmer.cleanAndStem(line.trim()) );
		br.close();
	}

	public Boolean contains(String word) {
		return _stopwords.contains(word);
	}

}