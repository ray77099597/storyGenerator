package treeNode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import generator.Parameter;
import others.StoryRecord;
import others.StoryStructure;
import others.Timer;
import others.Utility;

public class TwoCharacterTreeNode{
	protected Random r;
	protected int nAction;
	protected String state1, state2;
	protected String relation1, relation2;
	protected String concept1, concept2;
	protected double simulationScore;
	protected double weight1, weight2;
	protected boolean expanded;
	//use for selection
	protected TwoCharacterTreeNode[] children; //size = children1Num * children2Num
	//use to check where to trim the redundant node
	protected String[] children1Cpt;
	protected String[] children2Cpt;
	protected Set<Integer> nextGoalNum;
	protected Set<Integer> nextRedundantNum1;
	protected Set<Integer> nextRedundantNum2;
	protected int nVisits;
	protected double totValue;
	protected int[] numArr1, numArr2;
	protected int step;
	protected int index1, index2;
	protected Set<Integer> similarity1;
	protected Set<Integer> similarity2;
	boolean firstGoalIsFound = false;
	boolean secondGoalIsFound = false;
	
	public TwoCharacterTreeNode(String state1, String state2, String concept1, String concept2, int step, String relation1, String relation2, double weight1, double weight2, boolean firstGoalIsFound, boolean secondGoalIsFound) {
		this.r = new Random(System.currentTimeMillis());
		this.nAction = 0;
		this.state1 = state1;
		this.state2 = state2;
		this.concept1 = concept1;
		this.concept2 = concept2;
		this.step = step;
		this.simulationScore = 0.0;
		this.totValue = 0.0;
		this.relation1 = relation2;
		this.relation1 = relation2;
		this.weight1 = weight1;
		this.weight2 = weight2;
		this.expanded = false;
		this.nVisits = 0;
		this.firstGoalIsFound = firstGoalIsFound;
		this.secondGoalIsFound = secondGoalIsFound;
		this.nextGoalNum = new HashSet<Integer>();
		this.nextRedundantNum1 = new HashSet<Integer>();
		this.nextRedundantNum2 = new HashSet<Integer>();
		this.similarity1 = new HashSet<Integer>();
		this.similarity2 = new HashSet<Integer>();
		StringBuffer selectSQL = new StringBuffer();
		ResultSet rs;
		int index = -1;
		String[] similarity = null;
		try {
			selectSQL.append("select ").append("* from ").append(Parameter.storyTeller_DBname).append(".").append("concept").append(" where ").append("concept").append(" = '").append(this.concept1.replace("'", "\\'")).append("'");
			rs = Parameter.stm.executeQuery(selectSQL.toString());
			rs.beforeFirst();
			if(rs.next()){
				index = rs.getInt("id");
				String str = rs.getString("simset");
				if (!str.equals("")){
					similarity = str.split(" ");
					for(String s :similarity){
						if(!s.equals(""))
							this.similarity1.add(Integer.parseInt(s));
					}
				}
			}
			rs.close();
			this.index1 = index;
			selectSQL = new StringBuffer();
			selectSQL.append("select ").append("* from ").append(Parameter.storyTeller_DBname).append(".").append("concept").append(" where ").append("concept").append(" = '").append(this.concept2.replace("'", "\\'")).append("'");
			rs = Parameter.stm.executeQuery(selectSQL.toString());
			rs.beforeFirst();
			if(rs.next()){
				index = rs.getInt("id");
				String str = rs.getString("simset");
				if (!str.equals("")){
					similarity = str.split(" ");
					for(String s :similarity){
						if(!s.equals(""))
							this.similarity2.add(Integer.parseInt(s));
					}
				}
			}
			rs.close();
			this.index2 = index;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void selectAction() throws SQLException {

		List<StoryRecord> story1 = new LinkedList<StoryRecord>();
		List<StoryRecord> story2 = new LinkedList<StoryRecord>();
		List<TwoCharacterTreeNode> visited = new LinkedList<TwoCharacterTreeNode>();
		Set<String> examine1 = new HashSet<String>();
		Set<String> examine2 = new HashSet<String>();
		Set<Integer> indexExamine1 = new HashSet<Integer>();
		Set<Integer> indexExamine2 = new HashSet<Integer>();
		Timer t = new Timer();
		TwoCharacterTreeNode cur = this;
		TwoCharacterTreeNode prev = null;
		visited.add(this);
		story1.add(this.toStoryRecord(this.state1, this.concept1, this.relation1, this.weight1));
		story2.add(this.toStoryRecord(this.state2, this.concept2, this.relation2, this.weight2));
		//store story to check redundant
		examine1.add(this.concept1);
		examine2.add(this.concept2);
		indexExamine1.addAll(this.similarity1);
		indexExamine2.addAll(this.similarity2);
		
		//search for the high possibility expand node
		while (!cur.isLeaf()) {
			prev = cur;
			examine1.add(prev.concept1);
			examine2.add(prev.concept2);
			indexExamine1.addAll(prev.similarity1);
			indexExamine2.addAll(prev.similarity2);
			cur = cur.select();
			visited.add(cur);
			if(!cur.state1.equals("")){
				story1.add(this.toStoryRecord(cur.state1, cur.concept1, cur.relation1, cur.weight1));
			}
			if(!cur.state2.equals("")){
				story2.add(this.toStoryRecord(cur.state2, cur.concept2, cur.relation2, cur.weight2));
			}
		}
		t.stopTimer();
		Parameter.selectTimer.accumTime(t.getTotalTime());
		
		//story is found
		//if same length and same goal
		//TODO should be some way to refine
		if (cur.concept1.equals(Parameter.curGoalConcept)) {
			cur.firstGoalIsFound = true;
		}
		if (cur.concept2.equals(Parameter.curGoalConcept)) {
			cur.secondGoalIsFound = true;
		}
		if (cur.firstGoalIsFound && cur.secondGoalIsFound) {
			// remember trim's goal index
			TwoCharacterTreeNode nodeToTrimFrom = visited.get(visited.size() - 2);
			//first story
			for (int i = 0; i < nodeToTrimFrom.children.length; i++) {
				if (!nodeToTrimFrom.nextGoalNum.contains(i) && nodeToTrimFrom.children[i] != null && nodeToTrimFrom.children[i].equals(cur)) {
					double sc = 0.0;
					List <String> cList = new LinkedList<String>();
					for (StoryRecord k : story1) {
						cList.add(k.getConcept());
						sc += k.getScore();
					}
					double lscore = Parameter.lengthModel.getValue(story1.size(), Parameter.curStoryLength);
					double cScore = Parameter.constraintModel.getValue(cList, Parameter.storyCnt);
					double loScore = Parameter.locationConstraintModel.getValue(cList);
//					sc = Parameter.ScoreConstant * constraintScore * (sc / (story.size() - 1)) * Parameter.gaussianDistribution.value(story.size()) / (Parameter.MaxScore * Parameter.gaussianDistribution.value(Parameter.story[Parameter.storyCnt].storylength));
					sc = Parameter.ScoreConstant * loScore * cScore * (sc / (story1.size() - 1)) / (Parameter.MaxScore) * lscore;
					
					StoryStructure storystructure = new StoryStructure(sc, story1);
					Parameter.storyList.add(storystructure);
					
					sc = 0.0;
					cList = new LinkedList<String>();
					for (StoryRecord k : story2) {
						cList.add(k.getConcept());
						sc += k.getScore();
					}
					lscore = Parameter.lengthModel.getValue(story2.size(), Parameter.curStoryLength);
					cScore = Parameter.constraintModel.getValue(cList, Parameter.storyCnt);
					loScore = Parameter.locationConstraintModel.getValue(cList);
//					sc = Parameter.ScoreConstant * constraintScore * (sc / (story.size() - 1)) * Parameter.gaussianDistribution.value(story.size()) / (Parameter.MaxScore * Parameter.gaussianDistribution.value(Parameter.story[Parameter.storyCnt].storylength));
					sc = Parameter.ScoreConstant * loScore * cScore * (sc / (story2.size() - 1)) / (Parameter.MaxScore) * lscore;
					
					storystructure = new StoryStructure(sc, story2);
					Parameter.storyList2.add(storystructure);
					
					cur.expanded = true;
					nodeToTrimFrom.nextGoalNum.add(i);
					break;
				}
			}
		}
		
		//trim node from tree if we found it
		if ((examine1.contains(cur.concept1) || indexExamine1.contains(cur.index1)) && visited.size() >= 2) {
			TwoCharacterTreeNode nodeToTrimFrom = visited.get(visited.size() - 2);
			/**
			 * search the index of the node
			 * 1.parent's node-to-trim contains the node
			 * 2.parent has children
			 * 3.child equal node
			 */
			for (int i = 0; i < nodeToTrimFrom.children1Cpt.length; i++) {
				if (!nodeToTrimFrom.nextRedundantNum1.contains(i)&& nodeToTrimFrom.children1Cpt[i] != null 
						&& nodeToTrimFrom.children1Cpt[i].equals(cur.concept1)) {
					cur.expanded = true;
					nodeToTrimFrom.nextRedundantNum1.add(i);
					break;
				}
			}
		}
		
		if ((examine2.contains(cur.concept2) || indexExamine2.contains(cur.index2)) && visited.size() >= 2) {
			TwoCharacterTreeNode nodeToTrimFrom = visited.get(visited.size() - 2);
			for (int i = 0; i < nodeToTrimFrom.children2Cpt.length; i++) {
				if (!nodeToTrimFrom.nextRedundantNum2.contains(i)&& nodeToTrimFrom.children2Cpt[i] != null 
						&& nodeToTrimFrom.children2Cpt[i].equals(cur.concept2)) {
					cur.expanded = true;
					nodeToTrimFrom.nextRedundantNum2.add(i);
					break;
				}
			}
		}
		
		// System.out.println("--------------start expand----------------");
		t.resetTimer();
		cur.expand();
		t.stopTimer();
		Parameter.expandTimer.accumTime(t.getTotalTime());
//		 System.out.println("--------------select after expand----------------");
		TwoCharacterTreeNode newNode = cur.select();
		if (newNode != null) {
			visited.add(newNode);
//			 System.out.println("--------------start simulation----------------");
			t.resetTimer();
			newNode.simulationScore = simulation(visited);
			t.stopTimer();
			Parameter.simulTimer.accumTime(t.getTotalTime());
			// System.out.println(newNode.score);
			for (TwoCharacterTreeNode node : visited) {
				node.updateStats(newNode.simulationScore);
			}
		} else {
			for (TwoCharacterTreeNode node : visited) {
				node.updateStats(0.0);
			}
		}

	}
	
	protected void expand() throws SQLException {
		if(this.expanded){
			return;
		}
		this.expanded = true;
		List<String> nextStates1 = Parameter.nextStates.get(state1);
		List<String> nextStates2 = Parameter.nextStates.get(state2);
		ResultSet rs;
		this.numArr1 = new int[nextStates1.size()];
		this.numArr2 = new int[nextStates2.size()];
		String nextState = null;
		String prevState = this.state1;
		String tableName = null;
		String findPos = null;
		StringBuffer countSQL = new StringBuffer();
		for (int i = 0; i < nextStates1.size(); i++) {
			if (i != 0)
				countSQL.append(" union ");
			nextState = nextStates1.get(i);
			if(Parameter.curStorytype.equals(Parameter.StoryType.UNLIMITED_INVERSE_STORY)){
				tableName = nextState + "_" + prevState;
				findPos = "end";
			}else{
				tableName = prevState + "_" + nextState;
				findPos = "start";
			}
			countSQL.append("select ").append((i + 1)).append(" as id , count(*) from ").append(Parameter.storyTeller_DBname).append(".").append(tableName).append(" where ").append(findPos).append(" = '").append(this.concept1.replace("'", "\\'")).append("'");
				
		}
		rs = Parameter.stm.executeQuery(countSQL.toString());
		rs.beforeFirst();
		int tempCnt = 0;
		while (rs.next()) {
			numArr1[tempCnt++] = rs.getInt("count(*)");
		}
		rs.close();
		int totalrsNum = 0;
		for (int i = 0; i < nextStates1.size(); i++) {
			totalrsNum += numArr1[i];
		}
		if (totalrsNum == 0) {
			// System.out.println("cannot expand, no child!!!");
			return;
		}
		children1Cpt = new String[totalrsNum];
		
		nextState = null;
		prevState = this.state2;
		tableName = null;
		findPos = null;
		countSQL = new StringBuffer();
		for (int i = 0; i < nextStates2.size(); i++) {
			if (i != 0)
				countSQL.append(" union ");
			nextState = nextStates2.get(i);
			if(Parameter.curStorytype.equals(Parameter.StoryType.UNLIMITED_INVERSE_STORY)){
				tableName = nextState + "_" + prevState;
				findPos = "end";
			}else{
				tableName = prevState + "_" + nextState;
				findPos = "start";
			}
			countSQL.append("select ").append((i + 1)).append(" as id , count(*) from ").append(Parameter.storyTeller_DBname).append(".").append(tableName).append(" where ").append(findPos).append(" = '").append(this.concept2.replace("'", "\\'")).append("'");
				
		}
		rs = Parameter.stm.executeQuery(countSQL.toString());
		rs.beforeFirst();
		tempCnt = 0;
		while (rs.next()) {
			numArr2[tempCnt++] = rs.getInt("count(*)");
		}
		rs.close();
		totalrsNum = 0;
		for (int i = 0; i < nextStates2.size(); i++) {
			totalrsNum += numArr2[i];
		}
		if (totalrsNum == 0) {
			// System.out.println("cannot expand, no child!!!");
			return;
		}
		children2Cpt = new String[totalrsNum];
		
		children = new TwoCharacterTreeNode[children1Cpt.length * children2Cpt.length];
		
	}
	
	protected double simulation(List<TwoCharacterTreeNode> visited) throws SQLException{
		int rndNum;
		TwoCharacterTreeNode curNode = visited.get(visited.size() - 1);
		List<String> nextStates1 = Parameter.nextStates.get(curNode.state1);
		List<String> nextStates2 = Parameter.nextStates.get(curNode.state2);
		ResultSet rs;
		int[] numArr = new int[Parameter.fabulaElement.length];
		String nextState1 = null;
		String nSConcept1 = curNode.concept1;
		String nSRelation1 = curNode.relation1;
		double nSScore1 = curNode.weight1;
		String prevState1 = curNode.state1;
		String nextState2 = null;
		String nSConcept2 = curNode.concept2;
		String nSRelation2 = curNode.relation2;
		double nSScore2 = curNode.weight2;
		String prevState2 = curNode.state2;
		String tableName = null;
		String startPos = null;
		String endPos = null;
		
		StringBuffer selectSQL = new StringBuffer();
		StringBuffer countSQL = new StringBuffer();

		List<TwoCharacterTreeNode> simulationStroy = new LinkedList<TwoCharacterTreeNode>();
		Set<String> examine1 = new HashSet<String>();
		Set<Integer> indexExamine1 = new HashSet<Integer>();
		Set<String> examine2 = new HashSet<String>();
		Set<Integer> indexExamine2 = new HashSet<Integer>();
		
		for (TwoCharacterTreeNode t : visited.subList(0, visited.size()-2)) {
			examine1.add(t.concept1);
			indexExamine1.addAll(t.similarity1);
			examine2.add(t.concept2);
			indexExamine2.addAll(t.similarity2);
		}
		
		while (curNode.step <= Parameter.fabulaLimit) {
			simulationStroy.add(curNode);
//			System.out.println(curNode.concept);
			if(examine1.contains(curNode.concept1) || indexExamine1.contains(curNode.index1))
				return 0.0;
			if(examine2.contains(curNode.concept2) || indexExamine2.contains(curNode.index2))
				return 0.0;
//			examine.add(curNode.concept);
			//check constrain term
			

			if (curNode.concept1.equals(Parameter.curGoalConcept)) {
				curNode.firstGoalIsFound = true;
			}
			if (curNode.concept2.equals(Parameter.curGoalConcept)) {
				curNode.secondGoalIsFound = true;
			}
			
			if(curNode.firstGoalIsFound == true && curNode.secondGoalIsFound == true){
				return calculateSimilarityScore(simulationStroy);
			}
			
			if(!curNode.firstGoalIsFound){
				examine1.add(curNode.concept1);
				indexExamine1.addAll(curNode.similarity1);
				for (int i = 0; i < nextStates1.size(); i++) {
					if (i != 0)
						countSQL.append(" union ");
					nextState1 = nextStates1.get(i);
					if(Parameter.curStorytype.equals(Parameter.StoryType.UNLIMITED_INVERSE_STORY)){
						tableName = nextState1 + "_" + prevState1;
						startPos = "end";
					}else{
						tableName = prevState1 + "_" + nextState1;
						startPos = "start";
					}
					countSQL.append("select ").append((i + 1)).append(" as id , count(*) from ").append(Parameter.storyTeller_DBname).append(".").append(tableName).append(" where ").append(startPos).append(" = '").append(curNode.concept1.replace("'", "\\'")).append("'");
					
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
				for (int i = 0; i < nextStates1.size(); i++) {
					totalrsNum += numArr[i];
				}
				if (totalrsNum == 0) {
					break;
				}
	
				// pick nActions different random numbers from 1 to totoalrsNum
				rndNum = r.nextInt(totalrsNum);
				int temprsNum = 0;
				int cur = 0;
				for (cur = 0; cur < nextStates1.size(); cur++) {
					temprsNum += numArr[cur];
					if (rndNum < temprsNum)
						break;
				}
				nextState1 = nextStates1.get(cur);
				if(Parameter.curStorytype.equals(Parameter.StoryType.UNLIMITED_INVERSE_STORY)){
					tableName = nextState1 + "_" + prevState1;
					startPos = "end";
					endPos = "start";
				}else{
					tableName = prevState1 + "_" + nextState1;
					startPos = "start";
					endPos = "end";
				}
				selectSQL.append("select ").append("* from ").append(Parameter.storyTeller_DBname).append(".").append(tableName).append(" where ").append(startPos).append(" = '").append(curNode.concept1.replace("'", "\\'")).append("'");
				rs = Parameter.stm.executeQuery(selectSQL.toString());
				selectSQL.delete(0, selectSQL.length());
				rs.absolute(rndNum - temprsNum + numArr[cur] + 1);
				nSConcept1 = rs.getString(endPos);
				nSRelation1 = rs.getString("rel");
				nSScore1 = rs.getDouble("weight");
				rs.close();
			}else{
				nextState1 = "";
				nSConcept1 = "";
				nSRelation1 = "";
				nSScore1 = 0.0;
			}
			if(!curNode.secondGoalIsFound){
				examine2.add(curNode.concept2);
				indexExamine2.addAll(curNode.similarity2);
				for (int i = 0; i < nextStates2.size(); i++) {
					if (i != 0)
						countSQL.append(" union ");
					nextState2 = nextStates2.get(i);
					if(Parameter.curStorytype.equals(Parameter.StoryType.UNLIMITED_INVERSE_STORY)){
						tableName = nextState2 + "_" + prevState2;
						startPos = "end";
					}else{
						tableName = prevState2 + "_" + nextState2;
						startPos = "start";
					}
					countSQL.append("select ").append((i + 1)).append(" as id , count(*) from ").append(Parameter.storyTeller_DBname).append(".").append(tableName).append(" where ").append(startPos).append(" = '").append(curNode.concept2.replace("'", "\\'")).append("'");
					
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
				for (int i = 0; i < nextStates2.size(); i++) {
					totalrsNum += numArr[i];
				}
				if (totalrsNum == 0) {
					break;
				}
	
				// pick nActions different random numbers from 1 to totoalrsNum
				rndNum = r.nextInt(totalrsNum);
				int temprsNum = 0;
				int cur = 0;
				for (cur = 0; cur < nextStates2.size(); cur++) {
					temprsNum += numArr[cur];
					if (rndNum < temprsNum)
						break;
				}
				nextState2 = nextStates2.get(cur);
				if(Parameter.curStorytype.equals(Parameter.StoryType.UNLIMITED_INVERSE_STORY)){
					tableName = nextState2 + "_" + prevState2;
					startPos = "end";
					endPos = "start";
				}else{
					tableName = prevState2 + "_" + nextState2;
					startPos = "start";
					endPos = "end";
				}
				selectSQL.append("select ").append("* from ").append(Parameter.storyTeller_DBname).append(".").append(tableName).append(" where ").append(startPos).append(" = '").append(curNode.concept2.replace("'", "\\'")).append("'");
				rs = Parameter.stm.executeQuery(selectSQL.toString());
				selectSQL.delete(0, selectSQL.length());
				rs.absolute(rndNum - temprsNum + numArr[cur] + 1);
				nSConcept2 = rs.getString(endPos);
				nSRelation2 = rs.getString("rel");
				nSScore2 = rs.getDouble("weight");
				rs.close();
			}else{
				nextState2 = "";
				nSConcept2 = "";
				nSRelation2 = "";
				nSScore2 = 0.0;
			}
			
			curNode = new TwoCharacterTreeNode(nextState1, nextState2, nSConcept1, nSConcept2, curNode.step + 1, nSRelation1, nSRelation2, nSScore1, nSScore2, curNode.firstGoalIsFound, curNode.secondGoalIsFound);

			prevState1 = curNode.state1;
			if(!curNode.firstGoalIsFound){
				nextStates1 = Parameter.nextStates.get(curNode.state1);
			}else{
				nextStates1 = null;
			}
			prevState2 = curNode.state2;
			if(!curNode.secondGoalIsFound){
				nextStates2 = Parameter.nextStates.get(curNode.state2);
			}else{
				nextStates2 = null;
			}
		}
		return 0.0;
	}
	

	protected TwoCharacterTreeNode select() {
		if (children1Cpt == null || children1Cpt.length == nextRedundantNum1.size() || 
			children2Cpt == null  || children2Cpt.length == nextRedundantNum2.size() ||
			children == null  || children.length == nextGoalNum.size()){
//			System.out.println("return null");
			return null;
		}
		
		TwoCharacterTreeNode selected = null;
		int selectedI = -1;
		double uctValue = 0;
		double uctProbability[] = new double[children.length];
		for (int i = 0; i < children.length; i++) {
			if (children[i] == null) {
				uctValue = (Parameter.alpha) * Math.sqrt(Math.log(nVisits + 1) / (1 + Parameter.epsilon)) + r.nextDouble() * Parameter.epsilon;
			} else {
				uctValue = (1 - Parameter.alpha) * children[i].totValue + (Parameter.alpha) * Math.sqrt(Math.log(nVisits + 1) / (children[i].nVisits + 1 + Parameter.epsilon)) + r.nextDouble() * Parameter.epsilon;
			}
			uctProbability[i] = uctValue;
		}
		double total = 0.0;
		for (int i = 0; i < children.length; i++) {
			//System.out.println(uctProbability[i]);
			//			uctProbability[i] = Math.exp(uctProbability[i]);
			if(this.nextRedundantNum1.contains(i) || this.nextGoalNum.contains(i)){
				uctProbability[i] = r.nextDouble() * Parameter.epsilon;
			}else{
				uctProbability[i] = Math.pow(uctProbability[i], 2);
			}
			total += uctProbability[i];
		}			
			
		for (int i = 0; i < uctProbability.length; i++) {
			uctProbability[i] = uctProbability[i] / total;
		}
		selectedI = Utility.sampling(r, uctProbability);
		
		selected = children[selectedI];
		
		
		List<String> nextStates1 = Parameter.nextStates.get(this.state1);
		List<String> nextStates2 = Parameter.nextStates.get(this.state2);
		String tableName = null;
		String startPos = null;
		String endPos = null;
		
		//if node is not found, expand the node
		if (selected == null) {
			ResultSet rs;
			String nextState1 = null;
			String nextState2 = null;
			String prevState1 = this.state1;
			String prevState2 = this.state2;
			String nSConcept1 = null, nSConcept2 = null;
			String nSRelation1 = null, nSRelation2 = null;
			double nSScore1 = 0, nSScore2 = 0;
			int selectedI1 = selectedI / children2Cpt.length;
			int selectedI2 = selectedI % children2Cpt.length;
			
			StringBuffer SQL = new StringBuffer();
			int temprsNum = 0;
			int cur;
			for (cur = 0; cur < nextStates1.size(); cur++) {
				temprsNum += numArr1[cur];
				if (selectedI1 < temprsNum)
					break;
			}   
			nextState1 = nextStates1.get(cur);
			if(Parameter.curStorytype.equals(Parameter.StoryType.UNLIMITED_INVERSE_STORY)){
				tableName = nextState1 + "_" + prevState1;
				startPos = "end";
				endPos = "start";
			}else{
				tableName = prevState1 + "_" + nextState1;
				startPos = "start";
				endPos = "end";
			}
			try {
				SQL.append("select ").append("* from ").append(Parameter.storyTeller_DBname).append(".").append(tableName).append(" where ").append(startPos).append(" = '").append(this.concept1.replace("'", "\\'")).append("'");
				rs = Parameter.stm.executeQuery(SQL.toString());
				rs.absolute(selectedI1 - temprsNum + numArr1[cur] + 1);
				nSConcept1 = rs.getString(endPos);
				nSRelation1 = rs.getString("rel");
				nSScore1 = rs.getDouble("weight");
				SQL.delete(0, SQL.length());
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			temprsNum = 0;
			SQL = new StringBuffer();
			for (cur = 0; cur < nextStates2.size(); cur++) {
				temprsNum += numArr2[cur];
				if (selectedI2 < temprsNum)
					break;
			}
			nextState2 = nextStates2.get(cur);
			if(Parameter.curStorytype.equals(Parameter.StoryType.UNLIMITED_INVERSE_STORY)){
				tableName = nextState2 + "_" + prevState2;
				startPos = "end";
				endPos = "start";
			}else{
				tableName = prevState2 + "_" + nextState2;
				startPos = "start";
				endPos = "end";
			}
			try {
				SQL.append("select ").append("* from ").append(Parameter.storyTeller_DBname).append(".").append(tableName).append(" where ").append(startPos).append(" = '").append(this.concept2.replace("'", "\\'")).append("'");
				rs = Parameter.stm.executeQuery(SQL.toString());
				rs.absolute(selectedI2 - temprsNum + numArr2[cur] + 1);
				nSConcept2 = rs.getString(endPos);
				nSRelation2 = rs.getString("rel");
				nSScore2 = rs.getDouble("weight");
				SQL.delete(0, SQL.length());
				rs.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			TwoCharacterTreeNode tn = new TwoCharacterTreeNode(nextStates1.get(cur), nextStates2.get(cur), nSConcept1, nSConcept2, step + 1, nSRelation1, nSRelation2, nSScore1, nSScore2, this.firstGoalIsFound, this.secondGoalIsFound);
			children[selectedI] = tn;
			nAction++;
			selected = children[selectedI];

			System.out.println(selected.concept1 + " " + selected.concept2);
		}
		return selected;
	}
	
	public boolean isLeaf() {
		if (children1Cpt == null || children1Cpt.length == nextRedundantNum1.size() || 
				children2Cpt == null  || children2Cpt.length == nextRedundantNum2.size() ||
				children == null  || children.length == nextGoalNum.size()
				){
			return true;
		}else {
			return false;
		}
	}

	public void updateStats(double value) {
		nVisits++;
		totValue = totValue > value ? totValue : value;
	}

	public int arity() {
		return children == null ? 0 : nAction;
	}

	public StoryRecord toStoryRecord(String state, String relation, String concept, double weight) {
		return new StoryRecord(state, relation, concept, this.totValue, this.step, weight);
	}
	
	@Override
	public boolean equals(Object obj) {
		TwoCharacterTreeNode t = (TwoCharacterTreeNode) obj;
		return (this.concept1.equals(t.concept1) && this.state1.equals(t.state1) && this.relation1.equals(t.relation1) &&
				this.concept2.equals(t.concept2) && this.state2.equals(t.state2) && this.relation2.equals(t.relation2) &&
				this.simulationScore == t.simulationScore && this.totValue == t.totValue);
	}
	
	private double calculateSimilarityScore(List<TwoCharacterTreeNode> simulationStroy){
		double sc1 = 0.0, sc2 = 0.0;
		double cScore1 = 1.0, cScore2 = 1.0;
		double loScore1 = 1.0, loScore2 = 1.0;
		List <String> cList1 = new LinkedList<String>();
		List <String> cList2 = new LinkedList<String>();
		for (TwoCharacterTreeNode k : simulationStroy) {
			if(k.state1 != ""){
				sc1 += k.weight1;
				cList1.add(k.concept1);
			}
			if(k.state2 != ""){
				sc2 += k.weight2;
				cList2.add(k.concept2);
			}
		}
		cScore1 = Parameter.constraintModel.getValue(cList1, Parameter.storyCnt);
		cScore2 = Parameter.constraintModel.getValue(cList2, Parameter.storyCnt);
		loScore1 = Parameter.locationConstraintModel.getValue(cList1);
		loScore2 = Parameter.locationConstraintModel.getValue(cList2);
		double lScore1 = Parameter.lengthModel.getValue(cList1.size(), Parameter.curStoryLength);
		double lScore2 = Parameter.lengthModel.getValue(cList2.size(), Parameter.curStoryLength);
//		System.out.println(cScore+" "+loScore+" "+(sc / simulationStroy.size()) / (Parameter.MaxScore) +" "+lScore);
		return ((Parameter.ScoreConstant * loScore1 * cScore1 * (sc1 / simulationStroy.size()) / (Parameter.MaxScore)* lScore1) +
				(Parameter.ScoreConstant * loScore2 * cScore2 * (sc2 / simulationStroy.size()) / (Parameter.MaxScore)* lScore2));
	}
	
}
