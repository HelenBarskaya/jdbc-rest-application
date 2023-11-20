package org.example.servlet.mapping;

import org.example.dto.GroupDto;
import org.example.model.Group;
import org.mapstruct.Mapper;

@Mapper
public interface GroupMapper {

    Group dtoToEntity(GroupDto groupDto);

    GroupDto entityToDto(Group group);
}
