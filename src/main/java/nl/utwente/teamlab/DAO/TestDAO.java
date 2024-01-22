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

import nl.utwente.teamlab.model.Question;
import nl.utwente.teamlab.model.Teacher;
import nl.utwente.teamlab.model.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TestDAO {
    Connection conn;
    DB db;

    public TestDAO(Connection conn, DB db) {
        this.conn = conn;
        this.db = db;
    }

    public int createTest(Test test) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_TEST.toString(), Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, test.getName());
        ps.setString(2, test.getTeacher().getMail());
        ps.executeUpdate();
        int id = ps.getGeneratedKeys().getInt(1);
        ps.close();
        return id;
    }

    public ArrayList<Test> getTests(Teacher teacher) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.GET_TESTS.toString());
        ps.setString(1,teacher.getMail());
        ResultSet rs = ps.executeQuery();
        ArrayList<Test> tests = new ArrayList<Test>();
        int id;
        String name;

        while(rs.next()){
            id = rs.getInt("id");
            name = rs.getString("name");
            Test test = new Test(id, name);
            tests.add(test);
        }
        ps.close();
        return tests;
    }

    public Test getTest(int id, boolean answer) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.GET_TEST.toString());
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            String name = rs.getString("name");
            String mail = rs.getString("owner");
            ps.close();
            Teacher teacher = new Teacher(mail);
            HashMap<String, ArrayList<Question>> questions = db.getQuestions(id, answer);
            return new Test(id, name, teacher, questions);
        }
        return new Test();
    }

    public void addName(int id, String name) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.ADD_NAME_TO_TEST.toString());
        ps.setString(1,name);
        ps.setInt(2,id);
        ps.executeUpdate();
        ps.close();
    }
}
