import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import connection.Myjdbc;
import edu.stanford.nlp.simple.Sentence;
import generator.Parameter;
import others.Utility;
import treeNode.StartToGoalTreeNode;
import treeNode.TreeNodeAbstract;

public class test {

	public static void main(String[] args) {

		Parameter.con = Myjdbc.connect();
		Parameter.stm = Myjdbc.createStatement(Parameter.con);
		
		try {
			simulation();
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		w2vPreprocess();
		
//		LengthModel lengthModel = new LengthModel();
//		System.out.println(lengthModel.getValue(10, 12));
//		
//		List<Integer> storySeperateCount  = new LinkedList<Integer>();
//		storySeperateCount.add(2);
//		storySeperateCount.add(6);
//		storySeperateCount.add(1);
//		//Combine single concept to the adjacent smaller concept group
//		List<Integer> storySeperateCountTmp = new LinkedList<Integer>();
//		for(int i=0; i<storySeperateCount.size(); i++){
//			if(storySeperateCount.get(i) == 1){
//				if(i==storySeperateCount.size()-1){
//					storySeperateCountTmp.add(1);
//				}else{
//						storySeperateCountTmp.add(2);
//						storySeperateCount.set(i+1, storySeperateCount.get(i+1)-1);
//				}
//			}else{
//				if(storySeperateCount.get(i) != 0)
//					storySeperateCountTmp.add(storySeperateCount.get(i));
//			}
//		}
//		
//		storySeperateCount = storySeperateCountTmp;
//		
//		for(int ii : storySeperateCount){
//			System.out.println(ii);
//		}
//		
//		storySeperateCountTmp = new LinkedList<Integer>();
//		
//		for(int n : storySeperateCount){
//			if(n>3){
//				while(n>6){
//					storySeperateCountTmp.add(3);
//					n-=3;
//				}
//				if(n == 4){
//					storySeperateCountTmp.add(2);
//					storySeperateCountTmp.add(2);
//				}else if(n == 5){
//					storySeperateCountTmp.add(2);
//					storySeperateCountTmp.add(3);
//				}else if(n == 6){
//					storySeperateCountTmp.add(3);
//					storySeperateCountTmp.add(3);
//				}else{
//					storySeperateCountTmp.add(n);
//				}
//			}else{
//				storySeperateCountTmp.add(n);
//			}
//		}
//		storySeperateCount = storySeperateCountTmp;
//		System.out.println("!!");
//		for(int ii : storySeperateCount){
//			System.out.println(ii);
//		}
//		
		
		
//		Sentence sent = new Sentence("Lucy go to china at summer.");
//		List<String> nerTags = sent.nerTags();  // [PERSON, O, O, O, O, O, O, O]
//		for(String str : nerTags){
//			System.out.println(str);
//		}
//		findLocationTerm();
		
//		Tagger tag = new Tagger();
//		List<String> s = tag.tagConcept_CoreNLP("jesus");
//		for(String ss : s){
//			System.out.println(ss);
//		}
		
		
//		Tagger tagger = new Tagger();
//		String[] tag = tagger.tagConcept("swim");
//		for(String s : tag){
//			System.out.println(s);
//		}
//		tagger.taggerRelease();
//		
//		Wikitionary wk = new Wikitionary();
//		System.out.println(wk.isVerb("wall"));
//		wk.releaseWKT();
		
//		EssayGenerator essay = new EssayGenerator();
//		System.out.println(essay.generateSentenseWithtwoConcept("go to school", "play with friend", "causes"));
		
		// TODO Auto-generated method stub
//		Parameter.con = Myjdbc.connect();
//		Parameter.stm = Myjdbc.createStatement(Parameter.con);
//		Parameter.nextStates = Utility.initNextStates();
//		Parameter.nextStatesInverse = Utility.initNextStatesInverse();
//		HumorConceptGenerator h = new HumorConceptGenerator();
//		h.generateConcept("wake_up", "e");
		
		
//		IWiktionaryEdition wkt;
//		wkt = JWKTL.openEdition(new File(Parameter.wiktionaryPath));
//		List<IWiktionaryEntry> entries = wkt.getEntriesForWord("home");
//		for(IWiktionaryEntry e : entries){
//			System.out.println(e.getRelations(RelationType.ANTONYM));
//			for(IWiktionaryRelation ee : e.getRelations(RelationType.ANTONYM)){
//				System.out.println(ee.getTarget().trim().toString());
//			}
//		}
//		wkt.close();
//		final RelationType a = "aa";
//		
//		System.out.println(a.valueOf("ANTONYM").ANTONYM);
////		t.stopTimer();
//		System.out.println(t.toString());
//		t.resetTimer();
//		for(int m = 0; m<1000000000;m++){
//			if(i == j){
//				
//			}
//		}
//		t.stopTimer();
//		System.out.println(t.toString());
		
//		String a = "go_to_doctor";
//		String b = "go_see_doctor";
//		//System.out.print(myequal(a,b));
		
//		File dumpFile = new File(Parameter.wiktionaryXML);	
//		File outputDirectory = new File(Parameter.wiktionaryPath);
//		boolean overwriteExisting = Boolean.valueOf(true);
		
		// parse dump file
//		JWKTL.parseWiktionaryDump(dumpFile, outputDirectory, overwriteExisting);
//		// Create new IWiktionaryEdition for our parsed data.
//		IWiktionaryEdition wkt = JWKTL.openEdition(outputDirectory);
//		
//		// Print the information of the parsed entries.
//		//for (IWiktionaryEntry entry : entries)
//		//	System.out.println(WiktionaryFormatter.instance().formatHeader(entry));
//		//Tagger.taggerInit();
//
//		List<IWiktionaryEntry> entries = wkt.getEntriesForWord("cross");
//		for(IWiktionaryEntry e : entries)
//			System.out.println(e.getPartOfSpeech().name());
//		for(IWiktionaryWordForm s : entries.get(0).getWordForms())
//		  System.out.println(s.getWordForm());
//		 //System.out.println(changePOS("cross a man", 2));
//		// Close the Wiktionary edition.
//		String ss = "ajajsbgwe";
//		System.out.println(ss.substring(0, ss.lastIndexOf("e")) + "ing");
//		wkt.close();	
		
	}

	static int count;
	private static void simulation() throws SQLException, IOException{
		Parameter.nextStates = Utility.initNextStates();
		//initialize file setting
		if (!(new File(Parameter.resultFolder)).mkdirs()) {
			System.out.println(Parameter.resultFolder + " folder already open!");
		}
		FileWriter saveFile = null;
		try {
			saveFile = new FileWriter(Parameter.resultFolder + "//w2v_story.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Parameter.fwriter = new BufferedWriter(saveFile);
		
		ResultSet rs;
		List<String> startToFind = new ArrayList<String>();
		String SQL = "select concept from " + Parameter.storyTeller_DBname + "." + Parameter.storyTeller_concept_concept;
		rs = Parameter.stm.executeQuery(SQL);
		rs.beforeFirst();
		while(rs.next()){
			startToFind.add(rs.getString(Parameter.storyTeller_concept_concept));
		}
		rs.close();
		
		Map<String, Integer> conceptCountMap = new HashMap<String, Integer>();
		
		//DFS find all story
		for(int i=0; i<startToFind.size(); i++){
			String curConcept = startToFind.get(i);
			Set<String> examine = new HashSet<String>();
			count = 0;
			findNextConcept(curConcept, curConcept, 0, examine, "e");
			conceptCountMap.put(curConcept, count);
			System.out.println((i+1)+"/"+startToFind.size()+" "+curConcept+" "+count);
		}
		
//		for(int i=0; i<startToFind.size(); i++){
//			String curConcept = startToFind.get(i);
//			System.out.println((i+1)+"/"+startToFind.size()+" "+curConcept);
//			int storyCount = (int) Math.pow(conceptCountMap.get(curConcept), 1.2);
////			System.out.println(conceptCountMap.get(curConcept)+" "+storyCount);
//			findRandomStory(curConcept, "e", storyCount);
//		}

		Parameter.fwriter.close();
		
	}
	

	private static void findNextConcept(String currentStory, String curConcept, int step, Set<String> examine, String curState) throws SQLException, IOException{
		
		if(step > 3) {
			count++;
			Parameter.fwriter.write(currentStory);
			Parameter.fwriter.newLine();
			Parameter.fwriter.flush();
			return ;
		}
		
		ResultSet rs;
		String nextState = null;
		String tableName = null;
		List<String> nextStates = Parameter.nextStates.get(curState);
		StringBuffer selectSQL = new StringBuffer();
		StringBuffer countSQL = new StringBuffer();
		Map<String, String> conceptStateMap = new HashMap<String, String>();
		Set<String> newExamine = new HashSet<String>(examine);
		newExamine.add(curConcept);
		
		//find all next concept
		for (int i = 0; i < nextStates.size(); i++) {
			nextState = nextStates.get(i);
			tableName = curState + "_" + nextState;
			countSQL.append("select end from "+Parameter.storyTeller_DBname+"."+tableName+" where start = '"+curConcept.replace("'", "\\'")+"' ");
			for(String notFindConcept : examine){
				countSQL.append("and end != '" + notFindConcept.replace("'", "\\'") + "' ");
			}
			rs = Parameter.stm.executeQuery(countSQL.toString());
			countSQL.delete(0, countSQL.length());
			rs.beforeFirst();
			while (rs.next()) {
				conceptStateMap.put(rs.getString("end"), nextState);
			}
			rs.close();
		}
		
		if (conceptStateMap.isEmpty()) {
			count++;
			Parameter.fwriter.write(currentStory);
			Parameter.fwriter.newLine();
			Parameter.fwriter.flush();
			return;
		}
		
		Set<String> conceptToFindSet = conceptStateMap.keySet();
		for(String concept:conceptToFindSet){
			findNextConcept(currentStory+" "+concept, concept, step+1, newExamine, conceptStateMap.get(concept));
		}
		
	}
	
	private static void findRandomStory(String startConcept, String startState, int totalStoryNum) throws SQLException, IOException{
		ResultSet rs;
		for(int storyNum = 0; storyNum < totalStoryNum; storyNum++){
			int rndNum;
			Random r = new Random();
			int[] numArr = new int[Parameter.fabulaElement.length];
			String nextState = null;
			String curConcept = startConcept;
			String prevState = startState;
			String tableName = null;
			List<String> nextStates = Parameter.nextStates.get(prevState);
			List<String> storyList = new ArrayList<String>();
			StringBuffer selectSQL = new StringBuffer();
			StringBuffer countSQL = new StringBuffer();
			
			List<String> simulationStory = new LinkedList<String>();
			Set<String> examine = new HashSet<String>();
			
			int step = 0;
			while (step <= 30) {
				simulationStory.add(curConcept);
				for (int i = 0; i < nextStates.size(); i++) {
					if (i != 0)
						countSQL.append(" union ");
					nextState = nextStates.get(i);
					tableName = prevState + "_" + nextState;
					countSQL.append("select "+(i+1)+" as id , count(*) from "+Parameter.storyTeller_DBname+"."+tableName+" where start = '"+curConcept.replace("'", "\\'")+"'");
					for(String notFindConcept : examine){
						countSQL.append("and end != '" + notFindConcept.replace("'", "\\'") + "' ");
					}
				}
				rs = Parameter.stm.executeQuery(countSQL.toString());
				countSQL.delete(0, countSQL.length());
				rs.beforeFirst();
				int tempCnt = 0;
				while (rs.next()) {
					numArr[tempCnt++] = rs.getInt("count(*)");
				}
				rs.close();
				int totalrsNum = 0;
				for (int i = 0; i < nextStates.size(); i++) {
					totalrsNum += numArr[i];
				}
				if (totalrsNum == 0) {
					break;
				}

				// pick nActions different random numbers from 1 to totoalrsNum
				rndNum = r.nextInt(totalrsNum);
				int temprsNum = 0;
				int cur = 0;
				for (cur = 0; cur < nextStates.size(); cur++) {
					temprsNum += numArr[cur];
					if (rndNum < temprsNum)
						break;
				}
				nextState = nextStates.get(cur);
				tableName = prevState + "_" + nextState;
				selectSQL.append("select end from "+Parameter.storyTeller_DBname+"."+tableName+" where start = '"+curConcept.replace("'", "\\'")+"'");
				for(String notFindConcept : examine){
					selectSQL.append("and end != '" + notFindConcept.replace("'", "\\'") + "' ");
				}
				rs = Parameter.stm.executeQuery(selectSQL.toString());
				selectSQL.delete(0, selectSQL.length());
				rs.absolute(rndNum - temprsNum + numArr[cur] + 1);
				examine.add(curConcept);
				curConcept = rs.getString("end");
				rs.close();
				prevState = nextState;
				nextStates = Parameter.nextStates.get(nextState);
				step++;
			}
			StringBuffer sentence = new StringBuffer();
			for(String concept : simulationStory){
				sentence.append(concept+" ");
			}
			sentence.delete(sentence.length()-1, sentence.length());
			Parameter.fwriter.write(sentence.toString());
			Parameter.fwriter.newLine();
			Parameter.fwriter.flush();
		}
	}
	
	
	private static void w2vPreprocess(){
		Parameter.con = Myjdbc.connect();
		Parameter.stm = Myjdbc.createStatement(Parameter.con);
		HashMap<String, String> concept_similarity_map = new HashMap<String, String>();
		HashMap<String, Integer> concept_id_map = new HashMap<String, Integer>();
		HashMap<Integer, String> id_concept_map = new HashMap<Integer, String>();
		
		String SQL;
		ResultSet rs;
		//retrieve concept information (concept, id, similarity)
		SQL = "select * from " + Parameter.storyTeller_DBname + "." + Parameter.storyTeller_concept_table;
		try {
			rs = Parameter.stm.executeQuery(SQL);
			rs.beforeFirst();
			while(rs.next()){
				String concept = rs.getString(Parameter.storyTeller_concept_concept);
				int id = rs.getInt(Parameter.storyTeller_concept_id);
				String similarity = rs.getString(Parameter.storyTeller_concept_similarity);
				concept_id_map.put(concept, id);
				id_concept_map.put(id, concept);
				concept_similarity_map.put(concept, similarity);
			}
			rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		//initialize file setting
		if (!(new File(Parameter.resultFolder)).mkdirs()) {
			System.out.println(Parameter.resultFolder + " folder already open!");
		}
		FileWriter saveFile = null;
		try {
			saveFile = new FileWriter(Parameter.resultFolder + "//w2v_all.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Parameter.fwriter = new BufferedWriter(saveFile);
		
		Iterator<String> conceptIter = concept_id_map.keySet().iterator();

		try {
			while(conceptIter.hasNext()){
				String curCpt = conceptIter.next();
				Set<String> causalConceptSet = new HashSet<String>();
				Parameter.nextStates = Utility.initNextStates();
				//get all causal concept
				for(String startState : Parameter.fabulaElement){
					List<String> nextStates = Parameter.nextStates.get(startState);
					for(String nextState : nextStates){
						String table = startState+"_"+nextState;
						SQL = ("select end from "+Parameter.storyTeller_DBname+"."+table+" where start = '"+curCpt.replace("'", "\\'")+"'");
						try {
							rs = Parameter.stm.executeQuery(SQL);
							rs.beforeFirst();
							while(rs.next()){
								causalConceptSet.add(rs.getString("end"));
							}
							rs.close();
						}catch(SQLException e){
							e.printStackTrace();
						}
					}
				}
				//get all backward causal concept
				Set<String> causalConceptSetBackward = new HashSet<String>();
				Parameter.nextStates = Utility.initNextStatesInverse();
				for(String endState : Parameter.fabulaElement){
					List<String> prevStates = Parameter.nextStates.get(endState);
					for(String prevState : prevStates){
						String table = prevState+"_"+endState;
						SQL = ("select start from "+Parameter.storyTeller_DBname+"."+table+" where end = '"+curCpt.replace("'", "\\'")+"'");
						try {
							rs = Parameter.stm.executeQuery(SQL);
							rs.beforeFirst();
							while(rs.next()){
								causalConceptSetBackward.add(rs.getString("start"));
							}
							rs.close();
						}catch(SQLException e){
							e.printStackTrace();
						}
					}
				}
				String text = curCpt;
				String[] similaritySplit = concept_similarity_map.get(curCpt).split(" ");
				if(similaritySplit[0]!=""){
					for(String sim : similaritySplit){
						text = text+" "+id_concept_map.get(Integer.parseInt(sim));
					}
				}
				for(String causalConcept : causalConceptSetBackward){
					text = text+" "+causalConcept;
				}

				for(String causalConcept : causalConceptSet){
					text = text+" "+causalConcept;
				}
				Parameter.fwriter.write(text);
				Parameter.fwriter.newLine();
			}
			Parameter.fwriter.flush();
			Parameter.fwriter.close();
			
			try {
				saveFile = new FileWriter(Parameter.resultFolder + "//w2v_causal.txt");
			} catch (IOException e) {
				e.printStackTrace();
			}
			Parameter.fwriter = new BufferedWriter(saveFile);
			
			conceptIter = concept_id_map.keySet().iterator();
			
			while(conceptIter.hasNext()){
				Parameter.nextStates = Utility.initNextStates();
				String curCpt = conceptIter.next();
				//get all causal concept
				Set<String> causalConceptSet = new HashSet<String>();
				for(String startState : Parameter.fabulaElement){
					List<String> nextStates = Parameter.nextStates.get(startState);
					for(String nextState : nextStates){
						String table = startState+"_"+nextState;
						SQL = ("select end from "+Parameter.storyTeller_DBname+"."+table+" where start = '"+curCpt.replace("'", "\\'")+"'");
						try {
							rs = Parameter.stm.executeQuery(SQL);
							rs.beforeFirst();
							while(rs.next()){
								causalConceptSet.add(rs.getString("end"));
							}
							rs.close();
						}catch(SQLException e){
							e.printStackTrace();
						}
					}
				}
				//get all backward causal concept
				Set<String> causalConceptSetBackward = new HashSet<String>();
				Parameter.nextStates = Utility.initNextStatesInverse();
				for(String endState : Parameter.fabulaElement){
					List<String> prevStates = Parameter.nextStates.get(endState);
					for(String prevState : prevStates){
						String table = prevState+"_"+endState;
						SQL = ("select start from "+Parameter.storyTeller_DBname+"."+table+" where end = '"+curCpt.replace("'", "\\'")+"'");
						try {
							rs = Parameter.stm.executeQuery(SQL);
							rs.beforeFirst();
							while(rs.next()){
								causalConceptSetBackward.add(rs.getString("start"));
							}
							rs.close();
						}catch(SQLException e){
							e.printStackTrace();
						}
					}
				}
				String text = curCpt;
				for(String causalConcept : causalConceptSetBackward){
					text = text+" "+causalConcept;
				}
				for(String causalConcept : causalConceptSet){
					text = text+" "+causalConcept;
				}
				Parameter.fwriter.write(text);
				Parameter.fwriter.newLine();
			}
			Parameter.fwriter.flush();
			Parameter.fwriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private static void dataPreprocess(){
		Parameter.con = Myjdbc.connect();
		Parameter.stm = Myjdbc.createStatement(Parameter.con);
		List<ArrayList<Integer>> attributeList = new ArrayList<ArrayList<Integer>>();
		HashMap<String, Integer> concept_id_map = new HashMap<String, Integer>();
	
		String SQL;
		ResultSet rs;
		
		int conceptNum = 0;
		Set<String> wordSet = new HashSet<String>();
		SQL = "select "+ Parameter.storyTeller_concept_concept +" from " + Parameter.storyTeller_DBname + "." + Parameter.storyTeller_concept_table;
		try {
			rs = Parameter.stm.executeQuery(SQL);
			rs.beforeFirst();
			while(rs.next()){
				String[] sp = rs.getString(Parameter.storyTeller_concept_concept).split("_");
				for(String str : sp){
					wordSet.add(str);
				}
				conceptNum+=1;
			}
			rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}

		List<String> wordList = new ArrayList<String>();
		wordList.addAll(wordSet);

		List<ArrayList<Integer>> similarityList = new ArrayList<ArrayList<Integer>>();
		List<ArrayList<Integer>> wordComponentList = new ArrayList<ArrayList<Integer>>();
		SQL = "select * from " + Parameter.storyTeller_DBname + "." + Parameter.storyTeller_concept_table;
		try {
			rs = Parameter.stm.executeQuery(SQL);
			rs.beforeFirst();
			while(rs.next()){
				String concept = rs.getString(Parameter.storyTeller_concept_concept);
				concept_id_map.put(concept, rs.getInt(Parameter.storyTeller_concept_id));
				String[] sim = rs.getString(Parameter.storyTeller_concept_similarity).split(" ");
				//onehotSimList shift for 1 
				ArrayList<Integer> simList = new ArrayList<Integer>();
				if(!sim[0].equals("")){
					for(String s:sim){
						simList.add(Integer.parseInt(s));
					}
				}
				similarityList.add(simList);
				String[] strSp = concept.split("_");
				ArrayList<Integer> wList = new ArrayList<Integer>();
				if(!strSp[0].equals("")){
					for(String s:strSp){
						wList.add(wordList.indexOf(s));
					}
				}
				wordComponentList.add(wList);
			}
			rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		Map<String, Integer> actionMap = new HashMap<String, Integer>();
		SQL = "select * from " + Parameter.storyTeller_DBname + "." + Parameter.storyTeller_action_table;
		try {
			rs = Parameter.stm.executeQuery(SQL);
			rs.beforeFirst();
			while(rs.next()){
				actionMap.put(rs.getString(Parameter.storyTeller_action_concept), rs.getInt(Parameter.storyTeller_action_location));
			}
			rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		if (!(new File(Parameter.resultFolder)).mkdirs()) {
			System.out.println(Parameter.resultFolder + " folder already open!");
		}
		FileWriter saveFile = null;
		try {
			saveFile = new FileWriter(Parameter.resultFolder + "//attribute.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Parameter.fwriter = new BufferedWriter(saveFile);
		
		try {
//			Parameter.fwriter.write("NULL ");
//			for(String concept : concept_id_map.keySet()){
//				Parameter.fwriter.write(" "+concept);
//			}
//			for(String term : wordList){
//				Parameter.fwriter.write(" "+term);
//			}

			int count=0;
			for(String concept : concept_id_map.keySet()){
				Parameter.fwriter.write(concept);
				for(int i=1; i<conceptNum+1; i++){
					if(similarityList.get(count).contains(i)){
						Parameter.fwriter.write(" 1");
					}else{
						Parameter.fwriter.write(" 0");
					}
				}
				Parameter.fwriter.write("|");
				for(int i=0; i<wordList.size(); i++){
					if(wordComponentList.get(count).contains(i)){
						Parameter.fwriter.write(" 1");
					}else{
						Parameter.fwriter.write(" 0");
					}
				}
				count++;
				Parameter.fwriter.newLine();
			}
			Parameter.fwriter.flush();
			Parameter.fwriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private static void conceptToTextFile(){
		Parameter.con = Myjdbc.connect();
		Parameter.stm = Myjdbc.createStatement(Parameter.con);
		Set<String> conceptSet = new HashSet<String>();
	
		String SQL;
		ResultSet rs;
		
		int conceptNum = 0;
		Set<String> wordSet = new HashSet<String>();
		SQL = "select "+ Parameter.storyTeller_concept_concept +" from " + Parameter.storyTeller_DBname + "." + Parameter.storyTeller_concept_table;
		try {
			rs = Parameter.stm.executeQuery(SQL);
			rs.beforeFirst();
			while(rs.next()){
				conceptSet.add(rs.getString(Parameter.storyTeller_concept_concept));
			}
			rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}

		if (!(new File(Parameter.resultFolder)).mkdirs()) {
			System.out.println(Parameter.resultFolder + " folder already open!");
		}
		FileWriter saveFile = null;
		try {
			saveFile = new FileWriter(Parameter.resultFolder + "//concept.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Parameter.fwriter = new BufferedWriter(saveFile);
		
		try {
			Iterator<String> iter = conceptSet.iterator();
			Parameter.fwriter.write(iter.next());
			while(iter.hasNext()){
				Parameter.fwriter.write(" "+iter.next());
			}
			Parameter.fwriter.flush();
			Parameter.fwriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private static void findLocationTerm(){
		Parameter.con = Myjdbc.connect();
		Parameter.stm = Myjdbc.createStatement(Parameter.con);
		Set<String> conceptSet = new HashSet <String> ();
		Set<String> locationSet = new HashSet <String> ();
		String SQL;
		ResultSet rs;
		
		SQL = "select " + Parameter.storyTeller_action_concept + " from " + Parameter.storyTeller_DBname + "." + Parameter.storyTeller_action_table;
		try {
			rs = Parameter.stm.executeQuery(SQL);
			rs.beforeFirst();
			while(rs.next()){
				conceptSet.add(rs.getString(Parameter.storyTeller_action_concept));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		for(String cpt : conceptSet){
			System.out.println("I "+cpt.replace("_", " "));
			Sentence sent = new Sentence("I "+cpt.replace("_", " "));
			if(sent.nerTags().contains("LOCATION")){
				locationSet.add(cpt);
			}
		}
		for(String cpt : locationSet){
			System.out.println(cpt);
		}
	}
	
	private void findLocation(){
		//go to, located, located near
		Parameter.con = Myjdbc.connect();
		Parameter.stm = Myjdbc.createStatement(Parameter.con);
		Set<String> locationSet = new HashSet <String> ();
		Set<String> gotoLocationSet = new HashSet <String> ();
		String SQL;
		ResultSet rs;
		String start, end;
		SQL = "select " + Parameter.conceptNet5_end + " from " + Parameter.conceptNet5_DBname + "." + Parameter.conceptNet5_table + " where rel = 'LocatedNear' or rel = 'AtLocation' or rel = 'locationofaction'";
		try {
			rs = Parameter.stm.executeQuery(SQL);
			rs.beforeFirst();
			while(rs.next()){
				locationSet.add(rs.getString(Parameter.conceptNet5_end));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		for(String rel : Parameter.causalRelation) {
			SQL = "select * from " + Parameter.conceptNet5_DBname + "." + Parameter.conceptNet5_table + " where rel = '" + rel + "'";
			try {
				rs = Parameter.stm.executeQuery(SQL);
				rs.beforeFirst();
				while(rs.next()){
					start = rs.getString(Parameter.conceptNet5_start);
					end = rs.getString(Parameter.conceptNet5_end);
					if(start.contains("go_to")) gotoLocationSet.add(start);
					if(end.contains("go_to")) gotoLocationSet.add(end);
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		for(String rel : Parameter.backwardCausalRelation) {
			SQL = "select * from " + Parameter.conceptNet5_DBname + "." + Parameter.conceptNet5_table + " where rel = '" + rel + "'";
			try {
				rs = Parameter.stm.executeQuery(SQL);
				rs.beforeFirst();
				while(rs.next()){
					start = rs.getString(Parameter.conceptNet5_start);
					end = rs.getString(Parameter.conceptNet5_end);
					if(start.contains("go_to")) gotoLocationSet.add(start);
					if(end.contains("go_to")) gotoLocationSet.add(end);
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		for(String s : locationSet){
			System.out.println(s);
		}
		
	}
	
	private static void collectCandidateAction(){
		Parameter.con = Myjdbc.connect();
		Parameter.stm = Myjdbc.createStatement(Parameter.con);
		Set<String> actionSet = new HashSet <String> ();
		String[] actionLink = new String[] {"a_e", "a_p", "g_a", "ie_a"}; 
		String[] actionInLink = new String[] {"a_e", "a_p"}; 
		String[] actionOutLink = new String[] {"g_a", "ie_a"};
		HashMap<String, Integer> actionIndegree = new HashMap<String, Integer>();
		HashMap<String, Integer> actionOutdegree = new HashMap<String, Integer>();
		
		String SQL;
		ResultSet rs;
		for(String link : actionInLink){
			SQL = "select " + Parameter.storyTeller_fabula_start + " from " + Parameter.storyTeller_DBname + "." + link;
			try {
				rs = Parameter.stm.executeQuery(SQL);
				rs.beforeFirst();
				while(rs.next()){
					actionSet.add(rs.getString(Parameter.storyTeller_fabula_start));
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		for(String link : actionOutLink){
			SQL = "select " + Parameter.storyTeller_fabula_end + " from " + Parameter.storyTeller_DBname + "." + link;
			try {
				rs = Parameter.stm.executeQuery(SQL);
				rs.beforeFirst();
				while(rs.next()){
					actionSet.add(rs.getString(Parameter.storyTeller_fabula_end));
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		
		for(String action: actionSet){
			System.out.println(action);
			actionIndegree.put(action, 0);
			actionOutdegree.put(action, 0);
			for(String link : actionLink){
				SQL = "select count(*) from " + Parameter.storyTeller_DBname + "." + link +" where " + Parameter.storyTeller_fabula_end + " = '" + action.replace("'", "\\'")+"'";
				try {
					rs = Parameter.stm.executeQuery(SQL);
					rs.beforeFirst();
					if(rs.next()){
						actionIndegree.put(action, actionIndegree.get(action)+rs.getInt(1));
					}
					rs.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
				SQL = "select count(*) from " + Parameter.storyTeller_DBname + "." + link +" where " + Parameter.storyTeller_fabula_start + " = '" + action.replace("'", "\\'")+"'";
				try {
					rs = Parameter.stm.executeQuery(SQL);
					rs.beforeFirst();
					if(rs.next()){
						actionOutdegree.put(action, actionOutdegree.get(action)+rs.getInt(1));
					}
					rs.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		
		Set <String> candidateActionSet = new HashSet <String>();
		System.out.println("all action : "+actionSet.size()+"!!!!!!");
		int count = 0;
		for(String action: actionSet){
			if(actionOutdegree.get(action) == 0 || actionIndegree.get(action) == 0) continue;
			count++;
		}
		System.out.println("in and out > 0 : "+count+"!!!!!!");
		count=0;
		for(String action: actionSet){
			if(actionOutdegree.get(action) < 2 || actionIndegree.get(action) < 2) continue;
			candidateActionSet.add(action);
			count++;
		}
		System.out.println("in and out > 1 : "+count+"!!!!!!");
		count=0;
		for(String action: actionSet){
			if(actionOutdegree.get(action) < 3 || actionIndegree.get(action) < 3) continue;
			count++;
		}
		System.out.println("in and out > 2 : "+count+"!!!!!!");
		count=0;
		for(String action: actionSet){
			if(actionOutdegree.get(action) < 4 || actionIndegree.get(action) < 4) continue;
			count++;
		}
		System.out.println("in and out > 3 : "+count+"!!!!!!");
		count=0;
		for(String action: actionSet){
			if(actionOutdegree.get(action) < 5 || actionIndegree.get(action) < 5) continue;
			count++;
		}
		System.out.println("in and out > 4 : "+count+"!!!!!!");
		count=0;
		for(String action: actionSet){
			if(actionOutdegree.get(action) < 6 || actionIndegree.get(action) < 6) continue;
			count++;
		}
		System.out.println("in and out > 5 : "+count+"!!!!!!");
		count=0;
		for(String action: actionSet){
			if(actionOutdegree.get(action) < 11 || actionIndegree.get(action) < 11) continue;
			count++;
		}
		System.out.println("in and out > 10 : "+count+"!!!!!!");
		
		try{
			SQL = "truncate table "+Parameter.storyTeller_DBname +".action";
			Parameter.stm.executeUpdate(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for(String str : candidateActionSet){
			try{
				SQL = "insert into " + Parameter.storyTeller_DBname +".action" +
						" (concept, indegree, outdegree, type) " + 
						"values('"+str.replace("'", "\\'")+"', "+actionIndegree.get(str)+", "+actionOutdegree.get(str)+", 0)";
				Parameter.stm.executeUpdate(SQL);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}



