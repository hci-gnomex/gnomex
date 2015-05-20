<!--    

function setPrintStyleSheet(title) {
  var i, a, main;
  
  if (document.getElementsByTagName("link")) {
	  for(i=1; (a = document.getElementsByTagName("link")[i]); i++) {
  	  if(a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("title")) {
    	  a.disabled = true;
      	if(a.getAttribute("title") == title) a.disabled = false;
    	}
  	}
  } 
}
window.onload = function(e) {
  <!-- Need this for cross-browser reason so that on firefox and ie, the default printing excludes instructions -->
  setPrintStyleSheet('print');
}

-->