package com.attendances.AttendancesAppBackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// decorator to map with the database
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Classroom {

    // classroom id as primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*
    * A classroom has a name and a Student list
    * */
    private String classroomName;
    /*
    * Classroom and Student has a 'one-to-many' relation
    * */
    @OneToMany
    /*
    * Student column will have a column to store the classroom with the classroom id
    * */
    @JoinColumn(name = "classroom_id")
    private List<Student> students;

}
