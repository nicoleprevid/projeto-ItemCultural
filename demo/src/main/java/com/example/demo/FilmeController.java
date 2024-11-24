package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
class FilmeController {

    @Autowired
    private FilmeRepo filmeRepo;

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

    // Cria um novo filme
    @PostMapping("/api/filmes")
    Filme createFilme(@RequestBody Filme filme) {
        return filmeRepo.save(filme);
    }

    // Atualiza os dados de um filme pelo ID
    @PutMapping("/api/filmes/{filmeId}")
    Filme updateFilme(@RequestBody Filme filmeRequest, @PathVariable long filmeId) {
        return filmeRepo.findById(filmeId)
            .map(filme -> {
                // Atualiza os campos do filme existente
                filme.setNome(filmeRequest.getNome());
                filme.setTempoDeDuracao(filmeRequest.getTempoDeDuracao());
                filme.setAnoLancamento(filmeRequest.getAnoLancamento());
                filme.setGenero(filmeRequest.getGenero());
                return filmeRepo.save(filme); // Salva as alterações
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
