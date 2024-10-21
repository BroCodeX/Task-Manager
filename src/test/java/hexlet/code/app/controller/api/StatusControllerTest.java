package hexlet.code.app.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.status.StatusDTO;
import hexlet.code.app.mapper.StatusMapper;
import hexlet.code.app.model.Status;
import hexlet.code.app.repository.StatusRepository;
import hexlet.code.app.service.StatusService;
import hexlet.code.app.util.ModelsGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StatusControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelsGenerator generator;

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private StatusService service;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private StatusMapper statusMapper;

	private JwtRequestPostProcessor token;

	private JwtRequestPostProcessor tokenFailed;

	private Status status;

	@BeforeEach
	void prepare() {
		token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

		tokenFailed = jwt().jwt(builder -> builder.subject("token@failed.test"));

		status = Instancio.of(generator.getStatusModel()).create();

		statusRepository.save(status);
	}

	@AfterEach
	void cleanRepo() {
		statusRepository.deleteById(status.getId());
	}

	@Test
	void getAllTest() throws Exception {
		var request = get("/api/task_statuses").with(token);
		var response = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andReturn();
		var body = response.getResponse().getContentAsString();

//		List<StatusDTO> statusDTOS = objectMapper.readValue(body, new TypeReference<>() { });
		List<Status> actual = objectMapper.readValue(body, new TypeReference<>() { });
		List<Status> expected = statusRepository.findAll();

		assertThatJson(body).isArray();
		assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
	}

	@Test
	void getByIdTest() throws Exception {
		long id  = status.getId();

		var request = get("/api/task_statuses/{id}", id).with(token);
		var response = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andReturn();
		var body = response.getResponse().getContentAsString();
		var testStatus = statusRepository.findById(id);

		assertThat(testStatus).isNotEmpty();
		assertThatJson(body).and(
				n -> n.node("name").isEqualTo(status.getName()),
				n -> n.node("slug").isEqualTo(status.getSlug())
		);

	}

	@Test
	void getByIdTestFailed() throws Exception {
		long id  = status.getId();

		var request = get("/api/task_statuses/{id}", id).with(tokenFailed);
		mockMvc.perform(request)
				.andExpect(status().isForbidden());
		var testStatus = statusRepository.findById(id);

		assertThat(testStatus).isNotEmpty();
	}

	@Test
	void createTest() throws Exception {
		Map<String, String> refData = new HashMap<>();
		refData.put("name", "yandex-status-create-test");
		refData.put("slug", "yandex-slug-create-test");

		var request = post("/api/task_statuses")
				.with(token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(refData));
		var response = mockMvc.perform(request)
				.andExpect(status().isCreated())
				.andReturn();
		var body = response.getResponse().getContentAsString();
		var teststatus = statusRepository.findBySlug(refData.get("slug")).orElse(null);

		assertThat(teststatus).isNotNull();
		assertThatJson(body).and(
				n -> n.node("name").isEqualTo(refData.get("name")),
				n -> n.node("slug").isEqualTo(refData.get("slug"))
		);
	}

	@Test
	void createTestFailed() throws Exception {
		Map<String, String> refData = new HashMap<>();
		refData.put("name", "");
		refData.put("slug", "");

		var request = post("/api/task_statuses")
				.with(token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(refData));
		mockMvc.perform(request)
				.andExpect(status().isBadRequest());
	}

	@Test
	void updateTest() throws Exception {
		long id  = status.getId();

		Map<String, String> refData = new HashMap<>();
		refData.put("name", "yandex-name-update");
		refData.put("slug", "yandex-slug-update");

		var request = put("/api/task_statuses/{id}", id)
				.with(token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(refData));
		var response = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andReturn();
		var body = response.getResponse().getContentAsString();

		assertThatJson(body).and(
				n -> n.node("name").isEqualTo(refData.get("name")),
				n -> n.node("slug").isEqualTo(refData.get("slug"))
		);
		assertThat(statusRepository.findById(id).get().getName()).isEqualTo(refData.get("name"));
	}

	@Test
	void updateTestFailed() throws Exception {
		long id  = status.getId();

		Map<String, String> refData = new HashMap<>();
		refData.put("name", "");
		refData.put("slug", "");

		var request = put("/api/task_statuses/{id}", id)
				.with(tokenFailed)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(refData));
		mockMvc.perform(request)
				.andExpect(status().isBadRequest());
		assertThat(statusRepository.findById(id).get().getName()).isEqualTo(status.getName());
	}

	@Test
	void destroyTest() throws Exception {
		long id  = status.getId();

		var request = delete("/api/task_statuses/{id}", id).with(token);
		mockMvc.perform(request)
				.andExpect(status().isNoContent());

		var maybestatus = statusRepository.findById(id).orElse(null);
		assertThat(maybestatus).isNull();
	}

	@Test
	void destroyTestFailed() throws Exception {
		long id  = status.getId();

		var request = delete("/api/task_statuses/{id}", id).with(tokenFailed);
		mockMvc.perform(request)
				.andExpect(status().isForbidden());

		var maybestatus = statusRepository.findById(id).orElse(null);
		assertThat(maybestatus).isNotNull();
	}
}
