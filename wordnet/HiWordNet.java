package com.wordnet;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

public class HiWordNet {
	public static void main(String[] args) throws IOException {
		// 建立指向WordNet词典目录的URL。
		String wnhome = System.getenv("WNHOME");
		String path = wnhome + File.separator + "dict";
		URL url = new URL("file", null, path);

		// 建立词典对象并打开它。
		IDictionary dict = new Dictionary(url);
		dict.open();

		// 查询money这个词的第一种意思。POS后面的参数表示要选的哪种词性的含义
		IIndexWord idxWord = dict.getIndexWord("money", POS.NOUN);
		IWordID wordID = (IWordID) idxWord.getWordIDs().get(0);
		IWord word = dict.getWord(wordID);
		System.out.println("Id = " + wordID);
		System.out.println("Lemma = " + word.getLemma());
		System.out.println("Gloss = " + word.getSynset().getGloss());

		// 第二种意思
		IWordID wordID2 = (IWordID) idxWord.getWordIDs().get(1);
		IWord word2 = dict.getWord(wordID2);
		System.out.println(word2.getSynset().getGloss());
		// 第三种意思
		IWordID wordID3 = (IWordID) idxWord.getWordIDs().get(2);
		IWord word3 = dict.getWord(wordID3);
		System.out.println(word3.getSynset().getGloss());

	}
}
