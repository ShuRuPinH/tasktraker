package ru.progwards.tasktracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.progwards.tasktracker.controller.converter.Converter;
import ru.progwards.tasktracker.controller.dto.TaskDtoFull;
import ru.progwards.tasktracker.controller.dto.TaskDtoPreview;
import ru.progwards.tasktracker.controller.exception.BadRequestException;
import ru.progwards.tasktracker.controller.exception.NotExistException;
import ru.progwards.tasktracker.controller.exception.NotFoundException;
import ru.progwards.tasktracker.service.facade.GetListService;
import ru.progwards.tasktracker.service.facade.GetService;
import ru.progwards.tasktracker.service.vo.Task;
import ru.progwards.tasktracker.service.vo.User;
import ru.progwards.tasktracker.util.types.TaskType;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskController taskController;

    @Autowired
    private GetListService<Task> taskGetListService;

    @Autowired
    private GetService<String, Task> byCodeGetService;

    @Autowired
    private Converter<Task, TaskDtoPreview> dtoConverter;

    @Autowired
    private Converter<Task, TaskDtoFull> dtoFullConverter;

    @Test
    public void testController() {
        assertThat(taskController, is(notNullValue()));
    }

    @Test
    void getAllProjectTasks() throws Exception {
        Collection<TaskDtoPreview> tempTasks = taskGetListService.getList().stream()
                .filter(task -> task.getProject_id().equals(2L))
                .map(task -> dtoConverter.toDto(task))
                .collect(Collectors.toList());

        String jsonString = new ObjectMapper()
                .registerModule(new JavaTimeModule()).writeValueAsString(tempTasks);

        mockMvc.perform(get("/rest/project/2/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));
    }

    @Test()
    void getAllProjectTasks_BadRequestException() {
        Exception exception = assertThrows(BadRequestException.class,
                () -> taskController.getAllTasks(null));
        assertTrue(exception.getMessage().contains(" не задан или задан неверно!"));
    }

    @Test()
    void getAllProjectTasks_NotFoundException() {
        Exception exception = assertThrows(NotFoundException.class,
                () -> taskController.getAllTasks(20L));
        assertTrue(exception.getMessage().contains("Список задач пустой!"));
    }

    @Test
    void addTask() throws Exception {
        mockMvc.perform(post("/rest/task/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\n" +
                                "    \"id\": 110,\n" +
                                "    \"code\": \"TT110-1\",\n" +
                                "    \"name\": \"Test task 110\",\n" +
                                "    \"description\": \"Description task 110\",\n" +
                                "    \"type\": \"BUG\",\n" +
                                "    \"project_id\": 2,\n" +
                                "    \"author\": {},\n" +
                                "    \"executor\": {},\n" +
                                "    \"created\": 1603274345,\n" +
                                "    \"updated\": null,\n" +
                                "    \"timeSpent\": null,\n" +
                                "    \"timeLeft\": null,\n" +
                                "    \"relatedTasks\": [],\n" +
                                "    \"attachments\": [],\n" +
                                "    \"workLogs\": []\n" +
                                "  }"
                ))
                .andDo(print())
                .andExpect(status().is2xxSuccessful()
                );

        mockMvc.perform(get("/rest/task/TT110-1/getbycode"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(110)))
                .andExpect(jsonPath("$.name", equalTo("Test task 110")));
    }

    @Test()
    void addTask_NotExistException() {
        Exception exception = assertThrows(NotExistException.class,
                () -> taskController.addTask(null));
        assertTrue(exception.getMessage().contains("Задача не существует!"));
    }

    @Test()
    void addTask_BadRequestException() {
        taskController.addTask(
                new TaskDtoFull(100L, "TT100", "Test task 1 TEST", "Description task 1",
                        TaskType.BUG, null, 11L, new User(), new User(),
                        ZonedDateTime.now(), ZonedDateTime.now().plusDays(1),
                        null,
                        Duration.ofDays(3), Duration.ofDays(1), Duration.ofDays(2),
                        new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
        );

        TaskDtoFull task = new TaskDtoFull(100L, "TT100", "Test task 1 TEST", "Description task 1",
                TaskType.BUG, null, 11L, new User(), new User(),
                ZonedDateTime.now(), ZonedDateTime.now().plusDays(1),
                null,
                Duration.ofDays(3), Duration.ofDays(1), Duration.ofDays(2),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Exception exception = assertThrows(BadRequestException.class,
                () -> taskController.addTask(task));
        assertTrue(exception.getMessage().contains("Такая задача уже существует!"));
    }

    @Test
    void updateTask() throws Exception {
        this.mockMvc.perform(put("/rest/project/2/tasks/111/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\n" +
                                "    \"id\": 111,\n" +
                                "    \"code\": \"TT111-1\",\n" +
                                "    \"name\": \"Test task 111 updated\",\n" +
                                "    \"description\": \"Description task 111\",\n" +
                                "    \"type\": \"BUG\",\n" +
                                "    \"project_id\": 2,\n" +
                                "    \"author\": {},\n" +
                                "    \"executor\": {},\n" +
                                "    \"created\": 1603274345,\n" +
                                "    \"updated\": null,\n" +
                                "    \"timeSpent\": null,\n" +
                                "    \"timeLeft\": null,\n" +
                                "    \"relatedTasks\": [],\n" +
                                "    \"attachments\": [],\n" +
                                "    \"workLogs\": []\n" +
                                "  }"
                ))
                .andDo(print())
                .andExpect(status().is2xxSuccessful()
                );

        this.mockMvc.perform(get("/rest/task/TT111-1/getbycode"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(111)))
                .andExpect(jsonPath("$.name", equalTo("Test task 111 updated")));
    }

    @Test()
    void updateTask_NotExistException() {
        Exception exception = assertThrows(NotExistException.class,
                () -> taskController.updateTask(null, null));
        assertTrue(exception.getMessage().contains("Задача не существует!"));
    }

    @Test()
    void updateTask_BadRequestException() {
        TaskDtoFull task = new TaskDtoFull(1L, "TT1", "Test task 1 TEST", "Description task 1",
                TaskType.BUG, null, 11L, new User(), new User(),
                ZonedDateTime.now(), ZonedDateTime.now().plusDays(1),
                null,
                Duration.ofDays(3), Duration.ofDays(1), Duration.ofDays(2),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Exception exception = assertThrows(BadRequestException.class,
                () -> taskController.updateTask(2L, task));
        assertTrue(exception.getMessage().contains("Данная операция недопустима!"));
    }

    @Test
    void deleteTaskById() throws Exception {
        mockMvc.perform(delete("/rest/project/2/tasks/{id}/delete", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/rest/project/2/tasks/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test()
    void deleteTaskById_BadRequestException() {
        Exception exception = assertThrows(BadRequestException.class,
                () -> taskController.deleteTask(null));
        assertTrue(exception.getMessage().contains(" не задан или задан неверно!"));
    }

    @Test()
    void deleteTaskById_NotFoundException() {
        Exception exception = assertThrows(NotFoundException.class,
                () -> taskController.deleteTask(20L));
        assertTrue(exception.getMessage().contains(" не найдена!"));
    }

    @Test
    void getTaskByCode() throws Exception {
        mockMvc.perform(post("/rest/task/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\n" +
                                "    \"id\": 110,\n" +
                                "    \"code\": \"TT110-1\",\n" +
                                "    \"name\": \"Test task 110\",\n" +
                                "    \"description\": \"Description task 110\",\n" +
                                "    \"type\": \"BUG\",\n" +
                                "    \"project_id\": 2,\n" +
                                "    \"author\": {},\n" +
                                "    \"executor\": {},\n" +
                                "    \"created\": 1603274345,\n" +
                                "    \"updated\": null,\n" +
                                "    \"timeSpent\": null,\n" +
                                "    \"timeLeft\": null,\n" +
                                "    \"relatedTasks\": [],\n" +
                                "    \"attachments\": [],\n" +
                                "    \"workLogs\": []\n" +
                                "  }"
                ));

        TaskDtoFull task = dtoFullConverter.toDto(byCodeGetService.get("TT110-1"));

        String jsonString = new ObjectMapper()
                .registerModule(new JavaTimeModule()).writeValueAsString(task);

        mockMvc.perform(get("/rest/task/TT110-1/getbycode"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));
    }

    @Test
    void getTaskByCode_BadRequestException() {
        Exception exception = assertThrows(BadRequestException.class,
                () -> taskController.getTaskByCode(null));
        assertTrue(exception.getMessage().contains(" не задан или задан неверно!"));
    }

    @Test
    void getTaskByCode_NotFoundException() {
        Exception exception = assertThrows(NotFoundException.class,
                () -> taskController.getTaskByCode("TT10-11"));
        assertTrue(exception.getMessage().contains(" не найдена!"));
    }
}