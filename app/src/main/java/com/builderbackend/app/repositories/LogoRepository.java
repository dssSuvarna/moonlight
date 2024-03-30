
package com.builderbackend.app.repositories;

import java.util.List;
import com.builderbackend.app.models.Logo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface LogoRepository extends JpaRepository<Logo, String> {


    Optional<List<Logo>> findByBusinessBusinessId(String businessId);


}