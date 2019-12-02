<%@ page isThreadSafe="true" language="java" contentType="text/html" %>
<%@ page import="org.dase.ecii.core.CandidateSolutionFinder" %>
<%@ page import="org.dase.ecii.core.SharedDataHolder" %>
<%@ page import="org.dase.ecii.datastructure.CandidateClass" %>
<%@ page import="org.dase.ecii.datastructure.CandidateSolution" %>

<%--import org.dase.ecii.core.CandidateSolutionFinder;--%>
<%--import org.dase.ecii.core.SharedDataHolder;--%>
<%--import org.dase.ecii.datastructure.CandidateClass;--%>
<%--import org.dase.ecii.datastructure.CandidateSolution;--%>
<%--import org.dase.ecii.exceptions.MalFormedIRIException;--%>
<%--import org.dase.ecii.ontofactory.DLSyntaxRendererExt;--%>
<%--import org.dase.parser.DLSyntaxParser;--%>
<%--import org.dase.ieciiutil.ConfigParams;--%>
<%--import org.dase.ieciiutil.Monitor;--%>
<%--import org.dase.ieciiutil.Utility;--%>
<%--import org.semanticweb.owlapi.apibinding.OWLManager;--%>
<%--import org.semanticweb.owlapi.model.*;--%>
<%--import org.semanticweb.owlapi.reasoner.OWLReasoner;--%>
<%--import org.slf4j.Logger;--%>
<%--
 <%@ import
 <%! declaration
 <% script
 <%= assign %>
 <%-- jsp comment --%>
 <!-- html comment -->
<%-- import org.slf4j.LoggerFactory;--%>



<!DOCTYPE html>
<html lang="en">
<%--<jsp:forward page="/test" />--%>
<meta>
    <title>Interactive ECII</title>
    <meta charset="utf-8">
    <meta  name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../css/bootstrap-4.3.1/bootstrap.min.css">
    <script src="../js/bootstrap-4.3.1/bootstrap.min.js"></script>
    <script src="../js/jquery-3.4.1.min.js"></script>

    <style>
        /* Remove the navbar's default margin-bottom and rounded borders */
        /* .navbar {
          margin-bottom: 0;
          border-radius: 0;
        } */

        /* Set height of the grid so .sidenav can be 100% (adjust as needed) */
        .row.content {height: auto}

        /* Set gray background color and 100% height */
        /* .sidenav {
          padding-top: 20px;
          background-color: #f1f1f1;
          height: 100%;
        } */

        /* Set black background color, white text and some padding */
        /* footer {
          background-color: #555;
          color: white;
          padding: 5px;
        } */

        /* On small screens, set height to 'auto' for sidenav and grid */
        @media screen and (max-width: 767px) {
            .sidenav {
                height: auto;
                padding: 15px;
            }
            .row.content {height:auto;}
        }

        .list-group{
            max-height: 300px;
            margin-bottom: 10px;
            overflow-y:auto;
            -webkit-overflow-scrolling: touch;
        }
    </style>
</head>
<body>


<div class="container">
<div class="col-sm-2" id="left-col"></div>
<div class="col-sm-8" id="center-col"></div>
    <div class="container text-cneter" id="settings">
        <div class="col-sm-2">
            <%! CandidateSolution candidateSolution = new CandidateSolution();  %>
            <% config.getInitParameterNames().nextElement(); %>
        </div>
        <div class="=col-sm-8">
            <form action="/ontology_loader_servlet" method="post" id="load_ontology_form" role="form">
            <div class="input-group">
                <span class="input-group-addon">Ontology:</span>
                <input type="text" class="form-control" id="input-ontology">
                <button type="submit" class="btn btn-info">
                    <span class="glyphicon glyphicon-search"></span> Browse
                </button>
            </div>
            </form>
        </div>
        <div class="col-sm-2">
            <%--        Make a drop down list for renderer: <p></p>--%>
        </div>

    </div>
    <div class="container-fluid text-left" id="second_row">
        <div class="row content">
            <div class="col-sm-2">

                <h4>Positive Individuals</h4>
                <ul class="list-group">
<%--   https://stackoverflow.com/questions/47256185/bootstrap-list-group-scroll--%>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                </ul>
            </div>
            <div class="col-sm-2">
                <h4>Negative Individuals</h4>
                <ul class="list-group">
                    <li class="list-group-item">a1</li>
                </ul>
            </div>
            <div class="col-sm-6">
                <div class="row content" id="symbols">
                    <h3>Symbols will be here</h3>
                </div>
                <div class="input-group" id="input">
                    <span class="input-group-addon">Concept:</span>
                    <input type="text" class="form-control" id="input_raw_concept">

                    <button type="submit" class="btn btn-info">
                        <span class="glyphicon glyphicon-search"></span> Finetune
                    </button>
                </div>
            </div>
            <div class="col-sm-2">
                <h3>Precision</h3>
                <h3>Recall</h3>
                <h3>Accuracy</h3>
            </div>
        </div>
    </div>
    <div class="container-fluid text-left" id="third_row">
        <div class="row content">
            <div class="col-sm-4">
                <h4>All Individuals</h4>
                <ul class="list-group">
                    <%--   https://stackoverflow.com/questions/47256185/bootstrap-list-group-scroll--%>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                    <li class="list-group-item">a1</li>
                </ul>
            </div>
            <div class="col-sm-4">
                <h4>All Classes</h4>
                <ul class="list-group">
                    <li class="list-group-item">a1</li>
                </ul>
            </div>
            <div class="col-sm-4">
                <h4>All Object Properties</h4>
                <ul class="list-group">
                    <li class="list-group-item">a1</li>
                </ul>
            </div>
        </div>
    </div>

    <div class="container-fluid text-left" id="fourth_row">
        <div class="row content">
            <div class="col-sm-12">
                <h4>Prefixes</h4>
                <table class="table">
                    <thead>
                    <tr>
                        <th>Prefix</th>
                        <th>Value</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>John</td>
                        <td>Doe</td>
                    </tr>
                    <tr>
                        <td>Mary</td>
                        <td>Moe</td>
                    </tr>
                    <tr>
                        <td>July</td>
                        <td>Dooley</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <footer class="container-fluid text-center">
        <div class="col-md-1 sidenav">
            <!--  -->
        </div>
        <div class="col-md-10">
            <p>Please contact <a  href="mailto:mdkamruzzamansarker@ksu.edu?Subject=Interactive%20ECII">Md Kamruzzaman Sarker</a> if you have question.</p>
        </div>
        <div class="col-md-1 sidenav">
            <!--  -->
        </div>

</footer>
<div class="col-sm-2" id="right-col"></div>
</div>
</body>
</html>
