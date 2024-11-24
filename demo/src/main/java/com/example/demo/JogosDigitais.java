package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class JogosDigitais {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;
    private int idadeMinima;
    private int qtdeMinimaJogadores;
    private int qtdeMaximaJogadores;
    private int duracaoMediaMinutos;
    private int anoLancamento;
    private String area;

    public JogosDigitais() {
        super();
    }

    // Getter e Setter para id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    // Getter e Setter para nome
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Getter e Setter para idadeMinima
    public int getIdadeMinima() {
        return idadeMinima;
    }

    public void setIdadeMinima(int idadeMinima) {
        this.idadeMinima = idadeMinima;
    }

    // Getter e Setter para qtdeMinimaJogadores
    public int getQtdeMinimaJogadores() {
        return qtdeMinimaJogadores;
    }

    public void setQtdeMinimaJogadores(int qtdeMinimaJogadores) {
        this.qtdeMinimaJogadores = qtdeMinimaJogadores;
    }

    // Getter e Setter para qtdeMaximaJogadores
    public int getQtdeMaximaJogadores() {
        return qtdeMaximaJogadores;
    }

    public void setQtdeMaximaJogadores(int qtdeMaximaJogadores) {
        this.qtdeMaximaJogadores = qtdeMaximaJogadores;
    }

    // Getter e Setter para duracaoMediaMinutos
    public int getDuracaoMediaMinutos() {
        return duracaoMediaMinutos;
    }

    public void setDuracaoMediaMinutos(int duracaoMediaMinutos) {
        this.duracaoMediaMinutos = duracaoMediaMinutos;
    }

    // Getter e Setter para anoLancamento
    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    // Getter e Setter para area
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
