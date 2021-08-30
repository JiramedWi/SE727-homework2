package camt.se494.course.service;

import camt.se494.course.dao.CourseDao;
import camt.se494.course.dao.CourseEnrolmentDao;
import camt.se494.course.entity.CourseEnrolment;
import camt.se494.course.entity.CourseReport;
import camt.se494.course.entity.Student;
import camt.se494.course.service.util.GradeMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.IsCloseTo.*;

class CourseServiceImplTestHomeWork {
    Student student1 = null;
    Student student2 = null;
    Student student3 = null;
    CourseEnrolmentDao courseEnrolmentDao = null;
    List<CourseEnrolment> courseEnrolmentList = new ArrayList<>();
    CourseReport courseReport = null;
    GradeMatcher gradeMatcher = null;
    @BeforeEach
    void init(){
        //Set gradematcher
        gradeMatcher = mock(GradeMatcher.class);
        when(gradeMatcher.getGradeScore("A")).thenReturn(4.00);
        when(gradeMatcher.getGradeScore("B+")).thenReturn(3.50);
        when(gradeMatcher.getGradeScore("B")).thenReturn(3.00);
        when(gradeMatcher.getGradeScore("C+")).thenReturn(2.50);
        when(gradeMatcher.getGradeScore("C")).thenReturn(2.00);
        when(gradeMatcher.getGradeScore("D+")).thenReturn(1.50);
        when(gradeMatcher.getGradeScore("D")).thenReturn(1.00);
        when(gradeMatcher.getGradeScore("F")).thenReturn(0.00);

        CourseEnrolment courseEnrolment0 = mock(CourseEnrolment.class);
        when(courseEnrolment0.getGrade()).thenReturn("A");
        courseEnrolmentList.add(courseEnrolment0);
        CourseEnrolment courseEnrolment1 = mock(CourseEnrolment.class);
        when(courseEnrolment1.getGrade()).thenReturn("B+");
        courseEnrolmentList.add(courseEnrolment1);
        CourseEnrolment courseEnrolment2 = mock(CourseEnrolment.class);
        when(courseEnrolment2.getGrade()).thenReturn("B");
        courseEnrolmentList.add(courseEnrolment2);
//        when(courseEnrolmentDao.getCourseEnrolments()).thenReturn(courseEnrolmentList);
    }

    @Test
    //GIVE UP
    void GetCourseReport() {
        CourseServiceImpl courseService = new CourseServiceImpl();
        List<Student> students1 = new ArrayList<>();
        students1.add(student1);
        students1.add(student2);
        students1.add(student3);
        when(courseReport.getStudents()).thenReturn(students1);


        List<Student> students2 = new ArrayList<>();
        students2.add(student1);
        students2.add(student3);
        when(courseReport.getStudents()).thenReturn(students2);

    }

    @Test
    void GetCourseGpa() {
        CourseServiceImpl courseService = new CourseServiceImpl();
        assertThat(courseService.getCourseGpa(courseEnrolmentList),is(closeTo(3.50,0.001)));
    }
}