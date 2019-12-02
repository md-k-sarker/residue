package org.dase.util;
/*
Written by sarker.
Written at 9/18/19.
*/

import org.dase.ecii.util.Utility;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Property_selector {



    public static void main(String [] args) {
        String dbpedia_schema_path = "/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/dbpedia_2016-10.owl";
        String property_file_path = "/Users/sarker/Workspaces/Jetbrains/residue/data/dbpedia_properties_v1.txt";
        try {
            OWLOntology ontology = Utility.loadOntology(dbpedia_schema_path);
            FileWriter fileWriter = new FileWriter(property_file_path);


            List<OWLObjectProperty> props_sorted =  ontology.getObjectPropertiesInSignature().stream().sorted((o1, o2) ->
                o1.getIRI().getShortForm().compareTo(o2.getIRI().getShortForm())).collect(Collectors.toList());


            props_sorted.forEach(objprop -> {
                        try {
                            fileWriter.write(objprop.getIRI().getShortForm() +"\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            fileWriter.close();
        }catch (Exception e){

        }
    }
}
