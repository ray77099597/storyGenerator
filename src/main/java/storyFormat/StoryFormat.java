package storyFormat;

import generator.Parameter.StoryType;
import others.Character;

public class StoryFormat {
	public StoryType storyGeneratedFormat;
	public int MCTSNum;
	public int storylength;
	public String startConcept;
	public String startState;
	public String goalConcept;
	public String[] positiveConstraint;
	public String[] negativeConstraint;
	public String positiveConstraintString;
	public String negativeConstraintString;
	public Character character1;
	public Character character2;
	
	//Start to Goal Story Format
	public StoryFormat(StoryType storyGeneratedFormat, int MCTSNum, int storylength,
			String startConcept,String startState,String goalConcept, 
			String positiveConstraint, String negativeConstraint, Character character1, Character character2){
		this.storyGeneratedFormat = storyGeneratedFormat;
		this.MCTSNum = MCTSNum;
		this.storylength = storylength;
		this.startConcept = startConcept;
		this.startState = startState;
		this.goalConcept = goalConcept;
		this.positiveConstraintString = positiveConstraint;
		this.negativeConstraintString = negativeConstraint;
		this.positiveConstraint = positiveConstraint.split(",");
		this.negativeConstraint = negativeConstraint.split(",");
		this.character1 = character1;
		this.character2 = character2;
	}
	
}
