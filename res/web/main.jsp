<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<meta charset="utf-8" />
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<title>Планировщик Финансов</title>
	</head>
	<body>
		<h1>Планировщик Финансов</h1>
		<h2>Команда №<c:out value="${frame.teamId}"/>, $<c:out value="${frame.cashMoney}"/></h2>
		<h3>Сезон <c:out value="${frame.currentSeason}"/>, день <c:out value="${frame.currentDay}"/></h3>
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
				<form action="/vsol/main.do" method="POST" >
					<tr>
						<td><input name="newSeason" type="text" size="2" maxlength="2" value="<c:out value="${frame.currentSeason}" />" /></td>
						<td><input name="newDay" type="text" size="3" maxlength="3" value="<c:out value="${frame.currentDay}" />" /></td>
						<td><input name="newIsDuringGeneration" type="checkbox" /></td>
						<td><input name="newDescription" type="text" value="описание события" /></td>
						<td colspan="4"><input name="addEvent" type="submit" value="добавить новое событие" /></td>
					</tr>
				</form>
				<c:forEach var="event" items="${frame.events}">
					<form action="/vsol/main.do" method="POST" >
						<input name="eventId" type="hidden" value="<c:out value="${event.id}" />" />
						<tr>
							<td><input name="season" type="text" size="2" maxlength="2" value="<c:out value="${event.season}" />" /></td>
							<td><input name="day" type="text" size="3" maxlength="3" value="<c:out value="${event.day}" />" /></td>
							<td><input name="isDuringGeneration" type="checkbox" <c:if test="${event.duringGeneration}">checked</c:if> /></td>
							<td><input name="description" type="text" value="<c:out value="${event.description}" />" /></td>
							<td colspan="3"><input name="updateEvent" type="submit" value="обновить событие" /></td>
							<td><input name="deleteEvent" type="submit" value="удалить событие" /></td>
						</tr>
					</form>
					<c:forEach var="action" items="${event.managerActions}">
						<form action="/vsol/main.do" method="POST" >
							<input name="actionId" type="hidden" value="<c:out value="${action.id}" />" />
							<tr>
								<td colspan="4"><input name="updateAction" type="submit" value="обновить действие" /></td>
								<td><input name="description" type="text" value="<c:out value="${action.description}" />" /></td>
								<td><input name="profit" type="text" size="10" maxlength="10" value="<c:out value="${action.profit}" />" /></td>
								<c:set target="${frame}" property="cashMoney" value="${frame.cashMoney + action.profit}" />
								<td><c:out value="${frame.cashMoney}" /></td>
								<td><input name="deleteAction" type="submit" value="удалить действие" /></td>
							</tr>
						</form>
					</c:forEach>
					<form action="/vsol/main.do" method="POST" >
						<input name="eventId" type="hidden" value="<c:out value="${event.id}" />" />
						<tr>
							<td colspan="4"><input name="addAction" type="submit" value="добавить действие" /></td>
							<td><input name="newActionDescription" type="text" value="описание действия" /></td>
							<td><input name="newActionProfit" type="text" size="10" maxlength="10" value="0" /></td>
						</tr>
					</form>
				</c:forEach>
				<form action="/vsol/main.do" method="POST" >
					<tr>
						<td><input name="newSeason" type="text" size="2" maxlength="2" value="<c:out value="${frame.currentSeason}" />" /></td>
						<td><input name="newDay" type="text" size="3" maxlength="3" value="<c:out value="${frame.currentDay}" />" /></td>
						<td><input name="newIsDuringGeneration" type="checkbox" /></td>
						<td><input name="newDescription" type="text" value="описание события" /></td>
						<td colspan="4"><input name="addEvent" type="submit" value="добавить новое событие" /></td>
					</tr>
				</form>
			</tbody>
		</table>
	</body>
</html>