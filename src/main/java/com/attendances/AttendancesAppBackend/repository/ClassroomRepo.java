package com.attendances.AttendancesAppBackend.repository;

import com.attendances.AttendancesAppBackend.models.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepo extends JpaRepository<Classroom, Long> {
}
