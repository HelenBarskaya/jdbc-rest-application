package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.database.ConnectionManager;
import org.example.dto.ClientDto;
import org.example.repository.impl.ClientRepository;
import org.example.repository.impl.GroupRepository;
import org.example.servlet.mapping.ClientMapper;
import org.example.model.Client;
import org.example.service.impl.ClientService;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/client")
public class ClientServlet extends HttpServlet {

    private ClientService clientService;
    private ClientMapper clientMapper;
    private ObjectMapper jsonMapper;

    @Override
    public void init() {
        ConnectionManager connectionManager = new ConnectionManager();

        clientService = new ClientService(
                new ClientRepository(connectionManager),
                new GroupRepository(connectionManager)
        );
        clientMapper = Mappers.getMapper(ClientMapper.class);
        jsonMapper = new ObjectMapper();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String param = req.getParameter("id");

        if (param != null) {
            Long id = Long.parseLong(param);
            try {
                Client client = clientService.findById(id);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");

                ClientDto dto = clientMapper.entityToDto(client);
                resp.getWriter().write(jsonMapper.writeValueAsString(dto));

                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (IllegalArgumentException | IllegalStateException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            try {
                List<Client> clients = clientService.findAll();
                List<ClientDto> dto = new ArrayList<>();

                for (Client client : clients) {
                    dto.add(clientMapper.entityToDto(client));
                }

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");

                resp.getWriter().write(jsonMapper.writeValueAsString(dto));
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (IllegalArgumentException | IllegalStateException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ClientDto clientDto = jsonMapper.readValue(req.getInputStream().readAllBytes(), ClientDto.class);
        clientService.save(clientMapper.dtoToEntity(clientDto));
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ClientDto clientDto = jsonMapper.readValue(req.getInputStream().readAllBytes(), ClientDto.class);
        clientService.update(clientMapper.dtoToEntity(clientDto));
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String param = req.getParameter("id");

        if (param != null) {
            Long id = Long.parseLong(param);
            clientService.deleteById(id);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }
}
