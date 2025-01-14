package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PermanentReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PermanentReservationDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PermanentReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PermanentReservationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationEditDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationModalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationWalkInDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.ReservationResponseEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import jakarta.mail.MessagingException;

import java.util.List;


/**
 * Service for operations on reservations (e.g. creating a new Reservation).
 */
public interface ReservationService {

    /**
     * Create a reservation.
     *
     * @param reservationCreateDto the reservation data
     * @return the reservation as provided from the Repository layer after creation in the database
     */
    ReservationCreateDto create(ReservationCreateDto reservationCreateDto) throws MessagingException;

    /**
     * Check if any tables are available for requested time and pax.
     *
     * @param reservationCheckAvailabilityDto the reservation data
     * @return the availability status
     */
    ReservationResponseEnum getAvailability(ReservationCheckAvailabilityDto reservationCheckAvailabilityDto);

    /**
     * Get the next three available tables for a reservation.
     *
     * @param reservationCheckAvailabilityDto the reservation data
     * @return the next three available tables
     */
    ReservationCheckAvailabilityDto[] getNextAvailableTables(ReservationCheckAvailabilityDto reservationCheckAvailabilityDto);

    /**
     * Get the details of a reservation specified by its Hashed id.
     *
     * @param id the Hashed id of the reservation
     * @return the reservation details
     */
    ReservationEditDto getByHashedId(String id);

    /**
     * Get the details of a reservation specified by its id.
     *
     * @param id the id of the reservation
     * @return the reservation details
     */
    ReservationModalDetailDto getModalDetail(String id);

    /**
     * Update a reservation.
     *
     * @param reservationEditDto the reservation data
     * @return the updated reservation
     */
    ReservationEditDto update(ReservationEditDto reservationEditDto);

    /**
     * Find all reservations that match the search parameters ordered by startDate (desc).
     *
     * @param reservationSearchDto the search parameters to use in filtering.
     * @return List of ReservationListDto that match the search parameters
     */
    List<ReservationListDto> search(ReservationSearchDto reservationSearchDto);


    /**
     * Cancel a reservation.
     *
     * @param hashId the Hashed id of the reservation.
     */
    void cancel(String hashId);

    /**
     * Confirm a reservation.
     *
     * @param hashId the Hashed id of the reservation.
     * @throws NotFoundException if the reservation is not found
     */
    void confirm(String hashId) throws NotFoundException;

    /**
     * Unconfirm a reservation.
     *
     * @param hashId the Hashed id of the reservation.
     * @throws NotFoundException if the reservation is not found
     */
    void unconfirm(String hashId) throws NotFoundException;

    /**
     * Create a walk-in reservation.
     *
     * @param reservationWalkInDto the necessary data for a walk-in reservation
     * @return the walk-in reservation as provided from the Repository layer after creation in the database
     */
    ReservationCreateDto createWalkIn(ReservationWalkInDto reservationWalkInDto) throws ConflictException, MessagingException;

    /**
     * Create a permanent reservation (still needs to be confirmed).
     *
     * @param permanentReservationCreateDto the necessary data for a permanent reservation
     * @return the permanent reservation as provided from the persistence layer after creation in the data storage
     */
    PermanentReservationCreateDto createPermanent(PermanentReservationCreateDto permanentReservationCreateDto);


    /**
     * Confirm a Permanent Reservation and create single reservations for it.
     *
     * @param id ID to confirm
     */
    void confirmPermanentReservation(Long id) throws MessagingException;

    /**
     * Find all permanent reservations that match the search parameters ordered by startDate (desc).
     *
     * @param searchParams the search parameters to use in filtering.
     * @return List of PermanentReservationCreateDto that match the search parameters
     */
    List<PermanentReservationListDto> searchPermanent(PermanentReservationSearchDto searchParams);


    /**
     * Get the details for a permanent reservation with the given hashedId.
     *
     * @param hashedId the unique hashedId for the permanent reservation
     * @return a PermanentReservationDetailDto with the details of the permanent reservation
     */
    PermanentReservationDetailDto getPermanentDetails(String hashedId);

    /**
     * Delete a permanent reservation and all its single reservations.
     *
     * @param permanentReservationHashId hashed id of the permanent reservation to delete
     */
    void deletePermanentReservation(String permanentReservationHashId) throws MessagingException;
}


