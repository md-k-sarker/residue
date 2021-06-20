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
 * @formatter:off
 * Creation of knowledge graph to run sample experiment with ecii.
 * Flow-chart of the experimental setup is written in PLIER_phase-3-flow-chart-mike-verified.png
 * Data is collected from PHITE_chest_press_miRNA_ECII_data.csv (Srikanth) and phite_unique_gene_targets_top10pct.csv (Billy)
 * <p>
 * KG flow-chart: PLIER_phase-3-flow-chart-mike-verified.png
 *
 * class/concepts:
 *          target_symbol column of phite_unique_gene_targets_top10pct.csv file
 *              example: MXD1, HIVEP3...
 * objProp: highInmiRNAGeneTarget, mediumInmiRNAGeneTarget, lowInmiRNAGeneTarget
 * individuals:
 *          participant_id column of PHITE_chest_press_miRNA_ECII_data.csv file.
 *              example: PH001, PH002
 *          miRNA column of PHITE_chest_press_miRNA_ECII_data.csv file
 *              example: hsa-miR-1-3p, hsa-miR-133a-3p
 * <p>
 * Triples:
 * PH001---high---hsa-miR-1-3p
 * hsa-miR-1-3p---rdf:Type---MXD1
 *
 * Information of a participant:
 * PH001: high hsa-miR-1-3p, high hsa-miR-133a-3p,
 *             medium hsa-miR-378a-3p,
 *             low hsa-miR-451a
 * PH002: high hsa-miR-29a-3p
 *
 * Explanation sought:
 * Possible explanations for high responders
 * 1. high.(MXD1 and XLPM3) and medium.(NCL) and low.(UST)
 * 2. high.(MXD1 and XLPM3 and not ETS1) and medium.(NCL) and low.(UST)
 *
 * @formatter:on
 */
public class PhiteP3 {

    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private OWLOntology owlOntology;
    private OWLDataFactory owlDataFactory;
    private OWLOntologyManager owlOntologyManager;

    private String prefix = "http://www.daselab.org/ontologies/phite#";
    private String participants_id_csv_file = "/Users/sarker/Workspaces/Jetbrains/residue-emerald/residue/data/phite/data_from_srikanth/PHITE_chest_press_miRNA_ECII_data.csv";
    private String miRNA_to_gene_target_csv_file = "/Users/sarker/Workspaces/Jetbrains/residue-emerald/residue/data/phite/gene_targets_from_billy/phite_unique_gene_targets_top10pct.csv";
    private String KGSave_to_path = "/Users/sarker/Workspaces/Jetbrains/residue-emerald/residue/data/phite/phite-removed-dates-and-or-sign.owl";


    public void initOntoFactory() {
        owlOntologyManager = OWLManager.createOWLOntologyManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
    }

    /**
     * create triple: hsa-miR-1-3p---rdf:Type---MXD1
     *
     * @param path
     * @return
     */
    public HashSet<OWLAxiom> create_relations_miRNA_to_geneTarget(String path) {

        logger.info("Parsing " + path + " to create type assertion");
        CSVParser csvParser = Utility.parseCSV(path, true);

        HashSet<OWLAxiom> owlClassAssertionAxioms = new HashSet<>();
        for (CSVRecord strings : csvParser) {

            // column mature_mirna_id
            String mature_mirna_id = strings.get("mature_mirna_id");
            // column target_symbol
            String gene_target = strings.get("target_symbol");

            if (null != mature_mirna_id && mature_mirna_id.length() > 0
                    && null != gene_target && gene_target.length() > 0) {
                IRI mirnaIRI = IRI.create(prefix, mature_mirna_id);
                OWLNamedIndividual owlIndivmiRNA = owlDataFactory.getOWLNamedIndividual(mirnaIRI);

                IRI gene_targetIRI = IRI.create(prefix, gene_target);
                OWLClass owlClassGeneTarget = owlDataFactory.getOWLClass(gene_targetIRI);

                OWLClassAssertionAxiom owlClassAssertionAxiom = owlDataFactory.
                        getOWLClassAssertionAxiom(owlClassGeneTarget, owlIndivmiRNA);

                owlClassAssertionAxioms.add(owlClassAssertionAxiom);
            }
        }

        logger.info("Parsing " + path + " to create type assertion finished.");
        logger.info("Total owlClassAssertionAxioms created: " + owlClassAssertionAxioms.size());

        return owlClassAssertionAxioms;
    }

    /**
     * create triple like:  PH001---high---hsa-miR-1-3p
     *
     * @param path
     * @return
     */
    public HashSet<OWLAxiom> create_relations_participant_id_to_miRNA(String path) {

        logger.info("Parsing " + path + " to create facts");
        CSVParser csvParser = Utility.parseCSV(path, true);

        HashSet<OWLAxiom> owlAssertionAxioms = new HashSet<>();

        IRI highForIRI = IRI.create(prefix, "highFor");
        OWLObjectProperty objPropHighFor = owlDataFactory.getOWLObjectProperty(highForIRI);

        IRI mediumForIRI = IRI.create(prefix, "mediumFor");
        OWLObjectProperty objPropMediumFor = owlDataFactory.getOWLObjectProperty(mediumForIRI);

        IRI lowForIRI = IRI.create(prefix, "lowFor");
        OWLObjectProperty objPropLowFor = owlDataFactory.getOWLObjectProperty(lowForIRI);

        for (CSVRecord strings : csvParser) {

            // column value
            String value = strings.get("value");

            if (value.equalsIgnoreCase("Low") || value.equalsIgnoreCase("Medium")
                    || value.equalsIgnoreCase("High")) {
                // column participant_id
                String participant_id = strings.get("participant_id");
                if (null != participant_id && participant_id.length() > 0) {
                    IRI participant_id_iri = IRI.create(prefix, participant_id);
                    OWLNamedIndividual indivParticipantID = owlDataFactory.getOWLNamedIndividual(participant_id_iri);
                    OWLClassAssertionAxiom owlClassAssertionAxiom = owlDataFactory.
                            getOWLClassAssertionAxiom(owlDataFactory.getOWLThing(), indivParticipantID);

                    // column miRNA
                    String miRNA = strings.get("miRNA");
                    if (null != miRNA && miRNA.length() > 0 && !miRNA.contains("|")) {
                        IRI miRNA_IRI = IRI.create(prefix, miRNA);
                        OWLNamedIndividual indivmiRNA = owlDataFactory.getOWLNamedIndividual(miRNA_IRI);
                        owlClassAssertionAxiom = owlDataFactory.
                                getOWLClassAssertionAxiom(owlDataFactory.getOWLThing(), indivmiRNA);

                        OWLAxiom owlPropertyAssertionAxiom = null;
                        if (value.equalsIgnoreCase("Low")) {
                            owlPropertyAssertionAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(objPropLowFor, indivParticipantID, indivmiRNA);
                        } else if (value.equalsIgnoreCase("Medium")) {
                            owlPropertyAssertionAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(objPropMediumFor, indivParticipantID, indivmiRNA);
                        } else if (value.equalsIgnoreCase("High")) {
                            owlPropertyAssertionAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(objPropHighFor, indivParticipantID, indivmiRNA);
                        }

                        owlAssertionAxioms.add(owlClassAssertionAxiom);
                        owlAssertionAxioms.add(owlPropertyAssertionAxiom);
                    }
                }
            }
        }

        logger.info("Parsing " + path + " to create facts finished.");
        logger.info("Total assertionAxioms (facts) created: " + owlAssertionAxioms.size());

        return owlAssertionAxioms;
    }


    private void createKG() {
        try {
            initOntoFactory();

            HashSet<OWLAxiom> owlAxioms = new HashSet<>();
            owlAxioms.addAll(create_relations_participant_id_to_miRNA(participants_id_csv_file));
            owlAxioms.addAll(create_relations_miRNA_to_geneTarget(miRNA_to_gene_target_csv_file));

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
        PhiteP3 phiteP3 = new PhiteP3();
        try {
            phiteP3.createKG();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
