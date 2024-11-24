package com.example.demo;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import java.nio.file.Path;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName = "nome-do-seu-bucket"; // Substitua pelo nome do seu bucket

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(String fileName, Path filePath) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            s3Client.putObject(putObjectRequest, filePath);
            return "Upload concluído!";
        } catch (S3Exception e) {
            e.printStackTrace();
            return "Erro no upload: " + e.getMessage();
        }
    }
}
