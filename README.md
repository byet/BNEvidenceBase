# BNEvidenceBase
BNEvidenceBase transforms an BN evidence base that is developed as an OWL ontology into a browsable webpage. 

For more details about BN Evidence Bases see: Yet B, Perkins ZB, Tai NR, and Marsh DWR (2014). "Explicit Evidence for Prognostic Bayesian Network Models" Studies in Health Technology and Informatics, 205, pp.53-57. http://dx.doi.org/10.3233/978-1-61499-432-9-53

For examples of BN Evidence Bases generated with BNEvidenceBase see:
  1. http://www.traumamodels.com/Asia_EBase/
  2. http://www.traumamodels.com/atcbn/ATC_EBase
 
  
# Running BNEvidenceBase

BNEvidenceBase requires three input arguments to run: 
	(1) The name of the ontology file
	(2) The title of the ontology (this is defined by user)
	(3) The name of the folder where the generated html files will be saved
	
EXAMPLE: 

To run the project from the command line, go to the dist folder and
type the following:

java -jar BNEvidenceBase.jar "EvidenceBase_AsiaBN.owl" "Asia BN Evidence-Base" "AsiaEBase"

# License

This project is licensed under the GNU Lesser General Public License. A license file is provided in the project repository.
