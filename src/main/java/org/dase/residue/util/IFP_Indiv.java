package org.dase.residue.util;
/*
Written by sarker.
Written at 11/11/19.
*/

import org.apache.commons.csv.CSVParser;
import org.dase.ecii.util.Utility;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

public class IFP_Indiv {

    private OWLOntology owlOntology;
    private OWLDataFactory owlDataFactory;
    private OWLOntologyManager owlOntologyManager;

    String pathToSave = "/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/IFP_only_instances_v2.owl";
    String csvPath = "/Users/sarker/Workspaces/Jetbrains/residue/data/RCTA_IFP/RCTA_IFP_data_hfcD_with_POS_keywords_v4.csv";

//    int questionCounter = 0;

    /**
     * Create Individuals from the IFP ids
     * @throws OWLOntologyCreationException
     * @throws OWLOntologyStorageException
     */
    public void process_csv() throws OWLOntologyCreationException, OWLOntologyStorageException {
        owlOntologyManager = OWLManager.createOWLOntologyManager();
        owlOntology = owlOntologyManager.createOntology(IRI.create("http://www.daselab.com/residue/analysis"));
        owlDataFactory = owlOntologyManager.getOWLDataFactory();


        CSVParser csvRecords = Utility.parseCSV(csvPath, true);
        String column_name_question = "question";
        String column_name_ifpId = "ifpId";
        String indiv_prefix = "http://www.daselab.com/residue/analysis#IFP_";
        csvRecords.forEach(strings -> {
            String val = strings.get(column_name_question);
            String ifp_number = strings.get(column_name_ifpId);

            IRI indiIRI = IRI.create(indiv_prefix + ifp_number);
            OWLNamedIndividual individual = owlDataFactory.getOWLNamedIndividual(indiIRI);
            OWLLiteral owlLiteral = owlDataFactory.getOWLLiteral(val);
            OWLAnnotation annotation = owlDataFactory.getOWLAnnotation(owlDataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), owlLiteral);

            OWLAxiom owlDeclarationAxiom = owlDataFactory.getOWLDeclarationAxiom(individual);
            OWLAxiom owlAnnotationAxiom = owlDataFactory.getOWLAnnotationAssertionAxiom(individual.getIRI(), annotation);

            owlOntologyManager.addAxiom(owlOntology, owlDeclarationAxiom);
            owlOntologyManager.addAxiom(owlOntology, owlAnnotationAxiom);

//            questionCounter += 1;
        });

        Utility.saveOntology(owlOntology, pathToSave);
    }

    public static void main(String [] args){
        try {

            IFP_Indiv ifp_indiv = new IFP_Indiv();
            ifp_indiv.process_csv();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
