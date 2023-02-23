package com.attendances.AttendancesAppBackend.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// decorator to map with the database
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    // classroom id as primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Date
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate studentIncome;

    // Income and exit confirmation
    /*
    * To specify column name
    * */
    @Column(name = "income_confirmation")
    private Boolean incomeConfirmation;
    @Column(name = "exit_confirmation")
    private Boolean exitConfirmation;

    /*
    * Attendance will have a 'many-to-one' relation with Student
    * */
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
