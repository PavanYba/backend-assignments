package com.example.pdapi.controller;

import com.example.pdapi.model.Route;
import com.example.pdapi.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController {
    @Autowired
    private RouteService routeService;

    @GetMapping("/get_route")
    public Route getRoute(@RequestParam String fromPincode, @RequestParam String toPincode) {
        return routeService.getRoute(fromPincode, toPincode);
    }
}
