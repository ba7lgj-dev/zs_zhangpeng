package com.addressbook.storage;

import com.addressbook.entity.Student;
import com.addressbook.service.Classroom;

public interface DataDeserializer {
    Student deserializeStudent(String line);

    Classroom deserializeClass(String line);
}
