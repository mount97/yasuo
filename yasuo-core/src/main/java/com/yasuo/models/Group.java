package com.yasuo.models;

import com.yasuo.pojo.GroupProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
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
    @Indexed
    private String name;
    @Builder.Default
    private GroupProperties groupProperties = new GroupProperties();
    @Builder.Default
    private List<String> memberIds = new ArrayList<>();
    private List<String> adminIds;
    private String createdBy;
    private boolean isDeleted;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
