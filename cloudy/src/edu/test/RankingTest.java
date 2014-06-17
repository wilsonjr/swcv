package edu.test;

import edu.cloudy.nlp.WCVDocument;
import edu.cloudy.nlp.Word;
import edu.cloudy.nlp.ranking.TFIDFRankingAlgo;
import edu.cloudy.nlp.ranking.TFRankingAlgo;
import edu.cloudy.utils.Logger;
import edu.cloudy.utils.WikipediaXMLReader;
import edu.stanford.nlp.io.StringOutputStream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RankingTest
{

	public static void main(String[] args)
	{
		Logger.doLogging = false;

		WCVDocument doc = readDocument("data/twitter");

		//List<WCVDocument> docs = readDocumentCollection("data/test_wiki1");
		//WCVDocument doc = docs.get(0);
		//System.out.println("#docs: " + docs.size());
		//
		//		doc.weightFilter(10, new LexRankingAlgo());
		//		
		//		for (Word w : doc.getWords())
		//			System.out.println(w.word + "   " + w.stem + "   " + w.weight);

		System.out.println("\nTF RANKING:\n");

		doc.weightFilter(1000000, new TFRankingAlgo());
		int count = 0;
		try
		{
			PrintWriter pw = new PrintWriter("output2.txt");
			for (Word w : doc.getWords())
				pw.println((count++) + ": " + w.word + "   " + w.stem + "   " + w.weight);
			pw.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//WCVDocument doc2 = readDocument("data/test_wiki3");/*
		//doc2.weightFilter(10, new TFRankingAlgo());

		//		System.out.println("\nTF RANKING:\n");
		//		
		//		for (Word w : doc2.getWords())
		//			System.out.println(w.word + "   " + w.stem + "   " + w.weight);*/
	}

	private static WCVDocument readDocument(String filename)
	{
		WikipediaXMLReader xmlReader = new WikipediaXMLReader(filename);
		xmlReader.read();
		Iterator<String> texts = xmlReader.getTexts();

		int index = 0;
		for (int i = 0; i < index; i++)
			texts.next();

		WCVDocument wordifier = new WCVDocument(texts.next());
		// 2. build similarities, words etc
		wordifier.parse();
		return wordifier;
	}

	private static List<WCVDocument> readDocumentCollection(String filename)
	{
		WikipediaXMLReader xmlReader = new WikipediaXMLReader(filename);
		xmlReader.read();
		Iterator<String> texts = xmlReader.getTexts();

		List<WCVDocument> docs = new ArrayList<WCVDocument>();
		while (texts.hasNext())
		{
			WCVDocument doc = new WCVDocument(texts.next());
			doc.parse();
			docs.add(doc);
		}

		return docs;
	}

}
