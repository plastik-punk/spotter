package at.ac.tuwien.sepr.groupphase.backend.endpoint.resterrors;

import java.util.List;

public record ConflictErrorRestDto(
    String message,
    List<String> errors
) {
}
