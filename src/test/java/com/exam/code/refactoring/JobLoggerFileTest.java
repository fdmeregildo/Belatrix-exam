package com.exam.code.refactoring;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.exam.code.refactoring.ConstantEnum.JobLogLevel;

public class JobLoggerFileTest {

	@Test
	public void testLogToFile() throws Exception {
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("logFileFolder", "D://logFile.txt");
		
		JobLogger logger = JobLogger.getInstance(true, false, false, params);
		logger.LogMessage("Log to file ", JobLogLevel.MESSAGE);
		File logFileBefore = new File("D://logFile.log");
		
		logger.LogMessage("Log to file 2 ", JobLogLevel.ERROR);
		File logFileActual = new File("D://logFile.log");
		
		assertTrue("files are differ!", FileUtils.contentEquals(logFileBefore, logFileActual));
		
	}
}
