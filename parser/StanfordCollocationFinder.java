package com.parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
//	public static String modelPath = "edu/stanford/nlp/models/parser/nndep/CTB_CoNLL_params.txt.gz";
//	public static String taggerPath = "models/chinese-nodistsim.tagger";
//	public static String lexiPath = "edu/stanford/nlp/models/lexparser/chineseFactored.ser.gz";

	public static String modelPath = null;
	public static String taggerPath = null;
	public static String lexiPath = null;
			
	public static MaxentTagger tagger = null;
	public static DependencyParser parser = null;
	public static LexicalizedParser lp = null;
	public static WordNetConnection wnConnect = null;
	
	public static int countParserTimes = 0;
	
	public static void initParser(boolean bLex){
		if(tagger == null){
			tagger = new MaxentTagger(taggerPath);
		}
		if(parser == null){
			parser = DependencyParser.loadFromModelFile(modelPath);
		}
		if(bLex && lp == null){
//			String[] options = {"-maxLength", "5000", "-MAX_ITEMS","500000"};
			String[] options = {"-maxLength", "5000", "-MAX_ITEMS","500000", "-fastFactored"};
			lp = LexicalizedParser.loadModel(lexiPath, options);  
		}
	}
	
	public static void unInitParser() {
		tagger = null;
		parser = null;
		lp = null;
		countParserTimes = 0;
	}
	
	public static Map<Integer, String> getPhraseTag(List<HasWord> sentence){
		if(tagger == null || parser == null || sentence == null || lp == null){
			return null;
		}
		Map<Integer, String> phraseTag = new HashMap<Integer, String>();
		
		Tree parse = null;
		try {
			parse = lp.apply(sentence); 
		}catch (Exception e) {
			e.printStackTrace();
			return phraseTag;
		}
		
		if(parse == null){
			return phraseTag;
		}
		
		List<String> keyValue = new ArrayList<String>();
		keyValue = parserTree(parse, keyValue, null, null);
//		parse.pennPrint();
		
		for(int i = 0; i < keyValue.size(); i++){
			phraseTag.put(i+1, keyValue.get(i));
		}
		return phraseTag;
	}
	public static GrammaticalStructure ParserWord(List<HasWord> sentence) {
		if(tagger == null || parser == null || sentence == null){
			return null;
		}

		List<TaggedWord> tagged = tagger.tagSentence(sentence);
//		System.out.println(tagged.toString());
		GrammaticalStructure gs = parser.predict(tagged);
		
//		ChineseGrammaticalStructure cgs = new ChineseGrammaticalStructure(gs.typedDependenciesCCprocessed(), gs.root());

//		Collection<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
//		System.out.println(tdl);
//
//		String structure = GrammaticalStructure.dependenciesToString(gs, tdl, gs.root(), true, true);
//		System.out.println("structure = " + structure);
		
		// Print typed dependencies
		System.err.println(gs);
//		System.err.println(cgs);
		return gs;
	}
	
	public static List<HasWord> getWordList(String[] arrWord){
		if(arrWord == null || arrWord.length == 0){
			return null;
		}
		List<HasWord> sentence = new ArrayList<HasWord>();
		for(int i = 0; i < arrWord.length; i++){
			HasWord hWord = new Word();
	    	hWord.setWord(arrWord[i]);
	    	sentence.add(hWord);
		}
		return sentence;
	}

	
	private static List<String> parserTree(Tree parse, 
			List<String> keyValue, String grandpa, String father) {
		Tree[] children = parse.children();
		int ilen = children.length;
		for (Tree child : children) {
			//System.out.println(child.value());
			keyValue = parserTree(child, keyValue, father, parse.value());
		}
		if(ilen == 0){
//			System.out.println(parse.value()+":"+grandpa);
			keyValue.add(grandpa);
		}
		return keyValue;
	}
  public static void main(String[] args) throws Exception {
	  //initParser(true);
		
	  String words = "UNITED OLD AND MAN STATES";
	  
	  String[] wordStr = words.split(" ");
	  
	  LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishFactored.ser.gz");  
	  Tree parse = lp.apply(getWordList(wordStr));
	  for(int i=0;i<parse.size();i++){
		  System.out.println(parse.getChild(0));
	  }	  
	  PrintWriter pw = new PrintWriter(new FileWriter("file/result.txt"));
	  
	//建立指向WordNet词典目录的URL。  
	  String wnhome = System.getenv("WNHOME");
		
	  String path = wnhome + File.separator + "dict";
		
	  URL urls = new URL("file", null, path); 
		
	//建立词典对象并打开它。  
	  IDictionary dict = new Dictionary(urls);
	  
	  dict.open();
	  
	  WordNet wn = new WordNet(dict);
	  
	  CollocationFinder collocationFinder = new CollocationFinder(parse, wn);
	  
	  collocationFinder.PrintCollocationStrings(pw);
	  
	  pw.close();

//	  TreePrint treePrint = new TreePrint("collocations");
//	  
//	  treePrint.printTree(parse, pw);
	 
//	  List<String> keyValue = new ArrayList<String>();
//	  parserTree(parse, keyValue, null, null);
//	  parse.pennPrint();

//		StanfordParserStr p = parseAndLexi(words);
//		GrammaticalStructure gs = p.gs;
//		//Tree tree = gs.root();
//		Collection<TypedDependency> tdl = gs.allTypedDependencies();//cgs.allTypedDependencies();
//		Object[] tdArray = (Object[]) tdl.toArray();
//		for (int i = 0; i < tdArray.length; i++) {
//			TypedDependency td = (TypedDependency)tdArray[i];
//			System.out.println(td.dep().value() + "	" + td.dep().tag() + "	" + td);
//		}
//		for (Entry e : p.phraseTag.entrySet()) {
//			System.out.println(e.getKey() + " " + e.getValue());
//		}
		
  }

  public static GrammaticalStructure parse(String[] wordList) {
	  initParser(false);
	  return ParserWord(getWordList(wordList));
  }
  
  public static StanfordParserStr parseOnly(String[] wordList){
	  countParserTimes ++;
	  if (countParserTimes >= 3000) {
		  unInitParser();
	  }
	  
	  StanfordParserStr parseTag = new StanfordParserStr();
	  initParser(false);
	  List<HasWord> sentence = getWordList(wordList);
	  parseTag.gs = ParserWord(sentence);
	  parseTag.phraseTag = new HashMap<Integer, String>();
	  
	  return parseTag;
  }
  
  public static StanfordParserStr parseAndLexi(String[] wordList){
	  countParserTimes ++;
	  if (countParserTimes >= 3000) {
		  unInitParser();
	  }
	  
	  StanfordParserStr parseTag = new StanfordParserStr();
	  initParser(true);
	  List<HasWord> sentence = getWordList(wordList);
	  parseTag.gs = ParserWord(sentence);
	  parseTag.phraseTag = getPhraseTag(sentence);
	  
	  return parseTag;
  }
}
