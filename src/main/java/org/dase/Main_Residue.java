package org.dase;


import org.apache.log4j.PropertyConfigurator;
import org.dase.ecii.core.CandidateSolutionFinderV1;
import org.dase.ecii.core.SharedDataHolder;
import org.dase.ecii.core.CandidateSolutionFinder;
import org.dase.ecii.core.SharedDataHolder;
import org.dase.ecii.datastructure.CandidateSolutionV1;
import org.dase.ecii.datastructure.HashMapUtility;
import org.dase.ecii.exceptions.MalFormedIRIException;
import org.dase.ecii.ontofactory.DLSyntaxRendererExt;
import org.dase.ecii.exceptions.MalFormedIRIException;
import org.dase.ecii.util.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main class to initiate the induction process
 */
public class Main_Residue {

    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static OWLOntology ontology;
    //private static OWLOntologyManager manager;
    //private static OWLDataFactory dataFacotry;
    private static OWLReasoner owlReasoner;
    private static PrintStream outPutStream;
    private static Monitor monitor;

    private static JTextPane jTextPane;

    public static void setTextPane(JTextPane textPane) {
        jTextPane = textPane;
    }


    /**
     * Initiate the variables by using namespace from loaded ontology.
     * Must be called after loading ontology.
     */
    public static void init() {
        // make sure ontology is loaded before init.
        if (null != ontology) {
            SharedDataHolder.owlOntology = ontology;
            SharedDataHolder.owlOntologyManager = ontology.getOWLOntologyManager();
            SharedDataHolder.owlDataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();


            IRI objectPropIri = IRI.create(ConfigParams.namespace, "imageContains");
            OWLObjectProperty imgContains = SharedDataHolder.owlDataFactory.getOWLObjectProperty(objectPropIri);
            // SharedDataHolder.objPropImageContains = imgContains;
            SharedDataHolder.dlSyntaxRendererExt = new DLSyntaxRendererExt();
        } else {
            logger.error("init called before ontology loading.");
            logger.error("program exiting");
            monitor.stopSystem("", true);
        }
    }


    /**
     * clean the shared data holders.
     * should be called before starting the each induction operation.
     */
    private static void cleanSharedDataHolder() {

        SharedDataHolder.objProperties.clear();
        SharedDataHolder.posIndivs.clear();
        SharedDataHolder.negIndivs.clear();

        SharedDataHolder.objectsInPosIndivs.clear();
        SharedDataHolder.objectsInNegIndivs.clear();

        SharedDataHolder.typeOfObjectsInPosIndivs = new HashMap<>();
        SharedDataHolder.typeOfObjectsInNegIndivs = new HashMap<>();


        SharedDataHolder.individualHasObjectTypes = new HashMap<>();

        // public static OWLConceptHierarchy owlConceptHierarchy;
        // owlClassExpressionTrees;

        SharedDataHolder.CandidateSolutionSet.clear();
        // HashMap<Solution:solution,Boolean:shouldTraverse> SolutionsMap
        SharedDataHolder.SortedCandidateSolutionSet.clear();
    }


    /**
     * Initiate the outputpath, logger path, monitor etc and call doOps().
     *
     * @param outputResultPath
     */
    private static void initiateSingleDoOps(String outputResultPath) {

        try {
            // file to write
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputResultPath));
            PrintStream printStream = new PrintStream(bos, true);
            outPutStream = printStream;

            jTextPane = new JTextPane();
            monitor = new Monitor(outPutStream, jTextPane);
            monitor.start("Program started.............", true);
            logger.info("Program started................");
            doOps();

            monitor.displayMessage("Result saved at: " + ConfigParams.outputResultPath, true);
            monitor.stop(System.lineSeparator() + "Program finished.", true);
            logger.info("Program finished.");

            outPutStream.close();
        } catch (Exception e) {
            logger.info("\n\n!!!!!!!Fatal error!!!!!!!\n" + Utility.getStackTraceAsString(e));
            if (null != monitor) {
                monitor.stopSystem("\n\n!!!!!!!Fatal error!!!!!!!\n" + Utility.getStackTraceAsString(e), true);
            } else {
                System.exit(0);
            }
        }
    }


    /**
     * Start the single induction process.
     *
     * @throws OWLOntologyCreationException
     * @throws IOException
     */
    private static void doOps() throws OWLOntologyCreationException, IOException, MalFormedIRIException {

        logger.info("Working with confFile: " + ConfigParams.confFilePath);
        monitor.writeMessage("Working with confFile: " + Paths.get(ConfigParams.confFilePath).getFileName());
        // load ontotology
        ontology = Utility.loadOntology(ConfigParams.ontoPath);
        //loadOntology();

        //init variables
        init();

        // algorithm starting time here.
        DateFormat dateFormat = Utility.getDateTimeFormat();
        Long algoStartTime = System.currentTimeMillis();
        monitor.displayMessage("Algorithm starts at: " + dateFormat.format(new Date()), true);

        // initiate reasoner
        owlReasoner = Utility.initReasoner(ConfigParams.reasonerName, ontology, monitor);

        SharedDataHolder.posIndivs = Utility.readPosExamplesFromConf(SharedDataHolder.confFileFullContent);
        SharedDataHolder.negIndivs = Utility.readNegExamplesFromConf(SharedDataHolder.confFileFullContent);

        // write user defined values to resultFile
        monitor.writeMessage("\nUser defined parameters:");
        monitor.writeMessage("K1/negExprTypeLimit: " + ConfigParams.conceptLimitInNegExpr);
        monitor.writeMessage("K2/hornClauseLimit: " + ConfigParams.hornClauseLimit);
        monitor.writeMessage("K3/objPropsCombinationLimit: " + ConfigParams.objPropsCombinationLimit);
        monitor.writeMessage("K4/hornClausesListMaxSize: " + ConfigParams.hornClausesListMaxSize);
        monitor.writeMessage("K5/candidateClassesListMaxSize: " + ConfigParams.candidateClassesListMaxSize);
        monitor.writeMessage("ReasonerName: " + ConfigParams.reasonerName);

        logger.info("posIndivs from conf:");
        monitor.writeMessage("posIndivs from conf:");
        SharedDataHolder.posIndivs.forEach(owlNamedIndividual -> {
            logger.info("\t" + Utility.getShortName(owlNamedIndividual));
            monitor.writeMessage("\t" + Utility.getShortName(owlNamedIndividual));
        });

        logger.info("negIndivs from conf:");
        monitor.writeMessage("negIndivs from conf:");
        SharedDataHolder.negIndivs.forEach(owlNamedIndividual -> {
            logger.info("\t" + Utility.getShortName(owlNamedIndividual));
            monitor.writeMessage("\t" + Utility.getShortName(owlNamedIndividual));
        });

        // Create a new ConceptFinder object with the given reasoner.
        CandidateSolutionFinderV1 findConceptsObj = new CandidateSolutionFinderV1(owlReasoner, ontology, outPutStream, monitor);
//        ConceptFinderComplex findConceptsObj = new ConceptFinderComplex(owlReasoner, ontology, outPutStream, monitor);

        logger.info("finding solutions started...............");
        // SharedDataHolder.objPropImageContains,
        findConceptsObj.findConcepts(0, 0);
        //findConceptsObj.findConcepts(ConfigParams.tolerance, SharedDataHolder.objPropImageContains, ConfigParams.conceptsCombinationLimit);
        logger.info("\nfinding solutions finished.");

        logger.info("sorting solutions................");
        findConceptsObj.sortSolutionsCustom(false);
        //findConceptsObj.sortSolutions(false);
        logger.info("sorting solutions finished.");

        logger.info("calculating accuracy using reasoner for top k6 solutions................");
        int K6 = 6;
        findConceptsObj.calculateAccuracyOfTopK6ByReasoner(K6);
        logger.info("calculating accuracy using reasoner for top k6 solutions................");

        Long algoEndTime = System.currentTimeMillis();
        monitor.displayMessage("\nAlgorithm ends at: " + dateFormat.format(new Date()), true);
        logger.info("Algorithm ends at: " + dateFormat.format(new Date()), true);
        monitor.displayMessage("\nAlgorithm duration: " + (algoEndTime - algoStartTime) / 1000.0 + " sec", true);
        logger.info("Algorithm duration: " + (algoEndTime - algoStartTime) / 1000.0 + " sec", true);

        logger.info("printing solutions started...............");
        findConceptsObj.printSolutions(K6);
        logger.info("printing solutions finished.");
    }

    private static HashSet<OWLClass> extractObjectTypes(OWLNamedIndividual owlIndividual, OWLObjectProperty owlObjectProperty) {
        logger.info("Given obj property: " + Utility.getShortName(owlObjectProperty));

        HashSet<OWLClass> types = new HashSet<>();

        // find the indivs and corresponding types of indivs which appeared in the positive images
        // bare type/direct type
        if (owlObjectProperty.equals(SharedDataHolder.noneOWLObjProp)) {
            //for no object property or direct types we used SharedDataHolder.noneOWLObjProp
            logger.info("Below concepts are type/supertype of positive " + owlIndividual.getIRI().toString() + " individual.");
            logger.info("object count: " + owlReasoner.getTypes(owlIndividual, false).getFlattened().size());
            owlReasoner.getTypes(owlIndividual, false).getFlattened().forEach(eachType -> {
                logger.info("eachType: " + eachType.toString());
                if (!eachType.equals(SharedDataHolder.owlDataFactory.getOWLThing()) && !eachType.equals(SharedDataHolder.owlDataFactory.getOWLNothing())) {
                    // add it
                    types.add(eachType);
                }
            });
        } else {

            logger.info("Below concepts are type/supertype of positive " + owlIndividual.getIRI().toString() + " individual through objProp " + owlObjectProperty.getIRI());
            logger.info("object count: " + owlReasoner.getObjectPropertyValues(owlIndividual, owlObjectProperty).getFlattened().size());
            owlReasoner.getObjectPropertyValues(owlIndividual, owlObjectProperty).getFlattened().forEach(eachIndi -> {
                logger.debug("\tindi: " + eachIndi.getIRI());

                owlReasoner.getTypes(eachIndi, false).getFlattened().forEach(eachType -> {
                    logger.info("eachType: " + eachType.toString());
                    if (!eachType.equals(SharedDataHolder.owlDataFactory.getOWLThing()) && !eachType.equals(SharedDataHolder.owlDataFactory.getOWLNothing())) {
                        // add it
                        types.add(eachType);
                    }
                });
            });
        }
        return types;
    }


    /**
     * Find similarity score of a new IFP with respect to the solutions.
     * Steps: Find all the types in that IFP.
     * If all types are subsumed by a solution then add that solution. Calculate each concept at a time, dont make new concpet by: concept1 and concept 2
     */
    static double max_precision = 0.0;

    private static void findSimilarity() throws IOException {

        logger.info("Finding similarity started...............");
        String ifps_path = "/Users/sarker/Workspaces/Jetbrains/residue/experiments/KG-based similarity/IFP_Categories/ifps_to_compare.txt";

        FileReader fileReader = new FileReader(ifps_path);
        BufferedReader bf = new BufferedReader(fileReader);

        String[] strs = bf.readLine().split(",");

        HashSet<OWLNamedIndividual> owlNamedIndividualHashSet = new HashSet<>();

        for (String str : strs) {
            IRI iri = IRI.create(str);
            OWLNamedIndividual owlNamedIndividual = SharedDataHolder.owlDataFactory.getOWLNamedIndividual(iri);
            owlNamedIndividualHashSet.add(owlNamedIndividual);
        }

        for (CandidateSolutionV1 candidateSolutionV1 : SharedDataHolder.SortedCandidateSolutionSetV1) {
            if (candidateSolutionV1.getScore().getPrecision() > max_precision) {
                max_precision = candidateSolutionV1.getScore().getPrecision();
            }
        }

        ArrayList<CandidateSolutionV1> solutions_with_max_accuracy = new ArrayList<>(SharedDataHolder.SortedCandidateSolutionSetV1.stream().filter(candidateSolutionV1 -> candidateSolutionV1.getScore().getPrecision() == max_precision).collect(Collectors.toList()));

        for (OWLNamedIndividual individual : owlNamedIndividualHashSet) {

//            HashSet<OWLClass> targetTypes = new HashSet<>();
//            for (Map.Entry<OWLObjectProperty, Double> entry : SharedDataHolder.objProperties.entrySet()) {
//                logger.debug("Extracting objectTypes using objectProperty: " + Utility.getShortName(entry.getKey()));
//                targetTypes = extractObjectTypes(individual, entry.getKey());
//            }

            logger.info("started looking subsumed.........");
            double accuracy_total = 0;
            double similarity = 0;
            for (CandidateSolutionV1 candidateSolutionV1 : solutions_with_max_accuracy) {
                logger.info("started looking subsumed for " + candidateSolutionV1.getSolutionAsString(true));
                boolean contained = false;
//                for (OWLClass owlClass : targetTypes) {
                HashSet<OWLNamedIndividual> containedIndivs = new HashSet<>(owlReasoner.getInstances(
                        candidateSolutionV1.getSolutionAsOWLClassExpression(), false).getFlattened());
//                logger.info("total invivs subsumed: " + containedIndivs.size());
//                containedIndivs.forEach(owlNamedIndividual -> {
//                    logger.info("sumsumed: " + owlNamedIndividual);
//                });
                if (containedIndivs.contains(individual)) {
                    contained = true;
                } else {
                    contained = false;
                    break;
                }
//                }
                if (contained) {
                    logger.info("Found a matching type");
                    accuracy_total += candidateSolutionV1.getScore().getPrecision();
                }
            }
            logger.info("accuracy_total: " + accuracy_total);
            similarity = accuracy_total / SharedDataHolder.SortedCandidateSolutionSetV1.size();

            logger.info("similarity: " + similarity);
            if (accuracy_total > 0)
                break;
        }


//        System.out.println(bf.readLine().split(",")[0]);
    }


    public static void printHelp() {
        String helpCommand = "Program runs in two mode. " +
                "\n\tBatch mode and " +
                "\n\tsingle mode. " +
                "\nIn single mode it will take a config file as input parameter and run the program as mentioned by the parameters in config file.\n" +
                "    \n" +
                "    In Batch mode it take directory as parameter and will run all the config files within that directory.\n" +
                "Command: \n" +
                "    For single mode: [config_file_path]\n" +
                "\n" +
                "    For Batch mode:  [-b directory_path]" +
                "\n" +
                "    For Help: [-h]" +
                "\n\n" +
                "Example:\n" +
                "    For single mode:\n" +
                "        java -jar ecii.jar config_file\n" +
                "\n" +
                "    For Batch mode:\n" +
                "        java -jar ecii.jar -b directory";

        String helpCommandParameter = "";

        System.out.println(helpCommand);

    }


    /**
     * Process the configurations of the program.
     */
    private static void processConfigurations() {

    }


    /**
     * @param args
     * @throws OWLOntologyCreationException
     * @throws IOException
     */
    public static void main(String[] args) throws OWLOntologyCreationException, IOException, MalFormedIRIException {

        PropertyConfigurator.configure("/Users/sarker/Workspaces/Jetbrains/residue/java/residue_java_v1/src/main/resources/log4j.properties");


        SharedDataHolder.programStartingDir = System.getProperty("user.dir");
        logger.info("Working directory/Program starting directory = " + SharedDataHolder.programStartingDir);
        logger.debug("args.length: " + args.length);

        String config_path = "/Users/sarker/Workspaces/Jetbrains/residue/experiments/KG-based similarity/IFP_Categories/Experiment_2_posExamples_265/Experiment_2_posExamples_265.config";

        ConfigParams.batch = false;

        ConfigParams.parseConfigParams(config_path);
        System.out.println("parsing okay");
        initiateSingleDoOps(ConfigParams.outputResultPath);
        findSimilarity();
    }
}
