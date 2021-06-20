package org.dase.residue.mapper;
/*
Written by sarker.
Written at 9/18/19.
*/

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import okhttp3.*;
import org.dase.ecii.util.Utility;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Map entity from the text with the entity from the knowledge graph
 */
public class EntityMapperWithDbpedia {

    public OWLOntology owlOntology;
    private OWLDataFactory owlDataFactory;
    private OWLOntologyManager owlOntologyManager;

    private String pagesOntoPath = "/Users/sarker/Workspaces/Jetbrains/residue/java/residue_java_v1/src/test/resources/entity_mapper_test_onto.owl";
    //    private String pagesOntoPath = "/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/automated_kg_wiki/wiki_full_pages_v0_non_cyclic_jan_20_32808131.rdf";
    private String categoryOntoPath = "/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/automated_kg_wiki/wiki_full_pages_v0_non_cyclic_jan_20_32808131.rdf";
    String onto_prefix = "http://www.daselab.com/residue/analysis#";

    //    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();



    public ArrayList<String> parseJson(String text) {
        ArrayList<String> dbpediaEntities = new ArrayList<>();
        if (null != text) {
            Gson gson = new Gson();
            JsonElement jsonRootElement = gson.fromJson(text, JsonElement.class);
            JsonElement jsonResourcesElement = jsonRootElement.getAsJsonObject().get("Resources");
            if (null != jsonResourcesElement) {
                jsonResourcesElement.getAsJsonArray().forEach(jsonElement -> {
                    String resourceDbpediaName = jsonElement.getAsJsonObject().get("@URI").getAsString();
                    String truncatedDbname = resourceDbpediaName.replaceAll("http://dbpedia.org/resource/", "");
                    if (null != truncatedDbname) {
                        dbpediaEntities.add(truncatedDbname);
                    }
                });
            }
        }
        return dbpediaEntities;
    }

    /**
     * return indiv types,
     * does it return all types or just the immediate parent? need to check.
     *  return just immediate parent: https://stackoverflow.com/questions/26130265/retrieve-class-to-which-a-named-individual-belongs-to-in-owl-api-4-0
     * @param truncatedDbname
     * @return
     */
    public Collection<OWLClassExpression> indivTypes(String truncatedDbname) {
        Collection<OWLClassExpression> owlClasses = new HashSet<>();

        IRI cIRI = IRI.create(onto_prefix + truncatedDbname);

        System.out.println("cIRI: " + cIRI);
        if (owlOntology.containsIndividualInSignature(cIRI)) {
            System.out.println(cIRI + " found in ontology, making indiv and searching types");
            OWLNamedIndividual cIndiv = owlDataFactory.getOWLNamedIndividual(cIRI);
            owlClasses = EntitySearcher.getTypes(cIndiv, owlOntology);

//            owlClasses = (HashSet<OWLClass>) cIndiv.gett();
            System.out.println("Types found: " + owlClasses.size());
        } else {
            System.out.println(cIRI + " not found in ontology");
        }
        return owlClasses;
    }

    public void initOnto() {
        try {
            System.out.println("Loading pages ontology .....................");
            owlOntology = Utility.loadOntology(pagesOntoPath);
            System.out.println("Loading pages ontology finished");
            owlOntologyManager = owlOntology.getOWLOntologyManager();
            owlDataFactory = owlOntologyManager.getOWLDataFactory();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        EntityMapperWithDbpedia entityMapperWithDBPedia = new EntityMapperWithDbpedia();

    }

}
