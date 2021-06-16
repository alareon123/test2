package app.test;

import app.test.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestConfig {
    @Before
    public void before(Scenario scenario) {
        log.info("------------------------------");
        log.info("Starting - " + scenario.getId() + " " + scenario.getName());
        log.info("------------------------------");
    }

    @After
    public void after(Scenario scenario) {
        log.info("------------------------------");
        log.info(scenario.getId() + " " + scenario.getName() + " Status - " + scenario.getStatus());
        log.info("------------------------------");
        DriverManager.tearDown();
    }
}
