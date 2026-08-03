package com.agrilink.service;

import com.agrilink.dto.*;
import com.agrilink.entity.*;
import com.agrilink.repository.*;
import com.agrilink.util.HaversineUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepo;
    private final UserRepository userRepo;
    private final ApplicationRepository appRepo;
    private final HaversineUtil haversine;

    public JobResponse createJob(
            JobRequest req, String email) {

        User landowner =
            userRepo.findByEmail(email).orElseThrow();

        // Coordinates come from frontend (Photon browser geocoding)
        // Backend does NOT call any geocoding API
        if (req.getLatitude() == null
                || req.getLongitude() == null) {
            throw new RuntimeException(
                "Location coordinates missing. "
                + "Please wait for GPS to resolve "
                + "before submitting.");
        }

        Job job = Job.builder()
            .title(req.getTitle())
            .description(req.getDescription())
            .payment(req.getPayment())
            .workersRequired(req.getWorkersRequired())
            .locationName(req.getLocation())
            .latitude(req.getLatitude())
            .longitude(req.getLongitude())
            .maxDistance(req.getMaxDistance())
            .landowner(landowner)
            .build();

        jobRepo.save(job);
        return toResponse(job, null);
    }

    // Labourer browses jobs
    // DOUBLE FILTER:
    // 1. distance <= job.maxDistance (landowner rule)
    // 2. distance <= labourer.workRadius (labourer rule)
    // Both must pass. Sorted nearest first.
    // Also excludes jobs that the labourer has already applied for.
    public List<JobResponse> getNearbyJobs(String email) {

        User labourer =
            userRepo.findByEmail(email).orElseThrow();

        if (labourer.getLatitude() == null) {
            throw new RuntimeException(
                "Your location is not set. "
                + "Please update your profile.");
        }

        Set<Long> appliedJobIds = appRepo.findByLabourer(labourer).stream()
            .map(app -> app.getJob().getId())
            .collect(Collectors.toSet());

        return jobRepo.findByStatus(Job.JobStatus.OPEN)
            .stream()
            .filter(job -> !appliedJobIds.contains(job.getId()))
            .filter(job -> job.getWorkersAcceptedCount() < job.getWorkersRequired())
            .map(job -> {
                double dist = haversine.distance(
                    labourer.getLatitude(),
                    labourer.getLongitude(),
                    job.getLatitude(),
                    job.getLongitude());
                return Map.entry(job, dist);
            })
            .filter(e ->
                e.getValue() <= e.getKey().getMaxDistance()
                && (labourer.getWorkRadius() == null
                    || e.getValue()
                        <= labourer.getWorkRadius()))
            .sorted(Comparator.comparingDouble(
                Map.Entry::getValue))
            .map(e -> toResponse(
                e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }

    public List<JobResponse> getMyJobs(String email) {
        User landowner =
            userRepo.findByEmail(email).orElseThrow();
        return jobRepo.findByLandowner(landowner)
            .stream()
            .map(j -> toResponse(j, null))
            .collect(Collectors.toList());
    }

    public JobResponse getById(Long id) {
        return toResponse(
            jobRepo.findById(id).orElseThrow(
                () -> new RuntimeException(
                    "Job not found")), null);
    }

    private JobResponse toResponse(
            Job job, Double distance) {
        return JobResponse.builder()
            .id(job.getId())
            .title(job.getTitle())
            .description(job.getDescription())
            .payment(job.getPayment())
            .workersRequired(job.getWorkersRequired())
            .workersAcceptedCount(
                job.getWorkersAcceptedCount())
            .locationName(job.getLocationName())
            .latitude(job.getLatitude())
            .longitude(job.getLongitude())
            .maxDistance(job.getMaxDistance())
            .status(job.getStatus().name())
            .landownerName(job.getLandowner().getName())
            .landownerAverageRating(job.getLandowner().getAverageRating())
            .landownerRatingCount(job.getLandowner().getRatingCount())
            .distanceKm(distance)
            .createdAt(job.getCreatedAt())
            .build();
    }
}