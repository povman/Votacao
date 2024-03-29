package com.example.votacao.controller;

import static com.example.votacao.controller.CpfController.isValidCPF;
import com.example.votacao.service.PautaService;
import com.example.votacao.service.VotoService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Fabio Moraes
 */

@RestController
@RequestMapping("/")
public class VotoController {
    
    @Autowired 
    private final VotoService votoService;
    
    @Autowired
    private final PautaService pautaService = null;

    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }
    
    @RequestMapping("/")
    public String getHome() {
        return "Pagina Inical dos Votos!";
    }
    
    @RequestMapping("/api/votos")
    public String getVotos() {
        return "Pegando Votos!";
    }
    
@RequestMapping("/api/votos/votar")
    public ResponseEntity<Map<String, Object>> votar(@RequestBody Map<String, String> requestBody) {
        try {
            String cpf = requestBody.get("cpf");
            String votoStr = requestBody.get("voto");
            Long pautaId = Long.valueOf(requestBody.get("pauta"));

            boolean sessaoAberta = pautaService.isSessaoVotacaoAberta(pautaId);

            if (sessaoAberta && cpf != null && !cpf.isEmpty() && votoStr != null && !votoStr.isEmpty()) {
                Boolean voto = votoStr.equalsIgnoreCase("sim");
                Boolean verVotoRegistrado = votoService.votoJaRegistrado(cpf, pautaId);
                Boolean checkedCPF = isValidCPF(cpf);
                if(checkedCPF == false){
                    Map<String, Object> response = new HashMap<>();
                    response.put("mensagem", "CPF é Inválido.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }else{
                if(verVotoRegistrado == false){
                    return votoService.salvarVoto(voto, cpf, pautaId);
                }else{
                    Map<String, Object> response = new HashMap<>();
                    response.put("mensagem", "O CPF já votou nesta Pauta.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
                }
            } else {
                Map<String, Object> response = new HashMap<>();
                if (!sessaoAberta) {
                    response.put("mensagem", "A sessão de votação para esta pauta está fechada.");
                } else {
                    response.put("mensagem", "O CPF, o Voto e o ID da Pauta são obrigatórios.");
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", "Erro interno ao processar a solicitação." + e.getCause() + " || " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
