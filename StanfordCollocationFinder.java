package com.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
//import edu.stanford.nlp.graph.Graph;
//import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.CollocationFinder;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.HeadFinder;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.WordNetConnection;
import edu.stanford.nlp.trees.international.pennchinese.ChineseGrammaticalStructure;

public class StanfordCollocationFinder {

	public static String lexiPath = "edu/stanford/nlp/models/lexparser/englishFactored.ser.gz";

	private static ArrayList<String> FileList = new ArrayList<String>(); // 获取所有文件

	public static LexicalizedParser lp = null;

	public static WordNetConnection wnConnect = null;

	public static List<HasWord> getWordList(String[] arrWord) {

		if (arrWord == null || arrWord.length == 0) {

			return null;

		}
		List<HasWord> sentence = new ArrayList<HasWord>();

		for (int i = 0; i < arrWord.length; i++) {

			HasWord hWord = new Word();

			hWord.setWord(arrWord[i]);

			sentence.add(hWord);

		}

		return sentence;
	}

	// 使用斯坦福生成的句法依存树和WordNet进行搭配提取
	public static void getCollocation(String filePath) throws Exception {

		lp = LexicalizedParser.loadModel(lexiPath);

		for (int i = 0; i < FileList.size(); i++) {

			ArrayList<String> list = readTxtFile(FileList.get(i));

			PrintWriter pw = new PrintWriter(new FileWriter(
					"file/ContinueResult_" + i + ".txt"));

			BufferedWriter bw = new BufferedWriter(new FileWriter(
					"file/NoncontinueResult_" + i + ".txt"));

			// 建立指向WordNet词典目录的URL。
			String wnhome = System.getenv("WNHOME");

			String path = wnhome + File.separator + "dict";

			URL urls = new URL("file", null, path);

			// 建立词典对象并打开它。
			IDictionary dict = new Dictionary(urls);

			dict.open();

			WordNet wn = new WordNet(dict, bw);

			for (String text : list) {

				String[] wordStr = text.split(" ");

				// 生成依存句法树
				Tree parse = lp.apply(getWordList(wordStr));

				// 搭配提取
				CollocationFinder collocationFinder = new CollocationFinder(
						parse, wn);

				collocationFinder.PrintCollocationStrings(pw);

			}

			bw.close();

			pw.close();

		}

	}

	// 获取当前目录及子目录下的所有文件
	public static List<String> readDirs(String filepath)
			throws FileNotFoundException, IOException {
		try {

			File file = new File(filepath);

			if (!file.isDirectory()) {

				System.out.println("输入的[]");

				System.out.println("filepath:" + file.getAbsolutePath());

			} else {

				String[] flist = file.list();

				for (int i = 0; i < flist.length; i++) {

					File newfile = new File(filepath + "\\" + flist[i]);

					if (!newfile.isDirectory()) {

						FileList.add(newfile.getAbsolutePath());

					} else if (newfile.isDirectory()) // if file is a directory,
														// call ReadDirs
					{

						readDirs(filepath + "\\" + flist[i]);

					}
				}
			}
		} catch (FileNotFoundException e) {

			System.out.println(e.getMessage());

		}
		return FileList;
	}

	/*
	 * 读取文件内容
	 */
	public static ArrayList<String> readTxtFile(String filePath) {

		ArrayList<String> textList = new ArrayList<String>();

		try {

			String encoding = "utf8";

			File file = new File(filePath);

			if (file.isFile() && file.exists()) { // 判断文件是否存在

				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 设定文件格式

				BufferedReader bufferedReader = new BufferedReader(read);

				String lineTxt = null;

				while ((lineTxt = bufferedReader.readLine()) != null) {

					textList.add(lineTxt);

				}

				read.close();

			} else {

				System.out.println("文件不存在！");

			}
		} catch (Exception e) {

			System.out.println("读取文件内容出错！");

			e.printStackTrace();

		}
		return textList;

	}

	public static void main(String[] args) throws Exception {
		// initParser(true);

		String filePath = "D:\\CollocationFind\\";

		readDirs(filePath);

		getCollocation(filePath);

	}
}
