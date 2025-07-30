package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository1) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository1;
    }

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        logger.debug("Creating faculty with details: {}", faculty);
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        logger.info("Was invoked method for find faculty");
        logger.debug("Trying to find faculty with id = {}", id);
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null) {
            logger.warn("Faculty not found with id = {}", id);
        }
        return faculty;
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty");
        logger.debug("Editing faculty with new data: {}", faculty);
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        logger.info("Was invoked method for delete faculty");
        logger.debug("Deleting faculty with id = {}", id);
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method for getAllFaculties");
        Collection<Faculty> facultyCollection = facultyRepository.findAll();
        logger.debug("Found {} faculties", facultyCollection.size());
        return facultyCollection;
    }

    public Collection<Faculty> findByNameOrColor(String request) {
        logger.info("Was invoked method findByNameOrColor");
        logger.debug("Finding faculties by name or color: {}", request);
        Collection<Faculty> byName = facultyRepository.findByNameIgnoreCase(request);
        if (byName != null && !byName.isEmpty()) {
            logger.debug("Found {} faculties by name matching '{}'", byName.size(), request);
            return byName;
        }
        Collection<Faculty> byColor = facultyRepository.findByColorIgnoreCase(request);
        if (byColor != null && !byColor.isEmpty()) {
            logger.debug("Found {} faculties by color matching '{}'", byColor.size(), request);
            return byColor;
        }
        logger.warn("No faculties found by name or color matching '{}'", request);
        return null;
    }

    public Collection<Student> getStudentsByFacultyId(Long facultyId) {
        logger.info("Was invoked method getStudentsByFacultyId");
        logger.debug("Getting students by faculty id = {}", facultyId);
        Collection<Student> students = studentRepository.findByFacultyId(facultyId);
        logger.debug("Found {} students in faculty id = {}", students.size(), facultyId);
        return students;
    }
}
