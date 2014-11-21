package edu.nyu.cs.cs2580;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import edu.nyu.cs.cs2580.CorpusAnalyzer.HeuristicLinkExtractor;
import edu.nyu.cs.cs2580.Document.HeuristicDocumentChecker;
import edu.nyu.cs.cs2580.SearchEngine.Options;

import java.io.Serializable;
/**
 * @CS2580: Implement this class for HW3.
 */
public class LogMinerNumviews extends LogMiner implements Serializable { 
  private static final long serialVersionUID = 467974245689L;
  private HashMap<String, Integer> _numViews = new HashMap<String, Integer>();

  public LogMinerNumviews(Options options) {
    super(options);
  }

  /**
   * This function processes the logs within the log directory as specified by
   * the {@link _options}. The logs are obtained from Wikipedia dumps and have
   * the following format per line: [language]<space>[article]<space>[#views].
   * Those view information are to be extracted for documents in our corpus and
   * stored somewhere to be used during indexing.
   *
   * Note that the log contains view information for all articles in Wikipedia
   * and it is necessary to locate the information about articles within our
   * corpus.
   *
   * @throws IOException
   */
  @Override
  public void compute() throws IOException, NoSuchAlgorithmException {
    System.out.println("Computing using " + this.getClass().getName());
 
    String logDir = _options._logPrefix;
    String corpusDir = _options._corpusPrefix;
    final File DirCorpus = new File(corpusDir);
    Document.HeuristicDocumentChecker Checker = new Document.HeuristicDocumentChecker();
    
    final File DirLog = new File(logDir);
    BufferedReader reader = null;
    String line = null;
    int num_docs=0;
    String[] splitline = null;
    for (final File fileEntry : DirCorpus.listFiles()) {
        
        if ( !fileEntry.isDirectory() ) {
          
          // dont read hidden files
          if(fileEntry.isHidden())
  		      continue;
  	    
  	    // Create Extract link object
  	    HeuristicLinkExtractor f = new HeuristicLinkExtractor(fileEntry);
  	    
  	    // Get Main source page link
  	    Checker.addDoc(f.getLinkSource().toLowerCase());
        }
	
    }
    for (final File logEntry : DirLog.listFiles()) {
      
      if ( !logEntry.isDirectory()) {
        
        // dont read hidden files
        if(logEntry.isHidden())
          continue;

        reader = new BufferedReader(new FileReader(logEntry));
        while ((line = reader.readLine()) != null) {
          splitline = line.split(" ");

	    
          if (splitline.length >=2 && Checker.checkDoc(splitline[1].toLowerCase()) && splitline.length <=3)
          {
        	if(!_numViews.containsKey(splitline[1].toLowerCase()))
        	{
        		_numViews.put(splitline[1].toLowerCase(),0);
        	}
        	  
            if(splitline.length ==2)
            {
            
            	 _numViews.put(splitline[1].toLowerCase(),_numViews.get(splitline[1].toLowerCase())+0);
            }
            else
            {
            int num = 0;
            boolean parse=true;
            try{
            	num=Integer.parseInt(splitline[2]);
            }
            catch(NumberFormatException e)
            {
            	parse=false;
            }
            if(parse)
	    {
            _numViews.put(splitline[1].toLowerCase(),_numViews.get(splitline[1].toLowerCase())+num );
        }
            }
          }
        }
      }
    }
    System.out.println(_numViews.get("somebody_that_i_used_to_know"));
    System.out.println(_numViews.size());     
    String indexFile = "numviews.idx";
    System.out.println("Store Numviews to: " + indexFile);
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
   * During indexing mode, this function loads the NumViews values computed
   * during mining mode to be used by the indexer.
   * 
   * @throws IOException
   */
  @Override
  public Object load() throws IOException,ClassNotFoundException {
    System.out.println("Loading using " + this.getClass().getName());

    String indexFile = "numviews.idx";
    System.out.println("Load Numviews from: " + indexFile);

    // read in the index file
    ObjectInputStream reader = new ObjectInputStream(new FileInputStream(indexFile));
  

	LogMinerNumviews loaded = (LogMinerNumviews) reader.readObject();

  
    this._numViews = loaded._numViews;
    loaded = null;
    
    return null;
  }
  public Integer getNumviews(String doc) {
    return  (_numViews.containsKey(doc) ? _numViews.get(doc) : 0);
  }

}
