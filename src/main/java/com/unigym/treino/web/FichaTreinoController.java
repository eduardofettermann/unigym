package com.unigym.treino.web;

import com.unigym.treino.service.FichaTreinoService;
import com.unigym.treino.web.dto.AtualizarAtivacaoFichaRequest;
import com.unigym.treino.web.dto.AtualizarCheckSerieRequest;
import com.unigym.treino.web.dto.FichaRequest;
import com.unigym.treino.web.dto.FichaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fichas")
public class FichaTreinoController {

    private final FichaTreinoService fichaTreinoService;

    public FichaTreinoController(FichaTreinoService fichaTreinoService) {
        this.fichaTreinoService = fichaTreinoService;
    }

    @PostMapping
    public ResponseEntity<FichaResponse> criar(@Valid @RequestBody FichaRequest request, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fichaTreinoService.criar(authentication.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<FichaResponse>> listar(
            @RequestParam(defaultValue = "false") boolean incluirInativas,
            Authentication authentication
    ) {
        return ResponseEntity.ok(fichaTreinoService.listar(authentication.getName(), incluirInativas));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FichaResponse> editar(
            @PathVariable Long id,
            @Valid @RequestBody FichaRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(fichaTreinoService.atualizar(authentication.getName(), id, request));
    }

    @PatchMapping("/{id}/ativacao")
    public ResponseEntity<FichaResponse> ativarDesativar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarAtivacaoFichaRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(fichaTreinoService.atualizarAtivacao(authentication.getName(), id, request.ativa()));
    }

    @PatchMapping("/{fichaId}/series/{serieId}/check")
    public ResponseEntity<FichaResponse> atualizarCheck(
            @PathVariable Long fichaId,
            @PathVariable Long serieId,
            @Valid @RequestBody AtualizarCheckSerieRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(fichaTreinoService.atualizarCheckSerie(
                authentication.getName(),
                fichaId,
                serieId,
                request.concluida())
        );
    }
}
