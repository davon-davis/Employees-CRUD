package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.Cookie;
import javax.transaction.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EmployeeControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    EmployeeRepository repository;


    @Test
    @Transactional
    @Rollback
    public void testList() throws Exception{
        Employee employee1 = new Employee();
        employee1.setName("Davon");
        employee1.setDate(new Date());

        repository.save(employee1);

        MockHttpServletRequestBuilder request = get("/employees")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Davon")));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetEmployee() throws Exception{
        Employee employee1 = new Employee();
        employee1.setName("Jimmy");
        employee1.setDate(new Date());

        repository.save(employee1);

        MockHttpServletRequestBuilder request = get("/employees/1")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Jimmy")));
    }

    @Test
    @Transactional
    @Rollback
    public void testPostEmployee() throws Exception{
        Employee employee1 = new Employee();
        employee1.setName("Vanilla Ice");
        employee1.setDate(new Date());

        repository.save(employee1);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(employee1);

        MockHttpServletRequestBuilder request = post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Vanilla Ice")));

    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteEmployee() throws Exception{
        Employee employee1 = new Employee();
        employee1.setName("Vanilla Ice");
        employee1.setDate(new Date());

        Employee employee2 = new Employee();
        employee2.setName("Jimmy");
        employee2.setDate(new Date());

        repository.save(employee1);
        repository.save(employee2);

        MockHttpServletRequestBuilder request = delete("/employees/1")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().
                        string("A user has been delete. Remaining employees: 1"));
    }

    @Test
    @Transactional
    @Rollback
    public void updateEmployee() throws Exception{
        Employee employee1 = new Employee();
        employee1.setName("Vanilla Ice");
        employee1.setDate(new Date());

        repository.save(employee1);

        MockHttpServletRequestBuilder request = patch("/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Timo\"}");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Timo")));

    }
}
