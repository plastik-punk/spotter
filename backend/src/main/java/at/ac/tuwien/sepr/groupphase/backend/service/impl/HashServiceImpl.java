package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.service.HashService;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class HashServiceImpl implements HashService {
    @Override
    public String hashSha256(String input) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return DatatypeConverter.printHexBinary(hash).toUpperCase();
    }
}
