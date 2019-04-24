package com.gaksvytech.fieldservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gaksvytech.fieldservice.entity.Events;
import com.gaksvytech.fieldservice.enums.EventStatusEnum;

public interface EventRepository extends JpaRepository<Events, Long> {

	List<Events> findAll();

	Optional<Events> findById(Long id);

	List<Events> findByZoneId(int zoneId);

	List<Events> findByStatus(EventStatusEnum status);

	@SuppressWarnings("unchecked")
	Events save(Events entity);

}
