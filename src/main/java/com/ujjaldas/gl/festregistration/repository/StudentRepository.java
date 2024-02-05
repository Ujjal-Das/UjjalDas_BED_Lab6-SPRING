package com.ujjaldas.gl.festregistration.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ujjaldas.gl.festregistration.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
