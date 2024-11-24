package com.example.demo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrincadeiraRepo extends JpaRepository<Brincadeira, Long> {

    Iterable<Brincadeira> findByHabilidadePrincipal(String habilidadePrincipal);
}