package org.dase.residue.kgcreation.phite;
/*
Written by sarker.
Written at 7/28/20.
*/

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.dase.ecii.util.Utility;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;

/**
 * Creation of knowledge graph to run sample experiment with ecii.
 * Flow-chart of the experimental setup is written in PLIER_DREAM_flow-chart.pdf
 * Data is collected from LV_facts_for_ECII.csv and LV_to_Pathway_mapping.csv
 * <p>
 * KG flow-chart:
 * class/concepts: Pathway1, Pathway2, Pathway3....
 * objProp: highFor, lowFor
 * individuals: sample1, sample2,....samplen, LV1, LV2,.....LVn
 * <p>
 * Triple:
 * sample1---highFor---LV1
 * LV1---rdf:Type---Pathway1
 */
public class LVFacts {

    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private OWLOntology owlOntology;
    private OWLDataFactory owlDataFactory;
    private OWLOntologyManager owlOntologyManager;

    private String prefix = "http://www.daselab.org/ontologies/dream#";
    private String lv_to_pathway_mapping_csv_path = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/residue-data-tmp-backup-on-03-10-2021/data/miRNA-gene_list-pliers/pliers/LV_facts/phase-4-more-after-PI-meeting-on-02-16-2021/LV_to_Pathway_mapping_03_10_2021.csv";
    private String lv_facts_for_ecii_csv_path = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/residue-data-tmp-backup-on-03-10-2021/data/miRNA-gene_list-pliers/pliers/LV_facts/phase-4-more-after-PI-meeting-on-02-16-2021/LV_facts_for_ECII_squat_03_11_2021.csv";
    private String KGSave_to_path = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/residue-data-tmp-backup-on-03-10-2021/data/miRNA-gene_list-pliers/pliers/LV_facts/phase-4-more-after-PI-meeting-on-02-16-2021/phite_squat_03_11_2021.owl";


    public void initOntoFactory() {
        owlOntologyManager = OWLManager.createOWLOntologyManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
    }

    /**
     * create triple: LV1---rdf:Type---Pathway1
     * @param path
     * @return
     */
    public HashSet<OWLAxiom> parseLV_to_Pathway_mapping(String path) {

        logger.info("Parsing " + path + " to create type assertion");
        CSVParser csvParser = Utility.parseCSV(path, true);

        HashSet<OWLAxiom> owlClassAssertionAxioms = new HashSet<>();
        for (CSVRecord strings : csvParser) {
            // column lv_id
            String lv_id = strings.get("lv_id");
            IRI lv_id_iri = IRI.create(prefix, lv_id);
            OWLNamedIndividual owlNamedIndividual = owlDataFactory.getOWLNamedIndividual(lv_id_iri);

            // column pathway
            String pathway = strings.get("pathway");
            IRI pathway_iri = IRI.create(prefix, pathway);
            OWLClass owlClass = owlDataFactory.getOWLClass(pathway_iri);

            OWLClassAssertionAxiom owlClassAssertionAxiom = owlDataFactory.
                    getOWLClassAssertionAxiom(owlClass, owlNamedIndividual);

            owlClassAssertionAxioms.add(owlClassAssertionAxiom);
        }

        logger.info("Parsing " + path + " to create type assertion finished.");
        logger.info("Total owlClassAssertionAxioms created: " + owlClassAssertionAxioms.size());

        return owlClassAssertionAxioms;
    }

    /**
     * create triple like:  sample1---highFor---LV1
     *
     * @param path
     * @return
     */
    public HashSet<OWLAxiom> parseLV_facts_for_ECII(String path) {

        logger.info("Parsing " + path + " to create facts");
        CSVParser csvParser = Utility.parseCSV(path, true);

        HashSet<OWLAxiom> owlAssertionAxioms = new HashSet<>();

        IRI highForIRI = IRI.create(prefix, "highFor");
        OWLObjectProperty objPropHighFor = owlDataFactory.getOWLObjectProperty(highForIRI);

        IRI lowForIRI = IRI.create(prefix, "lowFor");
        OWLObjectProperty objPropLowFor = owlDataFactory.getOWLObjectProperty(lowForIRI);

        for (CSVRecord strings : csvParser) {

            // column value
            String value = strings.get("value");

            if (value.equalsIgnoreCase("Low") || value.equalsIgnoreCase("High")) {
                // column sample_id
                String sample_id = strings.get("subject_id");
                IRI sample_id_iri = IRI.create(prefix, sample_id);
                OWLNamedIndividual indivSampleID = owlDataFactory.getOWLNamedIndividual(sample_id_iri);
                OWLClassAssertionAxiom owlClassAssertionAxiom = owlDataFactory.
                        getOWLClassAssertionAxiom(owlDataFactory.getOWLThing(), indivSampleID);

                // column lv_id_space_omitted
                String lv_id_space_omitted = strings.get("lv_id_space_omitted");
                IRI lv_id_space_omitted_iri = IRI.create(prefix, lv_id_space_omitted);
                OWLNamedIndividual indivLVID = owlDataFactory.getOWLNamedIndividual(lv_id_space_omitted_iri);
                owlClassAssertionAxiom = owlDataFactory.
                        getOWLClassAssertionAxiom(owlDataFactory.getOWLThing(), indivLVID);

                OWLAxiom owlPropertyAssertionAxiom;
                if (value.equalsIgnoreCase("Low")) {
                    owlPropertyAssertionAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(objPropLowFor, indivSampleID, indivLVID);
                } else {
                    owlPropertyAssertionAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(objPropHighFor, indivSampleID, indivLVID);
                }

                owlAssertionAxioms.add(owlClassAssertionAxiom);
                owlAssertionAxioms.add(owlPropertyAssertionAxiom);
            }
        }

        logger.info("Parsing " + path + " to create facts finished.");
        logger.info("Total assertionAxioms created: " + owlAssertionAxioms.size());

        return owlAssertionAxioms;
    }


    private void createKG() {
        try {
            initOntoFactory();

            HashSet<OWLAxiom> owlAxioms = new HashSet<>();
            owlAxioms.addAll(parseLV_to_Pathway_mapping(lv_to_pathway_mapping_csv_path));
            owlAxioms.addAll(parseLV_facts_for_ECII(lv_facts_for_ecii_csv_path));

            owlOntology = owlOntologyManager.createOntology(owlAxioms, IRI.create(prefix));
            Utility.saveOntology(owlOntology, KGSave_to_path);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        LVFacts lvFacts = new LVFacts();

        try {
            lvFacts.createKG();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
