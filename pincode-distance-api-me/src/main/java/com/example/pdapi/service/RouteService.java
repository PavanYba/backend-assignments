package com.example.pdapi.service;

import com.example.pdapi.model.Route;
import com.example.pdapi.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;

@Service
public class RouteService {
    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @Autowired
    private RouteRepository routeRepository;

    @Cacheable(value = "routes", key = "#fromPincode.concat('-').concat(#toPincode)")
    public Route getRoute(String fromPincode, String toPincode) {
        // Check cache
        Route cachedRoute = routeRepository.findByFromPincodeAndToPincode(fromPincode, toPincode);
        if (cachedRoute != null) {
            return cachedRoute;
        }

        // Call Google Maps API to get data
        String apiUrl = "https://maps.googleapis.com/maps/api/directions/json"
                + "?origin=" + fromPincode
                + "&destination=" + toPincode
                + "&key=" + googleMapsApiKey;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                String responseBody = response.getBody();
                System.out.println("Google Maps API Response: " + responseBody); // Print the response for debugging

                // Parse JSON response
                JSONObject json = new JSONObject(responseBody);
                JSONArray routes = json.getJSONArray("routes");

                if (routes.length() == 0) {
                    // No routes found
                    throw new RuntimeException("No routes found between the provided locations");
                }

                JSONObject routeJson = routes.getJSONObject(0);  // Assuming only one route
                JSONArray legs = routeJson.getJSONArray("legs");
                JSONObject leg = legs.getJSONObject(0);  // Assuming only one leg

                String distance = leg.getJSONObject("distance").getString("text");
                String duration = leg.getJSONObject("duration").getString("text");
                JSONArray steps = leg.getJSONArray("steps");

                List<String> routeSteps = new ArrayList<>();
                for (int i = 0; i < steps.length(); i++) {
                    JSONObject step = steps.getJSONObject(i);
                    routeSteps.add(step.getString("html_instructions"));
                }
                // Extracting additional information
                JSONObject startLocation = leg.getJSONObject("start_location");
                Double fromLatitude = startLocation.getDouble("lat");
                Double fromLongitude = startLocation.getDouble("lng");

                JSONObject endLocation = leg.getJSONObject("end_location");
                Double toLatitude = endLocation.getDouble("lat");
                Double toLongitude = endLocation.getDouble("lng");

                // Extracting polyline points for the route
                String polylinePoints = routeJson.getJSONObject("overview_polyline").getString("points");

                // Decoding polyline to get approximate coordinates
                List<String> decodedPolyline = decodePolyline(polylinePoints);

                // Joining decoded polyline points into a single string
                String polygonInfo = String.join(",", decodedPolyline);
                if (polygonInfo.length() > 255) {
                    polygonInfo = polygonInfo.substring(0, 255);
                }
                // Create a new Route object
                Route route = new Route();
                route.setFromPincode(fromPincode);
                route.setToPincode(toPincode);
                route.setDistance(distance);
                route.setDuration(duration);
                route.setRoutes(new Gson().toJson(routeSteps));  // Convert steps to JSON string
                route.setFromLatitude(fromLatitude);
                route.setFromLongitude(fromLongitude);
                route.setToLatitude(toLatitude);
                route.setToLongitude(toLongitude);
                route.setPolygonInfo(polygonInfo);
                // Save to cache and database
                routeRepository.save(route);

                return route;
            } catch (Exception e) {
                throw new RuntimeException("Error parsing data from Google Maps API", e);
            }
        } else {
            // Handle API error response
            throw new RuntimeException("Error retrieving data from Google Maps API");
        }
    }

    // Helper method to decode polyline points
    private List<String> decodePolyline(String encoded) {
        List<String> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1F) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1F) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            double latitude = lat / 1e5;
            double longitude = lng / 1e5;
            poly.add(latitude + "," + longitude);
        }
        return poly;
    }
}
