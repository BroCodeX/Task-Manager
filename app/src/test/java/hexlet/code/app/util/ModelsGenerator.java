package hexlet.code.app.util;

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

    private Model<User> userModel;
    private List<Model<User>> userModelList;
    private Model<Status> statusModel;
    private List<Model<Status>> statusModelList;
    private Model<Task> taskModel;
    private List<Model<Task>> taskList;
    private Model<Label> labelModel;
    private List<Model<Label>> labelList;

    @PostConstruct
    public void initData() {
        userModel =  makeFakeUser();
        userModelList = IntStream.range(0, 6)
                .mapToObj(i -> makeFakeUser())
                .collect(Collectors.toList());

        statusModel = makeFakeStatus();
        statusModelList = IntStream.range(0, 6)
                .mapToObj(i -> makeFakeStatus())
                .toList();

        taskModel = makeFakeTask();
        taskList = IntStream.range(0, 6)
                .mapToObj(i -> makeFakeTask())
                .toList();

        labelModel = makeFakeLabel();
        labelList = IntStream.range(0, 6)
                .mapToObj(i -> makeFakeLabel())
                .toList();

    }

    public Model<User> makeFakeUser() {
        return Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getPassword), () -> passwordEncoder.encode("password"))
                .toModel();
    }

    public Model<Status> makeFakeStatus() {
        return Instancio.of(Status.class)
                .ignore(Select.field(Status::getId))
                .supply(Select.field(Status::getName), () -> faker.name().name())
                .supply(Select.field(Status::getSlug), () -> faker.hobbit().character())
                .toModel();
    }

    public Model<Task> makeFakeTask() {
        return Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .ignore(Select.field(Task::getAssignee))
                .supply(Select.field(Task::getName), () -> faker.funnyName().name())
                .supply(Select.field(Task::getDescription), () -> faker.esports().game())
                .supply(Select.field(Task::getTaskStatus), () -> statusRepository.findByName("Draft").get())
                .supply(Select.field(Task::getIndex), () -> Integer.valueOf(faker.number().digit()))
                .toModel();
    }

    public Model<Label> makeFakeLabel() {
        return Instancio.of(Label.class)
                .ignore(Select.field(Label::getId))
                .ignore(Select.field(Label::getTasks))
                .supply(Select.field(Label::getName), () -> faker.lebowski().character())
                .toModel();
    }
}
