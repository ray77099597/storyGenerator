package template;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.json.JSONException;

import generator.Parameter;
import others.StoryRecord;
import others.StoryStructure;
import others.Utility;
import treeNode.StartToGoalTreeNode;
import treeNode.TreeNodeAbstract;

public class TwoCharacterStoryTemplate extends Template{
	private int c1MCTSNum;
	private List<Integer> c2MCTSNum;
	private String startConcept;
	private String startState;
	private String goalConcept;
	private List<StoryRecord> c1Story;
	private List<StoryRecord> c2Story;
	private StoryStructure c1StoryStructure;
	private StoryStructure c2StoryStructure;
	private StoryRecord initialStoryRecord;
	
	public TwoCharacterStoryTemplate(){
		//load two character actions as positive constraint
		c1MCTSNum = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.MCTSNum/2;
		startConcept = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.startConcept;
		startState = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.startState;
		goalConcept = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.goalConcept;
		Parameter.mainWindow.setProgressBarValue(0);
		Parameter.mainWindow.setProgressBarMax(Parameter.storyInformation.get(Parameter.storyCnt).storyformat.MCTSNum);
		c1Story = new LinkedList<StoryRecord>();
		c2Story = new LinkedList<StoryRecord>();
		c1StoryStructure = null;
		c2StoryStructure = null;
		initialStoryRecord = null;
	}
	
	@Override
	public void generateStory() throws JSONException, IOException {
		
		//reset timer
		Parameter.mainTimer.resetTimer();
		Parameter.selectTimer.resetTimer();
		Parameter.expandTimer.resetTimer();
		Parameter.simulTimer.resetTimer();
		
		Parameter.nextStates = Utility.initNextStates();
		Parameter.curStartConcept = startConcept;
		Parameter.curStartState = startState;
		Parameter.curGoalConcept = goalConcept;
		Parameter.curStoryLength = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storylength;
		Parameter.curStorytype = Parameter.StoryType.START_TO_GOAL_STORY;
		Parameter.forwardDirection = true;
		Parameter.gaussianDistribution = new Gaussian(Parameter.curStoryLength, Parameter.curStoryLength / 3);
		Parameter.examine = new HashSet<String>();
		Parameter.indexExamine = new HashSet<Integer>();
		TreeNodeAbstract tn  = new StartToGoalTreeNode(startState, startConcept, 0, "", 0.0);
		
		//run first character's story
		for (int j = 0; j < c1MCTSNum; j++) {
			if(Parameter.stopRunning) return;
			System.out.println("---------------------select action " + (j + 1) + "------------------------");
			Parameter.alpha = (double) (c1MCTSNum - j) / c1MCTSNum;
			Parameter.mainWindow.setProgressBarValue(j);
			try {
				tn.selectAction();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//find the best story of first character
		StoryStructure bestSS = null;
		double bestScore = -0.1;
		for(StoryStructure s:Parameter.storyList){
			if(s.getScore() > bestScore){
				bestScore = s.getScore();
				bestSS = s;
			}
		}
		c1StoryStructure = bestSS;
		c1Story = bestSS.getStory();
		
		//partition first character's story by action12 for second character to run
		List<String> actionList = new LinkedList<String>();
		List<Integer> storyLengthList = new LinkedList<Integer>();
		int count = 0;
		
		//choose 2 character's action, store into list
		for(int i=0; i<c1Story.size(); i++){
			count++;
			//if concept is 2 character's action and is high outdegree action, store into list
			if(i!=0 && i!=c1Story.size()-1 && Parameter.actionMap.get(c1Story.get(i).getConcept())!=null){
				if(Parameter.actionMap.get(c1Story.get(i).getConcept()) == 2 || count > 2){
					actionList.add(c1Story.get(i).getConcept());
					storyLengthList.add(count);
					count = 0;
				}
			}
		}
		storyLengthList.add(count);
		
		//distribute MCTS num to each MCTS task
		c2MCTSNum = new LinkedList<Integer>();
		for(int length : storyLengthList){
			c2MCTSNum.add(c1MCTSNum*length/c1Story.size());
		}
		
		//second character story generation
		Parameter.examine = new HashSet<String>();
		Parameter.indexExamine = new HashSet<Integer>();
		Parameter.curStorytype = Parameter.StoryType.START_TO_GOAL_STORY;
		Parameter.forwardDirection = true;
		Parameter.storyList.clear();
		int totalMCTS = c1MCTSNum;
		
		//add examine to prevent redundant
		for(int j=0 ; j<c1Story.size(); j++){
			Parameter.examine.add(c1Story.get(j).getConcept());
		}
		
		for(int i=0; i<actionList.size(); i++){
			
			//second story segment
			Parameter.curStartConcept = startConcept;
			Parameter.curStartState = startState;
			Parameter.curStoryLength = storyLengthList.get(i);
			Parameter.gaussianDistribution = new Gaussian(Parameter.curStoryLength, (double)Parameter.curStoryLength / 3);
			Parameter.curGoalConcept = actionList.get(i);
			tn  = new StartToGoalTreeNode(startState, startConcept, 0, "", 0.0);
			for (int j = 0; j < c2MCTSNum.get(i); j++) {
				if(Parameter.stopRunning) return;
				System.out.println("---------------------select action " + (j + 1) + "------------------------");
				Parameter.alpha = (double) (c2MCTSNum.get(i) - j) / c2MCTSNum.get(i);
				Parameter.mainWindow.setProgressBarValue(totalMCTS + j+1);
				try {
					tn.selectAction();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			totalMCTS += c2MCTSNum.get(i);
			
			bestSS = null;
			bestScore = -0.1;
			
			//find the story which concept is high outdegree concept
			for(StoryStructure s:Parameter.storyList){
				if(s.getScore() > bestScore){
					bestScore = s.getScore();
					bestSS = s;
				}
			}
			
			//calculate score and prepare for next MCTS task
			if(bestSS!=null){
				for(int j=0; j<bestSS.getStory().size(); j++){
					if(j==0) {
						if(i==0) initialStoryRecord = bestSS.getStory().get(0);
						continue;
					}
					c2Story.add(bestSS.getStory().get(j));
				}
				for(int j=0 ; j<c2Story.size(); j++){
					Parameter.examine.add(c2Story.get(j).getConcept());
				}
			}else{
				c2Story.add(new StoryRecord("NO STORY", "NO STORY", "NO STORY", 0.0, 0, 0.0));
				System.out.println("NO STORY, SHOULD BE REGENERATED");
			}
			
			startConcept = c2Story.get(c2Story.size()-1).getConcept();
			startState = c2Story.get(c2Story.size()-1).getState();
			Parameter.storyList.clear();
		}
		
		Parameter.curGoalConcept = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.goalConcept;
		Parameter.curStartConcept = startConcept;
		Parameter.curStartState = startState;
		Parameter.curStorytype = Parameter.StoryType.START_TO_GOAL_STORY;
		Parameter.forwardDirection = true;
		Parameter.curStoryLength = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storylength/3;
		Parameter.gaussianDistribution = new Gaussian(Parameter.curStoryLength, Parameter.curStoryLength / 3);
		tn  = new StartToGoalTreeNode(startState, startConcept, 0, "", 0.0);
		for (int j = 0; j < c2MCTSNum.get(c2MCTSNum.size()-1); j++) {
			if(Parameter.stopRunning) break;
			System.out.println("---------------------select action " + (j + 1) + "------------------------");
			Parameter.alpha = (double) (c2MCTSNum.get(c2MCTSNum.size()-1) - j) / c2MCTSNum.get(c2MCTSNum.size()-1);
			Parameter.mainWindow.setProgressBarValue(c1MCTSNum * 2 - c2MCTSNum.get(c2MCTSNum.size()-1) + j+1);
			try {
				tn.selectAction();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		bestSS = null;
		bestScore = 0.0;
		for(StoryStructure s:Parameter.storyList){
			if(s.getScore() > bestScore){
				bestScore = s.getScore();
				bestSS = s;
			}
		}
		
		if(bestSS!=null){
			for(int j=0; j<bestSS.getStory().size(); j++){
				if(j==0) continue;
				c2Story.add(bestSS.getStory().get(j));
			}
		}else{
			c2Story.add(new StoryRecord("NO STORY", "NO STORY", "NO STORY", 0.0, 0, 0.0));
			System.out.println("NO STORY, SHOULD BE REGENERATED");
		}
		c2Story.add(0, initialStoryRecord);
		
		//calculate score
		double sc = 0.0;
		List <String> cList = new LinkedList<String>();
		for (StoryRecord k : c2Story) {
			cList.add(k.getConcept());
			sc += k.getScore();
		}
		double lscore = Parameter.lengthModel.getValue(c2Story.size(), Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storylength);
		double cScore = Parameter.constraintModel.getValue(cList, Parameter.storyCnt);
		double loScore = Parameter.locationConstraintModel.getValue(cList);
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
