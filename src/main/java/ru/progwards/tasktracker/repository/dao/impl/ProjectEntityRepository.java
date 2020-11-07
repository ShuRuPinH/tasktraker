package ru.progwards.tasktracker.repository.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.progwards.tasktracker.repository.dao.JsonHandler;
import ru.progwards.tasktracker.repository.dao.Repository;
import ru.progwards.tasktracker.repository.entity.ProjectEntity;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Репозиторий проектов
 * @author Pavel Khovaylo
 */
@Component
public class ProjectEntityRepository implements Repository<Long, ProjectEntity> {

    @Autowired
    private JsonHandler<Long, ProjectEntity> projectEntityJsonHandler;

    @Override
    public Collection<ProjectEntity> get() {
        return projectEntityJsonHandler.getMap().values().stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public ProjectEntity get(Long id) {
        return id == null ? null : projectEntityJsonHandler.getMap().get(id);
    }

    @Override
    public void create(ProjectEntity entity) {
        if (entity != null) {
            ProjectEntity newEntity = projectEntityJsonHandler.getMap().put(entity.getId(), entity);
            if (newEntity == null)
                projectEntityJsonHandler.write();
        }
    }

    @Override
    public void update(ProjectEntity entity) {
        if (entity != null) {
            delete(entity.getId());
            create(entity);
        }
    }

    @Override
    public void delete(Long id) {
        if (id != null) {
            ProjectEntity entity = projectEntityJsonHandler.getMap().remove(id);
            if (entity != null)
                projectEntityJsonHandler.write();
        }
    }
}