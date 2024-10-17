package hexlet.code.app.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.label.LabelCreateDTO;
import hexlet.code.app.dto.label.LabelDTO;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.service.LabelService;
import hexlet.code.app.util.ModelsGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LabelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ModelsGenerator generator;

    @Autowired
    private LabelRepository repository;

    @Autowired
    private LabelMapper mapper;

    @Autowired
    private LabelService labelService;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor tokenFailed;

    private LabelDTO label;

    private List<LabelCreateDTO> labelList;

    @BeforeEach
    void prepare() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();

        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        tokenFailed = jwt().jwt(builder -> builder.subject("token@failed.test"));

        var dto = Instancio.of(generator.getLabelModel()).create();

        label = labelService.createLabel(dto);

        labelList = generator.getLabelList().stream()
                .map(Instancio::create)
                .toList();
    }

    @AfterEach
    void cleanRepo() {
        repository.deleteById(label.getId());
    }

    @Test
    void indexTest() throws Exception {
        labelList.forEach(labelService::createLabel);

        var request = get("/api/labels")
                .with(token);
        var response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = response.getResponse().getContentAsString();

        List<LabelDTO> labelDTOS = objectMapper.readValue(body, new TypeReference<>() { });
        List<Label> actual = labelDTOS.stream().map(mapper::map).toList();
        List<Label> expected = repository.findAll();

        assertThatJson(body).isArray();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void showTest() throws Exception {
        long id = label.getId();

        var request = get("/api/labels/{id}", id)
                .with(token);
        var responce = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = responce.getResponse().getContentAsString();

        var testLabel = repository.findById(id);

        assertThat(testLabel).isNotEmpty();
        assertThatJson(body).and(
                n -> n.node("name").isEqualTo(label.getName())
        );
    }

    @Test
    void showTestFailed() throws Exception {
        long id = label.getId();

        var request = get("/api/labels/{id}", id)
                .with(tokenFailed);
        mockMvc.perform(request)
                .andExpect(status().isForbidden());

        var testLabel = repository.findById(id);

        assertThat(testLabel).isNotEmpty();
    }

    @Test
    void createTest() throws Exception {
        Map<String, String> refData = new HashMap<>();
        refData.put("name", "yandex-name-label");

        var request = post("/api/labels")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refData));

        var response = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
        var body = response.getResponse().getContentAsString();

        var maybeLabel = repository.findByName(refData.get("name")).orElse(null);

        assertThat(maybeLabel).isNotNull();
        assertThatJson(body).and(
                n -> n.node(("name")).isEqualTo(refData.get(("name")))
        );
    }

    @Test
    void createTestFailedData() throws Exception {
        Map<String, String> refData = new HashMap<>();
        refData.put("name", "12");

        var request = post("/api/labels")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refData));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());

        var maybeLabel = repository.findByName(refData.get("name")).orElse(null);

        assertNull(maybeLabel);
    }

    @Test
    void updateTest() throws Exception {
        long id = label.getId();

        Map<String, String> refData = new HashMap<>();
        refData.put("name", "yandex-name-label-update");

        var request = put("/api/labels/{id}", id)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refData));

        var response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = response.getResponse().getContentAsString();

        assertThat(repository.findByName(refData.get("name"))).isNotEqualTo(label.getName());

        assertThatJson(body).and(
                n -> n.node("name").isEqualTo(refData.get("name"))
        );
    }

    @Test
    void updateTestFailed() throws Exception {
        long id = label.getId();

        Map<String, String> refData = new HashMap<>();
        refData.put("name", "yy");

        var request = put("/api/labels/{id}", id)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refData));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());

        var maybeLabel = repository.findById(id).get();
        var failedLabel = repository.findByName(refData.get("name")).orElse(null);

        assertThat(maybeLabel.getName()).isEqualTo(label.getName());
        assertNull(failedLabel);
    }

    @Test
    void destroyTest() throws Exception {
        long id = label.getId();

        var request = delete("/api/labels/{id}", id)
                .with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        var maybeLabel = repository.findById(id).orElse(null);
        assertNull(maybeLabel);
    }

    @Test
    void destroyTestFailed() throws Exception {
        long id = label.getId();

        var request = delete("/api/labels/{id}", id)
                .with(tokenFailed);
        mockMvc.perform(request)
                .andExpect(status().isForbidden());

        var maybeLabel = repository.findById(id);

        assertThat(maybeLabel).isNotEmpty();
    }

}
