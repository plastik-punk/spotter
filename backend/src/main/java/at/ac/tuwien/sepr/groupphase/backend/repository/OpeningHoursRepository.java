package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.OpeningHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;


/**
 * Repository for the entity OpeningHours.
 * Extends the JpaRepository interface to provide basic CRUD operations.
 */
@Repository
public interface OpeningHoursRepository extends JpaRepository<OpeningHours, Long> {


    /**
     * Finds opening hours based on the day of the week.
     *
     * @param dayOfWeek the day of the week to filter opening hours
     * @return a list of opening hours for the specified day of the week
     */
    List<OpeningHours> findByDayOfWeek(DayOfWeek dayOfWeek);
}
