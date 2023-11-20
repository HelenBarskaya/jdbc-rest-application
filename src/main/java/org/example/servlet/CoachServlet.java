package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.database.ConnectionManager;
import org.example.dto.CoachDto;
import org.example.model.Coach;
import org.example.repository.impl.CoachRepository;
import org.example.repository.impl.GroupRepository;
import org.example.servlet.mapping.CoachMapper;
import org.example.service.impl.CoachService;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/coach")
public class CoachServlet extends HttpServlet {
    private CoachService coachService;
    private CoachMapper coachMapper;
    private ObjectMapper jsonMapper;


    @Override
    public void init() {
        ConnectionManager connectionManager = new ConnectionManager();

        coachService = new CoachService(
                new CoachRepository(connectionManager),
                new GroupRepository(connectionManager)
        );
        coachMapper = Mappers.getMapper(CoachMapper.class);
        jsonMapper = new ObjectMapper();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String param = req.getParameter("id");

        if (param != null) {
            Long id = Long.parseLong(param);
            try {
                Coach coach = coachService.findById(id);

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");

                CoachDto dto = coachMapper.entityToDto(coach);

                resp.getWriter().write(jsonMapper.writeValueAsString(dto));
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (IllegalArgumentException | IllegalStateException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            try {
                List<Coach> coaches = coachService.findAll();
                List<CoachDto> dto = new ArrayList<>();

                for (Coach coach : coaches) {
                    dto.add(coachMapper.entityToDto(coach));
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
        CoachDto coachDto = jsonMapper.readValue(req.getInputStream().readAllBytes(), CoachDto.class);
        coachService.save(coachMapper.dtoToEntity(coachDto));
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CoachDto coachDto = jsonMapper.readValue(req.getInputStream().readAllBytes(), CoachDto.class);
        coachService.update(coachMapper.dtoToEntity(coachDto));
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String param = req.getParameter("id");

        if (param != null) {
            Long id = Long.parseLong(param);
            coachService.deleteById(id);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void setCoachService(CoachService coachService) {
        this.coachService = coachService;
    }
}
