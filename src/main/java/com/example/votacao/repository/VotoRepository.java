/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.votacao.repository;

import com.example.votacao.entity.VotoEO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Fabio Moraes
 */
public interface VotoRepository extends JpaRepository<VotoEO, Long> {
    
    boolean existsByCpfAndPautaId(String cpf, Long pautaId);
    
    long countByVotoAndPautaId(Boolean voto, Long pautaId);
    
}
