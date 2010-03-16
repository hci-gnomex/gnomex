<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
<link rel="stylesheet" href="plupload/css/plupload.queue.css" type="text/css" media="screen" />

<title>GNomEx - Upload analysis files</title>
<style type="text/css">
body {background: #FFFFF0;}
</style>
<script type="text/javascript" src="http://www.google.com/jsapi"></script>
<script type="text/javascript">
	google.load("jquery", "1.3");
</script>
<script type="text/javascript" src="plupload/js/gears_init.js"></script>
<script type="text/javascript" src="http://bp.yahooapis.com/2.4.21/browserplus-min.js"></script>
<script type="text/javascript" src="plupload/js/plupload.full.min.js"></script>
<script type="text/javascript" src="plupload/js/jquery.plupload.queue.min.js"></script>
<script>
$(function() {
	// Setup flash version
	$("#flash_uploader").pluploadQueue({
		// General settings
		runtimes : 'flash',
		url : 'UploadAnalysisFileServlet.gx',
    multipart : true,
    multipart_params : {analysisNumber : "<%=request.getParameter("analysisNumber")%>" },
		chunk_size : '1mb',
		unique_names : true,
		filters : [
			{title : "Image files", extensions : "jpg,gif,png"},
			{title : "Zip files", extensions : "zip"}
		],

		// Resize images on clientside if we can
		resize : {width : 320, height : 240, quality : 90},

		// Flash settings
		flash_swf_url : 'plupload/js/plupload.flash.swf'
	});

	// Setup gears version
	$("#gears_uploader").pluploadQueue({
		// General settings
		runtimes : 'gears',
		url : 'upload.php',
		max_file_size : '10mb',
		chunk_size : '1mb',
		unique_names : true,
		filters : [
			{title : "Image files", extensions : "jpg,gif,png"},
			{title : "Zip files", extensions : "zip"}
		],

		// Resize images on clientside if we can
		resize : {width : 320, height : 240, quality : 90}
	});

	// Setup silverlight version
	$("#silverlight_uploader").pluploadQueue({
		// General settings
		runtimes : 'silverlight',
		url : 'upload.php',
		max_file_size : '10mb',
		chunk_size : '1mb',
		unique_names : true,
		filters : [
			{title : "Image files", extensions : "jpg,gif,png"},
			{title : "Zip files", extensions : "zip"}
		],

		// Resize images on clientside if we can
		resize : {width : 320, height : 240, quality : 90},

		// Silverlight settings
		silverlight_xap_url : '../js/plupload.silverlight.xap'
	});

	// Setup html5 version
	$("#html5_uploader").pluploadQueue({
		// General settings
		runtimes : 'html5',
		url : 'UploadAnalysisFileServlet.gx',
    multipart : true,
    multipart_params : {analysisNumber : "<%=request.getParameter("analysisNumber")%>" },
		unique_names : true,
		filters : [
			{title : "Image files", extensions : "jpg,gif,png"},
			{title : "Zip files", extensions : "zip"}
		],

		// Resize images on clientside if we can
		resize : {width : 320, height : 240, quality : 90}
	});

	// Setup browserplus version
	$("#browserplus_uploader").pluploadQueue({
		// General settings
		runtimes : 'browserplus',
		url : 'upload.php',
		max_file_size : '10mb',
		chunk_size : '1mb',
		unique_names : true,
		filters : [
			{title : "Image files", extensions : "jpg,gif,png"},
			{title : "Zip files", extensions : "zip"}
		],

		// Resize images on clientside if we can
		resize : {width : 320, height : 240, quality : 90}
	});

	// Setup html4 version
	$("#html4_uploader").pluploadQueue({
		// General settings
		runtimes : 'html4',
		url : 'UploadAnalysisFileServlet.gx',
    multipart : true,
    multipart_params : {analysisNumber : "<%=request.getParameter("analysisNumber")%>" },
		unique_names : true,
		filters : [
			{title : "Image files", extensions : "jpg,gif,png"},
			{title : "Zip files", extensions : "zip"}
		]
	});
});
</script>

<!-- <script type="text/javascript"  src="http://getfirebug.com/releases/lite/1.2/firebug-lite-compressed.js"></script> -->
</head>
<body>
<h4>Upload files to Analysis <%=request.getParameter("analysisNumber")%> - <%=request.getParameter("analysisName")%></h4>
<div id="html4_uploader" style="width: 650px; height: 330px;">Your browser doesn't have HTML 4 support.</div>
<p class="warning">
When finished uploading, press 'Refresh' link in GNomEx Analysis Detail to see complete list of files.
</form>
<p class="credit">
This upload widget was provided by <a href="http://www.plupload.com/">Plupload</a>
</body>
</html>
