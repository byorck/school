package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    @MockitoBean
    private StudentService studentService;

    @Test
    void createStudent() throws Exception {
        final String name = "student_name_test";
        int age = 22;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("color", age);

        Student student = new Student();
        student.setName(name);
        student.setAge(age);

        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    void getStudentInfo() throws Exception {
        Student student = new Student();
        student.setId(42L);
        student.setName("student_name_test");
        student.setAge(22);

        when(studentService.findStudent(anyLong())).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}", student.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));
    }

    @Test
    void editStudentInfo() throws Exception {
        final Long id = 43L;
        final String updatedName = "updated_student_name_test";
        final int updatedAge = 22;

        Student updatedStudent = new Student();
        updatedStudent.setId(id);
        updatedStudent.setName(updatedName);
        updatedStudent.setAge(updatedAge);

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", updatedName);
        studentObject.put("age", updatedAge);

        when(studentService.editStudent(any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/{id}", id)
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(updatedName))
                .andExpect(jsonPath("$.age").value(updatedAge));
    }

    @Test
    void deleteStudent() throws Exception {
        Student student = new Student();
        student.setId(42L);
        student.setName("student_name_test");
        student.setAge(22);

        doNothing().when(studentService).deleteStudent(student.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/{id}", student.getId()))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(student.getId());
    }

    @Test
    void getAllStudent() throws Exception {
        Student student1 = new Student();
        student1.setId(42L);
        student1.setName("student_name_test");
        student1.setAge(22);

        Student student2 = new Student();
        student2.setId(43L);
        student2.setName("student_name_test2");
        student2.setAge(23);

        Collection<Student> students = Arrays.asList(student1, student2);

        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(student1.getId().intValue(), student2.getId().intValue())))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(student1.getName(), student2.getName())))
                .andExpect(jsonPath("$[*].age", containsInAnyOrder(student1.getAge(), student2.getAge())));
    }

    @Test
    void findByAge() throws Exception {
        int ageRequest = 22;

        Student student = new Student();
        student.setId(42L);
        student.setName("student_name_test");
        student.setAge(22);

        Collection<Student> studentsByAge = Collections.singletonList(student);

        when(studentService.findByAge(ageRequest)).thenReturn(studentsByAge);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/find/{age}", ageRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(42L))
                .andExpect(jsonPath("$[0].name").value("student_name_test"))
                .andExpect(jsonPath("$[0].age").value(22));
    }

    @Test
    void findByAgeBetween() throws Exception {
        int minAgeRequest = 20;
        int maxAgeRequest = 23;

        Student student = new Student();
        student.setId(42L);
        student.setName("student_name_test");
        student.setAge(22);

        Collection<Student> studentsByAge = Collections.singletonList(student);

        when(studentService.findByAgeBetween(minAgeRequest, maxAgeRequest)).thenReturn(studentsByAge);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/find/age_between")
                        .param("ageMin", String.valueOf(minAgeRequest))
                        .param("ageMax", String.valueOf(maxAgeRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(42L))
                .andExpect(jsonPath("$[0].name").value("student_name_test"))
                .andExpect(jsonPath("$[0].age").value(22));
    }

    @Test
    void getFacultyByStudentId() throws Exception {
        long studentId = 1L;

        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("faculty_name_test");
        faculty1.setColor("red");

        when(studentService.getFacultyByStudentId(studentId)).thenReturn(faculty1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/get_faculty/{id}", studentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("faculty_name_test"))
                .andExpect(jsonPath("$.color").value("red"));
    }
}