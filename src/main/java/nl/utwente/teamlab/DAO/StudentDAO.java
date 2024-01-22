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

import nl.utwente.teamlab.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentDAO {
    Connection conn;
    DB db;

    public StudentDAO(Connection conn, DB db) {
        this.conn = conn;
        this.db = db;
    }

    private void createStudent(Student student, int course) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_STUDENT.toString());
        ps.setInt(1,student.getId());
        ps.setString(2,student.getEmail());
        ps.setString(3,student.getName());
        ps.setString(4,student.getGender());
        ps.setString(5,student.getNationality());
        ps.setString(6,student.getBelbin());
        ps.executeUpdate();
        ps.close();
    }

    public void addStudentToCourse(Student student,int course) throws SQLException {
        createStudent(student,course);
        PreparedStatement ps = conn.prepareStatement(Queries.ADD_STUDENT_TO_COURSE.toString());
        ps.setInt(1,student.getId());
        ps.setInt(2,course);
        ps.setInt(3,student.getMotivation());
        ps.setInt(4,student.getKnowledge());
        ps.setString(5,student.getStudy());
        ps.setInt(6,0);
        ps.executeUpdate();
        ps.close();
    }

    public void addProgress(Student student, int course) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.ADD_PROGRESS.toString());
        ps.setInt(1,student.getId());
        ps.setInt(2,course);
        ps.setInt(3,student.getMotivation());
        ps.setInt(4,student.getKnowledge());
        ps.executeUpdate();
        ps.close();
    }

    public ArrayList<Student> getStudents (int course) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.GET_STUDENTS_BY_COURSE.toString());
        ps.setInt(1,course);
        ResultSet rs = ps.executeQuery();
        ArrayList<Student> students = new ArrayList<Student>();
        while(rs.next()){
            int id = rs.getInt("id");
            String email = rs.getString("email");
            String name = rs.getString("name");
            String gender = rs.getString("gender");
            String nationality = rs.getString("nationality");
            String belbin = rs.getString("belbin");
            int motivation = rs.getInt("motivation");
            int knowledge = rs.getInt("knowledge");
            String study = rs.getString("study");
            Student s = new Student (id,email,name,gender,nationality, belbin, motivation,knowledge, study);
            students.add(s);
        }
        ps.close();
        return students;
    }

    public void deleteStudentFromGroup(int course, int group, int student) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.DELETE_STUDENT_FROM_GROUP.toString());
        ps.setInt(1,student);
        ps.setInt(2,course);
        ps.executeUpdate();
        ps.close();
    }

    public void addStudentToGroup(int course, int group, int student) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.ADD_GROUP_TO_STUDENT.toString());
        ps.setInt(1,group);
        ps.setInt(2,student);
        ps.setInt(3,course);
        ps.executeUpdate();
        ps.close();
    }
}
