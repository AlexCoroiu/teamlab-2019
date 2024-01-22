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

import nl.utwente.teamlab.model.Group;
import nl.utwente.teamlab.model.Student;

import java.sql.*;
import java.util.ArrayList;

public class GroupDAO {
    Connection conn;
    DB db;

    public GroupDAO(Connection conn, DB db) {
        this.conn = conn;
        this.db = db;
    }

    public void createGroups(ArrayList<Group> groups, int id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_GROUP.toString(), Statement.RETURN_GENERATED_KEYS);
        ps.setInt(2,id);
        ps.setInt(3,0);
        ps.setInt(4,0);
        int gid;

        for(Group g: groups){
            ps.setInt(1,g.getName());
            ps.executeUpdate();
            gid = ps.getGeneratedKeys().getInt(1);
            g.setId(gid);
        }
        ps.close();
        for(Group g: groups) {
            addGroupToStudents(g,id);
        }
    }

    private void addGroupToStudents(Group g, int id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.ADD_GROUP_TO_STUDENT.toString());
        ps.setInt(1,g.getId());
        ps.setInt(3,id);
        ArrayList<Student> students = g.getStudents();
        for(Student s: students){
            ps.setInt(2,s.getId());
            ps.executeUpdate();
        }
        ps.close();
    }

    //workaroud nfor the bad db column naming
    public ArrayList<Group> getGroups(int course) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.GET_GROUPS.toString());
        ps.setInt(1,course);
        ResultSet rs = ps.executeQuery();

        ArrayList<Group> groups = new ArrayList<>();
        Group group = new Group();
        while(rs.next()) {
            int id = rs.getInt("gid");
            if(id != group.getId()) {
                group = new Group(id,
                        rs.getInt("gname"),
                        rs.getInt("grade"),
                        rs.getInt("performance"),
                        new ArrayList<Student>());
                groups.add(group);
            }
            group.addStudent(new Student(rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("name"),
                    rs.getString("gender"),
                    rs.getString("nationality"),
                    rs.getString("belbin"),
                    rs.getInt("motivation"),
                    rs.getInt("knowledge"),
                    rs.getString("study")));
        }
        ps.close();
        return groups;
    }
    public void addGradeToGroup(int grade, int id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.ADD_GRADE_TO_GROUP.toString());
        ps.setInt(1, grade);
        ps.setInt(2, id);
        ps.executeUpdate();
        ps.close();
    }


    public void addPerformanceToGroup(int performance, int id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.ADD_PERFORMANCE_TO_GROUP.toString());
        ps.setInt(1,performance);
        ps.setInt(2,id);
        ps.executeUpdate();
        ps.close();
    }
}
