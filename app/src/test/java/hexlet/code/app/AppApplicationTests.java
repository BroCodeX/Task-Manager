package hexlet.code.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.util.UserGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserGenerator generator;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private User user;

	private List<User> users;

	@BeforeEach
	void prepare() {
		user = generator.getUserModel();
		users = generator.getUsersModel();

	}

//	@Test
//	void contextLoads() {
//	}

	@Test
	void index() throws Exception {
		users.forEach(userRepository::save);

		var request = get("/api/users");
		var response = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andReturn();
		var body = response.getResponse().getContentAsString();

		assertThatJson(body).isArray();
	}

	@Test
	void create() throws Exception {
		Map<String, String> refData = new HashMap<>();
		refData.put("email", "yandextestcreate@test.com");
		refData.put("firstName", "yandexfirstName@test.com");
		refData.put("lastName", "yandexlastName@test.com");
		refData.put("password", "yandexPass");

		var request = post("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(refData));
		var response = mockMvc.perform(request)
				.andExpect(status().isCreated())
				.andReturn();
		var body = response.getResponse().getContentAsString();
		var user = userRepository.findByEmail(refData.get("email")).orElse(null);

		assertThat(user).isNotNull();
		assertThatJson(body).and(
				n -> n.node("email").isEqualTo("yandextestcreate@test.com"),
				n -> n.node("firstName").isEqualTo("yandexfirstName@test.com"),
				n -> n.node("lastName").isEqualTo("yandexlastName@test.com")
		);
	}

	@Test
	void update() throws Exception {
		userRepository.save(user);
		long id  = user.getId();
		Map<String, String> refData = new HashMap<>();
		refData.put("email", "yandextestupdate@test.com");
		refData.put("lastName", "yandexlastName@test.com");

		var request = put("/api/users/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(refData));
		var response = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andReturn();
		var body = response.getResponse().getContentAsString();

		assertThatJson(body).and(
				n -> n.node("email").isEqualTo("yandextestupdate@test.com"),
				n -> n.node("lastName").isEqualTo("yandexlastName@test.com")
		);
		assertThat(userRepository.findById(id).get().getEmail()).isEqualTo(refData.get("email"));

	}

	@Test
	void destroy() throws Exception {
		userRepository.save(user);
		long id = user.getId();

		var request = delete("/api/users/{id}", id);
		mockMvc.perform(request)
				.andExpect(status().isNoContent());

		var maybeUser = userRepository.findById(id).orElse(null);
		assertThat(maybeUser).isNull();
	}

}
