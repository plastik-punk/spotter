package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.AreaPlaceSegment;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AreaPlaceSegmentRepository extends JpaRepository<AreaPlaceSegment, Long> {
    @Query("SELECT aps FROM AreaPlaceSegment aps WHERE aps.id.areaId = :areaId")
    List<AreaPlaceSegment> findByAreaId(@Param("areaId") Long areaId);

    void deleteAreaPlaceSegmentByAreaIdAndPlaceIdAndSegmentId(Long areaId, Long placeId, Long segmentId);
}
