package com.agrilink.service;

import com.agrilink.dto.ApplicationResponse;
import com.agrilink.entity.*;
import com.agrilink.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.agrilink.dto.ReviewResponse;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository appRepo;
    private final JobRepository jobRepo;
    private final UserRepository userRepo;
    private final LabourerProfileRepository labRepo;
    private final ReviewRepository reviewRepo;

    // ── Labourer applies for a job ────────────────────────────────
    public Map<String, String> apply(Long jobId, String email) {

        User labourer = userRepo.findByEmail(email).orElseThrow();

        Job job = jobRepo.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Job not found"));

        if (job.getStatus() == Job.JobStatus.CLOSED)
            throw new RuntimeException("This job is already closed");

        if (appRepo.existsByJobAndLabourer(job, labourer))
            throw new RuntimeException("You have already applied for this job");

        Application app = Application.builder()
            .job(job).labourer(labourer).build();

        appRepo.save(app);
        return Map.of("message", "Application submitted successfully");
    }

    // ── Landowner views applications for their job ────────────────
    // Skills ALWAYS visible to landowner so they can decide.
    // Name/phone revealed only after ACCEPTED.
    public List<ApplicationResponse> getApplications(Long jobId, String email) {

        User landowner = userRepo.findByEmail(email).orElseThrow();

        Job job = jobRepo.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getLandowner().getId().equals(landowner.getId()))
            throw new RuntimeException("Not your job");

        return appRepo.findByJob(job).stream()
            .map(this::toResponseForLandowner)
            .collect(Collectors.toList());
    }

    // ── Landowner accepts or rejects ──────────────────────────────
    public ApplicationResponse updateStatus(Long appId, String newStatus, String email) {

        User landowner = userRepo.findByEmail(email).orElseThrow();

        Application app = appRepo.findById(appId)
            .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!app.getJob().getLandowner().getId().equals(landowner.getId()))
            throw new RuntimeException("Not authorised");

        Application.AppStatus status =
            Application.AppStatus.valueOf(newStatus.toUpperCase());
        app.setStatus(status);
        appRepo.save(app);

        if (status == Application.AppStatus.ACCEPTED) {
            Job job = app.getJob();
            job.setWorkersAcceptedCount(job.getWorkersAcceptedCount() + 1);
            // Job closes ONLY when all required spots are filled
            if (job.getWorkersAcceptedCount() >= job.getWorkersRequired()) {
                job.setStatus(Job.JobStatus.CLOSED);
            }
            jobRepo.save(job);
        }

        return toResponseForLandowner(app);
    }

    // ── Labourer views their own applications ─────────────────────
    // Shows landowner contact only after ACCEPTED.
    public List<ApplicationResponse> getMyApplications(String email) {

        User labourer = userRepo.findByEmail(email).orElseThrow();

        return appRepo.findByLabourer(labourer).stream()
            .map(this::toResponseForLabourer)
            .collect(Collectors.toList());
    }

    // ── Labourer notifications — returns {acceptedCount: N} ───────
    public Map<String, Long> getLabourerNotifications(String email) {
        User labourer = userRepo.findByEmail(email).orElseThrow();
        long accepted = appRepo.countByLabourerAndStatus(
            labourer, Application.AppStatus.ACCEPTED);
        return Map.of("acceptedCount", accepted);
    }

    // ── Landowner notifications — returns {pendingCount: N} ───────
    public Map<String, Long> getLandownerNotifications(String email) {
        User landowner = userRepo.findByEmail(email).orElseThrow();
        long pending = appRepo.countByJobLandownerAndStatus(
            landowner, Application.AppStatus.PENDING);
        return Map.of("pendingCount", pending);
    }

    // ── Build response for LANDOWNER ──────────────────────────────
    // Skills always shown. Name/phone hidden until ACCEPTED.
    private ApplicationResponse toResponseForLandowner(Application app) {
        User lab = app.getLabourer();
        boolean accepted = app.getStatus() == Application.AppStatus.ACCEPTED;

        String skills = labRepo.findByUser(lab)
            .map(LabourerProfile::getSkills)
            .orElse("—");

        List<ReviewResponse> labourerReviews = reviewRepo.findByReviewee(lab).stream()
            .map(r -> ReviewResponse.builder()
                .id(r.getId())
                .jobId(r.getJob().getId())
                .reviewerId(r.getReviewer().getId())
                .reviewerName(r.getReviewer().getName())
                .revieweeId(r.getReviewee().getId())
                .revieweeName(r.getReviewee().getName())
                .rating(r.getRating())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .build())
            .collect(Collectors.toList());

        return ApplicationResponse.builder()
            .id(app.getId())
            .jobId(app.getJob().getId())
            .jobTitle(app.getJob().getTitle())
            .jobLocation(app.getJob().getLocationName())
            .jobPayment(String.valueOf(app.getJob().getPayment()))
            .labourerId(lab.getId())
            .labourerName(accepted ? lab.getName() : null)
            .labourerPhone(accepted ? lab.getPhone() : null)
            .labourerEmail(accepted ? lab.getEmail() : null)
            .labourerSkills(skills)                     // always visible
            .labourerLocation(lab.getLocationName())
            .labourerAverageRating(lab.getAverageRating())
            .labourerRatingCount(lab.getRatingCount())
            .labourerReviews(labourerReviews)
            .landownerId(app.getJob().getLandowner().getId())
            .landownerName(app.getJob().getLandowner().getName())
            .landownerPhone(app.getJob().getLandowner().getPhone())
            .landownerEmail(app.getJob().getLandowner().getEmail())
            .status(app.getStatus().name())
            .appliedAt(app.getAppliedAt())
            .build();
    }

    // ── Build response for LABOURER ───────────────────────────────
    // Shows landowner contact ONLY after ACCEPTED.
    private ApplicationResponse toResponseForLabourer(Application app) {
        User lab = app.getLabourer();
        boolean accepted = app.getStatus() == Application.AppStatus.ACCEPTED;

        String skills = labRepo.findByUser(lab)
            .map(LabourerProfile::getSkills)
            .orElse("—");

        return ApplicationResponse.builder()
            .id(app.getId())
            .jobId(app.getJob().getId())
            .jobTitle(app.getJob().getTitle())
            .jobLocation(app.getJob().getLocationName())
            .jobPayment(String.valueOf(app.getJob().getPayment()))
            .labourerId(lab.getId())
            .labourerName(lab.getName())
            .labourerPhone(lab.getPhone())
            .labourerSkills(skills)
            .labourerLocation(lab.getLocationName())
            // Landowner contact revealed only after accepted
            .landownerId(app.getJob().getLandowner().getId())
            .landownerName(accepted ? app.getJob().getLandowner().getName() : null)
            .landownerPhone(accepted ? app.getJob().getLandowner().getPhone() : null)
            .landownerEmail(accepted ? app.getJob().getLandowner().getEmail() : null)
            .status(app.getStatus().name())
            .appliedAt(app.getAppliedAt())
            .build();
    }
}