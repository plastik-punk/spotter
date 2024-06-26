package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for the entity Restaurant.
 * Extends JpaRepository to provide basic CRUD operations.
 */
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {


    /**
     * Finds the name of the restaurant by its ID.
     *
     * @param id the ID of the restaurant
     * @return the name of the restaurant
     */
    @Query("SELECT r.name FROM Restaurant r WHERE r.id = :id")
    String findNameById(@Param("id") Long id);


    /**
     * Finds the address of the restaurant by its ID.
     *
     * @param id the ID of the restaurant
     * @return the address of the restaurant
     */
    @Query("SELECT r.address FROM Restaurant r WHERE r.id = :id")
    String findAddressById(@Param("id") Long id);
}
