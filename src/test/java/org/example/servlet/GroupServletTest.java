package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Coach;
import org.example.model.Group;
import org.example.service.impl.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

class GroupServletTest {
    GroupServlet groupServlet;
    ObjectMapper jsonMapper = new ObjectMapper();

    GroupService service = Mockito.mock(GroupService.class);
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    @BeforeEach
    void setUp(){
        groupServlet = new GroupServlet();
        groupServlet.init();

        groupServlet.setGroupService(service);
    }

    @Test
    void doGetGroupByIdTest() throws IOException {
        Coach coach = new Coach("Маргарита", "Мастерова", "86666666666");
        coach.setId(1);

        Group group = new Group("Растяжка", coach);
        group.setId(1);

        String json = jsonMapper.writeValueAsString(group);

        Mockito.when(service.findById(1L)).thenReturn(group);
        Mockito.when(request.getParameter("id")).thenReturn("1");
        Mockito.when(response.getWriter()).thenReturn(Mockito.mock(PrintWriter.class));

        groupServlet.doGet(request, response);
        Mockito.verify(response.getWriter()).write(json);
    }

    @Test
    void doGetGroupsTest() throws IOException {
        Coach coach = new Coach("Маргарита", "Мастерова", "86666666666");
        coach.setId(1);

        List<Group> groups = new ArrayList<>();
        groups.add(new Group("Растяжка", coach));
        groups.add(new Group("Пилатес", coach));

        String json = jsonMapper.writeValueAsString(groups);

        Mockito.when(service.findAll()).thenReturn(groups);
        Mockito.when(request.getParameter("id")).thenReturn(null);
        Mockito.when(response.getWriter()).thenReturn(Mockito.mock(PrintWriter.class));

        groupServlet.doGet(request, response);
        Mockito.verify(response.getWriter()).write(json);
    }

    @Test
    void doPostTest() throws IOException {
        Coach coach = new Coach("Маргарита", "Мастерова", "86666666666");
        coach.setId(1);

        Group group = new Group("Растяжка", coach);
        group.setId(1);

        Mockito.when(service.save(group)).thenReturn(group);
        Mockito.when(request.getInputStream()).thenReturn(Mockito.mock(ServletInputStream.class));
        Mockito.when(request.getInputStream().readAllBytes()).thenReturn(jsonMapper.writeValueAsBytes(group));

        groupServlet.doPost(request, response);
        Mockito.verify(service).save(group);
    }

    @Test
    void doPutTest() throws IOException {
        Coach coach = new Coach("Маргарита", "Мастерова", "86666666666");
        coach.setId(1);

        Group group = new Group("Растяжка", coach);
        group.setId(1);

        Mockito.when(service.save(group)).thenReturn(group);
        Mockito.when(request.getInputStream()).thenReturn(Mockito.mock(ServletInputStream.class));
        Mockito.when(request.getInputStream().readAllBytes()).thenReturn(jsonMapper.writeValueAsBytes(group));

        groupServlet.doPut(request, response);
        Mockito.verify(service).update(group);
    }

    @Test
    void doDeleteTest() throws IOException {
        Mockito.when(service.deleteById(1L)).thenReturn(true);
        Mockito.when(request.getParameter("id")).thenReturn("1");

        groupServlet.doDelete(request, response);
        Mockito.verify(service).deleteById(1L);
    }

}