package com.attendances.AttendancesAppBackend.service;

import com.attendances.AttendancesAppBackend.models.Attendance;
import com.attendances.AttendancesAppBackend.models.Classroom;
import com.attendances.AttendancesAppBackend.models.Student;

import java.util.Optional;

public interface ClassroomService {
    /*
    * Declared methods to define in the implementation class
    * */
    Optional<Classroom> searchClassroomById(Long id);
    Optional<Attendance> searchAttendanceByCode(String code);
    Optional<Student> searchStudentByCode(String code);

    void registerAttendance(Attendance attendance);
    void updateAttendance(Attendance attendance);

}
