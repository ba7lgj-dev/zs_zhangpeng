package com.addressbook.interfaces;

import com.addressbook.entity.Student;
import java.util.List;

public interface StudentOperations {
    boolean addStudent(Student student);

    boolean deleteStudent(String studentId);

    boolean updateStudent(String studentId, Student updatedStudent);

    List<Student> searchById(String studentId);

    List<Student> searchByName(String nameKeyword);

    void sortById();

    void sortByName();

    List<Student> getAllStudents();
}
