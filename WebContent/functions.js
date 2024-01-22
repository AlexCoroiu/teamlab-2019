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

function gotoPage (url) {
    if (url === window.location.href) return;
    else window.location.href = url
}
function saveId (id) {
    sessionStorage.setItem('id',id)
}

function saveProcedure (procedure) {
    sessionStorage.setItem('procedure',procedure)
}

function normalizeArrayDeserialization (unnormalizedArray) {
    let normalizedArray = null;
    if (Array.isArray(unnormalizedArray)) {
        normalizedArray = unnormalizedArray;
    } else {
        if (typeof unnormalizedArray === "undefined") {
            normalizedArray = []
        } else {
            normalizedArray = [unnormalizedArray];
        }
    }
    return normalizedArray;
}
