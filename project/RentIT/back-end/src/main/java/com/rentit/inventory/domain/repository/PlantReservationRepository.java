package com.rentit.inventory.domain.repository;

import com.rentit.inventory.domain.model.PlantReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantReservationRepository extends JpaRepository<PlantReservation, Long> {
}
