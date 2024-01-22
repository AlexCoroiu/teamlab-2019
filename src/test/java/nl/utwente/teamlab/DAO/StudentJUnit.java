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
import nl.utwente.teamlab.model.Student;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;


public class StudentJUnit {
    static DB db = DB.getInstance();
    static int id;
    static Course course = new Course();
    static Student student = new Student(10,"mada@mail","mada","F","RO","A",2,4,"TCS");

    @BeforeClass
    public static void prepare() throws SQLException {
        course.setName("test student");
        id = db.createCourse(course);
        db.addStudentToCourse(student,id);
    }

    @Test
    public void getStudents() throws SQLException {
        ArrayList<Student> result = db.getStudents(id);
        ArrayList<Student> students = new ArrayList<>(Arrays.asList(student));
        assertEquals(students,result);
    }

    @Test
    public void addResult() throws SQLException {
        db.addProgress(student,id);
    }
}
