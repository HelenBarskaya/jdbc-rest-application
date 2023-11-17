package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.ClientDto;
import org.example.repository.mapping.ClientMapper;
import org.example.model.Client;
import org.example.service.impl.ClientService;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/client")
public class ClientServlet extends HttpServlet {
    ClientService clientService;
    ClientMapper clientMapper;
    ObjectMapper jsonMapper;

    @Override
    public void init(ServletConfig config) {
        clientService = new ClientService();
        clientMapper = Mappers.getMapper(ClientMapper.class);
        jsonMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String param = req.getParameter("id");
        if (param != null) {
            try {
                Long id = Long.parseLong(param);
                Client client = clientService.findById(id);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                ClientDto dto = clientMapper.clientToClientDTO(client);
                jsonMapper.writeValue(resp.getWriter(), dto);
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                // TODO: 16.11.2023 Сделать так чтобы исключения выбрасывались
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            try {
                List<Client> clients = clientService.findAll();
                List<ClientDto> clientDto = new ArrayList<>();
                for (Client client : clients) {
                    clientDto.add(clientMapper.clientToClientDTO(client));
                }
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                jsonMapper.writeValue(resp.getWriter(), clientDto);
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                // TODO: 16.11.2023 Сделать так чтобы исключения выбрасывались
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ClientDto clientDto = jsonMapper.readValue(req.getInputStream(), ClientDto.class);
            clientService.save(clientMapper.clientDtoToClient(clientDto));
        } catch (SQLException e) {
            // TODO: 16.11.2023 Сделать так чтобы исключения выбрасывались
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ClientDto clientDto = jsonMapper.readValue(req.getInputStream(), ClientDto.class);
            clientService.update(clientMapper.clientDtoToClient(clientDto));
        } catch (SQLException e) {
            // TODO: 16.11.2023 Сделать так чтобы исключения выбрасывались
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String param = req.getParameter("id");
        if (param != null) {
            try {
                Long id = Long.parseLong(param);
                clientService.deleteById(id);
            } catch (SQLException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            // TODO: 16.11.2023 Сделать так чтобы исключения выбрасывались
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
