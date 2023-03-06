package ifsp.edu.br.springCRUD.controllers;

import ifsp.edu.br.springCRUD.dtos.StudentDto;
import ifsp.edu.br.springCRUD.models.Student;
import ifsp.edu.br.springCRUD.services.StudentService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/students")
public class StudentController {

    @Autowired
    final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> students(){
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getStudentById(@RequestBody @PathVariable UUID id){
        Optional<Student> studentOptional = studentService.getStudentById(id);
        if (studentOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        return ResponseEntity.status(HttpStatus.OK).body(studentOptional.get());
    }

    @PostMapping
    public ResponseEntity<Object> saveClient(@RequestBody @Valid StudentDto studentDto){
        var student = new Student(
                studentDto.getName(),
                studentDto.getEmail(),
                studentDto.getBirthDate(),
                Period.between(studentDto.getBirthDate(), LocalDate.now()).getYears()
        );
        if (studentService.findStudentByEmail(student.getEmail()).isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.saveStudent(student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStudent(@RequestBody @PathVariable UUID id){
        Optional<Student> studentOptional = studentService.getStudentById(id);
        if (studentOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        studentService.deleteStudent(id);
        return ResponseEntity.status(HttpStatus.OK).body("Student deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateStudent(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid StudentDto studentDto){
        Optional<Student> studentOptional = studentService.getStudentById(id);
        if (studentOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        var student = new Student(
                id,
                studentDto.getName(),
                studentDto.getEmail(),
                studentDto.getBirthDate(),
                Period.between(studentDto.getBirthDate(), LocalDate.now()).getYears()
        );
        return ResponseEntity.status(HttpStatus.OK).body(studentService.saveStudent(student));
    }

}
