package com.attendances.AttendancesAppBackend.repository;

import com.attendances.AttendancesAppBackend.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student, Long> {
    /*
    * Method to find a student by student code
    * Optional (student could not exist)
    * */
    Optional<Student> findByStudentCode(String code);
}
