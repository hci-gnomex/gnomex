<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%@ page language="java" import="java.util.ArrayList"%>
<%@ page language="java" import="hci.gnomex.utility.TopicTreeLinkInfo;"%>

<!--  These are the session objects passed into this jsp -->
<jsp:useBean id="topics" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="siteName" class="java.lang.String" scope="request"/>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Topic Tree</title>

<!-- Force latest IE rendering engine or ChromeFrame if installed -->
<!--[if IE]>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<![endif]-->
<meta charset="utf-8">

<title><%=siteName%> Topic Tree</title>

<link href='http://fonts.googleapis.com/css?family=Raleway:300,400' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Lato:300,400' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Muli:400,300' rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="css/mktree.css" type="text/css">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script type="text/javascript" src="mktree.js"></script>


<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<link rel="stylesheet" href="css/reportBootstrap.css">
<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
<script type='text/javascript'>
$(window).load(function(){


$('#topic-links-modal').on('show.bs.modal', function(e) {
    var topicName         = $(e.relatedTarget).data('topic-name');
    var topicContent      = $(e.relatedTarget).data('content');
    var topicLink         = $(e.relatedTarget).data('gnomex-link');
    document.getElementById('modal-title').innerHTML = topicName;
    document.getElementById('modal-content').innerHTML = topicContent;
    document.getElementById('modal-link').innerHTML = topicLink;
});
});

function alertIt() {
	alert("alert! Alert!");
}
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
<div class="modal fade topic-links-modal" id="topic-links-modal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
   <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h5 class="modal-title" id="modal-title"></h5>
      </div>
      <div class="modal-body">
        <span id="modal-content"></span>
      </div>
      <div class="modal-footer">
      	<span id="modal-link"></span>
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->

</div>


<div class="container">
<h2><%=siteName%> Topics Tree</h2>

<div class="tab-content">

<ul class="mktree" id="topicsTree">
<%
	Integer lastLevel = 0;
	for(Object obj : topics) {
	  TopicTreeLinkInfo top = (TopicTreeLinkInfo)obj; 
	  Integer currentLevel = top.getTreeLevel();
	  Boolean hasChildren = top.getHasChildren();
	  while (currentLevel < lastLevel) {
%>
</ul></li>
<%
        lastLevel--;
	  }
%>
<li><a href="#topic-links-modal" data-toggle="modal" 
				data-topic-name="<%=top.getNumber() + " - " + top.getName()%>" 
				data-gnomex-link="<a href='<%=top.getURL() %>' target='_blank' style='float:left'>View Topic in GNomEx</a>"
				data-content="
<%
      if (top.getIsPublic()) {
%>
   <div class='row'>
     <div class='col-xs-12'><%=top.getDescription()%></div>
  </div>
  <div class='row'><hr></div>
   <div class='row'>
     <div class='col-xs-3'>Owner</div>
     <div class='col-xs-4'><%=top.getOwner()%></div>
   </div>
   <div class='row'>
     <div class='col-xs-3'>Lab</div>
     <div class='col-xs-8'><%=top.getLab()%></div>
   </div>
<%
      } else {
%>
   <div class='row'>
    <div class='col-xs-8'>**Restricted**</div>
  </div>
  <div class='row'><hr></div>
<%
      }
%>
  <div class='row'>
    <div class='col-xs-3'># Experiments</div>
    <div class='col-xs-2'><%=top.getNumExperiments().toString()%></div>
  </div>
  <div class='row'>
    <div class='col-xs-3'># Analyses</div>
    <div class='col-xs-2'><%=top.getNumAnalyses().toString()%></div>
  </div>
				">
				<%=top.getNumber() + " - " + top.getName()%><%=top.getIsPublic() ? "" : "(Restricted)"%>
				</a>
<%
      if (!hasChildren) {
%>
</li>
<%	  } else {
%>
<ul>
<%	  }
      lastLevel = currentLevel;
	}
	while (lastLevel > 0) {
%>
</li></ul>
<%	  
      lastLevel--;
	}
%>
</div>
</div>
</body>
</html>

	