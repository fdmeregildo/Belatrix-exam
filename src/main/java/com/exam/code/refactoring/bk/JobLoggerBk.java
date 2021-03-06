package com.exam.code.refactoring.bk;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.exam.code.refactoring.ConnectionBd;
import com.exam.code.refactoring.ConstantEnum.JobLogLevel;

public class JobLoggerBk {

	private static Logger logger = Logger.getLogger("MyLog");
	private static boolean logToFile;
	private static boolean logToConsole;
	private static boolean logToDatabase;
	private static Map<String, String> parameters;

	public JobLoggerBk(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
			Map<String, String> parametersMap) throws Exception {

		if ((!logToFileParam && !logToConsoleParam && !logToDatabaseParam)
				|| (logToFileParam || logToDatabaseParam) && parametersMap == null) {
			throw new IllegalArgumentException("Invalid configuration");
		}

		logToFile = logToFileParam;
		logToConsole = logToConsoleParam;
		logToDatabase = logToDatabaseParam;
		parameters = parametersMap;
		
	}

	public void LogMessage(String messageText, JobLogLevel level) throws Exception {

		validateParameters(messageText, level);

		StringBuilder mesageLog = new StringBuilder();
		mesageLog.append(level.name() + " ");
		mesageLog.append(DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + " ");
		mesageLog.append(messageText);

		if (logToFile) {
			logToFile(mesageLog.toString());
		}

		if (logToConsole) {
			logToConsole(mesageLog.toString());
		}

		if (logToDatabase) {
			logToDatabase(messageText, level);
		}
	}

	private void logToConsole(String messageText) {

		ConsoleHandler ch = new ConsoleHandler();
		logger.addHandler(ch);
		logger.log(Level.INFO, messageText);
		ch.close();
	}

	private void logToFile(String messageText) throws Exception {

		if (parameters.get("logFileFolder") == null || parameters.get("logFileFolder").trim().isEmpty()) {
			throw new IllegalArgumentException("logFileFolder must be specified");
		}

		FileHandler fh = new FileHandler(parameters.get("logFileFolder"));
		logger.addHandler(fh);
		logger.log(Level.INFO, messageText);
		fh.close();
	}

	private void logToDatabase(String messageText, JobLogLevel level) throws SQLException {

		if (parameters.get("userName") == null || parameters.get("password") == null || parameters.get("dbms") == null
				|| parameters.get("serverName") == null || parameters.get("portNumber") == null) {
			throw new IllegalArgumentException("Database parameters must be specified");
		}

		Connection connection = ConnectionBd.getConnection(parameters);
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("insert into Log_Values('" + messageText + "', " + level.toString() + ")");
		connection.close();
	}

	private void validateParameters(String messageText, JobLogLevel level) throws IllegalArgumentException {

		if (messageText == null || messageText.trim().isEmpty()) {
			throw new IllegalArgumentException("Error or Warning or Message must be specified");
		} else if (level == null) {
			throw new IllegalArgumentException("Level must be specified");
		}
	}

}
