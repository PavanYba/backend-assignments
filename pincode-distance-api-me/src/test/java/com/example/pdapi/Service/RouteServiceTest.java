package com.example.pdapi.Service;

import com.example.pdapi.model.Route;
import com.example.pdapi.repository.RouteRepository;
import com.example.pdapi.service.RouteService;
import com.example.pdapi.util.GoogleMapsApiKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RouteServiceTest {

    @InjectMocks
    private RouteService routeService;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private GoogleMapsApiKey apiKey;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidRoute() {
        // Mock GoogleMapsApiKey to provide a valid API key
        when(apiKey.getApiKey()).thenReturn("AIzaSyBvMt99LRmvzQaSKoFS-HmMfBNZmkOqtKw");

        // Mock the Google Maps API response for a valid route
        String validApiResponse = "{\"routes\": [{\"legs\": [{\"distance\": {\"text\": \"10 km\"}, " +
                "\"duration\": {\"text\": \"15 mins\"}, \"steps\": [{\"html_instructions\": \"Step 1\"}, " +
                "{\"html_instructions\": \"Step 2\"}], \"start_location\": {\"lat\": 40.7128, \"lng\": -74.0060}, " +
                "\"end_location\": {\"lat\": 34.0522, \"lng\": -118.2437}, \"overview_polyline\": {\"points\": " +
                "\"encodedPolylineData\"}}]}], \"status\": \"OK\"}";

        ResponseEntity<String> googleMapsResponse = new ResponseEntity<>(validApiResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(any(String.class), any(Class.class))).thenReturn(googleMapsResponse);

        // Mock existing route in repository
        Route existingRoute = new Route();
        existingRoute.setFromPincode("10001");
        existingRoute.setToPincode("20001");
        existingRoute.setDistance("10 km");
        existingRoute.setDuration("15 mins");


        when(routeRepository.findByFromPincodeAndToPincode("10001", "20001")).thenReturn(existingRoute);

        // Call the method to test
        Route route = routeService.getRoute("10001", "20001");

        // Verify the result
        assertEquals("10 km", route.getDistance());
        assertEquals("15 mins", route.getDuration());
        assertEquals("10001", route.getFromPincode());
        assertEquals("20001", route.getToPincode());

    }

    @Test
    public void testInvalidApiKey() {
        // Mock GoogleMapsApiKey to provide an invalid API key
        when(apiKey.getApiKey()).thenReturn("jwafhjkwashfjlashflkahbvjlehdbv");

        // Mock the Google Maps API response for an invalid API key
        String invalidApiKeyResponse = "{\"error_message\": \"The provided API key is invalid.\", " +
                "\"routes\": [], \"status\": \"REQUEST_DENIED\"}";

        ResponseEntity<String> googleMapsResponse = new ResponseEntity<>(invalidApiKeyResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(any(String.class), any(Class.class))).thenReturn(googleMapsResponse);

        // Call the method to test
        try {
            Route route = routeService.getRoute("10001", "20001");
        } catch (RuntimeException e) {
            // Verify that the correct error message is thrown
            assertEquals("Error parsing data from Google Maps API", e.getMessage());
        }
    }
}
