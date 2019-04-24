package com.gaksvytech.fieldservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gaksvytech.fieldservice.scheduler.EventSchedulerEngine;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FieldserviceApiApplicationTests {
	
	@Autowired
	public EventSchedulerEngine eventSchedulerEngine;
	
	@Test
	public void runSchedule() throws Exception {
		eventSchedulerEngine.process();
	}

}
