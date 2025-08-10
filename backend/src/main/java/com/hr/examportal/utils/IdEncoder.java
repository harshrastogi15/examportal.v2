package com.hr.examportal.utils;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Service
public class IdEncoder {

    private byte[] uuidToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    private UUID bytesToUUID(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        return new UUID(bb.getLong(), bb.getLong());
    }

    private byte[] xorBytes(byte[] idBytes, byte[] keyBytes) {
        byte[] result = new byte[idBytes.length];
        for (int i = 0; i < idBytes.length; i++) {
            result[i] = (byte) (idBytes[i] ^ keyBytes[i % keyBytes.length]);
        }
        return result;
    }

    public UUID encodeId(UUID realId, UUID userId) {
        try {
            byte[] realIdBytes = uuidToBytes(realId);
            byte[] userIdBytes = uuidToBytes(userId);

            byte[] encodedBytes = xorBytes(realIdBytes, userIdBytes);
            return bytesToUUID(encodedBytes);
        } catch (Exception e) {
            throw new IllegalStateException("Error encoding UUID", e);
        }
    }

    public UUID decodeId(UUID encodedUUID, UUID userId) {
        try {
            return encodeId(encodedUUID, userId);
        }catch (Exception e) {
            throw new IllegalStateException("Error decoding UUID", e);
        }
    }
}
