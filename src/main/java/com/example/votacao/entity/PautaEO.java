package com.example.votacao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 *
 * @author Fabio Moraes
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, region = "pautaCache")
public class PautaEO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    
    @JsonIgnore
    @OneToMany(mappedBy = "pauta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VotoEO> votos = new ArrayList<>();
    
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sessao_votacao_id", referencedColumnName = "id")
    private SessaoVotacaoEO sessaoVotacao;
    
    public PautaEO() {
    }

   public PautaEO(String descricao) {
    this.descricao = descricao;
}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public List<VotoEO> getVotos() {
        return votos;
    }

    public void setVotos(List<VotoEO> votos) {
        this.votos = votos;
    }

    public SessaoVotacaoEO getSessaoVotacao() {
        return sessaoVotacao;
    }

    public void setSessaoVotacao(SessaoVotacaoEO sessaoVotacao) {
        this.sessaoVotacao = sessaoVotacao;
    }
    
    
    
    @Override
public String toString() {
    return "Pauta{" +
           "id=" + id +
           ", descricao='" + descricao + '\'' +
           ", votos=" + votos +
           '}';
}
    
}
