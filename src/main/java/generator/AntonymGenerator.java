package generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.jwktl.JWKTL;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEdition;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEntry;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryRelation;
import de.tudarmstadt.ukp.jwktl.api.RelationType;

public class AntonymGenerator {
	List<String> antonyms = new ArrayList<String>();
	IWiktionaryEdition wkt;
	String antonym;
	public AntonymGenerator(){
		wkt = JWKTL.openEdition(new File(Parameter.wiktionaryPath));
	}
	
	public List<String> generateAntonym(String str){
		List<IWiktionaryEntry> entries = wkt.getEntriesForWord(str);
		for(IWiktionaryEntry e : entries){
			//System.out.println(e.getRelations(RelationType.ANTONYM));
			for(IWiktionaryRelation iwkr : e.getRelations(RelationType.ANTONYM)){
				antonym = iwkr.getTarget().trim().toString();
				if(!antonyms.contains(antonym))
					antonyms.add(antonym);
			}
		}
		wkt.close();
		return antonyms;
	}
}
