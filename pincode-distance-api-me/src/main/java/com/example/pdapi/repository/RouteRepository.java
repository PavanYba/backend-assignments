package com.example.pdapi.repository;

import com.example.pdapi.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    Route findByFromPincodeAndToPincode(String fromPincode, String toPincode);
}
