package com.exam.code.refactoring;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.exam.code.refactoring.ConstantEnum.JobLogLevel;

public class JobLoggerParameterTest {

	JobLogger logger = null;
	
	@Before
	public void beforeTest() throws Exception {
		
		logger = JobLogger.getInstance(false, true, false, new HashMap<String, String>());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidParameterMessage() throws Exception {
		
		logger.LogMessage(null, JobLogLevel.WARNING);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidParameterLevel() throws Exception {
		
		logger.LogMessage("Testing", null);
	}
	

}
