package ru.progwards.tasktracker.service.facade.impl.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.progwards.tasktracker.repository.dao.Repository;
import ru.progwards.tasktracker.repository.entity.TaskEntity;
import ru.progwards.tasktracker.service.converter.Converter;
import ru.progwards.tasktracker.service.facade.CreateService;
import ru.progwards.tasktracker.service.facade.GetService;
import ru.progwards.tasktracker.service.facade.RefreshService;
import ru.progwards.tasktracker.service.vo.Project;
import ru.progwards.tasktracker.service.vo.Task;

import java.time.ZonedDateTime;

/**
 * Бизнес-логика работы с задачей
 *
 * @author Oleg Kiselev
 */
@Service
public class TaskCreateService implements CreateService<Task> {

    @Autowired
    private Repository<Long, TaskEntity> repository;
    @Autowired
    private Converter<TaskEntity, Task> converter;
    @Autowired
    private GetService<Long, Project> getService;
    @Autowired
    private RefreshService<Project> refreshService;

    /**
     * Метод создает задачу
     *
     * @param model value object
     */
    @Override
    public void create(Task model) {
        if (model.getCode() == null)
            model.setCode(generateTaskCode(model.getProject().getId()));
        if (model.getCreated() == null)
            model.setCreated(ZonedDateTime.now());

        repository.create(converter.toEntity(model));
    }

    /**
     * Метод создания кода задачи на основе префикса проекта
     *
     * @param project_id идентификатор проекта, к которому принадлежит задача
     * @return код задачи в формате "NGR-1"
     */
    private String generateTaskCode(Long project_id) {
        Project project = getService.get(project_id);
        Long lastTaskCode = project.getLastTaskCode();
        String taskCode = project.getPrefix() + "-" + lastTaskCode;
        project.setLastTaskCode(lastTaskCode + 1);
        refreshService.refresh(project);
        return taskCode;
    }
}
