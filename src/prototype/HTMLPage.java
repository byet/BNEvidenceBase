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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Ben Spencer
 * @author Barbaros Yet
 */
public class HTMLPage {
    
    private String pageName;
    private String pageTitle;
    private String pageSubTitle;
    private final ArrayList<Node> allTheVariables;
    private final ArrayList<Edge> allTheRelations;
    private final ArrayList<Distribution> allTheDistributions;
    private final ArrayList<Evidence> allTheEvidence;
    private final ArrayList<Fragment> allTheFragments;
    private final ArrayList<Source> allTheSources;
    private File currentFile;
    private String theDirectoryName;
    private File tempFile;
    
    
    public HTMLPage(String name, ArrayList<Edge> allTheEdges,
            ArrayList<Distribution> allTheDistributions, ArrayList<Evidence> allTheEvidence,
            ArrayList<Fragment> allTheFragments, ArrayList<Source> allTheSources,ArrayList<Node> allTheNodes, String directoryName){
        
        pageName = name+".html";
        pageTitle = name;
        allTheRelations = allTheEdges;
        this.allTheDistributions = allTheDistributions;
        this.allTheEvidence = allTheEvidence;
        this.allTheFragments = allTheFragments;
        allTheVariables = allTheNodes;
        this.allTheSources = allTheSources;
        theDirectoryName = directoryName;
        
        if(pageTitle.equals("index")){
            //create the index page
            tempFile = new File(theDirectoryName+"/"+this.getPageName());
            createBlankFile(this.getPageName());
            try {
                addHTMLMenu("index",null,theDirectoryName,this.getPageName());
                completeIndexDocument();
            } catch (IOException ex) {
                System.out.println("Error: " + ex);
            }
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
    
    public void addBNImage(String currentType) throws IOException{
        String tempText = "</map>\n<table>\n";
        appendCurrentDocument(tempText,currentType);
    }
    
    private void completeIndexDocument() throws IOException{
        String tempText = "";
        tempText+="<div id=\"maindesc\"><p>This website presents the evidence-base of the "+EvidenceBaseOWL.getbNetworkTitle()+". Evidence supporting the relations and variables included in the BN structure can be browsed.</p></div>";
        
        //this is specifically for adding the picture of the ATC BN
        if(this.theDirectoryName.equals("ATC_EBase")){
            tempText+= "<div id=\"bnimage\"><img src=\"model/model.jpg\" width=\"545\" height=\"274\" alt=\"BN\" usemap=\"#modelMap\">\n";
            tempText+= "<map name=\"modelMap\">\n";
            tempText+= "\t<area shape=\"rect\" coords=\"0,0,271,138\" alt=\"ShockArea\" href=\"./fragment/Shock.html\">\n";
            tempText+= "\t<area shape=\"rect\" coords=\"271,0,541,138\" alt=\"CoagArea\" href=\"./fragment/Coagulopathy.html\">\n";
            tempText+= "\t<area shape=\"rect\" coords=\"0,138,361,271\" alt=\"InjArea\" href=\"./fragment/Injury.html\">\n";
            tempText+= "\t<area shape=\"rect\" coords=\"362,138,541,281\" alt=\"DeathArea\" href=\"./fragment/Mortality.html\">\n";
            tempText+= "</map></div>\n";
        }

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        tempText+="<div id=\"update\"><p>Last updated: " + dateFormat.format(date) + "</p></div>";
        tempText+="</div>\n<div id=\"footer\">&copy; "+getYear()+" <a href=\"http://www.eecs.qmul.ac.uk/research/view/risk-and-information-management\"> Risk and Information Management (RIM) Research Group, Queen Mary, University of London</a></div></body></html>";
        appendCurrentDocument(tempText,"index");
    }
    
    public String getPageName(){
        return pageName;
    }
    
    public String getPageTitle(){
        return pageTitle;
    }
    
     public String getPageSubTitle(){
        return pageSubTitle;
    }
    
    public String getStartOfDocument(String evidenceBaseName,String givenPageName, String elementType){
        String temptext = "<!DOCTYPE html><html><head>\n\t<link rel=\"stylesheet\" type=\"text/css\" href=\"../style/eBstyle.css\">\n"
        + "\t<script type=\"text/javascript\" src=\"../script/jquery-1.9.0.min.js\"></script>\n"
        + "\t<script type=\"text/javascript\" src=\"../script/BNscript.js\"></script>\n"
        + "\t<script type=\"text/javascript\" src=\"../script/ga.js\"></script>\n"
        + "\t<title>Evidence Base for "+getPageMainTitle()+"</title>\n</head>\n<body>\n<div id=\"container\"><div id=\"header\">\n\t"
        + "<table>\n" +"<tbody><tr>\n" +"<td>\n" +"<h1>"+getPageMainTitle()+"</h1>\n" + "<h2>"+getPageMainSubTitle()+"</h2>\n" +
        "</td>\n" + "<td>\n" +"<a href=\"http://www.eecs.qmul.ac.uk/research/view/risk-and-information-management\"><img src=\"../model/qmlogo.gif\" class=\"right\" align=\"middle\" height=\"50\"></a>\n" ;
        if(this.theDirectoryName.equals("ATC_EBase")){
            temptext += "</td>\n" +"<td>\n" +"<a href=\"http://www.smd.qmul.ac.uk/research/neuro/traumascience\"><img src=\"../model/trauma.jpg\" class=\"right\" align=\"middle\" height=\"80\"></a>\n" ;   
            }
        temptext +=  "</td>\n" +"</tr>\n" +"</tbody></table>"
            + "</div>\t"         
            + addWebsiteMenu("a")
            + addLocator(givenPageName, elementType);
       
        return temptext;
        
        
    }
    
    public String getStartOfDocument(String evidenceBaseName,String givenPageName, Boolean b){
        //FOR THE INDEX PAGE
        String temptext = "<!DOCTYPE html><html><head>\n\t<link rel=\"stylesheet\" type=\"text/css\" href=\"style/eBstyle.css\">\n"
        + "\t<script type=\"text/javascript\" src=\"script/jquery-1.9.0.min.js\"></script>\n"
        + "\t<script type=\"text/javascript\" src=\"script/BNscript.js\"></script>\n"
        + "\t<script type=\"text/javascript\" src=\"script/ga.js\"></script>\n"                
        + "\t<title>Evidence Base for "+getPageMainTitle()+"</title>\n</head><body><div id=\"container\"><div id=\"header\">\n\t"
        + "<table>\n" +"<tbody><tr>\n" +"<td>\n" +"<h1>"+getPageMainTitle()+"</h1>\n" + "<h2>"+getPageMainSubTitle()+"</h2>\n" +
        "</td>\n" + "<td>\n" +"<a href=\"http://www.eecs.qmul.ac.uk/research/view/risk-and-information-management\"><img src=\"./model/qmlogo.gif\" class=\"right\" align=\"middle\" height=\"50\"></a>\n";        
        if(this.theDirectoryName.equals("ATC_EBase")){
            temptext += "</td>\n" +"<td>\n" +"<a href=\"http://www.smd.qmul.ac.uk/research/neuro/traumascience\"><img src=\"./model/trauma.jpg\" class=\"right\" align=\"middle\" height=\"80\"></a>\n";
        }
        temptext +=  "</td>\n" +"</tr>\n" +"</tbody></table>"
        + "</div>\t"        
        + addWebsiteMenu("Main")      
        + addLocator(givenPageName, "Main");
        
        return temptext;
    
    }
    
    public String getEvidenceSection(ProtegeIndividual pI){
        /*
        Check evidence: how many exclusions, how many others
        For exclusions put a section called: Excluded BN Elements
        For others put a section called: Evidence
        
        
        
        */
        ArrayList<Evidence> excludedElements = new ArrayList();
        ArrayList<Evidence> otherEvidence = new ArrayList();
        
        
        for(Evidence e:pI.getEvidence(allTheEvidence)){
            if (e.getType().equals("Excluded Relation")||e.getType().equals("Excluded Parent")||e.getType().equals("Excluded Child")){
                excludedElements.add(e);}
            else {
                otherEvidence.add(e);
            }
        }
        
        String evidenceSection="";
        
            if(otherEvidence.size()>0){
                
            evidenceSection+="<tr><td class=\"subHeading\">Evidence:</td><td class=\"info\" colspan=\"2\"><div>\n";
            
 
            for(Evidence e:otherEvidence){
               
                int dummy = 0;
                if(e.getType().equals("Conflicting Evidence")){
                    dummy = 1;
                    evidenceSection += "\t<div><a class=\"evi\">" + e.getName() + "(Conflicting Evidence)" + "</a><br>\n";
                }
            
 
                if(e.getType().equals("Supporting Evidence")){
                    dummy = 1;
                    evidenceSection += "\t<div><a class=\"evi\">" + e.getName() + "(Supporting Evidence)" + "</a><br>\n";
                }
                    if(dummy!=0){
                    evidenceSection += "<p class=\"eviblock\">" + e.getDescription() + "\n";
                    evidenceSection+="<br><a class=\"evref\">[References]</a></p> "+"\n<ul class=\"evrefblock\">";
                    for(Source s:e.getSources(allTheSources)){
                        evidenceSection+="\t<li><a href=\"../source/"+ s.getName() +".html\">" + s.getName() + "</a></li>\n";
            }
                    evidenceSection += "</ul></div>\n";
                }}

            evidenceSection+="</div>\n</td>\n</tr>\n";}
          
                        // Excluded Eleements
            
            if(excludedElements.size()>0){
                
            evidenceSection+="<tr><td class=\"subHeading\">Excluded BN Elements:</td><td class=\"info\" colspan=\"2\"><div>\n";            
            
            
            for(Evidence e:excludedElements){
                int dummy=0;
   
                if(e.getType().equals("Excluded Child")){
                    evidenceSection += "\t<div><a class=\"exc\">" + this.getPageName().substring(0, this.getPageName().length()-5) +   " <big>&nrarr;</big> " + e.getName()+  "</a><br>\n";
                    dummy=1;
                }

          
                
                if(e.getType().equals("Excluded Parent")){
                    evidenceSection += "\t<div><a class=\"exc\">" + e.getName()+ " <big>&nrarr;</big> " + this.getPageName().substring(0, this.getPageName().length()-5) + "</a><br>\n";
                    dummy=1;
                }



                if(e.getType().equals("Excluded Relation")){
                    evidenceSection += "\t<div><a class=\"exc\">" + e.getName() + " <big>&nharr;</big> " + this.getPageName().substring(0, this.getPageName().length()-5) + "</a><br>\n";
                    dummy=1;
                }
                if(dummy!=0){
                    evidenceSection += "<p class =\"excblock\">" + e.getDescription() + "\n";
                    evidenceSection+="<br><a class=\"exref\">[References]</a></p> "+"\n<ul class=\"exrefblock\">";
                    for(Source s:e.getSources(allTheSources)){
                        evidenceSection+="\t<li><a href=\"../source/"+ s.getName() +".html\">" + s.getName() + "</a></li>\n";
            }
                    evidenceSection += "</ul></div>\n";
                }
            }


            evidenceSection+="</div></td>\n</tr>\n";}
            
            
            
            
        return evidenceSection;
    }
    
    
    public void appendCurrentDocument(String textToAdd, String type) throws IOException{
        currentFile = new File(pageName);
        FileWriter fileWritter;
        BufferedWriter bufferWritter;
        if(!type.equals("index")){
            fileWritter = new FileWriter(theDirectoryName+"/"+type+"/"+pageName,true);
            bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(textToAdd);
            bufferWritter.close();
        }
        else{
            fileWritter = new FileWriter(theDirectoryName+"/"+pageName,true);
            bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(textToAdd);
            bufferWritter.close();
        }
    }
    
    public void addHTMLMenu(String currentType,Source tempSource, String evidenceBaseName ,String pageHeader) throws IOException{
        String tempHTML = "";
        
        if(currentType.equals("index")){
            appendCurrentDocument(getStartOfDocument(evidenceBaseName,pageHeader,true),currentType);
            ArrayList<String> namelist = new ArrayList() ;
            ArrayList<String> edgedesc = new ArrayList() ;
            HashMap<String, String> edgenames = new HashMap();
            tempHTML += "<div id=\"subcontent\"><a class=\"menu7\" href=\"index.html\" >\n\t<h2>Main Page</h2>\n</a>";
            tempHTML += "<a class=\"menu1\">\n\t<h2>Variables</h2>\n</a>\n<ul class=\"menublock\" id=\"menublock1\">";
            for(Node n: allTheVariables){
                namelist.add(n.getName());            
            }
            Collections.sort(namelist); //Alphabetical order
            for (String n:namelist){
            tempHTML += "\t<li><a href=\"" + "../" + this.theDirectoryName + "/variable/" + n + ".html" + "\">" + n + "</a></li>\n";
            }
            namelist.clear();
            
            tempHTML += "</ul><a class=\"menu6\"><h2>Relations</h2></a><ul class=\"menublock\" id=\"menublock6\">\n";
            
            for(Edge ed: allTheRelations){
                String tempText =  ed.getComesFrom().getName();
                tempText +=  " <big>&rarr;</big> ";
                tempText +=  ed.getPointsTo().getName();
                edgedesc.add(tempText);
                edgenames.put(tempText,ed.getName());
            }
            Collections.sort(edgedesc); //Alphabetical order
            
            for (int i=0; i<edgedesc.size(); i++){
                String ed = edgenames.get(edgedesc.get(i));
                tempHTML += "\t<li><a href=\"" + "../" + this.theDirectoryName + "/relation/" + ed + ".html" + "\">" + edgedesc.get(i) + "</a></li>\n";
            }

     
            edgedesc.clear();
            edgenames.clear();
            tempHTML += "</ul><a class=\"menu2\"><h2>Fragments</h2></a><ul class=\"menublock\" id=\"menublock2\">\n";

            for(Fragment frag: allTheFragments){
                namelist.add(frag.getName()); 
                
            }
            Collections.sort(namelist);
            
            for (String frag:namelist){
                tempHTML += "\t<li><a href=\"" + "../" + this.theDirectoryName + "/fragment/" + frag+ ".html" + "\">" + frag + "</a></li>\n";
            }

            tempHTML += "</ul><a class=\"menu3\"><h2>Data</h2></a><ul class=\"menublock\" id=\"menublock3\">\n";
            namelist.clear();
            for(Source s: allTheSources){
                if(s.getType().equals("Data")){
                    namelist.add(s.getName());
                }
            }
            Collections.sort(namelist);
            for (String s:namelist){
                       
                    tempHTML += "\t<li><a href=\"" + "../" + this.theDirectoryName + "/source/" + s+ ".html" + "\">" + s + "</a></li>\n";
                }
            tempHTML += "</ul><a class=\"menu4\"><h2>Experts</h2></a><ul class=\"menublock\" id=\"menublock4\">\n";
            
            namelist.clear();
            
            for(Source s: allTheSources){
                if(s.getType().equals("Expert")){
                    namelist.add(s.getName());
                }
            }
            Collections.sort(namelist);
            for (String s:namelist){
                    tempHTML += "\t<li><a href=\"" + "../" + this.theDirectoryName + "/source/" + s+ ".html" + "\">" + s + "</a></li>\n";
            }

            tempHTML += "</ul><a class=\"menu5\"><h2>Publications</h2></a><ul class=\"menublock\" id=\"menublock5\">\n";
            namelist.clear();
            
            
            for(Source s: allTheSources){
                if(s.getType().equals("Publication")){
                    namelist.add(s.getName());
                    
                }
            }
            Collections.sort(namelist);
            for (String s:namelist){   
                    tempHTML += "\t<li><a href=\"" + "../" + this.theDirectoryName + "/source/" + s+ ".html" + "\">" + s+ "</a></li>\n";
            }
            
            namelist.clear();

            tempHTML += "</ul></div>\n";

            if(currentType.equals("source") &&tempSource.getType().equals("Publication")){
                //its a publication
            }
            else{
                tempHTML += "<div id=\"content\">\n";
            }
        }
        else{
            appendCurrentDocument(getStartOfDocument(evidenceBaseName,pageHeader,currentType),currentType);
            ArrayList<String> namelist = new ArrayList() ;
            ArrayList<String> edgedesc = new ArrayList() ;
            HashMap<String, String> edgenames = new HashMap();
            
            tempHTML += "<div id=\"subcontent\"><a class=\"menu7\" href=\"../index.html\" >\n\t<h2>Main Page</h2></a>\n";
            tempHTML += "<a class=\"menu1\">\n\t<h2>Variables</h2>\n</a>\n<ul class=\"menublock\" id=\"menublock1\">";
            for(Node n: allTheVariables){
                namelist.add(n.getName());            
            }
            Collections.sort(namelist); //Alphabetical order
            for(String n: namelist){
                tempHTML += "\t<li><a href=\"../variable/" + n+ ".html" + "\">" + n+ "</a></li>\n";
            }
            namelist.clear();
                      tempHTML += "</ul><a class=\"menu6\"><h2>Relations</h2></a><ul class=\"menublock\" id=\"menublock6\">\n";
            
            for(Edge ed: allTheRelations){         
                String tempText =  ed.getComesFrom().getName();
                tempText +=  " <big>&rarr;</big> ";
                tempText +=  ed.getPointsTo().getName();
                edgedesc.add(tempText);
                edgenames.put(tempText,ed.getName());
            }
            Collections.sort(edgedesc); //Alphabetical order
            
            for (int i=0; i<edgedesc.size(); i++){
                String ed = edgenames.get(edgedesc.get(i));  
                tempHTML += "\t<li><a href=\"" + "../relation/" + ed + ".html" + "\">" + edgedesc.get(i) + "</a></li>\n";
            }

     
            edgedesc.clear();
            edgenames.clear();

            tempHTML += "</ul><a class=\"menu2\"><h2>Fragments</h2></a><ul class=\"menublock\" id=\"menublock2\">\n";
             
            for(Fragment frag: allTheFragments){
                namelist.add(frag.getName());            
            }
            Collections.sort(namelist); //Alphabetical order
                
            for(String frag: namelist){
                tempHTML += "\t<li><a href=\"../fragment/" + frag+ ".html" + "\">" + frag + "</a></li>\n";
            }

            tempHTML += "</ul><a class=\"menu3\"><h2>Data</h2></a><ul class=\"menublock\" id=\"menublock3\">\n";
            namelist.clear();
            for(Source s: allTheSources){
                if(s.getType().equals("Data")){
                    namelist.add(s.getName());
                }
            }
            Collections.sort(namelist);
            for (String s: namelist){       
                    tempHTML += "\t<li><a href=\"../source/" + s+ ".html" + "\">" + s + "</a></li>\n";
                }           
            namelist.clear();
            tempHTML += "</ul><a class=\"menu4\"><h2>Experts</h2></a><ul class=\"menublock\" id=\"menublock4\">\n";

            for(Source s: allTheSources){
                if(s.getType().equals("Expert")){
                    namelist.add(s.getName());
                }
            }
            Collections.sort(namelist);
            for(String s: namelist){
                    tempHTML += "\t<li><a href=\"../source/" + s+ ".html" + "\">" + s + "</a></li>\n";
                }

            tempHTML += "</ul><a class=\"menu5\"><h2>Publications</h2></a><ul class=\"menublock\" id=\"menublock5\">\n";
            namelist.clear();
            for(Source s: allTheSources){
                if(s.getType().equals("Publication")){
                    namelist.add(s.getName());
                }
            }
            Collections.sort(namelist);
            for (String s: namelist){
                    tempHTML += "\t<li><a href=\"../source/" + s+ ".html" + "\">" + s + "</a></li>\n";
                }
            namelist.clear();
            tempHTML += "</ul></div>\n";

            if(currentType.equals("source") &&tempSource.getType().equals("Publication")){
                //its a publication
            }
            else{
                tempHTML += "<div id=\"content\">\n";
            }
        }
        appendCurrentDocument(tempHTML,currentType);
    }
    
    public String addLocator(String name, String type){
        String tempHTML;
        tempHTML = "\n<div id=\"locator\">";
        if(type.equals("Main")){ 
            tempHTML += "Evidence Browser Main Page"; 
        }
        else{
            tempHTML += /* "<a href=\"../index.html\">"+*/"Evidence Browser: "+ firstCapitalLetter(type) +/*"</a>*/": ";
            if(type.equals("relation")){
                tempHTML +=/* "<a href=\"../"+ type+"/" + name +"\">" +*/ getLongEdgeName(name) /*+ "</a>"*/;
            }
            else{
                tempHTML += /*"<a href=\"../"+type+"/" + name + "\">" +*/ removeDotHTML(name) /*+ "</a>"*/;
        
            }
        }
        tempHTML += "</div>\n";
        return tempHTML;
    }
    
    public String removeDotHTML(String str){
        String temp = str.substring(0,str.lastIndexOf("."));
        return temp;
    }
    
    public String firstCapitalLetter (String str){
        String temp = Character.toString(str.charAt(0)).toUpperCase()+str.substring(1);
        return temp;
    }
    
    public String getLongEdgeName (Edge ed){
        String name =  ed.getComesFrom().getName();
        name +=  " <big>&rarr;</big> ";
        name +=  ed.getPointsTo().getName();
        return name;
    }
    // This only used for the main website of ATC BN (temporary)
    public String addWebsiteMenu(String currentType){
        String tempHTML ="";
        if(this.theDirectoryName.equals("ATC_EBase")){
            if(currentType.equals("Main")){
            tempHTML = "<div id=\"navigation\">\n";
            tempHTML += "<ul>\n<li><a href=\"../index.html\">Home</a></li>\n";
            tempHTML += "<li><a href=\"../atcbn.html\">ATC BN</a></li>\n";
            tempHTML += "<li><a href=\"index.html\">Evidence Browser</a></li>\n";
            tempHTML += "<li><a href=\"../docs.html\">Publications</a></li>\n";
            tempHTML += "<li><a href=\"../contact.html\">Contact us</a></li>\n";
            tempHTML += "</ul>\n</div>\n";}
            else{
            tempHTML = "<div id=\"navigation\">\n";
            tempHTML += "<ul>\n<li><a href=\"../../index.html\">Home</a></li>\n";
            tempHTML += "<li><a href=\"../../atcbn.html\">ATC BN</a></li>\n";
            tempHTML += "<li><a href=\"../index.html\">Evidence Browser</a></li>\n";
            tempHTML += "<li><a href=\"../../docs.html\">Publications</a></li>\n";
            tempHTML += "<li><a href=\"../../contact.html\">Contact us</a></li>\n";
            tempHTML += "</ul>\n</div>\n";}
            }
        return tempHTML;   
    }
            
    public String getLongEdgeName (String edgeName){
        edgeName = removeDotHTML(edgeName);
        String name = ""; 
        for(Edge ed: allTheRelations){
            if(ed.getName().equals(edgeName)){
                name = ed.getComesFrom().getName();
                name +=  " <big>&rarr;</big> ";
                name +=  ed.getPointsTo().getName();
            } 
       }
        return name;
    }
    
    public String getPageMainTitle(){
        String theTitle = EvidenceBaseOWL.getbNetworkTitle();       
        return theTitle;
    }
    
       public String getPageMainSubTitle(){
        String theTitle = "Evidence Browser for the "+getPageMainTitle();

        return theTitle;
    }
       
       public int getYear(){
           return Calendar.getInstance().get(Calendar.YEAR);
       }
       
        public String getEvidenceSectionFragment(ProtegeIndividual pI){
        String evidenceSection="";
            if(pI.getEvidence(allTheEvidence).size()>0){
            evidenceSection+="<tr><td class=\"subHeading\">Evidence ("+pI.getName()+")</td><td class=\"info\" colspan=\"2\">\n";
            
            int i=0;
            for(Evidence e:pI.getEvidence(allTheEvidence)){
   
                if(e.getType().equals("Conflicting Evidence")){
                    i++;
                    evidenceSection += "\t<li><a href=\"../evidence/" + e.getName() + ".html\">" +"Conflicting Evidence-" +  String.valueOf(i) + "</a></li>\n";
                }
            }

           
            i=0;
            for(Evidence e:pI.getEvidence(allTheEvidence)){
                if(e.getType().equals("Excluded Child")){
                    i++;
                    evidenceSection += "\t<li><a href=\"../evidence/" + e.getName() + ".html\">" + "Excluded Child-" +  String.valueOf(i) + "</a></li>\n";
                }
            }

          
            i=0;
            for(Evidence e:pI.getEvidence(allTheEvidence)){
                
                if(e.getType().equals("Excluded Parent")){
                    i++;
                    evidenceSection += "\t<li><a href=\"../evidence/" +  e.getName() + ".html\">" + "Excluded Parent-"  +  Integer.toString(i) + "</a></li>\n";
                }
            }

    
            i=0;
            for(Evidence e:pI.getEvidence(allTheEvidence)){

                if(e.getType().equals("Excluded Relation")){
                    i++;
                    evidenceSection += "\t<li><a href=\"../evidence/"  + e.getName() + ".html\">" + "Excluded Relation-" +  String.valueOf(i) + "</a></li>\n";
                }
            }

   
            i=0;
            for(Evidence e:pI.getEvidence(allTheEvidence)){
                if(e.getType().equals("Supporting Evidence")){
                    i++;
                    evidenceSection += "\t<li><a href=\"../evidence/" + e.getName() + ".html\">" + "Supporting Evidence-"  + String.valueOf(i) + "</a></li>\n";
                }
            }

            evidenceSection+="</ul>\n</ul>\n</td>\n</tr>\n";}
            
        return evidenceSection;
    }
       
       

}
