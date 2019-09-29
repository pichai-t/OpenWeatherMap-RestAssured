package APITestingRestAssured.stepDefinitions;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class HookStepsOpenWeatherMap {

    @Before
    public void beforeAny() {
        System.out.println(" Start Scenario: " + Scenario.class.getName());
    }

    @After
    public void afterAny() {
        System.out.println(" End of Scenario: " + Scenario.class.getName());
    }
}
