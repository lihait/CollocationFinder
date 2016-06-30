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
		// ����ָ��WordNet�ʵ�Ŀ¼��URL��
		String wnhome = System.getenv("WNHOME");
		String path = wnhome + File.separator + "dict";
		URL url = new URL("file", null, path);

		// �����ʵ���󲢴�����
		IDictionary dict = new Dictionary(url);
		dict.open();

		// ��ѯmoney����ʵĵ�һ����˼��POS����Ĳ�����ʾҪѡ�����ִ��Եĺ���
		IIndexWord idxWord = dict.getIndexWord("money", POS.NOUN);
		IWordID wordID = (IWordID) idxWord.getWordIDs().get(0);
		IWord word = dict.getWord(wordID);
		System.out.println("Id = " + wordID);
		System.out.println("Lemma = " + word.getLemma());
		System.out.println("Gloss = " + word.getSynset().getGloss());

		// �ڶ�����˼
		IWordID wordID2 = (IWordID) idxWord.getWordIDs().get(1);
		IWord word2 = dict.getWord(wordID2);
		System.out.println(word2.getSynset().getGloss());
		// ��������˼
		IWordID wordID3 = (IWordID) idxWord.getWordIDs().get(2);
		IWord word3 = dict.getWord(wordID3);
		System.out.println(word3.getSynset().getGloss());

	}
}
