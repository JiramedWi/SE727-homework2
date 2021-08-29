package camt.se494.course.service;

import camt.se494.course.entity.CourseEnrolment;
import camt.se494.course.entity.Student;
import camt.se494.course.service.util.GradeMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.number.IsCloseTo.*;


class StudentServiceImplTestHomeWork {
    Student student1 = null;
//    CourseDao courseDao = null;
//    OpenedCourseDao openedCourseDao = null;
    GradeMatcher gradeMatcher = null;

    @BeforeEach
    void initstudent() {
        //set 1st student
        student1 = mock(Student.class);
        when(student1.getStudentId()).thenReturn("5001");
        when(student1.getName()).thenReturn("Kong");
        //set course for 1st student
        List<CourseEnrolment> courseEnrolmentList = new ArrayList<>();
        CourseEnrolment courseEnrolment0 = mock(CourseEnrolment.class);
        when(courseEnrolment0.getGrade()).thenReturn("A");
        courseEnrolmentList.add(courseEnrolment0);
        CourseEnrolment courseEnrolment1 = mock(CourseEnrolment.class);
        when(courseEnrolment1.getGrade()).thenReturn("B+");
        courseEnrolmentList.add(courseEnrolment1);
        CourseEnrolment courseEnrolment2 = mock(CourseEnrolment.class);
        when(courseEnrolment2.getGrade()).thenReturn("B");
        courseEnrolmentList.add(courseEnrolment2);
        when(student1.getCourseEnrolments()).thenReturn(courseEnrolmentList);
        //Set gradematcher
        gradeMatcher = mock(GradeMatcher.class);
        when(gradeMatcher.getGradeScore("A")).thenReturn(4.00);
        when(gradeMatcher.getGradeScore("B+")).thenReturn(3.50);
        when(gradeMatcher.getGradeScore("B")).thenReturn(3.00);
    }

    @Test
    void getStudent() {
    }

    @Test
    void getStudentPartial() {
    }

    @Test
    void getStudentGradeLowerThan() {
    }

    @Test
    void getStudentGradeGreaterThan() {
    }

    @Test
    void getStudentGpa() {
        StudentServiceImpl studentService = new StudentServiceImpl();
        studentService.setGradeMatcher(gradeMatcher);
        assertThat(studentService.getStudentGpa(student1),is(closeTo(3.50,0.001)));
    }

    @Test
    void getStudentGpaAcademicYear() {
    }

    @Test
    void getStudentReport() {
    }

    @Test
    void getRegisterYear() {
    }
}