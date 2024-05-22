package at.ac.tuwien.sepr.groupphase.backend.service;

/**
 * Service for hashing values.
 */

public interface HashService {

    /**
     * Hash a string with SHA256.
     *
     * @param input the string to hash
     * @return the hashed string
     */
    String hashSha256(String input);


}
