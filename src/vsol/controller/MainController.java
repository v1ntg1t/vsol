package vsol.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.net.URL;
import java.net.URLConnection;

import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import vsol.dao.DAO;

import vsol.model.Action;
import vsol.model.Event;

import vsol.view.MainFrame;


public class MainController extends HttpServlet {

	public static final String ENCODING = "UTF-8";


	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}


	public void processRequest(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		req.setCharacterEncoding(ENCODING);
		resp.setCharacterEncoding(ENCODING);

		DAO dao = null;
		try {
			dao = DAO.getInstance();
		} catch (Exception e) {}

		int teamId = 23354;
		int cashMoney = 0;
		String url = "http://virtualsoccer.ru/roster.php?num=" + teamId;
		String content = getHttpContent(url);
		Pattern pattern = Pattern.compile("Финансы: <b>([^$]+)");
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()) {
			String parsedCashMoney = matcher.group(1).trim();
			cashMoney = Integer.parseInt(parsedCashMoney.replaceAll(" ", ""));
		}

		byte currentSeason = 1;
		short currentDay = 1;
		url = "http://virtualsoccer.ru";
		content = getHttpContent(url);
		pattern = Pattern.compile("Сезон: <b>([1-9][0-9]+)</b>, День: <b>([1-9][0-9]*)</b>");
		matcher = pattern.matcher(content);
		if(matcher.find()) {
			currentSeason = Byte.parseByte(matcher.group(1).trim());
			currentDay = Short.parseShort(matcher.group(2).trim());
			try {
				dao.deleteOldEvents(currentSeason, currentDay);
			} catch (Exception e) {}
		}
		
		try {
			if (req.getParameter("addEvent") != null) {
				byte newSeason = Byte.parseByte(req.getParameter("newSeason"));
				short newDay = Short.parseShort(req.getParameter("newDay"));
				boolean newIsDuringGeneration = (req.getParameter("newIsDuringGeneration") != null ? true : false);
				String newDescription = req.getParameter("newDescription");
				Event newEvent = new Event(newSeason, newDay, newIsDuringGeneration, newDescription);
				dao.addEvent(newEvent);
			} else if (req.getParameter("updateEvent") != null) {
				long id = Long.parseLong(req.getParameter("eventId"));
				byte newSeason = Byte.parseByte(req.getParameter("season"));
				short newDay = Short.parseShort(req.getParameter("day"));
				boolean newIsDuringGeneration = (req.getParameter("isDuringGeneration") != null ? true : false);
				String newDescription = req.getParameter("description");
				Event newEvent = new Event(id, newSeason, newDay, newIsDuringGeneration, newDescription);
				dao.updateEvent(newEvent);
			} else if (req.getParameter("deleteEvent") != null) {
				long id = Long.parseLong(req.getParameter("eventId"));
				Event event = dao.getEvent(id);
				dao.deleteEvent(event);
			} else if (req.getParameter("addAction") != null) {
				String newActionDescription = req.getParameter("newActionDescription");
				int newActionProfit = Integer.parseInt(req.getParameter("newActionProfit"));
				long eventId = Long.parseLong(req.getParameter("eventId"));
				Action newAction = new Action(newActionDescription, newActionProfit);
				dao.addAction(newAction, eventId);
			} else if (req.getParameter("updateAction") != null) {
				long id = Long.parseLong(req.getParameter("actionId"));
				String description = req.getParameter("description");
				int profit = Integer.parseInt(req.getParameter("profit"));
				Action action = new Action(id, description, profit);
				dao.updateAction(action);
			} else if (req.getParameter("deleteAction") != null) {
				long actionId = Long.parseLong(req.getParameter("actionId"));
				dao.deleteAction(actionId);
			}
		} catch (Exception e) {}

		List<Event> events = null;
		try {
			events = dao.getEvents(currentSeason, currentDay);
		} catch (Exception e) {}

		MainFrame mainFrame = new MainFrame(currentSeason, currentDay, teamId, cashMoney, events);
		req.setAttribute("frame", mainFrame);
		req.getRequestDispatcher("main.jsp").forward(req, resp);
	}
	

	private String getHttpContent(String urlPath, String... parameters) {
		int connectionTries = 8;
		StringBuilder sb = new StringBuilder();
		for (int currentTry = 0; currentTry < connectionTries; currentTry++) {
			try {
				URL url = new URL(urlPath);
				URLConnection uc = url.openConnection();
				addParameters(uc, parameters);
				InputStream is = uc.getInputStream(); 
				InputStreamReader isr = new InputStreamReader(is, ENCODING);
				BufferedReader br = new BufferedReader(isr);
				String line;
				sb = new StringBuilder();
				while ( (line = br.readLine()) != null ) {
					sb.append(line);
					sb.append("\n");
				}
				br.close();
				break;
			} catch(IOException e) {}
		}
		return sb.toString();
	}

	private void addParameters(URLConnection uc, String... parameters) 
			throws IOException {
		uc.setDoOutput(true);
		OutputStream os = uc.getOutputStream();
		OutputStreamWriter out = new OutputStreamWriter(os, ENCODING);
		for(String parameter : parameters) {
	        out.write(parameter);
		}
        out.close();
	}

}