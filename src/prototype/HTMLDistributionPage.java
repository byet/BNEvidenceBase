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
public class HTMLDistributionPage extends HTMLPage{    
    
    private final ArrayList<Node> allTheVariables;
    private final ArrayList<Edge> allTheRelations;
    private final ArrayList<Distribution> allTheDistributions;
    private final ArrayList<Evidence> allTheEvidence;
    private final ArrayList<Fragment> allTheFragments;
    private final ArrayList<Source> allTheSources;
    private final File tempFile;
    private final Distribution theDistribution;
    private final String theDirectoryName;
    
    public HTMLDistributionPage(String name, Distribution theGivenDistribution, ArrayList<Edge> allTheEdges,
            ArrayList<Distribution> allTheDistributions, ArrayList<Evidence> allTheEvidence,
            ArrayList<Fragment> allTheFragments, ArrayList<Source> allTheSources,ArrayList<Node> allTheNodes, String directoryName){
        super(name,allTheEdges,allTheDistributions,allTheEvidence,allTheFragments,allTheSources,allTheNodes, directoryName);
        
        allTheRelations = allTheEdges;
        this.allTheDistributions = allTheDistributions;
        this.allTheEvidence = allTheEvidence;
        this.allTheFragments = allTheFragments;
        allTheVariables = allTheNodes;
        this.allTheSources = allTheSources;
        theDistribution = theGivenDistribution;
        theDirectoryName = directoryName;
        
        tempFile = new File(theDirectoryName+"/distribution/"+this.getPageName());
        createBlankFile(this.getPageName());
        try {
            addHTMLMenu("distribution",null,theDirectoryName,this.getPageName());
            completeDistributionDocument();
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
    
    private void completeDistributionDocument() throws IOException{
        
        String tempText = "<tr><td class=\"subHeading\">Distribution Name:</td>\n<td class=\"info\">" 
                + this.theDistribution.getName() + "</td></tr>\n";
        
        tempText = "<tr><td class=\"subHeading\">Distribution Type:</td>\n<td class=\"info\">" 
                + this.theDistribution.getType() + "</td></tr>\n";
        //tempText +="</body></html>";
        tempText+="</table></div>\n<div id=\"footer\"><p>&copy; 2015 <a href=\"http://www.eecs.qmul.ac.uk/research/view/risk-and-information-management\"> Risk and Information Management (RIM) Research Group, Queen Mary, University of London</a></p></div>\n</body></html>";
        appendCurrentDocument(tempText, "distribution");
    }
}