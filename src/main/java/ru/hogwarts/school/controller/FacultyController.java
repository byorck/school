package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    @Operation(summary = "Создать факультет")
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("{id}")
    @Operation(summary = "Получить информацию о факультете по его ID")
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable Long id) {
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить информацию о факультете по его ID")
    public ResponseEntity<Faculty> editFacultyInfo(@PathVariable Long id, @RequestBody Faculty faculty) {
        faculty.setId(id);
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удалить факультет по его ID")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Получить все факультеты")
    public ResponseEntity<Collection<Faculty>> getAllFaculties() {
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    @GetMapping("/find")
    @Operation(summary = "Найти факультет по названию или цвету")
    public ResponseEntity<Collection<Faculty>> findByNameOrColor(@RequestParam String request) {
        return ResponseEntity.ok(facultyService.findByNameOrColor(request));
    }

    @GetMapping("/get_students/{id}")
    @Operation(summary = "Получить всех студентов факультета, по его ID")
    public ResponseEntity<Collection<Student>> getStudentsByFacultyId(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getStudentsByFacultyId(id));
    }

    @GetMapping("/get_most_longest_name")
    @Operation(summary = "Получить самое длинное название факультета")
    public ResponseEntity<Collection<String>> getMostLongestName() {
        return ResponseEntity.ok(facultyService.getMostLongestName());
    }
}
