package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() throws Exception {
        assertThat(facultyController).isNotNull();
    }

    @Test
    void createFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FacultyTest");
        faculty.setColor("ColorTest");
        Faculty createdFaculty = restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", faculty, Faculty.class);

        assertThat(createdFaculty).isNotNull();

        restTemplate.delete("http://localhost:" + port + "/faculty/" + createdFaculty.getId());
    }


    @Test
    void getFacultyInfo() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FacultyTest");
        faculty.setColor("ColorTest");
        Faculty createdFaculty = restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Faculty foundFaculty = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/" + createdFaculty.getId(), Faculty.class);

        assertThat(foundFaculty).isNotNull();
        assertThat(foundFaculty.getName()).isEqualTo(createdFaculty.getName());
        assertThat(foundFaculty.getColor()).isEqualTo(createdFaculty.getColor());

        restTemplate.delete("http://localhost:" + port + "/faculty/" + createdFaculty.getId());
    }

    @Test
    void deleteFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FacultyTest");
        faculty.setColor("ColorTest");
        Faculty createdFaculty = restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", faculty, Faculty.class);

        assertThat(createdFaculty).isNotNull();

        restTemplate.delete("http://localhost:" + port + "/faculty/" + createdFaculty.getId());
        Faculty deletedFaculty = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/" + createdFaculty.getId(), Faculty.class);

        assertThat(deletedFaculty).isNull();
    }

    @Test
    void editFacultyInfo() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FacultyTest");
        faculty.setColor("ColorTest");
        Faculty createdFaculty = restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", faculty, Faculty.class);

        assertThat(createdFaculty).isNotNull();

        String updatedName = "UpdatedName";
        String updatedColor = "UpdatedColor";
        createdFaculty.setName(updatedName);
        createdFaculty.setColor(updatedColor);

        restTemplate.put("http://localhost:" + port + "/faculty/" + createdFaculty.getId(), createdFaculty);

        Faculty foundFaculty = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/" + createdFaculty.getId(), Faculty.class);
        assertThat(foundFaculty.getName()).isEqualTo(updatedName);
        assertThat(foundFaculty.getColor()).isEqualTo(updatedColor);

        restTemplate.delete("http://localhost:" + port + "/faculty/" + createdFaculty.getId());
    }


    @Test
    void getAllFaculties() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setName("FacultyTest1");
        faculty1.setColor("ColorTest1");
        Faculty created1 = restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", faculty1, Faculty.class);

        Faculty faculty2 = new Faculty();
        faculty2.setName("FacultyTest2");
        faculty2.setColor("ColorTest2");
        Faculty created2 = restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", faculty2, Faculty.class);

        Faculty[] faculties = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty", Faculty[].class);

        assertThat(faculties).isNotNull();
        assertThat(faculties.length).isGreaterThanOrEqualTo(2);

        restTemplate.delete("http://localhost:" + port + "/faculty/" + created1.getId());
        restTemplate.delete("http://localhost:" + port + "/faculty/" + created2.getId());
    }


    @Test
    void findByNameOrColor() throws Exception {
        Faculty faculty1 = new Faculty();
        String testName1 = "FacultyTest1";
        String testColor1 = "ColorTest1";
        faculty1.setName(testName1);
        faculty1.setColor(testColor1);
        Faculty created1 = restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", faculty1, Faculty.class);
        Faculty faculty2 = new Faculty();
        String testName2 = "FacultyTest2";
        String testColor2 = "ColorTest2";
        faculty2.setName(testName2);
        faculty2.setColor(testColor2);
        Faculty created2 = restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", faculty2, Faculty.class);

        Faculty[] responseByName = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/find?request=" + testName1, Faculty[].class);
        assertThat(responseByName).isNotEmpty();
        for (Faculty f : responseByName) {
            assertThat(f.getName()).contains(testName1);
        }

        Faculty[] responseByColor = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/find?request=" + testColor2, Faculty[].class);
        assertThat(responseByColor).isNotEmpty();
        for (Faculty f : responseByColor) {
            assertThat(f.getColor()).contains(testColor2);
        }

        restTemplate.delete("http://localhost:" + port + "/faculty/" + created1.getId());
        restTemplate.delete("http://localhost:" + port + "/faculty/" + created2.getId());
    }

    @Test
    void getStudentsByFacultyId() throws Exception {
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

        Student[] studentsArray = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/get_student/" + createdFaculty.getId(), Student[].class);

        assertThat(studentsArray).isNotNull();
        assertThat(studentsArray).extracting(Student::getId).contains(createdStudent.getId());

        restTemplate.delete("http://localhost:" + port + "/student/" + createdStudent.getId());
        restTemplate.delete("http://localhost:" + port + "/faculty/" + createdFaculty.getId());
    }
}
    