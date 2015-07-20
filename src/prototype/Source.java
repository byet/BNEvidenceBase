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
 * 
 */
public class Source extends ProtegeIndividual{
    private final Map<OWLDataPropertyExpression, Set<OWLLiteral>> theDataProperties;
    private final Map<OWLObjectPropertyExpression, Set<OWLIndividual>> theObjectProperties;
    private final String individualType;
    private String actualType;

    public Source(OWLNamedIndividual givenSource, OWLOntology givenOntology,Map<OWLDataPropertyExpression, Set<OWLLiteral>> givenDataProperties,
            Map<OWLObjectPropertyExpression, Set<OWLIndividual>> givenObjectProperties, String givenType){
        super(givenSource,givenOntology,givenDataProperties,givenObjectProperties);
        theOntology = givenOntology;
        theDataProperties = givenDataProperties;
        theObjectProperties = givenObjectProperties;
        individualType = givenType; 
    }
    
    public String getType(){
        actualType = toStringOWLName(individualType);
        return actualType;
    }
    
    public String getPMID(){
        String pubMedID = "None";
        if(this.getType().equals("Publication")){
            for (Map.Entry<OWLDataPropertyExpression, Set<OWLLiteral>> e : theDataProperties.entrySet()){
                if(e.getKey().toString().contains("publicationPMID")){
                    String tempID = e.getValue().toString();
                    pubMedID = tempID.substring(2);
                    int tempStringPosition = pubMedID.lastIndexOf("\"");
                    tempID = pubMedID.substring(0, tempStringPosition);
                    pubMedID = tempID;
                }
            }
        }
        return pubMedID;
    }
     public String getISBN(){
        String isbnno = "None";
        if(this.getType().equals("Publication")){
            for (Map.Entry<OWLDataPropertyExpression, Set<OWLLiteral>> e : theDataProperties.entrySet()){
                if(e.getKey().toString().contains("publicationISBN")){
                    String tempID = e.getValue().toString();
                    isbnno = tempID.substring(2);
                    int tempStringPosition = isbnno.lastIndexOf("\"");
                    tempID = isbnno.substring(0, tempStringPosition);
                    isbnno = tempID;
                }
            }
        }
        return isbnno;
    }
     
    public String getURL(){
        String url = "None";
        if(this.getType().equals("Publication")){
            for (Map.Entry<OWLDataPropertyExpression, Set<OWLLiteral>> e : theDataProperties.entrySet()){
                if(e.getKey().toString().contains("publicationURL")){
                    String tempID = e.getValue().toString();
                    url = tempID.substring(2);
                    int tempStringPosition = url.lastIndexOf("\"");
                    tempID = url.substring(0, tempStringPosition);
                    url = tempID;
                }
            }
        }
        return url;
    }

    public String getExCredentials(){
        String exCreds = "None";
        if(this.getType().equals("Expert")){
            for (Map.Entry<OWLDataPropertyExpression, Set<OWLLiteral>> e : theDataProperties.entrySet()){
                if(e.getKey().toString().contains("expertCredentials")){
                    String tempID = e.getValue().toString();
                    exCreds = tempID.substring(2);
                    int tempStringPosition = exCreds.lastIndexOf("\"");
                    tempID = exCreds.substring(0, tempStringPosition);
                    exCreds = tempID;
                }
            }
        }
        return exCreds;
    }
    
        public String getDataInfo(){
        String datInfo = "None";
        if(this.getType().equals("Data")){
            for (Map.Entry<OWLDataPropertyExpression, Set<OWLLiteral>> e : theDataProperties.entrySet()){
                if(e.getKey().toString().contains("datasetInfo")){
                    String tempID = e.getValue().toString();
                    datInfo = tempID.substring(2);
                    int tempStringPosition = datInfo.lastIndexOf("\"");
                    tempID = datInfo.substring(0, tempStringPosition);
                    datInfo = tempID;
                }
            }
        }
        return datInfo;
    }
}
