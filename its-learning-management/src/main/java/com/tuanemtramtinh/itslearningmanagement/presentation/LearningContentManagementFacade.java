package com.tuanemtramtinh.itslearningmanagement.presentation;

import com.tuanemtramtinh.itslearningmanagement.services.CourseService;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseRequest;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseResponse;
import com.tuanemtramtinh.itscommon.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LearningContentManagementFacade {

    private final CourseService courseService;

    public LearningContentManagementFacade(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/")
    public String index() {
        return "Hello World!";
    }

    @GetMapping("/test")
    public String testAuthentication() {
        return "Hello World! Auth";
    }

    @PostMapping("/courses")
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(@RequestBody CourseRequest req) {
        CourseResponse result = courseService.createCourse(req);
        return ResponseEntity.ok(ApiResponse.ok("Create new course successfully", result));
    }

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getAllCourses() {
        List<CourseResponse> result = courseService.getAllCourse();
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
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(@PathVariable String id, @RequestBody CourseRequest req) {
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

}
