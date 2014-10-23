package vsol.model.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;


import vsol.model.Action;
import vsol.model.Event;


public class Manager {

	private static Manager instance;
	private static Connection connection;
/*
	private long id;
	private int profit;
	private String description;
*/	
	
	private Manager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/vsol";
			String user = "root";
			String password = "rootpassword";
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Manager getInstance() {
		if(instance == null) {
			instance = new Manager();
		}
		return instance;	
	}
	
	
	public List<Event> getEvents() {
		List<Event> events = new ArrayList<Event>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = connection.createStatement();
			rs = st.executeQuery("SELECT * FROM events");
			while (rs.next()) {
				Event event = new Event();
				event.setId(rs.getLong("id"));
				event.setSeason(rs.getByte("season"));
				event.setDay(rs.getShort("day"));
				event.setDuringGeneration(rs.getBoolean("isDuringGeneration"));
				event.setDescription(rs.getString("description"));
				event.setActions(getActions(event));
				events.add(event);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return events;
	}
	
	public List<Action> getActions(Event event) {
		List<Action> actions = new ArrayList<Action>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = connection.createStatement();
			rs = st.executeQuery("SELECT * FROM actions where eventId = " 
					+ event.getId());
			while (rs.next()) {
				Action action = new Action();
				action.setId(rs.getLong("id"));
				action.setProfit(rs.getInt("profit"));
				action.setDescription(rs.getString("description"));
				actions.add(action);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return actions;
	}
	
/*	
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
*/	
}