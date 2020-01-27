/*
Written by sarker.
Written at 9/26/19.
*/

import org.dase.ecii.util.Utility;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class TestRaw {

    public static void main(String []args) {


//        OWLOntology ontology = Utility.loadOntology("/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/enwiki-20191109-categories.ttl");

//         ontology.getClassesInSignature().forEach(owlClass -> {
//             System.out.println(owlClass);
//         });

//
//        OWLDocumentFormat format = ontology.getOWLOntologyManager().getOntologyFormat(ontology);
//
//        Map<String, String> prefixes = format.asPrefixOWLOntologyFormat().getPrefixName2PrefixMap();
//        prefixes.forEach((e1,e2) -> {
//            System.out.println(e1 + e2);
//        });

//        String str = "exists imageContains. not WN_Ceiling";
        //"Trees and river";  got solution: ((Trees) ⊔ (river))
        // "(Trees or river)"; got solution: (river)
        // "(Trees or river or animal)"; got solution: (animal)
        // "exists imageContains.((WN_Ceiling) ⊔ ( ¬ Region) ⊔ (Process))" got ParseException: Encountered " "(" "( "" at line 1, column 22. //Was expecting://    <ID> ...
        // exists imageContains.WN_Ceiling got ∃imageContains.(WN_Ceiling)
        // exists imageContains.(WN_Ceiling) got ParseException: Encountered " "(" "( "" at line 1, column 22. //Was expecting://    <ID> ...
        // "exists imageContains. not WN_Ceiling"; got ParseException: Encountered " "(" "( "" at line 1, column 22. //Was expecting://    <ID> ...

//        InputStream stream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
//        SimpleParser_new parser = new SimpleParser_new(stream);
//        parser.setDefaultNamespace("empty");
//
//        OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
//        OWLDataFactory owlDataFactory = owlOntologyManager.getOWLDataFactory();
//        parser.setOWLDataFactory(owlDataFactory);

        try {

//            CandidateSolution candidateSolution =  parser.parseCandidateSolution();
//            OWLClassExpression owlClassExpression;
//            OWLClass owlClass;
//
//
//           System.out.println(candidateSolution.getSolutionAsString());


//            String [] string = {"Q", "Q2", "QQ", "Q222", "AQ2"};
//            for(String _str : string){
//
//                if(_str.matches("^Q\\d+")){
//                    System.out.println(_str);
//                }
//            }

            String reasonerFactoryClassName = null;

            OWLReasonerFactory reasonerFactory = (OWLReasonerFactory)  Class.forName(reasonerFactoryClassName).newInstance();

            System.out.println(reasonerFactory.getReasonerName());

        } catch (Exception  e) {
            e.printStackTrace();
        }


    }
}
