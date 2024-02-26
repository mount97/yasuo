package com.yasuo.services.mappers;

import com.yasuo.dtos.common.GroupDto;
import com.yasuo.models.Group;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    GroupDto toDto(Group group);
}
