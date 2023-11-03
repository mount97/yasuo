package com.yasuo.models;

import com.yasuo.pojo.GroupProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Group")
@Builder
public class Group {
    @Id
    private String id;
    private String name;
    private GroupProperties groupProperties = new GroupProperties();
    private List<String> memberIdsList = new ArrayList<>();
    private List<String> adminIds;
    private String createdBy;
    private boolean isDeleted;
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime modifiedDate;
}
