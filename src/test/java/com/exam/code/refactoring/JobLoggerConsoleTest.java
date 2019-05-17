package com.exam.code.refactoring;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.exam.code.refactoring.ConstantEnum.JobLogLevel;

public class JobLoggerConsoleTest {

  
    private final PrintStream systemOut = System.out;

    private ByteArrayOutputStream testOut;
   
    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

	@After
    public void restoreSystemInputOutput() {
        System.setOut(systemOut);
    }
    
	@Test
	public void logToConsoleOk() throws Exception {
	
		StringBuilder mesageExpected = new StringBuilder();
		mesageExpected.append(JobLogLevel.MESSAGE + " ");
		mesageExpected.append(DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + " ");
		mesageExpected.append("Testing log console");
		
		//Execute
		JobLogger logger = JobLogger.getInstance(false, true, false, null);
		logger.LogMessage("Testing log console", JobLogLevel.MESSAGE);
		
		assertEquals(mesageExpected, logger.toString());
			
	}
	
	

}
