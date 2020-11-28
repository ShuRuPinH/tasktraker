package ru.progwards.tasktracker.controller.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.progwards.tasktracker.controller.converter.Converter;
import ru.progwards.tasktracker.controller.dto.ProjectDtoFull;
import ru.progwards.tasktracker.controller.dto.UserDtoPreview;
import ru.progwards.tasktracker.service.facade.GetService;
import ru.progwards.tasktracker.service.vo.Project;
import ru.progwards.tasktracker.service.vo.User;

/**
 * Конвертер Project <-> ProjectDtoFull
 * @author Pavel Khovaylo
 */
@Component
public class ProjectDtoFullConverter implements Converter<Project, ProjectDtoFull> {
    /**
     * конвертер User <-> UserDtoPreview
     */
    @Autowired
    private Converter<User, UserDtoPreview> userDtoPreviewConverter;
    /**
     * сервис для получения бизнес модели
     */
    @Autowired
    private GetService<Long, Project> projectGetService;

    /**
     * метод конвертирует объект ProjectDtoFull в model Project
     * @param dto объект ProjectDto, который конвертируется в модель
     * @return бизнес-модель проекта
     */
    @Override
    public Project toModel(ProjectDtoFull dto) {
        if (dto == null)
            return null;

        Project model = projectGetService.get(dto.getId());

        //проверка на наличие этого проекта в базе данных
        if (model == null) {
            return new Project(dto.getId(), dto.getName(), dto.getDescription(), dto.getPrefix(),
                    userDtoPreviewConverter.toModel(dto.getOwner()), dto.getCreated(),
                    null, 0L);
        }

        return new Project(dto.getId(), dto.getName(), dto.getDescription(), dto.getPrefix(),
                userDtoPreviewConverter.toModel(dto.getOwner()), dto.getCreated(),
                model.getTaskTypes(), model.getLastTaskCode());
    }

    /**
     * метод конвертирует model Project в объект ProjectDtoFull
     * @param model бизнес-модель проекта, которая конвертируется в ProjectDto
     * @return объект ProjectDto
     */
    @Override
    public ProjectDtoFull toDto(Project model) {
        if (model == null)
            return null;

        return new ProjectDtoFull(model.getId(), model.getName(), model.getDescription(), model.getPrefix(),
                userDtoPreviewConverter.toDto(model.getOwner()), model.getCreated());
    }
}