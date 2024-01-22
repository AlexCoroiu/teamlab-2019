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

package nl.utwente.teamlab.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Objects;

@XmlRootElement
public class Chart {
    String name = "";
    ArrayList<String> labels = new ArrayList<>();
    ArrayList<Integer> values = new ArrayList<>();

    public Chart (){

    }

    public Chart(String name,ArrayList<String> labels, ArrayList<Integer> values) {
        this.name = name;
        this.labels = labels;
        this.values = values;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    public ArrayList<Integer> getValues() {
        return values;
    }

    public void setValues(ArrayList<Integer> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "Chart{" +
                "labels=" + labels +
                ", values=" + values +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chart chart = (Chart) o;
        return Objects.equals(name, chart.name) &&
                Objects.equals(labels, chart.labels) &&
                Objects.equals(values, chart.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, labels, values);
    }
}
