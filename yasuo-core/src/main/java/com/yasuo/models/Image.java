package com.yasuo.models;

import com.yasuo.enums.ImageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Image")
public class Image {
    @Id
    private String id;
    private String name;
    private ImageType type;
    private String url;
}
