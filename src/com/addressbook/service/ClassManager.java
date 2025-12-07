package com.addressbook.service;

import com.addressbook.interfaces.ClassManagement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassManager implements ClassManagement {
    private final List<Classroom> classrooms = new ArrayList<>();

    @Override
    public boolean addClass(Classroom classroom) {
        return classrooms.add(classroom);
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

    public void addAll(List<Classroom> classroomList) {
        classrooms.addAll(classroomList);
    }
}
