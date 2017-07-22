package treeNode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import generator.Parameter;
import others.StoryRecord;
import others.StoryStructure;
import others.Timer;
import others.Utility;

public class StartToGoalTreeNode extends TreeNodeAbstract{

	public StartToGoalTreeNode(String state, String concept, int step, String relation, double weight) {
		super(state, concept, step, relation, weight);
	}
	
	protected void checkStoryisFound(TreeNodeAbstract cur, List<StoryRecord> story, List<TreeNodeAbstract> visited){
		if (cur.getConcept().equals(Parameter.curGoalConcept)) {
			// remember trim's goal index
			TreeNodeAbstract nodeToTrimFrom = visited.get(visited.size() - 2);
			for (int i = 0; i < nodeToTrimFrom.children.length; i++) {
				if (!nodeToTrimFrom.nextGoalNum.contains(i) && nodeToTrimFrom.children[i] != null && nodeToTrimFrom.children[i].equals(cur)) {
					double sc = 0.0;
					List <String> cList = new LinkedList<String>();
					for (StoryRecord k : story) {
						cList.add(k.getConcept());
						sc += k.getScore();
					}
					double lscore = Parameter.lengthModel.getValue(story.size(), Parameter.curStoryLength);
					double cScore = Parameter.constraintModel.getValue(cList, Parameter.storyCnt);
					double loScore = Parameter.locationConstraintModel.getValue(cList);
//					sc = Parameter.ScoreConstant * constraintScore * (sc / (story.size() - 1)) * Parameter.gaussianDistribution.value(story.size()) / (Parameter.MaxScore * Parameter.gaussianDistribution.value(Parameter.story[Parameter.storyCnt].storylength));
					sc = Parameter.ScoreConstant * loScore * cScore * (sc / (story.size() - 1)) / (Parameter.MaxScore) * lscore;
					
					StoryStructure storystructure = new StoryStructure(sc, story);
					Parameter.storyList.add(storystructure);
					cur.expanded = true;
					nodeToTrimFrom.nextGoalNum.add(i);
					break;
				}
			}
		}
	}

	@Override
	protected boolean checkSimStoryisFound(TreeNodeAbstract curNode, List<TreeNodeAbstract> simulationStroy) {
		// TODO Auto-generated method stub
		if (curNode.getConcept().equals(Parameter.curGoalConcept)) {
			return true;
		}
		return false;
	}
	
}
