package org.dase;


import org.apache.log4j.PropertyConfigurator;
import org.dase.ecii.core.CandidateSolutionFinderV1;
import org.dase.ecii.core.SharedDataHolder;
import org.dase.ecii.core.CandidateSolutionFinder;
import org.dase.ecii.core.SharedDataHolder;
import org.dase.ecii.exceptions.MalFormedIRIException;
import org.dase.ecii.ontofactory.DLSyntaxRendererExt;
import org.dase.ecii.exceptions.MalFormedIRIException;
import org.dase.ecii.util.*;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Main class to initiate the induction process
 */
public class Main_ECII {

    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static OWLOntology ontology;
    //private static OWLOntologyManager manager;
    //private static OWLDataFactory dataFacotry;
    private static OWLReasoner owlReasoner;
    private static PrintStream outPutStream;
    private static Monitor monitor;

    static String alreadyGotResultPath = "/home/sarker/MegaCloud/ProjectHCBD/experiments/ade_with_wn_sumo/automated/without_score_got_result/";
    static ArrayList<String> alreadyGotResult = new ArrayList<String>();

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
            SharedDataHolder.owlOntologyOriginal = ontology;
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

        SharedDataHolder.CandidateSolutionSetV2.clear();
        // HashMap<Solution:solution,Boolean:shouldTraverse> SolutionsMap
        SharedDataHolder.SortedCandidateSolutionListV2.clear();
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

        SharedDataHolder.posIndivs = Utility.readPosExamplesFromConf(SharedDataHolder.confFileFullContent, "#");
        SharedDataHolder.negIndivs = Utility.readNegExamplesFromConf(SharedDataHolder.confFileFullContent, "#");

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

//        logger.info("sorting solutions................");
//        findConceptsObj.sortSolutionsCustom(false);
//        //findConceptsObj.sortSolutions(false);
//        logger.info("sorting solutions finished.");

//        logger.info("calculating accuracy using reasoner for top k6 solutions................");
//        int K6 = 6;
//        findConceptsObj.calculateAccuracyOfTopK6ByReasoner(K6);
//        logger.info("calculating accuracy using reasoner for top k6 solutions................");

        Long algoEndTime = System.currentTimeMillis();
        monitor.displayMessage("\nAlgorithm ends at: " + dateFormat.format(new Date()), true);
        logger.info("Algorithm ends at: " + dateFormat.format(new Date()), true);
        monitor.displayMessage("\nAlgorithm duration: " + (algoEndTime - algoStartTime) / 1000.0 + " sec", true);
        logger.info("Algorithm duration: " + (algoEndTime - algoStartTime) / 1000.0 + " sec", true);

        logger.info("printing solutions started...............");
        findConceptsObj.printSolutions(0);
        logger.info("printing solutions finished.");
    }

    private static void processBatchRunning(String dirPath) {
        processBatchRunning(Paths.get(dirPath));
    }

    /**
     * Iterate over the folders and call initiateSingleDoOps() for each confFile.
     *
     * @param dirPath
     */
    private static void processBatchRunning(Path dirPath) {

        try {
            // iterate over the files of a the folder
            Files.walk(dirPath).filter(f -> f.toFile().isFile()).filter(f -> f.toFile().getAbsolutePath().endsWith(".config")).forEach(f -> {
                // will get each file
                if (alreadyGotResult.contains(f.toFile().getName())) {
                    logger.info(f.toString() + " already has result, not running it.");
                } else {
                    logger.info(" Program running for config file: " + f.toString());

                    // parse the config file
                    cleanSharedDataHolder();
                    ConfigParams.parseConfigParams(f.toString());
                    initiateSingleDoOps(ConfigParams.outputResultPath);
                }
            });
        } catch (Exception e) {
            logger.error("\n\n!!!!!!!Fatal error!!!!!!!\n" + Utility.getStackTraceAsString(e));
            if (null != monitor) {
                monitor.stopSystem("\n\n!!!!!!!Fatal error!!!!!!!\n" + Utility.getStackTraceAsString(e), true);
            } else {
                System.exit(0);
            }
        }

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

    }
}
