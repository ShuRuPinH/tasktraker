package ru.progwards.tasktracker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.progwards.tasktracker.dto.RelationTypeDtoFull;
import ru.progwards.tasktracker.exception.BadRequestException;
import ru.progwards.tasktracker.exception.NotFoundException;
import ru.progwards.tasktracker.model.RelationType;
import ru.progwards.tasktracker.repository.RelationTypeRepository;

import javax.validation.ConstraintViolationException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.progwards.tasktracker.objects.GetDtoFull.getRelationTypeDtoFull;
import static ru.progwards.tasktracker.objects.GetModel.getRelationTypeModel;

/**
 * ???????????????????????? ?????????????? ?????????????????????? RelationTypeController
 *
 * @author Oleg Kiselev
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class RelationTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RelationTypeRepository relationTypeRepository;

    private static final String GET_PATH = "/rest/relationType/{id}";
    private static final String GET_LIST_PATH = "/rest/relationType/list";
    private static final String CREATE_PATH = "/rest/relationType/create";
    private static final String DELETE_PATH = "/rest/relationType/{id}/delete";
    private static final String UPDATE_PATH = "/rest/relationType/{id}/update";

    public static MockHttpServletRequestBuilder postJson(String uri, Object body) {
        try {
            String json = new ObjectMapper().writeValueAsString(body);
            return post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static MockHttpServletRequestBuilder getUriAndMediaType(String uri, Long id) {
        return get(uri.replace("{id}", String.valueOf(id)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    public static MockHttpServletRequestBuilder getListUriAndMediaType(String uri) {
        return get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    public static MockHttpServletRequestBuilder deleteUriAndMediaType(String uri, Long id) {
        return delete(uri.replace("{id}", String.valueOf(id)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    public static MockHttpServletRequestBuilder putJson(String uri, Long id, Object body) {
        try {
            String json = new ObjectMapper().writeValueAsString(body);
            return put(uri.replace("{id}", String.valueOf(id)))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void create_RelationType_without_counter_RelationType() throws Exception {
        MvcResult result = mockMvc.perform(
                postJson(CREATE_PATH, getRelationTypeDtoFull()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Long id = getResultId(result);

        try {
            mockMvc.perform(get(GET_PATH.replace("{id}", String.valueOf(id))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(id), Long.class))
                    .andExpect(jsonPath("$.counterRelationId", equalTo(null)))
                    .andExpect(jsonPath("$.name", equalTo("relation name")));
        } finally {
            relationTypeRepository.deleteById(id);
        }
    }

    private Long getResultId(MvcResult result) throws UnsupportedEncodingException {
        String resultJson = result.getResponse().getContentAsString();
        return JsonPath.parse(resultJson).read("$.id", Long.class);
    }

    @Test
    void create_RelationType_with_counter_RelationType() throws Exception {
        RelationType counterType = getRelationTypeModel();
        counterType.setName("counter name");
        Long counterId = relationTypeRepository.save(counterType).getId();
        RelationTypeDtoFull dto = getRelationTypeDtoFull();
        dto.setCounterRelationId(counterId);

        MvcResult result = mockMvc.perform(
                postJson(CREATE_PATH, dto))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Long id = getResultId(result);

        try {
            mockMvc.perform(get(GET_PATH.replace("{id}", String.valueOf(id))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(id), Long.class))
                    .andExpect(jsonPath("$.counterRelationId", is(counterType.getId()), Long.class))
                    .andExpect(jsonPath("$.name", equalTo("relation name")));
        } finally {
            relationTypeRepository.deleteById(id);
        }
    }

    @Test
    void create_RelationType_BadRequest_Validation_If_Id_is_NotNull() throws Exception {
        RelationTypeDtoFull dto = getRelationTypeDtoFull();
        dto.setId(anyLong());
        mockMvcPerformPost(dto);
    }

    @Test
    void create_RelationType_BadRequest_Validation_If_Name_is_Empty() throws Exception {
        RelationTypeDtoFull dto = getRelationTypeDtoFull();
        dto.setName("   ");
        mockMvcPerformPost(dto);
    }

    private void mockMvcPerformPost(RelationTypeDtoFull dto) throws Exception {
        mockMvc.perform(
                postJson(CREATE_PATH, dto))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult ->
                        assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void create_RelationType_BadRequest_Validation_If_Name_is_Null() throws Exception {
        RelationTypeDtoFull dto = getRelationTypeDtoFull();
        dto.setName(null);
        mockMvc.perform(
                postJson(CREATE_PATH, dto))
                .andExpect(status().isInternalServerError())
                .andExpect(mvcResult -> assertNotNull(mvcResult.getResolvedException()));
    }

    @Test
    void get_RelationType() throws Exception {
        RelationType rt = relationTypeRepository.save(getRelationTypeModel());

        try {
            mockMvc.perform(
                    getUriAndMediaType(GET_PATH, rt.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(rt.getId()), Long.class))
                    .andExpect(jsonPath("$.name", equalTo("relation name")));
        } finally {
            relationTypeRepository.deleteById(rt.getId());
        }
    }

    @Test
    void get_RelationType_when_NotFound() throws Exception {
        mockMvc.perform(
                getUriAndMediaType(GET_PATH, Long.MAX_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult ->
                        assertTrue(mvcResult.getResolvedException() instanceof NotFoundException));
    }

    @Test
    void get_one_RelationType_when_Id_is_negative() throws Exception {
        mockMvcPerformGet(-1L);
    }

    private void mockMvcPerformGet(long l) throws Exception {
        mockMvc.perform(
                getUriAndMediaType(GET_PATH, l))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult ->
                        assertTrue(mvcResult.getResolvedException() instanceof ConstraintViolationException));
    }

    @Test
    void getList_RelationType() throws Exception {
        RelationType one = getRelationTypeModel();
        one.setName("name one");
        RelationType two = getRelationTypeModel();
        two.setName("name two");
        List<RelationType> listType = List.of(one, two);
        relationTypeRepository.saveAll(listType);

        try {
            mockMvc.perform(
                    getListUriAndMediaType(GET_LIST_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[*].name", containsInAnyOrder("name one", "name two")));
        } finally {
            relationTypeRepository.deleteAll(listType);
        }
    }

    @Test
    void getList_RelationType_when_return_Empty_List() throws Exception {
        mockMvc.perform(
                getListUriAndMediaType(GET_LIST_PATH))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult ->
                        assertTrue(mvcResult.getResolvedException() instanceof NotFoundException));
    }

    @Test
    void delete_RelationType() {
        RelationType rt = relationTypeRepository.save(getRelationTypeModel());

        try {
            mockMvc.perform(
                    deleteUriAndMediaType(DELETE_PATH, rt.getId()))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            relationTypeRepository.deleteById(rt.getId());
        }
    }

    @Test
    void delete_RelationType_when_Id_is_negative() throws Exception {
        mockMvcPerformDelete(-1L);
    }

    private void mockMvcPerformDelete(long l) throws Exception {
        mockMvc.perform(
                deleteUriAndMediaType(DELETE_PATH, l))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult ->
                        assertTrue(mvcResult.getResolvedException() instanceof ConstraintViolationException));
    }

    @Test
    void update_RelationType() throws Exception {
        RelationType rt = relationTypeRepository.save(getRelationTypeModel());
        RelationTypeDtoFull dto = getRelationTypeDtoFull();
        dto.setName("updated name");
        dto.setId(rt.getId());

        MvcResult result = mockMvc.perform(
                putJson(UPDATE_PATH, rt.getId(), dto))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Long id = getResultId(result);

        try {
            mockMvc.perform(get(GET_PATH.replace("{id}", String.valueOf(id))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(id), Long.class))
                    .andExpect(jsonPath("$.counterRelationId", equalTo(null)))
                    .andExpect(jsonPath("$.name", equalTo("updated name")));
        } finally {
            relationTypeRepository.deleteById(id);
        }
    }

    @Test
    void update_RelationType_when_Request_Id_is_different_Dto_Id() throws Exception {
        RelationType rt = relationTypeRepository.save(getRelationTypeModel());
        RelationTypeDtoFull dto = getRelationTypeDtoFull();
        dto.setName("another name");
        dto.setId(rt.getId() + 1);

        try {
            mockMvc.perform(
                    putJson(UPDATE_PATH, rt.getId(), dto))
                    .andExpect(status().isBadRequest())
                    .andExpect(mvcResult ->
                            assertTrue(mvcResult.getResolvedException() instanceof BadRequestException));
        } finally {
            relationTypeRepository.deleteById(rt.getId());
        }
    }

    @Test
    void update_RelationType_when_Name_is_already_used_another_RelationType() throws Exception {
        RelationType rt = relationTypeRepository.save(getRelationTypeModel());
        RelationTypeDtoFull dto = getRelationTypeDtoFull();
        dto.setId(rt.getId() + 1);

        try {
            mockMvc.perform(
                    putJson(UPDATE_PATH, rt.getId(), dto))
                    .andExpect(status().isBadRequest())
                    .andExpect(mvcResult -> assertNotNull(mvcResult.getResolvedException()));
        } finally {
            relationTypeRepository.deleteById(rt.getId());
        }
    }

    @Test
    void update_RelationType_when_NotFound() throws Exception {
        RelationTypeDtoFull dto = getRelationTypeDtoFull();
        dto.setId(Long.MAX_VALUE);

        mockMvc.perform(
                putJson(UPDATE_PATH, Long.MAX_VALUE, dto))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult ->
                        assertTrue(mvcResult.getResolvedException() instanceof NotFoundException));
    }
}