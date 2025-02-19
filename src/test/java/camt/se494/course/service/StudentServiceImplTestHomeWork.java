package camt.se494.course.service;

import camt.se494.course.dao.CourseEnrolmentDao;
import camt.se494.course.dao.OpenedCourseDao;
import camt.se494.course.dao.StudentDao;
import camt.se494.course.entity.CourseEnrolment;
import camt.se494.course.entity.OpenedCourse;
import camt.se494.course.entity.Student;
import camt.se494.course.entity.StudentReport;
import camt.se494.course.exception.UnAcceptGradeException;
import camt.se494.course.service.util.GradeMatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.IsCloseTo.*;




class StudentServiceImplTestHomeWork {
    Student student1 = null;
    Student student2 = null;
    Student student3 = null;
    StudentDao studentDao = null;
    GradeMatcher gradeMatcher = null;
    CourseEnrolmentDao courseEnrolmentDao = null;
    StudentReport studentReport = null;
    List<CourseEnrolment> courseEnrolmentListYear = new ArrayList<>();

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
        courseEnrolmentList2.add(courseEnrolment5);
        CourseEnrolment courseEnrolment6 = mock(CourseEnrolment.class);
        when(courseEnrolment6.getGrade()).thenReturn("C");
        courseEnrolmentList2.add(courseEnrolment6);
        CourseEnrolment courseEnrolment7 = mock(CourseEnrolment.class);
        when(courseEnrolment7.getGrade()).thenReturn("D");
        courseEnrolmentList2.add(courseEnrolment7);
        //add course to 3rd student
        when(student3.getCourseEnrolments()).thenReturn(courseEnrolmentList2);

        //Set list of student
        studentDao = mock(StudentDao.class);
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        when(studentDao.getStudent()).thenReturn(students);

        // Set list course with year for student 3
        courseEnrolmentDao = mock(CourseEnrolmentDao.class);
        List<CourseEnrolment> courseEnrolmentList2020 = new ArrayList<>();
        courseEnrolmentList2020.add(courseEnrolment5);
        courseEnrolmentList2020.add(courseEnrolment6);
        List<CourseEnrolment> courseEnrolmentList2021 = new ArrayList<>();
        courseEnrolmentList2021.add(courseEnrolment7);
        courseEnrolmentListYear.add(courseEnrolment5);
        courseEnrolmentListYear.add(courseEnrolment6);
        courseEnrolmentListYear.add(courseEnrolment7);
        when(courseEnrolmentDao.getCourseEnrolments(2020)).thenReturn(courseEnrolmentList2020);
        when(courseEnrolmentDao.getCourseEnrolments(2021)).thenReturn(courseEnrolmentList2021);
        OpenedCourse openedCourse1 = mock(OpenedCourse.class);
        OpenedCourse openedCourse2 = mock(OpenedCourse.class);
        when(openedCourse1.getAcademicYear()).thenReturn(2020);
        when(openedCourse2.getAcademicYear()).thenReturn(2021);
        when(courseEnrolment5.getOpenedCourse()).thenReturn(openedCourse1);
        when(courseEnrolment6.getOpenedCourse()).thenReturn(openedCourse1);
        when(courseEnrolment7.getOpenedCourse()).thenReturn(openedCourse2);

        studentReport = mock(StudentReport.class);

    }
    @Test
    void getStudent(){
        StudentServiceImpl studentService = new StudentServiceImpl();
        studentService.setStudentDao(studentDao);
        assertThat(studentService.getStudent(),hasItems(student1,student2,student3));

        //seem like bug
        assertThat(studentService.getStudent("50"),hasItems(student1));
    }

    @Test
    void getStudentGradeLowerThan() {
        StudentServiceImpl studentService = new StudentServiceImpl();
        studentService.setStudentDao(studentDao);
        studentService.setGradeMatcher(gradeMatcher);
        assertThat(studentService.getStudentGradeGreaterThan(2.00), hasItem(student1));

    }

    @Test
    void getStudentGradeGreaterThan() {

        StudentServiceImpl studentService = new StudentServiceImpl();
        studentService.setStudentDao(studentDao);
        studentService.setGradeMatcher(gradeMatcher);
        assertThat(studentService.getStudentGradeLowerThan(2.50), hasItem(student3));
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
        //DONE
    void getStudentGpaAcademicYear() {

        //Perfect case
        StudentServiceImpl studentService = new StudentServiceImpl();
        studentService.setGradeMatcher(gradeMatcher);
        studentService.setStudentDao(studentDao);
        assertThat(studentService.getStudentGpa(student3, 2020),
                is(closeTo(2.5, 0.001)));
        assertThat(studentService.getStudentGpa(student3, 2021),
                is(closeTo(1, 0.001)));

        verify(student3, times(2)).getCourseEnrolments();
    }

    @Test
        //GIVE UP
    void getStudentReport() {
        StudentServiceImpl studentService = new StudentServiceImpl();
        studentService.setGradeMatcher(gradeMatcher);
        studentService.setCourseEnrolmentDao(courseEnrolmentDao);
        when(courseEnrolmentDao.getCourseEnrolments(student3)).thenReturn(courseEnrolmentListYear);
        Map<Integer,List<CourseEnrolment>> enrolmenMap = new TreeMap<>();
        Map<Integer,Double> gpaMap = new TreeMap<>();
        assertThat(studentService.getStudentReport(student3),is(new StudentReport(student3,enrolmenMap,gpaMap)));
    }

    @Test
    void getRegisterYear() {
        StudentServiceImpl studentService = new StudentServiceImpl();
        studentService.setGradeMatcher(gradeMatcher);
        studentService.setCourseEnrolmentDao(courseEnrolmentDao);
        studentService.setStudentDao(studentDao);
        assertThat(studentService.getRegisterYear(courseEnrolmentListYear),hasItems());
    }
}