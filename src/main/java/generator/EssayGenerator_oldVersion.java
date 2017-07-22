package generator;

import java.io.File;
import java.util.List;
import java.util.Random;

import de.tudarmstadt.ukp.jwktl.JWKTL;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEdition;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEntry;
import others.StoryRecord;
import others.Tagger;

public class EssayGenerator_oldVersion {
//	private IWiktionaryEdition wkt;
//	Tagger tagger;
//	public EssayGenerator_oldVersion(){
//		wkt = JWKTL.openEdition(new File(Parameter.wiktionaryPath));
//		tagger = new Tagger();
//	}
//	public void releaseWKT(){
//		wkt.close();
//		tagger.taggerRelease();
//	}
//	public String getEssay(List<StoryRecord> storySeq){
//		StringBuffer essay = new StringBuffer();
//		String rel;
//		String start;
//		String end;
//		String sentense;
//		String prevConcept = "";
//		String sp[];
//		for(StoryRecord term : storySeq){
//			int i = storySeq.indexOf(term);
//			if(i!=0){
//				if(i%2!=0){
//					rel = term.getRelation();
//						sp = prevConcept.split("_");
//						boolean first = true;
//						start = "";
//						for(String s:sp){
//							if(s.equals("you")) s = Parameter.objectPronoun_male;
//							else if(s.equals("your")) s = Parameter.possessivePronouns_male;
//							else if(s.equals("yourself")) s = Parameter.reflexivePronoun_male;
//							
//							if (first){
//								first = false;
//								start = s;
//							}else start = start+" "+s;
//						}
//						sp = term.getConcept().split("_");
//						first = true;
//						end = "";
//						for(String s:sp){
//							if(s.equals("you")) s = Parameter.objectPronoun_male;
//							else if(s.equals("your")) s = Parameter.possessivePronouns_male;
//							else if(s.equals("yourself")) s = Parameter.reflexivePronoun_male;
//							
//							if (first){
//								first = false;
//								end = s;
//							}else end = end+" "+s;
//						}
//					sentense = generateSentense(start, end, rel);
//					essay.append(sentense);
//					essay.append(" ");
//				}
//			}
//			if(i==storySeq.size()-1 && i%2==0){
//				end = term.getConcept().replace("_", " ");
//				essay.append("And "+Parameter.subjectPronoun_male+" "+ changePOS(end, 2) +".");
//			}
//			prevConcept = term.getConcept();
//		}
//		
//		return essay.toString();
//	}
//	private String generateSentense(String start, String end, String rel){
//		Random random = new Random();
//		String sentense = "";
//		String startTag;
//		String endTag;
//		
//		switch (rel) {
//		case "hasprerequisite":
////			sentense = "NP|VP requires NP|VP";
////			startTag = tag(start);
////			endTag = tag(end);
////			if(isVerb(startTag) && isNoun(endTag)){
////				sentense = character.getName()+" need " +start+ " to "+ end +".";
////			}else if(isVerb(startTag) && isVerb(endTag)){
////				sentense = "After "+character.getName()+" "+start+", "+character.getName()+" "+end+".";
////			}else{
////				sentense = character.getName()+" need " +start+ " to "+ end +".";
////			}
//			
//			switch (random.nextInt(4)) {
//			case 0:
//				sentense = character.getName()+" needed to " +start+ " to "+ end +".";
//				break;
//			case 1:
//				sentense = character.getName()+" " +start+ " to "+ end +".";
//				break;
//			case 2:
//				sentense = "After "+character.getName()+" "+ start+ ", "+Parameter.subjectPronoun_male+" "+ changePOS(end, 2) +".";
//				break;
//			case 3:
//				sentense = character.getName()+" "+ changePOS(start, 2)+ ", then "+Parameter.subjectPronoun_male+" "+ changePOS(end, 2) +".";
//				break;
//			default:
//				sentense = character.getName()+" needed to " +start+ " to "+ end +".";
//				break;
//			}
//			break;
//		case "motivatedbygoal":
////			sentense = character.getName()+" would VP because "+character.getName()+" want VP.";
////			sentense = "In order to "+start+", "+character.getName()+" will "+end+".";
//			switch (random.nextInt(3)) {
//			case 0:
//				sentense = "In order to "+start+", "+character.getName()+" would "+end+".";
////				sentense = character.getName()+" would "+end+" in order to "+start+".";
//				break;
//			case 1:
//				sentense = character.getName()+" wanted to "+start+", so "+Parameter.subjectPronoun_male+" "+changePOS(end, 2)+".";
////				sentense = character.getName()+" "+changePOS(end, 2)+" for "+changePOS(start, 1)+".";
//				break;
//			case 2:
////				sentense = character.getName()+" wanted to "+end+", so "+Parameter.subjectPronoun_male+" "+changePOS(start, 2)+".";
//				sentense = "Since "+character.getName()+" would like to "+start+", "+Parameter.subjectPronoun_male+" "+changePOS(end, 2)+".";
//				break;
//			default:
//				sentense = character.getName()+" wanted to "+end+", so "+Parameter.subjectPronoun_male+" "+changePOS(start, 2)+".";
//				break;
//			}
//			break;
//		case "usedfor":
////			sentense = "NP is used for VP";
////			sentense = character.getName()+" "+start+" by "+end+".";
//			switch (random.nextInt(3)) {
//			case 0:
//				sentense = character.getName()+" wanted to "+start+", so "+Parameter.subjectPronoun_male+" "+changePOS(end, 2)+".";
//				break;
//			case 1:
//				sentense = "Since "+character.getName()+" would like to "+start+", "+Parameter.subjectPronoun_male+" "+changePOS(end, 2)+".";
//				break;
//			case 2:
//				sentense = "In order to "+start+", "+character.getName()+" would "+end+".";
//				break;
//			default:
//				sentense = character.getName()+" "+changePOS(end, 2)+" to "+start+".";
//				break;
//			}
//			break;
//		case "capableof":
////			sentense = start.substring(0,1).toUpperCase() + start.substring(1)+" can "+end+".";
////			sentense = "NP can VP";
//			switch (random.nextInt(3)) {
//			case 0:
//				sentense = character.getName()+" "+changePOS(start, 2)+" to "+end+".";
//				break;
//			case 1:
//				sentense = character.getName() +" "+changePOS(start, 2)+" in order to " +end+".";
//				break;
//			case 2:
//				sentense = character.getName()+" wanted to "+start+", so "+Parameter.subjectPronoun_male+" "+changePOS(end, 2)+".";
//				break;
//			default:
//				sentense = character.getName()+" wanted to "+start+", so "+Parameter.subjectPronoun_male+" "+changePOS(end, 2)+".";
//				break;
//			}
//			break;
//		case "causes":
////			sentense = "The effect of VP is NP|VP";
////			startTag = tag(start);
////			endTag = tag(end);
////			if(isNoun(endTag)){
////				sentense = start.substring(0,1).toUpperCase() + start.substring(1)+" causes "+end+".";
////			}else if(isVerb(endTag)){
////				sentense = start.substring(0,1).toUpperCase() + start.substring(1)+" will make "+character.getName()+" "+end+".";
////			}else{
////				sentense = start.substring(0,1).toUpperCase() + start.substring(1)+" causes "+end+".";
////			}
//			switch (random.nextInt(3)) {
//			case 0:
//				sentense = "When "+character.getName()+" "+changePOS(start, 2)+", "+Parameter.subjectPronoun_male+" would "+end+".";
//				break;
//			case 1:
////				sentense = character.getName()+" would " +end+" because "+Parameter.subjectPronoun_male +" "+changePOS(start, 2)+".";
//				sentense = character.getName()+" "+changePOS(start, 2)+", so "+Parameter.subjectPronoun_male+" "+end+".";
//				break;
//			case 2:
//				sentense = character.getName()+" "+changePOS(start, 2)+", therefore "+Parameter.subjectPronoun_male+" would "+end+".";
//				break;
//			default:
//				sentense = character.getName()+" would " +end+" because "+Parameter.subjectPronoun_male +" "+changePOS(start, 2)+".";
//				break;
//			}
//			break;
//		case "causesdesire":
////			sentense = start.substring(0,1).toUpperCase() + start.substring(1)+" will make "+character.getName()+" want to "+end+".";
////			sentense = "NP would make "+character.getName()+" want to VP.";
//			switch (random.nextInt(3)) {
//			case 0:
//				sentense = "When "+character.getName()+" "+changePOS(start, 2)+", "+Parameter.subjectPronoun_male+" would like to "+end+".";
//				break;
//			case 1:
////				sentense = character.getName()+" would" +end+" because "+Parameter.subjectPronoun_male +" "+changePOS(start, 2)+".";
//				sentense = character.getName()+" "+changePOS(start, 2)+", so "+Parameter.subjectPronoun_male+" would desire to "+end+".";
//				break;
//			case 2:
//				sentense = character.getName()+" "+changePOS(start, 2)+", therefore "+Parameter.subjectPronoun_male+" would want to  "+end+".";
//				break;
//			default:
//				sentense = character.getName()+" would" +end+" because "+Parameter.subjectPronoun_male +" "+changePOS(start, 2)+".";
//				break;
//			}
//			break;
//		case "hascontext":
//			sentense = start.substring(0,1).toUpperCase() + start.substring(1)+" had context of "+end+".";
////			sentense = "NP has context of NP";
//			break;
//		case "hasfirstsubevent":
////			sentense = "Before "+character.getName()+" "+start+" "+character.getName()+" will "+end+" first.";
////			sentense = "The first thing "+character.getName()+" do when "+character.getName()+" VP is NP|VP";
//			switch (random.nextInt(3)) {
//			case 0:
//				sentense = "When "+character.getName()+" "+changePOS(start, 2)+", "+Parameter.subjectPronoun_male+" would like to "+end+".";
//				break;
//			case 1:
////				sentense = character.getName()+" would "+end+", while "+Parameter.subjectPronoun_male+" "+changePOS(start, 2)+", ";
//				sentense = "While "+character.getName()+" "+changePOS(start, 1)+", "+Parameter.subjectPronoun_male+" would "+end+".";
//				break;
//			case 2:
//				sentense = "As "+character.getName()+" "+changePOS(start, 2)+", "+Parameter.subjectPronoun_male+" would "+end+".";
//				break;
//			default:
//				sentense = character.getName()+" would" +end+" because "+Parameter.subjectPronoun_male +" "+changePOS(start, 2)+".";
//				break;
//			}
//			break;
//		case "haslastsubevent":
////			sentense = "After "+character.getName()+" "+start+" "+character.getName()+" will "+end+".";
////			sentense = "The last thing "+character.getName()+" do when "+character.getName()+" VP is NP|VP";
//			switch (random.nextInt(3)) {
//			case 0:
//				sentense = "When "+character.getName()+" "+changePOS(start, 2)+", "+Parameter.subjectPronoun_male+" would like to "+end+".";
//				break;
//			case 1:
////				sentense = character.getName()+" would "+end+", while "+Parameter.subjectPronoun_male+" "+changePOS(start, 2)+", ";
//				sentense = "While "+character.getName()+" "+changePOS(start, 1)+", "+Parameter.subjectPronoun_male+" would "+end+".";
//				break;
//			case 2:
//				sentense = "As "+character.getName()+" "+changePOS(start, 2)+", "+Parameter.subjectPronoun_male+" would "+end+".";
//				break;
//			default:
//				sentense = character.getName()+" would" +end+" because "+Parameter.subjectPronoun_male +" "+changePOS(start, 2)+".";
//				break;
//			}
//			break;
//		case "hassubevent":
////			startTag = tag(start);
////			endTag = tag(end);
////			if(isNoun(endTag)){
////				sentense = character.getName()+" will "+start+" to do "+end+".";
////			}else if(isVerb(endTag)){
////				sentense = "when "+character.getName()+" "+start+" "+character.getName()+" will "+end+".";
////			}else{
////				sentense = character.getName()+" will "+start+" to do "+end+".";
////			}
////			sentense = "One of the things "+character.getName()+" do when "+character.getName()+" VP is NP|VP.";
//			switch (random.nextInt(3)) {
//			case 0:
//				sentense = "When "+character.getName()+" "+changePOS(start, 2)+", "+Parameter.subjectPronoun_male+" would like to "+end+".";
//				break;
//			case 1:
////				sentense = character.getName()+" would "+end+", while "+Parameter.subjectPronoun_male+" "+changePOS(start, 2)+", ";
//				sentense = "While "+character.getName()+" "+changePOS(start, 1)+", "+Parameter.subjectPronoun_male+" would "+end+".";
//				break;
//			case 2:
//				sentense = "As "+character.getName()+" "+changePOS(start, 2)+", "+Parameter.subjectPronoun_male+" would "+end+".";
//				break;
//			default:
//				sentense = character.getName()+" would" +end+" because "+Parameter.subjectPronoun_male +" "+changePOS(start, 2)+".";
//				break;
//			}
//			break;
//		case "desires":
//			sentense = start.substring(0,1).toUpperCase() + start.substring(1)+" wanted to "+end+".";
////			sentense = "NP wants to VP.";
//			break;
//		case "desireof":
//			sentense = start.substring(0,1).toUpperCase() + start.substring(1)+" wanted "+end+".";
////			sentense = "NP wants to VP.";
//			break;
//		default:
//			sentense = "";
//			break;
//		}
//		return sentense;
//	}
//	/**
//	 * WordForms
//	 * 1.s
//	 * 2.ing
//	 * 3.past
//	 * 4.pp
//	 * @param s
//	 * @return
//	 */
//	private String changePOS(String s, int tense){
//		String cs = s.replace(" ", "_");
//		String[] ls = s.split(" ");
//		try{
//		if(tagger.isVerb(tagger.tagConcept(cs)[0])){
//			List<IWiktionaryEntry> entries = wkt.getEntriesForWord(ls[0]);
////			System.out.println("=====");
//			for(IWiktionaryEntry e : entries)
//				if(e.getPartOfSpeech().name().equals("VERB")){
//					//System.out.println(ls[0]);
////					System.out.println(e.getEntryLink());
////					System.out.println(e.getWordForms().size());
////					System.out.println(e.getWordForms().get(tense).getWordForm());
//					if(e.getWordForms() == null) {
//						String replace = ls[0];
//						if(ls[0].equals("be")){
//							if(tense==1){
//								replace = "being";
//							}else if(tense==2){
//								replace = "was";
//							}
//						}else if(ls[0].equals("has")){
//							if(tense==1){
//								replace = "having";
//							}else if(tense==2){
//								replace = "had";
//							}
//						}else if(ls[0].charAt(ls[0].length()-1) == 'e'){
//							if(tense==1){
//								replace = replace.substring(0, replace.lastIndexOf("e")) + "ing";
//							}else if(tense==2){
//								replace = ls[0]+"d";
//							}
//						}else{
//							if(tense==1){
//								replace = ls[0]+"ing";
//							}else if(tense==2){
//								replace = ls[0]+"ed";
//							}
//						}
//						return s.replace(ls[0], replace);
//					}else{
////						System.out.println(s + " " + ls[0] + " " + str + " " +e.getWordForms().get(tense).getWordForm());
//						return s.replace(ls[0], e.getWordForms().get(tense).getWordForm());
//					}
//				}
//		}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return s;
//	}
	
}
