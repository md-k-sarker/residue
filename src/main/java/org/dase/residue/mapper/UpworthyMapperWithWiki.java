package org.dase.residue.mapper;
/*
Written by sarker.
Written at 10/5/20.
*/

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.dase.ecii.util.Utility;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Map upworhty text entity with the wikipedia kg
 */
public class UpworthyMapperWithWiki {

    final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private String trimOrReplaceSearchChars = " `~!@#$%^&*()-+={}[]|\\;'\"<>,.?/";
    // length of replaceChars must be same with trimOrReplaceSearchChars
    // https://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/StringUtils.html#replaceChars(java.lang.String,%20java.lang.String,%20java.lang.String)
    private String replaceChars = "_______________________________";

    public String beautifyName(String name) {
        String trimmed = StringUtils.strip(name, trimOrReplaceSearchChars);
        return StringUtils.replaceChars(trimmed, trimOrReplaceSearchChars, replaceChars);
    }

    /**
     * Return the values of column bag_of_words in a arraylist
     * Order is important, first item in arraylist denotes the first row of the csv file
     *
     * @param csvPath
     * @return
     */
    public ArrayList<HashSet<String>> readBagOfWords(String csvPath, String columnName) {

        logger.info("Starting readBagOfWords...........");
        ArrayList<HashSet<String>> bagOfWords = new ArrayList<>();

        CSVParser parser = Utility.parseCSV(csvPath, true);

        for (CSVRecord strings : parser) {
            // get a row value
            String value = strings.get(columnName);

            if (null != value && value.length() > 0) {
                HashSet<String> row_values = new HashSet<>();

                value = value.replaceAll("\\[|\\]", "");
//                System.out.println("value: "+ value);
                String[] values = value.split(",");
                for (String v : values) {
                    if (v.length() > 0) {
                        v = v.replaceAll("'", "").trim();
                        v = beautifyName(v);

                        row_values.add(v);
                    }
                }
                bagOfWords.add(row_values);
//                System.out.println("row_values.length: "+ row_values.size());
            }
        }
        logger.info("Finished readBagOfWords.");
        logger.info("bagOfWords size after parsing csv : " + bagOfWords.size());
        return bagOfWords;
    }

    /**
     * Tes method readBagOfWords
     *
     * @param bagOfWords
     */
    public void testReadBagOfWords(ArrayList<HashSet<String>> bagOfWords) {
        bagOfWords.forEach(textEntities -> {
            HashSet<String> rowEntities = new HashSet<>();
            System.out.print("\nrow entities size: " + textEntities.size());
            textEntities.forEach(entityName -> {
//                if (indivs.containsKey(entityName.toLowerCase())) {
//                    rowEntities.add(indivs.get(entityName.toLowerCase()));
//                }
                System.out.print(entityName + " ");
            });
        });
    }

    /**
     * Read individuals from ontology and return the hashmap<indiv_lowercase, indiv>
     * <p>
     * unicode error on osf uploaded file
     * error: http://www.daselab.org/ontologies/wiki#
     * file: wiki_pages_with_category_v1.owl
     * <p>
     * main source of error:
     * search on the file for string:  Redirects_from_Unicode_characters
     *
     * @param ontoPath
     * @param prefix
     * @return
     */
    public HashMap<String, String> readOntoIndivs(String ontoPath, String prefix) {
        logger.info("Starting readOntoIndivs...........");
        HashMap<String, String> indivs = new HashMap<>();

        OWLOntology owlOntology = null;
        try {
            owlOntology = Utility.loadOntology(ontoPath, false);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Program exiting........");
            System.exit(-1);
        }
        logger.info("indivsHashMap creating ..........");
        owlOntology.getIndividualsInSignature().forEach(indiv -> {
            String name = indiv.getIRI().toString().replaceAll(prefix, "");
            String lowerCaseName = name.toLowerCase();
            indivs.put(lowerCaseName, name);
        });
        logger.info("indivsHashMap creating successfull.");

        logger.info("Finished readOntoIndivs.");
        logger.info("indivs size after reading onto : " + indivs.size());
        return indivs;
    }

    /**
     * Write onto indivs to csv
     *
     * @param indivs
     * @param writeCSVPath
     */
    public void saveOntoIndivsToCSV(HashMap<String, String> indivs, String writeCSVPath) {
        logger.info("Indivs writing to csv starting........");

        ArrayList<String> writeColumnNames = new ArrayList<>();
        writeColumnNames.add("WikiPages");

        ArrayList<String> writeColumnValues = new ArrayList<>(indivs.keySet());

        // write to csv
        Utility.writeToCSV(writeCSVPath, writeColumnNames, writeColumnValues);
        logger.info("Indivs writing to csv finished");
    }

    /**
     * Map with wikipedia page-names
     *
     * @param indivs
     * @param bagOfWords
     * @param writeCSVPath
     */
    public void mapWithWikiPagename(HashMap<String, String> indivs, ArrayList<HashSet<String>> bagOfWords, String writeCSVPath) {
        logger.info("Starting mapWithWikiPagename.........");

        ArrayList<HashSet<String>> matchedEntities = new ArrayList<>();

        logger.info("Starting match finding............");
        // find matching
        bagOfWords.forEach(textEntities -> {
            HashSet<String> rowEntities = new HashSet<>();
            textEntities.forEach(entityName -> {
                if (indivs.containsKey(entityName.toLowerCase())) {
                    rowEntities.add(indivs.get(entityName.toLowerCase()));
                }
            });
            matchedEntities.add(rowEntities);
        });
        logger.info("Finished match finding.");

        int i = 1;
        // print to verify
        System.out.println("Matching entities------");
        for (HashSet<String> matchedEntity : matchedEntities) {
            System.out.print("row: " + i + ": ");
            matchedEntity.forEach(s -> {
                System.out.print(s + ", ");
            });
            System.out.println("");
            i++;
        }

        logger.info("Starting writing to csv..........");
        ArrayList<String> writeColumnNames = new ArrayList<>();
        writeColumnNames.add("matchedEntity");

        ArrayList<String> writeColumnValues = matchedEntities.stream().map(strings -> String.join(";", strings))
                .collect(Collectors.toCollection(ArrayList::new));

        // write to csv
        Utility.writeToCSV(writeCSVPath, writeColumnNames, writeColumnValues);
        logger.info("Writing to csv finished");

        logger.info("Finished mapWithWikiPagename.");
    }


    /**
     * Map with wikipedia category-names
     */
    public void mapWithWikiCategoryname() {
    }

    /**
     * driver to run the script like functions
     *
     * @param args
     */
    public static void main(String[] args) {

        logger.info("Program started..........");

        String ontology_path = "/Users/sarker/Downloads/osf-uploaded/wiki_pages_and_all_categories_v1.owl";
        String base_prefix = "http://www.daselab.org/ontologies/wiki#";
        String pos_csv = "/Users/sarker/Workspaces/Jetbrains/residue-emerald/emerald/data/upworthy/tokenized-by-Srikanth/upworthy_positive_headlines_lemmatized.csv";
        String neg_csv = "/Users/sarker/Workspaces/Jetbrains/residue-emerald/emerald/data/upworthy/tokenized-by-Srikanth/upworthy_negative_headlines_lemmatized.csv";
        String indivs_csv = "/Users/sarker/Workspaces/Jetbrains/residue-emerald/emerald/data/upworthy/tokenized-by-Srikanth/wiki_indivs.csv";

        String pos_csv_extend = pos_csv.replace(".csv", "_match.csv");
        String neg_csv_extend = neg_csv.replace(".csv", "_match.csv");

        UpworthyMapperWithWiki upworthyMapperWithWiki = new UpworthyMapperWithWiki();

        // load positive
        ArrayList<HashSet<String>> bagOfWordsPos = upworthyMapperWithWiki.readBagOfWords(pos_csv, "bag_of_words_lemmatized");

        // load negative
        ArrayList<HashSet<String>> bagOfWordsNeg = upworthyMapperWithWiki.readBagOfWords(neg_csv, "bag_of_words_lemmatized");

        // test entities
        // upworthyMapperWithWiki.testReadBagOfWords(bagOfWordsPos);

        // load onto indivs
        HashMap<String, String> indivs = upworthyMapperWithWiki.readOntoIndivs(ontology_path, base_prefix);
        // save ontoIndivs to csv
        // upworthyMapperWithWiki.saveOntoIndivsToCSV(indivs, indivs_csv);

        // map with positive
//        upworthyMapperWithWiki.mapWithWikiPagename(indivs, bagOfWordsPos, pos_csv_extend);

        // map with negative
        upworthyMapperWithWiki.mapWithWikiPagename(indivs, bagOfWordsNeg, neg_csv_extend);

        logger.info("Program finished.");
        // http://localhost:8888/notebooks/Workspaces/Jetbrains/residue-emerald/residue/python/preprocess-upworthy-positive-negative.ipynb

    }
}
