package template;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
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

public class LongStoryTemplate extends Template{

	private int MCTSNum;
	private String startConcept;
	private String startState;
	private int partition;
	private List<StoryRecord> fullStory;
	private double totalScore;
	private StoryRecord initialStoryRecord;
	
	public LongStoryTemplate(){
		partition = 5;
		Parameter.nextStates = Utility.initNextStates();
		MCTSNum = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.MCTSNum / partition;
		startConcept = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.startConcept;
		startState = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.startState;
		Parameter.nextStates = Utility.initNextStates();
		fullStory = new LinkedList<StoryRecord>();
		totalScore = 0.0;
		initialStoryRecord = null;
	}
	
	@Override
	public void generateStory() throws JSONException, IOException {
		
		for(int i=0; i<partition-1; i++){
			TreeNodeAbstract tn  = new UnlimitedTreeNode(startState, startConcept, 0, "", 0.0);
			for (int j = 0; j < MCTSNum; j++) {
				System.out.println("---------------------select action " + (j + 1) + "------------------------");
				Parameter.alpha = (double) (MCTSNum - j) / MCTSNum;
				try {
					tn.selectAction();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			StoryStructure bestSS = null;
			double bestScore = 0.0;
			for(StoryStructure s:Parameter.storyList){
				if(s.getScore() > bestScore){
					bestScore = s.getScore();
					bestSS = s;
				}
			}
			
			if(!(bestSS==null || bestSS.getStory().size()==1)){
				totalScore = totalScore * (fullStory.size());
				for(int j=0; j<bestSS.getStory().size(); j++){
					if(j==0) {
						if(i==0) initialStoryRecord = bestSS.getStory().get(0);
						continue;
					}
					totalScore += bestSS.getStory().get(j).getScore();
					fullStory.add(bestSS.getStory().get(j));
				}
				totalScore = totalScore / fullStory.size();
			}
			startConcept = fullStory.get(fullStory.size()-1).getConcept();
			startState = fullStory.get(fullStory.size()-1).getState();
			
		}
		TreeNodeAbstract tn  = new StartToGoalTreeNode(startState, startConcept, 0, "", 0.0);
		for (int j = 0; j < MCTSNum; j++) {
			System.out.println("---------------------select action " + (j + 1) + "------------------------");
			Parameter.alpha = (double) (MCTSNum - j) / MCTSNum;
			try {
				tn.selectAction();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		StoryStructure bestSS = null;
		double bestScore = 0.0;
		for(StoryStructure s:Parameter.storyList){
			if(s.getScore() > bestScore){
				bestScore = s.getScore();
				bestSS = s;
			}
		}
		
		if(!(bestSS==null || bestSS.getStory().size()==1)){
			totalScore = totalScore * (fullStory.size());
			for(int j=0; j<bestSS.getStory().size(); j++){
				if(j==0) continue;
				totalScore += bestSS.getStory().get(j).getScore();
				fullStory.add(bestSS.getStory().get(j));
			}
			totalScore = totalScore / fullStory.size();
		}
		fullStory.add(0, initialStoryRecord);
		StoryStructure s = new StoryStructure(totalScore, fullStory);
		Parameter.storyList.clear();
		Parameter.storyList.add(s);
		
	}

}
