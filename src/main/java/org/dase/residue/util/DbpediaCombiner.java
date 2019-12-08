package org.dase.residue.util;
/*
Written by sarker.
Written at 5/22/18.
*/

import org.apache.commons.logging.impl.Log4JLogger;
import org.dase.ecii.util.Monitor;
import org.dase.ecii.util.Utility;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Combine ontology.
 * Steps.
 * 1. Take all axioms from the original sumo ontology
 * 2. Iterate over the ontologies onto
 * 3. For each onto take axioms.
 * 4. Merge the axioms with original axioms.
 * 5.
 */
public class DbpediaCombiner  {

    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static Monitor monitor;
    private static HashSet<OWLAxiom> owlAxioms;
    /* Folder name is the key and set< of .owl file> (annotations files) are the value.*/
    private static HashMap<String, HashSet<String>> fileBrowserMapping;

    private static String sumoPath = "/Users/sarker/Workspaces/ProjectHCBD/datas/sumo_may_13_2018/" +
            "sumo_without_indi_fresh.owl";

    private static String OntoCombinerLogPath = "/Users/sarker/Workspaces/ProjectHCBD/experiments/may_13_2018/" +
            "logs/OntoCombiner_training_a.log";
    private static String OntoCombinerSavingPath = "/Users/sarker/Workspaces/ProjectHCBD/datas/sumo_may_13_2018/" +
            "sumo_with_ade_training_a.owl";
    private static String traversingRootPath = "/Users/sarker/Workspaces/ProjectHCBD/datas/ade20k_extended/training/a/";

    private static String ontoIRIString = "http://www.daselab.org/ontologies/ADE20K/hcbdwsu";

//    public DbpediaCombiner() {
//
//    }


    public static long addAxioms(HashSet<OWLAxiom> newAxioms) {

        if (null != owlAxioms) {
            owlAxioms.addAll(newAxioms);
        } else {
            logger.error("base owlAxioms is null.");
        }
        return owlAxioms.size();
    }

    public static void saveOntology( String path, String iri) {
        IRI ontoIRI = IRI.create(iri);
        saveOntology(path, ontoIRI);
    }

    public static void saveOntology(HashSet<OWLAxiom> owlAxioms, String path, String iri) {
        IRI ontoIRI = IRI.create(iri);
        saveOntology(owlAxioms,path, ontoIRI);
    }

    /**
     * Create an ontology using all the axioms in owlAxioms.
     */
    public static void saveOntology(String savingPath, IRI ontologyIRI) {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        try {
            OWLOntology ontology = manager.createOntology(owlAxioms, ontologyIRI);
            Utility.saveOntology(ontology, savingPath);
        } catch (Exception ex) {
            logger.error("!!!!!!!!!!!!OWLOntologyCreationException!!!!!!!!!!!!");
            logger.error(Utility.getStackTraceAsString(ex));
            monitor.stopSystem("Stopping program", true);
        }
    }

    /**
     * Create an ontology using all the axioms in owlAxioms.
     */
    public static void saveOntology(HashSet<OWLAxiom> owlAxioms, String savingPath, IRI ontologyIRI) {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        try {
            OWLOntology ontology = manager.createOntology(owlAxioms, ontologyIRI);
            Utility.saveOntology(ontology, savingPath);
        } catch (Exception ex) {
            logger.error("!!!!!!!!!!!!OWLOntologyCreationException!!!!!!!!!!!!");
            logger.error(Utility.getStackTraceAsString(ex));
            monitor.stopSystem("Stopping program", true);
        }
    }


    /**
     * Check wether the file structure is correct.
     */
    private static void printFileHierarchy() {
        logger.info("############Printing files hierarchy###################");
        fileBrowserMapping.forEach((dir, files) -> {
            logger.info("\ndir: " + dir.toString());
            files.forEach(file -> {
                logger.info("\tfile:" + file.toString());
            });
        });
    }


    private static void createFileHierarchy() {
        try {

            //Files.walk(Paths.get(traversingRootPath)).filter(d->)
            // actually we can simply start from any folder and traverse.
            // because we need only file names and traversing with a folder will give any files even the file is nested.


            try {
                logger.info("iterating started with " + traversingRootPath.toString());
                HashSet<String> files = Files.walk(Paths.get(traversingRootPath))
                        .filter(f -> f.toFile().isFile() && f.toString().endsWith(".owl")).
                                map(f -> f.toString()).collect(Collectors.toCollection(HashSet::new));
                fileBrowserMapping.put(traversingRootPath.toString(), files);
            } catch (Exception ex) {
                logger.error("!!!!!!!!!!!!Exception!!!!!!!!!!!!");
                logger.error(Utility.getStackTraceAsString(ex));
                monitor.stopSystem("Stopping program", true);
            }

        } catch (Exception ex) {
            logger.error("!!!!!!!!!!!!Exception!!!!!!!!!!!!");
            logger.error(Utility.getStackTraceAsString(ex));
            monitor.stopSystem("Stopping program", true);
        }
    }

    /**
     * Iterate over folders/files to combine ontologies.
     */
    private static void doOps(String sumoOntoPath) {
        try {

            logger.info("doOps started.............");

            createFileHierarchy();

            printFileHierarchy();

            // load original sumo
            OWLOntology _ontology = Utility.loadOntology(sumoOntoPath, monitor);
            HashSet<OWLAxiom> _axioms = _ontology.getAxioms().stream().collect(Collectors.toCollection(HashSet::new));
            addAxioms(_axioms);

            // load each small onto.
            fileBrowserMapping.forEach((dir, files) -> {
                files.forEach(file -> {
                    try {

                        OWLOntology ontology = Utility.loadOntology(file, monitor);
                        logger.info("Adding axioms from : " + file.toString());
                        HashSet<OWLAxiom> axioms = ontology.getAxioms().stream().collect(Collectors.toCollection(HashSet::new));
                        addAxioms(axioms);
                        logger.info("axioms size now: " + owlAxioms.size());
                    } catch (Exception ex) {
                        logger.error("!!!!!!!!!!!!Exception!!!!!!!!!!!!");
                        logger.error(Utility.getStackTraceAsString(ex));
                        monitor.stopSystem("Stopping program", true);
                    }
                });
            });

            logger.info("\nSaving ontology at: " + OntoCombinerSavingPath);
            saveOntology(OntoCombinerSavingPath, ontoIRIString);

            logger.info("doOps finished.");
        } catch (Exception ex) {
            logger.error("!!!!!!!!!!!!Exception!!!!!!!!!!!!");
            logger.error(Utility.getStackTraceAsString(ex));
            monitor.stopSystem("Stopping program", true);
        }
    }


    private static void combineAllSmallOntoFromADE20KWithSumo(String ade2krootPath, String OntoCombinerSavingPath, String sumoPath) {
        try {
            String[] paths = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "y", "z"};
            for (int i = 0; i < paths.length; i++) {

                if (i > 0) {
                    traversingRootPath = ade2krootPath.replace("/" + paths[i - 1] + "/", "/" + paths[i] + "/");

                    OntoCombinerSavingPath = OntoCombinerSavingPath.replace("_" + paths[i - 1] + ".owl", "_" + paths[i] + ".owl");

                    OntoCombinerLogPath = OntoCombinerLogPath.replace("_" + paths[i - 1] + ".log", "_" + paths[i] + ".log");
                } else {

                }
                owlAxioms = new HashSet<>();
                fileBrowserMapping = new HashMap<>();

                logger.info("Program started.............");
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(OntoCombinerLogPath));
                PrintStream printStream = new PrintStream(bos, true);
                monitor = new Monitor(printStream);


                System.out.println("traversingRootPath: " + traversingRootPath);
                System.out.println("OntoCombinerSavingPath: " + OntoCombinerSavingPath);
                System.out.println("OntoCombinerLogPath: " + OntoCombinerLogPath);
                Log4JLogger l = new Log4JLogger();

                doOps(sumoPath);

                printStream.close();
            }

        } catch (FileNotFoundException ex) {
            logger.error("!!!!!!!!!!!!OWLOntologyCreationException!!!!!!!!!!!!");
            logger.error(Utility.getStackTraceAsString(ex));
            monitor.stopSystem("Stopping program", true);
        } finally {
            monitor.stopSystem("Program finished", true);
        }
    }


    private static void test1(String traversingRootPath, String ontoCombinerSavingPath, String ontoIRIString) {


        HashSet<OWLAxiom> allAxioms = new HashSet<>();
        try {
            Files.walk(Paths.get(traversingRootPath)).filter(f -> f.toString().endsWith(".owl")).forEach(ontoFile -> {
                try {
                    OWLOntology ontology = Utility.loadOntology(ontoFile, monitor);
                    logger.info("Adding axioms from : " + ontoFile.toString());
                    HashSet<OWLAxiom> axioms = ontology.getAxioms().stream().collect(Collectors.toCollection(HashSet::new));
                    allAxioms.addAll(axioms);

                    logger.info("axioms size now: " + allAxioms.size());
                } catch (Exception ex) {
                    logger.error("!!!!!!!!!!!!!!");
                    ex.printStackTrace();
                }
            });
        } catch (Exception ex) {
            logger.error("!!!!!!!!!!!!!!");
            ex.printStackTrace();
        }

        logger.info("\nSaving ontology at: " + ontoCombinerSavingPath);
        saveOntology(allAxioms, ontoCombinerSavingPath, ontoIRIString);

    }

    public static void combine_dbpedia_instances(){

        System.out.println("Program started");

        String dbpedia_instance_dir= "/Volumes/mdkamruzzamansarker/workspace/DBpediaKnowledgeBase/data/graphs_from_resources_v3";
        String save_merged_ontology_path= "/Volumes/mdkamruzzamansarker/workspace/DBpediaKnowledgeBase/data/graphs_from_resources_v3/combined.rdf";
        String iri= "http://dbpedia.org/ontology";

        HashSet<OWLAxiom> instance_axioms = new HashSet<>();

        // load all instances
        try {
            Files.walk(Paths.get(dbpedia_instance_dir)).filter(f-> f.toString().endsWith(".rdf")).forEach(f-> {
                try {
                    System.out.println("Loading "+ f.getFileName());
                    OWLOntology ontology = Utility.loadOntology(f.toAbsolutePath().toString());
                    HashSet<OWLAxiom> _axioms = ontology.getAxioms().stream().collect(Collectors.toCollection(HashSet::new));
                    if(_axioms != null && _axioms.size()>0){
                        instance_axioms.addAll(_axioms);
                    }
                    System.out.println("Loading "+ f.getFileName() + " finished");
                } catch (OWLOntologyCreationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Saving ontology to "+ save_merged_ontology_path);
        saveOntology(instance_axioms, save_merged_ontology_path, iri);
    }


    public static void combine_dbpedia_schema_and_combined(){

        System.out.println("Program started");

        String sumo_schema_path = "/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/country_example_v8_wrong_ifp_number.owl";
        String dbpedia_instance_path = "/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/IFP_only_instances_v2.owl";
        String save_merged_ontology_path = "/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/country_example_v9.owl";
        String iri= "http://www.daselab.com/residue/analysis";

        HashSet<OWLAxiom> axioms = new HashSet<>();

        try {
            System.out.println("Loading schema");
            // load base dbpedia
            OWLOntology dbpedia_schema = Utility.loadOntology(sumo_schema_path);
            HashSet<OWLAxiom> schema_axioms = dbpedia_schema.getAxioms().stream().collect(Collectors.toCollection(HashSet::new));
            if(schema_axioms != null && schema_axioms.size()>0){
                axioms.addAll(schema_axioms);
            }
            System.out.println("Loading  schema finished");

            System.out.println("Loading  instances");
            // load dbpedia instances
            OWLOntology dbpedia_instances = Utility.loadOntology(dbpedia_instance_path);
            HashSet<OWLAxiom> instance_axioms = dbpedia_instances.getAxioms().stream().collect(Collectors.toCollection(HashSet::new));
            if(instance_axioms != null && instance_axioms.size()>0){
                axioms.addAll(instance_axioms);
            }
            System.out.println("Loading instances finished");

        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("Saving ontology to "+ save_merged_ontology_path);
        saveOntology(axioms, save_merged_ontology_path, iri);
    }



    public static void main(String[] args) {
        String p1 = "/Users/sarker/Dropbox/Project_HCBD/Experiments/nesy-2017/";
        String p2 = "/Users/sarker/Dropbox/Project_HCBD/Experiments/nesy-2017/sumo_with_wordnet.owl";

        //  test1(p1, p2, ontoIRIString);

        combine_dbpedia_schema_and_combined();

    }

}
