package com.addressbook.storage;

import com.addressbook.entity.Student;
import com.addressbook.service.Classroom;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TxtFileStorage implements DataStorage, DataSerializer, DataDeserializer {
    private static final String CLASS_FILE_NAME = "class_list.txt";
    private static final String STUDENT_FILE_PREFIX = "student_data_";
    private static final String STUDENT_FILE_SUFFIX = ".txt";

    @Override
    public void saveAllClasses(List<Classroom> classrooms) {
        Path classPath = Paths.get(CLASS_FILE_NAME);
        List<String> lines = classrooms.stream()
                .map(this::serializeClass)
                .collect(Collectors.toList());
        writeWithBackup(classPath, lines);
    }

    @Override
    public void saveStudents(Classroom classroom) {
        Path studentPath = getStudentFilePath(classroom.getClassId());
        List<String> lines = classroom.getAllStudents().stream()
                .map(this::serializeStudent)
                .collect(Collectors.toList());
        writeWithBackup(studentPath, lines);
    }

    @Override
    public List<Classroom> loadAllClasses() {
        Path classPath = Paths.get(CLASS_FILE_NAME);
        if (!Files.exists(classPath)) {
            return new ArrayList<>();
        }
        List<Classroom> classrooms = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(classPath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Classroom classroom = deserializeClass(line);
                if (classroom != null) {
                    classrooms.add(classroom);
                }
            }
        } catch (IOException e) {
            System.err.println("读取班级文件失败：" + e.getMessage());
        }
        return classrooms;
    }

    @Override
    public void loadStudents(Classroom classroom) {
        Path studentPath = getStudentFilePath(classroom.getClassId());
        if (!Files.exists(studentPath)) {
            return;
        }
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(studentPath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Student student = deserializeStudent(line);
                if (student != null) {
                    students.add(student);
                }
            }
            classroom.replaceStudents(students);
        } catch (IOException e) {
            System.err.println("读取学生文件失败：" + e.getMessage());
        }
    }

    @Override
    public String serializeStudent(Student student) {
        return String.join("|",
                student.getId(),
                student.getName(),
                student.getPhone(),
                student.getQq(),
                student.getEmail(),
                student.getDormAddress(),
                student.getGender(),
                String.valueOf(student.getAge())
        );
    }

    @Override
    public String serializeClass(Classroom classroom) {
        return String.join("|",
                classroom.getClassId(),
                classroom.getClassName(),
                classroom.getMajor(),
                classroom.getHeadTeacher()
        );
    }

    @Override
    public Student deserializeStudent(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = line.split("\\|");
        if (parts.length < 6) {
            System.err.println("学生数据格式错误，已跳过：" + line);
            return null;
        }
        try {
            String id = parts[0].trim();
            String name = parts[1].trim();
            String phone = parts[2].trim();
            String qq = parts[3].trim();
            String email = parts[4].trim();
            String dorm = parts[5].trim();
            String gender = parts.length > 6 ? parts[6].trim() : "";
            int age = parts.length > 7 ? Integer.parseInt(parts[7].trim()) : 0;
            if (id.isEmpty() || name.isEmpty()) {
                System.err.println("学生数据缺少必填字段，已跳过：" + line);
                return null;
            }
            return new Student(id, name, phone, email, qq, dorm, gender, age);
        } catch (Exception e) {
            System.err.println("反序列化学生数据失败，已跳过：" + line);
            return null;
        }
    }

    @Override
    public Classroom deserializeClass(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = line.split("\\|");
        if (parts.length < 4) {
            System.err.println("班级数据格式错误，已跳过：" + line);
            return null;
        }
        String classId = parts[0].trim();
        String className = parts[1].trim();
        String major = parts[2].trim();
        String headTeacher = parts[3].trim();
        if (classId.isEmpty() || className.isEmpty()) {
            System.err.println("班级数据缺少必填字段，已跳过：" + line);
            return null;
        }
        return new Classroom(classId, className, headTeacher, major, new ArrayList<>());
    }

    private void writeWithBackup(Path target, List<String> lines) {
        Path backupPath = null;
        try {
            if (Files.exists(target)) {
                backupPath = createBackup(target);
            }
            try (BufferedWriter writer = Files.newBufferedWriter(target, StandardCharsets.UTF_8)) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("写入文件失败，尝试恢复备份：" + target.getFileName());
            if (backupPath != null) {
                try {
                    Files.copy(backupPath, target, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    System.err.println("恢复备份失败：" + ex.getMessage());
                }
            }
        }
    }

    private Path createBackup(Path original) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Path backupPath = Paths.get(original.toString() + ".bak." + timestamp);
        Files.copy(original, backupPath, StandardCopyOption.REPLACE_EXISTING);
        return backupPath;
    }

    private Path getStudentFilePath(String classId) {
        Objects.requireNonNull(classId, "班级编号不能为空");
        return Paths.get(STUDENT_FILE_PREFIX + classId + STUDENT_FILE_SUFFIX);
    }
}
