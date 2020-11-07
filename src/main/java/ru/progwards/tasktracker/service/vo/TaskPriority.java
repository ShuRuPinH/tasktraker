package ru.progwards.tasktracker.service.vo;

/**
 * Класс TaskPriority - бизнес-модель
 * @author Pavel Khovaylo
 */
public class TaskPriority {
    /**
     * идентификатор
     */
    private Long id;
    /**
     * имя
     */
    private String name;
    /**
     * числовой приоритет
     */
    private Integer value;

    public TaskPriority(Long id, String name, Integer value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}