package ru.progwards.tasktracker.controller.converter.impl;

import org.springframework.stereotype.Component;
import ru.progwards.tasktracker.controller.converter.Converter;
import ru.progwards.tasktracker.controller.dto.AttachmentContentDtoFull;
import ru.progwards.tasktracker.service.vo.AttachmentContent;


/**
 * Преобразование valueObject <-> dto
 *
 * AttachmentContent <-> AttachmentContentDtoFull
 *
 * @author Gregory Lobkov
 */
@Component
public class AttachmentContentDtoFullConverter implements Converter<AttachmentContent, AttachmentContentDtoFull> {


    /**
     * Преобразовать в бизнес-объект
     *
     * @param dto сущность dto
     * @return бизнес-объект
     */
    @Override
    public AttachmentContent toModel(AttachmentContentDtoFull dto) {
        return new AttachmentContent(dto.getId(), dto.getData());
    }


    /**
     * Преобразовать в сущность dto
     *
     * @param model бизнес-объект
     * @return сущность dto
     */
    @Override
    public AttachmentContentDtoFull toDto(AttachmentContent model) {
        return new AttachmentContentDtoFull(model.getId(), model.getData());
    }

}