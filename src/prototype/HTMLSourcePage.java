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
public class HTMLSourcePage extends HTMLPage{    
    
    private final ArrayList<Node> allTheVariables;
    private final ArrayList<Edge> allTheRelations;
    private final ArrayList<Distribution> allTheDistributions;
    private final ArrayList<Evidence> allTheEvidence;
    private final ArrayList<Fragment> allTheFragments;
    private final ArrayList<Source> allTheSources;
    private final File tempFile;
    private final Source theSource;
    private final String theDirectoryName;
    
    public HTMLSourcePage(String name, Source theGivenSource, ArrayList<Edge> allTheEdges,
            ArrayList<Distribution> allTheDistributions, ArrayList<Evidence> allTheEvidence,
            ArrayList<Fragment> allTheFragments, ArrayList<Source> allTheSources,ArrayList<Node> allTheNodes, String directoryName){
        super(name,allTheEdges,allTheDistributions,allTheEvidence,allTheFragments,allTheSources,allTheNodes,directoryName);
        
        allTheRelations = allTheEdges;
        this.allTheDistributions = allTheDistributions;
        this.allTheEvidence = allTheEvidence;
        this.allTheFragments = allTheFragments;
        allTheVariables = allTheNodes;
        this.allTheSources = allTheSources;
        theSource = theGivenSource;
        theDirectoryName = directoryName;
        
        tempFile = new File(theDirectoryName+"/source/"+this.getPageName());
        createBlankFile(this.getPageName());
        try {
            addHTMLMenu("source",theSource,theDirectoryName,this.getPageName());
            completeSourceDocument();
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
    
    private void completeSourceDocument() throws IOException{
        String tempText="";
        if(this.theSource.getType().equals("Data")){
         tempText += "<table><tr><td class=\"subHeading\">Dataset:</td><td class=\"info\">\n" 
                + this.theSource.getName() + "</td></tr>\n";
         tempText += "<tr><td class=\"subHeading\">Description:</td><td class=\"info\">\n" 
                + this.theSource.getDataInfo() + "</td></tr>\n";
        }
        if(this.theSource.getType().equals("Publication")){
            if(!this.theSource.getPMID().equals("None")){
                tempText ="";
                tempText += "<iframe scrolling=\"yes\" width=\"675\" height=\"550\" src=\"http://www.ncbi.nlm.nih.gov/pubmed/"+ this.theSource.getPMID() +"\"></iframe>\n";
                tempText += "</table>\n<div id=\"footer\">&copy; "+getYear()+" <a href=\"http://www.eecs.qmul.ac.uk/research/view/risk-and-information-management\"> Risk and Information Management (RIM) Research Group, Queen Mary, University of London</a></div></body></html>\n";
            }
            else if(!this.theSource.getISBN().equals("None")){
                tempText ="";
                tempText += "<iframe scrolling=\"yes\" width=\"675\" height=\"550\" src=\"http://www.amazon.co.uk/s/ref=nb_sb_noss?url=search-alias%3Dstripbooks&field-keywords="+ this.theSource.getISBN() +"\"></iframe>\n";
                tempText += "</table>\n<div id=\"footer\">&copy; "+getYear()+" <a href=\"http://www.eecs.qmul.ac.uk/research/view/risk-and-information-management\"> Risk and Information Management (RIM) Research Group, Queen Mary, University of London</a></div></body></html>\n";
            
            }
            else if(!this.theSource.getURL().equals("None")){
                tempText ="";
                tempText += "<iframe scrolling=\"yes\" width=\"675\" height=\"550\" src=\""+ this.theSource.getURL() +"\"></iframe>\n";
                tempText += "</table>\n<div id=\"footer\">&copy; "+getYear()+" <a href=\"http://www.eecs.qmul.ac.uk/research/view/risk-and-information-management\"> Risk and Information Management (RIM) Research Group, Queen Mary, University of London</a></div></body></html>\n";
            
            }
            else {
                tempText ="";
                tempText += "<iframe scrolling=\"yes\" width=\"600\" height=\"1000\" src=\"http://www.ncbi.nlm.nih.gov/pubmed/\"></iframe>\n";
                tempText += "</table>\n<div id=\"footer\">&copy; "+getYear()+" <a href=\"http://www.eecs.qmul.ac.uk/research/view/risk-and-information-management\"> Risk and Information Management (RIM) Research Group, Queen Mary, University of London</a></div></body></html>\n";
            }
        }
        else{
            if(this.theSource.getType().equals("Expert")){
                tempText = "<table><tr><td class=\"subHeading\">Domain Expert:</td><td class=\"info\">\n" 
                    + this.theSource.getExCredentials() + "</td></tr>\n";
            }
            tempText+="</table></div>\n<div id=\"footer\">&copy; "+getYear()+" <a href=\"http://www.eecs.qmul.ac.uk/research/view/risk-and-information-management\"> Risk and Information Management (RIM) Research Group, Queen Mary, University of London</a></div>\n</body></html>\n";
        }
        //tempText +="</body></html>";
        appendCurrentDocument(tempText, "source");
    }
}