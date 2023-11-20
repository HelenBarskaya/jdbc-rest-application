package org.example.servlet.mapping;

import org.example.dto.CoachDto;
import org.example.model.Coach;
import org.mapstruct.Mapper;

@Mapper
public interface CoachMapper {

    Coach dtoToEntity(CoachDto coachDto);

    CoachDto entityToDto(Coach coach);
}
