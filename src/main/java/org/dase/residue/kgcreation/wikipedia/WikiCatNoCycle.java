package org.dase.residue.kgcreation.wikipedia;
/*
Written by sarker.
Written at 11/16/19.
*/


import org.apache.commons.lang3.StringUtils;
import org.dase.ecii.util.Utility;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
//import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.*;

import java.sql.*;
import java.util.*;

/**
 * Signature of subclass : getOWLSubClassOfAxiom(OWLClassExpression subClass, OWLClassExpression superClass)
 */

/**
 * Process wiki category from the db dump and make the category hierarchy ontology, without breaking the cycle.
 */
public class WikiCatNoCycle {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSetPages = null;
    private ResultSet resultSetCat = null;
    private String rootCategoryName = "Main_topic_classifications";
    private String rootCategoryNameIncludingAdminInfo = "Contents";

    private HashMap<Long, String> pageIdToTitleMap = new HashMap<>();
    private HashMap<Long, HashSet<String>> catIdToParentsCat = new HashMap<>();

    private OWLOntology owlOntology;
    //    private TurtleDocumentFormat turtleDocumentFormat;
    private OWLXMLDocumentFormat owlxmlDocumentFormat;
    private OWLDataFactory owlDataFactory;
    private OWLOntologyManager owlOntologyManager;
    private int counter = 0;

    String pathToSave = "/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/automated_kg_wiki/wiki_full_cats_v0_non_cyclic_jan_20_";
    String onto_prefix = "http://www.daselab.com/residue/analysis#";

    public void fillCatHierHashMap(ResultSet resultSetCat) {
        try {
            while (resultSetCat.next()) {
                Long id = resultSetCat.getLong("cl_from");
                String name = resultSetCat.getString("cl_to");
                if (null != name) {
                    name = beautifyName(name);
                    if (catIdToParentsCat.containsKey(id)) {
                        catIdToParentsCat.get(id).add(name);
                    } else {
                        HashSet namesHashSet = new HashSet();
                        namesHashSet.add(name);
                        catIdToParentsCat.put(id, namesHashSet);
                    }
                } else {
                    System.out.println("category cl_to is null for id: " + id + " skipping");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void cachePageTitles() {
        try {
            String pageTitleQuery = "select page_id, page_title from wiki_pages.page where wiki_pages.page.page_namespace=14";
            System.out.println("Query '" + pageTitleQuery + "' started.............. ");
            resultSetPages = statement.executeQuery(pageTitleQuery);
            System.out.println("Query '" + pageTitleQuery + "' successfull. ");
            System.out.println("fillIdToTitleHashMap started..........");
            fillIdToTitleHashMap(resultSetPages);
            System.out.println("fillIdToTitleHashMap finished. size of hashMap " + pageIdToTitleMap.size());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param resultSetPages
     */
    public void fillIdToTitleHashMap(ResultSet resultSetPages) {
        System.out.println("initially pageIdToTitleMap size: "+ pageIdToTitleMap.size());
        try {
            while (resultSetPages.next()) {
                Long page_id = resultSetPages.getLong("page_id");
                String page_title = resultSetPages.getString("page_title");
                if (null != page_id && null != page_title) {
                    pageIdToTitleMap.put(page_id, beautifyName(page_title));
                } else {
                    System.out.println("ID or Title is null, skipping.. id: " + page_id + "\t title: " + page_title);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("after filling pageIdToTitleMap size: "+ pageIdToTitleMap.size());
    }


    /**
     *
     */
    public void traverseDataBaseBFS() throws Exception {

        String catLinksQuery = "";
        String pageTitleQuery = "";

        try {

            LinkedHashSet<String> catsQueue = new LinkedHashSet<>();
            HashSet<String> visitedCatTitles = new HashSet<>();
            catsQueue.add(rootCategoryNameIncludingAdminInfo);
            int level = 0;

            System.out.println("BFS started from the root node: " + rootCategoryNameIncludingAdminInfo);
            while (!catsQueue.isEmpty()) {

                String parentCategoryName = catsQueue.iterator().next();
                catsQueue.remove(parentCategoryName);
//                counter++;
                if (!visitedCatTitles.contains(parentCategoryName)) {

                    try {
                        visitedCatTitles.add(parentCategoryName);
                        if (parentCategoryName.contains("'")) {
                            catLinksQuery = "select cl_from, cl_to from wiki_cats.categorylinks where " +
                                    "wiki_cats.categorylinks.cl_to=\"" + parentCategoryName + "\" and wiki_cats.categorylinks.cl_type=\"subcat\"";
                        } else {
                            catLinksQuery = "select cl_from, cl_to from wiki_cats.categorylinks where " +
                                    "wiki_cats.categorylinks.cl_to='" + parentCategoryName + "' and wiki_cats.categorylinks.cl_type=\"subcat\"";
                        }
                        if (counter % 100000 == 0)
                            System.out.println("Counter: " + counter + "\t Queue size: " + catsQueue.size() +
                                    "\t Visited size: " + visitedCatTitles.size() + "\t Query '" + catLinksQuery + "' started.............. ");
                        resultSetCat = statement.executeQuery(catLinksQuery);
                        if (counter % 100000 == 0)
                            System.out.println("Counter: " + counter + "\t Queue size: " + catsQueue.size() +
                                    "\t Visited size: " + visitedCatTitles.size() + "\t Query '" + catLinksQuery + "'  successfull. ");

                        // cache the resultSetCat
                        HashSet<Long> childIdsHashSet = new HashSet<>();
                        while (resultSetCat.next()) {
                            Long child_Id = resultSetCat.getLong("cl_from");
                            if (null != child_Id)
                                childIdsHashSet.add(child_Id);
                        }

                        // get all children of this parent, parentCategoryName
                        for (Long child_Id : childIdsHashSet) {

                            String childCategoryName = pageIdToTitleMap.get(child_Id);
                            if (null != childCategoryName) {
                                // create relation
                                // we are breaking cycles, so we are not allowing relation  with already visited node as child node.
                                if (!visitedCatTitles.contains(childCategoryName)) {
                                    createRelation(childCategoryName, parentCategoryName);
                                    // do we need to visit this child?
                                    if (!catsQueue.contains(childCategoryName))
                                        catsQueue.add(childCategoryName);
                                }
                            }
                            counter++;
//                                if (counter > 200)
//                                    return;
                        }
                    } catch (SQLException ex) {
                        System.out.println("Exception in executing query....skipping this query");
                        System.out.println("catlinkQuery: " + catLinksQuery);
                        System.out.println("pageTitleQuery: " + pageTitleQuery);
                    }
                }
            }

            System.out.println("BFS finished. counter " + counter);
        } catch (Exception e) {
            System.out.println("Exception!!!!!!!!!");
            System.out.println("catlinkQuery: " + catLinksQuery);
            System.out.println("pageTitleQuery: " + pageTitleQuery);
            e.printStackTrace();
        } finally {
//            closeConnections();
        }
    }

    // You need to closeConnections the resultSet
    private void closeConnections() {
        try {
            System.out.println("Closing db connections..........");
            if (resultSetPages != null) {
                resultSetPages.close();
            }
            if (resultSetCat != null) {
                resultSetCat.close();
            }
            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
            System.out.println("Closing db connections successfull");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String trimOrReplaceSearchChars = " `~!@#$%^&*()-+={}[]|\\;'\"<>,.?/";
    // length of replaceChars must be same with trimOrReplaceSearchChars
    // https://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/StringUtils.html#replaceChars(java.lang.String,%20java.lang.String,%20java.lang.String)
    private String replaceChars = "_______________________________";

    public String beautifyName(String name) {
        String trimmed = StringUtils.strip(name, trimOrReplaceSearchChars);
        return StringUtils.replaceChars(trimmed, trimOrReplaceSearchChars, replaceChars);
    }

    /**
     * create a single subclassOf relation
     *
     * @param cName
     * @param pName
     */
    private void createRelation(String cName, String pName) {
        IRI cIRI = IRI.create(onto_prefix + beautifyName(cName));
        IRI pIRI = IRI.create(onto_prefix + beautifyName(pName));

        OWLClass cClass = owlDataFactory.getOWLClass(cIRI);
        OWLClass pClass = owlDataFactory.getOWLClass(pIRI);

        OWLAxiom owlAxiom = owlDataFactory.getOWLSubClassOfAxiom(cClass, pClass);

        owlOntologyManager.addAxiom(owlOntology, owlAxiom);
    }

    public void saveOntoToFile() {
        String finalPathToSave = pathToSave + counter + ".owl";
        System.out.println("\nSaving to " + finalPathToSave + " started...........");
        try {
            Utility.saveOntology(owlOntology, owlxmlDocumentFormat, finalPathToSave);
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }
        System.out.println("\nSaving to " + finalPathToSave + " successfull");
    }

    public void initData() {
        owlOntologyManager = OWLManager.createOWLOntologyManager();
        try {
            owlOntology = owlOntologyManager.createOntology(IRI.create("http://www.daselab.com/residue/analysis"));
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
//        turtleDocumentFormat = new TurtleDocumentFormat();
        owlxmlDocumentFormat = new OWLXMLDocumentFormat();
        try {
            System.out.println("connecting to db................ ");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/wiki_cats", "smk", "suba");
            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            System.out.println("connecting to db successfull");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        WikiCatNoCycle wikiCatNoCycle = new WikiCatNoCycle();
        wikiCatNoCycle.initData();

        final long readDatabaseStartTime = System.currentTimeMillis();
        wikiCatNoCycle.cachePageTitles();
        wikiCatNoCycle.traverseDataBaseBFS();
        final long readDatabaseEndTime = System.currentTimeMillis();
        System.out.println("Databse read+traverse time: " + (readDatabaseEndTime - readDatabaseStartTime) / 60000 + " minutes");

        wikiCatNoCycle.closeConnections();

        final long saveOntologyStartTime = System.currentTimeMillis();
        wikiCatNoCycle.saveOntoToFile();
        final long saveOntologyEndTime = System.currentTimeMillis();
        System.out.println("Save ontology time: " + (saveOntologyEndTime - saveOntologyStartTime) / 60000 + " minutes");
    }

}