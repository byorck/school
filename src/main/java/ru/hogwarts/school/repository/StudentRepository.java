package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAge(int age);

    Collection<Student> findByAgeBetween(int ageMax, int ageMin);

    Collection<Student> findByFacultyId(Long facultyId);

    @Query(value = "SELECT count(*) as student from student", nativeQuery = true)
    Integer countOfAllStudents();

    @Query(value = "SELECT avg(age) as age from student", nativeQuery = true)
    Double averageAgeOfAllStudents();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5;", nativeQuery = true)
    Collection<Student> getLastFiveStudents();


}
