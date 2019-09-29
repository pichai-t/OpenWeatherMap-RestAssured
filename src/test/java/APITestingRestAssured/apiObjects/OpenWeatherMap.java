package APITestingRestAssured.apiObjects;

import io.restassured.http.ContentType;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OpenWeatherMap implements IApiFields {

    // 'currentResponse', 'lastStationID', and 'APIKEY' can be referred to within Child Class
    protected static ResponseOptions<Response> currentResponse = null;
    protected static String latestStationID = "";

    // APIKey - final/fixed value
    protected static final String APIKEY = "76a78216af765a6392fcf44d4712c363";

    // Private parameters to use within this class
    private static final String BASE_STATION_URI = "http://api.openweathermap.org/data/3.0/stations";
    private static RequestSpecification request = null;

    // Constructor
    public OpenWeatherMap() {
        request = given().contentType(ContentType.JSON);
    }

    // --------------------------------
    // Post to register a station
    // --------------------------------
    public static ResponseOptions<Response> postStation(String apiKey, HashMap<String, Object> json ) {
        ResponseOptions<Response> responseOptionReturn = null;
        try {
            responseOptionReturn = request.with().body(json).
                                   when().post(BASE_STATION_URI + "?APPID=" + apiKey);
        } catch (NumberFormatException ex) {
            System.out.println("EXCEPTION!: Number Format Exception: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("EXCEPTION!: " + ex.getMessage());
        }
        // Extract Response into an Object "ResponseOptions<Response> response "
        return responseOptionReturn;
    }

    // --------------------------------
    // Delete a station
    // --------------------------------
    public static ResponseOptions<Response> deleteAStation(String apiKey, String stationID ) {
        ResponseOptions<Response> responseOptionReturn = null;
        try {
            responseOptionReturn = request.with().delete(BASE_STATION_URI+ "/" + stationID + "?APPID=" + apiKey);
        } catch (Exception ex) {
            System.out.println("EXCEPTION!: " + ex.getMessage());
        }
        // Extract Response into an Object "ResponseOptions<Response> response"
        return responseOptionReturn;
    }

    // --------------------------------
    // Delete ALL stations
    // --------------------------------
    public static int deleteAllStations(String apiKey ) {
        ResponseOptions<Response> responseOptionReturn = null;
        List<String> arrayOfIDs;  // Array to keep all IDs.
        try {
            // GET All IDs of stations associated with the APIKeyID
            responseOptionReturn = request.with().get(BASE_STATION_URI + "?APPID=" + apiKey);
            arrayOfIDs = responseOptionReturn.getBody().jsonPath().get(fields.ID.toString());
            // Looping to eelete all IDs
            for (String id:  arrayOfIDs) {
                responseOptionReturn = request.with().delete(BASE_STATION_URI+ "/" + id + "?APPID=" + apiKey);
            }
        } catch (Exception ex) {
            System.out.println("EXCEPTION!: " + ex.getMessage());
        }
        return ((Response) responseOptionReturn).then().extract().statusCode();
    }

    // --------------------------------
    // Get station information/values by key
    // --------------------------------
    public String getValueByKey(ResponseOptions<Response> resp, String key) {
        String returnString = "";
        try {
            returnString = resp.getBody().jsonPath().get(key).toString();
        } catch (JsonPathException jsonEx) {
            System.out.println("EXCEPTION: JSON PATH NOT FOUND");
            returnString = "NA";
        }
        return returnString;
    }
    // Get the Status Code
    public String getStatusCode(ResponseOptions<Response> resp) {
        return String.valueOf(((RestAssuredResponseImpl) resp).then().extract().statusCode());
    }

    // --------------------------------
    // Put
    // --------------------------------

    //  T B D...


    // ===================================
    //  P R O T E C T E D - M E T H O D S
    // ===================================
    protected HashMap<String, Object> createRequestJsonFromList (List<String> list) {
        HashMap<String, Object> requestJson = new HashMap<>();
        requestJson.put(fields.EXTERNAL_ID.toString(), list.get(0));
        requestJson.put(fields.NAME.toString(), list.get(1));
        requestJson.put(fields.LATITUDE.toString(), Float.parseFloat(list.get(2)));
        requestJson.put(fields.LONGITUDE.toString(), Float.parseFloat(list.get(3)));
        requestJson.put(fields.ALTITUDE.toString(), Float.parseFloat(list.get(4)));
        return requestJson;
    }
    // -------------------------------------------------------------------------------------
    // createRequestJson2DList() can be used later
    // to support multiple rows of data - anyway it's most likely to be just one row of data
    // -------------------------------------------------------------------------------------
    protected HashMap<String, Object> createRequestJson2DList(List<Map<String, String>> list) {
        HashMap<String, Object> requestJson = new HashMap<>();
        try {
            for(int i=0; i<list.size(); i++) {
                requestJson.put(fields.EXTERNAL_ID.toString(), list.get(i).get("external_id"));
                requestJson.put(fields.NAME.toString(), list.get(i).get("name"));
                requestJson.put(fields.LATITUDE.toString(), Float.parseFloat(list.get(i).get("latitude")));
                requestJson.put(fields.LONGITUDE.toString(), Float.parseFloat(list.get(i).get("longitude")));
                requestJson.put(fields.ALTITUDE.toString(), Float.parseFloat(list.get(i).get("altitude")));
            }
        }
        catch (NumberFormatException ex) {
            System.out.println("EXCEPTION!: Number Format Exception: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("EXCEPTION!: " + ex.getMessage());
        }
        return requestJson;
    }

}
