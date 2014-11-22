package edu.nyu.cs.cs2580;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;

import com.google.common.collect.HashBiMap;

public class PRF {
	private static HashMap<Integer, Integer> WordMap = new HashMap<Integer, Integer>();
	private static int Total = 0;
	private static Vector<ScoredTerms> scoreTerms = new Vector<ScoredTerms>();
	private static ScoredTerms scoreTs;
	private static Terms words;
	
	public static Vector<ScoredTerms> Relevance(Vector<ScoredDocument> scoredDocs, int numTerms, HashBiMap<String,Integer> dict){
		Queue<ScoredTerms> rankQueue = new PriorityQueue<ScoredTerms>();
		int i;
		for (i=0; i<scoredDocs.size(); i++){
			ScoredDocument docum = scoredDocs.get(i);
			Document d =  docum.get_doc();
			HashMap<Integer, Integer> wordHash = ((DocumentIndexed) d).getTopWords(numTerms);
			
			for (int j:wordHash.keySet())
			{
				if (WordMap.containsKey(j))
				{
					
					WordMap.put(j, wordHash.get(j)+WordMap.get(j));
				}
				else
				{
					WordMap.put(j, wordHash.get(j));
				}
				Total += 1;
			}
		}
			
		
		for (int keys:WordMap.keySet())
		{
			
			String name = dict.inverse().get(keys);
			words.setName(name);
			scoreTs.set_term(words);
			double scor = ((double) WordMap.get(keys))/Total;
			scoreTs.set_score(scor);
			
			scoreTerms.add(scoreTs);
		}
			
			Collections.sort(scoreTerms, Collections.reverseOrder());
			
		
		return scoreTerms;
		
	}
	

}
