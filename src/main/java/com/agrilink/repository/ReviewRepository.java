package com.agrilink.repository;

import com.agrilink.entity.Job;
import com.agrilink.entity.Review;
import com.agrilink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByJobAndReviewerAndReviewee(Job job, User reviewer, User reviewee);
    List<Review> findByReviewee(User reviewee);
    List<Review> findByReviewer(User reviewer);
    long countByReviewee(User reviewee);
}
