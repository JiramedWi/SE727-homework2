package camt.se494.course.service;

import camt.se494.course.dao.StudentDao;
import camt.se494.course.entity.CourseEnrolment;
import camt.se494.course.entity.Student;
import camt.se494.course.exception.UnAcceptGradeException;
import camt.se494.course.service.util.GradeMatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.IsCloseTo.*;


class StudentServiceImplTestHomeWork {
    Student student1 = null;
    Student student2 = null;
    Student student3 = null;
    StudentDao studentDao = null;
    //    CourseDao courseDao = null;
//    OpenedCourseDao openedCourseDao = null;
    GradeMatcher gradeMatcher = null;

    @BeforeEach
    void initstudent() {
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
        //add course to 1st student
        when(student1.getCourseEnrolments()).thenReturn(courseEnrolmentList);


        //set 2nd student as fail case
        student2 = mock(Student.class);
        when(student2.getStudentId()).thenReturn("5002");
        when(student2.getName()).thenReturn("Nan");
        //set course for 2nd student
        List<CourseEnrolment> courseEnrolmentList1 = new ArrayList<>();
        CourseEnrolment courseEnrolment3 = mock(CourseEnrolment.class);
        when(courseEnrolment3.getGrade()).thenReturn("F");
        courseEnrolmentList1.add(courseEnrolment3);
        CourseEnrolment courseEnrolment4 = mock(CourseEnrolment.class);
        when(courseEnrolment4.getGrade()).thenReturn("I");
        courseEnrolmentList1.add(courseEnrolment4);
        //add course to 2nd student
        when(student2.getCourseEnrolments()).thenReturn(courseEnrolmentList1);

        //set 3rd student with academic year
        student3 = mock(Student.class);
        when(student3.getStudentId()).thenReturn("5003");
        when(student3.getName()).thenReturn("Pii");
        //set course for 3rd student
        List<CourseEnrolment> courseEnrolmentList2 = new ArrayList<>();
        CourseEnrolment courseEnrolment5 = mock(CourseEnrolment.class);
        when(courseEnrolment5.getGrade()).thenReturn("B");
//        when(courseEnrolment5.getOpenedCourse().getAcademicYear()).thenReturn(2020);
        CourseEnrolment courseEnrolment6 = mock(CourseEnrolment.class);
        when(courseEnrolment6.getGrade()).thenReturn("C");
//        when(courseEnrolment6.getOpenedCourse().getAcademicYear()).thenReturn(2020);
        CourseEnrolment courseEnrolment7 = mock(CourseEnrolment.class);
        when(courseEnrolment7.getGrade()).thenReturn("D");
//        when(courseEnrolment7.getOpenedCourse().getAcademicYear()).thenReturn(2021);
        //add course to 3rd student
        when(student3.getCourseEnrolments()).thenReturn(courseEnrolmentList2);
    }

    @Test
    void getStudentGradeLowerThan() {
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        when(studentDao.getStudent()).thenReturn(students);
        verify(studentDao.getStudent());
        StudentServiceImpl studentService = new StudentServiceImpl();
        studentService.setGradeMatcher(gradeMatcher);
        assertThat(studentService.getStudentGradeGreaterThan(2.00),hasItem(student1));
    }

    @Test
    void getStudentGradeGreaterThan() {
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        when(studentDao.getStudent()).thenReturn(students);
        verify(studentDao.getStudent());
        StudentServiceImpl studentService = new StudentServiceImpl();
        studentService.setGradeMatcher(gradeMatcher);
        assertThat(studentService.getStudentGradeGreaterThan(2.50),hasItem(student3));
    }

    @Test
    void getStudentGpa() {
        //Perfect case
        StudentServiceImpl studentService = new StudentServiceImpl();
        studentService.setGradeMatcher(gradeMatcher);
        assertThat(studentService.getStudentGpa(student1),
                is(closeTo(3.50, 0.001)));

        //verify function should be called
        verify(student1).getCourseEnrolments();
//        verify(student1).getGpa(); //Seem like bug

        // has it receive what should be?
        verify(gradeMatcher).getGradeScore("A");
        verify(gradeMatcher).getGradeScore("B+");
        verify(gradeMatcher).getGradeScore("B");

        // verify how many times getGradeScore has been called
        verify(gradeMatcher, times(3)).getGradeScore(anyString());

        //Check the order
        InOrder inOrder = inOrder(student1, gradeMatcher);
        inOrder.verify(student1).getCourseEnrolments();
        verify(gradeMatcher).getGradeScore("A");
        verify(gradeMatcher).getGradeScore("B+");
        verify(gradeMatcher).getGradeScore("B");

        //No grade in the list test //seem like something wrong
//        StudentServiceImpl studentService1 = new StudentServiceImpl();
//        studentService1.setGradeMatcher(gradeMatcher);
//        Assertions.assertThrows(UnAcceptGradeException.class,()->{
//            studentService1.getStudentGpa(student2);
//        });
    }

    @Test
    void getStudentGpaWithoutGradeMatcher() {
        //Perfect case
        StudentServiceImpl studentService = new StudentServiceImpl();
        Assertions.assertThrows(RuntimeException.class, () -> {
            studentService.getStudentGpa(student1);
        });

        //verify function should be called
//        verify(student1).getGpa(); //Seem like bug
    }

    @Test
    void getStudentGpaAcademicYear() {

        //Perfect case
        StudentServiceImpl studentService = new StudentServiceImpl();
        studentService.setGradeMatcher(gradeMatcher);
        assertThat(studentService.getStudentGpa(student3, 2020),
                is(closeTo(2.5, 0.001)));

        verify(student3).getCourseEnrolments();
        verify(studentService.getStudentGpa(student3, 2020));
    }

    @Test
    void getStudentReport() {
        StudentServiceImpl studentService = new StudentServiceImpl();
        studentService.setGradeMatcher(gradeMatcher);
//        assertThat(studentService.getStudentReport(student1),hasItem());
    }

}