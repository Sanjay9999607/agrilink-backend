package com.agrilink.controller;

import com.agrilink.dto.*;
import com.agrilink.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.context
    .SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class JobController {

    private final JobService jobService;
    private final ApplicationService appService;

    // LANDOWNER — post a job
    @PostMapping
    public ResponseEntity<JobResponse> create(
            @Valid @RequestBody JobRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(jobService.createJob(req, email()));
    }

    // LANDOWNER — view their jobs
    @GetMapping("/my")
    public ResponseEntity<List<JobResponse>> myJobs() {
        return ResponseEntity.ok(
            jobService.getMyJobs(email()));
    }

    // LABOURER — browse nearby jobs (double filtered)
    @GetMapping
    public ResponseEntity<List<JobResponse>> nearby() {
        return ResponseEntity.ok(
            jobService.getNearbyJobs(email()));
    }

    // LANDOWNER — view applications for their job
    @GetMapping("/{id}/applications")
    public ResponseEntity<List<ApplicationResponse>>
            applications(@PathVariable Long id) {
        return ResponseEntity.ok(
            appService.getApplications(id, email()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getOne(
            @PathVariable Long id) {
        return ResponseEntity.ok(jobService.getById(id));
    }

    private String email() {
        return SecurityContextHolder.getContext()
            .getAuthentication().getName();
    }
}