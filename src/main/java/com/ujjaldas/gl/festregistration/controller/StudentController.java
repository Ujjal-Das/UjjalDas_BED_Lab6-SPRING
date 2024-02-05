package com.ujjaldas.gl.festregistration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
//import com.nimbusds.jose.util.Resource;
import com.ujjaldas.gl.festregistration.entity.Student;
import com.ujjaldas.gl.festregistration.service.StudentService;

import org.springframework.http.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.http.HttpHeaders;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/list")
    public String showStudentList(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "student-list";
    }

    @GetMapping("/{id}/edit")
    public String showEditStudentForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        return "edit-student";
    }

    @GetMapping("/create")
    public String showCreateStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "create-student";
    }

    @PostMapping("/save")
    public String saveStudent(@ModelAttribute("student") Student student) {
        studentService.saveStudent(student);
        return "redirect:/students/list";
    }

    @GetMapping("/{id}/delete")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students/list";
    }
    
    @GetMapping("/generate-pdf")
    public ResponseEntity<ByteArrayResource> generatePdf() throws DocumentException, IOException {
        List<Student> students = studentService.getAllStudents();

  
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

      
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.addCell("ID");
        table.addCell("First Name");
        table.addCell("Last Name");
        table.addCell("Course");
        table.addCell("Country");


        for (Student student : students) {
            table.addCell(String.valueOf(student.getId()));
            table.addCell(student.getFirstName());
            table.addCell(student.getLastName());
            table.addCell(student.getCourse());
            table.addCell(student.getCountry());
        }

        document.add(table);
        document.close();

        byte[] pdfBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=students.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(pdfBytes));
    }
    
    @Controller
    @RequestMapping("/students")
    public class StudentControllerImpl {

        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping("/admin-only")
        public String adminOnly() {
            // Code accessible only to Admin
            return "admin-page";
        }

        @PreAuthorize("hasRole('USER')")
        @GetMapping("/user-only")
        public String userOnly() {
            // Code accessible only to users
            return "user-page";
        }
    }
    
    
  
    
    
    
}
