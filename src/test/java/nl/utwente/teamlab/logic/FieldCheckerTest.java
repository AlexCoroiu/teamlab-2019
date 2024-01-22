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

package nl.utwente.teamlab.logic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class FieldCheckerTest {

    FieldChecker checker = new FieldChecker();
    @Test
    public void checkString() {
        assertTrue(checker.checkString("name"));
        assertFalse(checker.checkString(" "));
    }

    @Test
    public void checkGender() {
        assertTrue(checker.checkGender("F"));
        assertFalse(checker.checkGender("Q"));
    }

    @Test
    public void checkBelbin() {
        assertTrue(checker.checkBelbin("T"));
        assertFalse(checker.checkBelbin("Leader"));
    }

    @Test
    public void checkNationality() {
        assertTrue(checker.checkNationality("NL"));
        assertFalse(checker.checkNationality("dutch"));
    }

    @Test
    public void checkStudy() {
        ArrayList<String> studies = new ArrayList<>(Arrays.asList("TCS","BIT"));
        assertTrue(checker.checkStudy("TCS",studies));
        assertFalse(checker.checkStudy("create",studies));
    }

    @Test
    public void checkMotivation() {
        assertTrue(checker.checkMotivation(3));
        assertFalse(checker.checkMotivation(10));
    }

    @Test
    public void checkKnowledge() {
        assertTrue(checker.checkKnowledge(0));
        assertFalse(checker.checkKnowledge(3));
    }

    @Test
    public void checkId(){
        assertTrue(checker.checkId(1));
        assertFalse(checker.checkId(0));
    }
}
