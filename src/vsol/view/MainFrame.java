package vsol.view;


import java.util.List;

import org.apache.struts.action.ActionForm;
/*
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
*/

import vsol.model.Event;


public class MainFrame extends ActionForm {

	private byte currentSeason;
	private short currentDay;
	private int teamId;
	private int cashMoney;
	private List<Event> events;
	

	public MainFrame() {}
	
	public MainFrame(int teamId, int cashMoney) {
		this();
		setTeamId(teamId);
		setCashMoney(cashMoney);
	}
	
	public MainFrame(byte season, short day, int teamId, int cashMoney) {
		this(teamId, cashMoney);
		setCurrentSeason(season);
		setCurrentDay(day);
	}
	
	public MainFrame(byte season, short day, int teamId, int cashMoney, 
			List<Event> events) {
		this(season, day, teamId, cashMoney);
		setEvents(events);
	}
	
	
	public byte getCurrentSeason() {
		return currentSeason;
	}
	
	public short getCurrentDay() {
		return currentDay;
	}
	
	public int getTeamId() {
		return teamId;
	}
	
	public int getCashMoney() {
		return cashMoney;
	}
	
	public List<Event> getEvents() {
		return events;
	}
	
	
	public void setCurrentSeason(byte season) {
		currentSeason = season;
	}
	
	public void setCurrentDay(short day) {
		currentDay = day;
	}

	public void setTeamId(int id) {
		teamId = id;
	}
	
	public void setCashMoney(int money) {
		cashMoney = money;
	}
	
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
/*	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		currentSeason = null;
		currentDay = null;
		teamId = null;
		cashMoney = null;
		events = null;
	}
	
	public ActionErrors validate(ActionMapping map, HttpServletRequest req) {
		currentSeason = null;
		currentDay = null;
		teamId = null;
		cashMoney = null;
		events = null;

		ActionErrors ae = new ActionErrors();
		if (professionId == null || professionId.trim().isEmpty()) {
			ActionMessage am = new ActionMessage(
					"errors.lookup.symbol.required");
			ae.add("professionId", am);
		}
		return ae;
	}
*/	


	public int getEventsSize() {
		return events.size();
	}
	
	public Event getEvent(int index) {
		if (0 <= index && index < getEventsSize()) {
			return events.get(index);
		}
		return null;
	}

}