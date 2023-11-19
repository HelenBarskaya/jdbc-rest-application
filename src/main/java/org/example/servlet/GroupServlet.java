package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.database.ConnectionManager;
import org.example.dto.GroupDto;
import org.example.model.Group;
import org.example.repository.impl.ClientRepository;
import org.example.repository.impl.CoachRepository;
import org.example.repository.impl.GroupRepository;
import org.example.repository.mapping.GroupMapper;
import org.example.service.impl.GroupService;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/group")
public class GroupServlet extends HttpServlet {

    private ConnectionManager connectionManager;

    private GroupService groupService;
    private GroupMapper groupMapper;
    private ObjectMapper jsonMapper;

    @Override
    public void init() {
        connectionManager = new ConnectionManager();

        groupService = new GroupService(
                new GroupRepository(connectionManager),
                new ClientRepository(connectionManager),
                new CoachRepository(connectionManager)
        );
        groupMapper = Mappers.getMapper(GroupMapper.class);
        jsonMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String param = req.getParameter("id");

        if (param != null) {
            Long id = Long.parseLong(param);
            Group group = groupService.findById(id);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            GroupDto dto = groupMapper.entityToDto(group);
            resp.getWriter().write(jsonMapper.writeValueAsString(dto));

            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            List<Group> groups = groupService.findAll();
            List<GroupDto> dto = new ArrayList<>();

            for (Group group : groups) {
                dto.add(groupMapper.entityToDto(group));
            }

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write(jsonMapper.writeValueAsString(dto));
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GroupDto dto = jsonMapper.readValue(req.getInputStream().readAllBytes(), GroupDto.class);
        groupService.save(groupMapper.dtoToEntity(dto));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GroupDto dto = jsonMapper.readValue(req.getInputStream().readAllBytes(), GroupDto.class);
        groupService.update(groupMapper.dtoToEntity(dto));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String param = req.getParameter("id");

        if (param != null) {
            Long id = Long.parseLong(param);
            groupService.deleteById(id);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }
}
