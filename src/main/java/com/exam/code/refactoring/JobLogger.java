package com.exam.code.refactoring;

import java.io.File;
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

import org.apache.commons.lang3.StringUtils;

import com.exam.code.refactoring.ConstantEnum.JobLogLevel;

public class JobLogger {

	private static JobLogger joblogger = null;

	private static Logger logger = Logger.getLogger("MyLog");
	private static boolean logToFile;
	private static boolean logToConsole;
	private static boolean logToDatabase;
	private static Map<String, String> parameters;

	private JobLogger() throws Exception {}

	public static JobLogger getInstance() {
		return joblogger;
	}

	public static JobLogger getInstance(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
			Map<String, String> parametersMap) throws Exception {

		logToFile = logToFileParam;
		logToConsole = logToConsoleParam;
		logToDatabase = logToDatabaseParam;
		parameters = parametersMap;
		
		if (joblogger == null) {
			joblogger = new JobLogger();
		}
		return joblogger;
	};

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

		if (StringUtils.isBlank(parameters.get("logFileFolder"))) {
			throw new IllegalArgumentException("logFileFolder must be specified");
		}

		File logFile = new File(parameters.get("logFileFolder"));
		if (!logFile.exists()) {
			FileHandler fh = new FileHandler(parameters.get("logFileFolder"));
			logger.addHandler(fh);
		}
		
		logger.log(Level.INFO, messageText);
	}

	private void logToDatabase(String messageText, JobLogLevel level) throws SQLException {

		if (StringUtils.isBlank(parameters.get("userName")) || StringUtils.isBlank(parameters.get("password"))
				|| StringUtils.isBlank(parameters.get("dbms")) || StringUtils.isBlank(parameters.get("serverName"))
				|| StringUtils.isBlank(parameters.get("portNumber"))) {
			throw new IllegalArgumentException("Database parameters must be specified");
		}

		Connection connection = ConnectionBd.getConnection(parameters);
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("insert into Log_Values('" + messageText + "', " + level.toString() + ")");
		connection.close();
	}

	private void validateParameters(String messageText, JobLogLevel level) throws IllegalArgumentException {

		if ((!logToFile && !logToConsole && !logToDatabase) || (logToFile || logToDatabase) && parameters == null) {
			throw new IllegalArgumentException("Invalid configuration");
		} else if (StringUtils.isBlank(messageText)) {
			throw new IllegalArgumentException("Error or Warning or Message must be specified");
		} else if (level == null) {
			throw new IllegalArgumentException("Level must be specified");
		}
	}
	
	
}
