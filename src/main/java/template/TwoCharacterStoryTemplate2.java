package template;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.json.JSONException;

import generator.Parameter;
import others.StoryRecord;
import others.StoryStructure;
import others.Utility;
import treeNode.StartToGoalTreeNode;
import treeNode.TreeNodeAbstract;
import treeNode.UnlimitedTreeNode;

public class TwoCharacterStoryTemplate2 extends Template{
	private String startConcept;
	private String startState;
	private List<StoryRecord> c1Story;
	private List<StoryRecord> c2Story;
	private StoryRecord initialStoryRecord1;
	private StoryRecord initialStoryRecord2;
	private StoryStructure c1StoryStructure;
	private StoryStructure c2StoryStructure;
	private List<String> actionList;
	
	public TwoCharacterStoryTemplate2(){
		startConcept = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.startConcept;
		startState = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.startState;
		Parameter.mainWindow.setProgressBarValue(0);
		Parameter.mainWindow.setProgressBarMax(Parameter.storyInformation.get(Parameter.storyCnt).storyformat.MCTSNum);
		c1Story = new LinkedList<StoryRecord>();
		c2Story = new LinkedList<StoryRecord>();
		initialStoryRecord1 = null;
		initialStoryRecord2 = null;
		c1StoryStructure = null;
		c2StoryStructure = null;
		actionList = new ArrayList<String>();
	}
	
	@Override
	public void generateStory() throws JSONException, IOException {
		
		//reset timer
		Parameter.mainTimer.resetTimer();
		Parameter.selectTimer.resetTimer();
		Parameter.expandTimer.resetTimer();
		Parameter.simulTimer.resetTimer();
		
		int StorySegmentNum = 4;

		//distribute MCTS length and num
		int length = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storylength / StorySegmentNum;
		int lastLength = length + (Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storylength % StorySegmentNum);
		
		int MCTSNum = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.MCTSNum / (StorySegmentNum * 2);
		int lastMCTSNum = MCTSNum + (Parameter.storyInformation.get(Parameter.storyCnt).storyformat.MCTSNum % (StorySegmentNum * 2));
		
		TreeNodeAbstract tn;
		int totalMCTS = 0;
		double bestScore;
		StoryStructure bestSS;
		
		//story parameter (not changed)
		Parameter.curStoryLength = length;
		Parameter.gaussianDistribution = new Gaussian(Parameter.curStoryLength, Parameter.curStoryLength / 3);
		Parameter.nextStates = Utility.initNextStates();
		Parameter.examine = new HashSet<String>();
		
		for(int i=0; i<StorySegmentNum-1; i++){
			//c1 parameter
			Parameter.curStartConcept = startConcept;
			Parameter.curStartState = startState;
			Parameter.curStorytype = Parameter.StoryType.UNLIMITED_STORY;
			for(int j=0 ; j<c2Story.size(); j++){
				Parameter.examine.add(c2Story.get(j).getConcept());
			}
			
			//c1 story
			tn  = new UnlimitedTreeNode(startState, startConcept, 0, "", 0.0);
			for (int j = 0; j < MCTSNum; j++) {
				if(Parameter.stopRunning) break;
				System.out.println("---------------------select action " + (j + 1) + "------------------------");
				Parameter.alpha = (double) (MCTSNum - j) / MCTSNum;
				Parameter.mainWindow.setProgressBarValue(totalMCTS + j);
				try {
					tn.selectAction();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			totalMCTS += MCTSNum;
			
			//find best story
			bestSS = null;
			bestScore = 0.0;
			//if end of story is high outdegree action
			for(StoryStructure s:Parameter.storyList){
				if(s.getScore() > bestScore && Parameter.actionHighOutdegreeSet.contains(s.getStory().get(s.getStory().size()-1).getConcept())){
					bestScore = s.getScore();
					bestSS = s;
				}
			}
			//store best story
			if(bestSS!=null){
				for(int j=0; j<bestSS.getStory().size(); j++){
					if(j==0) {
						if(i==0) initialStoryRecord1 = bestSS.getStory().get(0);
						continue;
					}
					c1Story.add(bestSS.getStory().get(j));
				}
			}else{
				c1Story.add(new StoryRecord("NO STORY", "NO STORY", "NO STORY", 0.0, 0, 0.0));
				System.out.println("NO STORY, SHOULD BE REGENERATED");
			}
			actionList.add(c1Story.get(c1Story.size()-1).getConcept());
			Parameter.storyList.clear();
			
			//c2 story
			Parameter.curStorytype = Parameter.StoryType.START_TO_GOAL_STORY;
			Parameter.curGoalConcept = c1Story.get(c1Story.size()-1).getConcept();
			for(int j=0 ; j<c1Story.size(); j++){
				Parameter.examine.add(c1Story.get(j).getConcept());
			}
			if(startConcept.equals(Parameter.curGoalConcept)){
				System.out.println("SAME!!!!!!!!!!!!!!!!!!");
				Parameter.stopRunning = true;
				break;
			}
			tn  = new StartToGoalTreeNode(startState, startConcept, 0, "", 0.0);
			for (int j = 0; j < MCTSNum; j++) {
				if(Parameter.stopRunning) break;
				System.out.println("---------------------select action " + (j + 1) + "------------------------");
				Parameter.alpha = (double) (MCTSNum - j) / MCTSNum;
				Parameter.mainWindow.setProgressBarValue(totalMCTS + j);
				try {
					tn.selectAction();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			totalMCTS += MCTSNum;

			//find best story
			bestSS = null;
			bestScore = 0.0;
			for(StoryStructure s:Parameter.storyList){
				if(s.getScore() > bestScore){
					bestScore = s.getScore();
					bestSS = s;
				}
			}
			//store best story
			if(bestSS!=null){
				for(int j=0; j<bestSS.getStory().size(); j++){
					if(j==0) {
						if(i==0) initialStoryRecord2 = bestSS.getStory().get(0);
						continue;
					}
					c2Story.add(bestSS.getStory().get(j));
				}
			}else{
				c2Story.add(new StoryRecord("NO STORY", "NO STORY", "NO STORY", 0.0, 0, 0.0));
				System.out.println("NO STORY, SHOULD BE REGENERATED");
			}
			Parameter.storyList.clear();
			startConcept = c1Story.get(c1Story.size()-1).getConcept();
			startState = c1Story.get(c1Story.size()-1).getState();
		}
		//last time MCTS task

		Parameter.curStoryLength = lastLength;
		Parameter.gaussianDistribution = new Gaussian(Parameter.curStoryLength, Parameter.curStoryLength / 3);
	
		//c1 parameter
		Parameter.curStartConcept = startConcept;
		Parameter.curStartState = startState;
		Parameter.curGoalConcept = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.goalConcept;
		Parameter.curStorytype = Parameter.StoryType.START_TO_GOAL_STORY;
		for(int j=0 ; j<c2Story.size(); j++){
			Parameter.examine.add(c2Story.get(j).getConcept());
		}
		
		//c1 story
		tn  = new StartToGoalTreeNode(startState, startConcept, 0, "", 0.0);
		for (int j = 0; j < lastMCTSNum; j++) {
			if(Parameter.stopRunning) break;
			System.out.println("---------------------select action " + (j + 1) + "------------------------");
			Parameter.alpha = (double) (lastMCTSNum - j) / lastMCTSNum;
			Parameter.mainWindow.setProgressBarValue(totalMCTS + j);
			try {
				tn.selectAction();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		totalMCTS += lastMCTSNum;
		
		//find best story
		bestSS = null;
		bestScore = 0.0;
		//if end of story is high outdegree action
		for(StoryStructure s:Parameter.storyList){
			if(s.getScore() > bestScore && Parameter.actionMap.get(s.getStory().get(s.getStory().size()-1).getConcept())!=null){
				bestScore = s.getScore();
				bestSS = s;
			}
		}
		//store best story
		if(bestSS!=null){
			for(int j=0; j<bestSS.getStory().size(); j++){
				if(j==0) continue;
				c1Story.add(bestSS.getStory().get(j));
			}
		}else{
			c1Story.add(new StoryRecord("NO STORY", "NO STORY", "NO STORY", 0.0, 0, 0.0));
			System.out.println("NO STORY, SHOULD BE REGENERATED");
		}
		actionList.add(c1Story.get(c1Story.size()-1).getConcept());
		Parameter.storyList.clear();
		
		//c2 story
		for(int j=0 ; j<c1Story.size(); j++){
			Parameter.examine.add(c1Story.get(j).getConcept());
		}
		if(startConcept.equals(Parameter.curGoalConcept)){
			System.out.println("SAME!!!!!!!!!!!!!!!!!!");
			Parameter.stopRunning = true;
			return;
		}
		tn  = new StartToGoalTreeNode(startState, startConcept, 0, "", 0.0);
		for (int j = 0; j < lastMCTSNum; j++) {
			if(Parameter.stopRunning) break;
			System.out.println("---------------------select action " + (j + 1) + "------------------------");
			Parameter.alpha = (double) (lastMCTSNum - j) / lastMCTSNum;
			Parameter.mainWindow.setProgressBarValue(totalMCTS + j);
			try {
				tn.selectAction();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		totalMCTS += lastMCTSNum;

		//find best story
		bestSS = null;
		bestScore = 0.0;
		for(StoryStructure s:Parameter.storyList){
			if(s.getScore() > bestScore){
				bestScore = s.getScore();
				bestSS = s;
			}
		}
		//store best story
		if(bestSS!=null){
			for(int j=0; j<bestSS.getStory().size(); j++){
				if(j==0) continue;
				c2Story.add(bestSS.getStory().get(j));
			}
		}else{
			c2Story.add(new StoryRecord("NO STORY", "NO STORY", "NO STORY", 0.0, 0, 0.0));
			System.out.println("NO STORY, SHOULD BE REGENERATED");
		}
		
		//store story to show
		c1Story.add(0, initialStoryRecord1);
		c2Story.add(0, initialStoryRecord2);
		
		//calculate score
		double sc, lscore, cScore, loScore;
		List <String> c1List = new LinkedList<String>();
		List <String> c2List = new LinkedList<String>();
		
		sc = 0.0;
		for (StoryRecord k : c2Story) {
			c1List.add(k.getConcept());
			sc += k.getScore();
		}
		lscore = Parameter.lengthModel.getValue(c1Story.size(), Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storylength);
		cScore = Parameter.constraintModel.getValue(c1List, Parameter.storyCnt);
		loScore = Parameter.locationConstraintModel.getValue(c1List);
		sc = Parameter.ScoreConstant * loScore * cScore * (sc / (c1Story.size() - 1)) / (Parameter.MaxScore) * lscore;
		
		c1StoryStructure = new StoryStructure(sc, c1Story);
		
		sc = 0.0;
		for (StoryRecord k : c2Story) {
			c2List.add(k.getConcept());
			sc += k.getScore();
		}
		lscore = Parameter.lengthModel.getValue(c2Story.size(), Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storylength);
		cScore = Parameter.constraintModel.getValue(c2List, Parameter.storyCnt);
		loScore = Parameter.locationConstraintModel.getValue(c2List);
		sc = Parameter.ScoreConstant * loScore * cScore * (sc / (c2Story.size() - 1)) / (Parameter.MaxScore) * lscore;
		
		c2StoryStructure = new StoryStructure(sc, c2Story);
		
		//stop timer
		Parameter.mainTimer.stopTimer();
		Parameter.mainTimer.calcuTime();
		Parameter.selectTimer.calcuTime();
		Parameter.expandTimer.calcuTime();
		Parameter.simulTimer.calcuTime();
		
		//Store story
		storeStory_twoCharacter(c1StoryStructure, c2StoryStructure, actionList);
		//write file and print
		writeFileAndPrint();
		
	}
}
