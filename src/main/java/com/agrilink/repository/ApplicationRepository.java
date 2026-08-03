package com.agrilink.repository;

import com.agrilink.entity.Application;
import com.agrilink.entity.Job;
import com.agrilink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    List<Application> findByLabourer(User labourer);

    List<Application> findByJob(Job job);

    boolean existsByJobAndLabourer(Job job, User labourer);

    Optional<Application> findByJobAndLabourer(Job job, User labourer);

    Optional<Application> findByJobAndLabourerAndStatus(Job job, User labourer, Application.AppStatus status);

    // ── For DashboardService (LABOURER) ──────────────────────────
    long countByLabourer(User labourer);

    long countByLabourerAndStatus(User labourer, Application.AppStatus status);

    // ── For DashboardService (LANDOWNER) ─────────────────────────
    long countByJobLandowner(User landowner);

    long countByJobLandownerAndStatus(User landowner, Application.AppStatus status);
}