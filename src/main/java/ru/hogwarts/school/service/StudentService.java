package ru.hogwarts.school.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;


    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student) {
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            Faculty faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found with id " + student.getFaculty().getId()));
            student.setFaculty(faculty);
        } else {
            student.setFaculty(null);
        }
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(Student student) {
        return createStudent(student);
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Collection<Student> findByAge(int age) {
        return studentRepository.findByAge(age).stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    public Collection<Student> findByAgeBetween(int ageMax, int ageMin) {
        return studentRepository.findByAgeBetween(ageMax, ageMin);
    }

    public Faculty getFacultyByStudentId(Long studentId) {
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }
}

