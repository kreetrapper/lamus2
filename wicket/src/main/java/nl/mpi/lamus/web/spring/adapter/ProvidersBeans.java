/*
 * Copyright (C) 2013 Max Planck Institute for Psycholinguistics
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
package nl.mpi.lamus.web.spring.adapter;

import nl.mpi.archiving.corpusstructure.core.UnknownNodeException;
import nl.mpi.archiving.corpusstructure.provider.CorpusStructureProviderFactory;
import nl.mpi.archiving.tree.corpusstructure.CorpusStructureTreeModelProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author guisil
 */
@Configuration
@Profile(value = {"cmdi-adapter-csdb"})
public class ProvidersBeans {
    
    @Autowired
    private CorpusStructureProviderFactory csdbFactory;
    
    @Bean
    @Scope("prototype")
    @Qualifier("createWorkspaceTreeProvider")
    public CorpusStructureTreeModelProvider createWorkspaceTreeProvider() throws UnknownNodeException {
        return new CorpusStructureTreeModelProvider(csdbFactory);
    }
}