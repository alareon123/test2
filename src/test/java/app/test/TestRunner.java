package app.test;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"},
        glue = {"app.test"}, tags = "not @ignored")
public class TestRunner {
}
