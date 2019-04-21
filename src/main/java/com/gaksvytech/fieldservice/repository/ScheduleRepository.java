package com.gaksvytech.fieldservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gaksvytech.fieldservice.entity.Schedules;

public interface ScheduleRepository extends JpaRepository<Schedules, Long> {

	List<Schedules> findAll();

	@SuppressWarnings("unchecked")
	Schedules save(Schedules entity);

}
