package com.agrilink.service;

import com.agrilink.dto.DashboardResponse;
import com.agrilink.entity.*;
import com.agrilink.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepo;
    private final JobRepository jobRepo;
    private final ApplicationRepository appRepo;

    public DashboardResponse getDashboard(String email) {

        User user = userRepo.findByEmail(email).orElseThrow();

        if (user.getRole() == User.Role.LABOURER) {
            long totalJobs =
                jobRepo.findByStatus(Job.JobStatus.OPEN).size();
            long totalApps = appRepo.countByLabourer(user);
            long accepted = appRepo.countByLabourerAndStatus(
                user, Application.AppStatus.ACCEPTED);

            return DashboardResponse.builder()
                .role("LABOURER")
                .totalAvailableJobs(totalJobs)
                .totalApplicationsSent(totalApps)
                .acceptedJobsCount(accepted)
                .build();

        } else {
            long posted = jobRepo.countByLandowner(user);
            long appsReceived = appRepo.countByJobLandowner(user);
            long workers = appRepo.countByJobLandownerAndStatus(
                user, Application.AppStatus.ACCEPTED);

            return DashboardResponse.builder()
                .role("LANDOWNER")
                .jobsPosted(posted)
                .totalApplicationsReceived(appsReceived)
                .acceptedWorkersCount(workers)
                .build();
        }
    }
}