package com.tuanemtramtinh.itslearningmanagement.services;

import com.tuanemtramtinh.itscommon.entity.Attachment;
import com.tuanemtramtinh.itscommon.entity.Content;
import com.tuanemtramtinh.itscommon.enums.ContentStatusEnum;
import com.tuanemtramtinh.itslearningmanagement.dto.AttachmentRequest;
import com.tuanemtramtinh.itslearningmanagement.dto.AttachmentResponse;
import com.tuanemtramtinh.itslearningmanagement.dto.ContentRequest;
import com.tuanemtramtinh.itslearningmanagement.dto.ContentResponse;
import com.tuanemtramtinh.itslearningmanagement.mapper.AttachmentResponseMapper;
import com.tuanemtramtinh.itslearningmanagement.mapper.ContentResponseMapper;
import com.tuanemtramtinh.itslearningmanagement.repositories.AttachmentRepository;
import com.tuanemtramtinh.itslearningmanagement.repositories.ContentRepository;
import com.tuanemtramtinh.itslearningmanagement.repositories.CourseInstanceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ContentService {
    private final CourseInstanceRepository courseInstanceRepository;
    private final ContentRepository contentRepository;
    private final AttachmentRepository attachmentRepository;
    private final ContentResponseMapper contentResponseMapper;
    private final AttachmentResponseMapper attachmentResponseMapper;
    private final CloudinaryService cloudinaryService;

    public ContentService(CourseInstanceRepository courseInstanceRepository,
            ContentRepository contentRepository,
            AttachmentRepository attachmentRepository,
            ContentResponseMapper contentResponseMapper,
            AttachmentResponseMapper attachmentResponseMapper,
            CloudinaryService cloudinaryService) {
        this.courseInstanceRepository = courseInstanceRepository;
        this.contentRepository = contentRepository;
        this.attachmentRepository = attachmentRepository;
        this.contentResponseMapper = contentResponseMapper;
        this.attachmentResponseMapper = attachmentResponseMapper;
        this.cloudinaryService = cloudinaryService;
    }

    public ContentResponse createContent(ContentRequest contentRequest) {
        if (contentRequest.getCourseInstanceId() == null)
            throw new RuntimeException("courseInstanceId is null");
        if (contentRequest.getTitle() == null)
            throw new RuntimeException("title is null");

        courseInstanceRepository.findById(contentRequest.getCourseInstanceId())
                .orElseThrow(() -> new RuntimeException("course instance not found"));

        Content content = Content.builder()
                .courseInstanceId(contentRequest.getCourseInstanceId())
                .title(contentRequest.getTitle())
                .description(contentRequest.getDescription())
                .type(contentRequest.getType())
                .status(contentRequest.getStatus() != null ? contentRequest.getStatus() : ContentStatusEnum.DRAFT)
                .orderIndex(contentRequest.getOrderIndex() != null ? contentRequest.getOrderIndex() : 0)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        content = contentRepository.save(content);
        return contentResponseMapper.toDTO(content);
    }

    public ContentResponse updateContent(String contentId, ContentRequest contentRequest) {
        if (contentId == null)
            throw new RuntimeException("contentId is null");

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("content not found"));

        if (contentRequest.getTitle() != null)
            content.setTitle(contentRequest.getTitle());
        if (contentRequest.getDescription() != null)
            content.setDescription(contentRequest.getDescription());
        if (contentRequest.getType() != null)
            content.setType(contentRequest.getType());
        if (contentRequest.getStatus() != null)
            content.setStatus(contentRequest.getStatus());
        if (contentRequest.getOrderIndex() != null)
            content.setOrderIndex(contentRequest.getOrderIndex());

        content.setUpdatedAt(new Date());
        content = contentRepository.save(content);

        return contentResponseMapper.toDTO(content);
    }

    @Transactional
    public void deleteContent(String contentId) {
        if (contentId == null)
            throw new RuntimeException("contentId is null");

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("content not found"));

        attachmentRepository.deleteByOwnerId(contentId);

        contentRepository.delete(content);
    }

    public ContentResponse getContentDetail(String contentId) {
        if (contentId == null)
            throw new RuntimeException("contentId is null");

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("content not found"));

        return contentResponseMapper.toDTO(content);
    }

    public Page<ContentResponse> searchContent(String courseInstanceId, Pageable pageable) {
        if (courseInstanceId == null)
            throw new RuntimeException("courseInstanceId is null");

        courseInstanceRepository.findById(courseInstanceId)
                .orElseThrow(() -> new RuntimeException("course instance not found"));

        Page<Content> contentPage = contentRepository.findByCourseInstanceId(courseInstanceId, pageable);
        return contentResponseMapper.toDTOPage(contentPage);
    }

    public AttachmentResponse addContentAttachment(String contentId, MultipartFile file) {
        if (contentId == null)
            throw new RuntimeException("contentId is null");
        if (file == null || file.isEmpty())
            throw new RuntimeException("file is empty");

        contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("content not found"));

        try {
            String folder = "learning-management/contents/" + contentId;
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(file, folder);

            String fileUrl = (String) uploadResult.get("secure_url");
            Long fileSize = file.getSize();
            com.tuanemtramtinh.itscommon.enums.FileTypeEnum fileType = cloudinaryService
                    .determineFileType(file.getContentType());

            Attachment attachment = Attachment.builder()
                    .ownerId(contentId)
                    .fileUrl(fileUrl)
                    .fileName(file.getOriginalFilename())
                    .fileSize(fileSize)
                    .fileType(fileType)
                    .uploadedAt(new Date())
                    .build();

            attachment = attachmentRepository.save(attachment);
            return attachmentResponseMapper.toDTO(attachment);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }


    public void removeContentAttachment(String attachmentId) {
        if (attachmentId == null)
            throw new RuntimeException("attachmentId is null");

        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("attachment not found"));

        try {
            String publicId = cloudinaryService.extractPublicIdFromUrl(attachment.getFileUrl());
            if (publicId != null) {
                String resourceType = determineCloudinaryResourceType(attachment.getFileType());
                cloudinaryService.deleteFile(publicId, resourceType);
            }
        } catch (Exception e) {
            System.err.println("Failed to delete file from Cloudinary: " + e.getMessage());
        }

        attachmentRepository.delete(attachment);
    }


    private String determineCloudinaryResourceType(com.tuanemtramtinh.itscommon.enums.FileTypeEnum fileType) {
        if (fileType == null) {
            return "raw";
        }
        switch (fileType) {
            case IMAGE:
                return "image";
            case VIDEO:
                return "video";
            default:
                return "raw";
        }
    }

    public List<AttachmentResponse> getContentAttachments(String contentId) {
        if (contentId == null)
            throw new RuntimeException("contentId is null");

        contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("content not found"));

        List<Attachment> attachments = attachmentRepository.findByOwnerId(contentId);
        return attachmentResponseMapper.toDTOList(attachments);
    }
}
