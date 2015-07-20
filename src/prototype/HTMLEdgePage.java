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
public class HTMLEdgePage extends HTMLPage{    
    
    private final ArrayList<Node> allTheVariables;
    private final ArrayList<Edge> allTheRelations;
    private final ArrayList<Distribution> allTheDistributions;
    private final ArrayList<Evidence> allTheEvidence;
    private final ArrayList<Fragment> allTheFragments;
    private final ArrayList<Source> allTheSources;
    private final File tempFile;
    private final Edge theRelation;
    private final String theDirectoryName;
    
    public HTMLEdgePage(String name, Edge theGivenEdge, ArrayList<Edge> allTheEdges,
            ArrayList<Distribution> allTheDistributions, ArrayList<Evidence> allTheEvidence,
            ArrayList<Fragment> allTheFragments, ArrayList<Source> allTheSources,ArrayList<Node> allTheNodes, String directoryName){
        super(name,allTheEdges,allTheDistributions,allTheEvidence,allTheFragments,allTheSources,allTheNodes,directoryName);
        
        allTheRelations = allTheEdges;
        this.allTheDistributions = allTheDistributions;
        this.allTheEvidence = allTheEvidence;
        this.allTheFragments = allTheFragments;
        allTheVariables = allTheNodes;
        this.allTheSources = allTheSources;
        theRelation = theGivenEdge;
        theDirectoryName = directoryName;
        
        tempFile = new File(theDirectoryName+"/relation/"+this.getPageName());
        createBlankFile(this.getPageName());
        try {
            addHTMLMenu("relation",null,theDirectoryName,this.getPageName());
            addBNImage("relation"); 
            completeRelationDocument();
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
    
    private void completeRelationDocument() throws IOException{
        
        String tempText = "<tr><td class=\"subHeading\">Relation:</td>\n";
        tempText +=  "<td class=\"info\"><a href=\"../variable/"+ this.theRelation.getComesFrom().getName() +".html\">" + this.theRelation.getComesFrom().getName() + "</a>";
        tempText +=  "<big>&rarr;</big>";
        tempText +=  "<a href=\"../variable/"+ this.theRelation.getPointsTo().getName() +".html\">\n" + this.theRelation.getPointsTo().getName() + "</a></td></tr>";
                
        /*     
        //add the node which the edge comes from
        tempText += "<tr><td class=\"subHeading\">Coming From:</td>\n"
                + "<td class=\"info\"><a href=\"../variable/"+ this.theRelation.getComesFrom().getName() +".html\">" 
                + this.theRelation.getComesFrom().getName() + "</a></td></tr>\n";
        //add the node which the edge points to
        tempText += "<tr><td class=\"subHeading\">Points to:</td>\n"
                + "<td class=\"info\"><a href=\"../variable/"+ this.theRelation.getPointsTo().getName() +".html\">\n" 
                + this.theRelation.getPointsTo().getName() + "</a></td></tr>";
        
        */
        
        tempText += getEvidenceSection(this.theRelation);
        //tempText +="</body></html>";
        tempText +="</table></div>\n<div id=\"footer\">&copy; "+getYear()+" <a href=\"http://www.eecs.qmul.ac.uk/research/view/risk-and-information-management\"> Risk and Information Management (RIM) Research Group, Queen Mary, University of London</a></div></body></html>\n";
        appendCurrentDocument(tempText, "relation");
    }
      @Override
      public String getEvidenceSection(ProtegeIndividual pI){

        String evidenceSection="";
            if(pI.getEvidence(allTheEvidence).size()>0){
                
            evidenceSection+="<tr><td class=\"subHeading\">Evidence:</td><td class=\"info\" colspan=\"2\"><div>\n";
            evidenceSection+="<ul>";
            for(Evidence e:pI.getEvidence(allTheEvidence)){
                evidenceSection+="<div><li align=\"justify\">"+e.getDescription()+"<br><a class=\"ref\">[References]</a> "+"</li>\n";
        
                evidenceSection+="<ul class=\"refblock\">";
                for(Source s:e.getSources(allTheSources)){
                    evidenceSection+= "<li><a href=\"../source/"+ s.getName() +".html\">" + s.getName() +" "+ "</a></li>\n";
                        
                    
              
            }
                evidenceSection+="</ul></div>\n";
            }
   
            evidenceSection+="\n</ul></div></td>\n</tr>\n";}
        return evidenceSection;
    }
}
