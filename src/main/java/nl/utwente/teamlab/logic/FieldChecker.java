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

import nl.utwente.teamlab.DAO.DB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class FieldChecker {

    public static final ArrayList<String> GENDERS = new ArrayList<>(Arrays.asList("F","M","NB"));
    public static final ArrayList<String> BELBINS = new ArrayList<>(Arrays.asList("A","T","P"));
    public static final ArrayList<String> NATS = new ArrayList<>(Arrays.asList(Locale.getISOCountries())); //alpha-2
    public static final int LOW = 0;
    public static final int HIGH = 4;
    public static final int KNOWLEDGE_HIGH = 2;

    public boolean checkString(String name) {
        if (name == null || name.equals("") || name.equals(" ")){ //TODO more elaborate checking
            return false;
        }
        return true;
    }

    public boolean checkGender(String gender){
        if(GENDERS.contains(gender)){
            return true;
        }
        return false;
    }

    public boolean checkBelbin(String belbin){
        if(BELBINS.contains(belbin)){
            return true;
        }
        return false;
    }

    public boolean checkNationality(String nat){
        if(NATS.contains(nat)){
            return true;
        }
        return false;
    }

    public boolean checkStudy(String study, ArrayList<String> studies) {
        if(studies.contains(study)){
            return true;
        }
        return false;
    }

    public boolean checkMotivation(int mot){
        if(mot >=LOW && mot <= HIGH){
            return true;
        }
        return false;
    }

    public boolean checkKnowledge(int kno){
        if(kno >= LOW && kno <= KNOWLEDGE_HIGH){
            return true;
        }
        return false;
    }

    public boolean checkId(int id){
        if(id != 0 ){
            return true;
        }
        return false;
    }
}
