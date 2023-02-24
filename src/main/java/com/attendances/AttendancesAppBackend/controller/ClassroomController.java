package com.attendances.AttendancesAppBackend.controller;

import com.attendances.AttendancesAppBackend.models.Attendance;
import com.attendances.AttendancesAppBackend.models.Classroom;
import com.attendances.AttendancesAppBackend.models.Student;
import com.attendances.AttendancesAppBackend.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/*
* Annotation for permitting cross-origin requests on specific handler classes and/or handler methods
* */
@CrossOrigin
/*
* Generates a constructor with required arguments. Required arguments are final fields and fields with constraints such as @NonNull.
* */
@RequiredArgsConstructor
/*
* A convenience annotation that is itself annotated with @Controller and @ResponseBody.
Types that carry this annotation are treated as controllers where @RequestMapping methods assume @ResponseBody semantics by default.
* */
@RestController
/*
* Annotation for mapping web requests onto methods in request-handling classes with flexible method signatures
* */
@RequestMapping("/api/classrooms")
public class ClassroomController {
    // We are going to use our ClassroomService where there are all our methods
    private final ClassroomService classService;

    // SEARCH CLASSROOM BY ID
    @GetMapping("/search-classroom/{id}") // POST - "/api/classrooms/search-classroom/{id}"
    public ResponseEntity<?> searchById(@PathVariable Long id){
        // calling our classroom service method and store in a local variable
        Optional<Classroom> foundClassroom = classService.searchClassroomById(id);

        // validate if there is a found classroom
        if(foundClassroom.isEmpty()){
            String statusMessage = "Classroom not found!";
            return new ResponseEntity<>(statusMessage, HttpStatus.NOT_FOUND);
        }

        // if a classroom is found return it = HTTP status code 200
        return new ResponseEntity<>(foundClassroom.get(), HttpStatus.OK);

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // REGISTER A NEW ATTENDANCE
    @PostMapping("/register-attendance") // "/api/classrooms/register-attendance"
    public ResponseEntity<?> registerAttendance(@RequestBody Attendance attendance){
        // get student code
        String studentCode = attendance.getStudent().getStudentCode();
        // Find student
        Optional<Student> foundStudent = classService.searchStudentByCode(studentCode);

        // validate student
        if(foundStudent.isEmpty()){
            String statusMessage = "Student not found! with code: "+ studentCode;
            // return http response
            return new ResponseEntity<>(statusMessage, HttpStatus.NOT_FOUND);
        }

        // if student was found, search attendance by code
        Optional<Attendance> foundAttendance = classService.searchAttendanceByCode(studentCode);

        if(foundAttendance.isEmpty()){
            String statusMessage = "Classroom could not be registered!";
            // return http response
            return new ResponseEntity<>(statusMessage, HttpStatus.BAD_REQUEST);
        }

        // set student int oattendance
        attendance.setStudent(foundStudent.get());
        classService.registerAttendance(attendance);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // UPDATE ATTENDANCE
    @PutMapping("/register-exit-confirmation") // PUT - "/api/classrooms/register-exit-confirmation"
    public ResponseEntity<?> updateAttendance(@RequestBody Attendance attendance){
        String studentCode = attendance.getStudent().getStudentCode();

        // find student by code
        Optional<Student> foundStudent = classService.searchStudentByCode(studentCode);

        if(foundStudent.isEmpty()){
            String statusMessage = "Student not found by code: "+studentCode;
            return new ResponseEntity<>(statusMessage, HttpStatus.NOT_FOUND);
        }
        // find attendance by code
        Optional<Attendance> foundAttendance = classService.searchAttendanceByCode(studentCode);
        if(foundAttendance.isEmpty()){
            String statusMessage = "Attendance not found by code";
            return new ResponseEntity<>(statusMessage, HttpStatus.BAD_REQUEST);
        }
        // update attendance
        classService.updateAttendance(foundAttendance.get());
        // return http request
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // GET ATTENDANCE BY STUDENT CODE
    @GetMapping("/search-attendance/{studentCode}") // GET - "/api/classrooms/search-attendance/{studentId}"
    public ResponseEntity<?> searchAttendanceByCode(@PathVariable String studentCode){
        // get attendance by student code
        Optional<Attendance> foundAttendance = classService.searchAttendanceByCode(studentCode);
        // validate if attendance was found
        if(foundAttendance.isEmpty()){
            String statusMessage = "Attendance not found by code: "+studentCode;
            return new ResponseEntity<>(statusMessage, HttpStatus.NOT_FOUND);
        }
        // If attendance was found return it
        return new ResponseEntity<>(foundAttendance.get(), HttpStatus.OK);
    }
}
