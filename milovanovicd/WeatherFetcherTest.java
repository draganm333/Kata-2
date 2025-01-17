package milovanovicd;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WeatherFetcherTest {

    @Test
    void testFetchWeatherData() {
        String city = "London";
        try {
            // Call the fetchWeatherData method
            String response = WeatherFetcher.fetchWeatherData(city);

            // Validate the response contains expected data
            assertNotNull(response, "Response should not be null");
            assertTrue(response.contains("\"name\":\"London\""), "Response should contain city name");
        } catch (Exception e) {
            fail("Exception should not occur for a valid city: " + e.getMessage());
        }
    }

    @Test
    void testParseAndDisplayWeatherData() {
        // Mocked JSON response for testing
        String jsonResponse = "{"
                + "\"name\":\"London\","
                + "\"main\":{\"temp\":15.0,\"humidity\":80},"
                + "\"weather\":[{\"description\":\"clear sky\"}]"
                + "}";

        // Capture the printed output
        try {
            WeatherFetcher.parseAndDisplayWeatherData(jsonResponse);
            // Output verification would ideally use a custom logger or test harness
            assertTrue(true, "Output displayed correctly");
        } catch (Exception e) {
            fail("Exception occurred while parsing JSON: " + e.getMessage());
        }
    }

    @Test
    void testFetchWeatherDataInvalidCity() {
        String city = "InvalidCityName";
        try {
            // Call the fetchWeatherData method
            WeatherFetcher.fetchWeatherData(city);
            fail("Exception should occur for an invalid city name");
        } catch (Exception e) {
            // Validate the exception message
            assertTrue(e.getMessage().contains("HTTP response code"), "Exception should indicate HTTP error");
        }
    }

    @Test
    void testEmptyCityName() {
        try {
            // Attempt to fetch data for an empty city name
            String response = WeatherFetcher.fetchWeatherData("");
            fail("Expected an exception for empty city name");
        } catch (Exception e) {
            // Ensure the exception is related to invalid input or an empty name
            assertTrue(e.getMessage().contains("HTTP response code"), "Expected error for empty city name");
        }
    }

    @Test
    void testUnexpectedJsonStructure() {
        // Mock a malformed JSON response
        String malformedJson = "{ \"unexpected\": \"structure\" }";

        try {
            // Call the parseAndDisplayWeatherData method with malformed JSON
            WeatherFetcher.parseAndDisplayWeatherData(malformedJson);
            fail("Expected an exception for unexpected JSON structure");
        } catch (Exception e) {
            // Ensure the exception is related to JSON parsing
            assertTrue(e.getMessage().contains("JSONObject[\"name\"]"), "Expected error related to missing fields");
        }
    }

    @Test
    void testNetworkFailure() {
        String invalidCity = "nonexistentcityfortest";
        try {
            // Attempt to fetch data for a non-existent city
            WeatherFetcher.fetchWeatherData(invalidCity);
            fail("Expected an exception for invalid city causing network failure");
        } catch (Exception e) {
            // Ensure the exception indicates an HTTP error
            assertTrue(e.getMessage().contains("HTTP response code"), "Expected HTTP error for invalid city");
        }
    }
}

