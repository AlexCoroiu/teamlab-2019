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
import nl.utwente.teamlab.model.Group;
import nl.utwente.teamlab.model.Student;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class GroupJUnit {
    static DB db = DB.getInstance();
    static int id;
    static ArrayList<Group> groups = new ArrayList<>();
    static Student s1 = new Student();
    static Student s2 = new Student();
    static Student s3 = new Student();
    static Group group1 = new Group();
    static Group group2 = new Group();
    static ArrayList<Group> result;
    static int id1;
    static int id2;

    @BeforeClass
    public static void prepare() throws SQLException {
        ArrayList<Student> sts1 = new ArrayList<>();
        ArrayList<Student> sts2 = new ArrayList<>();
        s1.setId(1); sts1.add(s1);
        s2.setId(2); sts1.add(s2);
        s3.setId(3); sts2.add(s3);
        group1.setStudents(sts1);
        group1.setName(1);
        groups.add(group1);
        group2.setStudents(sts2);
        group2.setName(2);
        groups.add(group2);
        id = db.createCourse(new Course());
        db.addStudentToCourse(s1,id);
        db.addStudentToCourse(s2,id);
        db.addStudentToCourse(s3,id);
        db.createGroups(groups,id);
        result = db.getGroups(id);
        id1 = result.get(0).getId();
        id2 = result.get(1).getId();

    }

    @Test
    public void getGroups() throws SQLException {
        assertEquals(groups,result);

    }

    @Test
    public void deleteStudent() throws SQLException {
        db.deleteStudentFromGroup(id,id1,1);
        result = db.getGroups(id);
        assertNotEquals(1,result.get(0).getStudents().get(0).getId());
    }

    @Test
    public void addStudent() throws SQLException {
        db.addStudentToGroup(id,id1,1);
        result = db.getGroups(id);
        assertEquals(1,result.get(0).getStudents().get(0).getId());
    }

    @Test
    public void addGrade() throws SQLException {
        db.addGradeToGroup(10,id1);
        result = db.getGroups(id);
        assertEquals(10,result.get(0).getGrade());
        db.addGradeToGroup(0,id1);
    }

    @Test
    public void addPerformance() throws SQLException {
        db.addPerformanceToGroup(10,id1);
        result = db.getGroups(id);
        assertEquals(10,result.get(0).getPerformance());
        db.addPerformanceToGroup(0,id1);
    }
}
