package edu.nyu.cs.cs2580;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.HashSet;
import java.util.Map;

import edu.nyu.cs.cs2580.SearchEngine.Options;

/**
 * @CS2580: Implement this class for HW3.
 */
public class CorpusAnalyzerPagerank extends CorpusAnalyzer implements Serializable {

  private static final long serialVersionUID = 2698138733115785548L;

  private HashMap<Integer, HashSet<Integer> > _linkGraph = new HashMap<Integer, HashSet<Integer>>();
  private HashMap<String, Integer> _linkHash = new HashMap<String, Integer>();
  private HashMap<String, Double> _ranked_docs = new HashMap<String, Double>();

  public CorpusAnalyzerPagerank(Options options) {
    super(options);
  }

  /**
   * This function processes the corpus as specified inside {@link _options}
   * and extracts the "internal" graph structure from the pages inside the
   * corpus. Internal means we only store links between two pages that are both
   * inside the corpus.
   * 
   * Note that you will not be implementing a real crawler. Instead, the corpus
   * you are processing can be simply read from the disk. All you need to do is
   * reading the files one by one, parsing them, extracting the links for them,
   * and computing the graph composed of all and only links that connect two
   * pages that are both in the corpus.
   * 
   * Note that you will need to design the data structure for storing the
   * resulting graph, which will be used by the {@link compute} function. Since
   * the graph may be large, it may be necessary to store partial graphs to
   * disk before producing the final graph.
 * @return 
   *
   * @throws IOException
   */
  @Override
  public void prepare() throws IOException, NoSuchAlgorithmException  {
    System.out.println("Preparing " + this.getClass().getName());
    
    String corpusDir = _options._corpusPrefix;
    final File Dir = new File(corpusDir);
     
    String link_name;
    String corresponding_links;
    HashMap<String, HashSet<String> > linksource = new HashMap<String, HashSet<String>>();
    int num_docs = 0;
    System.out.println("Extracting Links");
    for (final File fileEntry : Dir.listFiles()) {
      
      if ( !fileEntry.isDirectory() ) {
        
        // dont read hidden files
        if(fileEntry.isHidden())
		      continue;
	    
	    // Create Extract link object
	    HeuristicLinkExtractor f = new HeuristicLinkExtractor(fileEntry);
	    
	    // Get Main source page link
	    link_name= f.getLinkSource();
	    
	    ArrayList<String> linkList = new ArrayList<String>();
	    // Get all links (Page names) present in the source page
	    while ( (corresponding_links = f.getNextInCorpusLinkTarget()) != null)
        linkList.add(corresponding_links);
	    
	    // Put the array list of Strings (Links in source page into a hash map)
	    HashSet<String> linkSet = new HashSet<String>(linkList);
	    linksource.put(link_name, linkSet);
	    _linkHash.put(link_name, num_docs);

	num_docs += 1;
    }
    
  }
  System.out.println(_linkHash.size());
  // Create a local map variable (efficient to iterate over)
  System.out.println("Creating Graph");
  // Iterate over Map keys
  for (String key : linksource.keySet()) {
    HashSet<String> links = new HashSet<String>();
    HashSet<Integer> linkAdjSet = new HashSet<Integer>();

    // Store Link Set of a particular key
    links = linksource.get(key);

    //Iterate over the links in the set
    for (String link_values : links) {
      //Add to the adjacency list  (HashSet) if present in corpus
	    if (linksource.containsKey(link_values))
        	linkAdjSet.add(_linkHash.get(link_values));
    }
    _linkGraph.put(_linkHash.get(key), linkAdjSet);
  }
	//System.out.println(_linkGraph);
  return;
} 
    /**
   * This function computes the PageRank based on the internal graph generated
   * by the {@link prepare} function, and stores the PageRank to be used for
   * ranking.
   * 
   * Note that you will have to store the computed PageRank with each document
   * the same way you do the indexing for HW2. I.e., the PageRank information
   * becomes part of the index and can be used for ranking in serve mode. Thus,
   * you should store the whatever is needed inside the same directory as
   * specified by _indexPrefix inside {@link _options}.
   *
   * @throws IOException
   */
  @Override
  public void compute() throws IOException {
    System.out.println("Computing using " + this.getClass().getName());
    
    // total number of pages included in graph
    int nnodes = _linkGraph.keySet().size();
    // initial value for beginning of each iteration
    Double init = (1.0 - _options._lambda)/nnodes;
    //Double init = 0.02f;
    // initialize pagerank of all pages to 0.5 maybe go bigger?
    ArrayList<Double> ranks = new ArrayList<Double>( Collections.nCopies(nnodes, 1.0) );
    // array to track pageranks as we update 
    ArrayList<Double> new_ranks = new ArrayList<Double>( Collections.nCopies(nnodes, 0.0) ); 

    for (int iters = 0; iters < _options._iterations; iters++) {
      // reinitialize if its not the first iteration
	//System.out.println("INIT: " + init);

      for (int i = 0; i < nnodes && iters > 0; i++) 
        new_ranks.set(i, 0.0);

      // go through every webpage in the graph
      for (Integer node : _linkGraph.keySet()) {
	  //System.out.println(ranks.get(node));
        HashSet<Integer> links = _linkGraph.get(node);
	Double weight = _options._lambda * (1.0 / links.size());
	//System.out.println("WEIGHT:" + weight);
	
	for (int i = 0; i < nnodes; i++) {
	    Double tmp = new_ranks.get(i);
	    //System.out.println("node: " + node + "i: " + i + "   " + tmp);
	    if (links.contains(i))
		new_ranks.set(i, tmp + (init + weight) * ranks.get(node));
	    else 
		new_ranks.set(i, tmp + init * ranks.get(node));
	}
	
      }
      // update the pageranks and repeat
      ranks = new ArrayList<Double>( new_ranks );
      //System.out.println(ranks);
    }
    System.out.println(ranks);
    for(String page : _linkHash.keySet())
      _ranked_docs.put(page, ranks.get(_linkHash.get(page)));
     	
    new_ranks = null;
    ranks = null;
    _linkHash = null;
    _linkGraph = null;

    String indexFile = "pageranks.idx";
    System.out.println("Store PageRanks to: " + indexFile);
    ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(indexFile));

    try {
      writer.writeObject(this);
      writer.close();
    }
    catch(Exception e) {
      System.out.println(e.toString());
    }
   
    return;
  }

  /**
   * During indexing mode, this function loads the PageRank values computed
   * during mining mode to be used by the indexer.
   *
   * @throws IOException
   */
  @Override
  public Object load() throws IOException {
    System.out.println("Loading using " + this.getClass().getName());

    String indexFile = "pageranks.idx";
    System.out.println("Load PageRanks from: " + indexFile);

    // read in the index file
    ObjectInputStream reader = new ObjectInputStream(new FileInputStream(indexFile));
    CorpusAnalyzerPagerank loaded = null;
	try {
		loaded = (CorpusAnalyzerPagerank) reader.readObject();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
  
    this._ranked_docs = loaded._ranked_docs;
    loaded = null;
    return null;
  }

  public Double getPagerank(String doc) {
    return  (_ranked_docs.containsKey(doc) ? _ranked_docs.get(doc) : 0.0);
  }

}
