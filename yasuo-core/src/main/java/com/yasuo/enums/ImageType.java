package com.yasuo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;

@AllArgsConstructor
@Getter
public enum ImageType {
    PNG("IMAGE/PNG"),
    JPEG("IMAGE/JPEG"),
    JPG("IMAGE/JPG");

    private final String type;

    public static ImageType getByType(String type) {
        for (ImageType imageType : values()) {
            if (imageType.getType().equalsIgnoreCase(type)) return imageType;
        }
        return null;
    }
}
