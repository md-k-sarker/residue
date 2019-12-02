package org.dase.util;
/*
Written by sarker.
Written at 10/7/19.
*/

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.csv.CSVParser;
import org.dase.ecii.util.Monitor;
import org.dase.ecii.util.Utility;
import org.semanticweb.owlapi.model.*;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class RefineDbpedia {

    private OWLOntology owlOntology;
    private OWLDataFactory owlDataFactory;
    private OWLOntologyManager owlOntologyManager;
    String dbpediaOntologyPath = "/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/dbpedia_2016-10_v2.owl";
    String dbpediaOntologyPathToSave = "/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/dbpedia_2016-10_v3.owl";
    String csvPath = "/Users/sarker/Workspaces/Jetbrains/residue/data/RCTA_IFP/RCTA_IFP_data_hfcD_with_POS_keywords_v4.csv";
    static int questionCounter = 0;
    String indiv_prefix = "http://dbpedia.org/ontology/" + "indiv_IFP_";

    // to match an entity with this
    HashSet<OWLClass> existingClasses = new HashSet<>();
    HashSet<OWLObjectProperty> existingProperties = new HashSet<>();


    /*
          2 ways to map
         1. q1Indi talksAboout Indiv1 // here indiv1 is another individual of class resource_1
    // talksAboutConcept = false,
    // this is our v1 data

         2. q1Indi talksAbout Class1 // here class1 is a resource_1
            2.1. if resource_1 is a class then we are fine.
            2.2. if resource_1 is object property then take the range of it as class1
    // talksAboutConcept = true
    // this is our v2 data
     */
    boolean talksAboutConcept = false;

    /*
    Type of axioms to be deleted:
    1. ANNOTATION_PROPERTY_DOMAIN
    2. ANNOTATION_PROPERTY_RANGE
    3. DATA_PROPERTY_ASSERTION
    4. DATA_PROPERTY_DOMAIN
    5. DATA_PROPERTY_RANGE
    6. DISJOINT_DATA_PROPERTIES
    7. EQUIVALENT_DATA_PROPERTIES
    8. FUNCTIONAL_DATA_PROPERTY
    9. NEGATIVE_DATA_PROPERTY_ASSERTION
    10. SUB_ANNOTATION_PROPERTY_OF
    11. 	SUB_DATA_PROPERTY
    12.
     */
    public boolean removeExtraAxioms() {
        HashSet<AxiomType> all_axiom_types = new HashSet<>();
        all_axiom_types.addAll(AxiomType.AXIOM_TYPES);
        System.out.println(all_axiom_types.size());
        Set<OWLAxiom> axiomsToRemove = new HashSet<OWLAxiom>();

        all_axiom_types.stream().filter(axiomType -> {
            if (axiomType.getName().contains("AnnotationProperty") || axiomType.getName().contains("DataProperty") ||
                    axiomType.getName().contains("DataProperties")) {
                return true;
            }
            return false;
        }).forEach(axiomType -> {
            axiomsToRemove.addAll(owlOntology.getAxioms(axiomType));
        });


        owlOntology.getDataPropertiesInSignature().forEach(owlDataProperty -> {
            axiomsToRemove.addAll(owlOntology.getAxioms(owlDataProperty, true));
        });

        System.out.println("Before removing: " + owlOntology.getAxiomCount());
        owlOntology.getOWLOntologyManager().removeAxioms(owlOntology, axiomsToRemove);
        System.out.println("After removing: " + owlOntology.getAxiomCount());

        return true;
    }


    // add individuals for each question to the ontology
    public void addIndividuals() {
        // each question is a individual
        try {
            owlOntology = Utility.loadOntology(dbpediaOntologyPath);
            owlOntologyManager = owlOntology.getOWLOntologyManager();
            owlDataFactory = owlOntologyManager.getOWLDataFactory();

            CSVParser csvRecords = Utility.parseCSV(csvPath, true);

            csvRecords.forEach(strings -> {
                questionCounter += 1;
                IRI indiIRI = IRI.create(indiv_prefix + questionCounter);
                OWLNamedIndividual individual = owlDataFactory.getOWLNamedIndividual(indiIRI);
                OWLAxiom owlAxiom = owlDataFactory.getOWLDeclarationAxiom(individual);
                owlOntologyManager.addAxiom(owlOntology, owlAxiom);
            });

            Utility.saveOntology(owlOntology, dbpediaOntologyPathToSave);
        } catch (Exception ex) {

        }
    }


    // q1Indi talksAbout topic1
    public void addObjPropAxiom(OWLNamedIndividual indiv, String resourceURI, String rawType) {
        /*
         2 ways to map
         1. q1Indi talksAboout Indiv1 // here indiv1 is another individual of class resource_1
         // this is our v1 data

         2. q1Indi talksAbout Class1 // here class1 is a resource_1
            2.1. if resource_1 is a class then we are fine.
            2.2. if resource_1 is object property then take the range of it as class1
         // this is our v2 data
         */


        boolean resource_is_obProp = false;
        boolean resource_is_Class = false;


        //

        //

        for(OWLObjectProperty objectProperty : existingProperties){
            if(objectProperty.getIRI().toString().contains(resourceURI)){
                resource_is_obProp = true;
                break;
            }
        }

        for(OWLClass owlClass : existingClasses){
            if(owlClass.getIRI().toString().contains(resourceURI)){
                resource_is_Class = true;
                break;
            }
        }

        if (!talksAboutConcept) {
            // map as indiv, like v1
            IRI resourceIRI = IRI.create(resourceURI);

            // currently every entity from the dbpedia-spotlight result is individual.
            if(! resource_is_obProp && ! resource_is_Class){
                // this resource is an individual, good
              OWLNamedIndividual owlNamedIndividual =  owlDataFactory.getOWLNamedIndividual(resourceIRI);
//             OWLAxiom owlAxiom = owlDataFactory.getOWLObjectPropertyAssertionAxiom(SharedDataHolder.talksAboutOWLObjectProperty, indiv, owlNamedIndividual);
//                System.out.println("axiom: "+ owlAxiom);
//             owlOntologyManager.addAxiom(owlOntology, owlAxiom);

            }else if (resource_is_Class){
                // this resource is an class
                System.out.println("resource is a class");
            }else {
                // need to get the range of this
                System.out.println("resource is a obj prop");
            }

        } else {
            // map as concept,  like v2
        }

    }


    public void createHashMapofExistingEntities(){

        existingClasses.addAll( owlOntology.getClassesInSignature());
        existingProperties.addAll(owlOntology.getObjectPropertiesInSignature());

    }

    public void addObjProps() {
        String column_name = "question_named_entities";
        CSVParser csvRecords = Utility.parseCSV(csvPath, true);

        try {
//            owlOntology = Utility.loadOntology(dbpediaOntologyPath);
//            owlOntologyManager = owlOntology.getOWLOntologyManager();
//            owlDataFactory = owlOntologyManager.getOWLDataFactory();
//
//            OWLAxiom owlAxiom = owlDataFactory.getOWLDeclarationAxiom(SharedDataHolder.talksAboutOWLObjectProperty);
//            owlOntologyManager.addAxiom(owlOntology, owlAxiom);

//            createHashMapofExistingEntities();

            csvRecords.forEach(strings -> {
                String val = strings.get(column_name);
                questionCounter += 1;
//                IRI indiIRI = IRI.create(indiv_prefix + questionCounter);
//                OWLNamedIndividual individual = owlDataFactory.getOWLNamedIndividual(indiIRI);

                // check whether dbpedia returned any resource for this question
                if (val.length() > 2) {

                    JsonElement jsonElement = JsonParser.parseString(val);
                    if (jsonElement.isJsonArray()) {
                        JsonArray resourceArray = jsonElement.getAsJsonArray();
                        Integer resource_array_size = resourceArray.size();

                        for (Integer resource_array_index = 0; resource_array_index < resource_array_size; resource_array_index++) {
                            String name = "resource_" + (resource_array_index + 1);
                            JsonElement resource = resourceArray.get(resource_array_index).getAsJsonObject();
                            // get the part without resource_n
                            resource = resource.getAsJsonObject().get(name);
                            // get uri
                            JsonElement uri = resource.getAsJsonObject().get("uri");
                            // uri/resource information
                            System.out.println(uri.getAsString());
                            String types = resource.getAsJsonObject().get("type").getAsString();

                            // check does it have any type information
                            if (types.length() > 2) {
                                String[] typeArray = types.split(",");
                                for (String eachType : typeArray) {
                                         //talks about this type
//                                        System.out.println("uri: " + uri);
//                                        System.out.println("type: " + eachType);
//                                    addObjPropAxiom(individual, uri.getAsString(), eachType);
                                }
                            } else {
                                // it dont have any type, it is working as object property, we need to get it's range and make that
//                                addObjPropAxiom(individual, uri.getAsString(), "");
                            }
                        }
                    }
//            System.out.println("line: "+ jsonElement.toString());
                } else {
                    // we can create indiv, but it will not have any mapping
                }
            });

//            OWLDocumentFormat rdfxmlDocumentFormat = new RDFXMLDocumentFormat();
//            Utility.saveOntology(owlOntology, rdfxmlDocumentFormat, dbpediaOntologyPathToSave);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
    Our ontology needs to be consistent
     */
    public void removeInconsistencies(){
        try {
            String ontoPath = "/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/dbpedia/merged_graph/dbpedia_sumo_with_instances_all_v9.rdf";
            String ontoPathToSave = "/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/dbpedia/merged_graph/dbpedia_sumo_with_instances_all_v10.rdf";

            owlOntology = Utility.loadOntology(ontoPath);
            owlOntologyManager = owlOntology.getOWLOntologyManager();
            owlDataFactory = owlOntologyManager.getOWLDataFactory();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("./log.log"));
            PrintStream printStream = new PrintStream(bos, true);
            Monitor monitor = new Monitor(printStream);

            Set<OWLAxiom> axiomsToRemove = new HashSet<OWLAxiom>();
            for(OWLAxiom axiom: owlOntology.getAxioms()){
                for(OWLEntity entity : axiom.getSignature()){
                    if(entity.getIRI().getShortForm().matches("^Q\\d+")){
                        axiomsToRemove.add(axiom);
                        break;
                    }
                    if(entity.getIRI().toString().contains("?")){
                        axiomsToRemove.add(axiom);
                        break;
                    }
                }
            }

//            OWLReasoner owlReasoner = Utility.initReasoner("pellet", owlOntology, monitor);

            System.out.println("Before removing: " + owlOntology.getAxiomCount());
            owlOntologyManager.removeAxioms(owlOntology, axiomsToRemove);
            System.out.println("After removing: " + owlOntology.getAxiomCount());

            Utility.saveOntology(owlOntology, ontoPathToSave);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }



    private void doTask() throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {

        removeInconsistencies();

//        addObjProps();

//
//        removeExtraAxioms();
//
//        OWLDocumentFormat rdfxmlDocumentFormat = new RDFXMLDocumentFormat();
//
//        Utility.saveOntology(owlOntology, rdfxmlDocumentFormat, dbpediaOntologyPathToSave);

//        addIndivandObjProp();
    }

    public static void main(String[] args) {
        RefineDbpedia refineDbpedia = new RefineDbpedia();
        try {
            refineDbpedia.doTask();
        } catch (OWLOntologyCreationException | OWLOntologyStorageException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
