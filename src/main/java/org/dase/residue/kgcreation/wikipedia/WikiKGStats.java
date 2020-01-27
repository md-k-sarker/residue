package org.dase.residue.kgcreation.wikipedia;
/*
Written by sarker.
Written at 1/26/20.
*/

import org.dase.ecii.util.Utility;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class WikiKGStats {

    private OWLOntology owlOntology;
    private OWLDataFactory owlDataFactory;
    private OWLOntologyManager owlOntologyManager;
    private String ontoPath = "/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/automated_kg_wiki/wiki_cats_v0_jan_20.ttl";
    private String onto_prefix = "http://www.daselab.com/residue/analysis#";
    private OWLClass rootClass;
    private OWLReasoner owlReasoner;

    public void calcStats() {
        System.out.println("Total axioms: " + owlOntology.getAxiomCount());
        System.out.println("Total classes: " + owlOntology.getClassesInSignature().size());
        System.out.println("Total subclassOf axioms: " + owlOntology.getAxiomCount(AxiomType.SUBCLASS_OF));
        System.out.println("Total individuals: " + owlOntology.getIndividualsInSignature().size());
        System.out.println("Total objectProperties: " + owlOntology.getObjectPropertiesInSignature().size());
    }

    public void getRootClass() {
        IRI rootClassIRI = IRI.create(onto_prefix + "Main_topic_classifications");
        rootClass = owlDataFactory.getOWLClass(rootClassIRI);
        if (owlOntology.containsClassInSignature(rootClassIRI)) {
            System.out.println("Ontology has root class " + rootClass.getIRI().toString());
        } else {
            System.out.println("Could not find root class");
        }
    }

    public void doReasoning() {
        if (null != owlOntology) {
            System.out.println("Reasoning starting .....................");
            owlReasoner = Utility.initReasoner("pellet", owlOntology, null);
            System.out.println("Reasoning finished");
        }
    }


    public void initOnto() {
        try {

            System.out.println("Loading ontology .....................");
            owlOntology = Utility.loadOntology(ontoPath);
            System.out.println("Loading ontology finished");
            owlOntologyManager = owlOntology.getOWLOntologyManager();
            owlDataFactory = owlOntologyManager.getOWLDataFactory();
        } catch (Exception ex) {

        }
    }

    public static void main(String[] args) {

        WikiKGStats wikiKGStats = new WikiKGStats();

        final long intiOntoStartTime = System.currentTimeMillis();
        wikiKGStats.initOnto();
        final long intiOntoEndTime = System.currentTimeMillis();
        System.out.println("Init ontology time: " + (intiOntoEndTime - intiOntoStartTime) / 60000 + " minutes");

        wikiKGStats.calcStats();

//        final long reasoningStartTime = System.currentTimeMillis();
//        wikiKGStats.doReasoning();
//        final long reasoningEndTime = System.currentTimeMillis();
//        System.out.println("Reasoning time: " + (reasoningEndTime - reasoningStartTime) / 60000 + " minutes");
//
//        final long findRootStartTime = System.currentTimeMillis();
//        wikiKGStats.getRootClass();
//        final long findRootEndTime = System.currentTimeMillis();
//        System.out.println("Find root time: " + (findRootEndTime - findRootStartTime) / 60000 + " minutes");

    }
}
