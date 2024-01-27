package com.example.votacao;

import com.example.votacao.entity.PautaEO;
import com.example.votacao.repository.PautaRepository;
import com.example.votacao.service.PautaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PautaServicePerformanceTest {

    private static final int NUM_EXECUCOES = 1000;

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @Test
    public void testPerformanceListarPautasComCache() {
        // Configura o Mock 
        List<PautaEO> pautas = Arrays.asList(new PautaEO(), new PautaEO());
        when(pautaRepository.findAll()).thenReturn(pautas);

        // Executa o método várias vezes para avaliar o desempenho
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < NUM_EXECUCOES; i++) {
            List<PautaEO> resultadoComCache = pautaService.listarPautas();
            assertEquals(pautas, resultadoComCache);  // Garanta que o resultado seja o esperado
            // Pequeno atraso entre as execuções
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        }
        long endTime = System.currentTimeMillis();

        long tempoTotal = endTime - startTime;
        System.out.println("Tempo total para " + NUM_EXECUCOES + " execuções: " + tempoTotal + " milissegundos");
    }
}
