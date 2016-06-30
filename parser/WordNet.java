package com.parser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.trees.WordNetConnection;

public class WordNet implements WordNetConnection {

	IDictionary dictUrl;

	public WordNet(IDictionary dictUrl) {

		this.dictUrl = dictUrl;

	}

	// 判断短语词组是否包含在WordNet中
	public boolean getWord(String str) {

		IIndexWord idxWord = dictUrl.getIndexWord(str, POS.NOUN);

		IIndexWord idxWord2 = dictUrl.getIndexWord(str, POS.ADJECTIVE);

		IIndexWord idxWord3 = dictUrl.getIndexWord(str, POS.ADVERB);

		IIndexWord idxWord4 = dictUrl.getIndexWord(str, POS.VERB);

		if (idxWord != null || idxWord2 != null || idxWord3 != null
				|| idxWord4 != null) {

			System.out.println(str);

			return true;

		}

		return false;

	}

	public boolean wordNetContains(String str) {

		// System.out.println(str);

		// 获取长度为2的非连续短语
		String[] CollocationWord = str.split("_");

		ArrayList<String> result = new ArrayList<String>();

		for (int i = 0; i < CollocationWord.length;) {
			
			int j = 2;
			
			while (i + j < CollocationWord.length) {
				
				result.add(CollocationWord[i] + "_" + CollocationWord[i + j]);
				
				j++;
			}
			
			i++;
		}

		for (int k = 0; k < result.size(); k++) {

			getWord(result.get(k));

		}

		// if (CollocationWord.length == 3) {
		//
		// str = CollocationWord[0] + "_" + CollocationWord[2];
		//
		// getWord(str);
		//
		// } else if (CollocationWord.length == 4) {
		//
		// String[] strResult = new String[3];
		//
		// strResult[0] = CollocationWord[0] + "_" + CollocationWord[2];
		//
		// strResult[1] = CollocationWord[0] + "_" + CollocationWord[3];
		//
		// strResult[2] = CollocationWord[1] + "_" + CollocationWord[3];
		//
		// for (int i = 0; i < strResult.length; i++) {
		//
		// getWord(strResult[i]);
		//
		// }
		//
		// } else if (CollocationWord.length == 5) {
		//
		// String[] strResult = new String[6];
		//
		// strResult[0] = CollocationWord[0] + "_" + CollocationWord[2];
		//
		// strResult[1] = CollocationWord[0] + "_" + CollocationWord[3];
		//
		// strResult[2] = CollocationWord[0] + "_" + CollocationWord[4];
		//
		// strResult[3] = CollocationWord[1] + "_" + CollocationWord[3];
		//
		// strResult[4] = CollocationWord[1] + "_" + CollocationWord[4];
		//
		// strResult[5] = CollocationWord[2] + "_" + CollocationWord[4];
		//
		// for (int i = 0; i < strResult.length; i++) {
		//
		// getWord(strResult[i]);
		//
		// }
		//
		// } else if (CollocationWord.length == 6) {
		//
		// String[] strResult = new String[10];
		//
		// strResult[0] = CollocationWord[0] + "_" + CollocationWord[2];
		//
		// strResult[1] = CollocationWord[0] + "_" + CollocationWord[3];
		//
		// strResult[2] = CollocationWord[0] + "_" + CollocationWord[4];
		//
		// strResult[3] = CollocationWord[0] + "_" + CollocationWord[5];
		//
		// strResult[4] = CollocationWord[1] + "_" + CollocationWord[3];
		//
		// strResult[5] = CollocationWord[1] + "_" + CollocationWord[4];
		//
		// strResult[6] = CollocationWord[1] + "_" + CollocationWord[5];
		//
		// strResult[7] = CollocationWord[2] + "_" + CollocationWord[4];
		//
		// strResult[8] = CollocationWord[2] + "_" + CollocationWord[5];
		//
		// strResult[9] = CollocationWord[3] + "_" + CollocationWord[5];
		//
		// for (int i = 0; i < strResult.length; i++) {
		//
		// getWord(strResult[i]);
		//
		// }
		//
		// }

		return getWord(str);
	}

}
