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
 * Similar as Gene_to_susceptibility.java but following flow-chart-2
 * Flowchart is in flow-chart-2-Gene-to-susceptibility.png file.
 *
 *
 * @formatter:on
 */
public class Gene_to_susceptibility_2 {

    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private OWLOntology owlOntology;
    private OWLDataFactory owlDataFactory;
    private OWLOntologyManager owlOntologyManager;

    private String prefix = "http://www.daselab.org/ontologies/dream#";

    private String lv_facts_for_ecii_csv_path = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/data/phite/gene-to-susceptibility/LV_facts_for_ECII_H3N2.csv";
    private String lv_to_geneID_mapping = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/data/phite/gene-to-susceptibility/LV_to_GeneID_mapping.csv";
    private String gene_to_disease_mapping = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/data/phite/gene-to-susceptibility/DREAM-genes.csv";
    private String KGSave_to_path = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/data/phite/gene-to-susceptibility/running-again-06-19-2021/gene-to-susceptibility-H3N2-v2.owl";

    OWLObjectProperty objPropSubjectAssociatedWithHighForLV = null;
    OWLObjectProperty objPropSubjectAssociatedWithLowForLV = null;
    OWLObjectProperty objPropSubjectAssociatedWithMediumForLV = null;
    // for domain and range settings
    OWLClass owlClassSuperClassSubject = null;
    OWLClass owlClassSuperClassGene = null;
    OWLClass owlClassSuperClassDisease = null;

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
    private HashSet<OWLAxiom> createObjProps() {

        HashSet<OWLAxiom> owlAxioms = new HashSet<>();

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

        return owlAxioms;
    }

    /**
     * create triple: Gene-1---rdf:Type---Disease-1
     *
     * @param path
     * @return
     */
    public HashSet<OWLAxiom> parseLV_to_Gene_ID_mapping(String path) {

        logger.info("Parsing " + path + " to create lv to gene mapping.............");
        CSVParser csvParser = Utility.parseCSV(path, true);

        HashSet<OWLAxiom> owlAxioms = new HashSet<>();
        for (CSVRecord strings : csvParser) {

            // dreamGenesCachedList
            // column gene_id
            String gene_id = strings.get("gene_id");
            if (gene_id != null && gene_id.length() > 0 && dreamGenesCachedList.contains(gene_id)) {

                // column lv_id
                String lv_id = strings.get("lv_id");
                if (lv_id != null && lv_id.length() > 0) {
                    IRI lv_id_iri = IRI.create(prefix, lv_id);
                    OWLNamedIndividual indivLVID = owlDataFactory.getOWLNamedIndividual(lv_id_iri);


                    IRI gene_id_iri = IRI.create(prefix, gene_id);
                    OWLClass owlClassGene = owlDataFactory.getOWLClass(gene_id_iri);
//                    OWLAxiom owlAxiom = owlDataFactory.getOWLSubClassOfAxiom(owlClassGene, owlClassSuperClassGene);
                    OWLClassAssertionAxiom owlClassAssertionAxiom = owlDataFactory.
                            getOWLClassAssertionAxiom(owlClassGene, indivLVID);

//                    owlAxioms.add(owlAxiom);
                    owlAxioms.add(owlClassAssertionAxiom);
                }
            }
        }

        logger.info("Parsing " + path + " to create lv to gene mapping finished.");
        logger.info("Total owlAxioms created: " + owlAxioms.size());

        return owlAxioms;
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
            if (value != null && value.length() > 0) {
                //| value.equalsIgnoreCase("Medium")
                if (value.equalsIgnoreCase("Low") || value.equalsIgnoreCase("High")) {
                    // column sample_id
                    String sample_id = strings.get("subject_ids");
                    if (sample_id != null && sample_id.length() > 0) {
                        IRI sample_id_iri = IRI.create(prefix, sample_id);
                        OWLNamedIndividual indivSampleID = owlDataFactory.getOWLNamedIndividual(sample_id_iri);
                        OWLClassAssertionAxiom owlClassAssertionAxiom = owlDataFactory.
                                getOWLClassAssertionAxiom(owlDataFactory.getOWLThing(), indivSampleID);

                        // column lv_id_space_omitted
                        String lv_id_space_omitted = strings.get("lv_id_space_omitted");
                        if (lv_id_space_omitted != null && lv_id_space_omitted.length() > 0) {
                            IRI lv_id_space_omitted_iri = IRI.create(prefix, lv_id_space_omitted);
                            OWLNamedIndividual indivLVID = owlDataFactory.getOWLNamedIndividual(lv_id_space_omitted_iri);
                            owlClassAssertionAxiom = owlDataFactory.
                                    getOWLClassAssertionAxiom(owlDataFactory.getOWLThing(), indivLVID);

                            OWLAxiom owlPropertyAssertionAxiom = null;
                            if (value.equalsIgnoreCase("Low")) {
                                owlPropertyAssertionAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(objPropSubjectAssociatedWithLowForLV, indivSampleID, indivLVID);
                            } else if (value.equalsIgnoreCase("Medium")) {
                                owlPropertyAssertionAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(objPropSubjectAssociatedWithMediumForLV, indivSampleID, indivLVID);
                            } else if (value.equalsIgnoreCase("High")) {
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


    private void createKG() {
        try {
            counter = 0;
            initOntoFactory();

            // cache the gene names, so only those genes will be used to relate
            // latent_variable to gene_id
            cacheDreamGenes(gene_to_disease_mapping);

            HashSet<OWLAxiom> owlAxioms = new HashSet<>();

            // create objProps
            IRI class_IRI = IRI.create(prefix, "owlClassSuperClassGene");
            owlClassSuperClassGene = owlDataFactory.getOWLClass(class_IRI);
            owlAxioms.addAll(createObjProps());

            owlAxioms.addAll(parseLV_facts_for_ECII(lv_facts_for_ecii_csv_path));
            owlAxioms.addAll(parseLV_to_Gene_ID_mapping(lv_to_geneID_mapping));

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
        Gene_to_susceptibility_2 gene_to_susceptibility = new Gene_to_susceptibility_2();

        try {
            gene_to_susceptibility.createKG();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
