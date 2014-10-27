package vsol.controller;


import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;
/*
import java.sql.SQLException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
*/
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import vsol.dao.DAO;

import vsol.model.Action;
import vsol.model.Event;

import vsol.view.MainFrame;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
import students.logic.Group;
import students.logic.ManagementSystem;
import students.logic.Student;

import students.web.forms.MainFrameForm;
import students.web.forms.StudentFrameForm;
*/

public class Main extends HttpServlet {

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
//		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

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
			} catch(Exception e) {}
		}
		
		List<Event> events = null;
		try {
			events = dao.getEvents(currentSeason, currentDay);
		} catch (Exception e) {}

		MainFrame mainFrame = new MainFrame(currentSeason, currentDay, teamId, cashMoney, events);
		

		req.setAttribute("frame", mainFrame);
		req.getRequestDispatcher("main.jsp").forward(req, resp);
/*
		StringBuilder sb = new StringBuilder();

		sb.append("<!DOCTYPE html>");
		sb.append("<html lang=\"ru\" xml:lang=\"ru\">");
		sb.append("<head>");
		sb.append("<meta charset=\"utf-8\" />");
		sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />");
		sb.append("<title>Планировщик</title>");
		sb.append("<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">");
		sb.append("<script type='text/javascript'>");
		sb.append("function numeric_format(val, thSep, dcSep) {");
		sb.append("if (!thSep) thSep = ' ';");
		sb.append("if (!dcSep) dcSep = ',';");
		sb.append("var res = val.toString();");
		sb.append("var lZero = (val < 0);");
		sb.append("var fLen = res.lastIndexOf('.');");
		sb.append("fLen = (fLen > -1) ? fLen : res.length;");
		sb.append("var tmpRes = res.substring(fLen);");
		sb.append("var cnt = -1;");
		sb.append("for (var ind = fLen; ind > 0; ind--) {");
		sb.append("cnt++;");
		sb.append("if (((cnt % 3) === 0) && (ind !== fLen) && (!lZero || (ind > 1))) {");
		sb.append("tmpRes = thSep + tmpRes;");
		sb.append("}");
		sb.append("tmpRes = res.charAt(ind - 1) + tmpRes;");
		sb.append("}");
		sb.append("return tmpRes.replace('.', dcSep);");
		sb.append("}");
		sb.append("</script>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<script src=\"http://code.jquery.com/jquery-latest.js\"></script>");
		sb.append("<script src=\"js/bootstrap.min.js\"></script>");
		sb.append("<div class=\"container\">");
*/
/*
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
*/		
/*
		sb.append("<h1>Планировщик Финансов</h1><h2>");
		sb.append("команда №");
		sb.append(teamId);
		sb.append(", $ ");
		sb.append("<script type='text/javascript'>document.write(numeric_format(");
		sb.append(cashMoney);
		sb.append("));</script>");
		sb.append("</h2><h3>");
		sb.append("сезон ");
		sb.append(currentSeason);
		sb.append(", день ");
		sb.append(currentDay);
		sb.append("</h3>");
		
		sb.append("<table class=\"table table-condensed table-bordered\"><thead><tr>");
		sb.append("<th>сезон</th>");
		sb.append("<th>день</th><th>генерация</th>");
		sb.append("<th>событие</th><th>действие</th><th>прибыль, $</th><th>итог, $</th>");
		sb.append("<th>удаление</th>");
		sb.append("</tr></thead><tbody>");

		sb.append("<tr><td></td></tr>");
		sb.append("<form action=\"/vsol/main\" method=\"POST\">");
		sb.append("<tr align=\"center\">");
		sb.append("<td>");
		sb.append("<input class=\"input-mini\" name=\"newSeason\" type=\"text\" size=\"2\" maxlength=\"2\" value=\"" + currentSeason + "\">");
		sb.append("</td>");
		sb.append("<td>");
		sb.append("<input class=\"input-mini\" name=\"newDay\" type=\"text\" size=\"3\" maxlength=\"3\" value=\"" + currentDay + "\">");
		sb.append("</td><td>");
		sb.append("<input class=\"input-mini\" name=\"newIsDuringGeneration\" type=\"checkbox\">");
		sb.append("</td><td>");
		sb.append("<input class=\"input-mini\" name=\"newDescription\" type=\"text\" value=\"описание события\">");
		sb.append("</td><td colspan=4 align=\"center\">");
		sb.append("<input class=\"input-mini\" name=\"addEvent\" type=\"submit\" value=\"добавить новое событие\">");
		sb.append("</td></tr>");
		sb.append("</form>");
*/
/*
		List<Event> events = null;
		try {
			events = dao.getEvents();
			events = dao.getEvents(currentSeason, currentDay);
		} catch (Exception e) {}
*/		
/*		
		for(Event event : events) {
			sb.append("<tr><td></td></tr>");
			sb.append("<form action=\"/vsol/main\" method=\"POST\">");
			sb.append("<input name=\"eventId\" type=\"hidden\" value=\"" + event.getId() + "\">");
			sb.append("<tr align=\"center\">");
			sb.append("<td>");
			sb.append("<input class=\"input-mini\" name=\"season\" type=\"text\" size=\"2\" maxlength=\"2\" value=\"");
			sb.append(event.getSeason());
			sb.append("\"></td>");
			sb.append("<td>");
			sb.append("<input class=\"input-mini\" name=\"day\" type=\"text\" size=\"3\" maxlength=\"3\" value=\"");
			sb.append(event.getDay());
			sb.append("\"></td><td>");
			sb.append("<input class=\"input-mini\" name=\"isDuringGeneration\" type=\"checkbox\"");
			if(event.isDuringGeneration()) {
				sb.append(" checked");
			}
			sb.append("></td><td>");
			sb.append("<input class=\"input-mini\" name=\"description\" type=\"text\" value=\"");
			sb.append(event.getDescription());
			sb.append("\"></td>");
			sb.append("<td colspan=3 align=\"center\">");
			sb.append("<input class=\"input-mini\" name=\"updateEvent\" type=\"submit\" value=\"обновить событие\">");
			sb.append("</td><td>");
			sb.append("<input class=\"input-mini\" name=\"deleteEvent\" type=\"submit\" value=\"удалить событие\">");
			sb.append("</td>");
			sb.append("</tr>");
			sb.append("</form>");
			
			for(Action action : event.getActions()) {
				sb.append("<form action=\"/vsol/main\" method=\"POST\">");
				sb.append("<input name=\"actionId\" type=\"hidden\" value=\"" + action.getId() + "\">");
				sb.append("<tr align=\"center\"><td colspan=3 align=\"center\">");
				sb.append("<input class=\"input-mini\" name=\"updateAction\" type=\"submit\" value=\"обновить действие\">");
				sb.append("</td><td>");
				sb.append("<input class=\"input-mini\" name=\"description\" type=\"text\" value=\"");
				sb.append(action.getDescription());
				sb.append("\"></td><td>");
				sb.append("<input class=\"input-mini\" name=\"profit\" type=\"text\" size=\"10\" maxlength=\"10\" value=\"");
				sb.append(action.getProfit());
				sb.append("\"></td><td>");
				cashMoney += action.getProfit();
				sb.append("<script type='text/javascript'>document.write(numeric_format(");
				sb.append(cashMoney);
				sb.append("));</script>");
				sb.append("</td><td>");
				sb.append("<input class=\"input-mini\"name=\"deleteAction\" type=\"submit\" value=\"удалить действие\">");
				sb.append("</td></tr>");
				sb.append("</form>");
			}

			sb.append("<form action=\"/vsol/main\" method=\"POST\">");
			sb.append("<input name=\"eventId\" type=\"hidden\" value=\"" + event.getId() + "\">");
			sb.append("<tr align=\"center\"><td colspan=3 align=\"center\">");
			sb.append("<input class=\"input-mini\" name=\"addAction\" type=\"submit\" value=\"добавить действие\">");
			sb.append("</td><td>");
			sb.append("<input class=\"input-mini\" name=\"newActionDescription\" type=\"text\" value=\"описание действия\">");
			sb.append("</td><td>");
			sb.append("<input class=\"input-mini\" name=\"newActionProfit\" type=\"text\" size=\"10\" maxlength=\"10\" value=\"0\">");
			sb.append("</td></tr>");
			sb.append("</form>");
		}

		sb.append("<tr><td></td></tr>");
		sb.append("<form action=\"/vsol/main\" method=\"POST\">");
		sb.append("<tr align=\"center\">");
		sb.append("<td>");
		sb.append("<input class=\"input-mini\" name=\"newSeason\" type=\"text\" size=\"2\" maxlength=\"2\" value=\"" + currentSeason + "\">");
		sb.append("</td>");
		sb.append("<td>");
		sb.append("<input class=\"input-mini\" name=\"newDay\" type=\"text\" size=\"3\" maxlength=\"3\" value=\"" + currentDay + "\">");
		sb.append("</td><td>");
		sb.append("<input class=\"input-mini\" name=\"newIsDuringGeneration\" type=\"checkbox\">");
		sb.append("</td><td>");
		sb.append("<input class=\"input-mini\" name=\"newDescription\" type=\"text\" value=\"описание события\">");
		sb.append("</td><td colspan=4 align=\"center\">");
		sb.append("<input class=\"input-mini\" name=\"addEvent\" type=\"submit\" value=\"добавить новое событие\">");
		sb.append("</td></tr>");
		sb.append("</form>");

		sb.append("</tbody></table>");

		sb.append("</div>");
		sb.append("</body>");
		sb.append("</html>");
		resp.getWriter().println(sb);
*/		
	}
	
	
	public String getHttpContent(String urlPath, String... parameters) {
		int connectionTries = 8;
		StringBuilder sb = new StringBuilder();
		for(int currentTry = 0; currentTry < connectionTries; currentTry++) {
			try {
				URL url = new URL(urlPath);
				URLConnection uc = url.openConnection();
				addParameters(uc, parameters);
				InputStream is = uc.getInputStream(); 
				InputStreamReader isr = new InputStreamReader(is, ENCODING);
				BufferedReader br = new BufferedReader(isr);
				String line;
				sb = new StringBuilder();
				while ((line = br.readLine()) != null) {
					sb.append(line);
					sb.append("\n");
				}
				br.close();
				break;
			} catch(IOException e) {
				System.out.println(e);
			}
			
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