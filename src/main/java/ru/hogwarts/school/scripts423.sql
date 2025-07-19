SELECT student.name, student.age, faculty.name
FROM student INNER JOIN faculty ON faculty.id = student.faculty_id;

SELECT * FROM student INNER JOIN avatar ON avatar.student_id = student.id;