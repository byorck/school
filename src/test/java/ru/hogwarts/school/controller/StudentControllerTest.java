package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createStudent() {
        Student studentTest = new Student();
        String studentNameTest = "StudentNameTest";
        int studentAgeTest = 22;
        studentTest.setName(studentNameTest);
        studentTest.setAge(studentAgeTest);

        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/student", studentTest, Student.class);

        assertThat(createdStudent).isNotNull();
        assertThat(createdStudent.getId()).isNotNull();
        assertThat(createdStudent.getName()).isEqualTo(studentNameTest);
        assertThat(createdStudent.getAge()).isEqualTo(studentAgeTest);

        restTemplate.delete("http://localhost:" + port + "/student/" + createdStudent.getId());
    }

    @Test
    void deleteStudent() {
        Student studentTest = new Student();
        String studentNameTest = "StudentNameTest";
        int studentAgeTest = 22;
        studentTest.setName(studentNameTest);
        studentTest.setAge(studentAgeTest);

        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/student", studentTest, Student.class);
        assertThat(createdStudent).isNotNull();

        restTemplate.delete("http://localhost:" + port + "/student/" + createdStudent.getId());

        Student deletedStudent = restTemplate.getForObject(
                "http://localhost:" + port + "/student/" + createdStudent.getId(), Student.class);

        assertThat(deletedStudent).isNull();
    }

    @Test
    void getStudentInfo() {
        Student studentTest = new Student();
        String studentNameTest = "StudentNameTest";
        int studentAgeTest = 22;
        studentTest.setName(studentNameTest);
        studentTest.setAge(studentAgeTest);

        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/student", studentTest, Student.class);
        assertThat(createdStudent).isNotNull();

        Student getStudent = restTemplate.getForObject(
                "http://localhost:" + port + "/student/" + createdStudent.getId(), Student.class);

        assertThat(getStudent.getName()).isEqualTo(studentNameTest);
        assertThat(getStudent.getAge()).isEqualTo(studentAgeTest);
        assertThat(getStudent.getId()).isEqualTo(createdStudent.getId());

        restTemplate.delete("http://localhost:" + port + "/student/" + createdStudent.getId());
    }

    @Test
    void editStudentInfo() {
        Student studentTest = new Student();
        String studentNameTest = "StudentNameTest";
        int studentAgeTest = 22;
        studentTest.setName(studentNameTest);
        studentTest.setAge(studentAgeTest);

        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/student", studentTest, Student.class);
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

        restTemplate.delete("http://localhost:" + port + "/student/" + createdStudent.getId());

    }

    @Test
    void getAllStudent() {
        Student studentTest = new Student();
        String studentNameTest = "StudentNameTest";
        int studentAgeTest = 22;
        studentTest.setName(studentNameTest);
        studentTest.setAge(studentAgeTest);
        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/student", studentTest, Student.class);
        assertThat(createdStudent).isNotNull();

        Student studentTest2 = new Student();
        String studentNameTest2 = "StudentNameTest2";
        int studentAgeTest2 = 24;
        studentTest.setName(studentNameTest2);
        studentTest.setAge(studentAgeTest2);
        Student createdStudent2 = restTemplate.postForObject(
                "http://localhost:" + port + "/student", studentTest2, Student.class);
        assertThat(createdStudent2).isNotNull();

        Student[] students = restTemplate.getForObject(
                "http://localhost:" + port + "/student", Student[].class);

        assertThat(students).isNotNull();
        assertThat(students.length).isGreaterThanOrEqualTo(2);

        restTemplate.delete("http://localhost:" + port + "/student/" + createdStudent.getId());
        restTemplate.delete("http://localhost:" + port + "/student/" + createdStudent2.getId());

    }

    @Test
    void findByAge() {
        String studentNameTest = "StudentNameTest";
        int studentAgeTest = 22;
        Student studentTest = new Student();
        studentTest.setName(studentNameTest);
        studentTest.setAge(studentAgeTest);

        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/student", studentTest, Student.class);
        assertThat(createdStudent).isNotNull();

        Student[] foundStudents = restTemplate.getForObject(
                "http://localhost:" + port + "/student/find/" + studentAgeTest, Student[].class);

        assertThat(foundStudents).isNotNull();
        assertThat(Arrays.stream(foundStudents)
                .anyMatch(s -> s.getId().equals(createdStudent.getId())))
                .isTrue();

        restTemplate.delete("http://localhost:" + port + "/student/" + createdStudent.getId());
    }

    @Test
    void findByAgeBetween() {
        String studentNameTest = "StudentNameTest";
        int studentAgeTest = 22;
        Student studentTest = new Student();
        studentTest.setName(studentNameTest);
        studentTest.setAge(studentAgeTest);
        int ageMin = 20;
        int ageMax = 23;

        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/student", studentTest, Student.class);
        assertThat(createdStudent).isNotNull();

        Student[] foundStudents = restTemplate.getForObject(
                "http://localhost:" + port + "/student/find/age_between?ageMin=" + ageMin + "&ageMax=" + ageMax, Student[].class);

        assertThat(foundStudents).isNotNull();
        assertThat(Arrays.stream(foundStudents)
                .anyMatch(s -> s.getId().equals(createdStudent.getId())))
                .isTrue();

        restTemplate.delete("http://localhost:" + port + "/student/" + createdStudent.getId());
    }

    @Test
    void getFacultyByStudentId() {
        Faculty faculty = new Faculty();
        faculty.setName("FacultyTest");
        faculty.setColor("ColorTest");

        Faculty createdFaculty = restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", faculty, Faculty.class);
        assertThat(createdFaculty).isNotNull();

        Map<String, Object> facultyMap = new HashMap<>();
        facultyMap.put("id", createdFaculty.getId());
        Map<String, Object> studentMap = new HashMap<>();
        studentMap.put("name", "StudentTest");
        studentMap.put("age", 21);
        studentMap.put("faculty", facultyMap);

        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/student", studentMap, Student.class);
        assertThat(createdStudent).isNotNull();

        Faculty facultyFromResponse = restTemplate.getForObject(
                "http://localhost:" + port + "/student/get_faculty/" + createdStudent.getId(), Faculty.class);

        assertThat(facultyFromResponse).isNotNull();
        assertThat(facultyFromResponse.getId()).isEqualTo(createdFaculty.getId());
        assertThat(facultyFromResponse.getName()).isEqualTo(createdFaculty.getName());
        assertThat(facultyFromResponse.getColor()).isEqualTo(createdFaculty.getColor());

        restTemplate.delete("http://localhost:" + port + "/student/" + createdStudent.getId());
        restTemplate.delete("http://localhost:" + port + "/faculty/" + createdFaculty.getId());
    }
}