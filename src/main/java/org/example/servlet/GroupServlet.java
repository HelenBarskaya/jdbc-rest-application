package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.GroupDto;
import org.example.repository.mapping.GroupMapper;
import org.example.model.Group;
import org.example.service.impl.GroupService;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/group")
public class GroupServlet extends HttpServlet {

    GroupService groupService;
    GroupMapper groupMapper;
    ObjectMapper jsonMapper;

    @Override
    public void init(ServletConfig config) {
        groupService = new GroupService();
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
                GroupDto dto = groupMapper.groupToGroupDto(group);
                jsonMapper.writeValue(resp.getWriter(), dto);
                resp.setStatus(HttpServletResponse.SC_OK);
        } else {
                List<Group> groups = groupService.findAll();
                List<GroupDto> groupDto = new ArrayList<>();
                for (Group group : groups) {
                    groupDto.add(groupMapper.groupToGroupDto(group));
                }
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                jsonMapper.writeValue(resp.getWriter(), groupDto);
                resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            GroupDto dto = jsonMapper.readValue(req.getInputStream(), GroupDto.class);
            groupService.save(groupMapper.groupDtoToGroup(dto));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            GroupDto dto = jsonMapper.readValue(req.getInputStream(), GroupDto.class);
            groupService.update(groupMapper.groupDtoToGroup(dto));
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
}
