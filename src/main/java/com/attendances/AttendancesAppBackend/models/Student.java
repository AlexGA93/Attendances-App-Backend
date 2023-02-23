package com.attendances.AttendancesAppBackend.models;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// decorator to map with the database
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    /*
    * Specifies the primary key of an entity
    * */
    @Id
    /*
     * Indicates that the persistence provider must assign primary keys for the entity using a database identity column.
     * */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String secondName;
    private String studentCode;

    /*
    * Student has a relation 'one-to-many' to Attendance
    *
    * To establish this relation to attendance (see Attendance java class)
    * we need to specify with the proper @ManyToOne variable name in the 'mappedBy' field
    * */
    @OneToMany(mappedBy = "student")
    // To avoid an infinite loop calling student more than once
    @JsonIgnoreProperties({"student", "hibernateLazyInitializer, 'handler"})
    private List<Attendance> attendances;

}
