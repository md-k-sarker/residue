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
 * Create Crest knowledge graph based on Ion's summarized data.
 * <p>
 * Ion provided team2.csv file via email
 * <p>
 * Objectives:
 * 1. Differentiate 2 teams on why their performance is different
 * Positive and negative individuals (teams) can be differentiated using column ind_perf_d column name.
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * Creation:
 * <p>
 * Column team_id (Ion put blank for it's name) : Individuals
 * <p>
 * individuals-attributes
 * Other columns
 * Column team is object-property and other columns are data property
 */

public class Team {

    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private OWLOntology owlOntology;
    private OWLDataFactory owlDataFactory;
    private OWLOntologyManager owlOntologyManager;

    private String prefix = "http://www.daselab.org/ontologies/crest#";


    String csv_path = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/residue-data-tmp-backup-on-03-10-2021/data/crest/summarized-data-by-Ion/teams2.csv";
    String KGSave_to_path = "/Volumes/Samsung_T5/projects_including_data_expr/residue_projects/residue-data-tmp-backup-on-03-10-2021/data/crest/summarized-data-by-Ion/teams2.owl";


    public void initOntoFactory() {
        owlOntologyManager = OWLManager.createOWLOntologyManager();
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
    }


    String[] columnNamesForDataProp = {"campaign", "mission", "session", "Extraversion", "Agreeable", "Open", "Conscientious", "Psych_collect", "Neroticism", "Gender_num"};
    String columnNameObjProp = "team";
    String columnNameIndiv = "team_id";


    /**
     * Create properties before creating triples
     *
     * @return
     */
    public HashMap<String, OWLDataProperty> createDataProperties() {

        logger.info("Creating data and object properties...............");
        HashMap<String, OWLDataProperty> columnNameToPropName = new HashMap<>();

        // data properties
        for (String columnName : columnNamesForDataProp) {
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
            IRI teamName_iri = IRI.create(prefix, "Team" + teamName);
            OWLNamedIndividual owlNamedIndividual = owlDataFactory.getOWLNamedIndividual(teamName_iri);

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

        Team team = new Team();

        try {
            team.createKG();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
