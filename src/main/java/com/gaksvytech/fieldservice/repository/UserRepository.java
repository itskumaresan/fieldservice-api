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

	List<Users> findByUsername(String username);

	List<Users> findByUsernameAndEmail(String username, String email);

	@Query("SELECT w FROM Users w WHERE (:username is null or w.username = :username) and (:email is null or w.email = :email)")
	List<Users> findWorkForceByUsernameAndEmail(@Param("username") String username, @Param("email") String email);

	@SuppressWarnings("unchecked")
	Users save(Users entity);

}
