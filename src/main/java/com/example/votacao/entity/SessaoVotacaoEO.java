package com.example.votacao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Fabio Moraes
 */
@Entity
public class SessaoVotacaoEO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pauta_id")
    private PautaEO pauta;

    @Column(name = "data_abertura")
    private LocalDateTime dataAbertura;

    @Column(name = "duracao_em_minutos")
    private Long duracaoEmMinutos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PautaEO getPauta() {
        return pauta;
    }

    public void setPauta(PautaEO pauta) {
        this.pauta = pauta;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    @NotNull
    public Long getDuracaoEmMinutos() {
//        return duracaoEmMinutos;
        return Optional.ofNullable(duracaoEmMinutos).orElse(0L);
    }

    public void setDuracaoEmMinutos(Long duracaoEmMinutos) {
        this.duracaoEmMinutos = duracaoEmMinutos;
    }

}
