package vsol.controller;


import java.io.IOException;

import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


import vsol.Const;

import vsol.dao.DAO;

import vsol.model.ManagerAction;
import vsol.model.Event;

import vsol.util.HttpUtil;

import vsol.view.MainFrame;


public class MainController extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {

		DAO dao = null;
		try {
			dao = DAO.getInstance();
		} catch (Exception e) {}

		int teamId = 23354;
		int cashMoney = 0;
		String url = "http://virtualsoccer.ru/roster.php?num=" + teamId;
		String content = HttpUtil.getHttpContent(url);
		Pattern pattern = Pattern.compile("Финансы: <b>([^$]+)");
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()) {
			String parsedCashMoney = matcher.group(1).trim();
			cashMoney = Integer.parseInt(parsedCashMoney.replaceAll(" ", ""));
		}

		byte currentSeason = 1;
		short currentDay = 1;
		url = "http://virtualsoccer.ru";
		content = HttpUtil.getHttpContent(url);
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
				ManagerAction newManagerAction = new ManagerAction(newActionDescription, newActionProfit);
				dao.addManagerAction(newManagerAction, eventId);
			} else if (req.getParameter("updateAction") != null) {
				long id = Long.parseLong(req.getParameter("actionId"));
				String description = req.getParameter("description");
				int profit = Integer.parseInt(req.getParameter("profit"));
				ManagerAction managerAction = new ManagerAction(id, description, profit);
				dao.updateManagerAction(managerAction);
			} else if (req.getParameter("deleteAction") != null) {
				long actionId = Long.parseLong(req.getParameter("actionId"));
				dao.deleteManagerAction(actionId);
			}
		} catch (Exception e) {}

		List<Event> events = null;
		try {
			events = dao.getEvents(currentSeason, currentDay);
		} catch (Exception e) {
			throw new IOException(e);
		}


		MainFrame mainForm = (MainFrame)form;
		mainForm.setCurrentSeason(currentSeason);
		mainForm.setCurrentDay(currentDay);
		mainForm.setTeamId(teamId);
		mainForm.setCashMoney(cashMoney);
		mainForm.setEvents(events);
		return mapping.findForward("success");
/*
		MainFrame mainFrame = new MainFrame(currentSeason, currentDay, teamId, 
				cashMoney, events);
		req.setAttribute("frame", mainFrame);
		req.getRequestDispatcher("main.jsp").forward(req, resp);
*/
/*
		String profession = null;
		String target = "success";
		Long professionId = null;
		if (lookupForm != null) {
			try {
				professionId = Long.parseLong(lookupForm.getProfessionId());
				profession = getProfession(professionId);
			} catch (Exception e) {}
		}
		if (profession == null) {
			target = "failure";
			ActionErrors actionErrors = new ActionErrors();
			ActionMessage am = new ActionMessage(
					"errors.lookup.unknown", professionId);
			actionErrors.add(ActionErrors.GLOBAL_MESSAGE, am);
			saveErrors(request, actionErrors);
		} else {
			lookupForm.setProfessionName(profession);
		}
		return (mapping.findForward(target));
*/		
	}

}