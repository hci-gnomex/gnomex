<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%@ page language="java" import="java.util.Iterator"%>
<%@ page language="java" import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.TreeMap"%>
<%@ page language="java" import="hci.gnomex.utility.ExperimentMatrixLinkInfo"%>

<!--  These are the session objects passed into this jsp -->
<jsp:useBean id="orgMap" class="java.util.TreeMap" scope="request"/>
<jsp:useBean id="orgColMap" class="java.util.TreeMap" scope="request"/>
<jsp:useBean id="siteName" class="java.lang.String" scope="request"/>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Experiment Matrix></title>

<!-- Force latest IE rendering engine or ChromeFrame if installed -->
<!--[if IE]>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<![endif]-->
<meta charset="utf-8">

<title><%=siteName%> Experiment Matrix</title>

<link href='http://fonts.googleapis.com/css?family=Raleway:300,400' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Lato:300,400' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Muli:400,300' rel='stylesheet' type='text/css'>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>

<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
<script type='text/javascript'>
$(window).load(function(){


$('#experiment-links-modal').on('show.bs.modal', function(e) {
    var orgName     = $(e.relatedTarget).data('org-name');
    var platform    = $(e.relatedTarget).data('platform');
    var property    = $(e.relatedTarget).data('property');
    var annot       = $(e.relatedTarget).data('annot');
    var expLinks    = $(e.relatedTarget).data('exp-links');
    document.getElementById('modal-title').innerHTML = orgName + " - " + platform + " - " + property + ' ' + annot;
    document.getElementById('exp-links').innerHTML = expLinks;
});
});

</script>

<style>

.table-narrow {
    width: 50%;
}
.table-condensed > thead > tr > th,
.table-condensed > tbody > tr > th,
.table-condensed > tfoot > tr > th,
.table-condensed > thead > tr > td,
.table-condensed > tbody > tr > td,
.table-condensed > tfoot > tr > td {
  padding: 3px;
}


td {
        text-align: center;
}

th {
   text-align: center;
}

td.left {
    text-align: left;
}

li {
padding-bottom: 1em;
}

</style>
</head>

<body>
<!-- This is the modal window that will show the list of experiments and analysis when the experiment/analysis count link is clicked -->
<div class="modal fade experiment-links-modal" id="experiment-links-modal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
   <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h5 class="modal-title" id="modal-title"></h5>
      </div>
      <div class="modal-body">
        <span id="exp-links">
        </span>
        
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->

</div>


<div class="container">
<h2><%=siteName%> Experiment Matrix</h2>

<div>
<ul class="nav nav-pills" style="padding-bottom: 20px">
<%
final String               DELIM = "\\$";

// Tab headers with links to content
boolean firstTime = true;
for (Object orgColKey : orgColMap.keySet()) {
	String[] orgTokens = ((String)orgColKey).split(DELIM);
	String orgSortOrder = orgTokens[0];
	String orgName      = orgTokens[1];
	String orgLink = orgName.replace(" ", "_");
	TreeMap platformColMap = (TreeMap)orgColMap.get(orgColKey);
	if (platformColMap == null || platformColMap.size() == 0) {
		continue;
	}
	String cssClass = firstTime ? " class=\"active\"" : "";
	
%>
	<li<%=cssClass%>><a data-toggle="tab" href="#<%=orgLink%>"><%=orgName%></a></li>
<% 
	firstTime = false;
}
%>
</ul>
</div>
<div class="tab-content">
<%
// Tab content
firstTime = true;
for (Object orgColKey : orgColMap.keySet()) {
	String[] orgTokens = ((String)orgColKey).split(DELIM);
	String orgSortOrder = orgTokens[0];
	String orgName      = orgTokens[1];
	String orgLink = orgName.replace(" ", "_");
	
	TreeMap propertyMap = (TreeMap)orgMap.get(orgColKey);

	TreeMap platformColMap = (TreeMap)orgColMap.get(orgColKey);
	if (platformColMap == null || platformColMap.size() == 0) {
		continue;
	}
	String activeClass = firstTime ? " in active" : "";
	String narrowTableClass = platformColMap.keySet().size() < 3 ? "table-narrow" : "";

	
%>
    <div id="<%=orgLink%>" class="tab-pane fade <%=activeClass%>">

		<table class="table table-striped table-hover table-bordered table-condensed <%=narrowTableClass%>" >
		
		<thead>
		  <tr>
			<th class="col-sm-2"></th>

<% 
	for (Object platformColKey : platformColMap.keySet()) {
		String platform = (String)platformColMap.get(platformColKey);
%>
			<th class="col-sm-1"><%=platform%></th>
<% 
	}
%>
		  </tr>
		</thead>
		
		<tbody>
		
<% 
	for (Object propertyKey : propertyMap.keySet()) {
		TreeMap annotMap = (TreeMap)propertyMap.get(propertyKey);
		
%>
		  <tr>
		  		<td class="warning left" colspan="<%=platformColMap.keySet().size() + 1%>"><%=propertyKey%></td>
		  </tr>
		  
<% 
		for(Object annotKey : annotMap.keySet()) {
%>		  
		  <tr>
<% 

			TreeMap platformMap = (TreeMap)annotMap.get(annotKey);
%>
		   		<td class="left"><%=annotKey%></td>
<% 
			for (Object platformKey : platformColMap.keySet()) {
				TreeMap expMap = (TreeMap)platformMap.get(platformKey);
				String expCount = expMap != null ? String.valueOf(expMap.keySet().size()) : "";
%>
				<td><a href="#experiment-links-modal" data-toggle="modal" 
				data-org-name="<%=orgName%>" 
				data-platform="<%=platformColMap.get(platformKey)%>" 
				data-property="<%=propertyKey%>"
				data-annot="<%=annotKey%>"
				data-exp-links="
				<ul class='list-group' >
<% 
				if (expMap != null) {
					for (Object expKey : expMap.keySet()) {
						ExperimentMatrixLinkInfo li = (ExperimentMatrixLinkInfo)expMap.get(expKey);
%>
						<li class='list-group-item'><%=li.getLink()%></li>
<% 
					}
				}
%>
				</ul>">
				<%=expCount%>
				</a></td>
<% 
			}

%>
		   </tr>
		   
<%
		}
	}
%>
		</tbody>
	</table>
</div>
		 
<% 
	firstTime = false;
}
%>

    

</div>
</div>
</body>
</html>

	