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

import nl.utwente.teamlab.model.Course;
import nl.utwente.teamlab.model.Teacher;
import nl.utwente.teamlab.model.Test;

import java.sql.*;
import java.util.ArrayList;

public class CourseDAO {
    Connection conn;
    DB db;

    public CourseDAO(Connection conn, DB db) {
        this.conn = conn;
        this.db = db;
    }

    public void addTestToCourse(int cid, int tid) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.ADD_TEST_TO_COURSE.toString());
        ps.setInt(1,tid);
        ps.setInt(2,cid);
        ps.executeUpdate();
        ps.close();
    }

    public int createCourse(Course course) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_COURSE.toString(), Statement.RETURN_GENERATED_KEYS);
        ps.setString(1,course.getName());
        ps.setString(2,course.getOwner().getMail());
        ps.setString(3,course.getStudiesString());
        ps.setInt(4, course.getTest().getId());
        ps.setString(5,course.getRules());
        ps.executeUpdate();
        int id = ps.getGeneratedKeys().getInt(1);
        ps.close();
        return id;
    }

    public ArrayList<Course> getCourses(Teacher teacher) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.GET_COURSES.toString());
        ps.setString(1,teacher.getMail());
        ResultSet rs = ps.executeQuery();
        ArrayList<Course> courses = new ArrayList<Course>();
        int id;
        String name;

        while(rs.next()){
            id = rs.getInt("id");
            name = rs.getString("name");
            Course course = new Course(id, name);
            courses.add(course);
        }
        ps.close();
        return courses;
    }

    public Course getCourse(int id, boolean t) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.GET_COURSE.toString());
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String name = rs.getString("name");
            String mail = rs.getString("owner");
            int testId = rs.getInt("test");
            String studies = rs.getString("studies");
            String rules = rs.getString("rules");
            boolean status = (rs.getInt("status") == 1) ? true : false;
            ps.close();
            Teacher teacher = new Teacher(mail);
            Test test = new Test(testId);
            if(!t){ //not for teacher
                test = db.getTest(testId, false);
            }
            Course course = new Course (id,name,teacher,test,studies,rules,status);
            course.studiesToArray();
            return course;
        }
        return new Course();
    }

    public void addRulesToCourse(int id,String rules) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.ADD_RULES_TO_COURSE.toString());
        ps.setString(1,rules);
        ps.setInt(2,id);
        ps.executeUpdate();
        ps.close();
    }

    public void addStudiesToCourse(int id, String studies) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.ADD_STUDIES_TO_COURSE.toString());
        ps.setString(1,studies);
        ps.setInt(2,id);
        ps.executeUpdate();
        ps.close();
    }

    public void addStatus(int id, boolean status) throws SQLException {
        int val = (status) ? 1 : 0;
        PreparedStatement ps = conn.prepareStatement(Queries.ADD_STATUS_TO_COURSE.toString());
        ps.setInt(1,val);
        ps.setInt(2,id);
        ps.executeUpdate();
        ps.close();
    }

    public void addName(int id, String name) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.ADD_NAME_TO_COURSE.toString());
        ps.setString(1,name);
        ps.setInt(2,id);
        ps.executeUpdate();
        ps.close();
    }
}
