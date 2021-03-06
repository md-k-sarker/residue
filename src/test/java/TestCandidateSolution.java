///*
//Written by sarker.
//Written at 10/6/19.
//*/
//
//import org.dase.ecii.datastructure.CandidateClass;
//import org.dase.ecii.datastructure.CandidateSolutionV2;
//import org.dase.ecii.datastructure.ConjunctiveHornClause;
//import org.semanticweb.owlapi.apibinding.OWLManager;
//import org.semanticweb.owlapi.model.*;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//
//public class TestCandidateSolutionV2 {
//
//    public static void main(String []args) throws OWLOntologyCreationException, IOException {
//
//        String str = "exists imageContains. not WN_Ceiling";
//        //"Trees and river";  got solution: ((Trees) ⊔ (river))
//        // "(Trees or river)"; got solution: (river)
//        // "(Trees or river or animal)"; got solution: (animal)
//        // "exists imageContains.((WN_Ceiling) ⊔ ( ¬ Region) ⊔ (Process))" got ParseException: Encountered " "(" "( "" at line 1, column 22. //Was expecting://    <ID> ...
//        // exists imageContains.WN_Ceiling got ∃imageContains.(WN_Ceiling)
//        // exists imageContains.(WN_Ceiling) got ParseException: Encountered " "(" "( "" at line 1, column 22. //Was expecting://    <ID> ...
//        // "exists imageContains. not WN_Ceiling"; got ParseException: Encountered " "(" "( "" at line 1, column 22. //Was expecting://    <ID> ...
//
//        InputStream stream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
////        SimpleParser_new parser = new SimpleParser_new(stream);
////        parser.setDefaultNamespace("empty");
//
//        OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
//        OWLDataFactory owlDataFactory = owlOntologyManager.getOWLDataFactory();
////        parser.setOWLDataFactory(owlDataFactory);
//
//        try {
//
////            CandidateSolutionV2 CandidateSolutionV2 =  parser.parseCandidateSolutionV2();
//            OWLClassExpression owlClassExpression;
//            OWLClass owlClass;
//            OWLObjectProperty owlObjectProperty1 = owlDataFactory.getOWLObjectProperty(IRI.create("objProp1") );
//            OWLObjectProperty owlObjectProperty2 = owlDataFactory.getOWLObjectProperty(IRI.create("objProp2") );
//            CandidateSolutionV2 CandidateSolutionV2 =new CandidateSolutionV2();
//
//            CandidateClass candidateClass1 = new CandidateClass(owlObjectProperty1);
//            ConjunctiveHornClause conjunctiveHornClause1 = new ConjunctiveHornClause(owlObjectProperty1);
//            conjunctiveHornClause1.setPosObjectType(owlDataFactory.getOWLClass(IRI.create("posClass1")));
//            candidateClass1.addConjunctiveHornClauses(conjunctiveHornClause1);
//
//            CandidateClass candidateClass2 = new CandidateClass(owlObjectProperty1);
//            ConjunctiveHornClause conjunctiveHornClause2 = new ConjunctiveHornClause(owlObjectProperty2);
//            conjunctiveHornClause2.setPosObjectType(owlDataFactory.getOWLClass(IRI.create("posClass2")));
//            candidateClass2.addConjunctiveHornClauses(conjunctiveHornClause2);
//
//            CandidateSolutionV2.addCandidateClass(candidateClass1);
//            CandidateSolutionV2.addCandidateClass(candidateClass2);
//
//            System.out.println(CandidateSolutionV2.getSolutionAsString());
//
//        } catch (Exception  e) {
//            e.printStackTrace();
//        }
//
//
//    }
//}
