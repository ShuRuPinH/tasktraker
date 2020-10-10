package ru.progwards.tasktracker.service.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.progwards.tasktracker.repository.dao.impl.TaskEntityRepositoryUpdateField;
import ru.progwards.tasktracker.service.vo.UpdateOneValue;
import ru.progwards.tasktracker.service.facade.OneFieldSetService;

@Service
public class TaskOneFieldSetService implements OneFieldSetService {

    private TaskEntityRepositoryUpdateField updateField;

    @Autowired
    public TaskOneFieldSetService(TaskEntityRepositoryUpdateField updateField){
        this.updateField = updateField;
    }

    @Override
    public void setOneField(UpdateOneValue oneValue) {
        updateField.updateField(oneValue);
    }
}
