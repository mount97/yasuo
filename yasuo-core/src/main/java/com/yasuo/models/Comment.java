package com.yasuo.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Comment")
@Builder
public class Comment {
    @Id
    private String id;
    private String content;
    private String commentedBy;
    private boolean isDeleted;
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime modifiedDate;
}
