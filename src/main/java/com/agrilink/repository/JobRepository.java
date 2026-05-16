package com.agrilink.repository;

import com.agrilink.entity.Job;
import com.agrilink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository
        extends JpaRepository<Job, Long> {

    List<Job> findByLandowner(User landowner);
    List<Job> findByStatus(Job.JobStatus status);
    long countByLandowner(User landowner);
}