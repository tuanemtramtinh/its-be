package com.tuanemtramtinh.itslearningmanagement.services;

import com.tuanemtramtinh.itscommon.dto.UserResponse;
import com.tuanemtramtinh.itscommon.entity.Course;
import com.tuanemtramtinh.itscommon.entity.CourseInstance;
import com.tuanemtramtinh.itscommon.entity.User;
import com.tuanemtramtinh.itscommon.enums.CourseInstanceEnum;
import com.tuanemtramtinh.itscommon.enums.RoleEnum;
import com.tuanemtramtinh.itslearningmanagement.dto.*;
import com.tuanemtramtinh.itslearningmanagement.mapper.CourseInstanceResponseMapper;
import com.tuanemtramtinh.itslearningmanagement.mapper.CourseInstanceUpdateStatusReponseMapper;
import com.tuanemtramtinh.itslearningmanagement.mapper.UserResponseMapper;
import com.tuanemtramtinh.itslearningmanagement.repositories.CourseInstanceRepository;
import com.tuanemtramtinh.itslearningmanagement.repositories.CourseRepository;
import com.tuanemtramtinh.itslearningmanagement.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseInstanceService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseInstanceRepository courseInstanceRepository;

    private final CourseInstanceResponseMapper courseInstanceResponseMapper;
    private final CourseInstanceUpdateStatusReponseMapper courseInstanceUpdateStatusReponseMapper;

    private final UserResponseMapper userResponseMapper;

    public CourseInstanceService(CourseRepository courseRepository,
                                 UserRepository userRepository,
                                 CourseInstanceRepository courseInstanceRepository,
                                 CourseInstanceResponseMapper courseInstanceResponseMapper,
                                 CourseInstanceUpdateStatusReponseMapper courseInstanceUpdateStatusReponseMapper,
                                 UserResponseMapper userResponseMapper) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.courseInstanceRepository = courseInstanceRepository;
        this.courseInstanceResponseMapper = courseInstanceResponseMapper;
        this.courseInstanceUpdateStatusReponseMapper = courseInstanceUpdateStatusReponseMapper;
        this.userResponseMapper = userResponseMapper;
    }

    public CourseInstanceResponse createCourseInstance(CourseInstanceRequest courseInstanceRequest){
        if(courseInstanceRequest.getCourseId() == null) throw new RuntimeException("courseId is null");
        if(courseInstanceRequest.getTeacherId() == null) throw new RuntimeException("teacherId is null");
        if(courseInstanceRequest.getStatus() == null) throw new RuntimeException("status is null");

        Course findCourse = courseRepository.findById(courseInstanceRequest.getCourseId()).orElseThrow(() -> new RuntimeException("course not found"));
        User findUser = userRepository.findById(courseInstanceRequest.getTeacherId()).orElseThrow(() -> new RuntimeException("user not found"));
        if(findUser.getRole() != RoleEnum.TEACHER) throw new RuntimeException("user is not teacher");

        CourseInstance courseInstance = CourseInstance.builder().courseId(courseInstanceRequest.getCourseId())
                                                                .teacherId(courseInstanceRequest.getTeacherId())
                                                                .status(CourseInstanceEnum.ACTIVE).build();

        if(courseInstance == null) throw new RuntimeException("course instance can't not be created");
        courseInstance = courseInstanceRepository.save(courseInstance);
        return courseInstanceResponseMapper.toDTO(courseInstance, findCourse, findUser);
    }

    public CourseInstanceUpdateStatusResponse updateStatusCourseInstance(CourseInstanceUpdateStatusRequest courseInstanceUpdateStatusRequest){
        if(courseInstanceUpdateStatusRequest.getCourseInstanceId() == null) throw new RuntimeException("courseInstanceId is null");
        if(courseInstanceUpdateStatusRequest.getNewStatus() == null) throw new RuntimeException("newStatus is null");

        CourseInstance courseInstance = courseInstanceRepository.findById(courseInstanceUpdateStatusRequest.getCourseInstanceId()).orElseThrow(() -> new RuntimeException("course instance not found"));

        CourseInstanceEnum newStatusEnum = CourseInstanceEnum.valueOf(
                courseInstanceUpdateStatusRequest.getNewStatus()
        );
        courseInstance.setStatus(newStatusEnum);
        courseInstance = courseInstanceRepository.save(courseInstance);

        return courseInstanceUpdateStatusReponseMapper.toDTO(courseInstance);
    }

    public void deleteCourseInstance(String courseInstanceId){
        if(courseInstanceId == null) throw new RuntimeException("courseInstanceId is null");

        CourseInstance courseInstance =  courseInstanceRepository.findById(courseInstanceId).orElseThrow(() -> new RuntimeException("courseInstance not found"));
        courseInstanceRepository.delete(courseInstance);
    }

    public void archiveCourseInstance(String courseInstanceId){
        if(courseInstanceId == null) throw new RuntimeException("courseInstanceId is null");

        CourseInstance courseInstance =  courseInstanceRepository.findById(courseInstanceId).orElseThrow(() -> new RuntimeException("courseInstance not found"));

        courseInstance.setStatus(CourseInstanceEnum.INACTIVE);
        courseInstanceRepository.save(courseInstance);
    }

    public CourseInstanceResponse getCourseInstanceDetails(String courseInstanceId){
        if(courseInstanceId == null) throw new RuntimeException("courseInstanceId is null");

        CourseInstance courseInstance =  courseInstanceRepository.findById(courseInstanceId).orElseThrow(() -> new RuntimeException("courseInstance not found"));
        Course course = courseRepository.findById(courseInstance.getCourseId()).orElseThrow(() -> new RuntimeException("course not found"));
        User teacher =  userRepository.findById(courseInstance.getTeacherId()).orElseThrow(() -> new RuntimeException("user not found"));

        return courseInstanceResponseMapper.toDTO(courseInstance, course, teacher);
    }

    public Page<CourseInstanceResponse> getAllCourseInstanceDetails(Pageable pageable) {
        Page<CourseInstance> page = courseInstanceRepository.findAll(pageable);

        List<CourseInstanceResponse> dtoList = page.stream()
                .map(ci -> {
                    Course course = courseRepository.findById(ci.getCourseId())
                            .orElseThrow(() -> new RuntimeException("Course not found"));

                    User teacher = userRepository.findById(ci.getTeacherId())
                            .orElseThrow(() -> new RuntimeException("Teacher not found"));

                    return courseInstanceResponseMapper.toDTO(ci, course, teacher);
                })
                .toList();

        return new PageImpl<>(
                dtoList,
                page.getPageable(),
                page.getTotalElements()
        );
    }

    public void enrollStudent(String courseInstanceId, String studentId){
        if(courseInstanceId == null) throw new RuntimeException("courseInstanceId is null");
        if(studentId == null) throw new RuntimeException("studentId is null");

        User student =  userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("student not found"));
        if(student.getRole() != RoleEnum.STUDENT) throw new RuntimeException("user is not student");
        CourseInstance courseInstance =  courseInstanceRepository.findById(courseInstanceId).orElseThrow(() -> new RuntimeException("courseInstance not found"));

        if(userRepository.existsByIdAndListCourseInstanceContaining(studentId, courseInstanceId)) throw new RuntimeException("user already join the course instance");
        List<String> list = student.getListCourseInstance();
        list.add(courseInstanceId);
        student.setListCourseInstance(list);
        userRepository.save(student);
    }

    public Page<UserResponse> getAllStudents(Pageable pageable, String courseInstanceId) {
        Page<User> page = userRepository.findAllByRoleAndListCourseInstanceContains(RoleEnum.STUDENT, courseInstanceId, pageable);

        return userResponseMapper.toDTOPage(page);
    }
}
