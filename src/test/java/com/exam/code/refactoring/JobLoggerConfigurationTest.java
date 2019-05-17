package com.exam.code.refactoring;

import org.junit.Test;

import com.exam.code.refactoring.ConstantEnum.JobLogLevel;

public class JobLoggerConfigurationTest {

	@Test(expected = IllegalArgumentException.class)
	public void invalidInitialize() throws Exception {
		
		JobLogger logger = JobLogger.getInstance(false, false, false, null);
		logger.LogMessage("Test configuration",  JobLogLevel.MESSAGE);
	}
}
