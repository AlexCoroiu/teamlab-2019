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

import nl.utwente.teamlab.model.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherDAO {
    Connection conn;
    DB db;

    public TeacherDAO(Connection conn, DB db) {
        this.conn = conn;
        this.db = db;
    }

    public Teacher getTeacher(String mail, String name) throws SQLException {
        createTeacher(mail, name);
        PreparedStatement ps = conn.prepareStatement(Queries.GET_TEACHER.toString());
        ps.setString(1,mail);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            String name2 = rs.getString("name");
            ps.close();
            return new Teacher(mail,name2);
        }
        return new Teacher();
    }

    private void createTeacher(String mail, String name) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_TEACHER.toString());
        ps.setString(1,mail);
        ps.setString(2,name);
        ps.executeUpdate();
        ps.close();
    }
}
