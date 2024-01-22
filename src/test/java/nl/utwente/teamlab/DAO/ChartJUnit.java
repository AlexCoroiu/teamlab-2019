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

import nl.utwente.teamlab.model.Chart;
import nl.utwente.teamlab.model.Course;
import nl.utwente.teamlab.model.Student;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ChartJUnit {
    static DB db = DB.getInstance();
    private static int id;

    @BeforeClass
    public static void prepare() throws SQLException {
        Course course = new Course();
        course.setStudies(new ArrayList<>(Arrays.asList("ITA","NLD")));
        id = db.createCourse(course);
        Student s1 = new Student(1,"s1@mail","s1","F","ITA","T",1,4,"TCS");
        db.addStudentToCourse(s1,id);
        Student s2 = new Student(2,"s2@mail","s2","M","NLD","T",2,1,"BIT");
        db.addStudentToCourse(s2,id);
    }

    @Test
    public void getCharts() throws SQLException {
        Chart gender = new Chart("Gender", new ArrayList<>(Arrays.asList("F","M")),
                new ArrayList<>(Arrays.asList(1,1)));
        Chart nat = new Chart("Nationality", new ArrayList<>(Arrays.asList("ITA","NLD")),
                new ArrayList<>(Arrays.asList(1,1)));
        Chart belbin = new Chart("Belbin", new ArrayList<>(Arrays.asList("T")),
                new ArrayList<>(Arrays.asList(2)));
        Chart mot = new Chart("Motivation", new ArrayList<>(Arrays.asList("1","2")),
                new ArrayList<>(Arrays.asList(1,1)));
        Chart kno = new Chart("Knowledge", new ArrayList<>(Arrays.asList("1","4")), //ascending
                new ArrayList<>(Arrays.asList(1,1)));
        Chart study = new Chart("Study", new ArrayList<>(Arrays.asList("BIT","TCS")), //ascending
                new ArrayList<>(Arrays.asList(1,1)));
        ArrayList<Chart> charts = new ArrayList<>(Arrays.asList(gender,nat,belbin,mot,kno,study));
        assertEquals(charts,db.getCharts(id));

    }
}