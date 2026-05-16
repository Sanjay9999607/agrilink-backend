package com.agrilink.controller;

import com.agrilink.dto.ApplicationResponse;
import com.agrilink.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ApplicationController {

    private final ApplicationService appService;

    // LABOURER — apply for a job
    @PostMapping("/{jobId}")
    public ResponseEntity<Map<String, String>> apply(
            @PathVariable Long jobId) {
        return ResponseEntity.ok(
            appService.apply(jobId, email()));
    }

    // LANDOWNER — accept or reject
    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(
            appService.updateStatus(id, status, email()));
    }

    // LABOURER — view own applications
    @GetMapping("/my")
    public ResponseEntity<List<ApplicationResponse>> myApps() {
        return ResponseEntity.ok(
            appService.getMyApplications(email()));
    }

    // LABOURER — notifications: returns {"acceptedCount": N}
    @GetMapping("/notifications")
    public ResponseEntity<Map<String, Long>> labourerNotifications() {
        return ResponseEntity.ok(
            appService.getLabourerNotifications(email()));
    }

    // LANDOWNER — notifications: returns {"pendingCount": N}
    @GetMapping("/notifications/landowner")
    public ResponseEntity<Map<String, Long>> landownerNotifications() {
        return ResponseEntity.ok(
            appService.getLandownerNotifications(email()));
    }

    private String email() {
        return SecurityContextHolder.getContext()
            .getAuthentication().getName();
    }
}