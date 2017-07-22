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
import others.Timer;
import others.Utility;

/**
 * TreeNode of Monte Carlo Tree Search
 * @author liqimou
 * r : random seed
 * nAction : numbers of generated children
 * relation : relation between current and previous treeNode
 * concept : concept of current treeNode 
 * simulationScore : score of simulation score of the expanded node
 * weight : weight between current and previous treeNode
 * expanded : the node is expanded or not
 * children : children of the current treeNode
 * nextGoalNum : the index set of the goal if the goal is in the children
 * nextRedundantNum : the index set of the redundant if the redundant is in the children
 * nVisits : numbers of visit of the current node 
 * totValue : value to select with the exploration and exploitation dilemma
 * numArr : array to store number of each next state
 * step : node in witch steps of MCTS
 * index : index of the concept in storyTeller_DBname
 * similarity : the similarity set
 */
public abstract class TreeNodeAbstract {
	protected Random r;
	protected int nAction;
	protected String state;
	protected String relation;
	private String concept;
	protected double simulationScore;
	protected double weight;
	protected boolean expanded;
	protected TreeNodeAbstract[] children;
	protected Set<Integer> nextGoalNum;
	protected Set<Integer> nextRedundantNum;
	protected int nVisits;
	protected double totValue;
	protected int[] numArr;
	private int step;
	protected int index;
	protected Set<Integer> similarity;

	public TreeNodeAbstract(String state, String concept, int step, String relation, double weight) {
		this.r = new Random(System.currentTimeMillis());
		this.nAction = 0;
		this.setState(state);
		this.setConcept(concept);
		this.setStep(step);
		this.simulationScore = 0.0;
		this.totValue = 0.0;
		this.relation = relation;
		this.weight = weight;
		this.expanded = false;
		this.nVisits = 0;
		this.nextGoalNum = new HashSet<Integer>();
		this.nextRedundantNum = new HashSet<Integer>();
		this.similarity = new HashSet<Integer>();
		StringBuffer selectSQL = new StringBuffer();
		ResultSet rs;
		int index = -1;
		String[] similarity = null;
		try {
			selectSQL.append("select ").append("* from ").append(Parameter.storyTeller_DBname).append(".").append("concept").append(" where ").append("concept").append(" = '").append(this.getConcept().replace("'", "\\'")).append("'");
			rs = Parameter.stm.executeQuery(selectSQL.toString());
			rs.beforeFirst();
			if(rs.next()){
				index = rs.getInt("id");
				String str = rs.getString("simset");
				if (!str.equals("")){
					similarity = str.split(" ");
					for(String s :similarity){
						if(!s.equals(""))
							this.similarity.add(Integer.parseInt(s));
					}
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.index = index;
	}

	public void selectAction() throws SQLException {

		List<StoryRecord> story = new LinkedList<StoryRecord>();
		List<TreeNodeAbstract> visited = new LinkedList<TreeNodeAbstract>();
		Set<String> examine = new HashSet<String>();
		Set<Integer> indexExamine = new HashSet<Integer>();
		examine.addAll(Parameter.examine);
		Timer t = new Timer();
		TreeNodeAbstract cur = this;
		TreeNodeAbstract prev = null;
		visited.add(this);
		story.add(this.toStoryRecord());
		//store story to check redundant
		examine.add(this.getConcept());
		indexExamine.addAll(this.similarity);
		
		//search for the high possibility expand node
		while (!cur.isLeaf()) {
			prev = cur;
			examine.add(prev.getConcept());
			indexExamine.addAll(prev.similarity);
			cur = cur.select();
			visited.add(cur);
			story.add(cur.toStoryRecord());
		}
		t.stopTimer();
		Parameter.selectTimer.accumTime(t.getTotalTime());
		
		//story is found
		checkStoryisFound(cur, story, visited);
		
		//trim node from tree if we found it
		if ((examine.contains(cur.getConcept()) || indexExamine.contains(cur.index)) && visited.size() >= 2) {
			TreeNodeAbstract nodeToTrimFrom = visited.get(visited.size() - 2);
			/**
			 * search the index of the node
			 * 1.parent's node-to-trim contains the node
			 * 2.parent has children
			 * 3.child equal node
			 */
			for (int i = 0; i < nodeToTrimFrom.children.length; i++) {
				if (!nodeToTrimFrom.nextRedundantNum.contains(i)&& nodeToTrimFrom.children[i] != null 
						&& nodeToTrimFrom.children[i].equals(cur)) {
					cur.expanded = true;
					nodeToTrimFrom.nextRedundantNum.add(i);
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
		TreeNodeAbstract newNode = cur.select();
		if (newNode != null) {
			visited.add(newNode);
//			 System.out.println("--------------start simulation----------------");
			t.resetTimer();
			newNode.simulationScore = simulation(visited);
			t.stopTimer();
			Parameter.simulTimer.accumTime(t.getTotalTime());
			// System.out.println(newNode.score);
			for (TreeNodeAbstract node : visited) {
				node.updateStats(newNode.simulationScore);
			}
		} else {
			for (TreeNodeAbstract node : visited) {
				node.updateStats(0.0);
			}
		}

	}
	
	protected void expand() throws SQLException {
		if(this.expanded){
			return;
		}
		this.expanded = true;
		List<String> nextStates = Parameter.nextStates.get(getState());
		ResultSet rs;
		this.numArr = new int[nextStates.size()];
		String nextState = null;
		String prevState = this.getState();
		String tableName = null;
		String findPos = null;
		StringBuffer countSQL = new StringBuffer();
		for (int i = 0; i < nextStates.size(); i++) {
			if (i != 0)
				countSQL.append(" union ");
			nextState = nextStates.get(i);
			if(Parameter.curStorytype.equals(Parameter.StoryType.UNLIMITED_INVERSE_STORY)){
				tableName = nextState + "_" + prevState;
				findPos = "end";
			}else{
				tableName = prevState + "_" + nextState;
				findPos = "start";
			}
			countSQL.append("select ").append((i + 1)).append(" as id , count(*) from ").append(Parameter.storyTeller_DBname).append(".").append(tableName).append(" where ").append(findPos).append(" = '").append(this.getConcept().replace("'", "\\'")).append("'");
				
		}
		rs = Parameter.stm.executeQuery(countSQL.toString());
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
			// System.out.println("cannot expand, no child!!!");
			return;
		}
		children = new TreeNodeAbstract[totalrsNum];
	}
	
	protected abstract void checkStoryisFound(TreeNodeAbstract cur, List<StoryRecord> story, List<TreeNodeAbstract> visited);
	protected abstract boolean checkSimStoryisFound(TreeNodeAbstract curNode, List<TreeNodeAbstract> simulationStroy);
	
	protected double simulation(List<TreeNodeAbstract> visited) throws SQLException{
		int rndNum;
		TreeNodeAbstract curNode = visited.get(visited.size() - 1);
		List<String> nextStates = Parameter.nextStates.get(curNode.getState());
		ResultSet rs;
		int[] numArr = new int[Parameter.fabulaElement.length];
		String nextState = null;
		String nSConcept = curNode.getConcept();
		String nSRelation = curNode.relation;
		double nSScore = curNode.weight;
		String prevState = curNode.getState();
		String tableName = null;
		String startPos = null;
		String endPos = null;
		
		StringBuffer selectSQL = new StringBuffer();
		StringBuffer countSQL = new StringBuffer();
		
		List<TreeNodeAbstract> simulationStroy = new LinkedList<TreeNodeAbstract>();
		Set<String> examine = new HashSet<String>();
		Set<Integer> indexExamine = new HashSet<Integer>();
		
		for (TreeNodeAbstract t : visited.subList(0, visited.size()-2)) {
			examine.add(t.getConcept());
			indexExamine.addAll(t.similarity);
		}
		
		while (curNode.getStep() <= Parameter.fabulaLimit) {
			simulationStroy.add(curNode);
//			System.out.println(curNode.concept);
			if(examine.contains(curNode.getConcept()) || indexExamine.contains(curNode.index))
				return 0.0;
//			examine.add(curNode.concept);
			//check constrain term
			
			if(checkSimStoryisFound(curNode, simulationStroy)){
				return calculateSimilarityScore(simulationStroy);
			}
			
			examine.add(curNode.getConcept());
			indexExamine.addAll(curNode.similarity);
			for (int i = 0; i < nextStates.size(); i++) {
				if (i != 0)
					countSQL.append(" union ");
				nextState = nextStates.get(i);
				if(Parameter.curStorytype.equals(Parameter.StoryType.UNLIMITED_INVERSE_STORY)){
					tableName = nextState + "_" + prevState;
					startPos = "end";
				}else{
					tableName = prevState + "_" + nextState;
					startPos = "start";
				}
				countSQL.append("select ").append((i + 1)).append(" as id , count(*) from ").append(Parameter.storyTeller_DBname).append(".").append(tableName).append(" where ").append(startPos).append(" = '").append(curNode.getConcept().replace("'", "\\'")).append("'");
				
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
			if(Parameter.curStorytype.equals(Parameter.StoryType.UNLIMITED_INVERSE_STORY)){
				tableName = nextState + "_" + prevState;
				startPos = "end";
				endPos = "start";
			}else{
				tableName = prevState + "_" + nextState;
				startPos = "start";
				endPos = "end";
			}
			selectSQL.append("select ").append("* from ").append(Parameter.storyTeller_DBname).append(".").append(tableName).append(" where ").append(startPos).append(" = '").append(curNode.getConcept().replace("'", "\\'")).append("'");
			rs = Parameter.stm.executeQuery(selectSQL.toString());
			selectSQL.delete(0, selectSQL.length());
			rs.absolute(rndNum - temprsNum + numArr[cur] + 1);
			nSConcept = rs.getString(endPos);
			nSRelation = rs.getString("rel");
			nSScore = rs.getDouble("weight");
			rs.close();
			curNode = new StartToGoalTreeNode(nextState, nSConcept, curNode.getStep() + 1, nSRelation, nSScore);
			prevState = curNode.getState();
			nextStates = Parameter.nextStates.get(curNode.getState());
		}
		if(Parameter.curStorytype.equals(Parameter.StoryType.START_TO_GOAL_STORY)){
			return 0.0;
		}else{
			return calculateSimilarityScore(simulationStroy);
		}
	}
	

	protected TreeNodeAbstract select() {
		if (children == null || children.length == (nextGoalNum.size()+nextRedundantNum.size())){
//			System.out.println("return null");
			return null;
		}
		TreeNodeAbstract selected = null;
		int selectedI = 0;
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
		for (int i = 0; i < uctProbability.length; i++) {
			//System.out.println(uctProbability[i]);
//			uctProbability[i] = Math.exp(uctProbability[i]);
			if(this.nextRedundantNum.contains(i) || this.nextGoalNum.contains(i)){
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

		List<String> nextStates = Parameter.nextStates.get(this.getState());
		String tableName = null;
		String startPos = null;
		String endPos = null;
		
		if (selected == null) {
			ResultSet rs;
			String nextState = null;
			String prevState = this.getState();
			
			StringBuffer SQL = new StringBuffer();
			int temprsNum = 0;
			int cur = 0;
			for (cur = 0; cur < nextStates.size(); cur++) {
				temprsNum += numArr[cur];
				if (selectedI < temprsNum)
					break;
			}
			nextState = nextStates.get(cur);
			if(Parameter.curStorytype.equals(Parameter.StoryType.UNLIMITED_INVERSE_STORY)){
				tableName = nextState + "_" + prevState;
				startPos = "end";
				endPos = "start";
			}else{
				tableName = prevState + "_" + nextState;
				startPos = "start";
				endPos = "end";
			}
			try {
				SQL.append("select ").append("* from ").append(Parameter.storyTeller_DBname).append(".").append(tableName).append(" where ").append(startPos).append(" = '").append(this.getConcept().replace("'", "\\'")).append("'");
				rs = Parameter.stm.executeQuery(SQL.toString());
				rs.absolute(selectedI - temprsNum + numArr[cur] + 1);
				String nSConcept = rs.getString(endPos);
				String nSRelation = rs.getString("rel");
				double nSScore = rs.getDouble("weight");
				SQL.delete(0, SQL.length());
				rs.close();
				
				TreeNodeAbstract tn = new StartToGoalTreeNode(nextStates.get(cur), nSConcept, getStep() + 1, nSRelation, nSScore);
				children[selectedI] = tn;
				nAction++;
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			selected = children[selectedI];
		}
		return selected;
	}
	
	protected double calculateSimilarityScore(List<TreeNodeAbstract> simulationStroy){
		double sc = 0.0;
		double cScore = 1.0;
		double loScore = 1.0;
		List <String> cList = new LinkedList<String>();
		for (TreeNodeAbstract k : simulationStroy) {
			sc += k.weight;
			cList.add(k.getConcept());
		}
		cScore = Parameter.constraintModel.getValue(cList, Parameter.storyCnt);
		loScore = Parameter.locationConstraintModel.getValue(cList);
		double lScore = Parameter.lengthModel.getValue(simulationStroy.size(), Parameter.curStoryLength);
//		System.out.println(cScore+" "+loScore+" "+(sc / simulationStroy.size()) / (Parameter.MaxScore) +" "+lScore);
		return Parameter.ScoreConstant * loScore * cScore * (sc / simulationStroy.size()) / (Parameter.MaxScore)* lScore;
	}
	
	public boolean isLeaf() {
		if (children == null || children.length == (nextGoalNum.size()+nextRedundantNum.size())) {
			return true;
		} else {
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

	public StoryRecord toStoryRecord() {
		return new StoryRecord(this.getState(), this.relation, this.getConcept(), this.totValue, this.getStep(), this.weight);
	}
	
	@Override
	public boolean equals(Object obj) {
		TreeNodeAbstract t = (TreeNodeAbstract) obj;
		return (this.getConcept().equals(t.getConcept()) && this.getState().equals(t.getState()) && this.relation.equals(t.relation) && this.simulationScore == t.simulationScore && this.totValue == t.totValue);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}









}

