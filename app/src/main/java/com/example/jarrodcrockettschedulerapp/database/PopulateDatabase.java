package com.example.jarrodcrockettschedulerapp.database;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jarrodcrockettschedulerapp.entities.Assessment;
import com.example.jarrodcrockettschedulerapp.entities.Course;
import com.example.jarrodcrockettschedulerapp.entities.Term;

import java.util.Calendar;
import java.util.List;

public class PopulateDatabase extends AppCompatActivity {

    Term term1 = new Term();
    Term term2 = new Term();
    Term term3 = new Term();

    Course course1 = new Course();
    Course course2 = new Course();
    Course course3 = new Course();
    Course course4 = new Course();
    Course course5 = new Course();
    Course course6 = new Course();
    Course course7 = new Course();
    Course course8 = new Course();
    Course course9 = new Course();
    Course course10 = new Course();

    Assessment assessment1 = new Assessment();
    Assessment assessment2 = new Assessment();
    Assessment assessment3 = new Assessment();
    Assessment assessment4 = new Assessment();
    Assessment assessment5 = new Assessment();
    Assessment assessment6 = new Assessment();
    Assessment assessment7 = new Assessment();
    Assessment assessment8 = new Assessment();
    Assessment assessment9 = new Assessment();
    Assessment assessment10 = new Assessment();

    FullRoomDatabase db;

    public void populateDatabase(Context context){
        db = FullRoomDatabase.getInstance(context);
        try {
            insertTerms();
            insertCourses();
            insertAssessments();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void insertTerms(){
        Calendar start;
        Calendar end;

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -14);
        end.add(Calendar.MONTH,-8);

        term1.setTitle("Spring 2020");
        term1.setStartDate(start.getTime());
        term1.setEndDate(end.getTime());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -8);
        end.add(Calendar.MONTH,-2);

        term2.setTitle("Fall 2020");
        term2.setStartDate(start.getTime());
        term2.setEndDate(end.getTime());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -2);
        end.add(Calendar.MONTH,4);

        term3.setTitle("Spring 2021");
        term3.setStartDate(start.getTime());
        term3.setEndDate(end.getTime());

        db.termDao().insertAll(term1, term2, term3);
    }

    private void insertCourses(){
        List<Term> termList = db.termDao().getTermList();
        if (termList == null) return;

        Calendar start;
        Calendar end;
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -14);
        end.add(Calendar.MONTH,-13);
        course1.setCourse_title("Introduction to IT");
        course1.setStartDate(start.getTime());
        course1.setEndDate(end.getTime());
        course1.setInstructorName("Course instructor group");
        course1.setInstructorEmail("cmitfund1@wgu.edu");
        course1.setInstructorPhone("Not Available");
        course1.setCourseStatus("Completed");
        course1.setTerm_id_fk(termList.get(0).getTerm_id());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -13);
        end.add(Calendar.MONTH,-12);
        course2.setCourse_title("Scripting and Programming Foundations");
        course2.setStartDate(start.getTime());
        course2.setEndDate(end.getTime());
        course2.setInstructorName("Course instructor group");
        course2.setInstructorEmail("cmcomputerscience@wgu.edu");
        course2.setInstructorPhone("Not Available");
        course2.setCourseStatus("Completed");
        course2.setTerm_id_fk(termList.get(0).getTerm_id());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -12);
        end.add(Calendar.MONTH,-11);
        course3.setCourse_title("Business of IT");
        course3.setStartDate(start.getTime());
        course3.setEndDate(end.getTime());
        course3.setInstructorName("John Sinanovic");
        course3.setInstructorEmail("john.sinanovic@wgu.edu");
        course3.setInstructorPhone("385-428-8009");
        course3.setCourseStatus("Completed");
        course3.setTerm_id_fk(termList.get(0).getTerm_id());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -11);
        end.add(Calendar.MONTH,-10);
        course4.setCourse_title("Data Management - Foundations");
        course4.setStartDate(start.getTime());
        course4.setEndDate(end.getTime());
        course4.setInstructorName("Paul Kercher");
        course4.setInstructorEmail("paul.kercher@wgu.edu");
        course4.setInstructorPhone("385-428-7488");
        course4.setCourseStatus("Completed");
        course4.setTerm_id_fk(termList.get(0).getTerm_id());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -10);
        end.add(Calendar.MONTH,-8);
        course5.setCourse_title("Data Management - Applications");
        course5.setStartDate(start.getTime());
        course5.setEndDate(end.getTime());
        course5.setInstructorName("Paul Kercher");
        course5.setInstructorEmail("paul.kercher@wgu.edu");
        course5.setInstructorPhone("385-428-7488");
        course5.setCourseStatus("Completed");
        course5.setTerm_id_fk(termList.get(0).getTerm_id());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -8);
        end.add(Calendar.MONTH,-6);
        course6.setCourse_title("Software 1");
        course6.setStartDate(start.getTime());
        course6.setEndDate(end.getTime());
        course6.setInstructorName("Alvaro Escobar");
        course6.setInstructorEmail("alvaro.escober@wgu.edu");
        course6.setInstructorPhone("385-428-8835");
        course6.setCourseStatus("Completed");
        course6.setTerm_id_fk(termList.get(1).getTerm_id());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -6);
        end.add(Calendar.MONTH,-4);
        course7.setCourse_title("Software Engineering");
        course7.setStartDate(start.getTime());
        course7.setEndDate(end.getTime());
        course7.setInstructorName("Wanda Burwick");
        course7.setInstructorEmail("wanda.burwick@wgu.edu");
        course7.setInstructorPhone("385-555-7192");
        course7.setCourseStatus("Completed");
        course7.setTerm_id_fk(termList.get(1).getTerm_id());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -4);
        end.add(Calendar.MONTH,-2);
        course8.setCourse_title("Software 2");
        course8.setStartDate(start.getTime());
        course8.setEndDate(end.getTime());
        course8.setInstructorName("Carolyn Sher-DeCusatis");
        course8.setInstructorEmail("carolyn.sher@wgu.edu");
        course8.setInstructorPhone("385-428-7192");
        course8.setCourseStatus("Completed");
        course8.setTerm_id_fk(termList.get(1).getTerm_id());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -2);
        end.add(Calendar.MONTH,1);
        course9.setCourse_title("Mobile App Development");
        course9.setStartDate(start.getTime());
        course9.setEndDate(end.getTime());
        course9.setInstructorName("Carolyn Sher-DeCusatis");
        course9.setInstructorEmail("carolyn.sher@wgu.edu");
        course9.setInstructorPhone("385-428-7192");
        course9.setCourseStatus("In Progress");
        course9.setTerm_id_fk(termList.get(2).getTerm_id());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, 1);
        end.add(Calendar.MONTH,3);
        course10.setCourse_title("Cap Stone Project");
        course10.setStartDate(start.getTime());
        course10.setEndDate(end.getTime());
        course10.setInstructorName("Carolyn Sher-DeCusatis");
        course10.setInstructorEmail("carolyn.sher@wgu.edu");
        course10.setInstructorPhone("385-428-7192");
        course10.setCourseStatus("Plan To Take");
        course10.setTerm_id_fk(termList.get(2).getTerm_id());

        db.courseDao().insertAll(course1, course2, course3, course4, course5, course6, course7, course8, course9, course10);
    }

    private void insertAssessments(){

        List<Course> courseList = db.courseDao().getAllCourses();
        if (courseList == null) return;


        Calendar assessmentDate;

        assessmentDate = Calendar.getInstance() ;
        assessmentDate.add(Calendar.MONTH,-13);
        assessment1.setTitle("Intro To Computer");
        assessment1.setAssessmentDate(assessmentDate.getTime());
        assessment1.setAssessmentType("Objective");
        assessment1.setCourse_id_fk(courseList.get(0).getCourse_id());

        assessmentDate = Calendar.getInstance() ;
        assessmentDate.add(Calendar.MONTH,-12);
        assessment2.setTitle("Scripting and Programming");
        assessment2.setAssessmentDate(assessmentDate.getTime());
        assessment2.setAssessmentType("Objective");
        assessment2.setCourse_id_fk(courseList.get(1).getCourse_id());

        assessmentDate = Calendar.getInstance() ;
        assessmentDate.add(Calendar.MONTH,-11);
        assessment3.setTitle("Project Management");
        assessment3.setAssessmentDate(assessmentDate.getTime());
        assessment3.setAssessmentType("Objective");
        assessment3.setCourse_id_fk(courseList.get(2).getCourse_id());

        assessmentDate = Calendar.getInstance() ;
        assessmentDate.add(Calendar.MONTH,-10);
        assessment4.setTitle("Database foundations");
        assessment4.setAssessmentDate(assessmentDate.getTime());
        assessment4.setAssessmentType("Objective");
        assessment4.setCourse_id_fk(courseList.get(3).getCourse_id());

        assessmentDate = Calendar.getInstance() ;
        assessmentDate.add(Calendar.MONTH,-8);
        assessment5.setTitle("Database applications");
        assessment5.setAssessmentDate(assessmentDate.getTime());
        assessment5.setAssessmentType("Objective");
        assessment5.setCourse_id_fk(courseList.get(4).getCourse_id());

        assessmentDate = Calendar.getInstance() ;
        assessmentDate.add(Calendar.MONTH,-6);
        assessment6.setTitle("Software 1 Java FX application");
        assessment6.setAssessmentDate(assessmentDate.getTime());
        assessment6.setAssessmentType("Performance");
        assessment6.setCourse_id_fk(courseList.get(5).getCourse_id());

        assessmentDate = Calendar.getInstance() ;
        assessmentDate.add(Calendar.MONTH,-4);
        assessment7.setTitle("Software Engineering Written Assignment");
        assessment7.setAssessmentDate(assessmentDate.getTime());
        assessment7.setAssessmentType("Performance");
        assessment7.setCourse_id_fk(courseList.get(6).getCourse_id());

        assessmentDate = Calendar.getInstance() ;
        assessmentDate.add(Calendar.MONTH,-2);
        assessment8.setTitle("Software 2 Java FX and Database Application");
        assessment8.setAssessmentDate(assessmentDate.getTime());
        assessment8.setAssessmentType("Performance");
        assessment8.setCourse_id_fk(courseList.get(7).getCourse_id());

        assessmentDate = Calendar.getInstance() ;
        assessmentDate.add(Calendar.MONTH,1);
        assessment9.setTitle("Mobile App Development Android App");
        assessment9.setAssessmentDate(assessmentDate.getTime());
        assessment9.setAssessmentType("Performance");
        assessment9.setCourse_id_fk(courseList.get(8).getCourse_id());

        assessmentDate = Calendar.getInstance() ;
        assessmentDate.add(Calendar.MONTH,3);
        assessment10.setTitle("Cap Stone Project");
        assessment10.setAssessmentDate(assessmentDate.getTime());
        assessment10.setAssessmentType("Performance");
        assessment10.setCourse_id_fk(courseList.get(9).getCourse_id());

        db.assessmentDao().insertAll(assessment1, assessment2, assessment3, assessment4, assessment5, assessment6, assessment7, assessment8, assessment9, assessment10);
    }
}
