package ru.progwards.tasktracker.service.converter.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.progwards.tasktracker.repository.entity.RelationTypeEntity;
import ru.progwards.tasktracker.service.converter.Converter;
import ru.progwards.tasktracker.service.vo.RelationType;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * тестирование конвертера между valueObject <-> entity
 *
 * @author Oleg Kiselev
 */
@SpringBootTest
class RelationTypeConverterTest {

    @Autowired
    private Converter<RelationTypeEntity, RelationType> converter;

    @Test
    void toVo_return_Null() {
        RelationType type = converter.toVo(null);

        assertThat(type, is(nullValue()));
    }

    @Test
    void toVo_return_NotNull() {
        RelationType type = converter.toVo(
                new RelationTypeEntity(
                        null, "блокирующая", 2L)
        );

        assertThat(type, is(notNullValue()));
    }

    @Test
    void toEntity_returnNull() {
        RelationTypeEntity typeEntity = converter.toEntity(null);

        assertThat(typeEntity, is(nullValue()));
    }

    @Test
    void toEntity_return_Not_Null() {
        RelationTypeEntity typeEntity = converter.toEntity(
                new RelationType(
                        null, "блокирующая", 2L)
        );

        assertThat(typeEntity, is(notNullValue()));
    }
}