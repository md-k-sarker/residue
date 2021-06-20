/*
Written by sarker.
Written at 2/12/20.
*/

import org.dase.residue.mapper.EntityMapperWithDbpedia;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.Collection;

public class TestEntityMapper {


    static EntityMapperWithDbpedia entityMapperWithDBPedia = new EntityMapperWithDbpedia();

    public static void recursiveSuperType(OWLClassExpression owlClassExpression) {
        if (((OWLClass) owlClassExpression).equals(
                entityMapperWithDBPedia.owlOntology.getOWLOntologyManager().getOWLDataFactory().getOWLThing())) {
            return;
        }
        EntitySearcher.getSuperClasses((OWLClass) owlClassExpression, entityMapperWithDBPedia.owlOntology).forEach(superClass -> {
            System.out.println("\t\t supertype: " + superClass);
            recursiveSuperType(superClass);
        });
    }

    public static void main(String[] args) {

        // test getDbpediaJson
        String jsonString = "";
//        entityMapperWithDBPedia.getDbpediaJson("https://api.dbpedia-spotlight.org/en/annotate?text=Barack%20Obama%20good");
        System.out.println("jsonResponse: " + jsonString);

        entityMapperWithDBPedia.initOnto();

        // test parseJson
        entityMapperWithDBPedia.parseJson(jsonString).forEach(s -> {
            System.out.println("entity: " + s);
            Collection<OWLClassExpression> owlClasses = entityMapperWithDBPedia.indivTypes(s);
            owlClasses.forEach(owlClass -> {
                System.out.println("\t type: " + owlClass);
                // test superclasses of a class without reasoning.
                recursiveSuperType(owlClass);
//                EntitySearcher.getSuperClasses((OWLClass) owlClass, entityMapperWithDBPedia.owlOntology).forEach(superClass -> {
//                    System.out.println("\t\t supertype: " + superClass);
//                });
            });
        });


    }
}
