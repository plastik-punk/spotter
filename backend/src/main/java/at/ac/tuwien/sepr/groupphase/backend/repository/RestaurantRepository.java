package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT r.name FROM Restaurant r WHERE r.id = :id")
    String findNameById(@Param("id") Long id);

    @Query("SELECT r.address FROM Restaurant r WHERE r.id = :id")
    String findAddressById(@Param("id") Long id);
}
