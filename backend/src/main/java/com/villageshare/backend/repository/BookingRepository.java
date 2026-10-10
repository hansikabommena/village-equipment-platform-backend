package com.villageshare.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.villageshare.backend.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
        SELECT COUNT(b) > 0
        FROM Booking b
        WHERE b.resourceName = :resourceName
        AND b.status IN ('PENDING', 'APPROVED')
        AND b.startDate <= :endDate
        AND b.endDate >= :startDate
    """)
    boolean existsOverlappingBooking(
        @Param("resourceName") String resourceName,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );

    List<Booking> findByOwnerEmail(String ownerEmail);
    List<Booking> findByName(String name);
    boolean existsByResourceId(Long resourceId);
}

