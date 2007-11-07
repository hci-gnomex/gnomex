<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta name="author" content="Wink Hosting (www.winkhosting.com)" />
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="gnomex.css" type="text/css" />
	<title>GNomEx Guest Login</title>
</head>
<body>


<%
String parm1 = request.getParameter("parm1");
String parm2 = request.getParameter("parm2");
%>

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
				<div id="title" class="titletext" align="right">GNomEx Guest Login</div>
			</div>
		</div>
		<div id="content" align="center">
			<div id="menu" align="right">
				<div align="right" style="width:189px; height:8px;"><img src="images/mnu_topshadow.gif" width="189" height="8" alt="mnutopshadow" /></div>
				<div id="linksmenu" align="center">
					
				</div>
				<div align="right" style="width:189px; height:8px;"><img src="images/mnu_bottomshadow.gif" width="189" height="8" alt="mnubottomshadow" /></div>
			</div>
		<div id="contenttext">
			<div class="bodytext" style="padding:12px;" align="justify">
				<strong>	<br />
                          
</strong>	
                  </div>

			  <div class="panel" align="justify">
			  
			  
			  
			  
         <img src="Captcha.jpg">
         <form action="/gnomex/CreateSecurityAdvisorForGuest.gx" method="post">
           <table width="200">
            <tr>
             <td class="bodytext">Enter the text displayed in the image</td> <td><input size="8" type="text" name="captchafield"></td>
            </tr>
            <tr>
            <td>&nbsp;</td>
            </tr>
            <tr>
             <td colspan="2" align="center"><INPUT class="bodytext" type="submit" name="submit" value="Login to GNomEx"></td>         
            </tr>
           </table>
           <br>
           <input type="hidden" name="launchAction" value="/guest_launcher.jsp">
           <input type="hidden" name="errorAction" value="/gnomex_guest.jsp">
           <input type="hidden" name="launchParm1" value="<%= parm1 %>">
           <input type="hidden" name="launchParm2" value="<%= parm2 %>">
           <input type="hidden" name="launchParm3" value="Y">
        </form>
      
      
      
      
        </div>
        
      </div>
			</div>
		</div>
		<div id="footer" class="smallgraytext" align="center">
		</div>
	</div>


</body>
</html>
