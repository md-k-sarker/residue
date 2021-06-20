package org.dase;


import org.apache.log4j.PropertyConfigurator;
import org.dase.ecii.core.CandidateSolutionFinder;
import org.dase.ecii.core.CandidateSolutionFinderV1;
import org.dase.ecii.core.CandidateSolutionFinderV2;
import org.dase.ecii.core.SharedDataHolder;
import org.dase.ecii.datastructure.CandidateSolutionV1;
import org.dase.ecii.exceptions.MalFormedIRIException;
import org.dase.ecii.ontofactory.DLSyntaxRendererExt;
import org.dase.ecii.util.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.lang.invoke.MethodHandles;
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

    static double max_precision = 0.0;

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
     * Initiate the outputpath, logger path, monitor etc and call doECIIOps().
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
            doECIIOps();

            // find similary
            // findSimilarity();

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

    static Long algoStartTime;
    static Long algoEndTime;

    /**
     * Start the single induction process.
     *
     * @throws OWLOntologyCreationException
     * @throws IOException
     */
    private static void doECIIOps() throws OWLOntologyCreationException, IOException, MalFormedIRIException {

        logger.info("Working with confFile: " + ConfigParams.confFilePath);
        monitor.writeMessage("Working with confFile: " + Paths.get(ConfigParams.confFilePath).getFileName());
        // load ontotology
        ontology = Utility.loadOntology(ConfigParams.ontoPath);
        //loadOntology();

        //init variables
        init();

        // algorithm starting time here.
        DateFormat dateFormat = Utility.getDateTimeFormat();
        algoStartTime = System.currentTimeMillis();
        monitor.displayMessage("Algorithm starts at: " + dateFormat.format(new Date()), true);

        // initiate reasoner
        owlReasoner = Utility.initReasoner(ConfigParams.reasonerName, ontology, monitor);

        SharedDataHolder.posIndivs = Utility.readPosExamplesFromConf(SharedDataHolder.confFileFullContent, "#");

        SharedDataHolder.negIndivs = Utility.readNegExamplesFromConf(SharedDataHolder.confFileFullContent, "#");

        // write user defined values to resultFile
        monitor.writeMessage("\nUser defined parameters:");
        monitor.writeMessage("Remove common types: " + ConfigParams.removeCommonTypes);
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
        CandidateSolutionFinder findConceptsObj = new CandidateSolutionFinderV2(owlReasoner, ontology, outPutStream, monitor);
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
        int K6 = 6;
//        findConceptsObj.calculateAccuracyOfTopK6ByReasoner(K6);
//        logger.info("calculating accuracy using reasoner for top k6 solutions................");

        algoEndTime = System.currentTimeMillis();
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


    private static void initLogSettings() {

    }

    /**
     * @param args
     * @throws OWLOntologyCreationException
     * @throws IOException
     */
    public static void main(String[] args) throws OWLOntologyCreationException, IOException, MalFormedIRIException {

        try {
            PropertyConfigurator.configure("/Users/sarker/Workspaces/Jetbrains/residue/java/residue_java_v1/src/main/resources/log4j.properties");


            SharedDataHolder.programStartingDir = System.getProperty("user.dir");
            logger.info("Working directory/Program starting directory = " + SharedDataHolder.programStartingDir);
            logger.debug("args.length: " + args.length);

            String config_path = "/Users/sarker/Workspaces/Jetbrains/emerald/experiments/ade20k-sumo/kitchen_vs_non-kitchen_fn_vs_tp/kitchen_vs_non-kitchen_fn_vs_tp.config";

            ConfigParams.batch = false;
            System.out.println("encoding: "+System.getProperty("file.encoding"));
//            OWLOntology ontology = Utility.loadOntology("/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/automated_wiki/wiki_full_pages_v0_non_cyclic_jan_20_32808131.rdf");

//            ConfigParams.parseConfigParams(config_path);
//            initiateSingleDoOps(ConfigParams.outputResultPath);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
