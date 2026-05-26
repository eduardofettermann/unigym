package com.unigym.treino.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unigym.atleta.domain.Atleta;
import com.unigym.atleta.domain.NivelExperiencia;
import com.unigym.atleta.repository.AtletaRepository;
import com.unigym.treino.domain.Exercicio;
import com.unigym.treino.repository.ExercicioRepository;
import com.unigym.treino.repository.FichaTreinoRepository;
import com.unigym.treino.repository.SerieFichaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class FichaTreinoControllerTest {

    @Autowired
    private AtletaRepository atletaRepository;

    @Autowired
    private ExercicioRepository exercicioRepository;

    @Autowired
    private FichaTreinoRepository fichaTreinoRepository;

    @Autowired
    private SerieFichaRepository serieFichaRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private Atleta atleta1;
    private Atleta atleta2;
    private Exercicio supino;
    private Exercicio agachamento;

    @BeforeEach
    void setUp() {
        serieFichaRepository.deleteAll();
        fichaTreinoRepository.deleteAll();
        exercicioRepository.deleteAll();
        atletaRepository.deleteAll();

        atleta1 = atletaRepository.save(criarAtleta("atleta1@exemplo.com"));
        atleta2 = atletaRepository.save(criarAtleta("atleta2@exemplo.com"));

        supino = exercicioRepository.save(criarExercicio("Supino"));
        agachamento = exercicioRepository.save(criarExercicio("Agachamento"));

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void deveCriarFichaComNomePersonalizado() throws Exception {
        mockMvc.perform(post("/api/fichas")
                        .with(SecurityMockMvcRequestPostProcessors.user(atleta1.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadFicha("Treino A - Peito")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Treino A - Peito"))
                .andExpect(jsonPath("$.ativa").value(true))
                .andExpect(jsonPath("$.exercicios[0].series[0].repeticoes").value(10));
    }

    @Test
    void deveRejeitarPayloadInvalido() throws Exception {
        String invalido = """
                {
                  "nome": "",
                  "series": []
                }
                """;

        mockMvc.perform(post("/api/fichas")
                        .with(SecurityMockMvcRequestPostProcessors.user(atleta1.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Dados inválidos"));
    }

    @Test
    void deveListarSomenteAtivasPorPadraoEPermitirIncluirInativas() throws Exception {
        Long fichaAtiva = criarFicha("Treino A");
        Long fichaInativa = criarFicha("Treino B");

                mockMvc.perform(patch("/api/fichas/{id}/ativacao", fichaInativa)
                        .with(SecurityMockMvcRequestPostProcessors.user(atleta1.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ativa\": false}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/fichas")
                        .with(SecurityMockMvcRequestPostProcessors.user(atleta1.getEmail())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(fichaAtiva))
                .andExpect(jsonPath("$[1]").doesNotExist());

        mockMvc.perform(get("/api/fichas?incluirInativas=true")
                        .with(SecurityMockMvcRequestPostProcessors.user(atleta1.getEmail())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(fichaAtiva))
                .andExpect(jsonPath("$[1].id").value(fichaInativa));
    }

    @Test
    void deveEditarFicha() throws Exception {
        Long fichaId = criarFicha("Treino A");

        String payloadEdicao = """
                {
                  "nome": "Treino A - Atualizado",
                  "series": [
                    {
                      "exercicioId": %d,
                      "repeticoes": 12,
                      "cargaSugerida": 80.0,
                      "ordem": 1
                    }
                  ]
                }
                """.formatted(agachamento.getId());

        mockMvc.perform(put("/api/fichas/{id}", fichaId)
                        .with(SecurityMockMvcRequestPostProcessors.user(atleta1.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadEdicao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Treino A - Atualizado"))
                .andExpect(jsonPath("$.exercicios[0].nome").value("Agachamento"))
                .andExpect(jsonPath("$.exercicios[0].series[0].repeticoes").value(12));
    }

    @Test
    void deveMarcarCheckDaSerieEDerivarCheckDeExercicioETreino() throws Exception {
        Long fichaId = criarFichaComDuasSeriesMesmoExercicio();

        String fichaJson = mockMvc.perform(get("/api/fichas")
                        .with(SecurityMockMvcRequestPostProcessors.user(atleta1.getEmail())))
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode raiz = objectMapper.readTree(fichaJson);
        Long serie1 = raiz.get(0).get("exercicios").get(0).get("series").get(0).get("id").asLong();
        Long serie2 = raiz.get(0).get("exercicios").get(0).get("series").get(1).get("id").asLong();

        mockMvc.perform(patch("/api/fichas/{fichaId}/series/{serieId}/check", fichaId, serie1)
                        .with(SecurityMockMvcRequestPostProcessors.user(atleta1.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"concluida\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.treinoConcluido").value(false))
                .andExpect(jsonPath("$.exercicios[0].concluido").value(false));

        mockMvc.perform(patch("/api/fichas/{fichaId}/series/{serieId}/check", fichaId, serie2)
                        .with(SecurityMockMvcRequestPostProcessors.user(atleta1.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"concluida\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.treinoConcluido").value(true))
                .andExpect(jsonPath("$.exercicios[0].concluido").value(true));
    }

    @Test
    void deveImpedirAcessoAFichaDeOutroAtleta() throws Exception {
        Long fichaId = criarFicha("Treino A");

        mockMvc.perform(put("/api/fichas/{id}", fichaId)
                        .with(SecurityMockMvcRequestPostProcessors.user(atleta2.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadFicha("Tentativa")))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarCatalogoGlobalDeExercicios() throws Exception {
        mockMvc.perform(get("/api/exercicios")
                        .with(SecurityMockMvcRequestPostProcessors.user(atleta1.getEmail())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Agachamento"))
                .andExpect(jsonPath("$[1].nome").value("Supino"));
    }

    private Long criarFicha(String nome) throws Exception {
        String response = mockMvc.perform(post("/api/fichas")
                        .with(SecurityMockMvcRequestPostProcessors.user(atleta1.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadFicha(nome)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    private Long criarFichaComDuasSeriesMesmoExercicio() throws Exception {
        String payload = """
                {
                  "nome": "Treino C",
                  "series": [
                    {
                      "exercicioId": %d,
                      "repeticoes": 10,
                      "cargaSugerida": 40.0,
                      "ordem": 1
                    },
                    {
                      "exercicioId": %d,
                      "repeticoes": 8,
                      "cargaSugerida": 45.0,
                      "ordem": 2
                    }
                  ]
                }
                """.formatted(supino.getId(), supino.getId());

        String response = mockMvc.perform(post("/api/fichas")
                        .with(SecurityMockMvcRequestPostProcessors.user(atleta1.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    private String payloadFicha(String nome) {
        return """
                {
                  "nome": "%s",
                  "series": [
                    {
                      "exercicioId": %d,
                      "repeticoes": 10,
                      "cargaSugerida": 40.5,
                      "ordem": 1
                    },
                    {
                      "exercicioId": %d,
                      "repeticoes": 12,
                      "cargaSugerida": 60.0,
                      "ordem": 2
                    }
                  ]
                }
                """.formatted(nome, supino.getId(), agachamento.getId());
    }

    private Atleta criarAtleta(String email) {
        Atleta atleta = new Atleta();
        atleta.setNome("Atleta");
        atleta.setEmail(email);
        atleta.setSenha("$2a$10$abcdefghijklmnopqrstuv");
        atleta.setDataNascimento(LocalDate.of(1995, 1, 1));
        atleta.setPeso(BigDecimal.valueOf(70));
        atleta.setAltura(BigDecimal.valueOf(1.75));
        atleta.setNivelExperiencia(NivelExperiencia.INTERMEDIARIO);
        return atleta;
    }

    private Exercicio criarExercicio(String nome) {
        Exercicio exercicio = new Exercicio();
        exercicio.setNome(nome);
        return exercicio;
    }
}
