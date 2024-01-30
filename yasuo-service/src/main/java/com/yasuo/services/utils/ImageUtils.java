package com.yasuo.services.utils;

import com.yasuo.enums.ImageType;
import org.springframework.web.multipart.MultipartFile;

public final class ImageUtils {
    public static boolean isNotImage(MultipartFile file) {
        ImageType imageType = ImageType.getByType(file.getContentType());
        return imageType == null;
    }
}
