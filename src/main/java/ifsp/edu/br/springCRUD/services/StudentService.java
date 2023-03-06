package ifsp.edu.br.springCRUD.services;

import ifsp.edu.br.springCRUD.models.Student;
import ifsp.edu.br.springCRUD.repositories.StudentReposity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    @Autowired
    private final StudentReposity studentReposity;

    public StudentService(StudentReposity studentReposity) {
        this.studentReposity = studentReposity;
    }

    @GetMapping
    public List getStudents(){
        return studentReposity.findAll();
    }

    @PostMapping
    public Student saveStudent(Student student) {
        return studentReposity.save(student);
    }

    public Optional<Student> findStudentByEmail(String email) {
        return studentReposity.findStudentByEmail(email);
    }

    public Optional<Student> getStudentById(UUID id) {
        return studentReposity.findById(id);
    }

    @Transactional
    public void deleteStudent(UUID id) {
        studentReposity.deleteById(id);
    }
}
