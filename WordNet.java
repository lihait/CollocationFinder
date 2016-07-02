package com.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.trees.WordNetConnection;

public class WordNet implements WordNetConnection {

	IDictionary dictUrl;
	
	BufferedWriter bufferedWriter;

	public WordNet(IDictionary dictUrl, BufferedWriter bw) throws Exception {

		this.dictUrl = dictUrl;
		
		this.bufferedWriter = bw;

	}

	// 判断短语词组是否包含在WordNet中
	public boolean getWord(String str) throws IOException {

		IIndexWord idxWord = dictUrl.getIndexWord(str, POS.NOUN);

		IIndexWord idxWord2 = dictUrl.getIndexWord(str, POS.ADJECTIVE);

		IIndexWord idxWord3 = dictUrl.getIndexWord(str, POS.ADVERB);

		IIndexWord idxWord4 = dictUrl.getIndexWord(str, POS.VERB);

		if (idxWord != null || idxWord2 != null || idxWord3 != null
				|| idxWord4 != null) {

			System.out.println(str);
			
			bufferedWriter.write(str);
			
			bufferedWriter.newLine();
			
			//bufferedWriter.close();

			return true;

		}
		
		return false;

	}

	public boolean wordNetContains(String str) {

		// System.out.println(str);

		String[] CollocationWord = str.split("_");

		ArrayList<String> result = new ArrayList<String>();

		// 获取长度为2的非连续短语
		for (int i = 0; i < CollocationWord.length;) {
			
			int j = 2;
			
			while (i + j < CollocationWord.length) {
				
				result.add(CollocationWord[i] + "_" + CollocationWord[i + j]);
				
				j++;
			}
			
			i++;
		}

		for (int k = 0; k < result.size(); k++) {

			try {
				getWord(result.get(k));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		try {
			return getWord(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
