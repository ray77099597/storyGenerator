package others;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import edu.stanford.nlp.simple.Sentence;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

/**
 * used in Essay generator and ConceptToFabulaElementFilter
 * @author liqimou
 *
 */
public class Tagger {
	private static InputStream modelIn = null;
	private static POSModel posModel = null;
	private static POSTaggerME tagger = null;
	
	public Tagger(){
		try {
			modelIn = new FileInputStream("en-pos-maxent.bin");
			posModel = new POSModel(modelIn);
			tagger = new POSTaggerME(posModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void taggerRelease() {
		if (modelIn != null) {
			try {
				modelIn.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public String[] tagConcept (String cpt){
		return tagger.tag(cpt.split("_"));
	}
	
	public List<String> tagConcept_CoreNLP (String cpt){
		return new Sentence(cpt.replace("_", " ")).posTags();
	}
	
	public boolean isVerb(String str) {
		if (str.equals("VB") || str.equals("VBN") || str.equals("VBG")
				|| str.equals("VBP") || str.equals("VBD") || str.equals("VBZ")) {
			return true;
		}
		return false;
	}

	public boolean isNoun(String str) {
		if (str.equals("NN") || str.equals("NNS") || str.equals("NNP")
				|| str.equals("NNPS")) {
			return true;
		}
		return false;
	}

	public boolean isAdj(String str) {
		if (str.equals("JJ")) {
			return true;
		}
		return false;
	}

	public boolean isPronoun(String str) {
		if (str.equals("PRP") || str.equals("FW")) {
			return true;
		}
		return false;
	}
	
	
}
