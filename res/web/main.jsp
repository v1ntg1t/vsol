<%@ page contentType = "text/html; charset=UTF-8" %>
<%@ page import = "vsol.view.MainFrame" %>
<% MainFrame frame = (MainFrame)request.getAttribute("frame"); %>
<% int cashMoney = frame.getCashMoney(); %>
<html>
	<head>
		<meta charset="utf-8" />
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<title>Планировщик Финансов</title>
	</head>
	<body>
		<h1>Планировщик Финансов</h1>
		<h2>Команда №<% out.print(frame.getTeamId()); %>, $<% out.print(cashMoney); %></h2>
		<h3>Сезон <% out.print(frame.getCurrentSeason()); %>, день <% out.print(frame.getCurrentDay()); %></h3>
		<table>
			<thead>
				<tr>
					<th>сезон</th>
					<th>день</th>
					<th>генерация</th>
					<th>событие</th>
					<th>действие</th>
					<th>прибыль</th>
					<th>итог</th>
					<th>удаление</th>
				</tr>
			</thead>
			<tbody>
				<form action="/vsol/main" method="POST" >
					<tr>
						<td><input name="newSeason" type="text" size="2" maxlength="2" value="<% out.print(frame.getCurrentSeason()); %>"/></td>
						<td><input name="newDay" type="text" size="3" maxlength="3" value="<% out.print(frame.getCurrentDay()); %>" /></td>
						<td><input name="newIsDuringGeneration" type="checkbox" /></td>
						<td><input name="newDescription" type="text" value="описание события" /></td>
						<td colspan="4"><input name="addEvent" type="submit" value="добавить новое событие" /></td>
					</tr>
				</form>
				<% for(int i = 0; i < frame.getEventsSize(); i++) { %>
					<form action="/vsol/main" method="POST" >
						<input name="eventId" type="hidden" value="<% out.print( frame.getEvent(i).getId() ); %>" />
						<tr>
							<td><input name="season" type="text" size="2" maxlength="2" value="<% out.print( frame.getEvent(i).getSeason() ); %>" /></td>
							<td><input name="day" type="text" size="3" maxlength="3" value="<% out.print( frame.getEvent(i).getDay() ); %>" /></td>
							<td><input name="isDuringGeneration" type="checkbox" <% out.print( frame.getEvent(i).isDuringGeneration() ? "checked " : "" ); %>/></td>
							<td><input name="description" type="text" value="<% out.print( frame.getEvent(i).getDescription() ); %>" /></td>
							<td colspan="3"><input name="updateEvent" type="submit" value="обновить событие" /></td>
							<td><input name="deleteEvent" type="submit" value="удалить событие" /></td>
						</tr>
					</form>
					<% for(int j = 0; j < frame.getEvent(i).getActionsSize(); j++) { %>
						<form action="/vsol/main" method="POST" >
							<input name="actionId" type="hidden" value="<% out.print( frame.getEvent(i).getAction(j).getId() ); %>" />
							<tr>
								<td colspan="4"><input name="updateAction" type="submit" value="обновить действие" /></td>
								<td><input name="description" type="text" value="<% out.print( frame.getEvent(i).getAction(j).getDescription() ); %>" /></td>
								<td><input name="profit" type="text" size="10" maxlength="10" value="<% out.print( frame.getEvent(i).getAction(j).getProfit() ); %>" /></td>
								<td><% cashMoney += frame.getEvent(i).getAction(j).getProfit(); out.print(cashMoney); %></td>
								<td><input name="deleteAction" type="submit" value="удалить действие" /></td>
							</tr>
						</form>
					<% } %>
					<form action="/vsol/main" method="POST" >
						<input name="eventId" type="hidden" value="<% out.print( frame.getEvent(i).getId() ); %>" />
						<tr>
							<td colspan="4"><input name="addAction" type="submit" value="добавить действие" /></td>
							<td><input name="newActionDescription" type="text" value="описание действия" /></td>
							<td><input name="newActionProfit" type="text" size="10" maxlength="10" value="0" /></td>
						</tr>
					</form>
				<% } %>
				<form action="/vsol/main" method="POST" >
					<tr>
						<td><input name="newSeason" type="text" size="2" maxlength="2" value="<% out.print(frame.getCurrentSeason()); %>"/></td>
						<td><input name="newDay" type="text" size="3" maxlength="3" value="<% out.print(frame.getCurrentDay()); %>" /></td>
						<td><input name="newIsDuringGeneration" type="checkbox" /></td>
						<td><input name="newDescription" type="text" value="описание события" /></td>
						<td colspan="4"><input name="addEvent" type="submit" value="добавить новое событие" /></td>
					</tr>
				</form>
			</tbody>
		</table>
	</body>
</html>