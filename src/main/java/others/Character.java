package others;

import generator.Parameter;

public class Character {
	private String name;
	private String gender;
	private String subjectPronoun;
	private String objectPronoun;
	private String possessivePronouns;
	private String reflexivePronoun;
	@SuppressWarnings("unused")
	private int[] personality;
	
	public Character(String n, String g){
		name = n;
		gender = g;
		if(gender.equals("male")){
			subjectPronoun = Parameter.subjectPronoun_male;
			objectPronoun = Parameter.objectPronoun_male;
			possessivePronouns = Parameter.possessivePronouns_male;
			reflexivePronoun = Parameter.reflexivePronoun_male;
		}else if(gender.equals("female")){
			subjectPronoun = Parameter.subjectPronoun_female;
			objectPronoun = Parameter.objectPronoun_female;
			possessivePronouns = Parameter.possessivePronouns_female;
			reflexivePronoun = Parameter.reflexivePronoun_female;
		}
	}
	
	public String getSubjectPronoun() {
		return subjectPronoun;
	}

	public String getObjectPronoun() {
		return objectPronoun;
	}

	public String getPossessivePronouns() {
		return possessivePronouns;
	}

	public String getReflexivePronoun() {
		return reflexivePronoun;
	}

	public String getName() {
		return name;
	}

	public String getGender() {
		return gender;
	}
}
