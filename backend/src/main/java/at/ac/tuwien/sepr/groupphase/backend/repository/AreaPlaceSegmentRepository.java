package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.AreaPlaceSegment;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Repository interface for {@link AreaPlaceSegment} entity.
 * Provides methods to perform CRUD operations and custom queries.
 */
@Repository
public interface AreaPlaceSegmentRepository extends JpaRepository<AreaPlaceSegment, Long> {

    /**
     * Finds all {@link AreaPlaceSegment} entities by the specified area ID.
     *
     * @param areaId the ID of the area
     * @return a list of {@link AreaPlaceSegment} entities
     */
    @Query("SELECT aps FROM AreaPlaceSegment aps WHERE aps.id.areaId = :areaId")
    List<AreaPlaceSegment> findByAreaId(@Param("areaId") Long areaId);


    /**
     * Deletes an {@link AreaPlaceSegment} entity by the specified area ID, place ID, and segment ID.
     *
     * @param areaId    the ID of the area
     * @param placeId   the ID of the place
     * @param segmentId the ID of the segment
     */
    void deleteAreaPlaceSegmentByAreaIdAndPlaceIdAndSegmentId(Long areaId, Long placeId, Long segmentId);

    /**
     * Deletes all {@link AreaPlaceSegment} entities by the specified area ID and place ID.
     *
     * @param areaId  the ID of the area
     * @param placeId the ID of the place
     */
    void deleteAreaPlaceSegmentByAreaIdAndPlaceId(Long areaId, Long placeId);
}
