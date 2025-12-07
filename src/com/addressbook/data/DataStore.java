package com.addressbook.data;

import com.addressbook.service.ClassManager;
import com.addressbook.service.Classroom;
import com.addressbook.storage.DataStorage;
import com.addressbook.storage.TxtFileStorage;
import java.util.List;

public final class DataStore {
    private static final DataStorage DATA_STORAGE = new TxtFileStorage();
    private static final ClassManager CLASS_MANAGER = new ClassManager(DATA_STORAGE);

    static {
        List<Classroom> classrooms = DATA_STORAGE.loadAllClasses();
        if (classrooms.isEmpty()) {
            classrooms = DataInitializer.initializeClasses();
            CLASS_MANAGER.addAll(classrooms);
            DATA_STORAGE.saveAllClasses(classrooms);
            classrooms.forEach(DATA_STORAGE::saveStudents);
        } else {
            CLASS_MANAGER.addAll(classrooms);
            classrooms.forEach(DATA_STORAGE::loadStudents);
        }
    }

    private DataStore() {
    }

    public static ClassManager getClassManager() {
        return CLASS_MANAGER;
    }

    public static DataStorage getDataStorage() {
        return DATA_STORAGE;
    }
}
