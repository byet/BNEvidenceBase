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

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

/**
 *
 * @author Ben Spencer
 * @author Barbaros Yet
 */
public class EvidenceBaseOWL {
    
    public static EvidenceBaseOWL eBaseOWL;
    
    private String oWLName;
    private File oWLFile;
    private static String bNetworkTitle;
    private String directoryName;

    public static OWLOntologyManager manager;
    public static OWLOntology currentOntology;
    public static OWLDataFactory factory;
    public static String OntologyIRI;
    public static Map<OWLDataPropertyExpression, Set<OWLLiteral>> individualDataProperties;
    public static Map<OWLObjectPropertyExpression, Set<OWLIndividual>> individualObjectProperties;
    public static ArrayList<Node> allNodes = new ArrayList<Node>();
    public static ArrayList<Edge> allEdges = new ArrayList<Edge>();
    public static ArrayList<Fragment> allFragments = new ArrayList<Fragment>();
    public static ArrayList<Distribution> allDistributions = new ArrayList<Distribution>();
    public static ArrayList<Evidence> allEvidence = new ArrayList<Evidence>();
    public static ArrayList<Source> allSources = new ArrayList<Source>();
    
    
    public EvidenceBaseOWL(String name, String title, String folderName){
        oWLName = /*"resources/" +*/ name;
        bNetworkTitle = title;
        directoryName = folderName;
    }
    
    public static void main(String[] args) throws OWLOntologyCreationException, IOException {

        if(args.length!=3){
           eBaseOWL = new EvidenceBaseOWL("EvidenceBase_AsiaBN.owl", "Asia Bayesian Network","AsiaEBase");
        }else {
            eBaseOWL = new EvidenceBaseOWL(args[0],args[1],args[2]);
        }
        eBaseOWL.setupOWL();
        eBaseOWL.setupPartTwo();
        eBaseOWL.setupFileStructure();
        eBaseOWL.generateHTML();
        eBaseOWL.openIndex();
        //at this point we now have access to all relevant parts of the ontology
        
    }
    
    public void setupOWL() throws OWLOntologyCreationException{
        //Fill lists with correct individuals create new objects for each individual (indy)
        
        oWLFile = new File(oWLName);
        
        manager = OWLManager.createOWLOntologyManager(); 
        currentOntology = manager.loadOntologyFromOntologyDocument(oWLFile);
        
        Set<OWLNamedIndividual> indivs = currentOntology.getIndividualsInSignature();
        
        for(OWLNamedIndividual indy : indivs){
            individualDataProperties = indy.getDataPropertyValues(currentOntology);
            individualObjectProperties = indy.getObjectPropertyValues(currentOntology);
            
            if(indy.getTypes(currentOntology).toString().contains("Node")){                
                allNodes.add(new Node(indy,currentOntology,individualDataProperties,individualObjectProperties));
            }
            else if(indy.getTypes(currentOntology).toString().contains("Edge")){
                allEdges.add(new Edge(indy,currentOntology,individualDataProperties,individualObjectProperties));
            }
            else if(indy.getTypes(currentOntology).toString().contains("Fragment")||
                    indy.getTypes(currentOntology).toString().contains("Diagnosis")||
                    indy.getTypes(currentOntology).toString().contains("MeasurementIdiom")){
                allFragments.add(new Fragment(indy,currentOntology,individualDataProperties,individualObjectProperties));
            }
            else if(indy.getTypes(currentOntology).toString().contains("Distribution")){
                allDistributions.add(new Distribution(indy,currentOntology,individualDataProperties,individualObjectProperties));
            }
            else if(indy.getTypes(currentOntology).toString().contains("Evidence")||
                    indy.getTypes(currentOntology).toString().contains("Excluded")){
                allEvidence.add(new Evidence(indy,currentOntology,individualDataProperties,individualObjectProperties,indy.getTypes(currentOntology).toString()));
            }
            else if(indy.getTypes(currentOntology).toString().contains("Data")||
                    indy.getTypes(currentOntology).toString().contains("Expert")||
                    indy.getTypes(currentOntology).toString().contains("Publication")){
                allSources.add(new Source(indy,currentOntology,individualDataProperties,individualObjectProperties,indy.getTypes(currentOntology).toString()));
            }
            else{
            }
        }
    }
    
    public void setupPartTwo(){
        //add Additional linking information between list and objects
        for(Node n:allNodes){
            n.setDistribution(allDistributions);
        }
        for(Edge edge:allEdges){
            edge.setComesFrom(allNodes);
            edge.setPointsTo(allNodes);
        }
        for(Fragment f:allFragments){
            f.setMembersList(allNodes, allFragments);
            f.setFragMembersList(allFragments);
        }
    }
    
    
    
    public void setupFileStructure(){
        
        ArrayList<File> directories = new ArrayList<File>();
        
        // Create the directory if it does not exists
        File fo = new File(directoryName);       
        if(!fo.exists()){
          fo.mkdir();
        }

        
 
        directories.add(new File(directoryName+"/distribution"));
        directories.add(new File(directoryName+"/evidence"));
        directories.add(new File(directoryName+"/fragment"));
        directories.add(new File(directoryName+"/relation"));
        directories.add(new File(directoryName+"/source"));
        directories.add(new File(directoryName+"/variable"));
         
        for(File f:directories){
            if(!checkDirectories(f)){
                System.out.println("Error: Directory Setup Error");
            }
        }
        
        addCssAndScript();
    }
    
    public void addCssAndScript(){
        System.out.println(directoryName);
        File dir = new File(directoryName);
        File css = new File(directoryName+"/style/"); 
        File script = new File(directoryName+"/script/");
        File model = new File(directoryName+"/model/");
    //    model.mkdir();
    //    css.mkdir();
    //    script.mkdir();
        
        File orgcss = new File("resources/style/"); 
        File orgscript = new File("resources/script/");
        File orgmodel = new File("resources/model/");         
        try {
            copyFolder(orgcss, css);
            copyFolder(orgscript, script);
            copyFolder(orgmodel, model);
        } catch (IOException ex) {
            Logger.getLogger(EvidenceBaseOWL.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }
    
        public static void copyFolder(File src, File dest)
    	throws IOException{
 
    	if(src.isDirectory()){
 
    		//if directory not exists, create it
    		if(!dest.exists()){
    		   dest.mkdir();
    		   System.out.println("Directory copied from " 
                              + src + "  to " + dest);
    		}
 
    		//list all the directory contents
    		String files[] = src.list();
 
    		for (String file : files) {
    		   //construct the src and dest file structure
    		   File srcFile = new File(src, file);
    		   File destFile = new File(dest, file);
    		   //recursive copy
    		   copyFolder(srcFile,destFile);
    		}
 
    	}else{
    		//if file, then copy it
    		//Use bytes stream to support all file types
    		InputStream in = new FileInputStream(src);
    	        OutputStream out = new FileOutputStream(dest); 
 
    	        byte[] buffer = new byte[1024];
 
    	        int length;
    	        //copy the file content in bytes 
    	        while ((length = in.read(buffer)) > 0){
    	    	   out.write(buffer, 0, length);
    	        }
 
    	        in.close();
    	        out.close();
    	        System.out.println("File copied from " + src + " to " + dest);
    	}
    }

    
    

    
    public static boolean checkDirectories(File givenDirectory){
        if(givenDirectory.exists()){
            if(givenDirectory.list().length>0){
                
                File []allFiles = givenDirectory.listFiles();
                for(File f:allFiles){
                    f.delete();
                }
                
            }
            
            if(givenDirectory.list().length==0){
                return true;
            }
            else{
                return false;
            }
        }
        
        return givenDirectory.mkdir();
    }
    
    public void generateHTML(){
        

        
        new HTMLPage("index", allEdges, allDistributions, 
                   allEvidence,allFragments,allSources,allNodes,directoryName);
        
        for(Node n: allNodes){
            new HTMLNodePage(n.getName(), n, allEdges, allDistributions, 
                    allEvidence,allFragments,allSources,allNodes,directoryName);
        }
        for(Edge ed:allEdges){
            new HTMLEdgePage(ed.getName(), ed, allEdges, allDistributions, 
                    allEvidence,allFragments,allSources,allNodes,directoryName);
        }
        for(Fragment frag:allFragments){
            new HTMLFragmentPage(frag.getName(), frag, allEdges, allDistributions, 
                    allEvidence,allFragments,allSources,allNodes,directoryName);
        }
        for(Evidence ev:allEvidence){
            new HTMLEvidencePage(ev.getName(), ev, allEdges, allDistributions, 
                    allEvidence,allFragments,allSources,allNodes,directoryName);        
        }
        for(Source s:allSources){
            new HTMLSourcePage(s.getName(), s, allEdges, allDistributions, 
                    allEvidence,allFragments,allSources,allNodes,directoryName);
        }
        for(Distribution dist:allDistributions){
            new HTMLDistributionPage(dist.getName(), dist, allEdges, allDistributions, 
                    allEvidence,allFragments,allSources,allNodes,directoryName);
        }
    }
    
    public void openIndex() throws IOException{
        Desktop.getDesktop().open(new File(directoryName+"/index.html"));
    }

    public static String getbNetworkTitle() {
        return bNetworkTitle;
    }
    
    
    
}