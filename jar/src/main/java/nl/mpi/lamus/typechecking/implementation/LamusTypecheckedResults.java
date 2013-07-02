/*
 * Copyright (C) 2013 Max Planck Institute for Psycholinguistics
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.mpi.lamus.typechecking.implementation;

import nl.mpi.lamus.typechecking.TypecheckedResults;
import nl.mpi.lamus.typechecking.TypecheckerJudgement;
import nl.mpi.lamus.workspace.model.WorkspaceNodeType;

/**
 *
 * @author Guilherme Silva <guilherme.silva@mpi.nl>
 */
public class LamusTypecheckedResults implements TypecheckedResults {

    private WorkspaceNodeType checkedNodeType;
    private String checkedMimetype;
    private String analysis;
    private TypecheckerJudgement typecheckerJudgement;
    
    public LamusTypecheckedResults(WorkspaceNodeType nodeType, String mimetype, String analysis, TypecheckerJudgement judgement) {
        this.checkedNodeType = nodeType;
        this.checkedMimetype = mimetype;
        this.analysis = analysis;
        this.typecheckerJudgement = judgement;
    }
    
    public WorkspaceNodeType getCheckedNodeType() {
        return checkedNodeType;
    }

    public String getCheckedMimetype() {
        return checkedMimetype;
    }
    
    public String getAnalysis() {
        return analysis;
    }

    public TypecheckerJudgement getTypecheckerJudgement() {
        return typecheckerJudgement;
    }
    
    public boolean isTypeUnspecified() {
        if(checkedMimetype.startsWith("Un")) { //TODO use a better way to identify these cases
            return true;
        }
        return false;
    }
}