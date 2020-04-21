/*
Written by sarker.
Written at 2/12/20.
*/

import org.dase.residue.mapper.EntityMapper;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.Collection;
import java.util.HashSet;

public class TestEntityMapper {


    static EntityMapper entityMapper = new EntityMapper();

    public static void recursiveSuperType(OWLClassExpression owlClassExpression) {
        if (((OWLClass) owlClassExpression).equals(
                entityMapper.owlOntology.getOWLOntologyManager().getOWLDataFactory().getOWLThing())) {
            return;
        }
        EntitySearcher.getSuperClasses((OWLClass) owlClassExpression, entityMapper.owlOntology).forEach(superClass -> {
            System.out.println("\t\t supertype: " + superClass);
            recursiveSuperType(superClass);
        });
    }

    public static void main(String[] args) {

        // test getDbpediaJson
        String jsonString = "";
//        entityMapper.getDbpediaJson("https://api.dbpedia-spotlight.org/en/annotate?text=Barack%20Obama%20good");
        System.out.println("jsonResponse: " + jsonString);

        entityMapper.initOnto();

        // test parseJson
        entityMapper.parseJson(jsonString).forEach(s -> {
            System.out.println("entity: " + s);
            Collection<OWLClassExpression> owlClasses = entityMapper.indivTypes(s);
            owlClasses.forEach(owlClass -> {
                System.out.println("\t type: " + owlClass);
                // test superclasses of a class without reasoning.
                recursiveSuperType(owlClass);
//                EntitySearcher.getSuperClasses((OWLClass) owlClass, entityMapper.owlOntology).forEach(superClass -> {
//                    System.out.println("\t\t supertype: " + superClass);
//                });
            });
        });


    }
}
