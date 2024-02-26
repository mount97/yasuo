package com.yasuo.dtos.common;

import lombok.Data;

@Data
public class CreateOrUpdateGroupRequest {
    private String id;
    private String name;
    private GroupPropertiesDto groupProperties;
    //private List<UserDto> membeList;
}
