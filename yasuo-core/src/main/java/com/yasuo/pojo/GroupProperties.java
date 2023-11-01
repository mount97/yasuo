package com.yasuo.pojo;

import com.yasuo.enums.GroupStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupProperties {
    private GroupStatus status;
    private String avatarUrl;
    private String backgroundUrl;
}
