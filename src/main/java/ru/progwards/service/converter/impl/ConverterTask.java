package ru.progwards.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.progwards.service.converter.Converter;
import ru.progwards.service.vo.Task;
import ru.progwards.repository.entity.TaskEntity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class ConverterTask implements Converter<TaskEntity, Task> {

    @Override
    public Task convertTo(TaskEntity taskEntity) {
        return new Task(taskEntity.getId(), taskEntity.getName(), taskEntity.getDescription(),
                taskEntity.getType(), taskEntity.getPriority(), taskEntity.getAuthorUserId(), taskEntity.getExecutorUserId(),
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(taskEntity.getCreated()), ZoneId.of("Europe/Moscow")),
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(taskEntity.getUpdated()), ZoneId.of("Europe/Moscow")),
                taskEntity.getStoryPoint(), taskEntity.getProjectId(), taskEntity.getStrCode(),
                taskEntity.getWfStatus(), taskEntity.getVersion(), taskEntity.getPlanDuration(),
                taskEntity.getSpentDuration(), taskEntity.getLeftDuration());
    }

    @Override
    public TaskEntity convertFrom(Task task) {
        return new TaskEntity(task.getId(), task.getName(), task.getDescription(),
                task.getType(), task.getPriority(), task.getAuthorUserId(),
                task.getExecutorUserId(), task.getCreated().toEpochSecond(),
                task.getUpdated().toEpochSecond(), task.getStoryPoint(),
                task.getProjectId(), task.getStrCode(), task.getWfStatus(),
                task.getVersion(), task.getPlanDuration(),
                task.getSpentDuration(), task.getLeftDuration());
    }
}
