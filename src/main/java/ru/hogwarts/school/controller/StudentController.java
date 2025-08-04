package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Создать студента")
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("{id}")
    @Operation(summary = "Получить информацию о студенте по ID")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить информацию о студенте по его ID")
    public ResponseEntity<Student> editStudentInfo(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удалить студента по его ID")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Получить список всех студентов")
    public ResponseEntity<Collection<Student>> getAllStudent() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/find/{age}")
    @Operation(summary = "Найти всех студентов, соответствующих возрасту")
    public ResponseEntity<Collection<Student>> findByAge(@PathVariable int age) {
        return ResponseEntity.ok(studentService.findByAge(age));
    }

    @GetMapping("/find/age_between")
    @Operation(summary = "Найти всех студентов, соответствующих возрасту в диапазоне")
    public ResponseEntity<Collection<Student>> findByAgeBetween(@RequestParam int ageMin, @RequestParam int ageMax) {
        return ResponseEntity.ok(studentService.findByAgeBetween(ageMin, ageMax));
    }

    @GetMapping("/get_faculty/{id}")
    @Operation(summary = "Получить информацию о факультете студента по его ID")
    public ResponseEntity<Faculty> getFacultyByStudentId(@PathVariable Long id) {
        Faculty faculty = studentService.getFacultyByStudentId(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/count_of_all_students")
    @Operation(summary = "Получить число всех студентов")
    public Integer countOfAllStudent() {
        return studentService.countOfAllStudents();
    }

    @GetMapping("/average_age_of_all_students")
    @Operation(summary = "Получить средний возраст всех студентов")
    public Double averageAgeOfAllStudents() {
        return studentService.averageAgeOfAllStudents();
    }

    @GetMapping("/get_last_five_students")
    @Operation(summary = "Получить пять последних студентов")
    public Collection<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }
    @GetMapping("/print-parallel")
    @Operation(summary = "Получить всех студентов(в параллельном режиме)")
    public void getAllStudentsWithParallelThreads() {
        studentService.getAllStudentsWithParallelThreads();
    }

    @GetMapping("/print-synchronized")
    @Operation(summary = "Получить всех студентов(в параллельном режиме с синхронизацией)")
    public void getAllStudentsWithParallelThreadsSynchronized() {
        studentService.getAllStudentsWithParallelThreadsSynchronized();
    }

    @GetMapping("/get_students_whose_name_starts_with_A")
    @Operation(summary = "Получить всех студентов, чье имя начинается с буквы \"А\"")
    public ResponseEntity<Collection<Student>> getStudentsWhoseNameStartsWithA() {
        return ResponseEntity.ok(studentService.getStudentsWhoseNameStartsWithA());
    }

    @GetMapping("/get_students_average_age")
    @Operation(summary = "Получить средний возраст всех студентов")
    public ResponseEntity<Double> getStudentsAverageAge() {
        return ResponseEntity.ok(studentService.getStudentsAverageAge());
    }
}
