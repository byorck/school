package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        logger.debug("Creating student with data: {}", student);
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            Faculty faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found with id " + student.getFaculty().getId()));
            student.setFaculty(faculty);
        } else {
            student.setFaculty(null);
        }
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Was invoked method for find student");
        logger.debug("Looking for student with id: {}", id);
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        logger.debug("Editing student with data: {}", student);
        return createStudent(student);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student");
        logger.debug("Deleting student with id: {}", id);
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method getAllStudents");
        Collection<Student> students = studentRepository.findAll();
        logger.debug("Retrieved {} students", students.size());
        return students;
    }

    public Collection<Student> findByAge(int age) {
        logger.info("Was invoked method for find students by age");
        logger.debug("Finding students by age = {}", age);
        Collection<Student> students = studentRepository.findByAge(age).stream()
                .filter(s -> s.getAge() == age)
                .collect(Collectors.toList());
        logger.debug("Found {} students with age {}", students.size(), age);
        return students;
    }

    public Collection<Student> findByAgeBetween(int ageMax, int ageMin) {
        logger.info("Was invoked method findByAgeBetween");
        logger.debug("Finding students with age between {} and {}", ageMin, ageMax);
        Collection<Student> students = studentRepository.findByAgeBetween(ageMin, ageMax);
        logger.debug("Found {} students in age range", students.size());
        return students;
    }

    public Faculty getFacultyByStudentId(Long studentId) {
        logger.info("Was invoked method for get faculty by student id");
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    public Integer countOfAllStudents() {
        logger.info("Was invoked method countOfAllStudents");
        Integer count = studentRepository.countOfAllStudents();
        logger.debug("Total number of students: {}", count);
        return count;
    }

    public Double averageAgeOfAllStudents() {
        logger.info("Was invoked method averageAgeOfAllStudents");
        Double average = studentRepository.averageAgeOfAllStudents();
        logger.debug("Average age of all students: {}", average);
        return average;
    }

    public Collection<Student> getLastFiveStudents() {
        logger.info("Was invoked method getLastFiveStudents");
        Collection<Student> students = studentRepository.getLastFiveStudents();
        logger.debug("Retrieved last five students, count: {}", students.size());
        return students;
    }
  
    public void getAllStudentsWithParallelThreads() {
        logger.info("Was invoked method getAllStudentsWithParallelThreads");
        for (int i = 1; i <= 6; i = i + 2) {
            final int index = i;
            new Thread(() -> {
                System.out.println((studentRepository.findAll().get(index)));
                System.out.println((studentRepository.findAll().get(index + 1)));
            }).start();
        }
    }
  
    public synchronized void getAllStudentsWithParallelThreadsSynchronized() {
        logger.info("Was invoked method getAllStudentsWithParallelThreadsSynchronized");
        Object printLock = new Object();
        for (int i = 1; i <= 6; i = i + 2) {
            final int index = i;
            new Thread(() -> {
                synchronized (printLock) {
                    System.out.println((studentRepository.findAll().get(index)));
                    System.out.println((studentRepository.findAll().get(index + 1)));
                }
            }).start();
        }
    }

    public Collection<Student> getStudentsWhoseNameStartsWithA() {
        logger.info("Was invoked method getStudentsWhoseNameStartsWithA");
        Collection<Student> students = studentRepository.findAll().stream().filter(s -> s.getName().startsWith("А")).toList();
        logger.debug("Students, whose name starts with the letter A count: {}", students.size());
        return students;
    }

    public Double getStudentsAverageAge() {
        logger.info("Was invoked method getStudentsAverageAge");
        Double average = studentRepository.findAll().stream().mapToDouble(Student::getAge).average().orElse(0.0);
        logger.debug("All students average age: {}", average);
        return average;
    }
}

