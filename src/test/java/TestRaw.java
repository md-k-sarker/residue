/*
Written by sarker.
Written at 9/26/19.
*/

import org.dase.ecii.util.Utility;
import org.semanticweb.owlapi.model.OWLOntology;

public class TestRaw {

    public static void main(String[] args) {

        try {

            OWLOntology ontology = Utility.
                    loadOntology("/Users/sarker/Downloads/wiki_cats_root_from_main_topic_non_cyclic_v1.owl");
            System.out.println("Total Axiom: " + ontology.getAxiomCount());
            System.out.println("Total Class: " + ontology.getClassesInSignature().size());
        } catch (Exception ex) {

        }


    }
}
