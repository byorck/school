package ru.hogwarts.school.controller;

import org.assertj.core.api.AssertionsForClassTypes;
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

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private Faculty testFaculty;

    @BeforeEach
    void setUp() {
        testFaculty = new Faculty();
        testFaculty.setName("FacultyTest");
        testFaculty.setColor("ColorTest");
        testFaculty = facultyRepository.save(testFaculty);
    }

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

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
    }


    @Test
    void getFacultyInfo() throws Exception {
        Faculty foundFaculty = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/" + testFaculty.getId(), Faculty.class);

        assertThat(foundFaculty).isNotNull();
        assertThat(foundFaculty.getName()).isEqualTo(testFaculty.getName());
        assertThat(foundFaculty.getColor()).isEqualTo(testFaculty.getColor());
    }

    @Test
    void deleteFaculty() throws Exception {
        AssertionsForClassTypes.assertThat(testFaculty).isNotNull();

        restTemplate.delete("http://localhost:" + port + "/faculty/" + testFaculty.getId());
        Faculty deletedFaculty = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/" + testFaculty.getId(), Faculty.class);

        assertThat(deletedFaculty).isNull();
    }

    @Test
    void editFacultyInfo() throws Exception {
        String updatedName = "UpdatedName";
        String updatedColor = "UpdatedColor";
        testFaculty.setName(updatedName);
        testFaculty.setColor(updatedColor);

        restTemplate.put("http://localhost:" + port + "/faculty/" + testFaculty.getId(), testFaculty);

        Faculty updatedFaculty = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/" + testFaculty.getId(), Faculty.class);

        assertThat(updatedFaculty.getName()).isEqualTo(updatedName);
        assertThat(updatedFaculty.getColor()).isEqualTo(updatedColor);
    }


    @Test
    void getAllFaculties() throws Exception {
        Faculty testFaculty2 = new Faculty();
        testFaculty2.setName("FacultyTest2");
        testFaculty2.setColor("ColorTest2");
        testFaculty2 = facultyRepository.save(testFaculty2);

        Faculty[] faculties = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty", Faculty[].class);

        assertThat(faculties).isNotNull();
        assertThat(faculties.length).isGreaterThanOrEqualTo(2);
    }


    @Test
    void findByNameOrColor() throws Exception {
        Faculty testFaculty2 = new Faculty();
        testFaculty2.setName("FacultyTest2");
        testFaculty2.setColor("ColorTest2");
        testFaculty2 = facultyRepository.save(testFaculty2);

        Faculty[] responseByName = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/find?request=" + testFaculty.getName(), Faculty[].class);
        assertThat(responseByName).isNotEmpty();
        for (Faculty f : responseByName) {
            assertThat(f.getName()).contains(testFaculty.getName());
        }

        Faculty[] responseByColor = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/find?request=" + testFaculty2.getColor(), Faculty[].class);
        assertThat(responseByColor).isNotEmpty();
        for (Faculty f : responseByColor) {
            assertThat(f.getColor()).contains(testFaculty2.getColor());
        }
    }

    @Test
    void getStudentsByFacultyId() throws Exception {
        Map<String, Object> facultyMap = new HashMap<>();
        facultyMap.put("id", testFaculty.getId());
        Map<String, Object> studentMap = new HashMap<>();
        studentMap.put("name", "StudentTest");
        studentMap.put("age", 21);
        studentMap.put("faculty", facultyMap);

        Student createdStudent = new Student();
        createdStudent.setName("StudentTest");
        createdStudent.setAge(21);
        createdStudent.setFaculty(testFaculty);
        createdStudent = studentRepository.save(createdStudent);

        Student[] studentsArray = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/get_student/" + testFaculty.getId(), Student[].class);

        assertThat(studentsArray).isNotNull();
        assertThat(studentsArray).extracting(Student::getId).contains(createdStudent.getId());
    }
}
    