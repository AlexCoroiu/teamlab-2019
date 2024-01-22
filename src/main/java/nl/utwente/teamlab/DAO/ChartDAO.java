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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChartDAO {
    Connection conn;
    DB db;

    public ChartDAO(Connection conn, DB db) {
        this.conn = conn;
        this.db = db;
    }

    public List<Chart> getCharts(int course) throws SQLException {
        List<Chart> charts = new ArrayList<Chart>();
        charts.add(getGenderChart(course));
        charts.add(getNationalityChart(course));
        charts.add(getBelbinChart(course));
        charts.add(getMotivationChart(course));
        charts.add(getKnowledgeChart(course));
        charts.add(getStudyChart(course));
        System.out.println(charts);
        return charts;
    }

    private Chart getStudyChart(int course) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.GET_STUDENTS_STUDY.toString());
        ps.setInt(1,course);
        ResultSet rs = ps.executeQuery();
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        while(rs.next()){
            labels.add(rs.getString("study"));
            values.add(rs.getInt("count(study)"));
        }
        ps.close();
        Chart chart = new Chart("Study",labels,values);

        return chart;
    }

    private Chart getKnowledgeChart(int course) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.GET_STUDENTS_KNOWLEDGE.toString());
        ps.setInt(1,course);
        ResultSet rs = ps.executeQuery();
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        while(rs.next()){
            labels.add(rs.getString("knowledge"));
            System.out.println(labels);
            values.add(rs.getInt("count(knowledge)"));
        }
        ps.close();
        Chart chart = new Chart("Knowledge",labels,values);

        return chart;
    }

    private Chart getMotivationChart(int course) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.GET_STUDENTS_MOTIVATION.toString());
        ps.setInt(1,course);
        ResultSet rs = ps.executeQuery();
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        while(rs.next()){
            labels.add(rs.getString("motivation"));
            values.add(rs.getInt("count(motivation)"));
        }
        ps.close();
        Chart chart = new Chart("Motivation",labels,values);

        return chart;
    }

    private Chart getBelbinChart(int course) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.GET_STUDENTS_BELBIN.toString());
        ps.setInt(1,course);
        ResultSet rs = ps.executeQuery();
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        while(rs.next()){
            labels.add(rs.getString("belbin"));
            values.add(rs.getInt("count(belbin)"));
        }
        ps.close();
        Chart chart = new Chart("Belbin",labels,values);

        return chart;
    }

    private Chart getNationalityChart(int course) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.GET_STUDENTS_NATIONALITY.toString());
        ps.setInt(1,course);
        ResultSet rs = ps.executeQuery();
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        while(rs.next()){
            labels.add(rs.getString("nationality"));
            values.add(rs.getInt("count(nationality)"));
        }
        ps.close();
        Chart chart = new Chart("Nationality",labels,values);

        return chart;
    }

    private Chart getGenderChart(int course) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.GET_STUDENTS_GENDER.toString());
        ps.setInt(1,course);
        ResultSet rs = ps.executeQuery();
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        while(rs.next()){
            labels.add(rs.getString("gender"));
            values.add(rs.getInt("count(gender)"));
        }
        ps.close();
        Chart chart = new Chart("Gender",labels,values);

        return chart;
    }
}
