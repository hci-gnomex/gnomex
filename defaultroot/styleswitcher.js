<!--    

function setActiveStyleSheet(title, printTitle) {
  var i, a, main;
  for(i=0; (a = document.getElementsByTagName("link")[i]); i++) {
    if(a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("title")) {
      a.disabled = true;
      if(a.getAttribute("title") == title) a.disabled = false;
      if(a.getAttribute("title") == printTitle) a.disabled = false;
    }
  }
}


-->