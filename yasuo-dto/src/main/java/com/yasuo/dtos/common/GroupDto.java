package com.yasuo.dtos.common;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GroupDto {
    private String name;
    private GroupPropertiesDto groupProperties;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
