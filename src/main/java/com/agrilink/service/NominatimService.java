package com.agrilink.service;

import org.springframework.stereotype.Service;

@Service
public class NominatimService {

    // Geocoding is now done in the browser using Photon API
    // This class is kept to avoid compilation errors
    // It is not called anywhere anymore

    public double[] getCoordinates(String location) {
        throw new RuntimeException(
            "Server-side geocoding disabled. "
            + "Coordinates must be sent from frontend.");
    }
}