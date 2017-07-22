package template;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import org.json.JSONException;

import generator.AntonymGenerator;
import generator.Parameter;
import treeNode.StartToGoalTreeNode;
import treeNode.TreeNodeAbstract;
import treeNode.UnlimitedTreeNode;

public class HumorStoryTemplate extends Template{
	
	AntonymGenerator antonymGenerator;
	List<String> antonyms;
	public HumorStoryTemplate(){
		antonymGenerator = new AntonymGenerator();
	}

	@Override
	public void generateStory() throws JSONException, IOException {
		antonyms = antonymGenerator.generateAntonym(Parameter.storyInformation.get(Parameter.storyCnt).storyformat.goalConcept);
		Random r = new Random(System.currentTimeMillis());
		System.out.println(antonyms.size());
		if(antonyms.size()>0){
			String antonym = antonyms.get(r.nextInt(antonyms.size()));
			System.out.println(antonym);
//			//positive
//			TreeNodeAbstract tn  = new StartToGoalTreeNode(Parameter.story[Parameter.storyCnt].startState, Parameter.story[Parameter.storyCnt].startConcept, 0, "", 0.0);
//			for (int j = 0; j < Parameter.story[Parameter.storyCnt].MCTSNum; j++) {
//				System.out.println("---------------------select action " + (j + 1) + "------------------------");
//				Parameter.alpha = (double) (Parameter.story[Parameter.storyCnt].MCTSNum - j) / Parameter.story[Parameter.storyCnt].MCTSNum;
//				try {
//					tn.selectAction();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			//negtive
//			TreeNodeAbstract tn2  = new StartToGoalTreeNode(Parameter.story[Parameter.storyCnt].startState, Parameter.story[Parameter.storyCnt].startConcept, 0, "", 0.0);
//			for (int j = 0; j < Parameter.story[Parameter.storyCnt].MCTSNum; j++) {
//				System.out.println("---------------------select action " + (j + 1) + "------------------------");
//				Parameter.alpha = (double) (Parameter.story[Parameter.storyCnt].MCTSNum - j) / Parameter.story[Parameter.storyCnt].MCTSNum;
//				try {
//					tn2.selectAction();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
			
		}else{
			System.out.println("antonym not found!!!");
		}
	}
}
