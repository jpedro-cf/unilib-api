package com.unilib.api.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class AWSConfig {
    @Value("${aws.access}")
    private String accessKey;
    @Value("${aws.secret}")
    private String secretKey;
    @Value("${aws.region}")
    private String region;

    @Value("${api.dev}")
    private boolean devMode;

    @Bean
    public S3Client s3Client(){
        S3ClientBuilder builder = S3Client
                .builder()
                .credentialsProvider(credentialsProvider())
                .region(Region.of(region));

        //localstack
        if(devMode){
            builder.endpointOverride(URI.create("http://localhost:4566"));
        }

        builder.forcePathStyle(true);

        return builder.build();
    }

    @Bean
    public S3Presigner presigner(){

        S3Presigner.Builder builder = S3Presigner.builder()
                .credentialsProvider(credentialsProvider())
                .region(Region.of(region));

        //localstack
        if(devMode){
            builder.endpointOverride(URI.create("http://localhost:4566"));
        }

        builder.serviceConfiguration(S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build());

        return builder.build();
    }

    private StaticCredentialsProvider credentialsProvider(){
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
        );
    }
}
