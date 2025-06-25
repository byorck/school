package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    /** Create http://localhost:8080/student **/
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("{id}")
    /** Read http://localhost:8080/student/{id} **/
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping
    /** Update http://localhost:8080/student **/
    public ResponseEntity<Student> editStudentInfo(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    /** Delete http://localhost:8080/student/{id} **/
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    /** Read(get all) http://localhost:8080/student/ **/
    public ResponseEntity<Collection<Student>> getAllStudent() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/find/{age}")
    /** Read(find by age) http://localhost:8080/student/find/{age} **/
    public ResponseEntity<Collection<Student>> findByAge(@PathVariable int age) {
        return ResponseEntity.ok(studentService.findByAge(age));
    }

    @GetMapping("/find/age_between")
    /** Read(find by age between range) http://localhost:8080/student/find/age_between?ageMin=12&ageMax=15 **/
    public ResponseEntity<Collection<Student>> findByAgeBetween(@RequestParam int ageMin, @RequestParam int ageMax) {
        return ResponseEntity.ok(studentService.findByAgeBetween(ageMin, ageMax));
    }

    @GetMapping("/get_faculty/{id}")
    /** Read(get faculty by student ID) http://localhost:8080/student/get_faculty/{id} **/
    public ResponseEntity<Faculty> getFacultyByStudentId(@PathVariable Long id) {
        Faculty faculty = studentService.getFacultyByStudentId(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }
}
