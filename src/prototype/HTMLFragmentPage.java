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
public class HTMLFragmentPage extends HTMLPage{    
    
    private final ArrayList<Node> allTheVariables;
    private final ArrayList<Edge> allTheRelations;
    private final ArrayList<Distribution> allTheDistributions;
    private final ArrayList<Evidence> allTheEvidence;
    private final ArrayList<Fragment> allTheFragments;
    private final ArrayList<Source> allTheSources;
    private final File tempFile;
    private final Fragment theFragment;
    private final String theDirectoryName;
    
    public HTMLFragmentPage(String name, Fragment theGivenFragment, ArrayList<Edge> allTheEdges,
            ArrayList<Distribution> allTheDistributions, ArrayList<Evidence> allTheEvidence,
            ArrayList<Fragment> allTheFragments, ArrayList<Source> allTheSources,ArrayList<Node> allTheNodes, String directoryName){
        super(name,allTheEdges,allTheDistributions,allTheEvidence,allTheFragments,allTheSources,allTheNodes,directoryName);
        
        allTheRelations = allTheEdges;
        this.allTheDistributions = allTheDistributions;
        this.allTheEvidence = allTheEvidence;
        this.allTheFragments = allTheFragments;
        allTheVariables = allTheNodes;
        this.allTheSources = allTheSources;
        theFragment = theGivenFragment;
        theDirectoryName = directoryName;
        
        tempFile = new File(directoryName+"/fragment/"+this.getPageName());
        createBlankFile(this.getPageName());
        try {
            addHTMLMenu("fragment",null,theDirectoryName,this.getPageName());
            addBNImage("fragment");
            completeFragmentDocument();
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
    
    private void completeFragmentDocument() throws IOException{
        
        String tempText = "<tr><td class=\"subHeading\" colspan=\"1\">Fragment:</td><td class=\"info\" colspan=\"3\">" 
                + this.theFragment.getName() + "</td></tr>\n";
        //add the summary of the fragment
        tempText += "<tr><td class=\"subHeading\">Summary:</td><td class=\"info\" colspan=\"3\"><p>";
        tempText += this.theFragment.getSummary() + "</p></td></tr>\n";
        
        //add the node members of the given fragment
        tempText += "<tr><td class=\"subHeading\">Member Variables:</td><td class=\"info\" colspan=\"1.5\">\n";
        
        int cycleNumber = 0;
        int counter=0;
        for(Node n:this.theFragment.getMembersList()){
                
                
                    if((2*counter)>=(this.theFragment.getMembersList().size())){
                        tempText+="</td><td class=\"info\"  colspan=\"1.5\">\n";
                        counter=-2;
                    }
                tempText+="\t<li><a href=\"../variable/"+ n.getName() +".html\">" + n.getName() + "</a></li>\n";    
                counter++;      

                
        }        
        tempText+= "</td></tr>\n";
        if(this.theFragment.getFragMembersList().size()>0){
        tempText += "<tr><td class=\"subHeading\">Member Fragments:</td><td class=\"info\" colspan=\"1.5\">\n";
        counter = 0;
                for(Fragment n:this.theFragment.getFragMembersList()){               
                    if((2*counter)>=this.theFragment.getFragMembersList().size()){
                        tempText+="</td><td class=\"info\"  colspan=\"1.5\">\n";
                        counter=-2;
                    }
                tempText+="\t<li><a href=\"../fragment/"+ n.getName() +".html\">" + n.getName() + "</a></li>\n";
                    if(this.theFragment.getFragMembersList().size()==1){
                        tempText += "</td><td class=\"info\"  colspan=\"1.5\">\n";
                        
                    }
                counter++;      
                }
                tempText+= "</td></tr>\n";
        }
                
             
        
        
        
        tempText += getEvidenceSection(this.theFragment);
        for(Fragment n:this.theFragment.getFragMembersList()){
            tempText += getEvidenceSectionFragment(n);
        }
        tempText +="</table></div><div id=\"footer\">&copy; "+getYear()+" <a href=\"http://www.eecs.qmul.ac.uk/research/view/risk-and-information-management\"> Risk and Information Management (RIM) Research Group, Queen Mary, University of London</a></div></body></html>";
        appendCurrentDocument(tempText, "fragment");
    }
}