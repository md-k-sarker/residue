grammar simple_parser_v0;

options {
    language=Java;
//    backtrack=true;
//    JAVA_UNICODE_ESCAPE=true;
//    //LOOKAHEAD=2;
//    JAVA_TEMPLATE_TYPE = "modern";
//    EXCEPTIONS_SUPER_CLASS = "org.semanticweb.owlapi.io.OWLParserException";
//    SUPPORT_CLASS_VISIBILITY_PUBLIC=false;
//    OUTPUT_DIRECTORY="/Users/sarker/Workspaces/Jetbrains/residue/java/residue_java_v1/src/main/java/org/dase/parser/parser_simple";
    //DEBUG_PARSER=true;
    //DEBUG_TOKEN_MANAGER=true;
}
// Antlr does not generate package declaration on top of generated classes. We have to use @parser::header and @lexer::header blocks to enforce it. Headers must follow options block:
//@header {
//  package org.dase.parser.antlr;
//}

//@parser::header {
//  package org.dase.parser.antlr;
//}


/**
* parser rules
*/
parsecandidatesolution : groupedcandidateclasses ( AND groupedcandidateclasses )* ;
groupedcandidateclasses : SOME parseobjectpropertyid DOT parsecandidateclasswithprop (OR parsecandidateclasswithprop)* |
                          parsecandidateclass ( AND parsecandidateclass)*  ;
// this rule has problem....
parsecandidateclasswithprop :  OPENPAR parseconjunctivehornclause ( OR parseconjunctivehornclause)* CLOSEPAR ;
parsecandidateclass : OPENPAR parsecandidateclass CLOSEPAR |
                        parseconjunctivehornclause ( AND parseconjunctivehornclause)* ;
parseconjunctivehornclause : OPENPAR parseposclasses CLOSEPAR ( AND NOT OPENPAR parsenegclasses CLOSEPAR )? ;
parseposclasses  :  parseclassid ( AND parseclassid )* ;
parsenegclasses :  parseclassid ( OR parseclassid )* ;
parseobjectpropertyid : parseid;
parseclassid : parseid ;
parseid : IDD ;

/**
* lexar rules
*/

// We skip over any white space
WS: [ \t\r\n]+ -> skip ;
SUBCLASSOF: '\u2291' | '->' | 'sub' | '\\sqsubseteq'  ;
EQUIVALENTTO: '\u2261' | '==' | '\\equiv' ;
NEQ: '\u2260' | '!=' | '\\not=' ;
COMPOSE: 'o' | '\u2218' ;
DOT: '.';
INVERSE: '\u207B' | '^-';
OPENPAR: '(' ;
CLOSEPAR: ')' ;
OPENSQPAR: '[' ;
CLOSESQPAR: ']' ;
OPENBRACE: '{' ;
CLOSEBRACE: '}' ;
COLON: ':' ;
AND: '\u2293' | 'and' | 'AND' | '\\sqcap' ;
OR: '\u2294' | 'or' | 'OR' | '\\sqcup' ;
NOT: '\u00AC' | 'not' | 'NOT' | '\\lnot' ;
SOME: '\u2203' | 'exists' | 'EXISTS' | 'some' | 'SOME' | '\\exists' ;
ALL: '\u2200' | 'forall' | 'FORALL' | '\\forall' ;
MIN: '\u2265' | '>' | '\\geq' ;
MAX: '\u2264' | '<' | '\\leq' ;
EXACT: '=' | 'equal' ;
IN: 'in' | '\u2208' ;
TRANSITIVEROLES: 'trans' | 'transitive' | 'R\u207A' ;
INT: ([0-9])+ ;
DOUBLE: (INT) + DOT (INT)* ;
IDD : (( ~[ \n\t)([\]}{,^=<>.] ))+ ;
//ID: ((~[' ',  \n\t, '', ')', '[', CLOSESQPAR, '{', '}', ',', '^', '=', '<', '>', '.', '\u207B', '\u00AC', '\u2208']))+;


/*
 * <pre>
 * Candidate solution consists of multiple candidate classes. Multiple candidate class are grouped by owlObjectproperty.
 * Each groups are combined by AND/Intersection.
 * Inside the group:
 *  * 1. When we have none ObjectProperty or bare types, then candidate classes will be combined by AND/Intersection
 *  * 2. When we have proper ObjectProperty, then candidate classes will be combined with OR/Disjunction
 *
 *  Example Solution
 *  Candidate solution is of the form:
 *
 *   l
 * A ⊓ 􏰃∃Ri.Ci,
 *   i=1
 *
 *    which can  also be written as:
 *
 *   k3       k2
 * A ⊓ 􏰃∃Ri. 􏰀(⊔(Bji ⊓¬(D1 ⊔...⊔Dji)))
 *   i=1    j=1
 *
 *   here,
 *   k3 = limit of object properties considered. = ConfigParams.objPropsCombinationLimit
 *   k2 = limit of horn clauses. = ConfigParams.hornClauseLimit.
 *
 * An Example
 *  *   Solution = (A1 ⊓ ¬(D1)) ⊓ (A2 ⊓ ¬(D1))  ⊓  R1.( (B1 ⊓ ... ⊓ Bn ⊓ ¬(D1 ⊔...⊔ Djk)) ⊔ (B1 ⊓ ... ⊓ Bn ⊓ ¬(D1 ⊔...⊔ Djk)) ) ⊓ R2.(..)...
 *  *      here, we have 3 groups.
 *  *       group1: with bare objectProperty: (A1 ⊓ ¬(D1)) ⊓ (A2 ⊓ ¬(D1))
 *  *       group2: with R1 objectProperty:   R1.( (B1 ⊓ ... ⊓ Bn ⊓ ¬(D1 ⊔...⊔ Djk)) ⊔ (B1 ⊓ ... ⊓ Bn ⊓ ¬(D1 ⊔...⊔ Djk)) )
 *  *       group3: with R2 objectProperty:   R2.(..)
 *
 *  *   Inside of a group, we have multiple candidateClass
 *  *       multiple candidateClass are conjuncted when we have hare object property, and
 *                                      unioned when we have proper object property.
 *  *       Inside of CandidateClass:
 *  *           multiple horclauses are conjuncted when we have hare object property, and
 *                                      unioned when we have proper object property.
 *
 * * Implementation note:
 * * Atomic class is also added using candidate class. If the object property is empty = SharedDataHolder.noneOWLObjProp then it is atomic class.
 * * it must be the same procedure in both
 *      1. Printing getAsString(
 *      2. calculating accuracy,
 *      3. making getAsOWLClassExpression(
 *  * We will have a single group for a single objectProperty.

        Grammer:
        CandidateSolution = (GroupedCandidateClasses) ( AND GroupedCandidateClasses)*
        ------------
        GroupedCandidateClasses = (CandidateClass) ( AND CandidateClass)* |
                                = SOME objProp DOT (CandidateClassWithProp) ( OR CandidateClassWithProp)*
        ------------
        CandidateClass =  (ConjunctiveHornClause) ( AND ConjunctiveHornClause)* | // without object properties and without bracket
                         "(" (ConjunctiveHornClause) ( AND ConjunctiveHornClause)* ")" | // without object properties and with bracket, if there exist AND then the first
                                                                    "(" should not be consumed here. It is becoming conflicting with ConjunctiveHornClause = "(" PosClasses AND NegClasses ")" for test case 7.
                                                                     By putting and extra parenthesis on test case 7, this rule will work.
        ------------
        CandidateClassWithProp = "(" (ConjunctiveHornClause) ( OR ConjunctiveHornClause)* ")" | // with object properties and with bracket
        --------------
        ConjunctiveHornClause = PosClasses |
                              = PosClasses AND NegClasses |   // lookahead ?? how much? For test case: (A1 ⊓ ¬(D1)) it will try to execute ConjunctiveHornClause = PosClasses,
                                                            rather than this rule, if lookahead is not enforced. lookAhead should be no. of PosClass Limit + 2 (AND NOT)
                              = "(" PosClasses AND NegClasses ")"
        --------------
        PosClasses = PosClass |
                   =  PosClass ( AND PosClass)*    // lookahead 2, 1 for AND, 1 for NOT, so 1+1=2
        NegClasses = NOT "(" NegClass ")"|
                   = NOT "(" NegClass ( OR NegClass )* ")" // lookahead 1

*
* test-case
*   1: A ⊓ B
*   2: R.(A ⊓ B)
*   3. (A1 ⊓ ¬(D1))
*   4: (A1 ⊓ ¬(D1)) ⊓ (A2 ⊓ ¬(D1))
*   5: R.( (A1 ⊓ ¬(D1)) ⊔ (A2 ⊓ ¬(D1)) )
*   6. A ⊓ B ⊓ R2.( (A1 ⊓ ¬(D1)) ⊔ (A2 ⊓ ¬(D1)) )
*   7. (A1 ⊓ ¬(D1)) ⊓ (A2 ⊓ ¬(D1))  ⊓  R1.( (B1 ⊓ ... ⊓ Bn ⊓ ¬(D1 ⊔...⊔ Djk)) ⊔ (B1 ⊓ ... ⊓ Bn ⊓ ¬(D1 ⊔...⊔ Djk)) ) ⊓ R2.(..)
*   7... ( (A1 ⊓ ¬(D1)) ⊓ (A2 ⊓ ¬(D1)) ) ⊓  R1.( (B1 ⊓ ... ⊓ Bn ⊓ ¬(D1 ⊔...⊔ Djk)) ⊔ (B1 ⊓ ... ⊓ Bn ⊓ ¬(D1 ⊔...⊔ Djk)) ) ⊓ R2.(..)
*   8. A
     </pre>
 */

//CandidateSolutionV1 parseCandidateSolution() :
//{
//    CandidateSolutionV1 candidateSolution = new CandidateSolutionV1(reasoner,ontology);
//    ArrayList<CandidateClassV1> candidateClasses = new ArrayList<CandidateClassV1>();
// }
//{    candidateClasses = GroupedCandidateClasses()
//            { if(null != candidateClasses) for(CandidateClassV1 c1: candidateClasses) candidateSolution.addCandidateClass(c1);}
//    (<AND> candidateClasses = GroupedCandidateClasses()
//            { if(null != candidateClasses) for(CandidateClassV1 c1: candidateClasses) candidateSolution.addCandidateClass(c1); } )*
//     <EOF>
//    { return candidateSolution; }
//}
//
//ArrayList<CandidateClassV1> GroupedCandidateClasses() :
//{
//    ArrayList<CandidateClassV1> candidateClasses = new ArrayList<CandidateClassV1>();
//    CandidateClassV1 candidateClass;
//    OWLObjectProperty owlObjectProperty;
//}
//{  // conflict, we are using <OPENPAR> here and also in parseConjunctiveHornClause(), which is being called by parseCandidateClass()!!!! todo(zaman)
//    (
////       <OPENPAR> candidateClasses = GroupedCandidateClasses() <CLOSEPAR> |
//    <SOME>  owlObjectProperty = parseObjectPropertyId() "." candidateClass = parseCandidateClassWithProp(owlObjectProperty)
//                     { if(null != candidateClass) candidateClasses.add(candidateClass); }
//         ( <OR>  candidateClass = parseCandidateClassWithProp(owlObjectProperty)
//                     { if(null != candidateClass) candidateClasses.add(candidateClass); } )*
//                         |
//     candidateClass = parseCandidateClass(SharedDataHolder.noneOWLObjProp)
//                { if(null != candidateClass) candidateClasses.add(candidateClass); }
//        ( LOOKAHEAD(2) <AND>  candidateClass = parseCandidateClass(SharedDataHolder.noneOWLObjProp)
//                    { if(null != candidateClass) candidateClasses.add(candidateClass); })*
//    )  // why this LOOKAHEAD(2) ? candidateClass also have AND inside, so that AND and this AND* is conflicting. not sure how many lookahead is needed
//    { return candidateClasses;}
//}
//
//
//CandidateClassV1 parseCandidateClass(OWLObjectProperty owlObjectProperty) :
//{
//    CandidateClassV1 candidateClass = new CandidateClassV1(owlObjectProperty,reasoner,ontology);
//    ConjunctiveHornClauseV1 conjunctiveHornClause;
//}
//{
//    (
////    LOOKAHEAD(3) <OPENPAR> conjunctiveHornClause = parseConjunctiveHornClause(owlObjectProperty) { candidateClass.addConjunctiveHornClauses(conjunctiveHornClause); }
////        ( LOOKAHEAD(2) <AND>  conjunctiveHornClause = parseConjunctiveHornClause(owlObjectProperty) { candidateClass.addConjunctiveHornClauses(conjunctiveHornClause); })* <CLOSEPAR>
////    |
////    LOOKAHEAD(3)
//     conjunctiveHornClause = parseConjunctiveHornClause(owlObjectProperty) { candidateClass.addConjunctiveHornClauses(conjunctiveHornClause); }
//        ( LOOKAHEAD(2) <AND>  conjunctiveHornClause = parseConjunctiveHornClause(owlObjectProperty) { candidateClass.addConjunctiveHornClauses(conjunctiveHornClause); })*
//    )
//    {
//    return candidateClass;
//    }
//}
//
//
//CandidateClassV1 parseCandidateClassWithProp(OWLObjectProperty owlObjectProperty) :
//{
//    CandidateClassV1 candidateClass = new CandidateClassV1(owlObjectProperty,reasoner,ontology);
//    ConjunctiveHornClauseV1 conjunctiveHornClause;
//}
//{   // we are requiring  1 <OPENPAR> after some/objectProp explicitly
//     (
//     <OPENPAR> conjunctiveHornClause = parseConjunctiveHornClause(owlObjectProperty) { candidateClass.addConjunctiveHornClauses(conjunctiveHornClause); }
//    ( LOOKAHEAD(2) <OR>  conjunctiveHornClause = parseConjunctiveHornClause(owlObjectProperty) { candidateClass.addConjunctiveHornClauses(conjunctiveHornClause); })* <CLOSEPAR>
//    )
//    {
//    return candidateClass;
//    }
//}
//
//ConjunctiveHornClauseV1 parseConjunctiveHornClause(OWLObjectProperty owlObjectProperty) :
//{   ConjunctiveHornClauseV1 conjunctiveHornClause = new ConjunctiveHornClauseV1(owlObjectProperty,reasoner,ontology);
//    ArrayList<OWLClassExpression> posTypes = new ArrayList<OWLClassExpression>();
//    ArrayList<OWLClassExpression> negTypes = new ArrayList<OWLClassExpression>();
//}
//{
//    (
//    // how to handle multiple parenthesis? we may have A ⊓ B or (A ⊓ B ) or ((A ⊓ B)) or (....((A ⊓ B))....) all of them are still valid!!!!.
////    <OPENPAR> conjunctiveHornClause = parseConjunctiveHornClause(owlObjectProperty) <CLOSEPAR>  |
//    // how to differentiate this AND and the AND of posTypes? this is conflicting!!!!!, need to avoid it. todo(zaman)
//    // how many lookahead here?
//    posTypes = parsePosClasses() {conjunctiveHornClause.setPosObjectTypes(posTypes);}
//                ( <AND> <NOT> <OPENPAR> negTypes = parseNegClasses() {conjunctiveHornClause.setNegObjectTypes(negTypes);} <CLOSEPAR> )?
////            | posTypes = parsePosClasses() {conjunctiveHornClause.setPosObjectTypes(posTypes);}
//    )
//  {
//     return conjunctiveHornClause;
//  }
//}
//
//
//ArrayList<OWLClassExpression> parsePosClasses() :
//{
//    ArrayList<OWLClassExpression> posTypes = new ArrayList<OWLClassExpression>();
//    OWLClassExpression owlClassExpression;
//}
//{
//    ( //   LOOKAHEAD(2) -- ClassID, AND
////    LOOKAHEAD(2) LOOKAHEAD(2)
//// 2.	To avoid the conflict of choosing AND of posTypes and AND between posTypes and negTypes, Pos portion of hornclauses must have parenthesis
//   <OPENPAR>  ( owlClassExpression = parseClassId() { posTypes.add(owlClassExpression); } )  ( <AND> owlClassExpression = parseClassId() { posTypes.add(owlClassExpression); } )* <CLOSEPAR>
////         |      owlClassExpression = parseClassId()  { posTypes.add(owlClassExpression); }
//    )
//  { return posTypes; }
//}
//
//ArrayList<OWLClassExpression> parseNegClasses() :
//{
//    ArrayList<OWLClassExpression> negTypes = new ArrayList<OWLClassExpression>();
//    OWLClassExpression owlClassExpression;
//}
//{
//    (
////    <OPENPAR> negTypes = parseNegClasses() <CLOSEPAR>  |
//      // this OR and OR inside negClasses are conflicting!!!!!, how can we avoid it?
//      // why we are using LOOKAHEAD here ?
//      // lookahead 2 is required because  CLASSID, OR
////       LOOKAHEAD(2)
//        (  owlClassExpression = parseClassId() { negTypes.add(owlClassExpression); } )
//            ( LOOKAHEAD(2) <OR> owlClassExpression = parseClassId() { negTypes.add(owlClassExpression); } )*
////      |      owlClassExpression = parseClassId()  { negTypes.add(owlClassExpression); }
//    )
//  { return negTypes; }
//}
//
//
//OWLObjectProperty parseObjectPropertyId():
//{
//    IRI iri;
//    boolean inverse = false;
//}
//{
//    iri=parseId() { return factory.getOWLObjectProperty(iri); }
//}
//
//
//OWLClass parseClassId():
//{ IRI iri; }
//{ iri=parseId() { return factory.getOWLClass(iri); } }
//
//IRI parseId() :
//{ Token t; }
//{ t=<ID> { return getIRIFromId(t.image); } }


//PARSER_BEGIN(SimpleParser_v4)
//
//package org.dase.parser.javacc;
//
//import java.util.Set;
//import java.util.HashSet;
//import java.util.*;
//
//import org.dase.core.SharedDataHolder;
//import org.dase.datastructure.CandidateClassV1;
//import org.dase.datastructure.CandidateSolutionV1;
//import org.dase.datastructure.ConjunctiveHornClauseV1;
//import org.semanticweb.owlapi.reasoner.OWLReasoner;import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
//import org.semanticweb.owlapi.util.*;
//
//import org.semanticweb.owlapi.model.*;
//
//@SuppressWarnings("all")
//public class SimpleParser_v4 {
//    private String defaultNamespace = IRI.create("http://www.semanticweb.org/ontologies/Ontology").toString();
//    private Map<String, String> namespaceMap = new HashMap<String, String>();
//    private OWLDataFactory factory;
//    private OWLOntology ontology;
//    private OWLReasoner reasoner;
//    private Map<String, IRI> iriMap = new HashMap<String, IRI>();
//    private Map<String, IRI> qnameIRIMap = new HashMap<String, IRI>();
//    private Set<OWLAxiom> axioms = new LinkedHashSet<OWLAxiom>();
//    private int a;
//    private static boolean negationHappened = false;
//
//    public void initiate(OWLDataFactory factory,OWLOntology ontology, OWLReasoner reasoner) {
//        this.factory = factory;
//        this.ontology = ontology;
//        this.reasoner = reasoner;
//    }
//
//    public void setPrefixMapping(String prefix, String namespace) {
//        namespaceMap.put(prefix, namespace);
//    }
//
//    public void setDefaultNamespace(String ns) {
//        defaultNamespace = ns;
//    }
//
//    public IRI getIRI(String val) {
//        IRI iri = iriMap.get(val);
//        if(iri == null) {
//            iri = IRI.create(val);
//            iriMap.put(val, iri);
//        }
//        return iri;
//    }
//
//    public IRI getIRIFromId(String qname) {
//        if(qname.equals("top") || qname.equals("\u22A4")) {
//            return OWLRDFVocabulary.OWL_THING.getIRI();
//        }
//        if(qname.equals("bottom") || qname.equals("\u22A5")) {
//            return OWLRDFVocabulary.OWL_NOTHING.getIRI();
//        }
//        IRI iri = qnameIRIMap.get(qname);
//        if(iri == null) {
//            iri = getIRI(defaultNamespace + "#" + qname);
//            qnameIRIMap.put(qname, iri);
//        }
//        return iri;
//    }
//
//    private void addAxiom(OWLAxiom ax) {
//        if (ax == null) {
//          return;
//        }
//        axioms.add(ax);
//    }
//}
//
//PARSER_END(SimpleParser_v4)