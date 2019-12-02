/*
Written by sarker.
Written at 9/26/19.
*/

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.log4j.Level;
import org.dase.core.SharedDataHolder;
import org.dase.datastructure.CandidateClassV1;
import org.dase.datastructure.CandidateSolutionV1;
import org.dase.datastructure.ConjunctiveHornClauseV1;
import org.dase.parser.antlr.simple_parser_v6BaseListener;
import org.dase.parser.antlr.simple_parser_v6BaseVisitorImpl;
import org.dase.parser.antlr.simple_parser_v6Lexer;
import org.dase.parser.antlr.simple_parser_v6Parser;
import org.dase.parser.dl_syntax_owlapi.DLSyntaxParser;
import org.dase.parser.parser_simple.SimpleParser_v3;
import org.dase.util.Utility;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

//import org.antlr.v4.runtime.CharStreams;

public class TestParser_antlr_v6 {

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
        String testCase0 = "A"; // okay
        String testCase1 = "(A)";   // okay
        String testCase2 = "A ⊓ B";  // okay
        String testCase3 = "(A ⊓ B )";   // okay
        String testCase4 = "((A ⊓ B))";   // okay
        String testCase5 = "exists R1.((A ⊓ B))";  // okay
        String testCase6 = "exists R1.((A ⊓ B) ⊓ ¬(D1))";  // okay
        String testCase7 = "(A ⊓ B ) ⊓ exists R2. ((A1) ⊓ ¬(D1))  ";  // okay
        String testCase8 = "(A ⊓ B ) ⊓ exists R2.( (A1) ⊓ ¬(D1)  )";  // okay
        String testCase9 = "(A ⊓ B ) ⊓ exists R2.( (A1) ⊓ ¬(D1)) ⊔ ((A2) ⊓ ¬(D2) )";  //  okay
        String testCase10 = "(A ⊓ B ) ⊓ exists R2.( ((A1) ⊓ ¬(D1))  )";  // okay
        String testCase11 = "(A ⊓ B ) ⊓ exists R2.( ((A1) ⊓ ¬(D1)) ⊔ ((A2) ⊓ ¬(D2)) )";  //  okay
        String testCase12 = "(A ⊓ B ) ⊓ exists R2.( (A1 ⊓ ¬(D1)) ⊔ (A2 ⊓ ¬(D2)) )";  //  okay
        String testCase13 = "(A ⊓ B ) ⊓ exists R2.( ((A1) ⊓ ¬(D1)) ⊔ ((A2) ⊓ ¬(D2)) )  ⊓ exists R3.( (A1 ⊓ ¬(D1 ⊔ D2 ⊔ D3)) ⊔ (A2 ⊓ ¬(D2)) )";  //  okay
        String testCase14 = "(A ⊓ B ) ⊓ exists R2.( ((A1) ⊓ ¬(D1)) ⊔ ((A2) ⊓ ¬(D2)) )";  //  okay
        String testCase15 = "(A ⊓ B ) ⊓ som R2.( ((A1) ⊓ ¬(D1)) ⊔ ((A2) ⊓ ¬(D2)) )";  //  wrong outputs ((A ⊓ B) ⊓ (som))
        String testCase16 = "(A ";  //  okay outputs (A)
        String testCase17 = " B )";  //  okay outputs (B)
        String testCase18 = "";  //  okay outputs empty
        String testCase19 = "(A ⊓ B ) ⊓ ";  // okay mismatched input '<EOF>' expecting {'(', SOME, IDD}
        String testCase20 = "(A ⊓ B ) ⊔ exists R2.( ((A1) ⊓ ¬(D1)) ⊔ ((A2) ⊓ ¬(D2)) )";  //  okay mismatched input '<EOF>' expecting {'(', SOME, IDD}
        String testCase21 = "(A ⊓ B ) ⊓  exists R2.( ((A1) ⊔ ¬(D1)) ⊔ ((A2) ⊓ ¬(D2)) )";  //  wrong outputs (A ⊓ B)
        String testCase22 = "(A ⊓ B ) ⊔ exists R2.( ((A1) ⊓ ¬(D1)) ⊓ ((A2) ⊓ ¬(D2)) )";  //  okay mismatched input '<EOF>' expecting {'(', SOME, IDD}

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
        testCases.add(testCase15);
        testCases.add(testCase16);
        testCases.add(testCase17);
        testCases.add(testCase18);
        testCases.add(testCase19);
        testCases.add(testCase20);
        testCases.add(testCase21);
        testCases.add(testCase22);

        String currentTestCase = testCase5;
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

        // parser object
        InputStream stream = new ByteArrayInputStream(currentTestCase.getBytes(StandardCharsets.UTF_8));
        // our parser
        SimpleParser_v3 parser = new SimpleParser_v3(stream);
        parser.setDefaultNamespace("empty");
        parser.initiate(owlDataFactory, ontology, owlReasoner);
        SharedDataHolder.owlDataFactory = owlDataFactory;

        // dlsyntax perser
        DLSyntaxParser dlSyntaxParser = new DLSyntaxParser(currentTestCase);
        dlSyntaxParser.setOWLDataFactory(owlDataFactory);
        dlSyntaxParser.setDefaultNamespace("empty");

        CandidateSolutionV1 candidateSolution = null;
        for (int i = 0; i < testCases.size(); i++) {
            try {

                currentTestCase = testCases.get(i);
                System.out.println("\nTest Case : " + i + " str: " + currentTestCase);

                // just try to parse and print
                ANTLRInputStream inputStream = new ANTLRInputStream(currentTestCase);
                simple_parser_v6Lexer lexer = new simple_parser_v6Lexer(inputStream);
                CommonTokenStream tokenStream = new CommonTokenStream(lexer);
                simple_parser_v6Parser antparser = new simple_parser_v6Parser(tokenStream);

                CandidateSolutionV1 candidateSolutionV1 = null;
                simple_parser_v6Parser.ParsecandidatesolutionContext tree = antparser.parsecandidatesolution();

                simple_parser_v6BaseVisitorImpl simple_parser_v6BaseVisitor = new simple_parser_v6BaseVisitorImpl(owlDataFactory,ontology,owlReasoner);
                candidateSolutionV1 = (CandidateSolutionV1) simple_parser_v6BaseVisitor.visit(tree);
                System.out.println("solution using ecii: " + candidateSolutionV1.getSolutionAsString());

//                ParseTreeWalker.DEFAULT.walk(new simple_parser_v6BaseListener(), tree);

//                if (tree.groupedcandidateclasses().size() > 0) {
//
//                    System.out.println("txt1: "+ tree.groupedcandidateclasses().get(0).getText());
//                    candidateSolutionV1 = new CandidateSolutionV1(owlReasoner, ontology);
//
//                    // grouped candidate classes
//                    for (simple_parser_v6Parser.GroupedcandidateclassesContext groupedcandidateclassesContext : tree.groupedcandidateclasses()) {
//
//                        System.out.println("txt2: "+ groupedcandidateclassesContext.getText());
//
//                        // candidate classes
//                        String objProp = "hello"; //groupedcandidateclassesContext.parseobjectpropertyid().getText();
//                        OWLObjectProperty owlObjectProperty;
//                        ArrayList<CandidateClassV1> candidateClassV1ArrayList = new ArrayList<>();
//                        if (null == objProp || objProp.length() == 0) {
//                            owlObjectProperty = SharedDataHolder.noneOWLObjProp;
//                        } else {
//                            owlObjectProperty = owlDataFactory.getOWLObjectProperty(IRI.create(objProp));
//                        }
//
//                        for (simple_parser_v6Parser.ParsecandidateclassContext parsecandidateclassContext : groupedcandidateclassesContext.parsecandidateclass()) {
//
//                            System.out.println("txt3: "+ parsecandidateclassContext.getText());
//                            CandidateClassV1 candidateClassV1 = new CandidateClassV1(owlObjectProperty, owlReasoner, ontology);
//
//                            // horn clauses
//                            for (simple_parser_v6Parser.ParseconjunctivehornclauseContext parseconjunctivehornclauseContext : parsecandidateclassContext.parseconjunctivehornclause()) {
//
//                                System.out.println("txt4: "+ parseconjunctivehornclauseContext.getText());
//
//                                ConjunctiveHornClauseV1 hornClauseV1 = new ConjunctiveHornClauseV1(owlObjectProperty, owlReasoner, ontology);
//                                // posclasses
//                                for (simple_parser_v6Parser.ParseclassidContext parseposclassContext : parseconjunctivehornclauseContext.parseposclasses().parseclassid()) {
//                                    OWLClass posClass = owlDataFactory.getOWLClass(IRI.create(parseposclassContext.getText()));
//                                    hornClauseV1.addPosObjectType(posClass);
//                                }
//                                // neg classes
//                                for (simple_parser_v6Parser.ParseclassidContext parsenegclassContext : parseconjunctivehornclauseContext.parsenegclasses().parseclassid()) {
//                                    OWLClass negClass = owlDataFactory.getOWLClass(IRI.create(parsenegclassContext.getText()));
//                                    hornClauseV1.addNegObjectType(negClass);
//                                }
//                                // add
//                                candidateClassV1.addConjunctiveHornClauses(hornClauseV1);
//                            }
//                            // add
//                            candidateClassV1ArrayList.add(candidateClassV1);
//                        }
//                        // add
//                        candidateSolutionV1.addGroupedCandidateClass(owlObjectProperty, candidateClassV1ArrayList);
//                    }
//                }

//                System.out.println("solution from ecii after parsing: "+ candidateSolutionV1.getSolutionAsString());

//                System.out.println("parsed: " + antparser.parsecandidatesolution().getText());
//                simple_parser_v6BaseVisitor asd = new simple_parser_v6BaseVisitor();
//                asd.visitParsecandidatesolution(antparser.parsecandidatesolution());

                // print tree
                inputStream = new ANTLRInputStream(currentTestCase);
                lexer = new simple_parser_v6Lexer(inputStream);
                tokenStream = new CommonTokenStream(lexer);
                antparser = new simple_parser_v6Parser(tokenStream);
                System.out.println("tree \n " + antparser.parsecandidatesolution().toStringTree(antparser));

//              OWLClassExpression owlClassExpression = candidateSolution.getSolutionAsOWLClassExpression();
//                owlClassExpression.getClassExpressionType()

                // gui

                //show AST in GUI
//            JFrame frame = new JFrame("Antlr AST");
//            JPanel panel = new JPanel();
//            TreeViewer viewr = new TreeViewer(Arrays.asList(antparser.getRuleNames()));
//            viewr.setScale(1.5);//scale a little
//            panel.add(viewr);
//            frame.add(panel);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(200,200);
//            frame.setVisible(true);

                // show tree in console
//            ParseTree tree;

//            System.out.println(antparser.parsecandidatesolution().toStringTree());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


    }
}

