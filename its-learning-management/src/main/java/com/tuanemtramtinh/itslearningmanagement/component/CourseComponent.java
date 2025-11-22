package com.tuanemtramtinh.itslearningmanagement.component;

import com.tuanemtramtinh.entity.Course;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseRequest;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseResponse;
import com.tuanemtramtinh.itslearningmanagement.repositories.CourseRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CourseComponent {

    private final CourseRepository courseRepository;

    public CourseComponent(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseResponse createCourse(CourseRequest req) {
        try {
            Course newCourse = Course.builder().title(req.getTitle()).code(req.getCode()).description(req.getDescription()).credit(3).status("ACTIVE").createdAt(new Date()).updatedAt(new Date()).build();
            newCourse = courseRepository.save(newCourse);
            return CourseResponse.builder().id(newCourse.getId()).title(newCourse.getTitle()).code(newCourse.getCode()).description(newCourse.getDescription()).status(newCourse.getStatus()).build();
        } catch (Exception e) {
            throw new RuntimeException("Error when create new course " + e.getMessage());
        }
    }

    public List<CourseResponse> getAllCourse() {
        List<Course> courses = courseRepository.findAll();
        List<CourseResponse> courseResponses = new ArrayList<>();
        for (Course course : courses) {
            courseResponses.add(CourseResponse.builder().id(course.getId()).title(course.getTitle()).code(course.getCode()).status(course.getStatus()).build());
        }
        return courseResponses;
    }

    public void deleteCourse(String id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course with id " + id + " does not exist");
        }

        courseRepository.deleteById(id);
    }
}
