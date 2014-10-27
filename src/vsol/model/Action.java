package vsol.model;


public class Action {

	private long id;
	private int profit;
	private String description;
	
	
	public Action() {}
	
	public Action(String description, int profit) {
		this();
		setDescription(description);
		setProfit(profit);
	}
	
	public Action(long id, String description, int profit) {
		this(description, profit);
		setId(id);
	}
	
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public int getProfit() {
		return profit;
	}
	
	public void setProfit(int profit) {
		this.profit = profit;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}