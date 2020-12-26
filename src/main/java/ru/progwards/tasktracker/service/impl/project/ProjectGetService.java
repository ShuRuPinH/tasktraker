package ru.progwards.tasktracker.service.impl.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.progwards.tasktracker.exception.OperationIsNotPossibleException;
import ru.progwards.tasktracker.model.Project;
import ru.progwards.tasktracker.repository.ProjectRepository;
import ru.progwards.tasktracker.service.GetService;

/**
 * Класс по получению одного проекта
 * @author Pavel Khovaylo
 */
@Service
public class ProjectGetService implements GetService<Long, Project> {

    /**
     * репозиторий с проектами
     */
    @Autowired
    private ProjectRepository repository;

    /**
     * метод по получению проекта
     * @param id идентификатор проекта, который необходимо получить
     * @return бизнес-модель проекта
     */
    @Override
    public Project get(Long id) {
        if (id == null)
            throw new OperationIsNotPossibleException("Project.id = " + id + " doesn't exist");

        return repository.findById(id).
                orElseThrow(() ->
                        new OperationIsNotPossibleException("Project.id = " + id + " doesn't exist"));
    }
}