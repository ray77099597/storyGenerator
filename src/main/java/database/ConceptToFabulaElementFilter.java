package database;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import generator.Parameter;
import others.Tagger;
import others.Wikitionary;

public class ConceptToFabulaElementFilter {
	private List<String> cptList;
	private List<String> tagList;
	private Tagger tagger;
	private Wikitionary wkt;
	
	public void filterInit(){
		tagger = new Tagger();
		wkt = new Wikitionary();
		Parameter.relationCounter.put("goal_attain", 0);
		Parameter.relationCounter.put("goal_maintain", 0);
		Parameter.relationCounter.put("goal_stop", 0);
		Parameter.relationCounter.put("goal_avoid", 0);
		Parameter.relationCounter.put("perception_see", 0);
		Parameter.relationCounter.put("perception_hear", 0);
		Parameter.relationCounter.put("perception_smell", 0);
		Parameter.relationCounter.put("perception_taste", 0);
		Parameter.relationCounter.put("perception_feel", 0);
		Parameter.relationCounter.put("action", 0);
		Parameter.relationCounter.put("outcome", 0);
		Parameter.relationCounter.put("event", 0);
		Parameter.relationCounter.put("internalElement_cognition", 0);
		Parameter.relationCounter.put("internalElement_beliefElement", 0);
		Parameter.relationCounter.put("internalElement_emotion", 0);
	}
	
	public void filterRelease(){
		tagger.taggerRelease();
		wkt.releaseWKT();
	}
	
	public Set <String> filter(String cpt) {
		Set <String> fabulaElementState = new HashSet <String>();
		
		//tag and split first using the original concept
		tagList = tagger.tagConcept_CoreNLP(cpt);
		cptList = new LinkedList<String>(Arrays.asList(cpt.split("_")));

		//check concept first
		if(isUnacceptedConcept()) return fabulaElementState;
		
		// classify event (first judge for event)
		if (isEvent()) {
			fabulaElementState.add("e");
			addRelationCounter("event");
			return fabulaElementState;
		}
		
		
		// if the first word is not pronoun, add a pronoun on first position to help determine the tags 
		if (!tagger.isPronoun(tagList.get(0))) {
			//if concept only have one word, classify it directly
			if(tagList.size() > 1){
				List<String>tmp = new LinkedList<String>();
				cpt = "you_" + cpt;
				tmp = tagger.tagConcept_CoreNLP(cpt);
				//remove pronoun
				tagList = new LinkedList<String>();
				for(int i=1; i<tmp.size(); i++){
					tagList.add(tmp.get(i));
				}
				//tagList.remove(0);
			}
		}

		// classify fabula element
		if (isPerception()) {
			fabulaElementState.add("p");
		} else if (isInternalElement()) {
			fabulaElementState.add("ie");
		} else if (isGoal()) {
			fabulaElementState.add("g");
		} else if (isOutcome()) {
			fabulaElementState.add("o");
		} else if (isAction()) {
			//TODO action concept here should not be add to event!!
			fabulaElementState.add("e");
			fabulaElementState.add("a");
		}
		
		return fabulaElementState;
	}
	
	private boolean isUnacceptedConcept(){
		
		//empty concept
		if(tagList.size() == 0) return true;
		//only one pronoun
		if(tagList.size() == 1 && tagger.isPronoun(tagList.get(0))) return true;
		
		return false;
	}
	
	// sustain leave attain avoid
	private boolean isGoal() {
		String firstWord = cptList.get(0);
		/**
		 * attain
		 */
		// to+V in first position
		if (cptList.size() >= 2) {
			if (firstWord.equals("to") && tagger.isVerb(tagList.get(1))) {
				addRelationCounter("goal_attain");
				return true;
			}
		}
		// want, need, wish, demand, intend, reach in first position
		if (firstWord.equals("want") || firstWord.equals("need")
				|| firstWord.equals("wish") || firstWord.equals("demand")
				|| firstWord.equals("desire") || firstWord.equals("intend")
				|| firstWord.equals("reach") || firstWord.equals("require")) {
			addRelationCounter("goal_attain");
			return true;
		}
		/**
		 * avoid
		 */
		// avoid, avert, prevent
		if (firstWord.equals("avoid") || firstWord.equals("avert")
				|| firstWord.equals("bypass") || firstWord.equals("escape")
				|| firstWord.equals("evade") || firstWord.equals("prevent")) {
			addRelationCounter("goal_avoid");
			return true;
		}
		// stay/keep+away/from
		if (cptList.size() >= 2) {
			if ((firstWord.equals("stay") || firstWord.equals("keep"))
					&& (cptList.get(1).equals("away") || cptList.get(1).equals(
							"from"))) {
				addRelationCounter("goal_avoid");
				return true;
			}
		}
		/**
		 * maintain
		 */
		// maintain, keep, retain, preserve ,stay
		if (firstWord.equals("maintain") || firstWord.equals("keep")
				|| firstWord.equals("retain") || firstWord.equals("preserve")
				|| firstWord.equals("sustain") || firstWord.equals("continue")
				|| firstWord.equals("remain")) {
			addRelationCounter("goal_maintain");
			return true;
		}
		// stay
		if (cptList.size() >= 2) {
			if (firstWord.equals("stay")
					&& (tagger.isVerb(tagList.get(1)) || tagger.isAdj(tagList.get(1)))) {
				addRelationCounter("goal_maintain");
				return true;
			}
		}
		/**
		 * stop
		 */
		// stop, pause, finish, cease, end, halt
		if (firstWord.equals("stop") || firstWord.equals("pause")
				|| firstWord.equals("finish") || firstWord.equals("cease")
				|| firstWord.equals("end") || firstWord.equals("halt")) {
			addRelationCounter("goal_stop");
			return true;
		}

		return false;
	}

	// Verb
	private boolean isAction() {
//		if(cptList.size() == 1){
//			if(tagger.isVerb(tagList.get(0)) && wkt.isVerb(tagList.get(0))){
//				addRelationCounter("action");
//				addRelationCounter("event");
//				return true;
//			}
//		}else{
			if (tagger.isVerb(tagList.get(0))) {
				addRelationCounter("action");
				addRelationCounter("event");
				return true;
			}
//		}
		
		return false;
	}

	private boolean isEvent() {
		// Noun+Verb
		if (tagList.size() >= 2) {
			if (tagger.isNoun(tagList.get(0)) && tagger.isVerb(tagList.get(1))) {
				return true;
			}
		}
		return false;
	}

	// Cognition BeliefElement Emotion
	private boolean isInternalElement() {
		String firstWord = cptList.get(0);
		// Cognition
		if (cptList.size() >= 2) {
			if (firstWord.equals("think") && cptList.get(1).equals("of")) {
				addRelationCounter("internalElement_cognition");
				return true;
			}
		}
		if (firstWord.equals("know") || firstWord.equals("learn")
				|| firstWord.equals("perceive")
				|| firstWord.equals("understand")
				|| firstWord.equals("remember") || firstWord.equals("notice")
				|| firstWord.equals("realize") || firstWord.equals("consider")
				|| firstWord.equals("recognize") || firstWord.equals("cognize")
				|| firstWord.equals("appreciate")
				|| firstWord.equals("experience")) {
			addRelationCounter("internalElement_cognition");
			return true;
		}
		// BeliefElement
		if (firstWord.equals("expect") || firstWord.equals("hope")
				|| firstWord.equals("wish") || firstWord.equals("believe")
				|| firstWord.equals("affirm") || firstWord.equals("trust")
				|| firstWord.equals("suspicion") || firstWord.equals("guess")
				|| firstWord.equals("doubt") || firstWord.equals("suppose")
				|| firstWord.equals("judge") || firstWord.equals("predict")
				|| firstWord.equals("presume") || firstWord.equals("infer")
				|| firstWord.equals("estimate")) {
			addRelationCounter("internalElement_beliefElement");
			return true;
		}
		// Emotion
		if (firstWord.equals("feel")) {
			addRelationCounter("internalElement_emotion");
			return true;
		}
		if (cptList.size() >= 2) {
			if (firstWord.equals("be") || firstWord.equals("get")
					|| firstWord.equals("become")) {
				if (isEmotion(cptList.get(1))) {
					addRelationCounter("internalElement_emotion");
					return true;
				}
			}
		}
		return false;
	}

	// see/hear/smell/taste/feel + Noun
	private boolean isPerception() {

		String firstWord = cptList.get(0);
		// see
		if (firstWord.equals("see") || firstWord.equals("watch")) {
			addRelationCounter("perception_see");
			return true;
		}
		if (cptList.size() >= 2) {
			if (firstWord.equals("look") && cptList.get(1).equals("at")) {
				addRelationCounter("perception_see");
				return true;
			}
		}
		// hear
		if (firstWord.equals("hear")) {
			addRelationCounter("perception_hear");
			return true;
		}
		if (cptList.size() >= 2) {
			if (firstWord.equals("listen") && (cptList.get(1).equals("to"))) {
				addRelationCounter("perception_hear");
				return true;
			}
		}
		// smell
		if (firstWord.equals("smell")) {
			addRelationCounter("perception_smell");
			return true;
		}
		// taste
		if (firstWord.equals("taste")) {
			addRelationCounter("perception_taste");
			return true;
		}
		// feel
		if (cptList.size() >= 2) {
			if (firstWord.equals("feel") && tagger.isNoun(tagList.get(1))) {
				addRelationCounter("perception_feel");
				return true;
			}
		}
		return false;
	}

	private boolean isOutcome() {

		return false;
	}

	private boolean isEmotion(String str) {

		if (str.equals("angry") || str.equals("annoy") || str.equals("disgust")
				|| str.equals("frustrate") || str.equals("mad")
				|| str.equals("envy") || str.equals("ashame")
				|| str.equals("embarrass") || str.equals("guilty")
				|| str.equals("humble") || str.equals("confident")
				|| str.equals("energetic") || str.equals("focus")
				|| str.equals("confuse") || str.equals("trouble")
				|| str.equals("hurt") || str.equals("happy")
				|| str.equals("joy") || str.equals("fun") || str.equals("bore")
				|| str.equals("tire") || str.equals("nervous")
				|| str.equals("pain") || str.equals("tense")
				|| str.equals("sad") || str.equals("depress")
				|| str.equals("upset") || str.equals("jealous")
				|| str.equals("proud") || str.equals("sorry")
				|| str.equals("afraid") || str.equals("scare")
				|| str.equals("fear") || str.equals("anxiety")
				|| str.equals("frighten") || str.equals("panic")
				|| str.equals("horror") || str.equals("shock")
				|| str.equals("surprise") || str.equals("excite")) {
			return true;
		}
		return false;
	}
	
	private void addRelationCounter(String key){
		Parameter.relationCounter.put(key,(int) Parameter.relationCounter.get(key) + 1);
	}
}
