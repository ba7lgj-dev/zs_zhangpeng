package com.addressbook.service;

import com.addressbook.entity.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class BasicGlobalSearchService implements GlobalSearchService {
    private final ClassManager classManager;

    public BasicGlobalSearchService(ClassManager classManager) {
        this.classManager = classManager;
    }

    @Override
    public GlobalSearchResult search(String keyword) {
        GlobalSearchResult result = new GlobalSearchResult();
        if (keyword == null || keyword.isBlank()) {
            return result;
        }
        String lowerKeyword = keyword.toLowerCase(Locale.ROOT);

        for (Classroom classroom : classManager.getAllClasses()) {
            List<String> classMatches = matchClassroomFields(classroom, lowerKeyword);
            if (!classMatches.isEmpty()) {
                String description = String.format("【班级】%s：命中字段=%s", classroom.getClassName(), String.join("、", classMatches));
                result.getClassMatches().add(new SearchMatch("CLASS", classroom.getClassName(), description));
            }

            for (Student student : classroom.getAllStudents()) {
                List<String> studentMatches = matchStudentFields(student, lowerKeyword);
                if (!studentMatches.isEmpty()) {
                    String description = String.format("【学生】%s（%s）：命中字段=%s",
                            student.getName(), classroom.getClassName(), String.join("、", studentMatches));
                    result.getStudentMatches().add(new SearchMatch("STUDENT", classroom.getClassName(), description));
                }
            }
        }
        return result;
    }

    private List<String> matchClassroomFields(Classroom classroom, String keyword) {
        Map<String, String> fields = Map.of(
                "班级编号", classroom.getClassId(),
                "班级名称", classroom.getClassName(),
                "专业", classroom.getMajor(),
                "班主任", classroom.getHeadTeacher()
        );
        return fuzzyMatch(fields, keyword);
    }

    private List<String> matchStudentFields(Student student, String keyword) {
        Map<String, String> fields = Map.of(
                "学号", student.getId(),
                "姓名", student.getName(),
                "手机号", student.getPhone(),
                "QQ", student.getQq(),
                "邮箱", student.getEmail(),
                "宿舍地址", student.getDormAddress(),
                "性别", student.getGender(),
                "年龄", String.valueOf(student.getAge())
        );
        return fuzzyMatch(fields, keyword);
    }

    private List<String> fuzzyMatch(Map<String, String> fields, String keyword) {
        return fields.entrySet().stream()
                .filter(entry -> containsIgnoreCase(entry.getValue(), keyword))
                .map(entry -> String.format("%s(%s)", entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private boolean containsIgnoreCase(String source, String keyword) {
        return source != null && source.toLowerCase(Locale.ROOT).contains(keyword);
    }
}
