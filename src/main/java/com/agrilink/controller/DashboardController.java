package com.agrilink.controller;

import com.agrilink.dto.DashboardResponse;
import com.agrilink.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context
    .SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashService;

    @GetMapping
    public ResponseEntity<DashboardResponse> get() {
        return ResponseEntity.ok(
            dashService.getDashboard(
                SecurityContextHolder.getContext()
                    .getAuthentication().getName()));
    }
}