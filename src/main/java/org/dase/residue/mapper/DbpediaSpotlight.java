package org.dase.residue.mapper;
/*
Written by sarker.
Written at 11/11/19.
*/

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.*;
import okio.BufferedSink;
import org.apache.commons.csv.CSVParser;
import org.dase.ecii.util.Utility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DbpediaSpotlight {

    private OWLOntology owlOntology;
    private OWLDataFactory owlDataFactory;
    private OWLOntologyManager owlOntologyManager;

    String pathToSave = "/Users/sarker/Workspaces/Jetbrains/residue/data/KGS/IFP_only_instances_v2.owl";
    String csvPath = "/Users/sarker/Workspaces/Jetbrains/residue/experiments/KG-based similarity/6_IFP/select_IFP_data.csv";
    String jsonInputPath = "/Users/sarker/Workspaces/Jetbrains/residue/experiments/KG-based similarity/6_IFP/select_IFP_data.json";
    String jsonOutputPath = "/Users/sarker/Workspaces/Jetbrains/residue/experiments/KG-based similarity/6_IFP/select_IFP_data_updated.json";

    OkHttpClient client = new OkHttpClient();
    String base_url = "curl -i -X POST -H \"Accept:application/json\"  -H \"content-type:application/x-www-form-urlencoded\" https://api.dbpedia-spotlight.org/en/annotate?text=\"";

    /**
     * @param url
     * @return
     */
    public String getDbpediaJson(String url, String requestStr) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.MIXED)
                .addFormDataPart("text", requestStr)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Accept", "application/json")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void process_json() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(jsonInputPath));

            Gson gson = new GsonBuilder().setLenient().create();
//            gson.newJsonReader().setLenient();
            JsonElement jsonRootElement = gson.fromJson(bufferedReader, JsonElement.class);

//            System.out.println(jsonRootElement.getClass());
//            System.out.println(jsonRootElement.toString());
            for (JsonElement jsonElement : jsonRootElement.getAsJsonArray()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                System.out.println(jsonObject.get("ifpId"));
                String searchStr = jsonObject.get("ifp_document").getAsString();

//                searchStr = base_url + searchStr;

                /**
                 * curl -i -X POST "http://model.dbpedia-spotlight.org/en/annotate?text=President%20Michelle%20Obama%20called%20Thursday%20on%20Congress%20to%20extend%20a%20tax%20break%20for%20students%20included%20in%20last%20year%27s%20economic%20stimulus%20package,%20arguing%20that%20the%20policy%20provides%20more%20generous%20assistance.&confidence=0.2&support=20" -H "Accept:application/json"  -H "content-type:application/x-www-form-urlencoded"
                 */


                String cmd = base_url + searchStr + "\"";
                Process process = Runtime.getRuntime().exec(cmd);
//                String response = process.getInputStream().toString();  //getDbpediaJson(base_url, searchStr);
//                if (null != response) {
//                    System.out.println("rawresponse: " + response);

                Reader reader = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);

                JsonElement jsonResponseElement = gson.fromJson(reader, JsonElement.class);
                System.out.println("jsonResponseElement: " + jsonResponseElement);
//                jsonObject.add("dbpedia_refs", jsonResponseElement);
//                    System.out.println("jsonObject: " + jsonObject);
//                }
                reader.close();
            }
//            jsonRootElement.getAsJsonArray().forEach(jsonElement -> {
//
//            });

            Writer writer = new FileWriter(jsonOutputPath);
            gson.toJson(jsonRootElement, writer);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {

            DbpediaSpotlight dbpediaSpotlight = new DbpediaSpotlight();
            dbpediaSpotlight.process_json();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
