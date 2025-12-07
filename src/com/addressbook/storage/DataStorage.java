package com.addressbook.storage;

import com.addressbook.service.Classroom;
import java.util.List;

public interface DataStorage {
    void saveAllClasses(List<Classroom> classrooms);

    void saveStudents(Classroom classroom);

    List<Classroom> loadAllClasses();

    void loadStudents(Classroom classroom);
}
