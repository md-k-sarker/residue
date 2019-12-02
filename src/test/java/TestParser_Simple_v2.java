/*
Written by sarker.
Written at 9/26/19.
*/

import org.dase.core.SharedDataHolder;
import org.dase.datastructure.CandidateClass;
import org.dase.datastructure.CandidateSolution;
import org.dase.datastructure.ConjunctiveHornClause;
import org.dase.parser.parser_simple.SimpleParser_v2;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TestParser_Simple_v2 {

    public static void main(String []args) throws OWLOntologyCreationException, IOException {

//        OWLOntology ontology = Utility.loadOntology("/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/dbpedia_2016-10.owl");
//
//        OWLDocumentFormat format = ontology.getOWLOntologyManager().getOntologyFormat(ontology);
//
//        Map<String, String> prefixes = format.asPrefixOWLOntologyFormat().getPrefixName2PrefixMap();
//        prefixes.forEach((e1,e2) -> {
//            System.out.println(e1 + e2);
//        });

        String str = " (WN_Ceiling or Animal or not Baby)";
        //"Trees and river";  got solution: ((Trees) ⊔ (river)), not allowed
        // "(Trees or river)"; got solution: (Trees or river )
        // "(River or People or Animal)"; got solution: (River or People or Animal)
        // " (WN_Ceiling or Animal or not Baby)" got solution: ((WN_Ceiling) ⊔ (Animal) ⊔ ( ¬ Baby))
        // "exists imageContains.((WN_Ceiling) ⊔ ( ¬ Region) ⊔ (Process))" got ParseException: Encountered " "(" "( "" at line 1, column 22. //Was expecting://    <ID> ...
        // exists imageContains.WN_Ceiling got ∃imageContains.(WN_Ceiling)
        // exists imageContains . (WN_Ceiling or Animal or Baby) got ∃imageContains.((WN_Ceiling) ⊔ (Animal) ⊔ (Baby))
        // exists imageContains.(WN_Ceiling) got ParseException: Encountered " "(" "( "" at line 1, column 22. //Was expecting://    <ID> ...
        // "exists imageContains. not WN_Ceiling"; got ParseException: Encountered " "(" "( "" at line 1, column 22. //Was expecting://    <ID> ...

        InputStream stream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        SimpleParser_v2 parser = new SimpleParser_v2(stream);
        parser.setDefaultNamespace("empty");

        OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
        OWLDataFactory owlDataFactory = owlOntologyManager.getOWLDataFactory();
        parser.setOWLDataFactory(owlDataFactory);

        try {

            CandidateSolution candidateSolution =  parser.parseCandidateSolution();
            OWLClassExpression owlClassExpression;
            OWLClass owlClass;


           System.out.println(candidateSolution.getSolutionAsString());

        } catch (Exception  e) {
            e.printStackTrace();
        }


    }
}
