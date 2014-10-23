package vsol.control;


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


import vsol.model.Action;
import vsol.model.Event;
import vsol.model.dao.Manager;

/*
import students.logic.Group;
import students.logic.ManagementSystem;
import students.logic.Student;

import students.web.forms.MainFrameForm;
import students.web.forms.StudentFrameForm;
*/

public class Main extends HttpServlet {

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
		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Initial Test: ant auto-deploying</title>");
		out.println("</head>");
		out.println("<body>");
//		out.println("<h1>Initial Test: ant auto-deploying</h1>");
		int money = 16578962;
		out.println("<h1>cash money: " + money + "</h1>");
		Manager dao = Manager.getInstance();
		List<Event> events = dao.getEvents();
		int eventsNumber = events.size();
//		out.println("<h2>number of events: " + eventsNumber + "</h2>");
		if (eventsNumber > 0) {
			out.println("<table border=1><tr>" 
					+ "<th>season</th><th>day</th><th>during generation</th><th>event</th>" 
					+ "<th>action</th><th>money</th><th>profit</th><th>result</th></tr>");
			for(Event event : events) {
				out.println("<tr>");
				out.println("<td>" + event.getSeason() + "</td>");
				out.println("<td>" + event.getDay() + "</td>");
				out.println("<td>" + event.isDuringGeneration() + "</td>");
				out.println("<td>" + event.getDescription() + "</td>");
				out.println("<td></td><td></td><td></td><td></td></tr>");
				for(Action action : event.getActions()) {
					out.println("<tr><td></td><td></td><td></td><td></td>");
					out.println("<td>" + action.getDescription() + "</td>");
					out.println("<td>" + money + "</td>");
					out.println("<td>" + action.getProfit() + "</td>");
					money += action.getProfit();
					out.println("<td>" + money + "</td></tr>");
				}
			}
			out.println("</table>");
		}
		out.println("</body>");
		out.println("</html>");
/*
		ManagementSystem ms = ManagementSystem.getInstance();
		int answer = 0;
		try {
			answer = checkAction(req);
		} catch(SQLException e) {
			throw new IOException(e.getMessage());
		}


		if (answer == 1) {
			try {
				Student student = new Student();
				student.setId(0);
				student.setBirthDay(new Date());
				int year = Calendar.getInstance().get(Calendar.YEAR);
				student.setEducationYear(year);
				List<Group> groups = ms.getGroups();
				StudentFrameForm studentForm = new StudentFrameForm(
						student, groups);
				req.setAttribute("student", studentForm);
				getServletContext().getRequestDispatcher(
						"/StudentFrame.jsp").forward(req, resp);
				return;
			} catch(SQLException e) {
				throw new IOException(e.getMessage());
			}
		}

		if (answer == 2) {
			try {
				if (req.getParameter("studentId") != null) {
					int id = Integer.parseInt(req.getParameter("studentId"));
					Student student = ms.getStudent(id);
					List<Group> groups = ms.getGroups();
					StudentFrameForm studentForm = new StudentFrameForm(
							student, groups);
					req.setAttribute("student", studentForm);
					getServletContext().getRequestDispatcher(
							"/StudentFrame.jsp").forward(req, resp);
					return;
				}
			} catch(SQLException e) {
				throw new IOException(e.getMessage());
			}
		}


		String groupIdValue = req.getParameter("groupId");
		String yearValue = req.getParameter("year");
		int groupId = -1;
		if (groupIdValue != null) {
			groupId = Integer.parseInt(groupIdValue);
		}
		int year = Calendar.getInstance().get(Calendar.YEAR);
		if (yearValue != null) {
			year = Integer.parseInt(yearValue);
		}

		if (answer == 3) {
			int newGroupId = Integer.parseInt(req.getParameter("newGroupId"));
			int newYear = Integer.parseInt(req.getParameter("newYear"));
			try {
				Group group = new Group(groupId);
				Group newGroup = new Group(newGroupId);
				ms.moveStudents(group, year, newGroup, newYear);
				groupId = newGroupId;
				year = newYear;
			} catch(SQLException e) {
				throw new IOException(e.getMessage());
			}
		}


		MainFrameForm form = new MainFrameForm();
		try {
			List<Group> groups = ms.getGroups();
			List<Student> students = ms.getStudents(groupId, year);
			form.setGroupId(groupId);
			form.setYear(year);
			form.setGroups(groups);
			form.setStudents(students);
		} catch(SQLException e) {
			throw new IOException(e.getMessage());
		}
		req.setAttribute("form", form);
		getServletContext().getRequestDispatcher(
				"/MainFrame.jsp").forward(req, resp);
*/				
	}
/*
	private int checkAction(HttpServletRequest req) throws SQLException {
		if (req.getParameter("Add") != null) {
			return 1;
		} else if (req.getParameter("Edit") != null) {
			return 2;
		} else if (req.getParameter("MoveGroup") != null) {
			return 3;
		} else if (req.getParameter("Delete") != null) {
			if (req.getParameter("studentId") != null) {
				Student student = new Student();
				student.setId(Integer.parseInt(req.getParameter("studentId")));
				ManagementSystem.getInstance().removeStudent(student);
			}
			return 0;
		}
		return 0;
	}
*/
}