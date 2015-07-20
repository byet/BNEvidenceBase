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

import java.util.ArrayList;
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
public class Evidence extends ProtegeIndividual{
    private final Map<OWLObjectPropertyExpression, Set<OWLIndividual>> theObjectProperties;
    private final Map<OWLDataPropertyExpression, Set<OWLLiteral>> theDataProperties;
    private final String individualType;
    private String actualType;
    private ArrayList<Source> evidenceSources = new ArrayList<Source>();
    
    public Evidence(OWLNamedIndividual givenEvidence, OWLOntology givenOntology,Map<OWLDataPropertyExpression, Set<OWLLiteral>> givenDataProperties,
            Map<OWLObjectPropertyExpression, Set<OWLIndividual>> givenObjectProperties, String givenType){
        super(givenEvidence,givenOntology,givenDataProperties,givenObjectProperties);
        theOntology = givenOntology;
        theDataProperties = givenDataProperties;
        theObjectProperties = givenObjectProperties;
        individualType = givenType; 
    }
    
    public String getType(){      
        if(toStringOWLName(individualType).equals("ConflictingEvidence")){
            actualType = "Conflicting Evidence";
        }
        else if(toStringOWLName(individualType).equals("ExcludedChild")){
            actualType = "Excluded Child";
        }
        else if(toStringOWLName(individualType).equals("ExcludedParent")){
            actualType = "Excluded Parent";
        }
        else if(toStringOWLName(individualType).equals("ExcludedRelation")){
            actualType = "Excluded Relation";
        }
        else if(toStringOWLName(individualType).equals("SupportingEvidence")){
            actualType = "Supporting Evidence";
        }
        
        return actualType;
    }
    
    public String getDescription(){
        String evidenceDescription = "None";
        String tempDescription;
        
        for (Map.Entry<OWLDataPropertyExpression, Set<OWLLiteral>> e : theDataProperties.entrySet()){
            if(toStringOWLName(e.getKey().toString()).equals("evidenceDescription")){
                Object []objs = e.getValue().toArray();
                tempDescription = objs[0].toString();
                evidenceDescription = tempDescription.substring(1, tempDescription.lastIndexOf("\""));
            }
        }
        
        return evidenceDescription;
    }
    
    public ArrayList<Source> getSources(ArrayList<Source> allTheSources){
        String tempName;
        if(evidenceSources.isEmpty()){
            for (Map.Entry<OWLObjectPropertyExpression, Set<OWLIndividual>> e : theObjectProperties.entrySet()){
                if(toStringOWLName(e.getKey().toString()).equals("hasSource")){
                    Object []objs = e.getValue().toArray();
                    for(int i=0; i<objs.length; i++){
                        tempName = objs[i].toString();
                        for(Source s: allTheSources){
                            if(s.getName().equals(toStringOWLName(tempName))){
                                evidenceSources.add(s);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return evidenceSources;
    }
}
