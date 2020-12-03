package ru.progwards.tasktracker.controller.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.progwards.tasktracker.controller.converter.Converter;
import ru.progwards.tasktracker.controller.dto.TaskDtoPreview;
import ru.progwards.tasktracker.controller.dto.TaskPriorityDtoPreview;
import ru.progwards.tasktracker.controller.dto.TaskTypeDtoPreview;
import ru.progwards.tasktracker.service.facade.GetService;
import ru.progwards.tasktracker.service.vo.Task;
import ru.progwards.tasktracker.service.vo.TaskPriority;
import ru.progwards.tasktracker.service.vo.TaskType;

/**
 * Конвертеры valueObject <-> dto
 *
 * @author Oleg Kiselev
 */
@Component
public class TaskDtoPreviewConverter implements Converter<Task, TaskDtoPreview> {

    @Autowired
    private GetService<Long, Task> taskGetService;
    @Autowired
    private Converter<TaskType, TaskTypeDtoPreview> typeDtoConverter;
    @Autowired
    private Converter<TaskPriority, TaskPriorityDtoPreview> priorityDtoConverter;

    /**
     * Метод конвертирует Dto сущность в бизнес объект
     *
     * @param dto сущность, приходящая из пользовательского интерфейса
     * @return value object - объект бизнес логики
     */
    @Override
    public Task toModel(TaskDtoPreview dto) {
        if (dto == null)
            return null;
        else {
            Task task = taskGetService.get(dto.getId());
            return new Task(
                    dto.getId(),
                    dto.getCode(),
                    dto.getName(),
                    task.getDescription(),
                    task.getType(),
                    task.getPriority(),
                    task.getProject(),
                    task.getAuthor(),
                    task.getExecutor(),
                    task.getCreated(),
                    task.getUpdated(),
                    task.getStatus(),
                    task.getActions(),
                    task.getEstimation(),
                    task.getTimeSpent(),
                    task.getTimeLeft(),
                    task.getRelatedTasks(),
                    task.getAttachments(),
                    task.getWorkLogs(),
                    task.getNotes()
            );
        }
    }

    /**
     * Метод конвертирует бизнес объект в сущность Dto
     *
     * @param model value object - объект бизнес логики
     * @return dto превью задачи для отображения в пользовательском интерфейсе
     */
    @Override
    public TaskDtoPreview toDto(Task model) {
        if (model == null)
            return null;
        else
            return new TaskDtoPreview(
                    model.getId(),
                    model.getCode(),
                    model.getName(),
                    typeDtoConverter.toDto(model.getType()),
                    priorityDtoConverter.toDto(model.getPriority())
            );
    }
}
