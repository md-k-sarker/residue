package org.dase.residue.kgcreation.insight;
/*
Written by sarker.
Written at 5/20/2021.
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
 * @formatter:off
 * Create insight knowledge graph (KG) based on Billy's (William Wue) combined data.
 * <p>
 * Billy provided insight_combined_G3G4_data_responderCoded.csv
 * file via email.
 * <p>
 * Objectives:
 * 1. Differentiate 2 group of subjects (5 different columns) on why their performance are different.
 *      --Subjects are categorized either as responder or nonresponder
 *      --For this categorization we have 5 predicting column.
 *          -- fitness_aprt_reponder_shuttle
 *          -- fitness_aprt_responder_run
 *          -- fitness_aprt_responder_longjump
 *          -- fitness_aprt_responder_rower
 *          -- fitness_aprt_responder_pushup
 * Positive and negative individuals (subject) are differentiated using those 5 columns based on responder or nonresponder.
 *
 * Creation:
 * <p>
 * Column Subject : Individuals
 * <p>
 * individuals-attributes:
 * Other columns (excluding Group column and other 5 columns)
 *  -- some are object Property
 *  -- some are data Property
 *
 *  Array: columnNamesForObjProp holds the object Property Names
 *  Array: columnNamesForDataProp holds the data property names
 * <p>
 * Create 2 version of the KG by including and excluding responder_token column data
 * @formatter:on
 */

public class G3G4_KG {
    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private OWLOntology owlOntology;
    private OWLDataFactory owlDataFactory;
    private OWLOntologyManager owlOntologyManager;

    private String prefix = "http://www.daselab.org/ontologies/insight#";


    String csv_path = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/data/insight-formerly-named-as-inference-or-may-be-dream/billy-05-03-2021/insight_combined_G3G4_data_responderCoded.csv";
    String KGSave_to_path = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/data/insight-formerly-named-as-inference-or-may-be-dream/billy-05-03-2021/insight_combined_G3G4_data_responderCoded-v1.owl";


    public void initOntoFactory() {
        owlOntologyManager = OWLManager.createOWLOntologyManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
    }

    String[] columnNamesForObjProp = {"demographic_gender", "demographic_brainInjury", "demographic_highestEd", "demographic_income", "demographic_raceEthnicity", "demographic_collegeMajor", "demographic_occupation", "demographic_maritalStatus", "demographic_smokingStatus", "fitness_vo2_pre_BMIclass"};

    String[] columnNamesForDataProp = {"demographic_age", "demographic_gpa", "personality_pre_grit", "emotion_pre_lonely", "motivation_pre_needForCognition45", "motivation_pre_needForCognition18", "personality_pre_theoriesOfIntelligence", "personality_big5_pre_extraversion", "personality_big5_pre_agreeableness", "personality_big5_pre_conscientousness", "personality_big5_pre_neuroticism", "personality_big5_pre_openess", "lifestyle_psqi_pre_sleepQuality", "lifestyle_psqi_pre_sleepQualityBinary", "lifestyle_pre_pretiePreference", "lifestyle_pre_pretieTolerance", "emotion_pre_bdi2", "fitness_pre_restingHR", "personality_mindfulness_ffmq_pre_observing", "personality_mindfulness_ffmq_pre_describing", "personality_mindfulness_ffmq_pre_awareness", "personality_mindfulness_ffmq_pre_nonjudging", "personality_mindfulness_ffmq_pre_nonreactivity", "personality_mindfulness_pre_maas", "emotion_masq_pre_distress", "emotion_masq_pre_anxious", "emotion_masq_pre_depression", "emotion_agq_pre_physicalAgression", "emotion_agq_pre_verbalAgression", "emotion_agq_pre_anger", "emotion_agq_pre_hosility", "lifestyle_karolinska_pre_sleepQuality", "lifestyle_karolinska_pre_sleepDuration", "demographic_act", "intelligence_pre_shipley2vocab", "intelligence_pre_figureSeries", "intelligence_pre_LSAT", "cognition_relationalMemory_tokenTask_pre_targetOverlap1_accuracy", "cognition_relationalMemory_tokenTask_pre_targetOverlap1_RT", "cognition_relationalMemory_tokenTask_pre_targetOverlap2_accuracy", "cognition_relationalMemory_tokenTask_pre_targetOverlap2_RT", "cognition_relationalMemory_tokenTask_pre_targetOverlap3Pos6_accuracy", "cognition_relationalMemory_tokenTask_pre_targetOverlap3Pos6_RT", "cognition_relationalMemory_tokenTask_pre_targetOverlap3Pos8_accuracy", "cognition_relationalMemory_tokenTask_pre_targetOverlap3Pos8_RT", "cognition_relationalMemory_tokenTask_pre_targetOverlap3Pos10_accuracy", "cognition_relationalMemory_tokenTask_pre_targetOverlap3Pos10_RT", "cognition_processingSpeed_letterComparison_pre_accuracy", "cognition_processingSpeed_letterComparison_pre_attemps", "cognition_processingSpeed_letterComparison_pre_correct", "cognition_processingSpeed_letterComparison_pre_RT", "cognition_processingSpeed_letterComparison_pre_correctRT", "cognition_processingSpeed_patternComparison_pre_accuracy", "cognition_processingSpeed_patternComparison_pre_attemps", "cognition_processingSpeed_patternComparison_pre_correct", "cognition_processingSpeed_patternComparison_pre_RT", "cognition_processingSpeed_patternComparison_pre_correctRT", "cognition_decisionMaking_admc_pre_resistanceToFraming", "cognition_decisionMaking_admc_pre_recognizingNorms", "cognition_decisionMaking_admc_pre_overUnderConfidence", "cognition_decisionMaking_admc_pre_decisionRules", "cognition_decisionMaking_admc_pre_consistentRiskPerception", "cognition_decisionMaking_admc_pre_resistanceToSunkCosts", "cognition_decisionMaking_admc_pre_zScore", "cognition_decisionMaking_admc_pre_overUnderConfidenceRescore", "cognition_processingSpeed_digitSubstitution_pre_correct", "cognition_processingSpeed_digitSubstitution_pre_attempted", "cognition_processingSpeed_digitSubstitution_pre_accuracy", "cognition_relationalMemory_iPosition_pre_swaps", "cognition_relationalMemory_iPosition_pre_rearrangements", "cognition_relationalMemory_iPosition_pre_misplacements", "cognition_relationalMemory_iPosition_pre_edgeResizing", "cognition_relationalMemory_iPosition_pre_edgeDeflection", "fitness_vo2_pre_BMI", "fitness_vo2_pre_VO2maxrel", "fitness_vo2_pre_VO2maxabs", "fitness_vo2_pre_VO2maxperc", "fitness_vo2_pre_FatFree_VO2", "fitness_vo2_pre_Hrmax", "fitness_vo2_pre_RPEmax", "fitness_vo2_pre_Fsmax", "fitness_vo2_pre_MaxConfidence", "fitness_vo2_pre_ReasonforStopping", "fitness_vo2_pre_EndingSpeed", "fitness_vo2_pre_EndingGrade", "fitness_vo2_pre_WaistNatural_cm", "fitness_vo2_pre_WaistUmbilicus_cm", "fitness_vo2_pre_WaistHips_cm", "fitness_vo2_pre_WHRatio", "fitness_vo2_pre_RLegLength", "fitness_vo2_pre_LLegLength", "fitness_vo2_pre_DominantFoot1Right_0Left", "fitness_vo2_pre_LeftAnt", "fitness_vo2_pre_LeftAnt_Perc", "fitness_vo2_pre_LeftAntMed", "fitness_vo2_pre_LeftAntMed_Perc", "fitness_vo2_pre_LeftMed", "fitness_vo2_pre_LeftMed_Perc", "fitness_vo2_pre_LeftPostMed", "fitness_vo2_pre_LeftPostMed_Perc", "fitness_vo2_pre_LeftPost", "fitness_vo2_pre_LeftPost_Perc", "fitness_vo2_pre_LeftPostLat", "fitness_vo2_pre_LeftPostLat_Perc", "fitness_vo2_pre_LeftLat", "fitness_vo2_pre_LeftLat_Perc", "fitness_vo2_pre_LeftAntLat", "fitness_vo2_pre_LeftAntLat_Perc", "fitness_vo2_pre_RightAnt", "fitness_vo2_pre_RightAnt_Perc", "fitness_vo2_pre_RightAntMed", "fitness_vo2_pre_RightAntMed_Perc", "fitness_vo2_pre_RightMed", "fitness_vo2_pre_RightMed_Perc", "fitness_vo2_pre_RightPostMed", "fitness_vo2_pre_RightPostMed_Perc", "fitness_vo2_pre_RightPost", "fitness_vo2_pre_RightPost_Perc", "fitness_vo2_pre_RightPostLat", "fitness_vo2_pre_RightPostLat_Perc", "fitness_vo2_pre_RightLat", "fitness_vo2_pre_RightLat_Perc", "fitness_vo2_pre_RightAntLat", "fitness_vo2_pre_RightAntLat_Perc", "fitness_vo2_pre_Flex", "fitness_boneDensity_pre_Wholebodybmd", "fitness_boneDensity_pre_Partialbmd", "fitness_boneDensity_pre_Headbmd", "fitness_boneDensity_pre_Leftarmbmd", "fitness_boneDensity_pre_Rightarmbmd", "fitness_boneDensity_pre_Leftribbmd", "fitness_boneDensity_pre_Rightribbmd", "fitness_boneDensity_pre_Thoracicspinalbmd", "fitness_boneDensity_pre_Lumbarspinalbmd", "fitness_boneDensity_pre_Pelvisbmd", "fitness_boneDensity_pre_Leftlegbmd", "fitness_boneDensity_pre_Rightlegbmd", "lifestyle_accelerometer_pre_pctSedentaryPA", "lifestyle_accelerometer_pre_pctLightPA", "lifestyle_accelerometer_pre_pctModeratePA", "lifestyle_accelerometer_pre_pctVigorousPA", "lifestyle_accelerometer_pre_pctVeryVigorousPA", "lifestyle_accelerometer_pre_totalMVPA", "lifestyle_accelerometer_pre_pctMVPA", "lifestyle_accelerometer_pre_avgDailyMVPA", "lifestyle_energyExpend_netKcal", "lifestyle_energyExpend_step_count", "lifestyle_energyExpend_metabolicEquivalent", "lifestyle_energyExpend_kcalPerDay", "lifestyle_energyExpend_kcalPerHour", "lifestyle_diet_pre_PreTotalFat", "lifestyle_diet_pre_PrePercentCaloriesFat", "lifestyle_diet_pre_PreTotalCarbohydrate", "lifestyle_diet_pre_PrePercentCaloriesCarbohydrate", "lifestyle_diet_pre_PreTotalProtein", "lifestyle_diet_pre_PrePercentCaloriesProtein", "lifestyle_diet_pre_PreAlcoholInG", "lifestyle_diet_pre_PrePercentCaloriesAlcohol", "lifestyle_diet_pre_PreCholesterolInMg", "fitness_aprt_pre_shuttle", "fitness_aprt_pre_longjump", "fitness_aprt_pre_rower", "fitness_aprt_pre_pushup", "fitness_aprt_pre_run"};

    String columnNameIndiv = "Subject";

    public String beautifyString(String str) {
        return str.replaceAll("/", "_").replaceAll(" ", "_").replaceAll(":", "_");
    }

    /**
     * Create properties before creating triples
     *
     * @return
     */
    public HashMap<String, OWLObjectProperty> createObjectProperties() {

        logger.info("Creating data properties...............");
        HashMap<String, OWLObjectProperty> columnNameToPropName = new HashMap<>();

        // data properties
        for (String columnName : columnNamesForObjProp) {
            IRI propIRI = IRI.create(prefix, columnName);
            OWLObjectProperty objProp = owlDataFactory.getOWLObjectProperty(propIRI);

            columnNameToPropName.put(columnName, objProp);
        }

        logger.info("Creating data properties successfull.");
        return columnNameToPropName;
    }

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
    public HashSet<OWLAxiom> parseG3G4CSV(String path) {

        HashSet<OWLAxiom> owlAssertionAxioms = new HashSet<>();

        int counter = 0;
        logger.info("Parsing " + path + " to create kg............");
        CSVParser csvParser = Utility.parseCSV(path, true);
        logger.info("Parsing " + path + " to create kg successfull.");

        // create obj prop
        HashMap<String, OWLObjectProperty> columnNameToObjPropName = createObjectProperties();

        // create data properties
        HashMap<String, OWLDataProperty> columnNameToDataPropName = createDataProperties();
        // add domain and range restrictions
        owlAssertionAxioms.addAll(setDomainAndRangeAxioms(new HashSet<>(columnNameToDataPropName.values())));

        logger.info("Creating facts from the csv................");
        for (CSVRecord strings : csvParser) {

            // basic individuals
            String subjectIdentifier = strings.get(columnNameIndiv);
            IRI subjectName_iri = IRI.create(prefix, "Subject_" + subjectIdentifier);
            OWLNamedIndividual owlNamedIndividual = owlDataFactory.getOWLNamedIndividual(subjectName_iri);

            // for each numeric column (dataProperty)
            for (String dataPropColumnName : columnNameToDataPropName.keySet()) {
                OWLDataProperty dataProp = columnNameToDataPropName.get(dataPropColumnName);

                String data_value = strings.get(dataPropColumnName);
                Double data_value_double = Double.parseDouble(data_value);

                if (null != data_value) {
                    OWLAxiom owlPropertyAssertionAxiom = owlDataFactory.getOWLDataPropertyAssertionAxiom(
                            dataProp,
                            owlNamedIndividual,
                            data_value_double);

                    owlAssertionAxioms.add(owlPropertyAssertionAxiom);
                }
            }

            // for the text column (objectProperty)
            for (String objPropColumnName : columnNameToObjPropName.keySet()) {
                OWLObjectProperty objProp = columnNameToObjPropName.get(objPropColumnName);

                String obj_prop_value = strings.get(objPropColumnName);

                if (null != obj_prop_value) {
                    // create dummy entity and assign type, because for assigning objectProperty we can not create triple like: indiv-objProp-String. It has to be indiv-objProp-indiv where object indiv will have the type String

                    obj_prop_value = beautifyString(obj_prop_value);

                    IRI dummyIndi_iri = IRI.create(prefix, obj_prop_value + counter);
                    counter += 1;
                    OWLNamedIndividual dummyIndiv = owlDataFactory.getOWLNamedIndividual(dummyIndi_iri);

                    IRI dummyClass_IRI = IRI.create(prefix, obj_prop_value);
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

        }
        logger.info("Creating facts from the csv successfull.");

        return owlAssertionAxioms;
    }


    private void createKG() {
        try {
            initOntoFactory();

            HashSet<OWLAxiom> owlAxioms = new HashSet<>();
            owlAxioms.addAll(parseG3G4CSV(csv_path));

            owlOntology = owlOntologyManager.createOntology(owlAxioms, IRI.create(prefix));
            Utility.saveOntology(owlOntology, KGSave_to_path);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

        G3G4_KG subject = new G3G4_KG();

        try {
            subject.createKG();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


