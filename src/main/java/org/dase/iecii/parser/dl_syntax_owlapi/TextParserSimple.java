package org.dase.iecii.parser.dl_syntax_owlapi;
/*
Written by sarker.
Written at 10/3/19.
*/

import org.dase.ecii.core.SharedDataHolder;
import org.dase.ecii.datastructure.CandidateClass;
import org.dase.ecii.datastructure.CandidateSolution;
import org.dase.ecii.datastructure.ConjunctiveHornClause;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.ArrayList;

public class TextParserSimple {

    private String originalText;

    /*
    Frequent removal will happen on this text
     */
    private String textAsToken;

    public TextParserSimple(String str){
        this.originalText = str;
        this.textAsToken = str;

        textAsToken = textAsToken.trim();
    }

    public CandidateSolution getCandidateSolutionFromString(){

        CandidateSolution candidateSolution = null;

        ArrayList<CandidateClass> candidateClasses = getCandidateClasses();
        for(CandidateClass candidateClass : candidateClasses){
            candidateSolution.addCandidateClass(candidateClass);
        }
        return candidateSolution;
    }

    private ArrayList<CandidateClass> getCandidateClasses(){
        ArrayList<CandidateClass> candidateClasses = new ArrayList<>();

        return candidateClasses;
    }

    private ArrayList<ConjunctiveHornClause> getConjunctiveHornClauses(){
        ArrayList<ConjunctiveHornClause> conjunctiveHornClauses = new ArrayList<>();

        return conjunctiveHornClauses;
    }

    private ConjunctiveHornClause getConjunctiveHornClause(){
//        ConjunctiveHornClause conjunctiveHornClause = new ConjunctiveHornClause();

        return null; //conjunctiveHornClause;
    }

    private OWLObjectProperty getOwlObjectProperty(){

        OWLObjectProperty objectProperty;

       OWLDocumentFormat format = SharedDataHolder.owlOntologyManager.getOntologyFormat(SharedDataHolder.owlOntology);
        format.asPrefixOWLOntologyFormat().getPrefixName2PrefixMap();
        if(textAsToken.startsWith("exists") || textAsToken.startsWith("EXISTS") || textAsToken.startsWith("\u2203")){

            textAsToken = textAsToken.replaceFirst("exists|EXISTS|\u2203", "").trim();

//            int posi

            String objPropString ;

            // it has object property
            // extract object proepry
//            IRI iri = Utility.createEntityIRI()
//            SharedDataHolder.owlDataFactory.getOWLObjectProperty(, )
        }else{
            // none
        }

        return null;
    }


}
