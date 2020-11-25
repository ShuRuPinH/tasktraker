package ru.progwards.tasktracker.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.progwards.tasktracker.service.vo.*;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Объект, содержащий данные задачи, выводимые в пользовательском интерфейсе
 *
 * @author Oleg Kiselev
 */
public class TaskDtoFull {

    private final Long id;
    private String code;
    private String name;
    private String description;
    private TaskTypeDtoFull type;
    private TaskPriorityDtoPreview priority;
    private Project project;
    private UserDto author;
    private UserDto executor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime updated;
    private WorkFlowStatusDto status;
    private List<WorkFlowActionDto> actions;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss.SSS")
    private Duration estimation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss.SSS")
    private Duration timeSpent;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss.SSS")
    private Duration timeLeft;
    private List<RelatedTaskDtoFull> relatedTasks;
    private List<TaskAttachmentDto> attachments;
    private List<WorkLogDtoFull> workLogs;

    public TaskDtoFull(
            Long id, String code, String name, String description,
            TaskTypeDtoFull type, TaskPriorityDtoPreview priority, Project project,
            UserDto author, UserDto executor,
            ZonedDateTime created, ZonedDateTime updated,
            WorkFlowStatusDto status, List<WorkFlowActionDto> actions,
            Duration estimation, Duration timeSpent, Duration timeLeft,
            List<RelatedTaskDtoFull> relatedTasks, List<TaskAttachmentDto> attachments, List<WorkLogDtoFull> workLogs
    ) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.type = type;
        this.priority = priority;
        this.project = project;
        this.author = author;
        this.executor = executor;
        this.created = created;
        this.updated = updated;
        this.status = status;
        this.actions = actions;
        this.estimation = estimation;
        this.timeSpent = timeSpent;
        this.timeLeft = timeLeft;
        this.relatedTasks = relatedTasks;
        this.attachments = attachments;
        this.workLogs = workLogs;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskTypeDtoFull getType() {
        return type;
    }

    public void setType(TaskTypeDtoFull type) {
        this.type = type;
    }

    public TaskPriorityDtoPreview getPriority() {
        return priority;
    }

    public void setPriority(TaskPriorityDtoPreview priority) {
        this.priority = priority;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public UserDto getAuthor() {
        return author;
    }

    public void setAuthor(UserDto author) {
        this.author = author;
    }

    public UserDto getExecutor() {
        return executor;
    }

    public void setExecutor(UserDto executor) {
        this.executor = executor;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public WorkFlowStatusDto getStatus() {
        return status;
    }

    public void setStatus(WorkFlowStatusDto status) {
        this.status = status;
    }

    public List<WorkFlowActionDto> getActions() {
        return actions;
    }

    public void setActions(List<WorkFlowActionDto> actions) {
        this.actions = actions;
    }

    public Duration getEstimation() {
        return estimation;
    }

    public void setEstimation(Duration estimation) {
        this.estimation = estimation;
    }

    public Duration getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Duration timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Duration getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Duration timeLeft) {
        this.timeLeft = timeLeft;
    }

    public List<RelatedTaskDtoFull> getRelatedTasks() {
        return relatedTasks;
    }

    public void setRelatedTasks(List<RelatedTaskDtoFull> relatedTasks) {
        this.relatedTasks = relatedTasks;
    }

    public List<TaskAttachmentDto> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<TaskAttachmentDto> attachments) {
        this.attachments = attachments;
    }

    public List<WorkLogDtoFull> getWorkLogs() {
        return workLogs;
    }

    public void setWorkLogs(List<WorkLogDtoFull> workLogs) {
        this.workLogs = workLogs;
    }
}
