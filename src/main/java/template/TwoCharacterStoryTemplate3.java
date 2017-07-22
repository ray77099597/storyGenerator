package template;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.json.JSONException;

import generator.Parameter;
import others.StoryStructure;
import others.Timer;
import others.Utility;
import treeNode.StartToGoalTreeNode;
import treeNode.TreeNodeAbstract;
import treeNode.TwoCharacterTreeNode;

public class TwoCharacterStoryTemplate3 extends Template{

	@Override
	public void generateStory() throws JSONException, IOException {
		
		
		
		Parameter.nextStates = Utility.initNextStates();
		Parameter.curGoalConcept = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.goalConcept;
		Parameter.curStartConcept = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.startConcept;
		Parameter.curStartState = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.startState;
		Parameter.curStoryLength = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storylength;
		Parameter.curStorytype = Parameter.StoryType.START_TO_GOAL_STORY;
		Parameter.gaussianDistribution = new Gaussian(Parameter.curStoryLength, Parameter.curStoryLength / 3);
		Parameter.examine = new HashSet<String>();
		Parameter.indexExamine = new HashSet<Integer>();
		TwoCharacterTreeNode tn  = new TwoCharacterTreeNode(Parameter.curStartState, Parameter.curStartState, Parameter.curStartConcept, Parameter.curStartConcept, 0, "", "", 0.0, 0.0, false, false);
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
		System.out.println(Parameter.storyList.size() + " " + Parameter.storyList2.size());
		//stop timer
		Parameter.mainTimer.stopTimer();
		Parameter.mainTimer.calcuTime();
		Parameter.selectTimer.calcuTime();
		Parameter.expandTimer.calcuTime();
		Parameter.simulTimer.calcuTime();
		
		
		//Store story
		storeStory_twoCharacter2();
		//write file and print
		writeFileAndPrint();
	}

}
