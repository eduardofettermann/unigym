package com.unigym.atleta.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.unigym.atleta.domain.Atleta;
import com.unigym.atleta.repository.AtletaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class AtletaControllerTest {

	@Autowired
	private AtletaRepository atletaRepository;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		atletaRepository.deleteAll();
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}

	@Test
	void deveCadastrarEAutenticarNaSessao() throws Exception {
		String json = """
				{
					"nome": "Ana Silva",
					"email": "ana@exemplo.com",
					"senha": "senhaSegura",
					"dataNascimento": "1995-04-12",
					"peso": 62.5,
					"altura": 1.70,
					"nivelExperiencia": "INTERMEDIARIO"
				}
				""";

		var result = mockMvc.perform(post("/api/atletas")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.email").value("ana@exemplo.com"))
				.andExpect(jsonPath("$.nivelExperiencia").value("INTERMEDIARIO"))
				.andExpect(jsonPath("$.senha").doesNotExist())
				.andReturn();

		assertThat(result.getRequest().getSession(false))
				.isNotNull()
				.extracting(session -> session.getAttribute("SPRING_SECURITY_CONTEXT"))
				.isNotNull();

		Atleta atletaPersistido = atletaRepository.findAll().getFirst();
		assertThat(atletaPersistido.getSenha()).isNotEqualTo("senhaSegura");
		assertThat(atletaPersistido.getSenha()).startsWith("$2");
	}

	@Test
	void deveRejeitarPayloadInvalido() throws Exception {
		String json = """
				{
					"nome": "",
					"email": "email-invalido",
					"senha": "123",
					"peso": -1,
					"altura": 0,
					"nivelExperiencia": null
				}
				""";

		mockMvc.perform(post("/api/atletas")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.fieldErrors.nome").value("Nome é obrigatório"));
	}

	@Test
	void deveRejeitarEmailDuplicado() throws Exception {
		String json = """
				{
					"nome": "Ana Silva",
					"email": "ana@exemplo.com",
					"senha": "senhaSegura",
					"dataNascimento": "1995-04-12",
					"peso": 62.5,
					"altura": 1.70,
					"nivelExperiencia": "INICIANTE"
				}
				""";

		mockMvc.perform(post("/api/atletas")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isCreated());

		mockMvc.perform(post("/api/atletas")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.fieldErrors.email").value("E-mail já em uso"));
	}

	@Test
	void deveExigirAutenticacaoNosOutrosEndpoints() throws Exception {
		mockMvc.perform(get("/api/atletas"))
				.andExpect(status().isUnauthorized());
	}
}
