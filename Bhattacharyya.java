package edu.nyu.cs.cs2580;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

public class Bhattacharyya {
	
	static String _prf_file=null;
	static String _output_file=null;
	static Vector<String> queries=new Vector<String>();
	static Vector<String> paths=new Vector<String>();
	
	
	
	private static void parse_CommandLine(String[] args)
	{
		if(args.length==0)
		{
			_prf_file= "";
			_output_file= "";
		}
		else
		{
			_prf_file=args[0];
			_output_file=args[1];
			
		}
	}
	private static void query_similarity() throws IOException
	{
		
		BufferedReader br1= new BufferedReader(new FileReader(_prf_file));
		String line=null;

		
		while((line=br1.readLine()) != null)
		{
			Scanner s= new Scanner(line).useDelimiter(":");
			queries.add(s.next());
			paths.add(s.next());
			s.close();
		}
		
		Double similarity;
		
		PrintWriter writer = new PrintWriter("./"+_output_file,"UTF-8");
		
		for(int i=0; i<paths.size();i++)
		{
			for(int j=i+1;j<paths.size(); j++)
			{
				similarity=calculate_Bhattacharyya_coff(paths.get(i),paths.get(j));
				writer.printf("%s\t%s\t%f\n",queries.get(i),queries.get(j),similarity);
				System.out.printf("%s\t%s\t%f\n",queries.get(i),queries.get(j),similarity);
			}
			
		}
		writer.close();
		
	}




	private static Double calculate_Bhattacharyya_coff(String f1,String f2) throws IOException
	{
		Double beta=0.0;
		BufferedReader br1= new BufferedReader(new FileReader(f1));
		BufferedReader br2= new BufferedReader(new FileReader(f2));
		
		String term1=null;
		Double prob1=null;
		String term2=null;
		Double prob2=null;
		
		String line1=null;
		String line2=null;
		
		Scanner s1 = null;
		Scanner s2 = null;
		
		
		while(((line1=br1.readLine())!=null)&&(line2=br2.readLine())!=null)
		{
			
			s1= new Scanner(line1).useDelimiter("\t");
			s2= new Scanner(line2).useDelimiter("\t");
			
			
			term1=s1.next();
			prob1=s1.nextDouble();
			
			term2=s2.next();
			prob2=s2.nextDouble();
			
			beta= beta + Math.sqrt(prob1*prob2);
			
		}
		
		s1.close();
		s2.close();
		br1.close();
		br2.close();
		return beta;	
	
		
	}
	
	
	
	public static void main(String[] args) throws IOException
	{
		parse_CommandLine(args);
		System.out.println("Calculating Bhattacharyya coefficient");
		query_similarity();
		
	}



}
