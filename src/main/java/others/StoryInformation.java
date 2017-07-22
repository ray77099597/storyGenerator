package others;

import java.util.ArrayList;
import java.util.List;

import storyFormat.StoryFormat;

public class StoryInformation {
	public StoryFormat storyformat;
	public String generatingTime;
	public List<StoryStructure> c1StoryList;
	public List<StoryStructure> c2StoryList;
	public List<String> c1StorySequenceList;
	public List<String> c2StorySequenceList;
	public List<String> essayList;
	public StoryInformation(StoryFormat storyformat){
		this.storyformat = storyformat;
		generatingTime = "";
		this.c1StoryList = new ArrayList<StoryStructure>();
		this.c2StoryList = new ArrayList<StoryStructure>();
		this.c1StorySequenceList = new ArrayList<String>();
		this.c2StorySequenceList = new ArrayList<String>();
		this.essayList = new ArrayList<String>();
	}
	
}
