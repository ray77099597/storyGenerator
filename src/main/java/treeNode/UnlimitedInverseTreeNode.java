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

public class UnlimitedInverseTreeNode extends TreeNodeAbstract{

	public UnlimitedInverseTreeNode(String state, String concept, int step, String relation, double weight) {
		super(state, concept, step, relation, weight);
		// TODO Auto-generated constructor stub
	}
	
	protected void checkStoryisFound(TreeNodeAbstract cur, List<StoryRecord> story, List<TreeNodeAbstract> visited){
		if(story.size()>Parameter.shortestStorylength ){
			double sc = 0.0;
			
			List <String> cList = new LinkedList<String>();
			for (StoryRecord k : story) {
				sc += k.getScore();
				cList.add(k.getConcept());
			}
			double lScore = Parameter.lengthModel.getValue(story.size(), Parameter.curStoryLength);
			double cScore = Parameter.constraintModel.getValue(cList, Parameter.storyCnt);
			double loScore = Parameter.locationConstraintModel.getValue(cList);
	
//			sc = buff * debuff * 10 * (sc / (story.size() - 1))/ Parameter.MaxScore;
			sc = Parameter.ScoreConstant * loScore * cScore * (sc / (story.size() - 1)) / (Parameter.MaxScore)*lScore;
//			sc = constraintScore * 10 * (sc / (story.size() - 1)) / (Parameter.MaxScore);
			StoryStructure storystructure = new StoryStructure(sc, story);
			Parameter.storyList.add(storystructure);
			
		}
	}
	
	@Override
	protected boolean checkSimStoryisFound(TreeNodeAbstract curNode, List<TreeNodeAbstract> simulationStroy) {
		return false;
	}

}
