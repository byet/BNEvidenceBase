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
public class Edge extends ProtegeIndividual{
    
    private final OWLNamedIndividual currentEdge;
    private final Map<OWLDataPropertyExpression, Set<OWLLiteral>> theDataProperties;
    private final Map<OWLObjectPropertyExpression, Set<OWLIndividual>> theObjectProperties;
    private Node comesFrom;
    private Node pointsTo;
    
    public Edge(OWLNamedIndividual givenEdge, OWLOntology givenOntology, Map<OWLDataPropertyExpression, Set<OWLLiteral>> givenDataProperties,
            Map<OWLObjectPropertyExpression, Set<OWLIndividual>> givenObjectProperties){
        super(givenEdge,givenOntology,givenDataProperties,givenObjectProperties);
        currentEdge = givenEdge;
        theOntology = givenOntology;
        theDataProperties = givenDataProperties;
        theObjectProperties = givenObjectProperties;
        
    }
    
    public void setComesFrom(ArrayList<Node> allTheNodes){
        OWLIndividual tempIndy;
        String tempName;
        
        for (Map.Entry<OWLObjectPropertyExpression, Set<OWLIndividual>> e : theObjectProperties.entrySet()){
            if(e.getKey().toString().contains("comesFrom")){
                Object []objs = e.getValue().toArray();
                tempIndy = (OWLIndividual) objs[0];
                tempName = tempIndy.toString().substring(tempIndy.toString().lastIndexOf("#")+1,tempIndy.toString().lastIndexOf(">"));              
                for(Node n:allTheNodes){
                    if(n.getName().equals(tempName)){
                        comesFrom = n;
                    }
                }
            }
        }
    }
    
    public Node getComesFrom(){
        return comesFrom;
    }
    
    public void setPointsTo(ArrayList<Node> allTheNodes){
        OWLIndividual tempIndy;
        String tempName;
        
        for (Map.Entry<OWLObjectPropertyExpression, Set<OWLIndividual>> e : theObjectProperties.entrySet()){
            if(e.getKey().toString().contains("pointsTo")){
                Object []objs = e.getValue().toArray();
                tempIndy = (OWLIndividual) objs[0];
                tempName = tempIndy.toString().substring(tempIndy.toString().lastIndexOf("#")+1,tempIndy.toString().lastIndexOf(">"));
                for(Node n:allTheNodes){
                    if(n.getName().equals(tempName)){
                        pointsTo = n;
                    }
                }
            }
        }
    }
    
    public Node getPointsTo(){
        return pointsTo;
    }
}
