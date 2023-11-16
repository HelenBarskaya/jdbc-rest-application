package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.CoachDto;
import org.example.repository.mapping.CoachMapper;
import org.example.model.Coach;
import org.example.service.impl.CoachService;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/coach")
public class CoachServlet extends HttpServlet {
    CoachService coachService;
    CoachMapper coachMapper;
    ObjectMapper jsonMapper;


    @Override
    public void init(ServletConfig config) {
        coachService = new CoachService();
        coachMapper = Mappers.getMapper(CoachMapper.class);
        jsonMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String param = req.getParameter("id");
        if (param != null) {
            try {
                Long id = Long.parseLong(param);
                Coach coach = coachService.findById(id);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                CoachDto dto = coachMapper.coachToCoachDto(coach);
                jsonMapper.writeValue(resp.getWriter(), dto);
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                // TODO: 16.11.2023 Сделать так чтобы исключения выбрасывались
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            try {
                List<Coach> coaches = coachService.findAll();
                List<CoachDto> coachDto = new ArrayList<>();
                for (Coach coach : coaches) {
                    coachDto.add(coachMapper.coachToCoachDto(coach));
                }
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                jsonMapper.writeValue(resp.getWriter(), coachDto);
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
            CoachDto coachDto = jsonMapper.readValue(req.getInputStream(), CoachDto.class);
            coachService.save(coachMapper.coachDtoToCoach(coachDto));
        } catch (SQLException e) {
            // TODO: 16.11.2023 Сделать так чтобы исключения выбрасывались
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        // TODO: 16.11.2023
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String param = req.getParameter("id");
        if (param != null) {
            try {
                Long id = Long.parseLong(param);
                coachService.deleteById(id);
            } catch (SQLException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
