///*
//Written by sarker.
//Written at 9/26/19.
//*/
//
//import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
//import org.apache.log4j.Level;
//import org.dase.core.SharedDataHolder;
//import org.dase.datastructure.CandidateSolutionV1;
//import org.dase.parser.dl_syntax_owlapi.DLSyntaxParser;
//import org.dase.parser.javacc.SimpleParser_v2;
//import org.dase.parser.javacc.SimpleParser_v3;
//import org.dase.util.Utility;
//import org.semanticweb.owlapi.apibinding.OWLManager;
//import org.semanticweb.owlapi.model.*;
//import org.semanticweb.owlapi.reasoner.*;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//
//public class TestParser_Simple_v3 {
//
//    public static void main(String[] args) throws OWLOntologyCreationException, IOException {
//
////        OWLOntology ontology = Utility.loadOntology("/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/dbpedia_2016-10.owl");
////
////        OWLDocumentFormat format = ontology.getOWLOntologyManager().getOntologyFormat(ontology);
////
////        Map<String, String> prefixes = format.asPrefixOWLOntologyFormat().getPrefixName2PrefixMap();
////        prefixes.forEach((e1,e2) -> {
////            System.out.println(e1 + e2);
////        });
//
//        // R.( (A1 ⊓ ¬(D1) ) )
////        String str = " exists R1. ((WN_Ceiling  AND  not (Baby) ))";
//        String testCase1 = "A ⊓ B  ";
//        String testCase2 = "(A ⊓ B )" ;
//        String testCase3 = "((A ⊓ B))" ;
//        String testCase7 = "(A ⊓ B ) ⊓ R2.( (A1 ⊓ ¬(D1)) ⊔ (A2 ⊓ ¬(D1)) )";
//
//        String currentTestCase = testCase3;
//        //"Trees and river";  got solution: ((Trees) ⊔ (river)), not allowed
//        // "(Trees or river)"; got solution: (Trees or river )
//        // "(River or People or Animal)"; got solution: (River or People or Animal)
//        // " (WN_Ceiling or Animal or not Baby)" got solution: ((WN_Ceiling) ⊔ (Animal) ⊔ ( ¬ Baby))
//        // "exists imageContains.((WN_Ceiling) ⊔ ( ¬ Region) ⊔ (Process))" got ParseException: Encountered " "(" "( "" at line 1, column 22. //Was expecting://    <ID> ...
//        // exists imageContains.WN_Ceiling got ∃imageContains.(WN_Ceiling)
//        // exists imageContains . (WN_Ceiling or Animal or Baby) got ∃imageContains.((WN_Ceiling) ⊔ (Animal) ⊔ (Baby))
//        // exists imageContains.(WN_Ceiling) got ParseException: Encountered " "(" "( "" at line 1, column 22. //Was expecting://    <ID> ...
//        // "exists imageContains. not WN_Ceiling"; got ParseException: Encountered " "(" "( "" at line 1, column 22. //Was expecting://    <ID> ...
//
//
//        // owldatafactory
//        OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
//        OWLDataFactory owlDataFactory = owlOntologyManager.getOWLDataFactory();
//
//        //owlontology
//
//        OWLOntology ontology = Utility.loadOntology("/Users/sarker/Workspaces/Jetbrains/residue/java/residue_java_v1/src/test/resources/concept_or_individual.owl");
//
//        // owlreasoner
//        OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
//        // change log level to WARN for Pellet, because otherwise log
//        // output will be very large
//        org.apache.log4j.Logger pelletLogger = org.apache.log4j.Logger.getLogger("org.mindswap.pellet");
//        pelletLogger.setLevel(Level.WARN);
//        ReasonerProgressMonitor progressMonitor = new NullReasonerProgressMonitor();
//        FreshEntityPolicy freshEntityPolicy = FreshEntityPolicy.ALLOW;
//        long timeOut = Integer.MAX_VALUE;
//        IndividualNodeSetPolicy individualNodeSetPolicy = IndividualNodeSetPolicy.BY_NAME;
//        OWLReasonerConfiguration reasonerConfig = new SimpleConfiguration(progressMonitor, freshEntityPolicy, timeOut, individualNodeSetPolicy);
//        OWLReasoner owlReasoner = reasonerFactory.createNonBufferingReasoner(ontology, reasonerConfig);
//
//        // parser object
//        InputStream stream = new ByteArrayInputStream(currentTestCase.getBytes(StandardCharsets.UTF_8));
//        // our parser
//        SimpleParser_v3 parser = new SimpleParser_v3(stream);
//        parser.setDefaultNamespace("empty");
//        parser.initiate(owlDataFactory, ontology, owlReasoner);
//        SharedDataHolder.owlDataFactory = owlDataFactory;
//
//        // dlsyntax perser
//        DLSyntaxParser dlSyntaxParser = new DLSyntaxParser(currentTestCase);
//        dlSyntaxParser.setOWLDataFactory(owlDataFactory);
//        dlSyntaxParser.setDefaultNamespace("empty");
//
//        CandidateSolutionV1 candidateSolution = null;
//        try {
//            // call parser
//            candidateSolution = parser.parseCandidateSolution();
//            System.out.println("ecii_string: " + candidateSolution.getSolutionAsString());
//        } catch (Exception ex) {
//            System.out.println("ecii parser: error");
//            ex.printStackTrace();
//        }
//        try {
//            System.out.println("ecii_reasoner: " + candidateSolution.getSolutionAsOWLClassExpression());
//        } catch (Exception ex) {
//            System.out.println("ecii reasoner error");
//            ex.printStackTrace();
//        }
//        try {
//            System.out.println("dlsyntax: " + dlSyntaxParser.parseDescription().toString());
//        } catch (Exception ex) {
//            System.out.println("dlsyntax: error");
//            ex.printStackTrace();
//        }
//
//    }
//}
//
