package com.tuanemtramtinh.itslearningmanagement.services;

import com.tuanemtramtinh.itscommon.entity.Course;
import com.tuanemtramtinh.itscommon.entity.CourseInstance;
import com.tuanemtramtinh.itscommon.entity.User;
import com.tuanemtramtinh.itscommon.enums.CourseInstanceEnum;
import com.tuanemtramtinh.itscommon.enums.CourseStatusEnum;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseInstanceResponse;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseRequest;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseResponse;
import com.tuanemtramtinh.itslearningmanagement.mapper.CourseInstanceResponseMapper;
import com.tuanemtramtinh.itslearningmanagement.mapper.CourseResponseMapper;
import com.tuanemtramtinh.itslearningmanagement.repositories.CourseInstanceRepository;
import com.tuanemtramtinh.itslearningmanagement.repositories.CourseRepository;
import com.tuanemtramtinh.itslearningmanagement.repositories.UserRepository;

import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

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
                .description(req.getDescription()).credit(3).status(CourseStatusEnum.ACTIVE).createdAt(new Date())
                .updatedAt(new Date()).build();

        if (newCourse == null) {
            throw new RuntimeException("Error when create New Course");
        }

        newCourse = courseRepository.save(newCourse);
        return courseResponseMapper.toDTO(newCourse);
    }

    public Page<CourseResponse> getAllCourse(String keyword, CourseStatusEnum status, Pageable pageable) {

        Page<Course> courseList;

        if (keyword == null || keyword.isBlank()) {
            courseList = courseRepository.findByStatus(status, pageable);
        } else {
            String safeKeyword = keyword == null ? "" : Pattern.quote(keyword);
            String regex = ".*" + safeKeyword + ".*";
            courseList = courseRepository.searchByKeyword(status, regex, pageable);
        }

        return courseResponseMapper.toDTOPage(courseList);
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
        course.setStatus(CourseStatusEnum.ACTIVE);
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
