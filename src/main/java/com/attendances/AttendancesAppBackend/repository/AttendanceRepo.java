package com.attendances.AttendancesAppBackend.repository;

import com.attendances.AttendancesAppBackend.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepo extends JpaRepository<Attendance, Long> {
    /*
    * Method to search by student(student_code) and attendance(studentIncome)
    * Name must follow the next format:
    * findBy + table + _tableAttribute + And + _secondParametter
    * "findByStudent_StudentCodeAndStudentIncome"
    * We pass as arguments our studentCode as "String code" and the LocalDate studentIncome as "LocalDate incomeDate"
    *
    * More info: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repository-query-keywords
    *
    * Attendance could not exist so it must be optional
    * */
    Optional<Attendance> findByStudent_StudentCodeAndStudentIncome(String code, LocalDate incomeDate);
}
