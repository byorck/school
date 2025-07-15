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

@WebMvcTest(FacultyController.class)
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    @MockitoBean
    private StudentService studentService;

    @Test
    void createFaculty() throws Exception {
        final String name = "faculty_name_test";
        final String color = "faculty_test_color";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void getFacultyInfo() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(42L);
        faculty.setName("faculty_name_test");
        faculty.setColor("faculty_test_color");

        when(facultyService.findFaculty(anyLong())).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", faculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    void editFacultyInfo() throws Exception {
        final Long id = 43L;
        final String updatedName = "updated_faculty_name_test";
        final String updatedColor = "updated_faculty_test_color";

        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(id);
        updatedFaculty.setName(updatedName);
        updatedFaculty.setColor(updatedColor);

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", updatedName);
        facultyObject.put("color", updatedColor);

        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/{id}", id)
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(updatedName))
                .andExpect(jsonPath("$.color").value(updatedColor));
    }

    @Test
    void deleteFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(42L);
        faculty.setName("faculty_name_test");
        faculty.setColor("faculty_test_color");

        doNothing().when(facultyService).deleteFaculty(faculty.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/{id}", faculty.getId()))
                .andExpect(status().isOk());

        verify(facultyService, times(1)).deleteFaculty(faculty.getId());
    }

    @Test
    void getAllFaculties() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setId(42L);
        faculty1.setName("faculty_name_test");
        faculty1.setColor("faculty_test_color");

        Faculty faculty2 = new Faculty();
        faculty2.setId(43L);
        faculty2.setName("faculty_name_test2");
        faculty2.setColor("faculty_test_color2");

        Collection<Faculty> faculties = Arrays.asList(faculty1, faculty2);

        when(facultyService.getAllFaculties()).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(faculty1.getId().intValue(), faculty2.getId().intValue())))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(faculty1.getName(), faculty2.getName())))
                .andExpect(jsonPath("$[*].color", containsInAnyOrder(faculty1.getColor(), faculty2.getColor())));
    }

    @Test
    void findByNameOrColor() throws Exception {
        String nameRequest = "name1";
        String colorRequest = "color2";

        Faculty faculty1 = new Faculty();
        faculty1.setId(42L);
        faculty1.setName("name1");
        faculty1.setColor("red");

        Faculty faculty2 = new Faculty();
        faculty2.setId(43L);
        faculty2.setName("name2");
        faculty2.setColor("blue");

        Collection<Faculty> facultiesByName = Collections.singletonList(faculty1);
        Collection<Faculty> facultiesByColor = Collections.singletonList(faculty2);

        when(facultyService.findByNameOrColor(nameRequest)).thenReturn(facultiesByName);
        when(facultyService.findByNameOrColor(colorRequest)).thenReturn(facultiesByColor);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/find")
                        .param("request", nameRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(42L))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[0].color").value("red"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/find")
                        .param("request", colorRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(43L))
                .andExpect(jsonPath("$[0].name").value("name2"))
                .andExpect(jsonPath("$[0].color").value("blue"));
    }

    @Test
    void getStudentsByFacultyId() throws Exception {
        long facultyId = 1L;

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Student1");
        student1.setAge(20);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Student2");
        student2.setAge(21);

        Collection<Student> students = Arrays.asList(student1, student2);

        when(facultyService.getStudentsByFacultyId(facultyId)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/get_student/{id}", facultyId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Student1"))
                .andExpect(jsonPath("$[0].age").value(20))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Student2"))
                .andExpect(jsonPath("$[1].age").value(21));
    }
}