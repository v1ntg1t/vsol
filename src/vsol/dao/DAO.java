package vsol.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


import vsol.model.ManagerAction;
import vsol.model.Event;


public class DAO {

	private static final String DATA_SOURCE_PATH = "java:comp/env/jdbc/VsolDB";

	private static DAO instance;
	private static Connection connection;

	
	private DAO() throws Exception {}
	
	public static DAO getInstance() throws Exception {
		if(instance == null) {
			instance = new DAO();
			Context context = new InitialContext();
			DataSource source = (DataSource)context.lookup(DATA_SOURCE_PATH);
			connection = source.getConnection();
		}
		return instance;	
	}
	
	
	public List<Event> getEvents(byte currentSeason, short currentDay) 
			throws Exception {
		List<Event> events = new ArrayList<Event>();
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(
				"SELECT * FROM events WHERE season > " + currentSeason 
				+ " OR (season = " + currentSeason + " AND day >= " + currentDay 
				+ ") order by season, day, isDuringGeneration"
		);
		while (rs.next()) {
			Event event = new Event();
			event.setId(rs.getLong("id"));
			event.setSeason(rs.getByte("season"));
			event.setDay(rs.getShort("day"));
			event.setDuringGeneration(rs.getBoolean("isDuringGeneration"));
			event.setDescription(rs.getString("description"));
			event.setManagerActions(getManagerActions(event));
			events.add(event);
		}
		rs.close();
		st.close();
		return events;
	}

	public List<Event> getEvents() throws Exception {
		return getEvents((byte)1, (short)1);
	}
	
	public Event getEvent(long id) throws Exception {
		Event event = null;
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM events WHERE id = " + id);
		rs.next();
		event = new Event();
		event.setId(id);
		event.setSeason(rs.getByte("season"));
		event.setDay(rs.getShort("day"));
		event.setDuringGeneration(rs.getBoolean("isDuringGeneration"));
		event.setDescription(rs.getString("description"));
		event.setManagerActions(getManagerActions(event));
		rs.close();
		st.close();
		return event;
	}

	public void addEvent(Event event) throws Exception {
		Statement st = connection.createStatement();
		st.executeUpdate(
				"INSERT events(season, day, isDuringGeneration, description) "
				+ "values (" + event.getSeason() + ", " + event.getDay() + ", " 
				+ (event.getDuringGeneration() ? "1" : "0") + ", '" 
				+ event.getDescription() + "')");
		st.close();
	}
	
	public void updateEvent(Event event) throws Exception {
		Statement st = connection.createStatement();
		st.executeUpdate("UPDATE events SET season = " + event.getSeason() + 
				", day = " + event.getDay() + 
				",isDuringGeneration = " + (event.getDuringGeneration() ? 1 : 0) 
				+ ", description = '" + event.getDescription() 
				+ "' WHERE id = " + event.getId() + ";");
		st.close();
	}

	public void deleteOldEvents(byte currentSeason, short currentDay) 
			throws Exception {
		List<Event> events = getEvents();
		for(Event event : events) {
			if (	event.getSeason() < currentSeason || (
							event.getSeason() == currentSeason && 
							event.getDay() < currentDay
					)
			) {
				deleteEvent(event);
			}
		}
	}
	
	public void deleteEvent(Event event) throws Exception {
		List<ManagerAction> managerActions = event.getManagerActions();
		for (ManagerAction managerAction : managerActions) {
			deleteManagerAction(managerAction);
		}
		Statement st = connection.createStatement();
		st.executeUpdate("DELETE FROM events WHERE id = " + event.getId());
		st.close();
	}
	
	public List<ManagerAction> getManagerActions(Event event) throws Exception {
		List<ManagerAction> managerActions = new ArrayList<ManagerAction>();
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM actions where eventId = " 
				+ event.getId());
		while (rs.next()) {
			ManagerAction managerAction = new ManagerAction();
			managerAction.setId(rs.getLong("id"));
			managerAction.setProfit(rs.getInt("profit"));
			managerAction.setDescription(rs.getString("description"));
			managerActions.add(managerAction);
		}
		rs.close();
		st.close();
		return managerActions;
	}
	
	public void addManagerAction(ManagerAction managerAction, long eventId) 
			throws Exception {
		Statement st = connection.createStatement();
		st.executeUpdate("INSERT actions(description, profit, eventId) " + 
				"values ('" + managerAction.getDescription() + "', " + 
				managerAction.getProfit() + ", " + eventId + ")");
		st.close();
	}
	
	public void updateManagerAction(ManagerAction managerAction) 
			throws Exception {
		Statement st = connection.createStatement();
		st.executeUpdate("UPDATE actions SET description = '" 
				+ managerAction.getDescription() 
				+ "', profit = " + managerAction.getProfit() 
				+ " WHERE id = " + managerAction.getId()
		);
		st.close();
	}

	public void deleteManagerAction(long id) throws Exception {
		Statement st = connection.createStatement();
		st.executeUpdate("DELETE FROM actions WHERE id = " + id + ";");
		st.close();
	}
	
	public void deleteManagerAction(ManagerAction managerAction) 
			throws Exception {
		deleteManagerAction(managerAction.getId());
	}

}