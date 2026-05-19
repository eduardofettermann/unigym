package com.unigym.controller;

import com.unigym.model.Atleta;
import com.unigym.service.AtletaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atletas")
@RequiredArgsConstructor
public class AtletaController {

    private final AtletaService atletaService;

    @PostMapping
    public ResponseEntity<Atleta> cadastrar(@RequestBody Atleta atleta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(atletaService.cadastrar(atleta));
    }

    @GetMapping
    public ResponseEntity<List<Atleta>> listarTodos() {
        return ResponseEntity.ok(atletaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Atleta> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(atletaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Atleta> atualizar(@PathVariable Long id, @RequestBody Atleta atleta) {
        return ResponseEntity.ok(atletaService.atualizar(id, atleta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        atletaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}