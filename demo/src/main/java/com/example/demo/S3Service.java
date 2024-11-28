package com.example.demo;


import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.nio.file.Path;

import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName = "projeto-bucket"; 

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
    // Novo método para obter arquivos
    public ResponseEntity<byte[]> getFile(String fileName) {
        try {
            // Monta a requisição para o S3
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            // Faz o download do objeto
            ResponseInputStream<GetObjectResponse> objectInputStream = s3Client.getObject(getObjectRequest);

            // Salva em um array de bytes
            byte[] content = objectInputStream.readAllBytes();

            // Determina o tipo de mídia
            MediaType mediaType = guessMediaType(fileName);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(content);
        } catch (S3Exception | IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    private MediaType guessMediaType(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (fileName.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (fileName.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else if (fileName.endsWith(".pdf")) {
            return MediaType.APPLICATION_PDF;
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }

}
