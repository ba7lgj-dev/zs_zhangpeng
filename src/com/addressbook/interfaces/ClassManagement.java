package com.addressbook.interfaces;

import com.addressbook.service.Classroom;
import java.util.List;

public interface ClassManagement {
    boolean addClass(Classroom classroom);

    Classroom getClassByName(String className);

    List<Classroom> getAllClasses();
}
