package com.example.votacao.controller;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author fabio
 */
@RestController
@RequestMapping("/")
public class CpfController {

    @RequestMapping("/api/valida-cpf")
    public ResponseEntity<Map<String, Object>> verificar(@RequestBody Map<String, String> requestBody) {
        Map<String, Object> response = new HashMap<>();
        Logger logger = LoggerFactory.getLogger(CpfController.class);

        try {
            String cpf = requestBody.get("cpf");

            if (cpf != null && !cpf.isEmpty()) {
                Boolean isValid = isValidCPF(cpf);

                if (isValid) {
                    response.put("status", "success");
                    response.put("mensagem", "CPF válido");
                    response.put("cpf", cpf);
                    return ResponseEntity.ok(response);
                } else {
                    response.put("status", "invalid");
                    response.put("mensagem", "CPF inválido");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            } else {
                response.put("status", "blank");
                response.put("mensagem", "É necessário enviar um CPF para pesquisa.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            logger.error("Erro interno ao processar a solicitação.", e);
            response.put("status", "error");
            response.put("mensagem", "Erro interno ao processar a solicitação.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @RequestMapping("/api/cria-cpf")
public ResponseEntity<Map<String, Object>> criarCPF() {
    Map<String, Object> response = new HashMap<>();
    String cpf;
    String cpfformatado = "";

    try {
        cpf = generateCPF();
        cpfformatado = formatarCPF(cpf);
        
        response.put("status", "success");
        response.put("message", "CPF criado com sucesso");
        response.put("cpf", cpf);
        response.put("cpf_formatado", cpfformatado);
        return ResponseEntity.ok(response);
    } catch (IllegalArgumentException ex) {
        response.put("status", "error");
        response.put("message", "Erro ao criar um novo CPF: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    } catch (Exception ex) {
        response.put("status", "error");
        response.put("message", "Erro interno ao processar a solicitação: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

    public static boolean isValidCPF(String cpf) {
        cpf = cpf.replaceAll("[^\\d]", "");
        if (cpf.length() != 11) {
            return false;
        }
        if (cpf.matches("(\\d)\\1*")) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int firstDigit = 11 - (sum % 11);
        if (firstDigit > 9) {
            firstDigit = 0;
        }
        if (Character.getNumericValue(cpf.charAt(9)) != firstDigit) {
            return false;
        }
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int secondDigit = 11 - (sum % 11);
        if (secondDigit > 9) {
            secondDigit = 0;
        }
        return Character.getNumericValue(cpf.charAt(10)) == secondDigit;
    }

    public static String generateCPF() {
        StringBuilder cpfBuilder = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            cpfBuilder.append(random.nextInt(10));
        }
        String cpfSemDigitosVerificadores = cpfBuilder.toString();
        int firstDigit = calculateDigit(cpfSemDigitosVerificadores);
        cpfBuilder.append(firstDigit);
        int secondDigit = calculateDigit(cpfBuilder.toString());
        cpfBuilder.append(secondDigit);

        return cpfBuilder.toString();
    }

    private static int calculateDigit(String cpf) {
        int sum = 0;
        int weight = cpf.length() + 1;

        for (char digitChar : cpf.toCharArray()) {
            int digit = Character.getNumericValue(digitChar);
            sum += digit * weight;
            weight--;
        }

        int remainder = sum % 11;
        int digit = 11 - remainder;

        return digit > 9 ? 0 : digit;
    }

    private String formatarCPF(String cpf) {
        MessageFormat mf = new MessageFormat("{0}.{1}.{2}-{3}");
        Object[] args = {cpf.substring(0, 3), cpf.substring(3, 6), cpf.substring(6, 9), cpf.substring(9)};
        return mf.format(args);
    }

}
