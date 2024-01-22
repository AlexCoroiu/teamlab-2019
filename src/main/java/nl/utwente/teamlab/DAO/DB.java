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

package nl.utwente.teamlab.DAO;

import nl.utwente.teamlab.model.*;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

//TODO split into more classes
//TODO make transactions
//TODO execute query/update & multiqueries
//TODO checkString resultSet is nto null
@Singleton
public class DB {
    private Connection conn;
    private static final DB INSTANCE = new DB();

    TeacherDAO teacherDAO;
    TestDAO testDAO;
    CourseDAO courseDAO;
    QuestionDAO questionDAO;
    StudentDAO studentDAO;
    GroupDAO groupDAO;
    ChartDAO chartDAO;

    private DB(){
        connect();
        teacherDAO = new TeacherDAO(conn,this);
        testDAO = new TestDAO(conn,this);
        courseDAO = new CourseDAO(conn,this);
        questionDAO = new QuestionDAO(conn,this);
        studentDAO = new StudentDAO(conn,this);
        groupDAO = new GroupDAO(conn, this);
        chartDAO = new ChartDAO(conn,this);
    };
    public static DB getInstance(){
        return INSTANCE;
    }


    private void connect() {
        //TODO FIND PROPER PLACE FOR THIS IN TOMCAT SERVER
        File db = new File("database.db");
        String url = "jdbc:sqlite:database.db";
        // make sure the driver is loaded
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (db.exists()) {
                conn = DriverManager.getConnection(url);
                System.out.println("Connection to SQLite has been established 1.");
            } else {
                System.out.println("creating db");
                db.createNewFile();
                conn = DriverManager.getConnection(url);
                System.out.println("Connection to SQLite has been established 2.");
                createDatabase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //setting
        /*try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        /*finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }*/
    }


    //DATABASE
    private void createDatabase() throws SQLException {
        createQuestionsTable();
        createStudentsTable();
        createTagsTable();
        createTagsQuestionTable();
        createTeachersTable();
        createTestsTable();
        createCoursesTable();
        createGroupsTable();
        createProgressTable();
        createQuestionsTestTable();
        createStudentsCourseTable();
    }

    private void createProgressTable() throws SQLException {
        PreparedStatement ps =conn.prepareStatement(Queries.CREATE_PROGRESS.toString());
        ps.executeUpdate();
        ps.close();
    }

    private void createQuestionsTestTable() throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_QUESTIONS_TEST.toString());
        ps.executeUpdate();
        ps.close();
    }

    private void createGroupsTable() throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_GROUPS.toString());
        ps.executeUpdate();
        ps.close();
    }

    private void createCoursesTable() throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_COURSES.toString());
        ps.executeUpdate();
        ps.close();
    }

    private void createTestsTable() throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_TESTS.toString());
        ps.executeUpdate();
        ps.close();
    }

    private void createTeachersTable() throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_TEACHERS.toString());
        ps.executeUpdate();
        ps.close();
    }

    private void createTagsQuestionTable() throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_TAGS_QUESTION.toString());
        ps.executeUpdate();
        ps.close();
    }

    private void createTagsTable() throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_TAGS.toString());
        ps.executeUpdate();
        ps.close();
    }

    private void createStudentsTable() throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_STUDENTS.toString());
        ps.executeUpdate();
        ps.close();
    }

    private void createStudentsCourseTable() throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_STUDENTS_COURSE.toString());
        ps.executeUpdate();
        ps.close();
    }

    private void createQuestionsTable() throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_QUESTIONS.toString());
        ps.executeUpdate();
        ps.close();
    }

/*    private void populateDatabase() throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.POPULATE_DATABASE.toString());
        ps.execute();
        ps.close();
    }*/

    //TEACHER

    public Teacher getTeacher(String mail, String name) throws SQLException {
        return teacherDAO.getTeacher(mail, name);
    }

    //TEST

    public int createTest(Test test) throws SQLException {
        return testDAO.createTest(test);
    }

    public void addNameToTest(int id, String name) throws SQLException {
        testDAO.addName(id, name);
    }
    public ArrayList<Test> getTests(Teacher teacher) throws SQLException {
        return testDAO.getTests(teacher);
    }

    public Test getTest(int id, boolean answer) throws SQLException {
        return testDAO.getTest(id, answer);
    }

    //QUESTION

    public ArrayList<Question> getQuestionsByTags(String tag) throws SQLException {
        return questionDAO.getQuestionsByTags(tag);
    }

    public void deleteQuestion(int q, int testId) throws SQLException {
        questionDAO.deleteQuestionFromTest(q, testId);
    }

    public int addQuestionToTest(int testID,Question question) throws SQLException {
        return questionDAO.addQuestionToTest(testID, question);
    }

    public HashMap<String,ArrayList<Question>> getQuestions(int id, boolean answer) throws SQLException {
        return questionDAO.getQuestions(id, answer);
    }


    //COURSE
    public void addTestToCourse(int cid, int tid) throws SQLException {
        courseDAO.addTestToCourse(cid, tid);
    }

    public int createCourse(Course course) throws SQLException {
        return courseDAO.createCourse(course);
    }

    public ArrayList<Course> getCourses(Teacher teacher) throws SQLException {
        return courseDAO.getCourses(teacher);
    }

    public Course getCourse(int id, boolean t) throws SQLException {
        return courseDAO.getCourse(id, t);
    }

    public void addNameToCourse(int id, String name) throws SQLException {
        courseDAO.addName(id,name);
    }

    public void addRulesToCourse(int id,String rules) throws SQLException {
        courseDAO.addRulesToCourse(id, rules);
    }

    public void addStudiesToCourse(int id, String studies) throws SQLException {
        courseDAO.addStudiesToCourse(id, studies);
    }

    public void addStatus(int id, boolean status) throws SQLException {
        courseDAO.addStatus(id, status);
    }


    //STUDENT
    public void addStudentToCourse(Student student, int course) throws SQLException {
        studentDAO.addStudentToCourse(student, course);
    }

    public void addProgress(Student student, int course) throws SQLException {
        studentDAO.addProgress(student,course);
    }

    public ArrayList<Student> getStudents (int course) throws SQLException {
        return  studentDAO.getStudents(course);
    }

    //GROUPS
    public void createGroups(ArrayList<Group> groups, int id) throws SQLException {
        groupDAO.createGroups(groups, id);
    }

    public ArrayList<Group> getGroups(int course) throws SQLException {
        return groupDAO.getGroups(course);
    }

    public void addGradeToGroup(int grade, int id) throws SQLException {
        groupDAO.addGradeToGroup(grade, id);
    }

    public void addPerformanceToGroup(int performance, int id) throws SQLException {
        groupDAO.addPerformanceToGroup(performance, id);
    }

    public void deleteStudentFromGroup(int course, int group, int student) throws SQLException {
        studentDAO.deleteStudentFromGroup(course, group, student);
    }

    public void addStudentToGroup(int course, int group, int student) throws SQLException {
        studentDAO.addStudentToGroup(course, group, student);
    }

    //CHART
    public List<Chart> getCharts(int course) throws SQLException {
        return chartDAO.getCharts(course);
    }
}


