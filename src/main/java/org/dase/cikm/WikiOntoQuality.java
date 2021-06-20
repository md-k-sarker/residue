package org.dase.cikm;
/*
Written by sarker.
Written at 5/12/20.
*/

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.dase.ecii.util.Utility;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class WikiOntoQuality {


    OWLOntology owlOntology;

    public OWLOntology getOwlOntology() {
        return owlOntology;
    }

    public void setOwlOntology(OWLOntology owlOntology) {
        this.owlOntology = owlOntology;
    }


    public WikiOntoQuality() {

    }

    /**
     * Read ontology and extract classes from the ontology
     *
     * @param owlOntology
     * @return
     */
    public HashSet<OWLClass> extractOWLClasses(OWLOntology owlOntology) {
        try {
            HashSet<OWLClass> owlClasses = new HashSet<>(owlOntology.getClassesInSignature());

            // to restrict dbpedia classes, which are originally part of other ontology, such as, foaf, wikidata etc
//            HashSet<OWLClass> owlClassesOnlydbo = owlClasses.stream().filter(owlClass -> owlClass.getIRI().getNamespace().startsWith("http://dbpedia.org/ontology/")).collect(Collectors.toCollection(HashSet::new));

            return owlClasses;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
            return null;
        }
    }

    /**
     * Read the concepts names from both csv files, match concept names and if matches write to output csv
     *
     * @param dbpediaCSVPath
     * @param wikiCSVPath
     * @return
     */
    public boolean saveMatchingNamesToCSV(String dbpediaCSVPath, String wikiCSVPath, String outputCSVPath) {
        try {
            CSVParser csvRecordsDbpedia = Utility.parseCSV(dbpediaCSVPath, true);
            CSVParser csvRecordsWiki = Utility.parseCSV(wikiCSVPath, true);

            HashMap<String, String> dbpediaHashMap = new HashMap<>();
            HashMap<String, String> wikiHashMap = new HashMap<>();

            csvRecordsDbpedia.forEach(strings -> {
                String className = strings.get(1);
                String pClassName = strings.get(3);
                if (null != className && className.length() > 0) {
                    if (pClassName != null && pClassName.length() > 0) {
                        dbpediaHashMap.put(className, pClassName);
                    }
                }
            });

            csvRecordsWiki.forEach(strings -> {
                String className = strings.get(1);
                String pClassName = strings.get(3);
                if (null != className && className.length() > 0) {
                    if (pClassName != null && pClassName.length() > 0) {
                        wikiHashMap.put(className, pClassName);
                    }
                }
            });

            System.out.println("Dbpedia concepts hashMap size: " + dbpediaHashMap.size());
            System.out.println("Wiki concepts hashMap size: " + wikiHashMap.size());

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputCSVPath));
            CSVPrinter csvPrinter = new CSVPrinter(bufferedWriter,
                    CSVFormat.DEFAULT.withHeader("concept_name", "wiki_parent_concept", "dbpedia_parent_concept"));

            int same_counter = 0;
            int different_counter = 0;
            for (Map.Entry entry : dbpediaHashMap.entrySet()) {
                // also exists in wikipedia?
                if (wikiHashMap.containsKey(entry.getKey())) {
                    try {
                        csvPrinter.printRecord(entry.getKey(), wikiHashMap.get(entry.getKey()), entry.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    same_counter++;
                } else {
                    different_counter++;
                }
            }

            csvPrinter.flush();
            csvPrinter.close();
            bufferedWriter.close();

            System.out.println("same_counter: " + same_counter);
            System.out.println("different_counter: " + different_counter);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * If a concept's (which appears in both dbpedia and wikipedia) parent appears in both dbpedia and in wikipedia.
     *
     * @param parentConceptName
     * @param wikiParentConcepts
     * @return
     */
    public boolean existInParentsPath(String parentConceptName, HashSet<String> wikiParentConcepts) {
        System.out.println("");
        if (wikiParentConcepts.contains(parentConceptName))
            return true;
        else
            return false;
    }

    /**
     * create concept parent map by reading concents from csv file. csv files column must in the order:
     * column 0: concept name
     * column 1: wiki parent names
     * column 2: dbpedia parent names
     *
     * @param csvPath
     * @param isWiki
     * @return
     */
    public HashMap<String, HashSet<String>> getConceptParentMapFromCSV(String csvPath, boolean isWiki) {
        HashMap<String, HashSet<String>> conceptParentMap = new HashMap<>();

        CSVParser csvRecord = Utility.parseCSV(csvPath, true);

        AtomicInteger csvRecordSize = new AtomicInteger();

        if (null != csvRecord) {
            csvRecord.forEach(strings -> {
                csvRecordSize.getAndIncrement();
                String conceptName = strings.get(0);
                String parentName = "";
                if (isWiki) {
                    parentName = strings.get(1);
                } else {
                    parentName = strings.get(2);
                }

                String[] parentNames = parentName.split(":");
                HashSet<String> parentNamesHashSet = new HashSet();
                if (parentNames != null) {

                    for (int i = 0; i < parentNames.length; i++) {
                        parentNamesHashSet.add(parentNames[i]);
                    }
                }
                conceptParentMap.put(conceptName, parentNamesHashSet);
            });
        }
        System.out.println("Total row in csv: " + csvRecordSize.get());
        try {
            csvRecord.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conceptParentMap;
    }

    public double countExistInParentsPath(String csvPath) {
        AtomicInteger existInParentsPathCounter = new AtomicInteger();
        System.out.println("CSV path: " + csvPath);

        HashMap<String, HashSet<String>> wikiConceptParentMap = getConceptParentMapFromCSV(csvPath, true);
        System.out.println("wikiConceptParentMap size: " + wikiConceptParentMap.size());

        HashMap<String, HashSet<String>> dbpediaConceptParentMap = getConceptParentMapFromCSV(csvPath, false);
        System.out.println("dbpediaConceptParentMap size: " + dbpediaConceptParentMap.size());

        dbpediaConceptParentMap.forEach((dbpediaConceptName, dbpediaParentNames) -> {
            dbpediaParentNames.forEach(dbpediaEachParentName -> {
                if (existInParentsPath(dbpediaEachParentName, wikiConceptParentMap.get(dbpediaConceptName))) {
                    existInParentsPathCounter.getAndIncrement();
                }
            });
        });


        AtomicInteger total_wiki_concept_parent_relation_size = new AtomicInteger();
        wikiConceptParentMap.values().forEach(strings -> {
            strings.forEach(eachParentConceptName -> {
                total_wiki_concept_parent_relation_size.getAndIncrement();
            });
        });

        AtomicInteger total_dbpedia_concept_parent_relation_size = new AtomicInteger();
        dbpediaConceptParentMap.values().forEach(strings -> {
            strings.forEach(eachParentConceptName -> {
                total_dbpedia_concept_parent_relation_size.getAndIncrement();
            });
        });


        System.out.println("existInParentsPathCounter: " + existInParentsPathCounter.get());
        System.out.println("total_wiki_concept_parent_relation_size: " + total_wiki_concept_parent_relation_size.get());
        System.out.println("total_dbpedia_concept_parent_relation_size: " + total_dbpedia_concept_parent_relation_size.get());

        double ratio = existInParentsPathCounter.get() / total_dbpedia_concept_parent_relation_size.get();
        System.out.println("Ratio of exist/total_concepts: " + ratio);

        return ratio;
    }

    /**
     * @param owlClassExpression
     * @param shortNameSB
     */
    public void recursiveSuperType(OWLClassExpression owlClassExpression, StringBuilder shortNameSB) {
        if (((OWLClass) owlClassExpression).equals(
                this.owlOntology.getOWLOntologyManager().getOWLDataFactory().getOWLThing())) {
            return;
        } else {
            EntitySearcher.getSuperClasses((OWLClass) owlClassExpression, owlOntology).forEach(superClass -> {
                //System.out.println("\t\t supertype: " + superClass);
                if (superClass instanceof OWLClass && !superClass.equals(
                        this.owlOntology.getOWLOntologyManager().getOWLDataFactory().getOWLThing())) {
//                    fullNameSB.append(":" + superClass.toString());
                    shortNameSB.append(":" + ((OWLClass) superClass).getIRI().getShortForm());
                    recursiveSuperType(superClass, shortNameSB);
                }
            });
        }
    }


    /**
     * Find the parent class of each owl class and then save the class and the parent.
     *
     * @param owlOntology
     * @param owlClasses
     * @param csvPath
     * @return
     */
    public boolean saveConceptNamesToCSV(OWLOntology owlOntology, HashSet<OWLClass> owlClasses, String csvPath) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(csvPath));
            CSVPrinter csvPrinter = new CSVPrinter(bufferedWriter, CSVFormat.DEFAULT.withHeader("wiki_class", "wiki_class_short_name", "wiki_parent_class", "wiki_parent_class_short_name"));


            try {
                for (OWLClass owlClass : owlClasses) {
                    String className = owlClass.toString();
                    String classShortName = owlClass.getIRI().getShortForm();

                    String pClassName = "";
                    String pClassShortName = "";

                    // parent classes
                    OWLClass parentClass = null;
                    HashSet<OWLClassExpression> owlClassExpressions = new HashSet<>(
                            EntitySearcher.getSuperClasses(owlClass, owlOntology));

                    for (OWLClassExpression owlClassExpression : owlClassExpressions) {
                        if (owlClassExpression instanceof OWLClass) {
                            parentClass = (OWLClass) owlClassExpression;
                        }
                    }

                    StringBuilder fullNameSB = new StringBuilder();
                    StringBuilder shortNameSB = new StringBuilder();

                    if (null != parentClass) {
                        pClassName = parentClass.toString();
                        pClassShortName = parentClass.getIRI().getShortForm();
                        fullNameSB.append(pClassName);
                        shortNameSB.append(pClassShortName);
                        recursiveSuperType(parentClass, shortNameSB);

                        System.out.println("sb-short: " + shortNameSB);

//                    if (!pClassName.equals("owl:Thing"))
                        csvPrinter.printRecord(className, classShortName, pClassName, shortNameSB);
                    }

                }
//                HashSet<String> shortNames = owlClasses.stream().map(owlClass -> owlClass.getIRI().getShortForm()).collect(Collectors.toCollection(HashSet::new));
//
//                csvPrinter.printRecords(owlClasses, shortNames);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }

            csvPrinter.flush();
            csvPrinter.close();
            bufferedWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
            return false;
        }
    }


    static String dbpediaOntoPath = "/Users/sarker/Dropbox/2020-CIKM-Wiki hierarchy/experiments/dataset/dbpedia_2016-10.owl";
    static String wikiOntoPath = "/Users/sarker/Workspaces/Jetbrains/residue-emerald/residue/data/KGS/automated_wiki/wiki_cats_v1_non_cyclic.owl";
    static String wikiCSVPath = "/Users/sarker/Dropbox/2020-CIKM-Wiki hierarchy/experiments/dataset/wiki_classes.csv";
    static String dbpediaCSVPath =
            "/Users/sarker/Dropbox/2020-CIKM-Wiki hierarchy/experiments/dataset/dbpedia_classes_only_dbo_pclass_not_owl_thing.csv";

    static String sameClassesCSVPath = "/Users/sarker/Dropbox/2020-CIKM-Wiki hierarchy/experiments/dataset/classes_found_in_both_dbpedia_and_wiki_all_parent.csv";


    public static void main(String[] args) {
        WikiOntoQuality wikiOntoQuality = new WikiOntoQuality();

        OWLOntology owlOntology = null;

        try {
            // ops 1
//            owlOntology = Utility.loadOntology(dbpediaOntoPath);
//            wikiOntoQuality.setOwlOntology(owlOntology);
//            HashSet<OWLClass> dbpediaOwlClasses = wikiOntoQuality.extractOWLClasses(owlOntology);
//            wikiOntoQuality.saveConceptNamesToCSV(owlOntology, dbpediaOwlClasses, dbpediaCSVPath);
//
//            owlOntology = Utility.loadOntology(wikiOntoPath);
//            wikiOntoQuality.setOwlOntology(owlOntology);
//            HashSet<OWLClass> wikiOwlClasses = wikiOntoQuality.extractOWLClasses(owlOntology);
//            wikiOntoQuality.saveConceptNamesToCSV(owlOntology, wikiOwlClasses, wikiCSVPath);

            // ops 2
//            wikiOntoQuality.saveMatchingNamesToCSV(dbpediaCSVPath, wikiCSVPath, sameClassesCSVPath);

            // ops 3
            wikiOntoQuality.countExistInParentsPath(sameClassesCSVPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
