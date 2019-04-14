package com.gaksvytech.fieldservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gaksvytech.fieldservice.entity.WorkForce;

public interface WorkForceRepository extends JpaRepository<WorkForce, Long> {

	List<WorkForce> findAll();

	Optional<WorkForce> findById(Long id);

	List<WorkForce> findByName(String name);

	List<WorkForce> findByNameAndEmail(String name, String email);

	@Query("SELECT w FROM WorkForce w WHERE (:name is null or w.name = :name) and (:email is null or w.email = :email)")
	List<WorkForce> findWorkForceByNameAndEmail(@Param("name") String name, @Param("email") String email);

	@SuppressWarnings("unchecked")
	WorkForce save(WorkForce entity);

}
