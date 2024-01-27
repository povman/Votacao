package com.example.votacao;

import com.example.votacao.entity.PautaEO;
import com.example.votacao.entity.SessaoVotacaoEO;
import com.example.votacao.repository.PautaRepository;
import com.example.votacao.service.PautaService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Fabio Moraes
 */
@SpringBootTest
public class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @Test
    public void testAbrirSessaoVotacao() {
        // Configurar o comportamento do mock
        PautaEO pauta = new PautaEO();
        pauta.setId(1L);

        Mockito.when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        Mockito.when(pautaRepository.save(any(PautaEO.class))).thenReturn(pauta);

        // Chamar o método do serviço
        ResponseEntity<Map<String, Object>> responseEntity = pautaService.abrirSessaoVotacao(1L, 10L);

        // Verificar se a resposta é a esperada
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Sessão de votação aberta com sucesso.", responseEntity.getBody().get("mensagem"));

        // Verificar se o método do repositório foi chamado corretamente
        Mockito.verify(pautaRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(pautaRepository, Mockito.times(1)).save(any(PautaEO.class));
    }

    @Test
    public void testIsSessaoVotacaoFechada() {
        // Configurar o comportamento do mock
        PautaEO pauta = new PautaEO();
        pauta.setId(1L);

        SessaoVotacaoEO sessaoVotacao = new SessaoVotacaoEO();
        sessaoVotacao.setDataAbertura(LocalDateTime.now().minusMinutes(5));
        pauta.setSessaoVotacao(sessaoVotacao);

        Mockito.when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        // Chamar o método do serviço
        boolean votacaoAberta = pautaService.isSessaoVotacaoAberta(1L);

        // Verificar se a resposta é a esperada
        assertEquals(false, votacaoAberta);

        // Verificar se o método do repositório foi chamado corretamente
        Mockito.verify(pautaRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void testListarPautas() {
        // Mockando o comportamento do repositório para retornar uma lista de pautas fictícia
        PautaEO pauta1 = new PautaEO();
        pauta1.setId(1L);
        pauta1.setDescricao("Pauta 1");

        PautaEO pauta2 = new PautaEO();
        pauta2.setId(2L);
        pauta2.setDescricao("Pauta 2");

        List<PautaEO> listaPautas = Arrays.asList(pauta1, pauta2);
        Mockito.when(pautaRepository.findAll()).thenReturn(listaPautas);

        // Chama o método do serviço
        List<PautaEO> resultado = pautaService.listarPautas();

        // Verifica se o resultado não é nulo
        assertNotNull(resultado);

        // Verifica se o tamanho do resultado é o esperado
        assertEquals(2, resultado.size());
    }
    
    

}
