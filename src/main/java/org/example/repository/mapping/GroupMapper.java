package org.example.repository.mapping;

import org.example.dto.GroupDto;
import org.example.model.Group;
import org.mapstruct.Mapper;

@Mapper
public interface GroupMapper {

    Group groupDtoToGroup(GroupDto groupDto);

    GroupDto groupToGroupDto(Group group);
}
