package ru.progwards.tasktracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Объект, содержащий краткие лог-данные (Журнала работ) о действиях в задаче
 *
 * @author Oleg Kiselev
 */
@Data
@AllArgsConstructor
public class WorkLogDtoPreview {

    @Min(0)
    @NotNull
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss.SSS")
    private Duration spent;

    private UserDtoPreview worker;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime start;

}
