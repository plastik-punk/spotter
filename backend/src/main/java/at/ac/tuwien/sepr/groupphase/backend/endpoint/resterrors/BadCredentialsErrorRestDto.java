package at.ac.tuwien.sepr.groupphase.backend.endpoint.resterrors;

import java.util.List;

public record BadCredentialsErrorRestDto(
    String message,
    List<String> errors
) {
}