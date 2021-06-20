package org.dase.residue.kgcreation.crest;

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
 * Create Crest knowledge graph based on Ion's extra data along with his summarized data.
 * <p>
 * Ion and Sarker created the sketch of the kg name: /Volumes/Samsung_T5/projects_including_data_expr/residue_projects/data/crest/kg-from-summarized-data-by-ion/teams-v2.png
 * Ion provided team2.csv file via email.
 *
 * <p>
 * Objectives:
 * 1. Differentiate 2 teams on why their performance is different
 * Positive and negative individuals (teams) can be differentiated using column ind_perf_d column name.
 *
 *
 * Creation:
 * <p>
 * Column team_id (Ion put blank for it's name) : Individuals
 * <p>
 * individuals-attributes
 * Other columns
 * Column team is object-property and other columns are data property
 * Skills (not provided in teams2.csv but obtained from literature which Ion shared during the kg sketchup) are object property.
 *
 * Tricky stuff:
 * We got information about skills of humans from literature which is further detailed by gender (male and female).
 * ----- How to relate Male and Female with Gender_num column (0=all female, 1=all male)?
 *
 * @formatter:on
 */

public class Team_V2 {

    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private OWLOntology owlOntology;
    private OWLDataFactory owlDataFactory;
    private OWLOntologyManager owlOntologyManager;

    private String prefix = "http://www.daselab.org/ontologies/crest#";


    String csv_path = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/data/crest/summarized-data-by-Ion/teams2-v4.csv";
    String KGSave_to_path = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/data/crest/kg-from-summarized-data-by-ion/teams2-v5.owl";


    public void initOntoFactory() {
        owlOntologyManager = OWLManager.createOWLOntologyManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
    }


   // String[] columnNamesForDataPropForTrait = {"Extraversion", "Open", "Conscientious", "Psych_collect", "Neroticism"};
    String[] columnNamesForDataPropForGender = {"Male", "Female"};
    String columnNameObjProp = "team_member_type";
    String columnNameIndiv = "team_id";

    // for global access
    OWLObjectProperty objPropBetterAt = null;
    OWLObjectProperty objPropHigherAt = null;
    OWLObjectProperty objPropHasGender = null; // male of female
    OWLClass owlClassMale = null;
    OWLClass owlClassFemale = null;
    OWLClass owlClassSkill = null;
    OWLClass owlClassTaskSwitching = null;
    OWLClass owlClassTeamWork = null;
    OWLClass owlClassCoOperation = null;
    OWLClass owlClassCommunication = null;
    OWLClass owlClassTrait = null;
    OWLClass owlClassIntelligence = null;
    OWLClass owlClassEmotion = null;
    // OWLClass owlClassExtraversion = null;


    /**
     * Insert the facts we got from teams-2.png
     *
     * @return
     */
    public HashSet<OWLAxiom> createFacts() {
        HashSet<OWLAxiom> axioms = new HashSet<>();

        // Team
        IRI team_class_iri = IRI.create(prefix, "Team");
        OWLClass owlClassTeam = owlDataFactory.getOWLClass(team_class_iri);
        OWLDeclarationAxiom owlDeclarationAxiom = owlDataFactory.getOWLDeclarationAxiom(owlClassTeam);
        axioms.add(owlDeclarationAxiom);

        // Human
        IRI human_class_iri = IRI.create(prefix, "Human");
        OWLClass owlClassHuman = owlDataFactory.getOWLClass(human_class_iri);
        owlDeclarationAxiom = owlDataFactory.getOWLDeclarationAxiom(owlClassHuman);
        axioms.add(owlDeclarationAxiom);

        // Gender
        IRI gender_class_iri = IRI.create(prefix, "Gender");
        OWLClass owlClassGender = owlDataFactory.getOWLClass(gender_class_iri);
        owlDeclarationAxiom = owlDataFactory.getOWLDeclarationAxiom(owlClassGender);
        axioms.add(owlDeclarationAxiom);

        // Male subClass of Human
        IRI male_class_iri = IRI.create(prefix, "Male");
        owlClassMale = owlDataFactory.getOWLClass(male_class_iri);
        owlDeclarationAxiom = owlDataFactory.getOWLDeclarationAxiom(owlClassMale);
        axioms.add(owlDeclarationAxiom);
        axioms.add(owlDataFactory.getOWLSubClassOfAxiom(owlClassMale, owlClassHuman));

        // Female subClass of Human
        IRI female_class_iri = IRI.create(prefix, "Female");
        owlClassFemale = owlDataFactory.getOWLClass(female_class_iri);
        owlDeclarationAxiom = owlDataFactory.getOWLDeclarationAxiom(owlClassFemale);
        axioms.add(owlDeclarationAxiom);
        axioms.add(owlDataFactory.getOWLSubClassOfAxiom(owlClassFemale, owlClassHuman));


        // Skill
        IRI skill_class_iri = IRI.create(prefix, "Skill");
        owlClassSkill = owlDataFactory.getOWLClass(skill_class_iri);
        owlDeclarationAxiom = owlDataFactory.getOWLDeclarationAxiom(owlClassSkill);
        axioms.add(owlDeclarationAxiom);

        // Task_switching subClass of Skill
        IRI task_switching_class_iri = IRI.create(prefix, "Task_switching");
        owlClassTaskSwitching = owlDataFactory.getOWLClass(task_switching_class_iri);
        owlDeclarationAxiom = owlDataFactory.getOWLDeclarationAxiom(owlClassTaskSwitching);
        axioms.add(owlDeclarationAxiom);
        axioms.add(owlDataFactory.getOWLSubClassOfAxiom(owlClassTaskSwitching, owlClassSkill));

        // Team_work subClass of Skill
        IRI team_work_class_iri = IRI.create(prefix, "Team_work");
        owlClassTeamWork = owlDataFactory.getOWLClass(team_work_class_iri);
        owlDeclarationAxiom = owlDataFactory.getOWLDeclarationAxiom(owlClassTeamWork);
        axioms.add(owlDeclarationAxiom);
        axioms.add(owlDataFactory.getOWLSubClassOfAxiom(owlClassTeamWork, owlClassSkill));

        // Co_operation subClass of Skill
        IRI co_operation_class_iri = IRI.create(prefix, "Co_operation");
        owlClassCoOperation = owlDataFactory.getOWLClass(co_operation_class_iri);
        owlDeclarationAxiom = owlDataFactory.getOWLDeclarationAxiom(owlClassCoOperation);
        axioms.add(owlDeclarationAxiom);
        axioms.add(owlDataFactory.getOWLSubClassOfAxiom(owlClassCoOperation, owlClassSkill));

        // Communication subClass of Skill
        IRI communication_class_iri = IRI.create(prefix, "Communication");
        owlClassCommunication = owlDataFactory.getOWLClass(communication_class_iri);
        owlDeclarationAxiom = owlDataFactory.getOWLDeclarationAxiom(owlClassCommunication);
        axioms.add(owlDeclarationAxiom);
        axioms.add(owlDataFactory.getOWLSubClassOfAxiom(owlClassCommunication, owlClassSkill));


        // Trait
        IRI trait_class_iri = IRI.create(prefix, "Trait");
        owlClassTrait = owlDataFactory.getOWLClass(trait_class_iri);
        owlDeclarationAxiom = owlDataFactory.getOWLDeclarationAxiom(owlClassTrait);
        axioms.add(owlDeclarationAxiom);

        // Ion mentioned 3 subclasses of Trait -> Intelligence, Emotion, Extraversion
        // data file teams-2.csv also has extraversion data so manually adding only intelligence and emotion
        // Other subclasses () of traits will be created by calling function createDataProperties(String[] columnNamesForDataPropForTrait)

        // Intelligence subClass of Trait
        IRI intelligence_class_iri = IRI.create(prefix, "Intelligence");
        owlClassIntelligence = owlDataFactory.getOWLClass(intelligence_class_iri);
        owlDeclarationAxiom = owlDataFactory.getOWLDeclarationAxiom(owlClassIntelligence);
        axioms.add(owlDeclarationAxiom);
        axioms.add(owlDataFactory.getOWLSubClassOfAxiom(owlClassIntelligence, owlClassTrait));

        // Emotion subClass of Trait
        IRI emotion_class_iri = IRI.create(prefix, "Emotion");
        owlClassEmotion = owlDataFactory.getOWLClass(emotion_class_iri);
        owlDeclarationAxiom = owlDataFactory.getOWLDeclarationAxiom(owlClassEmotion);
        axioms.add(owlDeclarationAxiom);
        axioms.add(owlDataFactory.getOWLSubClassOfAxiom(owlClassEmotion, owlClassTrait));


        // create object Properties
        // are made of
        IRI areMadeOf = IRI.create(prefix, "betterAt");
        OWLObjectProperty objPropAreMadeOf = owlDataFactory.getOWLObjectProperty(areMadeOf);
        axioms.add(owlDataFactory.getOWLObjectPropertyDomainAxiom(objPropAreMadeOf, owlClassTeam));
        axioms.add(owlDataFactory.getOWLObjectPropertyRangeAxiom(objPropAreMadeOf, owlClassHuman));

        // hasTrait
        IRI hasTrait = IRI.create(prefix, "hasTrait");
        OWLObjectProperty objPropHasTrait = owlDataFactory.getOWLObjectProperty(hasTrait);
        axioms.add(owlDataFactory.getOWLObjectPropertyDomainAxiom(objPropHasTrait, owlClassHuman));
        axioms.add(owlDataFactory.getOWLObjectPropertyRangeAxiom(objPropHasTrait, owlClassTrait));

        // hasGender
        IRI hasGender = IRI.create(prefix, "hasGender");
        objPropHasGender = owlDataFactory.getOWLObjectProperty(hasGender);
        axioms.add(owlDataFactory.getOWLObjectPropertyDomainAxiom(objPropHasGender, owlClassHuman));
        axioms.add(owlDataFactory.getOWLObjectPropertyRangeAxiom(objPropHasGender, owlClassGender));

        // hasSkills
        IRI hasSkills = IRI.create(prefix, "hasSkills");
        OWLObjectProperty objPropHasSkills = owlDataFactory.getOWLObjectProperty(hasSkills);
        axioms.add(owlDataFactory.getOWLObjectPropertyDomainAxiom(objPropHasSkills, owlClassHuman));
        axioms.add(owlDataFactory.getOWLObjectPropertyRangeAxiom(objPropHasSkills, owlClassSkill));

        // betterAt
        IRI objPropIRIBetterAt = IRI.create(prefix, "betterAt");
        objPropBetterAt = owlDataFactory.getOWLObjectProperty(objPropIRIBetterAt);
        axioms.add(owlDataFactory.getOWLObjectPropertyDomainAxiom(objPropBetterAt, owlClassGender));
        axioms.add(owlDataFactory.getOWLObjectPropertyRangeAxiom(objPropBetterAt, owlClassSkill));

        // highertAt
        IRI objPropIRIHigherAt = IRI.create(prefix, "higherAt");
        objPropHigherAt = owlDataFactory.getOWLObjectProperty(objPropIRIHigherAt);
        axioms.add(owlDataFactory.getOWLObjectPropertyDomainAxiom(objPropHigherAt, owlClassGender));
        axioms.add(owlDataFactory.getOWLObjectPropertyRangeAxiom(objPropHigherAt, owlClassTrait));

        // information like male betterAt taskSwitching can be added by individual information.
//        owlDataFactory.getowldataproperty
//        OWLAxiom owlPropertyAssertionAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(
//                objProp,
//                owlNamedIndividual,
//                dummyIndiv);

        return axioms;
    }

    /**
     * Create properties before creating triples
     *
     * @return
     */
    public HashMap<String, OWLDataProperty> createDataProperties(String[] columnNamesForDataPropsForTrait) {

        logger.info("Creating data and object properties...............");
        HashMap<String, OWLDataProperty> columnNameToPropName = new HashMap<>();

        // data properties
        for (String columnName : columnNamesForDataPropsForTrait) {
            IRI propIRI = IRI.create(prefix, columnName);
            OWLDataProperty dataProp = owlDataFactory.getOWLDataProperty(propIRI);

            columnNameToPropName.put(columnName, dataProp);
        }

        logger.info("Creating data and object properties successfull.");
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
        IRI objPropIRI = IRI.create(prefix, columnNameObjProp);
        OWLObjectProperty objProp = owlDataFactory.getOWLObjectProperty(objPropIRI);

        // create data properties
       // HashMap<String, OWLDataProperty> columnNameToPropName = createDataProperties(columnNamesForDataPropForTrait);
        // add domain and range restrictions
        //owlAssertionAxioms.addAll(setDomainAndRangeAxioms(new HashSet<>(columnNameToPropName.values())));


        logger.info("Creating facts from the csv................");
        for (CSVRecord strings : csvParser) {

            // basic individuals
            String teamName = strings.get(columnNameIndiv);
            IRI teamName_iri = IRI.create(prefix, "Team" + teamName);
            OWLNamedIndividual owlNamedIndividualTeam = owlDataFactory.getOWLNamedIndividual(teamName_iri);

            // for Gender_num column
            // if it's value < .5 then female dominant and if it's value > .5 then male dominant
            String gender_value = strings.get("Gender_num");
            Double gender_value_double = Double.parseDouble(gender_value);

            if (gender_value_double > 0.5) {
                // male dominant
                // betterAt -> task_switching,
                // higherAt -> intelligence,

                counter += 1;
                // team hasGender male
                IRI indi_male_IRI = IRI.create(prefix, "male" + counter);
                OWLNamedIndividual indi_Male = owlDataFactory.getOWLNamedIndividual(indi_male_IRI);
                owlAssertionAxioms.addAll(objPropertyAxioms(owlNamedIndividualTeam, objPropHasGender, indi_Male, owlClassMale));

                IRI objIndi_task_switching_IRI = IRI.create(prefix, "task_switching" + counter);
                OWLNamedIndividual objIndi_task_switching = owlDataFactory.getOWLNamedIndividual(objIndi_task_switching_IRI);
                owlAssertionAxioms.addAll(objPropertyAxioms(indi_Male, objPropBetterAt, objIndi_task_switching, owlClassTaskSwitching));

                IRI objIndi_intelligence_IRI = IRI.create(prefix, "intelligence" + counter);
                OWLNamedIndividual objIndi_intelligence = owlDataFactory.getOWLNamedIndividual(objIndi_intelligence_IRI);
                owlAssertionAxioms.addAll(objPropertyAxioms(indi_Male, objPropHigherAt, objIndi_intelligence, owlClassIntelligence));

            } else {
                // female dominant
                // betterAt -> team_work, communication,
                // higherAt -> emotion, extraversion

                counter += 1;

                // team hasGender female
                IRI indi_Female_IRI = IRI.create(prefix, "female" + counter);
                OWLNamedIndividual indi_Female = owlDataFactory.getOWLNamedIndividual(indi_Female_IRI);
                owlAssertionAxioms.addAll(objPropertyAxioms(owlNamedIndividualTeam, objPropHasGender, indi_Female, owlClassFemale));

                IRI objIndi_team_work_IRI = IRI.create(prefix, "team_work" + counter);
                OWLNamedIndividual objIndi_team_work = owlDataFactory.getOWLNamedIndividual(objIndi_team_work_IRI);
                owlAssertionAxioms.addAll(objPropertyAxioms(indi_Female, objPropBetterAt, objIndi_team_work, owlClassTeamWork));

                IRI objIndi_communication_IRI = IRI.create(prefix, "communication" + counter);
                OWLNamedIndividual objIndi_communication = owlDataFactory.getOWLNamedIndividual(objIndi_communication_IRI);
                owlAssertionAxioms.addAll(objPropertyAxioms(indi_Female, objPropBetterAt, objIndi_communication, owlClassCommunication));

                IRI objIndi_emotion_IRI = IRI.create(prefix, "emotion" + counter);
                OWLNamedIndividual objIndi_emotion = owlDataFactory.getOWLNamedIndividual(objIndi_emotion_IRI);
                owlAssertionAxioms.addAll(objPropertyAxioms(indi_Female, objPropHigherAt, objIndi_emotion, owlClassEmotion));

                IRI objIndi_intelligence_IRI = IRI.create(prefix, "intelligence" + counter);
                OWLNamedIndividual objIndi_intelligence = owlDataFactory.getOWLNamedIndividual(objIndi_intelligence_IRI);
                owlAssertionAxioms.addAll(objPropertyAxioms(indi_Female, objPropHigherAt, objIndi_intelligence, owlClassIntelligence));

                // extraversion take it from the data/csv file
            }

            // for each numeric column (dataProperty)
//            for (String dataPropColumnName : columnNameToPropName.keySet()) {
//                String data_value = strings.get(dataPropColumnName);
//                Double data_value_double = Double.parseDouble(data_value);
//
//                OWLDataProperty dataProp = columnNameToPropName.get(dataPropColumnName);
//
//                if (null != data_value) {
//                    OWLAxiom owlPropertyAssertionAxiom = owlDataFactory.getOWLDataPropertyAssertionAxiom(
//                            dataProp,
//                            owlNamedIndividualTeam,
//                            data_value_double);
//
//                    owlAssertionAxioms.add(owlPropertyAssertionAxiom);
//                }
//            }

            // for the text column (objectProperty)
            // skip the team type (engineer, geology etc)
//            String value = strings.get(columnNameObjProp);
//            if (null != value) {
//                // create dummy entity and assign type, because for assigning objectProperty we can not create triple like: indiv-objProp-String. It has to be indiv-objProp-indiv where object indiv will have the type String
//
//                counter += 1;
//                owlAssertionAxioms.addAll(objPropertyAxioms(counter, owlNamedIndividualTeam, objProp, value));
//            }
        }
        logger.info("Creating facts from the csv successfull.");

        return owlAssertionAxioms;
    }

    /**
     * @param counter
     * @param owlNamedIndividual
     * @param objProp
     * @param nameToCreateDummyClass
     * @return
     */
    public HashSet<OWLAxiom> objPropertyAxioms(int counter, OWLNamedIndividual owlNamedIndividual, OWLObjectProperty objProp, String nameToCreateDummyClass) {

        HashSet<OWLAxiom> owlAxioms = new HashSet<>();
        IRI dummyIndi_iri = IRI.create(prefix, nameToCreateDummyClass + counter);
        counter += 1;
        OWLNamedIndividual dummyIndiv = owlDataFactory.getOWLNamedIndividual(dummyIndi_iri);

        IRI dummyClass_IRI = IRI.create(prefix, nameToCreateDummyClass);
        OWLClass owlClass = owlDataFactory.getOWLClass(dummyClass_IRI);
        OWLClassAssertionAxiom owlClassAssertionAxiom = owlDataFactory.
                getOWLClassAssertionAxiom(owlClass, dummyIndiv);
        owlAxioms.add(owlClassAssertionAxiom);

        OWLAxiom owlPropertyAssertionAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(
                objProp,
                owlNamedIndividual,
                dummyIndiv);

        owlAxioms.add(owlPropertyAssertionAxiom);

        return owlAxioms;
    }


    /**
     * @param owlNamedIndividualSubj
     * @param objProp
     * @param owlClass
     * @return
     */
    public HashSet<OWLAxiom> objPropertyAxioms(OWLNamedIndividual owlNamedIndividualSubj, OWLObjectProperty objProp, OWLNamedIndividual owlNamedIndividualObj, OWLClass owlClass) {

        HashSet<OWLAxiom> owlAxioms = new HashSet<>();

        OWLClassAssertionAxiom owlClassAssertionAxiom = owlDataFactory.
                getOWLClassAssertionAxiom(owlClass, owlNamedIndividualObj);
        owlAxioms.add(owlClassAssertionAxiom);

        OWLAxiom owlPropertyAssertionAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(
                objProp,
                owlNamedIndividualSubj,
                owlNamedIndividualObj);

        owlAxioms.add(owlPropertyAssertionAxiom);

        return owlAxioms;
    }


    private void createKG() {
        try {
            initOntoFactory();

            HashSet<OWLAxiom> owlAxioms = new HashSet<>();
            owlAxioms.addAll(createFacts());
            owlAxioms.addAll(parseTeamsCSV(csv_path));

            owlOntology = owlOntologyManager.createOntology(owlAxioms, IRI.create(prefix));
            Utility.saveOntology(owlOntology, KGSave_to_path);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Team_V2 team = new Team_V2();

        try {
            team.createKG();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
