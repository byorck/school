package ru.hogwarts.school.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setName("FacultyTest");
        testStudent.setAge(22);
        testStudent = studentRepository.save(testStudent);
    }

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    void createStudent() {
        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/student", testStudent, Student.class);

        assertThat(createdStudent).isNotNull();
        assertThat(createdStudent.getId()).isNotNull();
        assertThat(createdStudent.getName()).isEqualTo(testStudent.getName());
        assertThat(createdStudent.getAge()).isEqualTo(testStudent.getAge());

        restTemplate.delete("http://localhost:" + port + "/student/" + createdStudent.getId());
    }

    @Test
    void deleteStudent() {
        restTemplate.delete("http://localhost:" + port + "/student/" + testStudent.getId());

        Student deletedStudent = restTemplate.getForObject(
                "http://localhost:" + port + "/student/" + testStudent.getId(), Student.class);

        assertThat(deletedStudent).isNull();
    }

    @Test
    void getStudentInfo() {
        Student getStudent = restTemplate.getForObject(
                "http://localhost:" + port + "/student/" + testStudent.getId(), Student.class);

        assertThat(getStudent.getName()).isEqualTo(testStudent.getName());
        assertThat(getStudent.getAge()).isEqualTo(testStudent.getAge());
        assertThat(getStudent.getId()).isEqualTo(testStudent.getId());
    }

    @Test
    void editStudentInfo() {

        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/student", testStudent, Student.class);
        assertThat(createdStudent).isNotNull();

        String updatedName = "UpdatedName";
        int updatedAge = 24;
        createdStudent.setName(updatedName);
        createdStudent.setAge(updatedAge);

        restTemplate.put("http://localhost:" + port + "/student/" + createdStudent.getId(), createdStudent);

        Student foundStudent = restTemplate.getForObject(
                "http://localhost:" + port + "/student/" + createdStudent.getId(), Student.class);
        assertThat(foundStudent.getName()).isEqualTo(updatedName);
        assertThat(foundStudent.getAge()).isEqualTo(updatedAge);
    }

    @Test
    void getAllStudent() {
        Student testStudent2 = new Student();
        testStudent2.setName("FacultyTest");
        testStudent2.setAge(22);
        testStudent2 = studentRepository.save(testStudent2);

        Student[] students = restTemplate.getForObject(
                "http://localhost:" + port + "/student", Student[].class);

        assertThat(students).isNotNull();
        assertThat(students.length).isGreaterThanOrEqualTo(2);
    }

    @Test
    void findByAge() {
        Student[] foundStudents = restTemplate.getForObject(
                "http://localhost:" + port + "/student/find/" + testStudent.getAge(), Student[].class);

        assertThat(foundStudents).isNotNull();
        assertThat(Arrays.stream(foundStudents)
                .anyMatch(s -> s.getId().equals(testStudent.getId())))
                .isTrue();
    }

    @Test
    void findByAgeBetween() {
        int ageMin = 20;
        int ageMax = 23;

        Student[] foundStudents = restTemplate.getForObject(
                "http://localhost:" + port + "/student/find/age_between?ageMin=" + ageMin + "&ageMax=" + ageMax, Student[].class);

        assertThat(foundStudents).isNotNull();
        assertThat(Arrays.stream(foundStudents)
                .anyMatch(s -> s.getId().equals(testStudent.getId())))
                .isTrue();
    }

    @Test
    void getFacultyByStudentId() {
        Faculty faculty = new Faculty();
        faculty.setName("FacultyTest");
        faculty.setColor("ColorTest");
        faculty = facultyRepository.save(faculty);
        testStudent.setFaculty(faculty);
        testStudent = studentRepository.save(testStudent);

        Faculty facultyFromResponse = restTemplate.getForObject(
                "http://localhost:" + port + "/student/get_faculty/" + testStudent.getId(), Faculty.class);

        assertThat(facultyFromResponse).isNotNull();
        assertThat(facultyFromResponse.getId()).isEqualTo(faculty.getId());
        assertThat(facultyFromResponse.getName()).isEqualTo(faculty.getName());
        assertThat(facultyFromResponse.getColor()).isEqualTo(faculty.getColor());
    }
}