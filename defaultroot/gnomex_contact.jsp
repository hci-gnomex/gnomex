<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta name="author" content="Wink Hosting (www.winkhosting.com)" />
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="css/gnomex.css" type="text/css" />
	<title>GNomEx</title>
</head>
<%@ page language="java" import="hci.gnomex.utility.HibernateGuestSession,hci.gnomex.utility.DictionaryHelper,hci.gnomex.model.Property,org.hibernate.Session" %>
<%
	   String softwareContactEmail = "";
	   try {
	     Session sess = HibernateGuestSession.currentGuestSession("guest");
	     DictionaryHelper dh = DictionaryHelper.getInstance(sess);
	     softwareContactEmail = dh.getProperty(Property.CONTACT_EMAIL_SOFTWARE_BUGS);
	   }catch (Exception e) {	     
	   }
%>
<body>
	<div id="page" align="center">
		<div id="toppage" align="center">
			<div id="date">
				<div class="smalltext" style="padding:13px;"><strong></strong></div>
			</div>
			<div id="topbar">
				<div align="right" style="padding:12px;" class="smallwhitetext"></div>
			</div>
		</div>
		<div id="header" align="center">
			<div class="titletext" id="logo">
				<div class="logotext" style="margin:30px"><span class="orangelogotext">GNomEx</span></div> 
			</div>
			<div id="pagetitle">
				<div id="title" class="titletext" align="right">Contact Us</div>
			</div>
		</div>
		<div id="content" align="center">
			<div id="menu" align="right">
				<div align="right" style="width:189px; height:8px;"><img src="images/mnu_topshadow.gif" width="189" height="8" alt="mnutopshadow" /></div>
				<div id="linksmenu" align="center">
					<a href="gnomex.html" title="home">Home</a>
					<a href="gnomex_about.html" title="about">About</a>
					<a href="gnomex_help.html">Help</a>
					<a href="gnomex_contact.html" title="contact">Contact Us</a>
				</div>
				<div align="right" style="width:189px; height:8px;"><img src="images/mnu_bottomshadow.gif" width="189" height="8" alt="mnubottomshadow" /></div>
			</div>
		<div id="contenttext">
			<div class="bodytext" style="padding:12px;" align="justify">
				<strong>	<br />
                          
</strong>	
                  </div>

			  <div class="panel" align="justify">
				<span class="orangetitle"></span>
				<span class="bodytext"><br />
			      To report software bugs or give feedback, please email <a href="mailto:<%=softwareContactEmail%>" class="email"><%=softwareContactEmail%></a>
                         </span>			
                    </div>
			</div>
		</div>
		<div id="footer" class="smallgraytext" align="center">
		</div>
	</div>


</body>
</html>
