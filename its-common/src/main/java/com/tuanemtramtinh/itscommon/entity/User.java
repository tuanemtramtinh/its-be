package com.tuanemtramtinh.itscommon.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tuanemtramtinh.itscommon.enums.RoleEnum;
import com.tuanemtramtinh.itscommon.enums.UserStatusEnum;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private RoleEnum role;
    private UserStatusEnum status;
    private List<String> listCourseInstance;
}
