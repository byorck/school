package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

@RestController
public class InfoController {
    @Value("${server.port}")
    private Integer serverPort;

    @GetMapping("/port")
    @Operation(summary = "Получить значение текущего порта")
    public ResponseEntity<Integer> getPort() {
        return ResponseEntity.ok(serverPort);
    }

    @GetMapping("/sum")
    @Operation(summary = "Получить сумму арифметической прогрессии от 1 до 1.000.000")
    public ResponseEntity<Integer> getSum() {
        int sum = IntStream.rangeClosed(1, 1000000).parallel().sum();
        return ResponseEntity.ok(sum);
    }
}
