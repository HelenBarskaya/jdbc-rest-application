package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Client;
import org.example.service.impl.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

class ClientServletTest {

    ClientServlet clientServlet;
    ObjectMapper jsonMapper = new ObjectMapper();

    ClientService service = Mockito.mock(ClientService.class);
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    @BeforeEach
    void setUp(){
        clientServlet = new ClientServlet();
        clientServlet.init();

        clientServlet.setClientService(service);
    }

    @Test
    void doGetClientByIdTest() throws IOException {
        Client client = new Client("Маргарита", "Мастерова", "86666666666");
        client.setId(1);

        String json = jsonMapper.writeValueAsString(client);

        Mockito.when(service.findById(1L)).thenReturn(client);
        Mockito.when(request.getParameter("id")).thenReturn("1");
        Mockito.when(response.getWriter()).thenReturn(Mockito.mock(PrintWriter.class));

        clientServlet.doGet(request, response);
        Mockito.verify(response.getWriter()).write(json);
    }

    @Test
    void doGetClientByIllegalIdTest() throws IOException {

        Mockito.when(service.findById(1L)).thenThrow(new IllegalArgumentException());
        Mockito.when(request.getParameter("id")).thenReturn("1");

        clientServlet.doGet(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGetClientsTest() throws IOException {
        Client client1 = new Client("Маргарита", "Мастерова", "86666666666");
        client1.setId(1);
        Client client2 = new Client("Анна", "Ахматова", "89379120428");
        client2.setId(2);

        List<Client> clients = new ArrayList<>();
        clients.add(client1);
        clients.add(client2);

        String json = jsonMapper.writeValueAsString(clients);

        Mockito.when(service.findAll()).thenReturn(clients);
        Mockito.when(request.getParameter("id")).thenReturn(null);
        Mockito.when(response.getWriter()).thenReturn(Mockito.mock(PrintWriter.class));

        clientServlet.doGet(request, response);
        Mockito.verify(response.getWriter()).write(json);
    }

    @Test
    void doPostTest() throws IOException {
        Client client = new Client("Маргарита", "Мастерова", "86666666666");
        client.setId(1);

        Mockito.when(service.save(client)).thenReturn(client);
        Mockito.when(request.getInputStream()).thenReturn(Mockito.mock(ServletInputStream.class));
        Mockito.when(request.getInputStream().readAllBytes()).thenReturn(jsonMapper.writeValueAsBytes(client));

        clientServlet.doPost(request, response);
        Mockito.verify(service).save(client);
    }

    @Test
    void doPutTest() throws IOException {
        Client client = new Client("Маргарита", "Мастерова", "86666666666");
        client.setId(1);

        Mockito.when(service.save(client)).thenReturn(client);
        Mockito.when(request.getInputStream()).thenReturn(Mockito.mock(ServletInputStream.class));
        Mockito.when(request.getInputStream().readAllBytes()).thenReturn(jsonMapper.writeValueAsBytes(client));

        clientServlet.doPut(request, response);
        Mockito.verify(service).update(client);
    }

    @Test
    void doDeleteTest() throws IOException {
        Mockito.when(service.deleteById(1L)).thenReturn(true);
        Mockito.when(request.getParameter("id")).thenReturn("1");

        clientServlet.doDelete(request, response);
        Mockito.verify(service).deleteById(1L);
    }
}
