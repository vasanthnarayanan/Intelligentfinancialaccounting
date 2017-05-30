package com.curious365.ifa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IntelligentFinancialAccountingApplication.class)
public abstract class IntelligentFinancialAccountingApplicationTests {
	protected Log log = LogFactory.getLog(this.getClass());
	
}
