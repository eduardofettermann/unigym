package com.unigym.atleta.web;

import com.unigym.atleta.domain.Atleta;
import com.unigym.atleta.service.AtletaService;
import com.unigym.atleta.service.JwtService;
import com.unigym.atleta.web.dto.ApiErrorResponse;
import com.unigym.atleta.web.dto.AtletaResponse;
import com.unigym.atleta.web.dto.CadastroAtletaRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/atletas")
@Tag(name = "Atletas", description = "Operações de cadastro de atletas.")
public class AtletaController {

    private final AtletaService atletaService;
    private final JwtService jwtService;

    public AtletaController(AtletaService atletaService, JwtService jwtService) {
        this.atletaService = atletaService;
        this.jwtService = jwtService;
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar atleta",
            description = "Cria um novo atleta, autentica a sessão do usuário e retorna os dados públicos do cadastro.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Atleta cadastrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AtletaResponse.class),
                            examples = {@ExampleObject(
                                    name = "Atleta criado",
                                    value = """
                                            {
                                              "id": 1,
                                              "nome": "Ana Silva",
                                              "email": "ana@exemplo.com",
                                              "dataNascimento": "1995-04-12",
                                              "peso": 62.5,
                                              "altura": 1.70,
                                              "nivelExperiencia": "INTERMEDIARIO"
                                            }
                                            """)})),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(
                                    name = "Erro de validação",
                                    value = """
                                            {
                                              "message": "Dados inválidos",
                                              "fieldErrors": {
                                                "email": "E-mail inválido",
                                                "senha": "Senha deve ter no mínimo 8 caracteres"
                                              }
                                            }
                                            """)})),
            @ApiResponse(
                    responseCode = "409",
                    description = "E-mail já cadastrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {@ExampleObject(
                                    name = "E-mail duplicado",
                                    value = """
                                            {
                                              "message": "E-mail já cadastrado: ana@exemplo.com",
                                              "fieldErrors": {
                                                "email": "E-mail já em uso"
                                              }
                                            }
                                            """)}))
    })
    public ResponseEntity<AtletaResponse> cadastrar(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados para cadastro do atleta.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CadastroAtletaRequest.class),
                            examples = {@ExampleObject(
                                    name = "Cadastro válido",
                                    value = """
                                            {
                                              "nome": "Ana Silva",
                                              "email": "ana@exemplo.com",
                                              "senha": "senhaSegura",
                                              "dataNascimento": "1995-04-12",
                                              "peso": 62.5,
                                              "altura": 1.70,
                                              "nivelExperiencia": "INTERMEDIARIO"
                                            }
                                            """)}))
            @RequestBody CadastroAtletaRequest request) {
        Atleta atleta = atletaService.cadastrar(request);
        Authentication authentication = atletaService.autenticar(atleta);
        String token = jwtService.gerarToken(authentication);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(AtletaResponse.from(atleta));
    }
}
