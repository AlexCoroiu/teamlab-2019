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
import org.junit.BeforeClass;
import org.junit.Test;
import nl.utwente.teamlab.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class CourseJUnit {
    static DB db = DB.getInstance();
    static Course course = new Course();
    static int id;
    static Teacher teacher = new Teacher("test@course");

    @BeforeClass
    public static void prepare() throws SQLException {
        course.setName("test course");
        course.setOwner(teacher);
        id = db.createCourse(course);
        course.setId(id);
    }

    @Test
    public void getCourse () throws SQLException {
        Course result = db.getCourse(id,true);
        assertEquals(course,result);
    }

    @Test
    public void getCourses() throws SQLException {
        ArrayList<Course> result = db.getCourses(teacher);
        course.setOwner(new Teacher());
        assertEquals(new ArrayList<>(Arrays.asList(course)), new ArrayList<>(Arrays.asList(result.get(0))));
        course.setOwner(teacher);

    }

    @Test
    public void setName() throws SQLException {
        String name = "Test name";
        db.addNameToCourse(id,name);
        Course result = db.getCourse(id,true);
        assertEquals(name,result.getName());
        db.addNameToCourse(id,"test course");
    }

    @Test
    public void setStudies() throws SQLException {
        String studies = "tcs,bit";
        db.addStudiesToCourse(id,studies);
        Course result = db.getCourse(id,true);
        assertEquals(studies,result.getStudiesString());
        db.addStudiesToCourse(id,"");
    }

    @Test
    public void setRules() throws SQLException {
        String rules = "groups size: 2+";
        db.addRulesToCourse(id,rules);
        Course result = db.getCourse(id,true);
        assertEquals(rules,result.getRules());
        db.addRulesToCourse(id,"");
    }

    @Test
    public void setStatus() throws SQLException {
        db.addStatus(id,true);
        Course result = db.getCourse(id,true);
        assertEquals(true, result.getStatus());
        db.addStatus(id,false);
    }
}
