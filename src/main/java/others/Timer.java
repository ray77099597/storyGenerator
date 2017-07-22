package others;

public class Timer {
	private long hour;
	private long min;
	private long sec;
	private long startTime;
	private long totalTime;

	public Timer() {
		// TODO Auto-generated constructor stub
		this.startTime = System.currentTimeMillis();
		this.totalTime = 0;
	}

	public void resetTimer() {
		this.startTime = System.currentTimeMillis();
		this.totalTime = 0;
	}

	public void stopTimer() {
		this.totalTime = System.currentTimeMillis() - this.startTime;
		this.calcuTime();
	}

	public void accumTime(long t) {
		this.totalTime += t;
	}

	public void calcuTime() {
		this.hour = this.totalTime / 3600000;
		this.min = (this.totalTime % 3600000) / 60000;
		this.sec = (this.totalTime % 60000) / 1000;
	}

	public long getTotalTime() {
		return this.totalTime;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return totalTime + " | " + hour + " hour, " + min + " min, " + sec
				+ " sec";
	}

}
