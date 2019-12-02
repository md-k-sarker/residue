// Generated from /Users/sarker/Workspaces/Jetbrains/residue/java/residue_java_v1/src/main/antlr4/org/dase/parser/antlr/simple_parser_v2.g4 by ANTLR 4.7.2

package org.dase.parser.antlr;

import org.dase.datastructure.CandidateClassV1;
import org.dase.datastructure.CandidateSolutionV1;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.*;

import org.dase.core.SharedDataHolder;
import org.dase.datastructure.ConjunctiveHornClauseV1;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

/**
 * This class provides an empty implementation of {@link simple_parser_v2Visitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 */
public class simple_parser_v2BaseVisitorImpl extends simple_parser_v2BaseVisitor<Object> {

    private String defaultNamespace = IRI.create("http://www.semanticweb.org/ontologies/Ontology").toString();
    private Map<String, String> namespaceMap = new HashMap<String, String>();
    private OWLDataFactory factory;
    private OWLOntology ontology;
    private OWLReasoner reasoner;
    private Map<String, IRI> iriMap = new HashMap<String, IRI>();
    private Map<String, IRI> qnameIRIMap = new HashMap<String, IRI>();
    private OWLObjectProperty currentOwlObjectProperty;

    private class GroupedCandidateClassWrapper {

        public OWLObjectProperty owlObjectProperty;
        public ArrayList<CandidateClassV1> candidateClassV1s;

        public GroupedCandidateClassWrapper(OWLObjectProperty owlObjectProperty, ArrayList<CandidateClassV1> candidateClassV1s) {
            this.owlObjectProperty = owlObjectProperty;
            this.candidateClassV1s = candidateClassV1s;
        }

    }

    /**
     * constructor
     */
    public simple_parser_v2BaseVisitorImpl(OWLDataFactory factory, OWLOntology ontology, OWLReasoner reasoner) {
        this.factory = factory;
        this.ontology = ontology;
        this.reasoner = reasoner;
        this.currentOwlObjectProperty = SharedDataHolder.noneOWLObjProp;
    }

    public void setPrefixMapping(String prefix, String namespace) {
        namespaceMap.put(prefix, namespace);
    }

    public void setDefaultNamespace(String ns) {
        defaultNamespace = ns;
    }

    public IRI getIRI(String val) {
        IRI iri = iriMap.get(val);
        if (iri == null) {
            iri = IRI.create(val);
            iriMap.put(val, iri);
        }
        return iri;
    }

    public IRI getIRIFromId(String qname) {
        if (qname.equals("top") || qname.equals("\u22A4")) {
            return OWLRDFVocabulary.OWL_THING.getIRI();
        }
        if (qname.equals("bottom") || qname.equals("\u22A5")) {
            return OWLRDFVocabulary.OWL_NOTHING.getIRI();
        }
        IRI iri = qnameIRIMap.get(qname);
        if (iri == null) {
            iri = getIRI(defaultNamespace + "#" + qname);
            qnameIRIMap.put(qname, iri);
        }
        return iri;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public CandidateSolutionV1 visitParsecandidatesolution(simple_parser_v2Parser.ParsecandidatesolutionContext ctx) {
        CandidateSolutionV1 candidateSolutionV1 = new CandidateSolutionV1(reasoner, ontology);
        for (simple_parser_v2Parser.GroupedcandidateclassesContext groupedcandidateclassesContext : ctx.groupedcandidateclasses()) {
            GroupedCandidateClassWrapper groupedCandidateClassWrapper = visitGroupedcandidateclasses(groupedcandidateclassesContext);
            candidateSolutionV1.addGroupedCandidateClass(groupedCandidateClassWrapper.owlObjectProperty,groupedCandidateClassWrapper.candidateClassV1s);
        }
        return candidateSolutionV1;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public GroupedCandidateClassWrapper visitGroupedcandidateclasses(simple_parser_v2Parser.GroupedcandidateclassesContext ctx) {

        ArrayList<CandidateClassV1> candidateClassV1s = new ArrayList<>();

        if (null != ctx.parseobjectpropertyid()) {
            currentOwlObjectProperty = visitParseobjectpropertyid(ctx.parseobjectpropertyid());
            for (simple_parser_v2Parser.ParsecandidateclasswithpropContext parsecandidateclasswithpropContext : ctx.parsecandidateclasswithprop()) {
                candidateClassV1s.add(visitParsecandidateclasswithprop(parsecandidateclasswithpropContext));
            }
        } else {
            currentOwlObjectProperty = SharedDataHolder.noneOWLObjProp;
            for (simple_parser_v2Parser.ParsecandidateclassContext parsecandidateclassContext : ctx.parsecandidateclass()) {
                candidateClassV1s.add(visitParsecandidateclass(parsecandidateclassContext));
            }
        }

        GroupedCandidateClassWrapper groupedCandidateClassWrapper = new GroupedCandidateClassWrapper(currentOwlObjectProperty, candidateClassV1s);
        return groupedCandidateClassWrapper;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public CandidateClassV1 visitParsecandidateclasswithprop(simple_parser_v2Parser.ParsecandidateclasswithpropContext ctx) {
        CandidateClassV1 candidateClassV1 = new CandidateClassV1(currentOwlObjectProperty, reasoner, ontology);

        for (simple_parser_v2Parser.ParseconjunctivehornclauseContext parseconjunctivehornclauseContext : ctx.parseconjunctivehornclause()) {
            candidateClassV1.addConjunctiveHornClauses(visitParseconjunctivehornclause(parseconjunctivehornclauseContext));
        }
        return candidateClassV1;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public CandidateClassV1 visitParsecandidateclass(simple_parser_v2Parser.ParsecandidateclassContext ctx) {
        currentOwlObjectProperty = SharedDataHolder.noneOWLObjProp;
        CandidateClassV1 candidateClassV1 = new CandidateClassV1(currentOwlObjectProperty, reasoner, ontology);

        for (simple_parser_v2Parser.ParseconjunctivehornclauseContext parseconjunctivehornclauseContext : ctx.parseconjunctivehornclause()) {
            candidateClassV1.addConjunctiveHornClauses(visitParseconjunctivehornclause(parseconjunctivehornclauseContext));
        }
        return candidateClassV1;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public ConjunctiveHornClauseV1 visitParseconjunctivehornclause(simple_parser_v2Parser.ParseconjunctivehornclauseContext ctx) {
        ConjunctiveHornClauseV1 conjunctiveHornClause = new ConjunctiveHornClauseV1(currentOwlObjectProperty, reasoner, ontology);
        // todo(zaman): do we need to visit everything of this rule, as we have tokens of the string, like parenthesis etc..
        ArrayList<OWLClassExpression> posClasses = new ArrayList<>();
        ArrayList<OWLClassExpression> negClasses = new ArrayList<>();

        if(null != visitParseposclasses(ctx.parseposclasses())){
            posClasses = visitParseposclasses(ctx.parseposclasses());
        }
        if(null !=ctx.parsenegclasses()) {
            negClasses = visitParsenegclasses(ctx.parsenegclasses());
        }
        conjunctiveHornClause.setPosObjectTypes(posClasses);
        conjunctiveHornClause.setNegObjectTypes(negClasses);
        return conjunctiveHornClause;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public ArrayList<OWLClassExpression> visitParseposclasses(simple_parser_v2Parser.ParseposclassesContext ctx) {
        ArrayList<OWLClassExpression> posClasses = new ArrayList<>();
        for (simple_parser_v2Parser.ParseclassidContext parseclassidContext : ctx.parseclassid()) {
            posClasses.add(visitParseclassid(parseclassidContext));
        }
        return posClasses;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public ArrayList<OWLClassExpression> visitParsenegclasses(simple_parser_v2Parser.ParsenegclassesContext ctx) {
        ArrayList<OWLClassExpression> negClasses = new ArrayList<>();
        if(null != ctx.parseclassid()) {
            for (simple_parser_v2Parser.ParseclassidContext parseclassidContext : ctx.parseclassid()) {
                negClasses.add(visitParseclassid(parseclassidContext));
            }
        }
        return negClasses;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public OWLObjectProperty visitParseobjectpropertyid(simple_parser_v2Parser.ParseobjectpropertyidContext ctx) {
        currentOwlObjectProperty = factory.getOWLObjectProperty(visitParseid(ctx.parseid()));
        return currentOwlObjectProperty;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public OWLClass visitParseclassid(simple_parser_v2Parser.ParseclassidContext ctx) {
//		return visitChildren(ctx.parseid());
        return factory.getOWLClass(visitParseid(ctx.parseid()));
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public IRI visitParseid(simple_parser_v2Parser.ParseidContext ctx) {
        return getIRIFromId(ctx.getText());
    }
}