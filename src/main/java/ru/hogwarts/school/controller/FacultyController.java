package ru.hogwarts.school.controller;

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
    /** Create http://localhost:8080/faculty **/
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("{id}")
    /** Read http://localhost:8080/faculty/{id} **/
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable Long id) {
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PutMapping("/{id}")
    /** Update http://localhost:8080/faculty **/
    public ResponseEntity<Faculty> editFacultyInfo(@PathVariable Long id, @RequestBody Faculty faculty) {
        faculty.setId(id);
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}")
    /** Delete http://localhost:8080/faculty/{id} **/
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    /** Read(get all) http://localhost:8080/faculty/ **/
    public ResponseEntity<Collection<Faculty>> getAllFaculties() {
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    @GetMapping("/find")
    /** Read(find by name or color) http://localhost:8080/faculty/find/{name or color} **/
    public ResponseEntity<Collection<Faculty>> findByNameOrColor(@RequestParam String request) {
        return ResponseEntity.ok(facultyService.findByNameOrColor(request));
    }

    @GetMapping("/get_student/{id}")
    /** Read(get all students from faculties ID) http://localhost:8080/faculty/get_student/{id} **/
    public ResponseEntity<Collection<Student>> getStudentsByFacultyId(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getStudentsByFacultyId(id));
    }

    @GetMapping("/get_most_longest_name")
    /** Read(get all students from faculties ID) http://localhost:8080/faculty/get_most_longest_name **/
    public ResponseEntity<Collection<String>> getMostLongestName() {
        return ResponseEntity.ok(facultyService.getMostLongestName());
    }
}
