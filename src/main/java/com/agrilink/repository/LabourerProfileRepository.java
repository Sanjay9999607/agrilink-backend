package com.agrilink.repository;

import com.agrilink.entity.LabourerProfile;
import com.agrilink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LabourerProfileRepository
        extends JpaRepository<LabourerProfile, Long> {

    Optional<LabourerProfile> findByUser(User user);
}