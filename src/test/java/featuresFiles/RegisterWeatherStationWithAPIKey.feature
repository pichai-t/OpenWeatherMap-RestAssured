Feature: API Testing for OpenWeatherMap

  @NegativeTest @RegressionTest
  Scenario: Test 1: Validate that attempt to register a weather station "without" APIKey (response code = 401 - error)
    # Test 1
    #Given I have cleaned all stations associated with my APIKey in OpenWhetherMap
    When I have registered a new station to OpenWhetherMap "without" APIKey and following values
      | external_id  | name            | latitude | longitude | altitude |
      | DEMO 0       | My negative Test|  1.3     | 2.2       | 222.3    |

    Then I should see the message body with "cod" as "401"
    And I should see the message body with "message" as "Invalid API key. Please see http://openweathermap.org/faq#error401 for more info."

  @ProgressionTest @RegressionTest
  Scenario Outline: Test 2 to Test 5:
        Test 2: validate registering weather stations successfully (response code = 201)
        Test 3: Validate values after registering a weather station
        Test 4: Validate deleting weather stations successfully (response code = 204)
        Test 5: Validate deleting non-exisiting weather stations (response code = 404 and message = 'Station not fond'

    # Test 2 - Register a new station with the following details and verify that HTTP response code is 201.
    When I have registered a new station to OpenWhetherMap with values of "<external_id>","<name>","<latitude>","<longitude>","<altitude>"
    Then I have received HTTP response code of "201"

    # Test 3 - Using "Get/stations" API to verify that the station was stored in the DB and their values are the same as specified in registration request
    Then I see my new station with values of "<external_id>","<name>","<latitude>","<longitude>","<altitude>"

    # Test 4
    When I delete my latest station associated with my APIKey in OpenWhetherMap
    Then I have received HTTP response code of "204"

    # Test 5
    When I delete my latest station associated with my APIKey in OpenWhetherMap
    Then I have received HTTP response code of "404"
    And I should see the message body with "message" as "Station not found"

   Examples:
   | external_id  | name                       | latitude | longitude | altitude |
   | DEMO_TEST001 | Team Demo Test Station 001 |   33.33  |  -122.43  |   222    |
   | DEMO_TEST002 | Team Demo Test Station 002 |   44.44  |  -122.44  |   111    |
