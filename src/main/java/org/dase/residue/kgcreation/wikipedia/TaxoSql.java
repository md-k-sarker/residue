package org.dase.residue.kgcreation.wikipedia;
/*
Written by sarker.
Written at 11/16/19.
*/


import org.dase.ecii.util.Utility;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class TaxoSql {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSetTaxonomy = null;
    private ResultSet resultSetCat = null;
    private HashMap<Integer, String> idToTitleMap = new HashMap<>();

    private OWLOntology owlOntology;
    private OWLDataFactory owlDataFactory;
    private OWLOntologyManager owlOntologyManager;
    private static int counter = 0;

    String pathToSave = "/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/automated_kg_wiki/wiki_v3_";
    String onto_prefix = "http://www.daselab.com/residue/analysis#";

    public void fillHashMap(ResultSet resultSetCat) {
        try {
            while (resultSetCat.next()) {
                Integer id = resultSetCat.getInt("id");
                String name = resultSetCat.getString("name");
                idToTitleMap.put(id, name);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void readDataBase() throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver
//            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/wiki", "smk", "suba");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query

            resultSetCat = statement.executeQuery("select * from wiki.wiki_cat");
            System.out.println("select * from wiki.wiki_cat successfull. ");
            System.out.println("fillCatHierHashMap started..........");
            fillHashMap(resultSetCat);
            System.out.println("fillCatHierHashMap finished.");

            resultSetTaxonomy = statement.executeQuery("select * from wiki.wiki_taxonomy");
            System.out.println("select * from wiki.wiki_taxonomy successfull. ");

            System.out.println("createRelations started..........");
            createRelations(resultSetTaxonomy);
            System.out.println("createRelations finished");

            String finalPathToSave = pathToSave + counter + ".owl";
            Utility.saveOntology(owlOntology, finalPathToSave);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close();
        }
    }

    private String beautifyName(String name) {
        return name.trim().replace(' ', '_').replace('<', '_').
                replace('>', '_').replace('&', '_').replace(';', '_').
                replace('#', '_').replace(',', '_').replace('(', '_').
                replace(')', '_').replace('{', '_').replace('}', '_');

    }

    private void createRelations(ResultSet resultSet) {
        try {
            while (resultSet.next()) {
                int p_id = resultSet.getInt("parent");
                int c_id = resultSet.getInt("child");

                String pName = idToTitleMap.get(p_id);
                String cName = idToTitleMap.get(c_id);
                if (pName != null && cName != null) {
                    createRelation(pName, cName);
                }
                counter++;
                if (counter % 10000 ==0){
                    System.out.println("Counter " + counter);
                }
                if (counter % 100000 == 0) {
                    String finalPathToSave = pathToSave + counter + ".owl";
                    System.out.println("Saving to " + finalPathToSave);
                    Utility.saveOntology(owlOntology, finalPathToSave);
                }
            }
        } catch (SQLException | OWLOntologyStorageException ex) {
            ex.printStackTrace();
        }

    }

    private void createRelation(String pName, String cName) {
        IRI pIRI = IRI.create(onto_prefix + beautifyName(pName));
        IRI cIRI = IRI.create(onto_prefix + beautifyName(cName));

        OWLClass pClass = owlDataFactory.getOWLClass(pIRI);
        OWLClass cClass = owlDataFactory.getOWLClass(cIRI);
        OWLAxiom owlAxiom = owlDataFactory.getOWLSubClassOfAxiom(pClass, cClass);

        owlOntologyManager.addAxiom(owlOntology, owlAxiom);
    }

    // You need to close the resultSet
    private void close() {
        try {
            if (resultSetTaxonomy != null) {
                resultSetTaxonomy.close();
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
        } catch (Exception e) {

        }
    }

    public void initData() {
        owlOntologyManager = OWLManager.createOWLOntologyManager();
        try {
            owlOntology = owlOntologyManager.createOntology(IRI.create("http://www.daselab.com/residue/analysis"));
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        owlDataFactory = owlOntologyManager.getOWLDataFactory();
    }

    public static void main(String[] args) throws Exception {
        TaxoSql taxoSql = new TaxoSql();
        taxoSql.initData();
        taxoSql.readDataBase();
    }


    private void writeMetaData(ResultSet resultSet) throws SQLException {
        //  Now get some metadata from the database
        // Result set get the result of the SQL query

        System.out.println("The columns in the table are: ");

        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            System.out.println("Column " + i + " " + resultSet.getMetaData().getColumnName(i));
        }
    }

    public void info() {
        // PreparedStatements can use variables and are more efficient
//            preparedStatement = connect.prepareStatement("insert into  feedback.comments values (default, ?, ?, ?, ? , ?, ?)");
        // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
        // Parameters start with 1
//            preparedStatement.setString(1, "Test");
//            preparedStatement.setString(2, "TestEmail");
//            preparedStatement.setString(3, "TestWebpage");
//            preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
//            preparedStatement.setString(5, "TestSummary");
//            preparedStatement.setString(6, "TestComment");
//            preparedStatement.executeUpdate();

//            preparedStatement = connect
//                    .prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from feedback.comments");
//            resultSet = preparedStatement.executeQuery();
//            createRelations(resultSet);

//            // Remove again the insert comment
//            preparedStatement = connect
//                    .prepareStatement("delete from feedback.comments where myuser= ? ; ");
//            preparedStatement.setString(1, "Test");
//            preparedStatement.executeUpdate();

//            resultSet = statement
//                    .executeQuery("select * from feedback.comments");
//            writeMetaData(resultSet);


        // also possible to get the columns via the column number
        // which starts at 1
        // e.g. resultSet.getSTring(2);
//            String user = resultSet.getString("myuser");
//            String website = resultSet.getString("webpage");
//            String summary = resultSet.getString("summary");
//            Date date = resultSet.getDate("datum");
//            String comment = resultSet.getString("comments");
//            System.out.println("User: " + user);
//            System.out.println("Website: " + website);
//            System.out.println("summary: " + summary);
//            System.out.println("Date: " + date);
//            System.out.println("Comment: " + comment);
    }

}