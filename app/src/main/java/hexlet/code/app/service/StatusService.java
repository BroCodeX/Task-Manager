package hexlet.code.app.service;

import hexlet.code.app.dto.StatusCreateDTO;
import hexlet.code.app.dto.StatusDTO;
import hexlet.code.app.dto.StatusUpdateDTO;
import hexlet.code.app.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {
    @Autowired
    private StatusRepository repository;

    public List<StatusDTO> getAll(int limit) {

    }

    public StatusDTO show(long id) {

    }

    public StatusDTO create(StatusCreateDTO dto) {

    }

    public StatusDTO update(StatusUpdateDTO dto, long id) {

    }

    public void destroy(long id) {

    }
}
