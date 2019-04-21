package com.gaksvytech.fieldservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gaksvytech.fieldservice.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

	List<Users> findAll();

	Optional<Users> findById(Long id);

	Optional<Users> findByZoneId(Long zoneId);

	List<Users> findByName(String name);

	List<Users> findByNameAndEmail(String name, String email);

	@Query("SELECT w FROM Users w WHERE (:name is null or w.name = :name) and (:email is null or w.email = :email)")
	List<Users> findUsersByNameAndEmail(@Param("name") String name, @Param("email") String email);

	@SuppressWarnings("unchecked")
	Users save(Users entity);

}
