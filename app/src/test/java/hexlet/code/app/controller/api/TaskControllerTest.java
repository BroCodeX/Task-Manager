package hexlet.code.app.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.util.ModelsGenerator;
import org.instancio.Instancio;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ModelsGenerator generator;

	@Autowired
	private TaskRepository repository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TaskMapper mapper;

	@Autowired
	private WebApplicationContext wac;

	private JwtRequestPostProcessor token;

	private JwtRequestPostProcessor tokenFailed;

	private Task task;

	private List<Task> taskList;

	@BeforeEach
	void prepare() {

		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
				.apply(springSecurity())
				.build();

		token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

		tokenFailed = jwt().jwt(builder -> builder.subject("token@failed.test"));

		task = Instancio.of(generator.makeFakeTask()).create();
		repository.save(task);

		taskList = generator.getTaskList().stream()
						.map(Instancio::create)
						.toList();
	}

	@Test
	void indexTest() throws Exception {
		taskList.forEach(repository::save);

		var request = get("/api/tasks").with(token);
		var response = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andReturn();
		var body = response.getResponse().getContentAsString();

		List<TaskDTO> taskDTOS = objectMapper.readValue(body, new TypeReference<>() {});
		List<Task> actual = taskDTOS.stream().map(mapper::map).toList();
		List<Task> expected = repository.findAll();

		assertThatJson(body).isArray();
		assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
	}

	@Test
	void showTest() throws Exception {
		long id  = task.getId();

		var request = get("/api/tasks/{id}", id).with(token);
		var response = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andReturn();
		var body = response.getResponse().getContentAsString();
		var testTask = repository.findById(id);

		assertThat(testTask).isNotEmpty();
		assertThatJson(body).and(
				n -> n.node("title").isEqualTo(task.getName()),
				n -> n.node("content").isEqualTo(task.getDescription())
		);
	}

	@Test
	void showTestFailed() throws Exception {
		long id  = task.getId();

		var request = get("/api/tasks/{id}", id).with(tokenFailed);
		mockMvc.perform(request)
				.andExpect(status().isForbidden());
		var testTask = repository.findById(id);

		assertThat(testTask).isNotEmpty();
	}

	@Test
	void createTest() throws Exception {
		Map<String, String> refData = new HashMap<>();
		refData.put("title", "yandex-name-create");
		refData.put("content", "yandex-description-create");
		refData.put("status", "Draft");

		var request = post("/api/tasks")
				.with(token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(refData));
		var response = mockMvc.perform(request)
				.andExpect(status().isCreated())
				.andReturn();
		var body = response.getResponse().getContentAsString();
		var testTask = repository.findByName(refData.get("title")).orElse(null);

		assertThat(testTask).isNotNull();
		assertThatJson(body).and(
				n -> n.node("title").isEqualTo(refData.get("title")),
				n -> n.node("content").isEqualTo(refData.get("content")),
				n -> n.node("status").isEqualTo(refData.get("status"))
		);
	}


	@Test
	void createTestFailed() throws Exception {
		Map<String, String> refData = new HashMap<>();
		refData.put("title", "yandex-name-create-failed");
		refData.put("content", "yandex-description-create-failed");
		refData.put("status", "Draft");

		var request = post("/api/tasks")
				.with(token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(refData));
		mockMvc.perform(request)
				.andExpect(status().isCreated());

		var testTask = repository.findByName(refData.get("title")).orElse(null);

		assertThat(testTask).isNull();
	}

	@Test
	void updateTest() throws Exception {
		long id  = task.getId();

		Map<String, String> refData = new HashMap<>();
		refData.put("title", "yandex-name-update");
		refData.put("content", "yandex-description-update");
		refData.put("status", "To Review");

		var request = put("/api/tasks/{id}", id)
				.with(token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(refData));
		var response = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andReturn();
		var body = response.getResponse().getContentAsString();

		assertThatJson(body).and(
				n -> n.node("title").isEqualTo(refData.get("title")),
				n -> n.node("content").isEqualTo(refData.get("content")),
				n -> n.node("status").isEqualTo(refData.get("status"))
		);
		assertThat(repository.findById(id).get().getName()).isEqualTo(refData.get("title"));
	}

	@Test
	void updateTestFailed() throws Exception {
		long id  = task.getId();

		Map<String, String> refData = new HashMap<>();
		refData.put("name", "yandex-name-failed");
		refData.put("description", "yandex-description-failed");

		var request = put("/api/tasks/{id}", id)
				.with(tokenFailed)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(refData));
		mockMvc.perform(request)
				.andExpect(status().isForbidden());
		assertThat(repository.findById(id).get().getName()).isEqualTo(task.getName());
	}

	@Test
	void destroyTest() throws Exception {
		long id  = task.getId();

		var request = delete("/api/tasks/{id}", id).with(token);
		mockMvc.perform(request)
				.andExpect(status().isNoContent());

		var maybeTask = repository.findById(id).orElse(null);
		assertThat(maybeTask).isNull();
	}

	@Test
	void destroyTestFailed() throws Exception {
		long id  = task.getId();

		var request = delete("/api/tasks/{id}", id).with(tokenFailed);
		mockMvc.perform(request)
				.andExpect(status().isForbidden());

		var maybeTask = repository.findById(id).orElse(null);
		assertThat(maybeTask).isNotNull();
	}
}
