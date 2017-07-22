package generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import others.StoryRecord;
import others.Tagger;
import others.Wikitionary;
import others.Character;

public class EssayGenerator {
	private Wikitionary wkt;
	private Tagger tagger;
	private Character character;
	public EssayGenerator(){
		wkt = new Wikitionary();
		tagger = new Tagger();
		character = null;
	}
	public void release(){
		wkt.releaseWKT();
		tagger.taggerRelease();
	}
	
	
	public String getOneCharacterEssay(List<StoryRecord> storySeq, Character crt){
		StringBuffer essay = new StringBuffer();
		character = crt;
		String rel1, rel2;
		String start, mid, end;
		String sentense = "";
		//Generate story sequence
		int curStoryindex = 0;
		int remaimStorySize = storySeq.size();
		while(remaimStorySize > 0){
			if(remaimStorySize == 1){
				start = changeConcept(storySeq.get(curStoryindex).getConcept());
				essay.append(generateSentenseWithOneConcept(start) + " ");
				remaimStorySize -= 1;
			}else if(remaimStorySize == 2){
				start = changeConcept(storySeq.get(curStoryindex).getConcept());
				end = changeConcept(storySeq.get(curStoryindex+1).getConcept());
				rel1 = storySeq.get(curStoryindex+1).getRelation();
				essay.append(generateSentenseWithTwoConcept(start, end, rel1) + " ");
				remaimStorySize -= 2;
			}else{
				start = changeConcept(storySeq.get(curStoryindex).getConcept());
				mid = changeConcept(storySeq.get(curStoryindex+1).getConcept());
				end = changeConcept(storySeq.get(curStoryindex+2).getConcept());
				rel1 = storySeq.get(curStoryindex+1).getRelation();
				rel2 = storySeq.get(curStoryindex+2).getRelation();
				
				sentense = generateSentenseWithThreeConcept(start, mid, end, rel1, rel2);
				if(!sentense.equals("not found")){
					essay.append(sentense + " ");
					curStoryindex += 3;
					remaimStorySize -= 3;
				}else{
					essay.append(generateSentenseWithTwoConcept(start, mid, rel1) + " ");
					curStoryindex += 2;
					remaimStorySize -= 2;
				}
			}
		}
		
		//Separate story sequences
//		List<List<StoryRecord>> storySeperatedList = generateStoryCuttingList(storySeq);
//		
//		for(List<StoryRecord> ssl : storySeperatedList){
//			if(ssl.size() == 2){
//				start = changeConcept(ssl.get(0).getConcept());
//				end = changeConcept(ssl.get(1).getConcept());
//				rel = ssl.get(1).getRelation();
//				essay.append(generateSentenseWithTwoConcept(start, end, rel) + " ");
//			}else if(ssl.size() == 3){
//				start = changeConcept(ssl.get(0).getConcept());
//				mid = changeConcept(ssl.get(1).getConcept());
//				end = changeConcept(ssl.get(2).getConcept());
//				rel1 = ssl.get(1).getRelation();
//				rel2 = ssl.get(2).getRelation();
//				essay.append(generateSentenseWithThreeConcept(start, mid, end, rel1, rel2) + " ");
//			}else{
//				System.out.println("Story sequence cutting Error");
//				return essay.toString();
//			}
//		}
		
		return essay.toString();
	}
	
//	private List<List<StoryRecord>> generateStoryCuttingList(List<StoryRecord> storySeq){
//		List<Integer> storySeperateCount  = new LinkedList<Integer>();
//		//Separate by most causal relation (causes, causesDesire, hasLastSubevent)
//		int count = 0;
//		for(StoryRecord sr : storySeq){
//			count++;
//			if((storySeq.indexOf(sr) == storySeq.size()-1)|| sr.getRelation().equals("causes") || sr.getRelation().equals("causesdesire") || sr.getRelation().equals("haslastsubevent")){
//				storySeperateCount.add(count);
//				count = 0;
//			}
//		}
//		
//		//Combine single concept to the next concept group
//		List<Integer> storySeperateCountTmp = new LinkedList<Integer>();
//		for(int i=0; i<storySeperateCount.size(); i++){
//			if(storySeperateCount.get(i) == 1){
//				if(i==storySeperateCount.size()-1){
//					storySeperateCountTmp.add(1);
//				}else{
//					storySeperateCountTmp.add(2);
//					storySeperateCount.set(i+1, storySeperateCount.get(i+1)-1);
//				}
//			}else{
//				if(storySeperateCount.get(i) != 0)
//					storySeperateCountTmp.add(storySeperateCount.get(i));
//			}
//		}
//		storySeperateCount = storySeperateCountTmp;
//		storySeperateCountTmp = new LinkedList<Integer>();
//		//Cut each group into fragment of length 2 or 3
//		for(int n : storySeperateCount){
//			if(n>3){
//				while(n>6){
//					storySeperateCountTmp.add(3);
//					n-=3;
//				}
//				if(n == 4){
//					storySeperateCountTmp.add(2);
//					storySeperateCountTmp.add(2);
//				}else if(n == 5){
//					storySeperateCountTmp.add(2);
//					storySeperateCountTmp.add(3);
//				}else if(n == 6){
//					storySeperateCountTmp.add(3);
//					storySeperateCountTmp.add(3);
//				}else{
//					storySeperateCountTmp.add(n);
//				}
//			}else{
//				storySeperateCountTmp.add(n);
//			}
//		}
//		storySeperateCount = storySeperateCountTmp;
//		
//		
//		List<List<StoryRecord>> storySeperatedList = new LinkedList <List<StoryRecord>> ();
//		List<StoryRecord> storyTempList = new LinkedList <StoryRecord>();;
//		int total = 0;
//		for(int num : storySeperateCount){
//			for(int i=0; i<num; i++){
//				storyTempList.add(storySeq.get(total+i));
//			}
//			storySeperatedList.add(storyTempList);
//			storyTempList = new LinkedList <StoryRecord>();
//			total += num;
//		}
//		
//		return storySeperatedList;
//	}
	
	public String getTwoCharacterEssay(List<StoryRecord> storySeq1, Character crt1, List<StoryRecord> storySeq2, Character crt2, List<String> actionList){

		
		StringBuffer essay = new StringBuffer();
		String rel1, rel2;
		String start, mid, end;
		String sentense = "";
		List<StoryRecord> storySeq;
		List<StoryRecord> storySubSeq1;
		List<StoryRecord> storySubSeq2;
		List<String> actionListWithLastConcept = new ArrayList<String>(actionList);
		actionListWithLastConcept.add(storySeq1.get(storySeq1.size()-1).getConcept());
		
		int startIdx1 = 0;
		int startIdx2 = 0;
		int endIdx1 = -1;
		int endIdx2 = -1;
		
		for(String commonAction : actionListWithLastConcept){
			storySubSeq1 = new ArrayList<StoryRecord>();
			storySubSeq2 = new ArrayList<StoryRecord>();
			for(int i=0;i<storySeq1.size(); i++){
				if(storySeq1.get(i).getConcept().equals(commonAction)){
					endIdx1 = i;
					break;
				}
			}
			for(int i=0;i<storySeq2.size(); i++){
				if(storySeq2.get(i).getConcept().equals(commonAction)){
					endIdx2 = i;
					break;
				}
			}
			for(int i=startIdx1; i<endIdx1+1; i++){
				storySubSeq1.add(storySeq1.get(i));
			}
			for(int i=startIdx2; i<endIdx2+1; i++){
				storySubSeq2.add(storySeq2.get(i));
			}
			for(int i=0; i<2; i++){
				if(i==0){
					character = crt1;
					storySeq = storySubSeq1;
				}else{
					character = crt2;
					storySeq = storySubSeq2;
				}
				//Generate story sequence
				int curStoryindex = 0;
				int remaimStorySize = storySeq.size();
				while(remaimStorySize > 0){
					if(remaimStorySize == 1){
						start = changeConcept(storySeq.get(curStoryindex).getConcept());
						essay.append(generateSentenseWithOneConcept(start) + " ");
						remaimStorySize -= 1;
					}else if(remaimStorySize == 2){
						start = changeConcept(storySeq.get(curStoryindex).getConcept());
						end = changeConcept(storySeq.get(curStoryindex+1).getConcept());
						rel1 = storySeq.get(curStoryindex+1).getRelation();
						essay.append(generateSentenseWithTwoConcept(start, end, rel1) + " ");
						remaimStorySize -= 2;
					}else{
						start = changeConcept(storySeq.get(curStoryindex).getConcept());
						mid = changeConcept(storySeq.get(curStoryindex+1).getConcept());
						end = changeConcept(storySeq.get(curStoryindex+2).getConcept());
						rel1 = storySeq.get(curStoryindex+1).getRelation();
						rel2 = storySeq.get(curStoryindex+2).getRelation();
						
						sentense = generateSentenseWithThreeConcept(start, mid, end, rel1, rel2);
						if(!sentense.equals("not found")){
							essay.append(sentense + " ");
							curStoryindex += 3;
							remaimStorySize -= 3;
						}else{
							essay.append(generateSentenseWithTwoConcept(start, mid, rel1) + " ");
							curStoryindex += 2;
							remaimStorySize -= 2;
						}
					}
				}
				essay.append("\n");
				startIdx1 = endIdx1;
				startIdx2 = endIdx2;
			}
		}
		
		return essay.toString();
	}
	
	private String generateSentenseWithTwoConcept(String start, String end, String rel){
		rel = rel.toLowerCase();
		String pastVerbStartConcept = wkt.getPastVerbTransformation(start);
		String progressiveVerbStartConcept = wkt.getProgressiveVerbTransformation(start);
		String pastVerbEndConcept = wkt.getPastVerbTransformation(end);
		String progressiveVerbEndConcept = wkt.getProgressiveVerbTransformation(end);
		Random random = new Random();
		String sentense = "";
		
		switch (rel) {
		case "hasprerequisite":
			switch (random.nextInt(4)) {
			case 0:
				sentense = character.getName()+" needed to " +start+ " to "+ end +".";
				break;
			case 1:
				sentense = character.getName()+" " +start+ " to "+ end +".";
				break;
			case 2:
				sentense = "After "+character.getName()+" "+ start+ ", "+character.getSubjectPronoun()+" "+ pastVerbEndConcept +".";
				break;
			case 3:
				sentense = character.getName()+" "+ pastVerbStartConcept+ ", then "+character.getSubjectPronoun()+" "+ pastVerbEndConcept +".";
				break;
			default:
				sentense = character.getName()+" needed to " +start+ " to "+ end +".";
				break;
			}
			break;
		case "motivatedbygoal":
			switch (random.nextInt(3)) {
			case 0:
				sentense = "In order to "+start+", "+character.getName()+" would "+end+".";
				break;
			case 1:
				sentense = character.getName()+" wanted to "+start+", so "+character.getSubjectPronoun()+" "+pastVerbEndConcept+".";
				break;
			case 2:
				sentense = "Since "+character.getName()+" would like to "+start+", "+character.getSubjectPronoun()+" "+pastVerbEndConcept+".";
				break;
			default:
				sentense = character.getName()+" wanted to "+end+", so "+character.getSubjectPronoun()+" "+pastVerbStartConcept+".";
				break;
			}
			break;
		case "usedfor":
			switch (random.nextInt(3)) {
			case 0:
				sentense = character.getName()+" wanted to "+start+", so "+character.getSubjectPronoun()+" "+pastVerbEndConcept+".";
				break;
			case 1:
				sentense = "Since "+character.getName()+" would like to "+start+", "+character.getSubjectPronoun()+" "+pastVerbEndConcept+".";
				break;
			case 2:
				sentense = "In order to "+start+", "+character.getName()+" would "+end+".";
				break;
			default:
				sentense = character.getName()+" "+pastVerbEndConcept+" to "+start+".";
				break;
			}
			break;
		case "capableof":
			switch (random.nextInt(3)) {
			case 0:
				sentense = character.getName()+" "+pastVerbStartConcept+" to "+end+".";
				break;
			case 1:
				sentense = character.getName() +" "+pastVerbStartConcept+" in order to " +end+".";
				break;
			case 2:
				sentense = character.getName()+" wanted to "+start+", so "+character.getSubjectPronoun()+" "+pastVerbEndConcept+".";
				break;
			default:
				sentense = character.getName()+" wanted to "+start+", so "+character.getSubjectPronoun()+" "+pastVerbEndConcept+".";
				break;
			}
			break;
		case "causes":
			switch (random.nextInt(3)) {
			case 0:
				sentense = "When "+character.getName()+" "+pastVerbStartConcept+", "+character.getSubjectPronoun()+" would "+end+".";
				break;
			case 1:
				sentense = character.getName()+" "+pastVerbStartConcept+", so "+character.getSubjectPronoun()+" "+end+".";
				break;
			case 2:
				sentense = character.getName()+" "+pastVerbStartConcept+", therefore "+character.getSubjectPronoun()+" would "+end+".";
				break;
			default:
				sentense = character.getName()+" would " +end+" because "+character.getSubjectPronoun() +" "+pastVerbStartConcept+".";
				break;
			}
			break;
		case "causesdesire":
			switch (random.nextInt(3)) {
			case 0:
				sentense = "When "+character.getName()+" "+pastVerbStartConcept+", "+character.getSubjectPronoun()+" would like to "+end+".";
				break;
			case 1:
				sentense = character.getName()+" "+pastVerbStartConcept+", so "+character.getSubjectPronoun()+" would desire to "+end+".";
				break;
			case 2:
				sentense = character.getName()+" "+pastVerbStartConcept+", therefore "+character.getSubjectPronoun()+" would want to  "+end+".";
				break;
			default:
				sentense = character.getName()+" would" +end+" because "+character.getSubjectPronoun() +" "+pastVerbStartConcept+".";
				break;
			}
			break;
		case "hascontext":
			sentense = start.substring(0,1).toUpperCase() + start.substring(1)+" had context of "+end+".";
		case "hasfirstsubevent":
			switch (random.nextInt(3)) {
			case 0:
				sentense = "When "+character.getName()+" "+pastVerbStartConcept+", "+character.getSubjectPronoun()+" would like to "+end+".";
				break;
			case 1:
				sentense = "While "+character.getName()+" "+progressiveVerbStartConcept+", "+character.getSubjectPronoun()+" would "+end+".";
				break;
			case 2:
				sentense = "As "+character.getName()+" "+pastVerbStartConcept+", "+character.getSubjectPronoun()+" would "+end+".";
				break;
			default:
				sentense = character.getName()+" would" +end+" because "+character.getSubjectPronoun() +" "+pastVerbStartConcept+".";
				break;
			}
			break;
		case "haslastsubevent":
			switch (random.nextInt(3)) {
			case 0:
				sentense = "When "+character.getName()+" "+pastVerbStartConcept+", "+character.getSubjectPronoun()+" would like to "+end+".";
				break;
			case 1:
				sentense = "While "+character.getName()+" "+progressiveVerbStartConcept+", "+character.getSubjectPronoun()+" would "+end+".";
				break;
			case 2:
				sentense = "As "+character.getName()+" "+pastVerbStartConcept+", "+character.getSubjectPronoun()+" would "+end+".";
				break;
			default:
				sentense = character.getName()+" would" +end+" because "+character.getSubjectPronoun() +" "+pastVerbStartConcept+".";
				break;
			}
			break;
		case "hassubevent":
			switch (random.nextInt(3)) {
			case 0:
				sentense = "When "+character.getName()+" "+pastVerbStartConcept+", "+character.getSubjectPronoun()+" would like to "+end+".";
				break;
			case 1:
				sentense = "While "+character.getName()+" "+progressiveVerbStartConcept+", "+character.getSubjectPronoun()+" would "+end+".";
				break;
			case 2:
				sentense = "As "+character.getName()+" "+pastVerbStartConcept+", "+character.getSubjectPronoun()+" would "+end+".";
				break;
			default:
				sentense = character.getName()+" would" +end+" because "+character.getSubjectPronoun() +" "+pastVerbStartConcept+".";
				break;
			}
			break;
		case "desires":
			sentense = start.substring(0,1).toUpperCase() + start.substring(1)+" wanted to "+end+".";
			break;
		case "desireof":
			sentense = start.substring(0,1).toUpperCase() + start.substring(1)+" wanted "+end+".";
			break;
		default:
			sentense = "not found";
			break;
		}
		return sentense;
	}
	
	public String generateSentenseWithThreeConcept(String start, String mid, String end, String rel1, String rel2){
		rel1 = rel1.toLowerCase();
		rel2 = rel2.toLowerCase();
		String sentense = "";
		String pastVerbStartConcept = wkt.getPastVerbTransformation(start);
		String progressiveVerbStartConcept = wkt.getProgressiveVerbTransformation(start);
		String pastVerbMidConcept = wkt.getPastVerbTransformation(mid);
		String progressiveVerbMidConcept = wkt.getProgressiveVerbTransformation(mid);
		String pastVerbEndConcept = wkt.getPastVerbTransformation(end);
		String progressiveVerbEndConcept = wkt.getProgressiveVerbTransformation(end);
		
		switch (rel1+" "+rel2) {
		case "capableof capableof":
			sentense = character.getName() + " " + pastVerbStartConcept + " in order to " + mid  + " and next " + character.getSubjectPronoun() + " would "+ end + ".";
			break;
		case "capableof hasfirstsubevent":
			sentense = character.getName() + " " + pastVerbStartConcept + " to " + mid + " and first of all " + character.getSubjectPronoun() + " "+ pastVerbEndConcept +".";
			break;
		case "capableof hassubevent":
			sentense = character.getName() + " " + pastVerbStartConcept + " to " + mid + " and " + character.getSubjectPronoun() + " "+ pastVerbEndConcept +".";
			break;
		case "capableof hasprerequisite":
			sentense = character.getName() + " " + pastVerbStartConcept + " in order to " + mid + ". After "+ progressiveVerbMidConcept + ", " + character.getSubjectPronoun() + " " + pastVerbEndConcept + ".";
			break;
		case "capableof motivatedbygoal":
			sentense = character.getName() + " " + pastVerbStartConcept + " to "+ mid + " and " + character.getSubjectPronoun() + " would " + end + ".";
			break;
		case "capableof usedfor":
			sentense = character.getName() + " wanted to " + start + ", so " + character.getSubjectPronoun() + " "+ pastVerbMidConcept + ". And then " + character.getSubjectPronoun() + " would " + end + ".";
			break;
		case "hasfirstsubevent capableof":
			sentense = character.getName() + " first "+ pastVerbStartConcept + " then " +pastVerbMidConcept+ " in order to "+ end +".";
			break;
		case "hasfirstsubevent hasfirstsubevent":
			sentense = "As " + character.getName() + " " + pastVerbStartConcept +", first " + character.getSubjectPronoun() + " would " + pastVerbMidConcept+ " and then "+ pastVerbEndConcept +".";
			break;
		case "hasfirstsubevent hassubevent":
			sentense = "As " + character.getName() + " " + pastVerbStartConcept + ", " + character.getSubjectPronoun() + " would " + mid + ". When " + progressiveVerbMidConcept + ", " + character.getSubjectPronoun() + " would " + end + ".";
			break;
		case "hasfirstsubevent hasprerequisite":
			sentense = "When " + character.getName() + " " + pastVerbStartConcept + ", " + character.getSubjectPronoun() + " would " + mid + ".  After "+ progressiveVerbMidConcept + ", " + character.getSubjectPronoun() + " " + pastVerbEndConcept + ".";
			break;
		case "hasfirstsubevent motivatedbygoal":
			sentense = "While " + character.getName() + " " + pastVerbStartConcept + ", " + character.getSubjectPronoun() + " would " + mid + " first. Since " + character.getSubjectPronoun() + " would " + mid + ", " + character.getSubjectPronoun() + ""+ pastVerbEndConcept + ".";
			break;
		case "hasfirstsubevent usedfor":
			sentense = "When " + character.getName() + " " + pastVerbStartConcept + ", " + character.getSubjectPronoun() + " would " + mid + " first and next " + character.getSubjectPronoun() + " would " + pastVerbEndConcept + "."; 
			break;
		case "hassubevent capableof":
			sentense = "As " + character.getName() + " " + pastVerbStartConcept + ", " + character.getSubjectPronoun() + " would " + mid + " so as to "+ end + ".";
			break;
		case "hassubevent hasfirstsubevent":
			sentense = "While " + character.getName() + " " + pastVerbStartConcept + ", " + character.getSubjectPronoun() + " would " + mid + ". As "+ progressiveVerbMidConcept + ", " + character.getSubjectPronoun() + " would like to " + end + ".";
			break;
		case "hassubevent hassubevent":
			sentense = "When " + character.getName() + " " + pastVerbStartConcept + ", " + character.getSubjectPronoun() + " would " + mid + " and as " + progressiveVerbMidConcept + ", " + character.getSubjectPronoun() + " " + pastVerbEndConcept + ".";
			break;
		case "hassubevent hasprerequisite":
			sentense = "As " + character.getName() + " " + pastVerbStartConcept + ", " + character.getSubjectPronoun() + " would " + mid + " and then " + end + ".";
			break;
		case "hassubevent motivatedbygoal":
			sentense = "When " + character.getName() + " " + pastVerbStartConcept + ", " + character.getSubjectPronoun() + " " + pastVerbMidConcept + " in order to " + end + ".";
			break;
		case "hassubevent usedfor":
			sentense = "While " + character.getName() + " " + pastVerbStartConcept + ", " + character.getSubjectPronoun() + " " + pastVerbMidConcept + " to " + end + ".";
			break;
		case "hasprerequisite capableof":
			sentense = "After " + character.getName() + " " + pastVerbStartConcept + ", " + character.getSubjectPronoun() + " " + pastVerbMidConcept + " to " + end + ".";
			break;
		case "hasprerequisite hasfirstsubevent":
			sentense = "After " + character.getName() + " " + pastVerbStartConcept + ", " + character.getSubjectPronoun() + " " + pastVerbMidConcept +". Thus " + character.getSubjectPronoun() + " would " + end + " first.";
			break;
		case "hasprerequisite hassubevent":
			sentense ="After " + character.getName() + " " + pastVerbStartConcept + ", " + character.getSubjectPronoun() + " " + pastVerbMidConcept + " and " + pastVerbEndConcept + ".";
			break;
		case "hasprerequisite hasprerequisite":
			sentense = character.getName() + " " + pastVerbStartConcept + ", " + character.getSubjectPronoun() + " would like to " + mid + " and next " + character.getSubjectPronoun() + " " + pastVerbEndConcept + ".";
			break;
		case "hasprerequisite motivatedbygoal":
			sentense = "After " + character.getName() + " " + pastVerbStartConcept + ", " + character.getSubjectPronoun() + " " + pastVerbMidConcept + " in order to " + end + ".";
			break;
		case "hasprerequisite usedfor":
			sentense = character.getName() + " " + pastVerbStartConcept + ", then " + character.getSubjectPronoun() + " " + pastVerbMidConcept + " to  "+ end +".";
			break;
		case "motivatedbygoal capableof":
			sentense = "In order to "  + start + ", " + character.getName() + " " + pastVerbMidConcept + " to " + end + ".";
			break;
		case "motivatedbygoal hasfirstsubevent":
			sentense = "In order to "  + start + ", " + character.getName() + " would " + pastVerbMidConcept + " and first " + character.getSubjectPronoun() + " " + end + ".";
			break;
		case "motivatedbygoal hassubevent":
			sentense = "Since " + character.getName() + " would like to " + start + ", " + character.getSubjectPronoun() + " " + pastVerbMidConcept + " and " + pastVerbEndConcept + ".";
			break;
		case "motivatedbygoal hasprerequisite":
			sentense = "To "  + start + ", " + character.getName() + " need to " + pastVerbMidConcept + " to " + end + ".";
			break;
		case "motivatedbygoal motivatedbygoal":
			sentense = "To "  + start + ", " + character.getName() + " would like to " + mid + " and so " + character.getSubjectPronoun() + " " + pastVerbEndConcept + ".";
			break;
		case "motivatedbygoal usedfor":
			sentense = "Since " + character.getName() + " would like to " + start + ", " + character.getSubjectPronoun() + " " + pastVerbMidConcept + " to " + end + ".";
			break;
		case "usedfor capableof":
			sentense = character.getName() + " " + pastVerbStartConcept + " to " + mid + " in order to " + pastVerbEndConcept +".";
			break;
		case "usedfor hasfirstsubevent":
			sentense = "Since " + character.getName() + " would like to " + start + ", " + character.getSubjectPronoun() + " " + pastVerbMidConcept + ". While " + progressiveVerbMidConcept + ", " + character.getSubjectPronoun() + " " + pastVerbEndConcept +".";
			break;
		case "usedfor hassubevent":
			sentense = character.getName() + " " + pastVerbStartConcept + ", and while " + progressiveVerbMidConcept + ", " + character.getSubjectPronoun() + " " + pastVerbEndConcept + ".";
			break;
		case "usedfor hasprerequisite":
			sentense = "In order to "  + start + ", " + character.getName() + " would " + mid + " and then " + end + ".";
			break;
		case "usedfor motivatedbygoal":
			sentense = character.getName() + " wanted to " +start+ ", so " + character.getSubjectPronoun() + " " + pastVerbMidConcept +" in order to " + end + ".";
			break;
		case "usedfor usedfor":
			sentense = character.getName()+" needed to " +start+ " to "+ end +".";
			break;
		
		default:
			sentense = "not found";
			break;
		}
		return sentense;
	}
	
	public String generateSentenseWithOneConcept(String start){
		return "And " + character.getSubjectPronoun() + " " + wkt.getPastVerbTransformation(start) + ".";
	}
	
	private String changeConcept(String cpt){
		
		String[] sp = cpt.split("_");
		String newCpt = "";
		boolean first = true;
		for(String s:sp){
			if(s.equals("you")) s = character.getObjectPronoun();
			else if(s.equals("your")) s = character.getPossessivePronouns();
			else if(s.equals("yourself")) s = character.getReflexivePronoun();
			
			if (first){
				first = false;
				newCpt = s;
			}else newCpt = newCpt+" "+s;
		}
		
		return newCpt;
	}
	
}
