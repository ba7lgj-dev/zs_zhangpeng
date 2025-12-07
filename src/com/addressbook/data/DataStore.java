package com.addressbook.data;

import com.addressbook.service.ClassManager;
import com.addressbook.service.Classroom;
import java.util.List;

public final class DataStore {
    private static final ClassManager CLASS_MANAGER = new ClassManager();

    static {
        List<Classroom> classrooms = DataInitializer.initializeClasses();
        CLASS_MANAGER.addAll(classrooms);
    }

    private DataStore() {
    }

    public static ClassManager getClassManager() {
        return CLASS_MANAGER;
    }
}
