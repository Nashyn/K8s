package com.docker.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedWriter; 
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@SpringBootApplication
public class CalculatorService {

    private static final String DATA_PATH = "/nisarg_PV_dir";

    public static void main(String[] args) {
        SpringApplication.run(CalculatorService.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RestController
    public static class CalculatorController {

        private final RestTemplate restTemplate;

        public CalculatorController(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        @PostMapping("/calculate")
        public ResponseEntity<Map<String, Object>> calculate(@RequestBody Map<String, Object> json_data) {
            Map<String, Object> response = new HashMap<>();
            String fileName = (String) json_data.get("file");
            if (fileName == null) {
                response.put("file", null);
                response.put("error", "Invalid JSON input.");
                return ResponseEntity.badRequest().body(response);
            }

            File file = new File(DATA_PATH, fileName);
            if (!file.exists()) {
                response.put("file", fileName);
                response.put("error", "File not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        
            String url = "http://validator-network-service/calculateAmount";
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(json_data, headers);

                ResponseEntity<Map<String, Object>> exchangeResponse = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        requestEntity,
                        new ParameterizedTypeReference<>() {}
                );

                return exchangeResponse;
            } catch (Exception e) {
                response.put("file", fileName);
               response.put("error", "Error communicating with the calculation service.");
                return ResponseEntity.internalServerError().body(response);
            }

            }

            @PostMapping("/store-file")
            public ResponseEntity<Map<String, String>> storeFile(@RequestBody Map<String, String> requestData) {
                Map<String, String> response = new HashMap<>();
                String fileName = requestData.get("file");
                String data = requestData.get("data");
    
                if (fileName == null || data == null) {
                    response.put("file", null);
                    response.put("error", "Invalid JSON input.");
                    return ResponseEntity.badRequest().body(response);
                }
    
                try {
                    File directory = new File(DATA_PATH);
                    if (!directory.exists()) {
                        boolean dirsCreated = directory.mkdirs();
                        if (!dirsCreated) {
                            response.put("error", "Could not create storage directory.");
                            return ResponseEntity.internalServerError().body(response);
                        }
                    }
    
                    Path filePath = Paths.get(DATA_PATH, fileName);
                    try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                        writer.write(data);
                    }
    
                    response.put("file", fileName);
                    response.put("message", "Success.");
                    return ResponseEntity.ok(response);
                } catch (Exception e) {
                    response.put("file", fileName);
                    response.put("error", "Error while storing the file to the storage.");
                    return ResponseEntity.internalServerError().body(response);
                }
        }
        
    }

}
