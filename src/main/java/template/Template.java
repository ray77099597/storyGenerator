package template;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONException;

import generator.Parameter;
import others.StoryRecord;
import others.StoryStructure;

public abstract class Template {
	public abstract void generateStory() throws JSONException, IOException;
	
	public void sortStory(){
		try{
			Collections.sort(Parameter.storyList, new Comparator<StoryStructure>() {
				@Override
				public int compare(StoryStructure s1, StoryStructure s2) {
					return Double.compare(s1.getScore(), s2.getScore())*-1;
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void storeStory_oneCharacter(){
		Parameter.storyInformation.get(Parameter.storyCnt).generatingTime = Parameter.mainTimer.toString();
		for(StoryStructure ss : Parameter.storyList){
			Parameter.storyInformation.get(Parameter.storyCnt).c1StoryList.add(ss);
			String sequence = "";
			for (StoryRecord sr : ss.getStory()) {
				if (ss.getStory().indexOf(sr) != 0) {
					sequence = sequence + " |- " + sr.getRelation() + " -> " + sr.getConcept();
				} else {
					sequence = sequence + sr.getConcept();
				}
			}
			Parameter.storyInformation.get(Parameter.storyCnt).c1StorySequenceList.add(sequence);
			Parameter.storyInformation.get(Parameter.storyCnt).essayList.add(Parameter.essayGenerator.getOneCharacterEssay(ss.getStory(), Parameter.storyInformation.get(Parameter.storyCnt).storyformat.character1));
		}
	}
	
	public void storeStory_twoCharacter(StoryStructure ss1, StoryStructure ss2, List<String> actionList){
		String sequence;
		Parameter.storyInformation.get(Parameter.storyCnt).generatingTime = Parameter.mainTimer.toString();
		Parameter.storyInformation.get(Parameter.storyCnt).c1StoryList.add(ss1);
		Parameter.storyInformation.get(Parameter.storyCnt).c2StoryList.add(ss2);
		
		sequence = "";
		for (StoryRecord sr : ss1.getStory()) {
			if(ss1.getStory().indexOf(sr) == ss1.getStory().size()-1){
				sequence = sequence + " |- " + sr.getRelation() + " -> (" + sr.getConcept() + ")";
			} else if (ss1.getStory().indexOf(sr) == 0) {
				sequence = sequence + "(" + sr.getConcept() + ")";
			} else {
				if(actionList.contains(sr.getConcept())){
					sequence = sequence + " |- " + sr.getRelation() + " -> (" + sr.getConcept() + ")";
				}else{
					sequence = sequence + " |- " + sr.getRelation() + " -> " + sr.getConcept();
				}
			}
		}
		Parameter.storyInformation.get(Parameter.storyCnt).c1StorySequenceList.add(sequence);
		
		sequence = "";
		for (StoryRecord sr : ss2.getStory()) {
			if(ss2.getStory().indexOf(sr) == ss2.getStory().size()-1){
				sequence = sequence + " |- " + sr.getRelation() + " -> (" + sr.getConcept() + ")";
			} else if (ss2.getStory().indexOf(sr) == 0) {
				sequence = sequence + "(" + sr.getConcept() + ")";
			} else {
				if(actionList.contains(sr.getConcept())){
					sequence = sequence + " |- " + sr.getRelation() + " -> (" + sr.getConcept() + ")";
				}else{
					sequence = sequence + " |- " + sr.getRelation() + " -> " + sr.getConcept();
				}
			}
		}
		Parameter.storyInformation.get(Parameter.storyCnt).c2StorySequenceList.add(sequence);
		Parameter.storyInformation.get(Parameter.storyCnt).essayList.add(Parameter.essayGenerator.getTwoCharacterEssay(ss1.getStory(), Parameter.storyInformation.get(Parameter.storyCnt).storyformat.character1, 
				ss2.getStory(), Parameter.storyInformation.get(Parameter.storyCnt).storyformat.character2, actionList));
		
	}
	
	public void storeStory_twoCharacter2(){
		Parameter.storyInformation.get(Parameter.storyCnt).generatingTime = Parameter.mainTimer.toString();
		
		for(StoryStructure ss : Parameter.storyList){
			Parameter.storyInformation.get(Parameter.storyCnt).c1StoryList.add(ss);
			String sequence = "";
			for (StoryRecord sr : ss.getStory()) {
				if (ss.getStory().indexOf(sr) != 0) {
					sequence = sequence + " |- " + sr.getRelation() + " -> " + sr.getConcept();
				} else {
					sequence = sequence + sr.getConcept();
				}
			}
			Parameter.storyInformation.get(Parameter.storyCnt).c1StorySequenceList.add(sequence);
		}
		for(StoryStructure ss : Parameter.storyList2){
			Parameter.storyInformation.get(Parameter.storyCnt).c1StoryList.add(ss);
			String sequence = "";
			for (StoryRecord sr : ss.getStory()) {
				if (ss.getStory().indexOf(sr) != 0) {
					sequence = sequence + " |- " + sr.getRelation() + " -> " + sr.getConcept();
				} else {
					sequence = sequence + sr.getConcept();
				}
			}
			Parameter.storyInformation.get(Parameter.storyCnt).c2StorySequenceList.add(sequence);
		}
		for(int i=0; i<Parameter.storyList.size(); i++){
			Parameter.storyInformation.get(Parameter.storyCnt).essayList.add(Parameter.essayGenerator.getTwoCharacterEssay(Parameter.storyList.get(i).getStory(), Parameter.storyInformation.get(Parameter.storyCnt).storyformat.character1, 
					Parameter.storyList2.get(i).getStory(), Parameter.storyInformation.get(Parameter.storyCnt).storyformat.character2, new ArrayList<String>()));
		}
		
	}
	
	public void writeFileAndPrint(){
		String sig;
		try {
			FileWriter saveFile = new FileWriter(Parameter.resultFolder + "//" + 
					Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storyGeneratedFormat + " "+
					Parameter.storyInformation.get(Parameter.storyCnt).storyformat.startConcept + " " + 
					Parameter.storyInformation.get(Parameter.storyCnt).storyformat.goalConcept + " " + 
					Parameter.storyInformation.get(Parameter.storyCnt).storyformat.MCTSNum + " " + 
					Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storylength + " " + 
												System.currentTimeMillis() + ".txt");
			Parameter.fwriter = new BufferedWriter(saveFile);
			

			System.out.println("Slection Time : " + Parameter.selectTimer.toString());
			System.out.println("Expansion Time : " + Parameter.expandTimer.toString());
			System.out.println("Simulation Time : " + Parameter.simulTimer.toString());
			System.out.println("Total Time : " + Parameter.mainTimer.toString());
			Parameter.fwriter.write("Mode : " + Parameter.storyInformation.get(Parameter.storyCnt).storyformat.storyGeneratedFormat);
			Parameter.fwriter.newLine();
			Parameter.fwriter.write("Slection Time : " + Parameter.selectTimer.toString());
			Parameter.fwriter.newLine();
			Parameter.fwriter.write("Expansion Time : " + Parameter.expandTimer.toString());
			Parameter.fwriter.newLine();
			Parameter.fwriter.write("Simulation Time : " + Parameter.simulTimer.toString());
			Parameter.fwriter.newLine();
			Parameter.fwriter.write("Total Time : " + Parameter.mainTimer.toString() + "\n");
			Parameter.fwriter.newLine();
			
			for (int i = 0; i < Parameter.generateStoryNum; i++) {
				
				if(i>=Parameter.storyList.size()) break;
				System.out.println("============Story" + i + "==============");
				Parameter.fwriter.write("============Story" + i + "==============");
				Parameter.fwriter.newLine();
				System.out.println("Score : " + Parameter.storyList.get(i).getScore());
				Parameter.fwriter.write("Score : " + Parameter.storyList.get(i).getScore() + " " + Parameter.storyList.get(i).getStory().size());
				Parameter.fwriter.newLine();
				
				for (StoryRecord sr : Parameter.storyList.get(i).getStory()) {
					if (sr.getRelation().equals("hasprerequisite") || sr.getRelation().equals("motivatedbygoal") || sr.getRelation().equals("usedfor"))
						sig = "*";
					else
						sig = "";
					if (Parameter.storyList.get(i).getStory().indexOf(sr) != 0) {
						System.out.printf(" -> (" + sr.getRelation() + sig + " | " + sr.getScore() + ") -> " + sr.getConcept() + " " + sr.getState());
	//					Parameter.fwriter.write(" -> (" + sr.getRelation() + sig + " | " + sr.getScore() + ") -> " + sr.getConcept() + " " + sr.getState());
					} else {
						System.out.printf(sr.getConcept() + " " + sr.getState() + sig);
	//					Parameter.fwriter.write(sr.getConcept() + " " + sr.getState() + sig);
					}
				}
				Parameter.fwriter.newLine();
				for (StoryRecord sr : Parameter.storyList.get(i).getStory()) {
					if (Parameter.storyList.get(i).getStory().indexOf(sr) != 0) {
						Parameter.fwriter.write(" |- " + sr.getRelation() +" -> " + sr.getConcept());
					} else {
						Parameter.fwriter.write(sr.getConcept());
					}
				}
				System.out.println();
				Parameter.fwriter.newLine();
	//			Parameter.fwriter.write(eg.getEssay(Parameter.storyList.get(i).getStory()));
				Parameter.fwriter.newLine();
				
			}
			Parameter.fwriter.flush();
			Parameter.fwriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
