package edu.nyu.cs.cs2580;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

public class Bhattacharyya {
	
	static String _prf_path=null;
	static String _output_path=null;
	
	
	
	private static void parse_CommandLine(String[] args)
	{
		if(args.length==0)
		{
			_prf_path= "";
			_output_path= "";
		}
		else
		{
			_prf_path=args[0];
			_output_path=args[1];
			
		}
	}
	
	
	
	
	public static void main(String[] args) throws IOException
	{
		parse_CommandLine(args);	
		query_similarity();
		
	}




	private static void query_similarity() throws IOException
	{
		Vector<String> file_names=new Vector<String>();
		final File Dir = new File(_prf_path);
		for (final File fileEntry : Dir.listFiles()) {
			if(fileEntry.isHidden())
				continue;
			
			if(fileEntry.getName().endsWith(".tsv"))
			{
				file_names.add(fileEntry.getName());
				System.out.println(fileEntry.getName());
			}
			
		}
		
		Double similarity;
		
		
		PrintWriter writer = new PrintWriter(_output_path+"Bhattacharyya.tsv", "UTF-8");
		
		
		
		for(int i=0; i<file_names.size();i++)
		{
			for(int j=i+1;j<file_names.size(); j++)
			{
				
				similarity=calculate_Bhattacharyya_coff(file_names.get(i),file_names.get(j));
				writer.printf("%s\t%s\t%f\n",file_names.get(i),file_names.get(j),similarity);
				
			}
			
		}
		writer.close();
		
	}




	private static Double calculate_Bhattacharyya_coff(String f1,String f2)
	{
		Double beta=0.0;
		
		
		
	
		return beta;
		
		
	
		
	}



}
