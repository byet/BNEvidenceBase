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
public class HTMLEvidencePage extends HTMLPage{    
    
    private final ArrayList<Node> allTheVariables;
    private final ArrayList<Edge> allTheRelations;
    private final ArrayList<Distribution> allTheDistributions;
    private final ArrayList<Evidence> allTheEvidence;
    private final ArrayList<Fragment> allTheFragments;
    private final ArrayList<Source> allTheSources;
    private final File tempFile;
    private final Evidence theEvidence;
    private final String theDirectoryName;
    
    public HTMLEvidencePage(String name, Evidence theGivenEvidence, ArrayList<Edge> allTheEdges,
            ArrayList<Distribution> allTheDistributions, ArrayList<Evidence> allTheEvidence,
            ArrayList<Fragment> allTheFragments, ArrayList<Source> allTheSources,ArrayList<Node> allTheNodes,String directoryName){
        super(name,allTheEdges,allTheDistributions,allTheEvidence,allTheFragments,allTheSources,allTheNodes,directoryName);
        
        allTheRelations = allTheEdges;
        this.allTheDistributions = allTheDistributions;
        this.allTheEvidence = allTheEvidence;
        this.allTheFragments = allTheFragments;
        allTheVariables = allTheNodes;
        this.allTheSources = allTheSources;
        theEvidence = theGivenEvidence;
        theDirectoryName = directoryName;
        
        
        tempFile = new File(directoryName+"/evidence/"+this.getPageName());
        createBlankFile(this.getPageName());
        try {
            addHTMLMenu("evidence",null,theDirectoryName,this.getPageName());
            completeEvidenceDocument();
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
    
    private void completeEvidenceDocument() throws IOException{
        
        String tempText = "<table><tr><td class=\"subHeading\">Evidence:</td><td class=\"info\">" 
                + this.theEvidence.getName() + "</td></tr>\n";
        
        //add the type of evidence
        tempText += "<tr><td class=\"subHeading\">Type:</td><td class=\"info\">" 
                + this.theEvidence.getType() + "</td></tr>\n";
        
        //add the evidence description
        tempText += "<tr><td class=\"subHeading\">Description:</td><td class=\"info\">" 
                + this.theEvidence.getDescription() + "</td></tr>\n";
        

        //add the sources relating to the given piece of evidence
        tempText += "<tr><td class=\"subHeading\">References:</td><td class=\"info\" colspan=\"3\">\n";
        
        for(Source s:this.theEvidence.getSources(allTheSources)){
                tempText+="\t<li><a href=\"../source/"+ s.getName() +".html\">" + s.getName() + "</a></li>\n";
            }

        
        
        tempText+= "</td></tr>\n";
        //tempText +="</body></html>";
        tempText+="</table></div>\n<div id=\"footer\">\n\t&copy; "+getYear()+" <a href=\"http://www.eecs.qmul.ac.uk/research/view/risk-and-information-management\"> Risk and Information Management (RIM) Research Group, Queen Mary, University of London</a>\n</div>\n</body>\n</html>";
        appendCurrentDocument(tempText, "evidence");
    }
}