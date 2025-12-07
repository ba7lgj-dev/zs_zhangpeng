package com.addressbook.strategy;

import com.addressbook.entity.Student;
import com.addressbook.interfaces.SortStrategy;
import java.util.Comparator;
import java.util.List;

public class StudentNameSortStrategy implements SortStrategy {
    @Override
    public void sort(List<Student> students) {
        students.sort(Comparator.comparing(Student::getName));
    }
}
