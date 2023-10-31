package com.yasuo.models;

import com.yasuo.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Post")
@Builder
public class Post {
    @Id
    private String id;
    private String title;
    private String body;
    private String postedBy;
    private String groupId;
    private PostStatus status;
    private List<String> taggedUserIdList;
    @DBRef
    private List<Comment> commentList;
    private boolean isDeleted;
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime modifiedDate;
}
