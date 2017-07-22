package template;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.json.JSONException;

import generator.Parameter;
import generator.Parameter.StoryType;
import others.StoryRecord;
import others.Timer;
import others.Utility;
import treeNode.StartToGoalTreeNode;
import treeNode.TreeNodeAbstract;
import treeNode.UnlimitedInverseTreeNode;

public class UnlimitedInverseStoryTemplate extends Template{

	@Override
	public void generateStory() throws JSONException, IOException {
		
		Parameter.nextStates = Utility.initNextStatesInverse();
		Parameter.curGoalConcept = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.goalConcept;
		Parameter.curStartState = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.startState;
		Parameter.curStoryLength = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storylength;
		Parameter.curStorytype = Parameter.StoryType.UNLIMITED_INVERSE_STORY;
		Parameter.gaussianDistribution = new Gaussian(Parameter.curStoryLength, Parameter.curStoryLength / 3);
		TreeNodeAbstract tn  = new UnlimitedInverseTreeNode(Parameter.curStartState, Parameter.curGoalConcept, 0, "", 0.0);
		Parameter.examine = new HashSet<String>();
		Parameter.indexExamine = new HashSet<Integer>();
		Parameter.mainWindow.setProgressBarMax(Parameter.storyInformation.get(Parameter.storyCnt).storyformat.MCTSNum);
		
		//reset timer
		Parameter.mainTimer.resetTimer();
		Parameter.selectTimer.resetTimer();
		Parameter.expandTimer.resetTimer();
		Parameter.simulTimer.resetTimer();
		
		//generate story
		for (int j = 0; j < Parameter.storyInformation.get(Parameter.storyCnt).storyformat.MCTSNum; j++) {
			if(Parameter.stopRunning) break;
			System.out.println("---------------------select action " + (j + 1) + "------------------------");
			Parameter.alpha = (double) (Parameter.storyInformation.get(Parameter.storyCnt).storyformat.MCTSNum - j) / Parameter.storyInformation.get(Parameter.storyCnt).storyformat.MCTSNum;
			Parameter.mainWindow.setProgressBarValue(j+1);
			try {
				tn.selectAction();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//stop timer
		Parameter.mainTimer.stopTimer();
		Parameter.mainTimer.calcuTime();
		Parameter.selectTimer.calcuTime();
		Parameter.expandTimer.calcuTime();
		Parameter.simulTimer.calcuTime();
		
		//sort story
		sortStory();
		//reverse story sequence
		for (int i = 0; i < Parameter.generateStoryNum; i++) {
			if(i>=Parameter.storyList.size()) break;
			String prevRel = "";
			boolean first = true;
			Collections.reverse(Parameter.storyList.get(i).getStory());
			for (StoryRecord sr : Parameter.storyList.get(i).getStory()) {
				if(first) {
					prevRel = sr.getRelation();
					sr.setRelation("");
					first = false;
				}else{
					String tmp = sr.getRelation();
					sr.setRelation(prevRel);
					prevRel = tmp;
				}
			}
		}
		//Store story
		storeStory_oneCharacter();
		//write file and print
		writeFileAndPrint();
		
	}

}