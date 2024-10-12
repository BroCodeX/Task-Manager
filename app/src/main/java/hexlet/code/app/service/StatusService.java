package hexlet.code.app.service;

import hexlet.code.app.dto.StatusCreateDTO;
import hexlet.code.app.dto.StatusDTO;
import hexlet.code.app.dto.StatusUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundExcepiton;
import hexlet.code.app.mapper.StatusMapper;
import hexlet.code.app.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {
    @Autowired
    private StatusRepository repository;

    @Autowired
    private StatusMapper mapper;

    public List<StatusDTO> getAll(int limit) {
        return repository.findAll().stream()
                .limit(limit)
                .map(mapper::map)
                .toList();
    }

    public StatusDTO showStatus(long id) {
        var maybeStatus =  repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExcepiton("This id: " + id + " is not found"));
        return mapper.map(maybeStatus);
    }

    public StatusDTO createStatus(StatusCreateDTO dto) {
        var status = mapper.map(dto);
        repository.save(status);
        return mapper.map(status);
    }

    public StatusDTO updateStatus(StatusUpdateDTO dto, long id) {
        var maybeStatus =  repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExcepiton("This id: " + id + " is not found"));
        mapper.update(dto, maybeStatus);
        repository.save(maybeStatus);
        return mapper.map(maybeStatus);
    }

    public void destroyStatus(long id) {
        repository.deleteById(id);
    }
}
