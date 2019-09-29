package APITestingRestAssured.stepDefinitions;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static java.util.Objects.isNull;
import static org.junit.Assert.assertEquals;
import java.util.*;

import APITestingRestAssured.apiObjects.OpenWeatherMap;


public class StepsOpenWhetherMap extends OpenWeatherMap {

    @When("^I have registered a new station to OpenWhetherMap with values of \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"$")
    public void iHaveRegisteredANewStationToOpenWhetherMapWithValuesOf(String external_id, String name, String longitude, String latitude, String altitude)  {
        HashMap<String, Object> requestJson = createRequestJsonFromList(Arrays.asList(external_id, name, longitude, latitude, altitude));
        currentResponse = OpenWeatherMap.postStation(APIKEY, requestJson);
        latestStationID = getValueByKey(currentResponse, "ID"); // After register a station, keep 'latestStationID'
    }

    @Then("^I have received HTTP response code of \"([^\"]*)\"$")
    public void iHaveReceivedHTTPResponseCodeOf(String expectedHTTPCode)  {
        String actualHTTPCode = isNull(currentResponse) ? "" : getStatusCode(currentResponse);
        assertEquals("ERROR: The actual Code is " + actualHTTPCode, expectedHTTPCode, actualHTTPCode);
    }

    @Then("^I should see the message body with \"([^\"]*)\" as \"([^\"]*)\"$")
    public void iShouldSeeTheMessageBodyWithAs(String strKey, String expectedString)  {
        // Guard: to make sure currentResponse not null.
        if (isNull(currentResponse)) { return; }

        String actualString = getValueByKey(currentResponse, strKey);
        assertEquals("ERROR: The actual Code is " + actualString, expectedString, actualString);
    }

    @Then("^I see my new station with values of \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"$")
    public void iSeeMyNewStationWithValuesOf(String strExternal_ID, String strName, String strLatitude, String strLongitude, String strAltitude)  {
        // Guard: to make sure currentResponse not null.
        if (isNull(currentResponse)) { return; }

        List<String> actualList = new ArrayList<>();
        actualList.add(getValueByKey(currentResponse,fields.EXTERNAL_ID.toString()));
        actualList.add(getValueByKey(currentResponse,fields.NAME.toString()));
        actualList.add(getValueByKey(currentResponse,fields.LATITUDE.toString()));
        actualList.add(getValueByKey(currentResponse,fields.LONGITUDE.toString()));
        actualList.add(getValueByKey(currentResponse,fields.ALTITUDE.toString()));

        List<String> expectedList = Arrays.asList(strExternal_ID,strName, strLatitude, strLongitude, strAltitude);
        assertEquals("ERROR: The values from new station are not matching; Actual: " + actualList.toString() ,
                     expectedList, actualList);
    }

    @When("^I have registered a new station to OpenWhetherMap \"([^\"]*)\" APIKey and following values$")
    public void iHaveRegisteredANewStationToOpenWhetherMapAPIKeyAndFollowingValues(String withOrWithOut_APIKey, DataTable dt)  {
        String apiKey = withOrWithOut_APIKey.equalsIgnoreCase("without") ? "" : APIKEY;
        // This can support multiple rows of data - but it's most likely to be just one row (not counting header row)
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        HashMap<String, Object> requestJson = new HashMap<>();
        try {
            for(int i=0; i<list.size(); i++) {
                requestJson.put("external_id", list.get(i).get(fields.EXTERNAL_ID.toString()));
                requestJson.put("name", list.get(i).get(fields.NAME.toString()));
                requestJson.put("latitude", Float.parseFloat(list.get(i).get(fields.LATITUDE.toString())));
                requestJson.put("longitude", Float.parseFloat(list.get(i).get(fields.LONGITUDE.toString())));
                requestJson.put("altitude", Float.parseFloat(list.get(i).get(fields.ALTITUDE.toString())));
                currentResponse = OpenWeatherMap.postStation(apiKey, requestJson);
                // Logging.
                System.out.println(">> Response from regitering a station: " + currentResponse.getBody().print());
            }
        }
        catch (NumberFormatException ex) {
            System.out.println("EXCEPTION!: Number Format Exception: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println(">> Can't register a new station: Is it a valid APIKey? -> '" + apiKey + "'");
        }
    }

    @When("^I delete my latest station associated with my APIKey in OpenWhetherMap$")
    public void iDeleteMyLatestStationsAssociatedWithMyAPIKeyInOpenWhetherMap()  {
        // To delete 'latest Station ID'
        currentResponse = deleteAStation(APIKEY, latestStationID);
    }

    @Given("^I have cleaned all stations associated with my APIKey in OpenWhetherMap$")
    public void iHaveCleanedAllStationsAssociatedWithMyAPIKeyInOpenWhetherMap()  {
        // Delete all stations belong to this APIKey.
        int statusCode = deleteAllStations(APIKEY); // 'statusCode' can possibly be used later.
    }

}
