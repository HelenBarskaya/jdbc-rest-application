package org.example.repository.mapping;

import org.example.dto.CoachDto;
import org.example.model.Coach;
import org.mapstruct.Mapper;

@Mapper
public interface CoachMapper {
    Coach coachDtoToCoach(CoachDto coachDto);

    CoachDto coachToCoachDto(Coach coach);
}
