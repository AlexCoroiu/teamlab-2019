/*
 *     TeamLab
 *     Copyright (C) 2019  Alexandra Coroiu, Ciprian Lăzăroaia
 *     a.coroiu@student.utwente.nl, c.lazaroaia@student.utwente.nl
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You have received a copy of the GNU Affero General Public License
 *     along with this program.  Alternatively, see <http://www.gnu.org/licenses/>.
 */

package nl.utwente.teamlab.logic;

import nl.utwente.teamlab.DAO.DB;
import nl.utwente.teamlab.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//CRUD
public class Service {
    private DB db;
    private QuestionMasterMind questionMaster;
    private FieldChecker checker;
    private Converter converter;


    public Service (){
        db = DB.getInstance();
        questionMaster = new QuestionMasterMind();
        checker = new FieldChecker();
        converter = new Converter();
    }



//----------------------------------------------------------------------------
    //CREATE
    //------------------------------------------------------------------------


    //teacher

    //course
    public Course createCourse(Course course) throws SQLException {
        if (checker.checkString(course.getName())){
            course.studiesToString();
            return db.getCourse(db.createCourse(course),true);
        }
        return new Course();
    }

    //test
    public Test createTest(Test test) throws SQLException {
        if(checker.checkString(test.getName())) {
            test.createQuestionsList();
            //ArrayList<Question> questions = test.getQuestions();
            int id = db.createTest(test);
           /* for (Question q : questions) {
                q.setScore(questionMaster.changeScore(q));
                q.setTags(questionMaster.generateTags(q));
                db.addQuestionToTest(id,q);
            }*/
            return db.getTest(id,true);
        }
        return new Test();
    }

    //student
    public void addResult(int id, Student student) throws SQLException {
        Course course = db.getCourse(id,true);
        if(checker.checkId(student.getId()) &&
                checker.checkString(student.getEmail()) && checker.checkString(student.getName())) {
            if (course.getStatus()) {
                db.addProgress(student, id);
            }
            if (checker.checkGender(student.getGender()) && checker.checkNationality(student.getNationality()) &&
                    checker.checkBelbin(student.getBelbin()) && checker.checkStudy(student.getStudy(), course.getStudies()) &&
                    checker.checkMotivation(student.getMotivation()) && checker.checkKnowledge(student.getKnowledge())) {
                Test test = db.getTest(student.getResults().getId(), true);
                int result = KnowledgeMasterMind.scoreStudent(student, test);
                student.setKnowledge(result);
                db.addStudentToCourse(student, id);
            }
        }
    }

    //groups
    //TODO maybe delete old groups
    public void createGroups(Course course, Teacher user) throws IOException, InterruptedException, SQLException {
        //db.deleteGroups(course);
        if(user.getMail().equals(db.getCourse(course.getId(),true).getOwner().getMail())) {
            GroupMasterMind master = new GroupMasterMind();
            course.setStudents(db.getStudents(course.getId()));
            ArrayList<Group> groups = master.createGroups(course);
            db.createGroups(groups, course.getId());
        }
    }


    //question
    public int addQuestionToTest(int testId, Question q, Teacher user) throws SQLException { //check type
        if(user.getMail().equals(db.getTest(testId,true).getTeacher().getMail())){
            if(checker.checkString(q.getText()) && checker.checkString(q.getAnswer())) {
                q.setScore(questionMaster.changeScore(q));
                q.setTags(questionMaster.generateTags(q));
                return db.addQuestionToTest(testId, q);
            }

        }
        return 0;
    }


//--------------------------------------------------------------------------------
    //READ
//--------------------------------------------------------------------------------

    //courses
    public ArrayList<Course> getCourses(Teacher teacher)  throws SQLException{
        return db.getCourses(teacher);
    }

    //tests
    public List<Test> getTests(Teacher teacher) throws SQLException {
        return db.getTests(teacher);
    }

    //course

    public Course getCourseForTeacher(int id, Teacher teacher) throws SQLException {
        Course course = db.getCourse(id,true);
        if (course.getOwner().getMail().equals(teacher.getMail())){
            course.getTest().createQuestionsList();
            return  course;
        }
        return new Course();

    }

    public Course getCourseForStudent(int id) throws SQLException {
        Course course = db.getCourse(id,false);
        course.getTest().createQuestionsList();
        return  course;
    }

    //test
    public Test getTest(int id, Teacher user)  throws SQLException{
        Test test = db.getTest(id, true);
        if(test.getTeacher().getMail().equals(user.getMail())){
            test.createQuestionsList();
            return test;
        }
        return new Test();
    }

    //questions
    public List<Question> getQuestionByTags(String tag) throws SQLException {
        return db.getQuestionsByTags(tag);
    }

    //students
    public List<Student> getStudents(int id, Teacher user) throws SQLException {
        if(user.getMail().equals(db.getCourse(id,true).getOwner().getMail())) {
            return db.getStudents(id);
        }
        return new ArrayList<>();
    }

    //groups
    public List<Group> getGroups(int id, Teacher user) throws SQLException {
        if(user.getMail().equals(db.getCourse(id,true).getOwner().getMail())) {
            return db.getGroups(id);
        }
        return new ArrayList<>();
    }

    public Course getGroupsCSV(int id, Teacher user) throws SQLException {
        Course course = new Course();
        if(user.getMail().equals(db.getCourse(id,true).getOwner().getMail())) {
            course.setGroupsCSV(converter.groupsToCSV(db.getGroups(id)));
            System.out.println("created");
        }
        return course;
    }

    //charts
    public List<Chart> getCharts(int id, Teacher user) throws SQLException {
        if(user.getMail().equals(db.getCourse(id,true).getOwner().getMail())) {
            return db.getCharts(id);
        }
        return new ArrayList<>();
    }
//----------------------------------------------------------------------------------------------
    //UPDATE
//------------------------------------------------------------------------------------------------
    //course-test
    public void addTestToCourse(int id,Test test, Teacher user) throws SQLException {
        if(user.getMail().equals(db.getCourse(id,true).getOwner().getMail())) {
            db.addTestToCourse(id, test.getId());
        }
    }

    //course-name
    public void addNameToCourse(int id, String name, Teacher user) throws SQLException {
        if(user.getMail().equals(db.getCourse(id,true).getOwner().getMail())) {
            db.addNameToCourse(id, name);
        }
    }

    //course-studies
    public void addStudiesToCourse(int id, String studies, Teacher user) throws SQLException {
        if(user.getMail().equals(db.getCourse(id,true).getOwner().getMail())) {
            db.addStudiesToCourse(id, studies);
        }
    }

    //course-rules
    public void addRulesToCourse(int id, String rules,Teacher teacher) throws SQLException {
        if(teacher.getMail().equals(db.getCourse(id,true).getOwner().getMail())) {
            db.addRulesToCourse(id, rules);
        }
    }

    //course-status
    public void addStatus(int id, boolean status, Teacher teacher) throws SQLException {
        if(teacher.getMail().equals(db.getCourse(id,true).getOwner().getMail())) {
            db.addStatus(id, status);
        }
    }

    //test-name
    public void addNameToTest(int id, String name, Teacher user) throws SQLException {
        if(user.getMail().equals(db.getTest(id,true).getTeacher().getMail())) {
            System.out.println("add name");
            db.addNameToTest(id, name);
        }
    }

    //test-question
    public void deleteQuestionFromTest(int testId, int q, Teacher user) throws SQLException {
        if(user.getMail().equals(db.getTest(testId,true).getTeacher().getMail())) {
            db.deleteQuestion(q, testId);
        }
    }

    //group- student
    public void deleteStudentFromGroup(int course, int group, int student, Teacher user) throws SQLException {
        if(user.getMail().equals(db.getCourse(course,true).getOwner().getMail())) {
            db.deleteStudentFromGroup(course, group, student);
        }
    }

    public void addStudentToGroup(int course, int group, int student, Teacher user) throws SQLException {
        if(user.getMail().equals(db.getCourse(course,true).getOwner().getMail())) {
            db.addStudentToGroup(course, group, student);
        }
    }

    //group-performance
    public void addPerformanceToGroup(int value, int id, int cid, Teacher user) throws SQLException {
        if(user.getMail().equals(db.getCourse(cid,true).getOwner().getMail())) {
            db.addPerformanceToGroup(value, id);
        }
    }

    //group-grade
    public void addGradeToGroup(int grade, int id, int cid, Teacher user) throws SQLException {
        if(user.getMail().equals(db.getCourse(cid,true).getOwner().getMail())){
            db.addGradeToGroup(grade, id);
        }
    }
}
