package template;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.json.JSONException;

import generator.Parameter;
import others.Timer;
import others.Utility;
import treeNode.StartToGoalTreeNode;
import treeNode.TreeNodeAbstract;
import treeNode.UnlimitedTreeNode;

public class UnlimitedStoryTemplate extends Template{

	@Override
	public void generateStory() throws JSONException, IOException {
		
		Parameter.nextStates = Utility.initNextStates();
		Parameter.curStartConcept = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.startConcept;
		Parameter.curStartState = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.startState;
		Parameter.curStoryLength = Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storylength;
		Parameter.curStorytype = Parameter.StoryType.UNLIMITED_STORY;
		Parameter.gaussianDistribution = new Gaussian(Parameter.curStoryLength, Parameter.curStoryLength / 3);
		TreeNodeAbstract tn  = new UnlimitedTreeNode(Parameter.curStartState, Parameter.curStartConcept, 0, "", 0.0);
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
		//Store story
		storeStory_oneCharacter();
		//write file and print
		writeFileAndPrint();
		
	}

}