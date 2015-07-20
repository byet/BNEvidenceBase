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
public abstract class ProtegeIndividual {
    
    private OWLNamedIndividual indy;
    public OWLOntology theOntology;
    private Map<OWLDataPropertyExpression, Set<OWLLiteral>> individualsDataProperties;
    private Map<OWLObjectPropertyExpression, Set<OWLIndividual>> individualObjectProperties;
    
    public ProtegeIndividual(OWLNamedIndividual givenIndy, OWLOntology givenOntology,Map<OWLDataPropertyExpression, Set<OWLLiteral>> givenDataProperties,
            Map<OWLObjectPropertyExpression, Set<OWLIndividual>> givenObjectProperties){
        indy = givenIndy;
        theOntology = givenOntology;
        individualsDataProperties = givenDataProperties;
        individualObjectProperties = givenObjectProperties;
    }
    
    public String getName(){
        String indyName = indy.toString();
            int startOfName = 0;
            for(int i = 0; i<indyName.length(); i++){
                if(indyName.charAt(i)=='#'){
                    startOfName = i + 1;
                    break;
                } 
            }
        return indyName.substring(startOfName, indyName.length()-1);
    }
    
    public static String toStringOWLName(String givenName){
        return givenName.substring(givenName.lastIndexOf("#")+1, givenName.lastIndexOf(">"));
        
    }
    
    public ArrayList<Evidence> getEvidence(ArrayList<Evidence> allTheEvidence){
        ArrayList<Evidence> individualEvidence = new ArrayList<Evidence>();
        for (Map.Entry<OWLObjectPropertyExpression, Set<OWLIndividual>> e : individualObjectProperties.entrySet()){
            if(toStringOWLName(e.getKey().toString()).equals("hasConflictingEvidence")||
                    toStringOWLName(e.getKey().toString()).equals("hasExcludedChild")||
                    toStringOWLName(e.getKey().toString()).equals("hasExcludedParent")||
                    toStringOWLName(e.getKey().toString()).equals("hasExcludedRelation")||
                    toStringOWLName(e.getKey().toString()).equals("hasSupportingEvidence")){
                
                Object []objs = e.getValue().toArray();
                for(int i=0; i<objs.length; i++){
                    for(Evidence evidence: allTheEvidence){
                        if(evidence.getName().equals(toStringOWLName(objs[i].toString()))){
                            individualEvidence.add(evidence);
                        }
                    }
                }
            }
            
        }
        return individualEvidence;
    }
}
