package com.example.pdapi.model;

import jakarta.persistence.*;

@Entity
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fromPincode;

    @Column(nullable = false)
    private String toPincode;

    @Column(nullable = false)
    private String distance;

    @Column(nullable = false)
    private String duration;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String routes;
    @Column(nullable = false)
    private Double fromLatitude;
    @Column(nullable = false)
    private Double fromLongitude;
    @Column(nullable = false)
    private Double toLatitude;
    @Column(nullable = false)
    private Double toLongitude;
    @Column(nullable = false, length = 100000000)
    private String polygonInfo;

    public Double getFromLatitude() {
        return fromLatitude;
    }

    public void setFromLatitude(Double fromLatitude) {
        this.fromLatitude = fromLatitude;
    }

    public Double getFromLongitude() {
        return fromLongitude;
    }

    public void setFromLongitude(Double fromLongitude) {
        this.fromLongitude = fromLongitude;
    }

    public Double getToLatitude() {
        return toLatitude;
    }

    public void setToLatitude(Double toLatitude) {
        this.toLatitude = toLatitude;
    }

    public Double getToLongitude() {
        return toLongitude;
    }

    public void setToLongitude(Double toLongitude) {
        this.toLongitude = toLongitude;
    }

    public String getPolygonInfo() {
        return polygonInfo;
    }

    public void setPolygonInfo(String polygonInfo) {
        this.polygonInfo = polygonInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromPincode() {
        return fromPincode;
    }

    public void setFromPincode(String fromPincode) {
        this.fromPincode = fromPincode;
    }

    public String getToPincode() {
        return toPincode;
    }

    public void setToPincode(String toPincode) {
        this.toPincode = toPincode;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRoutes() {
        return routes;
    }

    public void setRoutes(String routes) {
        this.routes = routes;
    }
}
