package com.builderbackend.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.builderbackend.app.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User getReferenceByUserName(String userName);

    List<User> findByBusinessBusinessId(String businessId);

    // get users based off business ID and user role
    List<User> findByBusinessBusinessIdAndRole(String businessId, String role);

    @Query("select u.business.businessId from User u where u.id = ?1")
    String findBusinessIdByUserId(String userId);

    @Query("select u.business.businessName from User u where u.id = ?1")
    String findBusinessNameByUserId(String userId);

    User findByUserId(String userId);

    User findByUserName(String userName);

}