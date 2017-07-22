package others;

public class StoryRecord {
	private String state;
	private String relation;
	private String concept;
	private double totValue;
	private double score;
	private int times;

	public StoryRecord(String state, String relation, String concept,
			double totValue, int times, double score) {
		// TODO Auto-generated constructor stub
		this.score = score;
		this.state = state;
		this.relation = relation;
		this.concept = concept;
		this.totValue = totValue;
		this.times = times;
	}

	public String getState() {
		return this.state;
	}

	public String getRelation() {
		return this.relation;
	}
	public void setRelation(String rel) {
		this.relation = rel;
	}

	public String getConcept() {
		return this.concept;
	}

	public double getTotValue() {
		return this.totValue;
	}

	public double getScore() {
		return this.score;
	}

	public int getTimes() {
		return this.times;
	}

}
