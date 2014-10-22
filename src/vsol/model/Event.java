package vsol.model;


import java.util.List;


public class Event {

	private long id;
	private byte season;
	private short day;
	private boolean isDuringGeneration;
	private String description;
	private List<Action> actions;
	
	
	public Event() {}
	
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public byte getSeason() {
		return season;
	}
	
	public void setSeason(byte season) {
		this.season = season;
	}
	
	public short getDay() {
		return day;
	}
	
	public void setDay(short day) {
		this.day = day;
	}
	
	public boolean isDuringGeneration() {
		return isDuringGeneration;
	}
	
	public void setDuringGeneration(boolean isDuringGeneration) {
		this.isDuringGeneration = isDuringGeneration;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Action> getActions() {
		return actions;
	}
	
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

}