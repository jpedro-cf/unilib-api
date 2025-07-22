package com.unilib.api.shared;

import com.unilib.api.shared.exceptions.ApplicationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedUploadPartRequest;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Map;

@Component
public class AmazonS3 implements Storage {
    @Value("${aws.bucket}")
    private String bucketName;

    private final S3Client client;
    private final S3Presigner presigner;

    public AmazonS3(S3Client client, S3Presigner presigner){
        this.client = client;
        this.presigner = presigner;
    }

    @Override
    public String generateSignedUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(getObjectRequest)
                .build();

        return presigner.presignGetObject(presignRequest).url().toString();
    }

    @Override
    public void uploadObject(String key, byte[] fileBytes, Map<String, String> metadata) {
        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .metadata(metadata)
                    .build();

            RequestBody body = RequestBody.fromByteBuffer(ByteBuffer.wrap(fileBytes));
            client.putObject(putRequest, body);
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }
}
