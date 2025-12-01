package com.tuanemtramtinh.itslearningmanagement.presentation;

import com.tuanemtramtinh.itscommon.dto.UserResponse;
import com.tuanemtramtinh.itslearningmanagement.dto.*;
import com.tuanemtramtinh.itslearningmanagement.services.CourseInstanceService;
import com.tuanemtramtinh.itslearningmanagement.services.CourseService;
import com.tuanemtramtinh.itscommon.enums.CourseStatusEnum;
import com.tuanemtramtinh.itscommon.response.ApiResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LearningContentManagementFacade {

    private final CourseService courseService;
    private final CourseInstanceService courseInstanceService;

    public LearningContentManagementFacade(CourseService courseService, CourseInstanceService courseInstanceService) {
        this.courseService = courseService;
        this.courseInstanceService = courseInstanceService;
    }

    @PostMapping("/courses-instance/create")
    public ResponseEntity<ApiResponse<CourseInstanceResponse>> createCourseInstance(@RequestBody CourseInstanceRequest courseInstanceRequest) {
        CourseInstanceResponse result = courseInstanceService.createCourseInstance(courseInstanceRequest);
        return ResponseEntity.ok(ApiResponse.ok("Create new course successfully", result));
    }

    @PostMapping("/courses-instance/updateStatus")
    public ResponseEntity<ApiResponse<CourseInstanceUpdateStatusResponse>> updateStatusCourseInstance(@RequestBody CourseInstanceUpdateStatusRequest courseInstanceUpdateStatusRequest) {
        CourseInstanceUpdateStatusResponse result = courseInstanceService.updateStatusCourseInstance(courseInstanceUpdateStatusRequest);
        return ResponseEntity.ok(ApiResponse.ok("Update course instance status successfully", result));
    }

    @DeleteMapping("/courses-instance/delete")
    public ResponseEntity<ApiResponse<Void>>  deleteCourseInstance(@RequestParam String courseInstanceId) {
        courseInstanceService.deleteCourseInstance(courseInstanceId);
        return ResponseEntity.ok(ApiResponse.ok("Delete course instance successfully"));
    }

    @PostMapping("/courses-instance/archive")
    public ResponseEntity<ApiResponse<Void>> archiveCourseInstance(@RequestParam String courseInstanceId) {
        courseInstanceService.archiveCourseInstance(courseInstanceId);
        return ResponseEntity.ok(ApiResponse.ok("Archive course instance successfully"));
    }

    @GetMapping("/courses-instance/getDetails")
    public ResponseEntity<ApiResponse<CourseInstanceResponse>> getCourseInstanceDetails(@RequestParam String courseInstanceId) {
        CourseInstanceResponse result = courseInstanceService.getCourseInstanceDetails(courseInstanceId);
        return ResponseEntity.ok(ApiResponse.ok("Get course instance successfully", result));
    }

    @GetMapping("/courses-instance/getDetailsList")
    public ResponseEntity<ApiResponse<Page<CourseInstanceResponse>>> getCourseInstanceDetailsList(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<CourseInstanceResponse> result = courseInstanceService.getAllCourseInstanceDetails(pageable);
        return ResponseEntity.ok(ApiResponse.ok("Get list course instance details successfully", result));
    }

    @PostMapping("/courses-instance/enrollStudent")
    public ResponseEntity<ApiResponse<Void>> enrollStudent(@RequestParam String courseInstanceId,
                                                           @RequestParam String studentId) {
        courseInstanceService.enrollStudent(courseInstanceId, studentId);
        return ResponseEntity.ok(ApiResponse.ok("Enrollment student successfully"));
    }

    @GetMapping("/courses-instance/getAllStudentDetails")
    public ResponseEntity<ApiResponse<Page<UserResponse>>>  getAllStudentDetails(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam String courseInstanceId){
        Page<UserResponse> result = courseInstanceService.getAllStudents(pageable,  courseInstanceId);
        return ResponseEntity.ok(ApiResponse.ok("Get all student details successfully", result));
    }

    @PostMapping("/courses")
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(@RequestBody CourseRequest req) {
        CourseResponse result = courseService.createCourse(req);
        return ResponseEntity.ok(ApiResponse.ok("Create new course successfully", result));
    }

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> getAllCourses(
            @RequestParam(required = false, defaultValue = "ACTIVE") CourseStatusEnum status,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CourseResponse> result = courseService.getAllCourse(keyword, status, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Get list of courses successfully", result));
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourse(@PathVariable String id) {
        try {
            CourseResponse result = courseService.getCourseById(id);
            return ResponseEntity.ok(ApiResponse.ok("Get course successfully", result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/courses/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(@PathVariable String id,
            @RequestBody CourseRequest req) {
        try {
            CourseResponse result = courseService.updateCourse(id, req);
            return ResponseEntity.ok(ApiResponse.ok("Update course successfully", result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable String id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok(ApiResponse.ok("Delete course successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/assign")
    public ResponseEntity<ApiResponse<CourseInstanceResponse>> assignTeacherToCourse(@RequestParam String courseId,
            @RequestParam String teacherId) {
        CourseInstanceResponse res = courseService.assignTeacherToCourse(courseId, teacherId);
        return ResponseEntity.ok(ApiResponse.ok("Assign teacher to course successfully", res));
    }

}
