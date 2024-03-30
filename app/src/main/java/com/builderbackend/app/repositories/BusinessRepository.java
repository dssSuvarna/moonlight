package com.builderbackend.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.builderbackend.app.models.Business;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<Business, String> {
    Business findByBusinessId(String businessId);
}
