package others;

import java.io.File;
import java.util.List;

import de.tudarmstadt.ukp.jwktl.JWKTL;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEdition;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEntry;
import generator.Parameter;

public class Wikitionary {
	
	private IWiktionaryEdition wkt;
	
	public Wikitionary(){
		wkt = JWKTL.openEdition(new File(Parameter.wiktionaryPath));
	}
	
	public void releaseWKT(){
		wkt.close();
	}
	
	/**
	 * If the concept has a verb at the first position, transform the verb into the given tense and return the concept.
	 * NOTE : input concept must be verb on the first position (must be check outside the method while called), and be split by " "
	 * 
	 * WordForms (0 : present, 1 : progressive, 2 : past, 3 : perfect)
	 * @param concept 		input concept
	 * @param tense 		tense to transform
	 * @return	concept
	 */
	public String getProgressiveVerbTransformation(String concept){
		String[] cptsplit = concept.split(" ");
		try{
			List<IWiktionaryEntry> entries = wkt.getEntriesForWord(cptsplit[0]);
			for(IWiktionaryEntry e : entries)
				if(e.getPartOfSpeech().name().equals("VERB")){
//					System.out.println(e.getEntryLink());
//					System.out.println(e.getWordForms().size());
//					System.out.println(e.getWordForms().get(tense).getWordForm());
					//if there is not any word form, we give a simple check on the verb
					if(e.getWordForms() == null) {
						String replace = cptsplit[0];
						if(cptsplit[0].equals("be")){
							replace = "being";
						}else if(cptsplit[0].equals("has")){
							replace = "having";
						}else if(cptsplit[0].charAt(cptsplit[0].length()-1) == 'e'){
							replace = replace.substring(0, replace.lastIndexOf("e")) + "ing";
						}else{
							replace = cptsplit[0]+"ing";
						}
						return concept.replace(cptsplit[0], replace);
					}else{
						//1 : progressive tense
						return concept.replace(cptsplit[0], e.getWordForms().get(1).getWordForm());
					}
				}
		}catch(Exception e){
			e.printStackTrace();
		}
		return concept;
	}
	
	public String getPastVerbTransformation(String concept){
		String[] cptsplit = concept.split(" ");
		try{
			List<IWiktionaryEntry> entries = wkt.getEntriesForWord(cptsplit[0]);
			for(IWiktionaryEntry e : entries)
				if(e.getPartOfSpeech().name().equals("VERB")){
					//dealing with the verb that are in the dictionary
					if(e.getWordForms() == null) {
						String replace = cptsplit[0];
						if(cptsplit[0].equals("be")){
							replace = "was";
						}else if(cptsplit[0].equals("has")){
							replace = "had";
						}else if(cptsplit[0].charAt(cptsplit[0].length()-1) == 'e'){
							replace = cptsplit[0]+"d";
						}else{
							replace = cptsplit[0]+"ed";
						}
						return concept.replace(cptsplit[0], replace);
					}else{
						//2 : past tense
//						System.out.println(e.getWordForms().get(2).getWordForm());
						return concept.replace(cptsplit[0], e.getWordForms().get(2).getWordForm());
					}
				}
		}catch(Exception e){
			e.printStackTrace();
		}
		return concept;
	}
	/**
	 * check the word is verb or not
	 * @param concept
	 * @return
	 */
	public boolean isVerb(String word){
		try{
			List<IWiktionaryEntry> entries = wkt.getEntriesForWord(word);
			for(IWiktionaryEntry e : entries){
				//System.out.println(entries.size());
				if(e.getPartOfSpeech().name().equals("VERB")){
					return true;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
}
