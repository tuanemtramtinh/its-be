package com.tuanemtramtinh.itslearningmanagement.services;

import com.cloudinary.Cloudinary;
import com.tuanemtramtinh.itscommon.enums.FileTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<String, Object> uploadFile(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String publicId = folder + "/" + UUID.randomUUID().toString();

        String resourceType = determineResourceType(file.getContentType());

        Map<String, Object> uploadParams = new HashMap<>();
        uploadParams.put("public_id", publicId);
        uploadParams.put("resource_type", resourceType);
        uploadParams.put("type", "upload");
        uploadParams.put("access_mode", "public");
        uploadParams.put("use_filename", false);
        uploadParams.put("unique_filename", false);
        uploadParams.put("overwrite", false);

        if ("raw".equals(resourceType) && !fileExtension.isEmpty()) {
            uploadParams.put("format", fileExtension.substring(1));
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);

        return uploadResult;
    }

    public void deleteFile(String publicId, String resourceType) throws IOException {
        if (publicId == null || publicId.isEmpty()) {
            throw new IllegalArgumentException("Public ID is empty or null");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("resource_type", resourceType);
        cloudinary.uploader().destroy(publicId, params);
    }

    private String determineResourceType(String contentType) {
        if (contentType == null) {
            return "raw";
        }

        if (contentType.startsWith("image/")) {
            return "image";
        } else if (contentType.startsWith("video/")) {
            return "video";
        } else {
            return "raw";
        }
    }

    public FileTypeEnum determineFileType(String contentType) {
        if (contentType == null) {
            return FileTypeEnum.OTHER;
        }

        if (contentType.startsWith("image/")) {
            return FileTypeEnum.IMAGE;
        } else if (contentType.startsWith("video/")) {
            return FileTypeEnum.VIDEO;
        } else if (contentType.startsWith("audio/")) {
            return FileTypeEnum.AUDIO;
        } else if (contentType.contains("pdf")) {
            return FileTypeEnum.PDF;
        } else if (contentType.contains("word") || contentType.contains("document")) {
            return FileTypeEnum.DOCUMENT;
        } else if (contentType.contains("spreadsheet") || contentType.contains("excel")) {
            return FileTypeEnum.SPREADSHEET;
        } else if (contentType.contains("presentation") || contentType.contains("powerpoint")) {
            return FileTypeEnum.PRESENTATION;
        } else {
            return FileTypeEnum.OTHER;
        }
    }

    public String extractPublicIdFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }

        try {
            String[] parts = url.split("/upload/");
            if (parts.length < 2) {
                return null;
            }

            String pathWithVersion = parts[1];
            String path = pathWithVersion.replaceFirst("v\\d+/", "");

            int lastDot = path.lastIndexOf('.');
            if (lastDot > 0) {
                path = path.substring(0, lastDot);
            }

            return path;
        } catch (Exception e) {
            return null;
        }
    }
}
