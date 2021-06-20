package org.dase.residue.kgcreation.insight;
/*
Written by sarker.
Written at 3/22/21.
*/

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.dase.ecii.util.Utility;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author sarker
 * Create insight knowledge graph (KG) based on Billy's (William Wue) summarized data.
 * <p>
 * Billy provided group_3_preTest_measures_continuous_scaled.csv and group_3_preTest_measures_discreet.csv
 * file via email.
 * <p>
 * Objectives:
 * 1. Differentiate 2 group of subjects (Subject column) on why their performance is different
 * Positive and negative individuals (subject) can be differentiated using column responder_dsst column name.
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * Creation:
 * <p>
 * Column Subject : Individuals
 * <p>
 * individuals-attributes
 * Other columns (excluding Group column)
 * All columns are data property
 * <p>
 * Create 2 version of the KG by including and excluding responder_token column data
 */

public class Subject {
    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private OWLOntology owlOntology;
    private OWLDataFactory owlDataFactory;
    private OWLOntologyManager owlOntologyManager;

    private String prefix = "http://www.daselab.org/ontologies/insight#";


    String csv_path = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/residue-data-tmp-backup-on-03-10-2021/data/insight-formerly-named-as-inference/summarized-data-by-Billy/group_3_preTest_measures_continuous_scaled.csv";
    String KGSave_to_path = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/residue-data-tmp-backup-on-03-10-2021/data/insight-formerly-named-as-inference/summarized-data-by-Billy/insight_continuous_scaled_excluding_responder_token-v1.owl";


    public void initOntoFactory() {
        owlOntologyManager = OWLManager.createOWLOntologyManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
    }

    String columnNameObjProp = "Group";

    String[] columnNamesForDataProp = { "demographic_gender", "demographic_highestEd", "demographic_gpa", "demographic_income", "demographic_act", "intelligence_pre_shipley2vocab", "intelligence_pre_figureSeries", "intelligence_pre_LSAT", "personality_pre_grit", "mood_pre_lonely", "motivation_pre_needForCognition45", "motivation_pre_needForCognition18", "personality_pre_theoriesOfIntelligence", "personality_big5_pre_extraversion", "personality_big5_pre_agreeableness", "personality_big5_pre_conscientousness", "personality_big5_pre_neuroticism", "personality_big5_pre_openess", "sleep_pre_psqi", "sleep_pre_psqiBinary", "lifestyle_pre_pretiePreference", "lifestyle_pre_pretieTolerance", "mood_pre_bdi2", "fitness_pre_restingHR", "mindfulness_ffmq_pre_observing", "mindfulness_ffmq_pre_describing", "mindfulness_ffmq_pre_awareness", "mindfulness_ffmq_pre_nonjudging", "mindfulness_ffmq_pre_nonreactivity", "mindfulness_pre_maas", "emotion_masq_pre_distress", "emotion_masq_pre_anxious", "emotion_masq_pre_depression", "emotion_agq_pre_physicalAgression", "emotion_agq_pre_verbalAgression", "emotion_agq_pre_anger", "emotion_agq_pre_hosility", "sleep_karolinska_pre_quality", "sleep_karolinska_pre_duration", "cognition_relationalMemory_tokenTask_pre_targetOverlap1_accuracy", "cognition_relationalMemory_tokenTask_pre_targetOverlap1_RT", "cognition_relationalMemory_tokenTask_pre_targetOverlap2_accuracy", "cognition_relationalMemory_tokenTask_pre_targetOverlap2_RT", "cognition_relationalMemory_tokenTask_pre_targetOverlap3Pos6_accuracy", "cognition_relationalMemory_tokenTask_pre_targetOverlap3Pos6_RT", "cognition_relationalMemory_tokenTask_pre_targetOverlap3Pos8_accuracy", "cognition_relationalMemory_tokenTask_pre_targetOverlap3Pos8_RT", "cognition_relationalMemory_tokenTask_pre_targetOverlap3Pos10_accuracy", "cognition_relationalMemory_tokenTask_pre_targetOverlap3Pos10_RT", "cognition_relationalMemory_tokenTask_pre_targetDprime", "cognition_relationalMemory_tokenTask_pre_targetFalseAlarms", "cognition_relationalMemory_tokenTask_pre_target_RT", "cognition_processingSpeed_letterComparison_pre_accuracy", "cognition_processingSpeed_letterComparison_pre_attemps", "cognition_processingSpeed_letterComparison_pre_correct", "cognition_processingSpeed_letterComparison_pre_RT", "cognition_processingSpeed_letterComparison_pre_correctRT", "cognition_processingSpeed_patternComparison_pre_accuracy", "cognition_processingSpeed_patternComparison_pre_attemps", "cognition_processingSpeed_patternComparison_pre_correct", "cognition_processingSpeed_patternComparison_pre_RT", "cognition_processingSpeed_patternComparison_pre_correctRT", "cognition_decisionMaking_admc_pre_resistanceToFraming", "cognition_decisionMaking_admc_pre_recognizingNorms", "cognition_decisionMaking_admc_pre_overUnderConfidence", "cognition_decisionMaking_admc_pre_decisionRules", "cognition_decisionMaking_admc_pre_consistentRiskPerception", "cognition_decisionMaking_admc_pre_resistanceToSunkCosts", "cognition_decisionMaking_admc_pre_zScore", "cognition_decisionMaking_admc_pre_overUnderConfidenceRescore", "cognition_relationalMemory_iPosition_pre_swaps", "cognition_relationalMemory_iPosition_pre_rearrangements", "cognition_relationalMemory_iPosition_pre_misplacements", "cognition_relationalMemory_iPosition_pre_edgeResizing", "cognition_relationalMemory_iPosition_pre_edgeDeflection", "cognition_relationalMemory_iPosition_pre_arrangement", "cognition_relationalMemory_iPosition_pre_distance", "fitness_aprt_pre_shuttleRun", "fitness_aprt_pre_longJump", "fitness_aprt_pre_rowing", "fitness_aprt_pre_5kRun", "fitness_aprt_pre_pushUp", "fitness_bodyComposition_pre_bmi", "fitness_aerobicCapacity_pre_maxVO2Absolute", "fitness_aerobicCapacity_pre_maxVO2Relative", "fitness_lifestyle_pre_maxVO2Absolute", "fitness_aerobicCapacity_pre_maxHR", "cognition_processingSpeed_dsst_differenceScore_correct", "TokenTask_dprime_diff", "mindfulness_ffmq_pre_mean"};

    String columnNameIndiv = "Subject";


    /**
     * Create properties before creating triples
     *
     * @return
     */
    public HashMap<String, OWLDataProperty> createDataProperties() {

        logger.info("Creating data properties...............");
        HashMap<String, OWLDataProperty> columnNameToPropName = new HashMap<>();

        // data properties
        for (String columnName : columnNamesForDataProp) {
            IRI propIRI = IRI.create(prefix, columnName);
            OWLDataProperty dataProp = owlDataFactory.getOWLDataProperty(propIRI);

            columnNameToPropName.put(columnName, dataProp);
        }

        logger.info("Creating data properties successfull.");
        return columnNameToPropName;
    }

    /**
     * @param owlDataProperties
     * @return
     */
    public HashSet<OWLAxiom> setDomainAndRangeAxioms(HashSet<OWLDataProperty> owlDataProperties) {

        HashSet<OWLAxiom> owlProeprtyDomainRangeAxioms = new HashSet<>();
        OWLAxiom owlDataPropertyAxiom;

        for (OWLDataProperty dataProp : owlDataProperties) {
            owlDataPropertyAxiom = owlDataFactory.getOWLDataPropertyDomainAxiom(dataProp,
                    owlDataFactory.getOWLThing());
            owlProeprtyDomainRangeAxioms.add(owlDataPropertyAxiom);
            owlDataPropertyAxiom = owlDataFactory.getOWLDataPropertyRangeAxiom(dataProp,
                    owlDataFactory.getDoubleOWLDatatype());
            owlProeprtyDomainRangeAxioms.add(owlDataPropertyAxiom);
        }
        return owlProeprtyDomainRangeAxioms;
    }

    /**
     * Create objectProperty for column team
     * Create dataProperty for other columns
     *
     * @param path
     * @return
     */
    public HashSet<OWLAxiom> parseTeamsCSV(String path) {

        HashSet<OWLAxiom> owlAssertionAxioms = new HashSet<>();

        int counter = 0;
        logger.info("Parsing " + path + " to create kg............");
        CSVParser csvParser = Utility.parseCSV(path, true);
        logger.info("Parsing " + path + " to create kg successfull.");

        // create obj prop
        IRI objPropIRI = IRI.create(prefix, columnNameObjProp + "_type");
        OWLObjectProperty objProp = owlDataFactory.getOWLObjectProperty(objPropIRI);

        // create data properties
        HashMap<String, OWLDataProperty> columnNameToPropName = createDataProperties();
        // add domain and range restrictions
        owlAssertionAxioms.addAll(setDomainAndRangeAxioms(new HashSet<>(columnNameToPropName.values())));


        logger.info("Creating facts from the csv................");
        for (CSVRecord strings : csvParser) {

            // basic individuals
            String teamName = strings.get(columnNameIndiv);
            IRI subjectName_iri = IRI.create(prefix, "Subject" + teamName);
            OWLNamedIndividual owlNamedIndividual = owlDataFactory.getOWLNamedIndividual(subjectName_iri);

            // for each numeric column (dataProperty)
            for (String dataPropColumnName : columnNameToPropName.keySet()) {
                String data_value = strings.get(dataPropColumnName);
                Double data_value_double = Double.parseDouble(data_value);

                OWLDataProperty dataProp = columnNameToPropName.get(dataPropColumnName);

                if (null != data_value) {
                    OWLAxiom owlPropertyAssertionAxiom = owlDataFactory.getOWLDataPropertyAssertionAxiom(
                            dataProp,
                            owlNamedIndividual,
                            data_value_double);

                    owlAssertionAxioms.add(owlPropertyAssertionAxiom);
                }
            }

            // for the text column (objectProperty)
            String value = strings.get(columnNameObjProp);
            if (null != value) {
                // create dummy entity and assign type, because for assigning objectProperty we can not create triple like: indiv-objProp-String. It has to be indiv-objProp-indiv where object indiv will have the type String

                IRI dummyIndi_iri = IRI.create(prefix, value + counter);
                counter += 1;
                OWLNamedIndividual dummyIndiv = owlDataFactory.getOWLNamedIndividual(dummyIndi_iri);

                IRI dummyClass_IRI = IRI.create(prefix, value);
                OWLClass owlClass = owlDataFactory.getOWLClass(dummyClass_IRI);
                OWLClassAssertionAxiom owlClassAssertionAxiom = owlDataFactory.
                        getOWLClassAssertionAxiom(owlClass, dummyIndiv);
                owlAssertionAxioms.add(owlClassAssertionAxiom);

                OWLAxiom owlPropertyAssertionAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(
                        objProp,
                        owlNamedIndividual,
                        dummyIndiv);

                owlAssertionAxioms.add(owlPropertyAssertionAxiom);
            }
        }
        logger.info("Creating facts from the csv successfull.");

        return owlAssertionAxioms;
    }


    private void createKG() {
        try {
            initOntoFactory();

            HashSet<OWLAxiom> owlAxioms = new HashSet<>();
            owlAxioms.addAll(parseTeamsCSV(csv_path));

            owlOntology = owlOntologyManager.createOntology(owlAxioms, IRI.create(prefix));
            Utility.saveOntology(owlOntology, KGSave_to_path);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Subject subject = new Subject();

        try {
            subject.createKG();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


