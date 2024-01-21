package com.example.votacao.service;


import com.example.votacao.entity.PautaEO;
import com.example.votacao.entity.VotoEO;
import com.example.votacao.repository.PautaRepository;
import com.example.votacao.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service 
public class VotoService {
    
    @Autowired
    private VotoRepository votoRepository;
    
    @Autowired
    private PautaRepository pautaRepository;
    
    @Cacheable(value = "votoCache")
    public List<?> listarVotos() {
        return votoRepository.findAll();
    }
    
    
    public boolean votoJaRegistrado(String cpf, Long pautaId) {
        return votoRepository.existsByCpfAndPautaId(cpf, pautaId);
    }
    
    @CacheEvict(value = "votoCache", allEntries = true)
public ResponseEntity<Map<String, Object>> salvarVoto(Boolean voto, String cpf, Long pautaId) {
    try {
        PautaEO pauta = pautaRepository.findById(pautaId).orElseThrow(() -> new RuntimeException("Pauta não encontrada"));
        VotoEO newVoto = new VotoEO();
        newVoto.setCpf(cpf);
        newVoto.setVoto(voto);
        newVoto.setPauta(pauta);

        votoRepository.save(newVoto);

        Map<String, Object> response = new HashMap<>();
        response.put("id", newVoto.getId());
        response.put("pauta_id", newVoto.getPauta().getId()); // Alterado para obter o ID da Pauta
        response.put("cpf", newVoto.getCpf());
        response.put("mensagem", "Voto registrado com sucesso!");
        response.put("cpf", cpf);

        return ResponseEntity.ok(response);
    } catch (Exception e) {
        throw new RuntimeException("Erro ao salvar o voto no banco de dados", e);
    }
}

public long contarVotos(Boolean voto, Long pautaId) {
    Long resultado = votoRepository.countByVotoAndPautaId(voto, pautaId);
    return resultado != null ? resultado : 0;
}

    
    
}
