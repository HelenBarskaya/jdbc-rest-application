package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.database.ConnectionManager;
import org.example.dto.CoachDto;
import org.example.repository.mapping.CoachMapper;
import org.example.model.Coach;
import org.example.service.impl.CoachService;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/coach")
public class CoachServlet extends HttpServlet {
    ConnectionManager connectionManager;
    CoachService coachService;
    CoachMapper coachMapper;
    ObjectMapper jsonMapper;


    @Override
    public void init(ServletConfig config) {
        connectionManager = new ConnectionManager();
        coachService = new CoachService(connectionManager);
        coachMapper = Mappers.getMapper(CoachMapper.class);
        jsonMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String param = req.getParameter("id");
        if (param != null) {
                Long id = Long.parseLong(param);
                Coach coach = coachService.findById(id);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                CoachDto dto = coachMapper.coachToCoachDto(coach);
                jsonMapper.writeValue(resp.getWriter(), dto);
                resp.setStatus(HttpServletResponse.SC_OK);
        } else {
                List<Coach> coaches = coachService.findAll();
                List<CoachDto> coachDto = new ArrayList<>();
                for (Coach coach : coaches) {
                    coachDto.add(coachMapper.coachToCoachDto(coach));
                }
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                jsonMapper.writeValue(resp.getWriter(), coachDto);
                resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            CoachDto coachDto = jsonMapper.readValue(req.getInputStream(), CoachDto.class);
            coachService.save(coachMapper.coachDtoToCoach(coachDto));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            CoachDto coachDto = jsonMapper.readValue(req.getInputStream(), CoachDto.class);
            coachService.update(coachMapper.coachDtoToCoach(coachDto));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String param = req.getParameter("id");
        if (param != null) {
                Long id = Long.parseLong(param);
                coachService.deleteById(id);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
