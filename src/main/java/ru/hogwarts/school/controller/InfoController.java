package ru.hogwarts.school.controller;

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
    public ResponseEntity<Integer> getPort() {
        return ResponseEntity.ok(serverPort);
    }

    @GetMapping("/sum")
    public ResponseEntity<Integer> getSum() {
        int sum = IntStream.rangeClosed(1, 1000000).parallel().sum();
        return ResponseEntity.ok(sum);
    }
}
