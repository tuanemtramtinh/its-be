package com.tuanemtramtinh.itslearningmanagement.presentation;

import com.tuanemtramtinh.itslearningmanagement.component.CourseComponent;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseRequest;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseResponse;
import com.tuanemtramtinh.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LearningContentManagementFacade {

    private final CourseComponent courseComponent;

    public LearningContentManagementFacade(CourseComponent courseComponent) {
        this.courseComponent = courseComponent;
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
        CourseResponse result = courseComponent.createCourse(req);
        return ResponseEntity.ok(ApiResponse.ok("Create new course successfully", result));
    }

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getAllCourses() {
        List<CourseResponse> result = courseComponent.getAllCourse();
        return ResponseEntity.ok(ApiResponse.ok("Get list of courses successfully", result));
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable String id) {
        try {
            courseComponent.deleteCourse(id);
            return ResponseEntity.ok(ApiResponse.ok("Delete course successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

}
