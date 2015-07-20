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
import static prototype.ProtegeIndividual.toStringOWLName;

/**
 *
 * @author Ben Spencer
 * @author Barbaros Yet
 */
public class Fragment extends ProtegeIndividual{
    
    private final OWLNamedIndividual currentFragment;
    private final Map<OWLDataPropertyExpression, Set<OWLLiteral>> theDataProperties;
    private final Map<OWLObjectPropertyExpression, Set<OWLIndividual>> theObjectProperties;
    private ArrayList<Node> membersList = new ArrayList<Node>();
    private ArrayList<Fragment> fragmembersList = new ArrayList<Fragment>();
    
    public Fragment(OWLNamedIndividual givenFragment, OWLOntology givenOntology, Map<OWLDataPropertyExpression, Set<OWLLiteral>> givenDataProperties,
            Map<OWLObjectPropertyExpression, Set<OWLIndividual>> givenObjectProperties){
        super(givenFragment,givenOntology,givenDataProperties,givenObjectProperties);
        currentFragment = givenFragment;
        theOntology = givenOntology;
        theDataProperties = givenDataProperties;
        theObjectProperties = givenObjectProperties;
    }
    
    public String getSummary(){
        String summaryString = "None";
        String temp;
        int posOfMark;
        
        for (Map.Entry<OWLDataPropertyExpression, Set<OWLLiteral>> e : theDataProperties.entrySet()){
            
            if(e.getKey().toString().contains("summary")){
                Object []objs = e.getValue().toArray();
                temp = objs[0].toString();
                posOfMark = temp.lastIndexOf("\"");
                summaryString = temp.substring(1, posOfMark);
                break;
            }
        }
        
        return summaryString;
    }
    
    public void setMembersList(ArrayList<Node> allTheNodes, ArrayList<Fragment> allTheFragments){
        int currentSize = 0;
        for (Map.Entry<OWLObjectPropertyExpression, Set<OWLIndividual>> e : theObjectProperties.entrySet()){
            if(toStringOWLName(e.getKey().toString()).equals("contains")){
                for(OWLIndividual indy: e.getValue()){
                    currentSize=membersList.size();
                    for(Fragment frag: allTheFragments){
                        if(toStringOWLName(indy.toString()).equals(frag.getName())){
                            //WE HAVE A FRAGMENT WITHIN A FRAGEMENT
                            this.fragmentWithinFragment(indy, allTheNodes, allTheFragments, theOntology);
                        }
                    }
                    //IF the fragment just contains a normal node
                    if(currentSize==membersList.size()){
                        membersList.add(checkNode(indy,allTheNodes));
                    }
                }
            }
        }
    }
    
    private static Node checkNode(OWLIndividual testIndy,ArrayList<Node> allTheNodes){
        for(Node n:allTheNodes){
            if(toStringOWLName(testIndy.toString()).equals(n.getName())){
                return n;
            }
        }
        return null;
    }
    
    private void fragmentWithinFragment(OWLIndividual fragIndy, ArrayList<Node> allTheNodes, ArrayList<Fragment> allTheFrags, OWLOntology theOntology){
        int fragmentCurrentSize = 0;
        Map<OWLObjectPropertyExpression, Set<OWLIndividual>> tempObjectProperties = fragIndy.getObjectPropertyValues(theOntology);
        for (Map.Entry<OWLObjectPropertyExpression, Set<OWLIndividual>> x : tempObjectProperties.entrySet()){
            if(toStringOWLName(x.getKey().toString()).equals("contains")){
                for(OWLIndividual indyNextLevel: x.getValue()){
                    fragmentCurrentSize = membersList.size();
                    for(Fragment frag: allTheFrags){
                        if(toStringOWLName(indyNextLevel.toString()).equals(frag.getName())){
                            this.fragmentWithinFragment(indyNextLevel, allTheNodes, allTheFrags, theOntology);
                            //method calls itself finding the lowest level where a fragment no longer contains
                            //any more fragments and just nodes
                        }
                    }
                    if(fragmentCurrentSize==membersList.size()){
                        membersList.add(checkNode(indyNextLevel,allTheNodes));
                    }
                }
            }
        }
    }
    
    public ArrayList<Node> getMembersList(){
        return membersList;
    }
    
        public void setFragMembersList(ArrayList<Fragment> allTheFragments){
        int currentSize = 0;
        for (Map.Entry<OWLObjectPropertyExpression, Set<OWLIndividual>> e : theObjectProperties.entrySet()){
            if(toStringOWLName(e.getKey().toString()).equals("contains")){
                for(OWLIndividual indy: e.getValue()){
                    currentSize=fragmembersList.size();
                     for(Fragment frag: allTheFragments){
                        if(toStringOWLName(indy.toString()).equals(frag.getName())){
                            //WE HAVE A FRAGMENT WITHIN A FRAGEMENT
                            fragmembersList.add(checkFrag(indy,allTheFragments));
                            
                        }
                    }
                }
            }
        }
    }
        private static Fragment checkFrag(OWLIndividual testIndy,ArrayList<Fragment> allTheFragments){
        for(Fragment n:allTheFragments){
            if(toStringOWLName(testIndy.toString()).equals(n.getName())){
                return n;
            }
        }
        return null;
    }
            public ArrayList<Fragment> getFragMembersList(){
        return fragmembersList;
    }
    
}
