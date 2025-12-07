package com.addressbook.service;

import com.addressbook.entity.Student;
import com.addressbook.interfaces.ClassManagement;
import com.addressbook.storage.DataStorage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassManager implements ClassManagement {
    private final List<Classroom> classrooms = new ArrayList<>();
    private final DataStorage dataStorage;

    public ClassManager(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public boolean addClass(Classroom classroom) {
        boolean added = classrooms.add(classroom);
        if (added) {
            dataStorage.saveAllClasses(classrooms);
            dataStorage.saveStudents(classroom);
        }
        return added;
    }

    public boolean updateClass(String classId, String newName, String newHeadTeacher, String newMajor) {
        Classroom classroom = getClassById(classId);
        if (classroom == null) {
            return false;
        }
        classroom.updateInfo(newName, newHeadTeacher, newMajor);
        dataStorage.saveAllClasses(classrooms);
        return true;
    }

    public Classroom getClassById(String classId) {
        return classrooms.stream()
                .filter(classroom -> classroom.getClassId().equalsIgnoreCase(classId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Classroom getClassByName(String className) {
        return classrooms.stream()
                .filter(classroom -> classroom.getClassName().equalsIgnoreCase(className))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Classroom> getAllClasses() {
        return Collections.unmodifiableList(classrooms);
    }

    public boolean addStudentToClass(String classId, Student student) {
        Classroom classroom = getClassById(classId);
        if (classroom == null) {
            return false;
        }
        boolean added = classroom.addStudent(student);
        if (added) {
            dataStorage.saveStudents(classroom);
        }
        return added;
    }

    public boolean deleteStudentFromClass(String classId, String studentId) {
        Classroom classroom = getClassById(classId);
        if (classroom == null) {
            return false;
        }
        boolean removed = classroom.deleteStudent(studentId);
        if (removed) {
            dataStorage.saveStudents(classroom);
        }
        return removed;
    }

    public boolean updateStudentInClass(String classId, String studentId, Student updatedStudent) {
        Classroom classroom = getClassById(classId);
        if (classroom == null) {
            return false;
        }
        boolean updated = classroom.updateStudent(studentId, updatedStudent);
        if (updated) {
            dataStorage.saveStudents(classroom);
        }
        return updated;
    }

    public void addAll(List<Classroom> classroomList) {
        classrooms.addAll(classroomList);
    }
}
