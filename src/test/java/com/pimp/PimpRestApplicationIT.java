package com.pimp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "db.port=37017")
public class PimpRestApplicationIT {

	@Test
	public void contextLoads() {
	}

}