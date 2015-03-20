/*
 * Copyright (C) 2014 Max Planck Institute for Psycholinguistics
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.mpi.lamus.typechecking;

import java.io.File;
import java.util.Collection;
import nl.mpi.lamus.typechecking.implementation.MetadataValidationIssue;

/**
 * Class used to perform validation checks in metadata files.
 * @author guisil
 */
public interface MetadataChecker {
    
    /**
     * Performs the appropriate validation checks for the given uploaded file.
     * @param metadataFile Metadata file to check
     * @return validation issues, if any
     */
    public Collection<MetadataValidationIssue> validateUploadedFile(File metadataFile) throws Exception;
    
    /**
     * Performs the appropriate validation checks for the given submitted file.
     * @param metadataFile Metadata file to check
     * @return validation issues, if any
     */
    public Collection<MetadataValidationIssue> validateSubmittedFile(File metadataFile) throws Exception;
}
