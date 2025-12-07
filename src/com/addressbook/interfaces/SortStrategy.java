package com.addressbook.interfaces;

import com.addressbook.entity.Student;
import java.util.List;

public interface SortStrategy {
    void sort(List<Student> students);
}
