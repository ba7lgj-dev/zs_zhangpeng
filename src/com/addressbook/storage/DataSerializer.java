package com.addressbook.storage;

import com.addressbook.entity.Student;
import com.addressbook.service.Classroom;

public interface DataSerializer {
    String serializeStudent(Student student);

    String serializeClass(Classroom classroom);
}
