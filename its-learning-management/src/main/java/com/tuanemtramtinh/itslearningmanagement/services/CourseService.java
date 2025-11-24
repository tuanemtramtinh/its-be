package com.tuanemtramtinh.itslearningmanagement.services;

import com.tuanemtramtinh.itscommon.entity.Course;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseRequest;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseResponse;
import com.tuanemtramtinh.itslearningmanagement.mapper.CourseResponseMapper;
import com.tuanemtramtinh.itslearningmanagement.repositories.CourseRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseResponseMapper courseResponseMapper;

    public CourseService(CourseRepository courseRepository, CourseResponseMapper courseResponseMapper) {
        this.courseResponseMapper = courseResponseMapper;
        this.courseRepository = courseRepository;
    }

    public CourseResponse createCourse(CourseRequest req) {
        Course newCourse = Course.builder().title(req.getTitle()).code(req.getCode())
                .description(req.getDescription()).credit(3).status("ACTIVE").createdAt(new Date())
                .updatedAt(new Date()).build();

        if (newCourse == null) {
            throw new RuntimeException("Error when create New Course");
        }

        newCourse = courseRepository.save(newCourse);
        return courseResponseMapper.toDTO(newCourse);
    }

    public List<CourseResponse> getAllCourse() {
        List<Course> courses = courseRepository.findAll();
        return courseResponseMapper.toDTOList(courses);
    }

    public CourseResponse getCourseById(String id) {
        if (id == null) {
            throw new RuntimeException("Course Id is null");
        }

        Course course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            throw new RuntimeException("Course with id " + id + " not found");
        }
        return CourseResponse.builder().id(id).title(course.getTitle()).code(course.getCode())
                .status(course.getStatus()).build();
    }

    public void deleteCourse(String id) {
        if (id == null) {
            throw new RuntimeException("Course Id is null");
        }

        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course with id " + id + " does not exist");
        }

        courseRepository.deleteById(id);
    }

    public CourseResponse updateCourse(String id, CourseRequest req) {
        if (id == null) {
            throw new RuntimeException("Course Id is null");
        }

        Course course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            throw new RuntimeException("Course with id " + id + " not found");
        }

        course.setTitle(req.getTitle());
        course.setCode(req.getCode());
        course.setDescription(req.getDescription());
        course.setCredit(req.getCredit());
        course.setStatus("ACTIVE");
        course = courseRepository.save(course);

        return courseResponseMapper.toDTO(course);
    }

}
