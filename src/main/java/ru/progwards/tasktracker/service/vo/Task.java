package ru.progwards.tasktracker.service.vo;

import ru.progwards.tasktracker.util.types.Priority;
import ru.progwards.tasktracker.util.types.TaskType;

import java.time.ZonedDateTime;

public class Task {

    private final Long id;
    private String name;
    private final String description;
    private final TaskType type;
    private final Priority priority;
    private final Long authorUserId;
    private final Long executorUserId;
    private final ZonedDateTime created;
    private final ZonedDateTime updated;
    private final int storyPoint;
    private final Long projectId;
    private String strCode; /* изменил имя на верблюжью нотацию с strcode на strCode*/
    private final String version;
    private final Long planDuration;
    private final Long spentDuration;
    private final Long leftDuration;

    public Task(Long id, String name, String description,
                TaskType type, Priority priority,
                Long authorUserId, Long executorUserId,
                ZonedDateTime created, ZonedDateTime updated,
                int storyPoint, Long projectId, String strCode, String version,
                Long planDuration, Long spentDuration, Long leftDuration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.priority = priority;
        this.authorUserId = authorUserId;
        this.executorUserId = executorUserId;
        this.created = created;
        this.updated = updated;
        this.storyPoint = storyPoint;
        this.projectId = projectId;
        this.strCode = strCode;
        this.version = version;
        this.planDuration = planDuration;
        this.spentDuration = spentDuration;
        this.leftDuration = leftDuration;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskType getType() {
        return type;
    }

    public Priority getPriority() {
        return priority;
    }

    public Long getAuthorUserId() {
        return authorUserId;
    }

    public Long getExecutorUserId() {
        return executorUserId;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public int getStoryPoint() {
        return storyPoint;
    }

    public Long getProjectId() {
        return projectId;
    }

    public String getStrCode() {
        return strCode;
    }

    public String getVersion() {
        return version;
    }

    public Long getPlanDuration() {
        return planDuration;
    }

    public Long getSpentDuration() {
        return spentDuration;
    }

    public Long getLeftDuration() {
        return leftDuration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStrCode(String strCode) {
        this.strCode = strCode;
    }
  
}
