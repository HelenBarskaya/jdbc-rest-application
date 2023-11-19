package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Coach;
import org.example.service.impl.CoachService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

class CoachServletTest {
    CoachServlet coachServlet;
    ObjectMapper jsonMapper = new ObjectMapper();

    CoachService service = Mockito.mock(CoachService.class);
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    @BeforeEach
    void setUp(){
        coachServlet = new CoachServlet();
        coachServlet.init();

        coachServlet.setCoachService(service);
    }

    @Test
    void doGetCoachByIdTest() throws IOException {
        Coach coach = new Coach("Маргарита", "Мастерова", "86666666666");
        coach.setId(1);

        String json = jsonMapper.writeValueAsString(coach);

        Mockito.when(service.findById(1L)).thenReturn(coach);
        Mockito.when(request.getParameter("id")).thenReturn("1");
        Mockito.when(response.getWriter()).thenReturn(Mockito.mock(PrintWriter.class));

        coachServlet.doGet(request, response);
        Mockito.verify(response.getWriter()).write(json);
    }

    @Test
    void doGetCoachesTest() throws IOException {
        Coach coach1 = new Coach("Маргарита", "Мастерова", "86666666666");
        coach1.setId(1);
        Coach coach2 = new Coach("Анна", "Ахматова", "89379120428");
        coach2.setId(2);

        List<Coach> coaches = new ArrayList<>();
        coaches.add(coach1);
        coaches.add(coach2);

        String json = jsonMapper.writeValueAsString(coaches);

        Mockito.when(service.findAll()).thenReturn(coaches);
        Mockito.when(request.getParameter("id")).thenReturn(null);
        Mockito.when(response.getWriter()).thenReturn(Mockito.mock(PrintWriter.class));

        coachServlet.doGet(request, response);
        Mockito.verify(response.getWriter()).write(json);
    }

    @Test
    void doPostTest() throws IOException {
        Coach coach = new Coach("Маргарита", "Мастерова", "86666666666");
        coach.setId(1);

        Mockito.when(service.save(coach)).thenReturn(coach);
        Mockito.when(request.getInputStream()).thenReturn(Mockito.mock(ServletInputStream.class));
        Mockito.when(request.getInputStream().readAllBytes()).thenReturn(jsonMapper.writeValueAsBytes(coach));

        coachServlet.doPost(request, response);
        Mockito.verify(service).save(coach);
    }

    @Test
    void doPutTest() throws IOException {
        Coach coach = new Coach("Маргарита", "Мастерова", "86666666666");
        coach.setId(1);

        Mockito.when(service.save(coach)).thenReturn(coach);
        Mockito.when(request.getInputStream()).thenReturn(Mockito.mock(ServletInputStream.class));
        Mockito.when(request.getInputStream().readAllBytes()).thenReturn(jsonMapper.writeValueAsBytes(coach));

        coachServlet.doPut(request, response);
        Mockito.verify(service).update(coach);
    }

    @Test
    void doDeleteTest() throws IOException {
        Mockito.when(service.deleteById(1L)).thenReturn(true);
        Mockito.when(request.getParameter("id")).thenReturn("1");

        coachServlet.doDelete(request, response);
        Mockito.verify(service).deleteById(1L);
    }
}