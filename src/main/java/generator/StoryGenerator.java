package generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import generator.Parameter.StoryType;
import others.StoryInformation;
import others.StoryRecord;
import others.StoryStructure;
import others.Timer;
import template.HumorStoryTemplate;
import template.LongStoryTemplate;
import template.StartToGoalStoryTemplate;
import template.Template;
import template.TwoCharacterStoryTemplate;
import template.TwoCharacterStoryTemplate2;
import template.TwoCharacterStoryTemplate3;
import template.UnlimitedInverseStoryTemplate;
import template.UnlimitedStoryTemplate;

public class StoryGenerator extends Thread{

	public void generateStory() throws IOException{
//		Timer t = new Timer();
		if (!(new File(Parameter.resultFolder)).mkdirs()) {
			System.out.println(Parameter.resultFolder + " folder already open!");
		}
		for (Parameter.storyCnt = 0; Parameter.storyCnt < Parameter.storyInformation.size(); Parameter.storyCnt++) {
//			t.resetTimer();
//			Parameter.selectTimer.resetTimer();
//			Parameter.expandTimer.resetTimer();
//			Parameter.simulTimer.resetTimer();
			Parameter.storyList = new ArrayList<StoryStructure>();
			Parameter.storyList2 = new ArrayList<StoryStructure>();
//			FileWriter saveFile = new FileWriter(Parameter.resultFolder + "//" + 
//					Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storyGeneratedFormat + " "+
//					Parameter.storyInformation.get(Parameter.storyCnt).storyformat.startConcept + " " + 
//					Parameter.storyInformation.get(Parameter.storyCnt).storyformat.goalConcept + " " + 
//					Parameter.storyInformation.get(Parameter.storyCnt).storyformat.MCTSNum + " " + 
//					Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storylength + " " + 
//												System.currentTimeMillis() + ".txt");
//			Parameter.fwriter = new BufferedWriter(saveFile);
			
			Template template = null;
			switch(Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storyGeneratedFormat){
			case START_TO_GOAL_STORY:
				template = new StartToGoalStoryTemplate();
				break;
			case UNLIMITED_STORY:
				template = new UnlimitedStoryTemplate();
				break;
			case UNLIMITED_INVERSE_STORY:
				template = new UnlimitedInverseStoryTemplate();
				break;
			case LONG_STORY:
				template = new LongStoryTemplate();
				break;
			case HUMOR_STORY:
				template = new HumorStoryTemplate();
				break;
			case TWO_CHARACTER_STORY:
				template = new TwoCharacterStoryTemplate();
				break;
			case TWO_CHARACTER_STORY2:
				template = new TwoCharacterStoryTemplate2();
				break;
			case TWO_CHARACTER_STORY3:
				template = new TwoCharacterStoryTemplate3();
				break;
			default:
				break;
			
			}
			template.generateStory();
			
			if(Parameter.stopRunning) break;
			
//			t.stopTimer();
//			Parameter.selectTimer.calcuTime();
//			Parameter.expandTimer.calcuTime();
//			Parameter.simulTimer.calcuTime();
//			System.out.println("Slection Time : " + Parameter.selectTimer.toString());
//			System.out.println("Expansion Time : " + Parameter.expandTimer.toString());
//			System.out.println("Simulation Time : " + Parameter.simulTimer.toString());
//			System.out.println("Total Time : " + t.toString());
//			Parameter.fwriter.write("Mode : " + Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storyGeneratedFormat);
//			Parameter.fwriter.newLine();
//			Parameter.fwriter.write("Slection Time : " + Parameter.selectTimer.toString());
//			Parameter.fwriter.newLine();
//			Parameter.fwriter.write("Expansion Time : " + Parameter.expandTimer.toString());
//			Parameter.fwriter.newLine();
//			Parameter.fwriter.write("Simulation Time : " + Parameter.simulTimer.toString());
//			Parameter.fwriter.newLine();
//			Parameter.fwriter.write("Total Time : " + t.toString() + "\n");
//			Parameter.fwriter.newLine();
//			Parameter.timeList.add(t.toString());
			
//			if(!(Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storyGeneratedFormat.equals(StoryType.TWO_CHARACTER_STORY) ||
//					Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storyGeneratedFormat.equals(StoryType.TWO_CHARACTER_STORY2))){
//				try{
//					Collections.sort(Parameter.storyList, new Comparator<StoryStructure>() {
//						@Override
//						public int compare(StoryStructure s1, StoryStructure s2) {
//							return Double.compare(s1.getScore(), s2.getScore())*-1;
//						}
//					});
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			}
			
//			if(Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storyGeneratedFormat.equals(StoryType.UNLIMITED_INVERSE_STORY)){
//				for (int i = 0; i < Parameter.generateStoryNum; i++) {
//					if(i>=Parameter.storyList.size()) break;
//					String prevRel = "";
//					boolean first = true;
//					Collections.reverse(Parameter.storyList.get(i).getStory());
//					for (StoryRecord sr : Parameter.storyList.get(i).getStory()) {
//						if(first) {
//							prevRel = sr.getRelation();
//							sr.setRelation("");
//							first = false;
//						}else{
//							String tmp = sr.getRelation();
//							sr.setRelation(prevRel);
//							prevRel = tmp;
//						}
//					}
//				}
//			}

//			String sig;
//			for (int i = 0; i < Parameter.generateStoryNum; i++) {
//				if(i>=Parameter.storyList.size()) break;
//				System.out.println("============Story" + i + "==============");
//				Parameter.fwriter.write("============Story" + i + "==============");
//				Parameter.fwriter.newLine();
//				System.out.println("Score : " + Parameter.storyList.get(i).getScore());
//				Parameter.fwriter.write("Score : " + Parameter.storyList.get(i).getScore() + " " + Parameter.storyList.get(i).getStory().size());
//				Parameter.fwriter.newLine();
//				
//				for (StoryRecord sr : Parameter.storyList.get(i).getStory()) {
//					if (sr.getRelation().equals("hasprerequisite") || sr.getRelation().equals("motivatedbygoal") || sr.getRelation().equals("usedfor"))
//						sig = "*";
//					else
//						sig = "";
//					if (Parameter.storyList.get(i).getStory().indexOf(sr) != 0) {
//						System.out.printf(" -> (" + sr.getRelation() + sig + " | " + sr.getScore() + ") -> " + sr.getConcept() + " " + sr.getState());
////						Parameter.fwriter.write(" -> (" + sr.getRelation() + sig + " | " + sr.getScore() + ") -> " + sr.getConcept() + " " + sr.getState());
//					} else {
//						System.out.printf(sr.getConcept() + " " + sr.getState() + sig);
////						Parameter.fwriter.write(sr.getConcept() + " " + sr.getState() + sig);
//					}
//				}
//				Parameter.fwriter.newLine();
//				for (StoryRecord sr : Parameter.storyList.get(i).getStory()) {
//					if (Parameter.storyList.get(i).getStory().indexOf(sr) != 0) {
//						Parameter.fwriter.write(" |- " + sr.getRelation() +" -> " + sr.getConcept());
//					} else {
//						Parameter.fwriter.write(sr.getConcept());
//					}
//				}
//				System.out.println();
//				Parameter.fwriter.newLine();
////				Parameter.fwriter.write(eg.getEssay(Parameter.storyList.get(i).getStory()));
//				Parameter.fwriter.newLine();
//			}
//			Parameter.fwriter.flush();
//			Parameter.fwriter.close();
//			Parameter.storyListCollection.add(new ArrayList<StoryStructure>(Parameter.storyList));
			Parameter.mainWindow.showResultList();
		}
		Parameter.mainWindow.storyFinishGenerating();
		Parameter.stopRunning = true;

//		eg.releaseWKT();
	}
	
	// override Thread's run()
	public void run() { 
        StoryGenerator sg = new StoryGenerator();
        try {
			sg.generateStory();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}