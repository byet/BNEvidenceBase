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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Ben Spencer
 * @author Barbaros Yet
 */
public class HTMLNodePage extends HTMLPage{    
    
    private final ArrayList<Node> allTheVariables;
    private final ArrayList<Edge> allTheRelations;
    private final ArrayList<Distribution> allTheDistributions;
    private final ArrayList<Evidence> allTheEvidence;
    private final ArrayList<Fragment> allTheFragments;
    private final ArrayList<Source> allTheSources;
    private final File tempFile;
    private final Node theVariable;
    private final String theDirectoryName;
    
    public HTMLNodePage(String name, Node theGivenNode, ArrayList<Edge> allTheEdges,
            ArrayList<Distribution> allTheDistributions, ArrayList<Evidence> allTheEvidence,
            ArrayList<Fragment> allTheFragments, ArrayList<Source> allTheSources,ArrayList<Node> allTheNodes, String directoryName){
        super(name,allTheEdges,allTheDistributions,allTheEvidence,allTheFragments,allTheSources,allTheNodes,directoryName);
        
        allTheRelations = allTheEdges;
        this.allTheDistributions = allTheDistributions;
        this.allTheEvidence = allTheEvidence;
        this.allTheFragments = allTheFragments;
        allTheVariables = allTheNodes;
        this.allTheSources = allTheSources;
        theVariable = theGivenNode;
        theDirectoryName = directoryName;
        
        tempFile = new File(theDirectoryName+"/variable/"+this.getPageName());
        createBlankFile(this.getPageName());
        try {
            addHTMLMenu("variable",null,theDirectoryName,this.getPageName());
            addBNImage("variable");
            completeVariableDocument();
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
        
    }

    private void createBlankFile(String givenPageName) {
        
        if(tempFile.exists()){
            tempFile.delete();
        }
        try {
            tempFile.createNewFile();
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }
    
    private void completeVariableDocument() throws IOException{
        String tempText = "<tr><td class=\"subHeading\">Variable:</td><td class=\"info\" id=\"top-info\" colspan=\"3\">" + this.theVariable.getName() + "</td></tr>\n";
        tempText += "<tr><td class=\"subHeading\">Description:</td><td class=\"info\" colspan=\"3\"><p>" + this.theVariable.getNodeDescription() + "</p></td></tr>\n";
        tempText += "<tr><td class=\"subHeading\">Distribution:</td><td class=\"info\" colspan=\"3\">" + this.theVariable.getType() + "</td></tr>\n";
        // add in states/bounds depending on type
        if(this.theVariable.getType().equals("Discrete")){
            String []states = this.theVariable.getStates();
            tempText += "<tr><td class=\"subHeading\">States:</td><td class=\"info\" colspan=\"3\">\n";
            for(int i=0; i<states.length; i++){
                tempText+="\t<li>" + states[i] + "</li>\n";
            }
            tempText+="</td></tr>";
        }
        else if(this.theVariable.getType().equals("Continuous")){
            double []bounds = this.theVariable.getBounds();
            tempText += "<tr><td class=\"subHeading\">Bounds:</td><td class=\"info\" colspan=\"3\">";
            tempText += "<li>Lower Bound: " + bounds[1] + "</li>";
            tempText += "<li>Upper Bound: " + bounds[0] + "</li></td></tr>\n";
            
        }
        //add in the evidene
        
        tempText+=getEvidenceSection(this.theVariable);
        
        //add the edges leading to parent nodes
        //BUT FIRST GET THE NUMBER OF PARENTS
        int numberOfParents = 3;
        /*if(this.theVariable.getParents(allTheRelations).size()<numberOfParents){
            numberOfParents = this.theVariable.getParents(allTheRelations).size();
        }*/
        

        tempText+=" <tr><td class=\"subHeading\" colspan=\"1\">Relations:</td>";
        
        
        tempText+="<td class=\"info\" colspan=\"1.5\">\n";
        tempText+="<p>Parent Variables:</p>\n";
        

        //THE PROBLEM IS HERE!
        for(Edge edge:this.theVariable.getParents(allTheRelations)){
 
                tempText+="\n\t<li><a href=\"../relation/"+ edge.getName() +".html\">" + getLongEdgeName(edge) + "</a></li>\n";


             
        }

        tempText+="</td>\n";
        //add edges leading to child nodes
        //BUT FIRST GET THE NUMBER OF CHILDREN
        int numberOfChildren = 3;
        /*if(this.theVariable.getChildren(allTheRelations).size()>2){
            numberOfChildren = this.theVariable.getChildren(allTheRelations).size();
        }*/
        tempText+="<td class=\"info\" colspan=\"1.5\">\n";
        tempText+="<p>Child Variables:</p>";
        
        for(Edge edge:this.theVariable.getChildren(allTheRelations)){

                tempText+="\n\t<li><a href=\"../relation/"+ edge.getName() +".html\">" + getLongEdgeName(edge) + "</a></li>\n";



        }
        // checks if there is any parameterisation source, adds the link if there is any.
        String parameterSource = "";        
        if (this.theVariable.getParametisedFrom(allTheSources) != null){
            parameterSource =  "\t<a href=\"../source/"+ this.theVariable.getParametisedFrom(allTheSources).getName() +".html\">" + this.theVariable.getParametisedFrom(allTheSources).getName() + "</a>";
        }
        tempText+="</td></tr>\n";
        //add what the node was parameterised from
        tempText+="<tr><td class=\"subHeading\">Parameterised From:</td>\n<td class=\"info\" colspan=\"2\">\n"
                + parameterSource +"</td></tr>\n";
        //add the distribution of the node
 /*       tempText+="<tr><td class=\"subHeading\">Distribution:</td>\n<td class =\"info\"  colspan=\"2\">\n"
                + "\t<a href=\"../distribution/"+ this.theVariable.getDistribution().getName() +".html\">" + this.theVariable.getDistribution().getType() + "</a></td></tr>\n";
   */     
        tempText+="</table></div>\n<div id=\"footer\">\n\t&copy; "+getYear()+" <a href=\"http://www.eecs.qmul.ac.uk/research/view/risk-and-information-management\"> Risk and Information Management (RIM) Research Group, Queen Mary, University of London</a>\n</div></body>\n</html>";
        appendCurrentDocument(tempText,"variable");
    }
}
