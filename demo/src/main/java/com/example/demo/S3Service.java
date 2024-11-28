package com.example.demo;


import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Date;

import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

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
    public String generatePresignedUrl(String imagemUrl) {
        try {
            // Verifique se a imagemUrl não está vazia
            if (imagemUrl == null || imagemUrl.isEmpty()) {
                throw new IllegalArgumentException("URL da imagem não pode ser vazia");
            }

            // Extrair a chave do objeto (objectKey) da URL completa
            String objectKey = imagemUrl.replace("https://" + bucketName + ".s3.amazonaws.com/", "");

            // Criar um S3Presigner
            try (S3Presigner presigner = S3Presigner.create()) {

                // Criar a requisição de assinatura
                GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofHours(1)) // Expiração da URL
                        .getObjectRequest(req -> req.bucket(bucketName).key(objectKey)) // Definir bucket e chave
                        .build();

                // Gerar a URL pré-assinada
                PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);

                // Retornar a URL como String
                return presignedGetObjectRequest.url().toString();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log do erro
            throw new RuntimeException("Erro ao gerar URL pré-assinada: " + e.getMessage());
        }
    }

}
