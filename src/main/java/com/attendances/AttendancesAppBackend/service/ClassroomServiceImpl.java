package com.attendances.AttendancesAppBackend.service;

import com.attendances.AttendancesAppBackend.models.Attendance;
import com.attendances.AttendancesAppBackend.models.Classroom;
import com.attendances.AttendancesAppBackend.models.Student;
import com.attendances.AttendancesAppBackend.repository.AttendanceRepo;
import com.attendances.AttendancesAppBackend.repository.ClassroomRepo;
import com.attendances.AttendancesAppBackend.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
// Generate automatically a constructor
@RequiredArgsConstructor
// To use all the final variables in the methods
@Transactional
public class ClassroomServiceImpl implements ClassroomService{

    // variables with final will be passed through the constructor
    private final AttendanceRepo attendanceRepo;
    private final StudentRepo studentRepo;
    private final ClassroomRepo classroomRepo;


    // ClassroomService Methods to define

    @Override
    public Optional<Classroom> searchClassroomById(Long id) {
        // calling Jpa method extended in classroomRepo class to  find by id
        return classroomRepo.findById(id);
    }

    @Override
    public Optional<Attendance> searchAttendanceByCode(String code) {
        // create a local variable wit the actual date
        LocalDate actualDate = LocalDate.now();
        // calling AttendanceRepo method
        return attendanceRepo.findByStudent_StudentCodeAndStudentIncome(code, actualDate);
    }

    @Override
    public Optional<Student> searchStudentByCode(String code) {
        // calling StudentRepo method
        return studentRepo.findByStudentCode(code);
    }

    @Override
    public void registerAttendance(Attendance attendance) {
        // set values to the class
        LocalDate actualDate = LocalDate.now();
        attendance.setStudentIncome(actualDate);
        attendance.setIncomeConfirmation(true);
        attendance.setExitConfirmation(false);

        // save into the database
        attendanceRepo.save(attendance);
    }

    @Override
    public void updateAttendance(Attendance attendance) {
        // update an attendance is to inform that student has gone so 'exitConfirmation' must be true
        attendance.setExitConfirmation(true);

        // save into the database
        attendanceRepo.save(attendance);
    }
}
