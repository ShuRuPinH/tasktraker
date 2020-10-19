package ru.progwards.tasktracker.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.progwards.tasktracker.service.converter.Converter;
import ru.progwards.tasktracker.service.vo.Task;
import ru.progwards.tasktracker.repository.entity.TaskEntity;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class ConverterTask implements Converter<TaskEntity, Task> {

    @Override
    public Task toVo(TaskEntity taskEntity) {
        if (taskEntity == null)
            return null;
        else
            return new Task(
                    taskEntity.getId(),
                    taskEntity.getCode(),
                    taskEntity.getName(),
                    taskEntity.getDescription(),
                    taskEntity.getType(),
                    taskEntity.getPriority(),
                    taskEntity.getProject(),
                    taskEntity.getAuthor(),
                    taskEntity.getExecutor(),
                    ZonedDateTime.ofInstant(Instant.ofEpochSecond(taskEntity.getCreated()), ZoneId.of("Europe/Moscow")),
                    checkThatUpdatedTaskNotNull(taskEntity.getUpdated()),
                    taskEntity.getStatus(),
                    checkThatDurationTaskNotNull(taskEntity.getEstimation()),
                    checkThatDurationTaskNotNull(taskEntity.getTimeSpent()),
                    checkThatDurationTaskNotNull(taskEntity.getTimeLeft()),
                    taskEntity.getRelatedTasks(),
                    taskEntity.getAttachments(),
                    taskEntity.getWorkLogs()
            );
    }

    private Duration checkThatDurationTaskNotNull(Long duration) {
        return duration != null ? Duration.ofSeconds(duration) : null;
    }

    private ZonedDateTime checkThatUpdatedTaskNotNull(Long updated) {
        return updated != null ? ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(updated), ZoneId.of("Europe/Moscow")) : null;
    }

    @Override
    public TaskEntity toEntity(Task task) {
        if (task == null)
            return null;
        else
            return new TaskEntity(
                    task.getId(),
                    task.getCode(),
                    task.getName(),
                    task.getDescription(),
                    task.getType(),
                    task.getPriority(),
                    task.getProject(),
                    task.getAuthor(),
                    task.getExecutor(),
                    task.getCreated().toEpochSecond(),
                    checkThatUpdatedTaskEntityNotNull(task.getUpdated()),
                    task.getStatus(),
                    checkThatDurationTaskEntityNotNull(task.getEstimation()),
                    checkThatDurationTaskEntityNotNull(task.getTimeSpent()),
                    checkThatDurationTaskEntityNotNull(task.getTimeLeft()),
                    task.getRelatedTasks(),
                    task.getAttachments(),
                    task.getWorkLogs()
            );
    }

    private Long checkThatDurationTaskEntityNotNull(Duration duration) {
        return duration != null ? duration.toSeconds() : null;
    }

    private Long checkThatUpdatedTaskEntityNotNull(ZonedDateTime updated) {
        return updated != null ? updated.toEpochSecond() : null;
    }
}
