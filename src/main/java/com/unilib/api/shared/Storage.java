package com.unilib.api.shared;

import java.util.Map;

public interface Storage {
    String generateSignedUrl(String key);
    void uploadObject(String key, byte[] fileBytes, Map<String, String> metadata);
}
