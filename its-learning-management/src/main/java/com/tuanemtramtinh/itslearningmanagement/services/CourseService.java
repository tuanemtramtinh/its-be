package com.tuanemtramtinh.itslearningmanagement.services;

import com.tuanemtramtinh.itscommon.entity.Course;
import com.tuanemtramtinh.itscommon.entity.CourseInstance;
import com.tuanemtramtinh.itscommon.entity.User;
import com.tuanemtramtinh.itscommon.enums.CourseInstanceEnum;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseInstanceResponse;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseRequest;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseResponse;
import com.tuanemtramtinh.itslearningmanagement.mapper.CourseInstanceResponseMapper;
import com.tuanemtramtinh.itslearningmanagement.mapper.CourseResponseMapper;
import com.tuanemtramtinh.itslearningmanagement.repositories.CourseInstanceRepository;
import com.tuanemtramtinh.itslearningmanagement.repositories.CourseRepository;
import com.tuanemtramtinh.itslearningmanagement.repositories.UserRepository;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseInstanceRepository courseInstanceRepository;
    private final UserRepository userRepository;

    private final CourseResponseMapper courseResponseMapper;
    private final CourseInstanceResponseMapper courseInstanceResponseMapper;

    public CourseService(CourseRepository courseRepository, CourseInstanceRepository courseInstanceRepository,
            UserRepository userRepository, CourseResponseMapper courseResponseMapper,
            CourseInstanceResponseMapper courseInstanceResponseMapper) {
        this.courseRepository = courseRepository;
        this.courseInstanceRepository = courseInstanceRepository;
        this.userRepository = userRepository;
        this.courseResponseMapper = courseResponseMapper;
        this.courseInstanceResponseMapper = courseInstanceResponseMapper;
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

    public CourseInstanceResponse assignTeacherToCourse(String courseId, String teacherId) {

        if (teacherId == null)
            throw new RuntimeException("TeacherId is null");
        User currentUser = userRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("User not found"));

        if (courseId == null)
            throw new RuntimeException("CourseId is null");

        Course currentCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        boolean isAssigned = courseInstanceRepository.existsByCourseIdAndTeacherId(courseId, teacherId);

        if (isAssigned)
            throw new RuntimeException("This teacher is already assigned to the Course");

        CourseInstance newCourseInstance = CourseInstance.builder().courseId(courseId).teacherId(teacherId)
                .createdAt(new Date()).status(CourseInstanceEnum.ACTIVE).build();

        newCourseInstance = courseInstanceRepository.save(newCourseInstance);

        return courseInstanceResponseMapper.toDTO(newCourseInstance, currentCourse, currentUser);
    }
}
