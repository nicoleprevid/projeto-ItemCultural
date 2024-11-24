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
class JogosDigitaisController {

	@Autowired
	private JogosDigitaisRepo jogosDigitaisRepo;

	public JogosDigitaisController() {

	}

	@GetMapping("/api/jogos")
	Iterable<JogosDigitais> getJogos(@RequestParam Optional<String> area) {
		if (area.isPresent()) {
			return jogosDigitaisRepo.findByArea(area.get());
		}
		return jogosDigitaisRepo.findAll();
	}

	@GetMapping("/api/jogos/{id}")
	Optional<JogosDigitais> getJogo(@PathVariable long id) {
		return jogosDigitaisRepo.findById(id);
	}

	@PostMapping("/api/jogos")
	JogosDigitais createJogo(@RequestBody JogosDigitais jogo) {
		JogosDigitais createdJogo = jogosDigitaisRepo.save(jogo);
		return createdJogo;
	}
	@PutMapping("/api/jogos/{id}")
	JogosDigitais updateJogo(@RequestBody JogosDigitais jogoRequest, @PathVariable long id) {
		// Busca o jogo pelo ID
		System.out.println("Chamou updateJogo");
		return jogosDigitaisRepo.findById(id)
			.map(jogo -> {
				// Atualiza os campos do jogo existente
				System.out.println("Chamou updateJogo com " + jogo);
				jogo.setIdadeMinima(jogoRequest.getIdadeMinima());
				jogo.setQtdeMinimaJogadores(jogoRequest.getQtdeMinimaJogadores());
				jogo.setQtdeMaximaJogadores(jogoRequest.getQtdeMaximaJogadores());
				jogo.setDuracaoMediaMinutos(jogoRequest.getDuracaoMediaMinutos());
				jogo.setAnoLancamento(jogoRequest.getAnoLancamento());
				jogo.setArea(jogoRequest.getArea());
				return jogosDigitaisRepo.save(jogo); // Salva as alterações
			})
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
					"Jogo com ID " + id + " não encontrado."));
	}
	
	@DeleteMapping("/api/jogos/{id}")
	void deleteJogo(@PathVariable long id) {
		jogosDigitaisRepo.deleteById(id);
	}
}
