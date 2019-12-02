/*
Written by sarker.
Written at 9/26/19.
*/

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import org.apache.log4j.Level;
import org.dase.ecii.core.SharedDataHolder;
import org.dase.ecii.datastructure.CandidateSolutionV1;
import org.dase.parser.dl_syntax_owlapi.DLSyntaxParser;
import org.dase.parser.javacc.SimpleParserJavacc_v1;
import org.dase.parser.javacc.SimpleParserJavacc_v1;
import org.dase.ecii.util.Utility;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

//import org.antlr.v4.runtime.CharStreams;

public class TestParserJavacc_v1 {

    public static void main(String[] args) throws OWLOntologyCreationException, IOException {

//        OWLOntology ontology = Utility.loadOntology("/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/dbpedia_2016-10.owl");
//
//        OWLDocumentFormat format = ontology.getOWLOntologyManager().getOntologyFormat(ontology);
//
//        Map<String, String> prefixes = format.asPrefixOWLOntologyFormat().getPrefixName2PrefixMap();
//        prefixes.forEach((e1,e2) -> {
//            System.out.println(e1 + e2);
//        });



        ArrayList<String> testCases = new ArrayList<>();

        // R.( (A1 ⊓ ¬(D1) ) )
//        String str = " exists R1. ((WN_Ceiling  AND  not (Baby) ))";
        String testCase0 = "A";
        String testCase1 = "(A)";   // okay
        String testCase2 = "A ⊓ B";
        String testCase3 = "(A ⊓ B )" ;   // okay
        String testCase4 = "((A ⊓ B))" ;   // okay
        String testCase5 = "exists R1.((A ⊓ B))" ;  // okay
        String testCase6 = "exists R1.((A ⊓ B) ⊓ ¬(D1))" ;  // okay
        String testCase7 = "(A ⊓ B ) ⊓ exists R2. ((A1) ⊓ ¬(D1))  ";  // okay
        String testCase8 = "(A ⊓ B ) ⊓ exists R2.( (A1) ⊓ ¬(D1)  )";  // okay
        String testCase9 = "(A ⊓ B ) ⊓ exists R2.( (A1) ⊓ ¬(D1)) ⊔ ((A2) ⊓ ¬(D2) )";  //  okay
        String testCase10 = "(A ⊓ B ) ⊓ exists R2.( ((A1) ⊓ ¬(D1))  )";  // extraneous input '(' expecting IDD
        String testCase11 = "(A ⊓ B ) ⊓ exists R2.( ((A1) ⊓ ¬(D1)) ⊔ ((A2) ⊓ ¬(D2)) )";  //  extraneous input '(' expecting IDD
        String testCase12 = "(A ⊓ B ) ⊓ exists R2.( (A1 ⊓ ¬(D1)) ⊔ (A2 ⊓ ¬(D2)) )";  //  extraneous input '(' expecting IDD
        String testCase13 = "(A ⊓ B ) ⊓ exists R2.( ((A1) ⊓ ¬(D1)) ⊔ ((A2) ⊓ ¬(D2)) )  ⊓ exists R3.( (A1 ⊓ ¬(D1 ⊔ D2 ⊔ D3)) ⊔ (A2 ⊓ ¬(D2)) )";  //  extraneous input '(' expecting IDD
        String testCase14 = "(A ⊓ B ) ⊓ exists R2.( ((A1) ⊓ ¬(D1)) ⊔ ((A2) ⊓ ¬(D2)) )";  //  extraneous input '(' expecting IDD

        testCases.add(testCase0);
        testCases.add(testCase1);
        testCases.add(testCase2);
        testCases.add(testCase3);
        testCases.add(testCase4);
        testCases.add(testCase5);
        testCases.add(testCase6);
        testCases.add(testCase7);
        testCases.add(testCase8);
        testCases.add(testCase9);
        testCases.add(testCase10);
        testCases.add(testCase11);
        testCases.add(testCase12);
        testCases.add(testCase13);
        testCases.add(testCase14);

        String currentTestCase = testCase9;
        //"Trees and river";  got solution: ((Trees) ⊔ (river)), not allowed
        // "(Trees or river)"; got solution: (Trees or river )
        // "(River or People or Animal)"; got solution: (River or People or Animal)
        // " (WN_Ceiling or Animal or not Baby)" got solution: ((WN_Ceiling) ⊔ (Animal) ⊔ ( ¬ Baby))
        // "exists imageContains.((WN_Ceiling) ⊔ ( ¬ Region) ⊔ (Process))" got ParseException: Encountered " "(" "( "" at line 1, column 22. //Was expecting://    <ID> ...
        // exists imageContains.WN_Ceiling got ∃imageContains.(WN_Ceiling)
        // exists imageContains . (WN_Ceiling or Animal or Baby) got ∃imageContains.((WN_Ceiling) ⊔ (Animal) ⊔ (Baby))
        // exists imageContains.(WN_Ceiling) got ParseException: Encountered " "(" "( "" at line 1, column 22. //Was expecting://    <ID> ...
        // "exists imageContains. not WN_Ceiling"; got ParseException: Encountered " "(" "( "" at line 1, column 22. //Was expecting://    <ID> ...


        // owldatafactory
        OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
        OWLDataFactory owlDataFactory = owlOntologyManager.getOWLDataFactory();

        //owlontology

        OWLOntology ontology = Utility.loadOntology("/Users/sarker/Workspaces/Jetbrains/residue/java/residue_java_v1/src/test/resources/concept_or_individual.owl");

        // owlreasoner
        OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
        // change log level to WARN for Pellet, because otherwise log
        // output will be very large
        org.apache.log4j.Logger pelletLogger = org.apache.log4j.Logger.getLogger("org.mindswap.pellet");
        pelletLogger.setLevel(Level.WARN);
        ReasonerProgressMonitor progressMonitor = new NullReasonerProgressMonitor();
        FreshEntityPolicy freshEntityPolicy = FreshEntityPolicy.ALLOW;
        long timeOut = Integer.MAX_VALUE;
        IndividualNodeSetPolicy individualNodeSetPolicy = IndividualNodeSetPolicy.BY_NAME;
        OWLReasonerConfiguration reasonerConfig = new SimpleConfiguration(progressMonitor, freshEntityPolicy, timeOut, individualNodeSetPolicy);
        OWLReasoner owlReasoner = reasonerFactory.createNonBufferingReasoner(ontology, reasonerConfig);




        for(int i=0;i<testCases.size();i++){
            try {

                CandidateSolutionV1 candidateSolution = null;

                currentTestCase = testCases.get(i);
                System.out.println("\nTest Case : "+ i + " str: "+ currentTestCase);


                // dlsyntax perser
                DLSyntaxParser dlSyntaxParser = new DLSyntaxParser(currentTestCase);
                dlSyntaxParser.setOWLDataFactory(owlDataFactory);
                dlSyntaxParser.setDefaultNamespace("empty");
                System.out.println("dl_syntax_string: " +dlSyntaxParser.parseDescription().toString());

                // parser object
                InputStream stream = new ByteArrayInputStream(currentTestCase.getBytes(StandardCharsets.UTF_8));
                // our parser
                SimpleParserJavacc_v1 parser = new SimpleParserJavacc_v1(stream);
                parser.setDefaultNamespace("empty");
                parser.initiate(owlDataFactory, ontology, owlReasoner);
                SharedDataHolder.owlDataFactory = owlDataFactory;

                // call parser
                candidateSolution = parser.parseCandidateSolution();
                System.out.println("ecii_string: " + candidateSolution.getSolutionAsString());



            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

    }
}

