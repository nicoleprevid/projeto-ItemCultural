package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Brincadeira{
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;
    private int qtdeMinimaJogadores;
    private int qtdeMaximaJogadores;
    private String habilidadePrincipal;

    public Brincadeira() {
        super();
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQtdeMinimaJogadores() {
        return qtdeMinimaJogadores;
    }

    public void setQtdeMinimaJogadores(int qtdeMinimaJogadores) {
        this.qtdeMinimaJogadores = qtdeMinimaJogadores;
    }

    public int getQtdeMaximaJogadores() {
        return qtdeMaximaJogadores;
    }

    public void setQtdeMaximaJogadores(int qtdeMaximaJogadores) {
        this.qtdeMaximaJogadores = qtdeMaximaJogadores;
    }

    public String getHabilidadePrincipal() {
        return habilidadePrincipal;
    }

    public void setHabilidadePrincipal(String habilidadePrincipal) {
        this.habilidadePrincipal = habilidadePrincipal;
    }
}
