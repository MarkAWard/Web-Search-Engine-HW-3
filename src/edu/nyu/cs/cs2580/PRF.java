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
	

	
	public static Vector<ScoredTerms> Relevance(Vector<ScoredDocument> scoredDocs,int numdocs, int numTerms, HashBiMap<String,Integer> dict){
		Queue<ScoredTerms> rankQueue = new PriorityQueue<ScoredTerms>();
		int i;
		for (i=0; i<numdocs; i++){
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
				Total += wordHash.get(j);
			}
		}
			
		Terms words = new Terms();
		ScoredTerms scoreTs = new ScoredTerms(words, 0.0);
		Vector<ScoredTerms> scoreTerms = new Vector<ScoredTerms>();
		for (int keys:WordMap.keySet())
		{
			
			String name = dict.inverse().get(keys);
			words.setName(name);
			//System.out.println(name);
			scoreTs.set_term(words);
			double scor = ((double) WordMap.get(keys))/Total;
			scoreTs.set_score(scor);
			scoreTerms.add(scoreTs);
	
		}
			
		//Collections.sort(scoreTerms, Collections.reverseOrder());
			
		for ( i =0; i< scoreTerms.size(); i++)
		{
			System.out.println(scoreTerms.get(i).get_term().getName());
		}
		System.out.println(scoreTerms.size());		
		return scoreTerms;
		
	}
	

}
