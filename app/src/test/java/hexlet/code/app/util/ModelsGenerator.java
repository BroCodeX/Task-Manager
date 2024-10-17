package hexlet.code.app.util;

import hexlet.code.app.dto.label.LabelCreateDTO;
import hexlet.code.app.dto.status.StatusCreateDTO;
import hexlet.code.app.dto.task.TaskCreateDTO;
import hexlet.code.app.dto.user.UserCreateDTO;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Status;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.StatusRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Getter
public class ModelsGenerator {
    @Autowired
    private Faker faker;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StatusRepository statusRepository;

    private Model<UserCreateDTO> userModel;
    private List<Model<UserCreateDTO>> userModelList;
    private Model<StatusCreateDTO> statusModel;
    private List<Model<StatusCreateDTO>> statusModelList;
    private Model<TaskCreateDTO> taskModel;
    private List<Model<TaskCreateDTO>> taskList;
    private Model<LabelCreateDTO> labelModel;
    private List<Model<LabelCreateDTO>> labelList;

    @PostConstruct
    public void initData() {
        userModel =  makeFakeUser();
        userModelList = IntStream.range(0, 5)
                .mapToObj(i -> makeFakeUser())
                .collect(Collectors.toList());

        statusModel = makeFakeStatus();
        statusModelList = IntStream.range(0, 5)
                .mapToObj(i -> makeFakeStatus())
                .toList();

        taskModel = makeFakeTask();
        taskList = IntStream.range(0, 5)
                .mapToObj(i -> makeFakeTask())
                .toList();

        labelModel = makeFakeLabel();
        labelList = IntStream.range(0, 5)
                .mapToObj(i -> makeFakeLabel())
                .toList();

    }

    public Model<UserCreateDTO> makeFakeUser() {
        return Instancio.of(UserCreateDTO.class)
                .supply(Select.field(UserCreateDTO::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(UserCreateDTO::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(UserCreateDTO::getLastName), () -> faker.name().lastName())
                .supply(Select.field(UserCreateDTO::getPassword), () -> passwordEncoder.encode("password"))
                .toModel();
    }

    public Model<StatusCreateDTO> makeFakeStatus() {
        return Instancio.of(StatusCreateDTO.class)
                .supply(Select.field(StatusCreateDTO::getName), () -> faker.name().name())
                .supply(Select.field(StatusCreateDTO::getSlug), () -> faker.hobbit().character())
                .toModel();
    }

    public Model<TaskCreateDTO> makeFakeTask() {
        return Instancio.of(TaskCreateDTO.class)
                .ignore(Select.field(TaskCreateDTO::getAssigneeId))
                .supply(Select.field(TaskCreateDTO::getTitle), () -> faker.funnyName().name())
                .supply(Select.field(TaskCreateDTO::getContent), () -> faker.esports().game())
                .supply(Select.field(TaskCreateDTO::getStatus), () -> "draft")
                .supply(Select.field(TaskCreateDTO::getIndex), () -> Integer.valueOf(faker.number().digit()))
                .supply(Select.field(TaskCreateDTO::getTaskLabelIds), () -> List.of(1L))
                .toModel();
    }

    public Model<LabelCreateDTO> makeFakeLabel() {
        return Instancio.of(LabelCreateDTO.class)
                .supply(Select.field(LabelCreateDTO::getName), () -> faker.funnyName().name())
                .toModel();
    }
}
