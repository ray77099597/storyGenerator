package others;

import java.util.List;

public class StoryStructure {
	private double score;
	private List<StoryRecord> story;
	

	public StoryStructure(double score, List<StoryRecord> story) {
		this.score = score;
		this.story = story;
	}

	public double getScore() {
		return score;
	}

	public List<StoryRecord> getStory() {
		return story;
	}
}
