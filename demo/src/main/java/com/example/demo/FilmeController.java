package com.example.demo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
class FilmeController {

    @Autowired
    private FilmeRepo filmeRepo;

    @Autowired
    private S3Service s3Service;

    public FilmeController() {
    }

    // Recupera todos os filmes ou por filtro opcional de ano de lançamento
    @GetMapping("/api/filmes")
    Iterable<Filme> getFilmes(@RequestParam Optional<Integer> anoLancamento) {
        if (anoLancamento.isPresent()) {
            return filmeRepo.findByAnoLancamento(anoLancamento.get());
        }
        return filmeRepo.findAll();
    }

    // Recupera um filme específico pelo ID
    @GetMapping("/api/filmes/{id}")
    Optional<Filme> getFilme(@PathVariable long id) {
        return filmeRepo.findById(id);
    }

    @PostMapping("/api/filmes")
    Filme createFilme(@RequestParam("nome") String nome,
                    @RequestParam("tempoDeDuracao") String tempoDeDuracao,
                    @RequestParam("anoLancamento") Integer anoLancamento,
                    @RequestParam("genero") String genero,
                    @RequestParam(value = "imagem", required = false) MultipartFile imagem) {
        String imagemUrl = null;

        if (imagem != null && !imagem.isEmpty()) {
            try {
                // Upload da imagem para o S3
                String fileName = "filmes/" + imagem.getOriginalFilename();
                Path tempFile = Files.createTempFile("upload-", imagem.getOriginalFilename());
                imagem.transferTo(tempFile.toFile());
                s3Service.uploadFile(fileName, tempFile);
                imagemUrl = "https://nome-do-seu-bucket.s3.amazonaws.com/" + fileName;
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao fazer upload da imagem", e);
            }
        }

        // Criação do filme
        Filme novoFilme = new Filme();
        novoFilme.setNome(nome);
        novoFilme.setTempoDeDuracao(tempoDeDuracao);
        novoFilme.setAnoLancamento(anoLancamento);
        novoFilme.setGenero(genero);
        novoFilme.setImagemUrl(imagemUrl);

        return filmeRepo.save(novoFilme);
    }


    @PutMapping("/api/filmes/{filmeId}")
    Filme updateFilme(@PathVariable long filmeId,
                    @RequestParam("nome") String nome,
                    @RequestParam("tempoDeDuracao") String tempoDeDuracao,
                    @RequestParam("anoLancamento") Integer anoLancamento,
                    @RequestParam("genero") String genero,
                    @RequestParam(value = "imagem", required = false) MultipartFile imagem) {
        return filmeRepo.findById(filmeId)
            .map(filme -> {
                String imagemUrl = filme.getImagemUrl();

                if (imagem != null && !imagem.isEmpty()) {
                    try {
                        // Upload da nova imagem para o S3
                        String fileName = "filmes/" + imagem.getOriginalFilename();
                        Path tempFile = Files.createTempFile("upload-", imagem.getOriginalFilename());
                        imagem.transferTo(tempFile.toFile());
                        s3Service.uploadFile(fileName, tempFile);
                        imagemUrl = "https://nome-do-seu-bucket.s3.amazonaws.com/" + fileName;
                    } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao fazer upload da imagem", e);
                    }
                }

                // Atualiza os campos do filme existente
                filme.setNome(nome);
                filme.setTempoDeDuracao(tempoDeDuracao);
                filme.setAnoLancamento(anoLancamento);
                filme.setGenero(genero);
                filme.setImagemUrl(imagemUrl);

                return filmeRepo.save(filme);
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Filme com ID " + filmeId + " não encontrado."));
    }

    // Deleta um filme pelo ID
    @DeleteMapping("/api/filmes/{id}")
    void deleteFilme(@PathVariable long id) {
        filmeRepo.deleteById(id);
    }
}
