package org.dase.residue.kgcreation.phite;
/*
Written by sarker.
Written at 05/21/2021.
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
 * @formatter:off
 * Same as the default Gene_to_susceptibility but applying the filter
 *
 * @formatter:on
 */
public class Gene_to_susceptibility_4 {

    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private OWLOntology owlOntology;
    private OWLDataFactory owlDataFactory;
    private OWLOntologyManager owlOntologyManager;

    private String prefix = "http://www.daselab.org/ontologies/dream#";

    private String lv_facts_for_ecii_csv_path = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/data/phite/gene-to-susceptibility/LV_facts_for_ECII_combined.csv";
    private String lv_to_geneID_mapping = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/data/phite/gene-to-susceptibility/LV_to_GeneID_mapping.csv";
    private String gene_to_disease_mapping = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/data/phite/gene-to-susceptibility/DREAM-genes.csv";
    private String KGSave_to_path = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/data/phite/gene-to-susceptibility/running-again-06-19-2021/gene-to-susceptibility-combined-v4.owl";


    OWLObjectProperty objPropSubjectAssociatedWithHighForLV = null;
    OWLObjectProperty objPropSubjectAssociatedWithLowForLV = null;
    OWLObjectProperty objPropSubjectAssociatedWithMediumForLV = null;

    OWLObjectProperty objPropLVAssociatedWithGene = null;
    OWLObjectProperty objPropGeneAssociatedWithDisease = null;

    private int counter;

    // all genes which were referred in dream-genes list
    private HashSet<String> dreamGenesCachedList = null;

    public void initOntoFactory() {
        owlOntologyManager = OWLManager.createOWLOntologyManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
    }

    /**
     *
     */
    private void createObjProps() {

        IRI lowForIRI = IRI.create(prefix, "lowFor");
        objPropSubjectAssociatedWithLowForLV = owlDataFactory.getOWLObjectProperty(lowForIRI);

        IRI mediumForIRI = IRI.create(prefix, "mediumFor");
        objPropSubjectAssociatedWithMediumForLV = owlDataFactory.getOWLObjectProperty(mediumForIRI);

        IRI highForIRI = IRI.create(prefix, "highFor");
        objPropSubjectAssociatedWithHighForLV = owlDataFactory.getOWLObjectProperty(highForIRI);

        IRI lvAssociatedWithGene = IRI.create(prefix, "lvAssociatedWithGene");
        objPropLVAssociatedWithGene = owlDataFactory.getOWLObjectProperty(lvAssociatedWithGene);

        IRI geneAssociatedWithDisease = IRI.create(prefix, "geneAssociatedWithDisease");
        objPropGeneAssociatedWithDisease = owlDataFactory.getOWLObjectProperty(geneAssociatedWithDisease);
    }

    private void cacheDreamGenes(String path) {

        logger.info("Parsing " + path + " to cache dream genes");
        dreamGenesCachedList = new HashSet<>();
        CSVParser csvParser = Utility.parseCSV(path, true);

        for (CSVRecord strings : csvParser) {
            String gene_id = strings.get("Gene_ids");
            if (gene_id != null && gene_id.length() > 0) {
                dreamGenesCachedList.add(gene_id);
            }
        }
    }

    /**
     * create triple: LV1---rdf:Type---Pathway1 ?????
     *
     * @param path
     * @return
     */
    public HashSet<OWLAxiom> parseLV_to_Gene_ID_mapping(String path) {

        logger.info("Parsing " + path + " to create lv to gene mapping.............");
        CSVParser csvParser = Utility.parseCSV(path, true);

        HashSet<OWLAxiom> owlAssertionAxioms = new HashSet<>();
        for (CSVRecord strings : csvParser) {
            // column lv_id
            String lv_id = strings.get("lv_id");
            if (lv_id != null) {
                IRI lv_id_iri = IRI.create(prefix, lv_id);
                OWLNamedIndividual indivLVID = owlDataFactory.getOWLNamedIndividual(lv_id_iri);

                OWLClassAssertionAxiom owlClassAssertionAxiom = null;

                // column gene_id
                String gene_id = strings.get("gene_id");
                if (gene_id != null && gene_id.length() > 0 && dreamGenesCachedList.contains(gene_id))  {
                    IRI gene_id_iri = IRI.create(prefix, gene_id);
                    OWLNamedIndividual indivGeneID = owlDataFactory.getOWLNamedIndividual(gene_id_iri);
                    owlClassAssertionAxiom = owlDataFactory.
                            getOWLClassAssertionAxiom(owlDataFactory.getOWLThing(), indivGeneID);

                    OWLAxiom owlPropertyAssertionAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(objPropLVAssociatedWithGene, indivLVID, indivGeneID);

                    owlAssertionAxioms.add(owlClassAssertionAxiom);
                    owlAssertionAxioms.add(owlPropertyAssertionAxiom);
                }
            }
        }

        logger.info("Parsing " + path + " to create lv to gene mapping finished.");
        logger.info("Total owlAssertionAxioms created: " + owlAssertionAxioms.size());

        return owlAssertionAxioms;
    }

    /**
     * create triple: LV1---rdf:Type---Pathway1
     * expected file to work: DREAM-genes.csv
     *
     * @param path
     * @return
     */
    public HashSet<OWLAxiom> parseGene_to_Disease_mapping(String path) {

        logger.info("Parsing " + path + " to create type assertion");
        CSVParser csvParser = Utility.parseCSV(path, true);

        HashSet<OWLAxiom> owlAssertionAxioms = new HashSet<>();

        for (CSVRecord strings : csvParser) {


            // column gene_id
            String gene_id = strings.get("Gene_ids");
            if (gene_id != null) {
                IRI gene_id_iri = IRI.create(prefix, gene_id);
                OWLNamedIndividual indivGeneId = owlDataFactory.getOWLNamedIndividual(gene_id_iri);
                OWLClassAssertionAxiom owlClassAssertionAxiom = owlDataFactory.
                        getOWLClassAssertionAxiom(owlDataFactory.getOWLThing(), indivGeneId);

                // column Disease_or_Condition
                String diseaseName = strings.get("Disease_or_Condition");
                if (diseaseName != null) {
                    // create dummy indiv
                    IRI dummy_disease_iri = IRI.create(prefix, diseaseName + counter);
                    counter += 1;
                    OWLNamedIndividual indiv_dummy_DiseaseID = owlDataFactory.getOWLNamedIndividual(dummy_disease_iri);

                    // assign to class
                    //our expected outcome is diseaseName so both typed and without typed (indiv without class assertion) versions are created
                    IRI disease_Class_IRI = IRI.create(prefix, diseaseName);
                    OWLClass owlClassDisease = owlDataFactory.getOWLClass(disease_Class_IRI);
                    owlClassAssertionAxiom = owlDataFactory.
                            getOWLClassAssertionAxiom(owlClassDisease, indiv_dummy_DiseaseID);
                    owlAssertionAxioms.add(owlClassAssertionAxiom);

                    OWLAxiom owlPropertyAssertionAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(
                            objPropGeneAssociatedWithDisease,
                            indivGeneId,
                            indiv_dummy_DiseaseID);

                    owlAssertionAxioms.add(owlPropertyAssertionAxiom);
                }
            }
        }

        logger.info("Parsing " + path + " to create type assertion finished.");
        logger.info("Total owlClassAssertionAxioms created: " + owlAssertionAxioms.size());

        return owlAssertionAxioms;
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


        for (CSVRecord strings : csvParser) {

            // column value
            String value = strings.get("value");
            if (value != null) {
                if (value.equalsIgnoreCase("Low") || value.equalsIgnoreCase("High") | value.equalsIgnoreCase("Medium")) {
                    // column sample_id
                    String sample_id = strings.get("subject_ids");
                    if (sample_id != null) {
                        IRI sample_id_iri = IRI.create(prefix, sample_id);
                        OWLNamedIndividual indivSampleID = owlDataFactory.getOWLNamedIndividual(sample_id_iri);
                        OWLClassAssertionAxiom owlClassAssertionAxiom = owlDataFactory.
                                getOWLClassAssertionAxiom(owlDataFactory.getOWLThing(), indivSampleID);

                        // column lv_id_space_omitted
                        String lv_id_space_omitted = strings.get("lv_id_space_omitted");
                        if (lv_id_space_omitted != null) {
                            IRI lv_id_space_omitted_iri = IRI.create(prefix, lv_id_space_omitted);
                            OWLNamedIndividual indivLVID = owlDataFactory.getOWLNamedIndividual(lv_id_space_omitted_iri);
                            owlClassAssertionAxiom = owlDataFactory.
                                    getOWLClassAssertionAxiom(owlDataFactory.getOWLThing(), indivLVID);

                            OWLAxiom owlPropertyAssertionAxiom;
                            if (value.equalsIgnoreCase("Low")) {
                                owlPropertyAssertionAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(objPropSubjectAssociatedWithLowForLV, indivSampleID, indivLVID);
                            } else if (value.equalsIgnoreCase("Medium")) {
                                owlPropertyAssertionAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(objPropSubjectAssociatedWithMediumForLV, indivSampleID, indivLVID);
                            } else {
                                owlPropertyAssertionAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(objPropSubjectAssociatedWithHighForLV, indivSampleID, indivLVID);
                            }

                            owlAssertionAxioms.add(owlClassAssertionAxiom);
                            owlAssertionAxioms.add(owlPropertyAssertionAxiom);
                        }
                    }
                }
            }
        }

        logger.info("Parsing " + path + " to create facts finished.");
        logger.info("Total assertionAxioms created: " + owlAssertionAxioms.size());

        return owlAssertionAxioms;
    }


    private void createKG() {
        try {
            counter = 0;
            initOntoFactory();

            // cache the gene names, so only those genes will be used to relate
            // latent_variable to gene_id
            cacheDreamGenes(gene_to_disease_mapping);

            // create objProps
            createObjProps();

            HashSet<OWLAxiom> owlAxioms = new HashSet<>();
            owlAxioms.addAll(parseLV_facts_for_ECII(lv_facts_for_ecii_csv_path));
            owlAxioms.addAll(parseLV_to_Gene_ID_mapping(lv_to_geneID_mapping));
            owlAxioms.addAll(parseGene_to_Disease_mapping(gene_to_disease_mapping));


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
        Gene_to_susceptibility_4 gene_to_susceptibility = new Gene_to_susceptibility_4();

        try {
            gene_to_susceptibility.createKG();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
