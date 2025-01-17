package milovanovicd;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * A program to fetch and display current weather data for a given city using OpenWeatherMap API.
 */
public class WeatherFetcher {

    /**
     * Base URL for OpenWeatherMap API.
     */
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather";

    /**
     * OpenWeatherMap API key.
     */
    private static final String API_KEY = "bd5e378503939ddaee76f12ad7a97608";

    /**
     * Main method to run the program.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        // Use try with resources to ensure Scanner is closed automatically
        try (Scanner scanner = new Scanner(System.in)) {
            // Prompt user for location
            System.out.println("Enter a city name to fetch its weather data:");
            String city = scanner.nextLine().trim();
            // Validate input
            if (city.isEmpty()) {
                System.out.println("City name cannot be empty. Please try again.");
                return;
            }
            // Fetch and display weather data
            String jsonResponse = fetchWeatherData(city);
            parseAndDisplayWeatherData(jsonResponse);
        } catch (Exception e) {
            // Handle any exceptions and display a user-friendly message
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Fetches weather data from the OpenWeatherMap API for the given city.
     *
     * @param city The name of the city for which to fetch weather data.
     * @return The JSON response as a string.
     * @throws Exception If an error occurs during the API request.
     */
    static String fetchWeatherData(String city) throws Exception {
        // Construct the complete API URL
        String urlString = API_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";
        URL url = new URL(urlString);

        // Open an HTTP connection to the API
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Check the response code
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Use try-with-resources to read the input stream
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                // Read each line of the response and append it to the StringBuilder
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        } else {
            // Throw an exception if the response code is not HTTP OK
            throw new Exception("Failed to fetch weather data. HTTP response code: " + responseCode);
        }
    }

    /**
     * Parses the JSON response and displays the relevant weather data in a table format.
     *
     * @param jsonResponse The JSON response as a string.
     */
    static void parseAndDisplayWeatherData(String jsonResponse) {
        // Parse the JSON response
        JSONObject jsonObject = new JSONObject(jsonResponse);

        // Extract relevant fields from the JSON response
        String cityName = jsonObject.getString("name");
        JSONObject main = jsonObject.getJSONObject("main");
        double temperature = main.getDouble("temp");
        int humidity = main.getInt("humidity");
        JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
        String description = weather.getString("description");

        // Display the weather data in a table format
        System.out.println("--------------------------------------------------");
        System.out.printf("| %-18s | %-25s |%n", "City", cityName);
        System.out.println("--------------------------------------------------");
        System.out.printf("| %-18s | %-25.2f |%n", "Temperature (°C)", temperature);
        System.out.println("--------------------------------------------------");
        System.out.printf("| %-18s | %-25d |%n", "Humidity (%)", humidity);
        System.out.println("--------------------------------------------------");
        System.out.printf("| %-18s | %-25s |%n", "Condition", description);
        System.out.println("--------------------------------------------------");
    }
}
