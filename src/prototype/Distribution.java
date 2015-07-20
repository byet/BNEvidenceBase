/* 
 * Copyright (C) 2015 Barbaros Yet & William Marsh.
 *
 * This file is a part of BNEvidenceBase library.
 * 
 * BNEvidenceBase library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package prototype;

import java.util.Map;
import java.util.Set;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 *
 * @author Ben Spencer
 * @author Barbaros Yet
 */
public class Distribution extends ProtegeIndividual{
    public Distribution(OWLNamedIndividual givenDistribution, OWLOntology givenOntology,Map<OWLDataPropertyExpression, Set<OWLLiteral>> givenDataProperties,
            Map<OWLObjectPropertyExpression, Set<OWLIndividual>> givenObjectProperties){
        super(givenDistribution,givenOntology,givenDataProperties,givenObjectProperties);
    }
    
    public String getType(){
        String name = this.getName();
        if(name.equals("NormalMixture")){
            return "Normal Mixture";
        }
        else if(name.equals("NPT")){
            return "NPT";
        }
        else if(name.equals("Normal")){
            return "Normal";
        }
        else if(name.equals("Uniform")){
            return "Uniform";
        }
        return"None";
    }
}
