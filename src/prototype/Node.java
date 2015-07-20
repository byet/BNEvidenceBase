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
public class Node extends ProtegeIndividual {
    
    private OWLNamedIndividual currentNode;
    //private OWLOntology theOntology;
    private Map<OWLDataPropertyExpression, Set<OWLLiteral>> theDataProperties;
    private Map<OWLObjectPropertyExpression, Set<OWLIndividual>> theObjectProperties;
    private Distribution currentDistribution;
    
    public Node(OWLNamedIndividual givenNode, OWLOntology givenOntology, Map<OWLDataPropertyExpression, Set<OWLLiteral>> givenDataProperties,
            Map<OWLObjectPropertyExpression, Set<OWLIndividual>> givenObjectProperties){
        super(givenNode,givenOntology,givenDataProperties,givenObjectProperties);
        currentNode = givenNode;
        theOntology = givenOntology;
        theDataProperties = givenDataProperties;
        theObjectProperties = givenObjectProperties;
    }
    
    public String getNodeDescription(){
        String nodeDescription = "None";
        String temp;
        for (Map.Entry<OWLDataPropertyExpression, Set<OWLLiteral>> e : theDataProperties.entrySet()){
            
            if(e.getKey().toString().contains("variableDescription")){
                temp = e.getValue().toString();
                nodeDescription = temp.substring(2, temp.lastIndexOf("\""));
                break;
            }
        }
        return nodeDescription;
    }
    
    public String getType(){
        String nodeType = "None";
        String temp;
        for (Map.Entry<OWLDataPropertyExpression, Set<OWLLiteral>> e : theDataProperties.entrySet()){
            
            if(e.getKey().toString().contains("upperBound")|| e.getKey().toString().contains("lowerBound")){
                nodeType = "Continuous";
                break;
            }
            else if(e.getKey().toString().contains("stateName")){
                nodeType = "Discrete";
                break;
            }
            
        }
        return nodeType;
    }
    
    public double[] getBounds(){
        double upperAndLower[] = new double[2];
        String upperString;
        String lowerString;
        String tempUpper;
        String tempLower;
        double upperBound = 0;
        double lowerBound = 0;
        boolean check1 = false;
        boolean check2 = false;
        
        if(this.getType().equals("Continuous")){

            for (Map.Entry<OWLDataPropertyExpression, Set<OWLLiteral>> e : theDataProperties.entrySet()){

                if(e.getKey().toString().contains("upperBound")){
                    tempUpper = e.getValue().toString();
                    upperString = tempUpper.substring(2);
                    int tempStringPosition = upperString.lastIndexOf("\"");
                    tempUpper = upperString.substring(0, tempStringPosition);
                    upperBound = Double.parseDouble(tempUpper);
                    check1 = true;
                }
                else if(e.getKey().toString().contains("lowerBound")){
                    tempLower = e.getValue().toString();
                    lowerString = tempLower.substring(2);
                    int tempStringPosition = lowerString.lastIndexOf("\"");
                    tempLower = lowerString.substring(0, tempStringPosition);
                    lowerBound = Double.parseDouble(tempLower);
                    check2 = true;
                }
                
                if(check1&&check2){
                    break;
                }
            }
            upperAndLower[0] = upperBound;
            upperAndLower[1] = lowerBound;
        }
        return upperAndLower;
    }
    
    public String[] getStates(){
        
        String states[] = null;
        
        for (Map.Entry<OWLDataPropertyExpression, Set<OWLLiteral>> e : theDataProperties.entrySet()){
            if(e.getKey().toString().contains("stateName")){
                Object objs[] = e.getValue().toArray();
                states = new String [objs.length];
                for(int i=0; i<objs.length; i++){
                    String tempStr = objs[i].toString();
                    states[i] = tempStr.substring(1, tempStr.length()-1);
                }
            }
        }
        return states;
    }
    
    public void setDistribution(ArrayList<Distribution> theDistributions){
        for (Map.Entry<OWLObjectPropertyExpression, Set<OWLIndividual>> e : theObjectProperties.entrySet()){
            if(toStringOWLName(e.getKey().toString()).contains("hasDistribution")){
                Object objs[] = e.getValue().toArray();
                String distributionName = toStringOWLName(objs[0].toString());
                for(Distribution dist: theDistributions){
                    if(dist.getName().equals(distributionName)){
                        currentDistribution = dist;
                    }
                }
            }
        }
    }
    public Distribution getDistribution(){
        return currentDistribution;
    }
    
    public ArrayList<Edge> getParents(ArrayList<Edge> allTheEdges){
        ArrayList<Edge> parentEdges = new ArrayList<Edge>();
        for(Edge e:allTheEdges){
            if(e.getPointsTo().getName().equals(this.getName())){
                parentEdges.add(e);
            }
        }
        return parentEdges;
    }
    
    public ArrayList<Edge> getChildren(ArrayList<Edge> allTheEdges){
        ArrayList<Edge> childEdges = new ArrayList<Edge>();
        for(Edge e:allTheEdges){
            if(e.getComesFrom().getName().equals(this.getName())){
                childEdges.add(e);
            }
        }
        return childEdges;
    }
    
    public Source getParametisedFrom(ArrayList<Source> allTheSources){
        Source tempSource = null;
        for (Map.Entry<OWLObjectPropertyExpression, Set<OWLIndividual>> e : theObjectProperties.entrySet()){
            if(toStringOWLName(e.getKey().toString()).contains("parameterisedFrom")){
                Object objs[] = e.getValue().toArray();
                String datasetName = toStringOWLName(objs[0].toString());
                for(Source s: allTheSources){
                    if(s.getName().equals(datasetName)){
                        return s;
                    }
                }
            }
        }
        return tempSource;
    }
}