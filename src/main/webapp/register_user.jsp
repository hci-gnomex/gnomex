<%@ page import="hci.gnomex.utility.HibernateSession" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="hci.gnomex.model.Lab" %>
<%@ page import="hci.gnomex.model.PropertyDictionary" %>
<%@ page import="hci.gnomex.controller.GNomExFrontController" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="hci.gnomex.utility.JspHelper" %>
<%@ page import="hci.gnomex.utility.PropertyDictionaryHelper" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="hci.gnomex.utility.PasswordUtil" %>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <link rel="stylesheet" href="css/login.css?v1.0" type="text/css"/>
    <title>Sign up for a GNomEx account</title>

    <script type="text/javascript" language="JavaScript">
        function setFocus() {
            showError();
            theform.firstName.focus();
        }

        function showError() {
            var errorMessage = document.getElementById("error").value;
            if (errorMessage.length > 0) {
                alert(errorMessage);
            }
        }

        function showHideExternal() {
            if (document.theform.uofuAffiliate[0].checked) {
                document.getElementById("UofUDiv").style.display = "block";
                document.getElementById("externalDiv").style.display = "none";
                document.getElementById("institute").value = "";
                document.getElementById("userNameExternal").value = "";
                document.getElementById("passwordExternal").value = "";
                document.getElementById("passwordExternalConfirm").value = "";
            }
            else {
                document.getElementById("UofUDiv").style.display = "none";
                document.getElementById("externalDiv").style.display = "block";
                document.getElementById("uNID").value = "";
            }
        }

        function showNewLab() {
            document.getElementById("labDropdown").selectedIndex = 0;
            document.getElementById("newLabDiv").style.display = "block";
        }

        function hideNewLab() {
            document.getElementById("newLabDiv").style.display = "none";
        }


        function checkAlphaNumeric(e) {
            var KeyID = e.keyCode;
            if ((KeyID >= 8 && KeyID <= 9) || (KeyID >= 35 && KeyID <= 39) || (KeyID == 46)) {
                return;
            }
            if (KeyID == 0) {
                KeyID = e.which;
            }
            if (KeyID < 32 || (KeyID >= 33 && KeyID <= 47) || (KeyID >= 58 && KeyID <= 64) || (KeyID >= 91 && KeyID <= 96) || (KeyID > 122)) {
                return false;
            }
        }

        function validateAndSubmit() {
            var valid = true;
            if (document.getElementById("uofuAffiliate_n").checked) {
                if (document.getElementById("passwordExternal").value == "") {
                    valid = false;
                    alert("Please enter a password");
                } else if (document.getElementById("passwordExternal").value != document.getElementById("passwordExternalConfirm").value) {
                    valid = false;
                    alert("The passwords you entered must match");
                }
            }
            if (valid) {
                document.forms["theform"].submit();
            }
        }


        <%
        Logger LOG = Logger.getLogger("register_user.jsp");

        String idFacilityAsString = (String)((request.getParameter("idFacility") != null)?request.getParameter("idFacility"):"");
        Integer idFacilityint = null;
        try {
            idFacilityint = Integer.valueOf(idFacilityAsString);
        } catch(NumberFormatException ex) {
            idFacilityint = null;
        }

        //String idFacility = (String) ((request.getParameter("idFacility") != null)?request.getParameter("idFacility"):"");
//        Integer idFacilityint = JspHelper.getIdFacility(request);
        String idFacility = idFacilityint == null?"":(idFacilityint.toString());

        Integer coreToPassThru = JspHelper.getIdCoreFacility(request);
        String idCoreParm = coreToPassThru == null?"":("?idCore=" + coreToPassThru.toString());

        String message = (String) ((request.getAttribute("message") != null)?request.getAttribute("message"):"");
        if (message == null) {
          message = "";
        }

        String firstName = (String) ((request.getParameter("firstName") != null)?request.getParameter("firstName"):"");
        if (firstName == null) {
          firstName = "";
        }

        String lastName = (String) ((request.getParameter("lastName") != null)?request.getParameter("lastName"):"");
        if (lastName == null) {
          lastName = "";
        }

        String email = (String) ((request.getParameter("email") != null)?request.getParameter("email"):"");
        if (email == null) {
          email = "";
        }

        String phone = (String) ((request.getParameter("phone") != null)?request.getParameter("phone"):"");
        if (phone == null) {
          phone = "";
        }

        String labDropdown = (String) ((request.getParameter("labDropdown") != null)?request.getParameter("labDropdown"):"");
        if (labDropdown == null) {
          labDropdown = "";
        }

        String newLabFirstName = (String) ((request.getParameter("newLabFirstName") != null)?request.getParameter("newLabFirstName"):"");
        if (newLabFirstName == null) {
          newLabFirstName = "";
        }

        String newLabLastName = (String) ((request.getParameter("newLabLastName") != null)?request.getParameter("newLabLastName"):"");
        if (newLabLastName == null) {
          newLabLastName = "";
        }

        String department = (String) ((request.getParameter("department") != null)?request.getParameter("department"):"");
        if (department == null) {
          department = "";
        }

        String contactEmail = (String) ((request.getParameter("contactEmail") != null)?request.getParameter("contactEmail"):"");
        if (contactEmail == null) {
            contactEmail = "";
        }

        String contactPhone = (String) ((request.getParameter("contactPhone") != null)?request.getParameter("contactPhone"):"");
        if (contactPhone == null) {
          contactPhone = "";
        }

        String uofuAffiliate = request.getParameter("uofuAffiliate");
        if (uofuAffiliate == null) {
          uofuAffiliate = "";
        }

        String institute = (String) ((request.getParameter("institute") != null)?request.getParameter("institute"):"");
        if (institute == null) {
          institute = "";
        }

        String userNameExternal = (String) ((request.getParameter("userNameExternal") != null)?request.getParameter("userNameExternal"):"");
        if (userNameExternal == null) {
          userNameExternal = "";
        }

        String uNID = (String) ((request.getParameter("uNID") != null)?request.getParameter("uNID"):"");
        if (uNID == null) {
          uNID = "";
        }

        // clear non-applicable fields
        if (!uofuAffiliate.equals("y")) {
          uNID = "";
        }
        if (!uofuAffiliate.equals("n")) {
          institute = "";
          userNameExternal = "";
        }


        List labs = null;

        // We can't obtain a hibernate session unless webcontextpath is initialized.  See HibernateSession.
        String webContextPath = getServletConfig().getServletContext().getRealPath("/");
        GNomExFrontController.setWebContextPath(webContextPath);

        boolean isUniversityUserAuthentication = false;
        String siteLogo = "";
        Session sess = null;
        String publicDataNotice = "";

        try {
          sess = HibernateSession.currentReadOnlySession("guest");
          isUniversityUserAuthentication = PropertyDictionaryHelper.getInstance(sess).isUniversityUserAuthentication();

          // Get site specific log
          siteLogo = PropertyDictionaryHelper.getSiteLogo(sess, coreToPassThru);

          //Get note informing users about not needing an account for public data
          PropertyDictionary dataNote = (PropertyDictionary)sess.createQuery("from PropertyDictionary p where p.propertyName='" + PropertyDictionary.PUBLIC_DATA_NOTICE + "'").uniqueResult();
          if(dataNote != null && dataNote.getPropertyValue()!=null && !dataNote.getPropertyValue().equals("")) {
              publicDataNotice = dataNote.getPropertyValue();
          }

          labs = sess.createQuery("from Lab l where l.isActive = 'Y' order by l.lastName, l.firstName").list();

        } catch (Exception e){
            LOG.error("Error in register_user.jsp", e);
          message = "Cannot obtain properties " + e.toString() + " sess=" + sess;
        } finally {
          try {
            HibernateSession.closeSession();
          } catch (Exception e) {
              LOG.error("Error in register_user.jsp", e);
          }
        }

        %>

    </script>
</head>

<body onload="setFocus()">


<div id="content" align="center" bgcolor="white">

    <div class="header-bar">
        <div class="leftMenu">
            <img src="<%=siteLogo%>"/>
        </div>
        <div class="rightMenu">
            <a href="gnomexFlex.jsp<%=idCoreParm%>">Sign in</a> |
            <a href="reset_password.jsp<%=idCoreParm%>">Reset password</a>
        </div>
    </div>

    <div style="width:500px; align:center; bgcolor:white; float:center;">
        <p style="color:blue"><%=publicDataNotice%>
        </p>
    </div>

    <form name="theform" method="POST" action="PublicSaveSelfRegisteredAppUser.gx">

        <div class="boxWide">
            <h3>Sign up for an account</h3>

            <div class="col1">
                <div class="right">*First name</div>
            </div>
            <div class="col2"><input id="firstName" type="text" class="textWide" name="firstName"
                                     value="<%=firstName%>"></div>

            <div class="col1">
                <div class="right">*Last name</div>
            </div>
            <div class="col2"><input type="text" class="textWide" name="lastName" id="lastName" value="<%=lastName%>"/>
            </div>

            <div class="col1">
                <div class="right">*Email</div>
            </div>
            <div class="col2"><input type="text" class="textWide" name="email" id="email" value="<%=email%>"/></div>

            <div class="col1">
                <div class="right">*Phone</div>
            </div>
            <div class="col2"><input type="text" class="textWide" name="phone" id="phone" value="<%=phone%>"/></div>

            <div class="empty"></div>
            <br>

            <div id="labDiv">
                <div class="col1">
                    <div class="right">*Choose Lab</div>
                </div>
                <div class="col2">
                    <select name="labDropdown" onchange="hideNewLab()" id="labDropdown" style="width:200">
                        <option value="0"></option>
                        <%
                            Iterator i = labs.iterator();
                            while (i.hasNext()) {
                                Lab l = (Lab) i.next();
                                String isSelected = "";
                                if (labDropdown.length() > 0 && labDropdown.compareTo("" + l.getIdLab()) == 0) {
                                    isSelected = "selected";
                                }
                        %>
                        <option value="<%=l.getIdLab()%>" <%=isSelected%>><%=l.getName()%>
                        </option>
                        <%}%>
                    </select>
                    <a class="button" onclick="showNewLab()">New lab...</a>
                </div>
            </div>

            <div id="newLabDiv" style="display:none;">
                <div class="col1">
                    <div class="right">Lab First Name</div>
                </div>
                <div class="col2"><input type="text" class="textWide" name="newLabFirstName"
                                         value="<%=newLabFirstName%>" onkeypress="return checkAlphaNumeric(event)"/>
                </div>

                <div class="col1">
                    <div class="right">*Lab Last Name</div>
                </div>
                <div class="col2"><input type="text" class="textWide" name="newLabLastName" value="<%=newLabLastName%>"
                                         onkeypress="return checkAlphaNumeric(event)"/></div>

                <div class="col1">
                    <div class="right">Department</div>
                </div>
                <div class="col2"><input type="text" class="textWide" name="department" value="<%=department%>"/></div>

                <div class="col1">
                    <div class="right">*PI Email</div>
                </div>
                <div class="col2"><input type="text" class="textWide" name="contactEmail" id="contactEmail"
                                         value="<%=contactEmail%>"/></div>

                <div class="col1">
                    <div class="right">*PI Phone</div>
                </div>
                <div class="col2"><input type="text" class="textWide" name="contactPhone" value="<%=contactPhone%>"/>
                </div>
            </div>


            <% if (isUniversityUserAuthentication) {%>
            <div class="empty"></div>
            <div id="userChoiceDiv">
                <div class="col1Wide">
                    <div class="right"> *Are you affiliated with the University of Utah?</div>
                </div>
                <div class="col2"><INPUT TYPE="radio" id="uofuAffiliate_y" NAME="uofuAffiliate"
                                         VALUE="y" <%=uofuAffiliate.equals("y") ? "checked='checked'" : ""%>
                                         onClick="showHideExternal();">Yes
                </div>
                <div class="col2"><INPUT TYPE="radio" id="uofuAffiliate_n" NAME="uofuAffiliate"
                                         VALUE="n" <%=uofuAffiliate.equals("n") ? "checked='checked'" : ""%>
                                         onClick="showHideExternal();">No
                </div>
            </div>
            <% } %>
            <div class="emptySmall"></div>

            <div id="UofUDiv" style="display:<%=uofuAffiliate.equals("y") ? "block" : "none"%>;">
                <div id="univUserNameArea1" class="col1">
                    <div class="right">*uNID</div>
                </div>
                <div id="univUserNameArea2" class="col2"><input type="text" class="textWide" name="uNID" id="uNID"
                                                                value="<%=uNID%>"></div>
                <div class="col1"></div>
                <div class="col2">
                    <note class="inline"><i>Format should be a "u" followed by 7 digits (u0000000)</i></note>
                </div>
            </div>

            <div id="externalDiv" style="display:<%=uofuAffiliate.equals("n") ? "block" : "none"%>">
                <div class="col1">
                    <div class="right">Institute</div>
                </div>
                <div class="col2"><input type="text" class="textWide" name="institute" id="institute"
                                         value="<%=institute%>"/></div>

                <div id="externalUserNameArea1" class="col1">
                    <div class="right">*User name</div>
                </div>
                <div id="externalUserNameArea2" class="col2"><input type="text" class="textWide" name="userNameExternal"
                                                                    id="userNameExternal" value="<%=userNameExternal%>">
                </div>

                <div id="externalPasswordArea1" class="col1">
                    <div class="right">*Password</div>
                </div>
                <div id="externalPasswordArea2" class="col2"><input type="password" name="passwordExternal"
                                                                    id="passwordExternal" class="textWide"></div>

                <div id="externalPasswordConfirmArea1" class="col1">
                    <div class="right">*Password&nbsp;(conf)</div>
                </div>
                <div id="externalPasswordConfirmArea2" class="col2"><input type="password"
                                                                           name="passwordExternalConfirm"
                                                                           id="passwordExternalConfirm"
                                                                           class="textWide"></div>
                <div class="col2">
                    <note class="inline"><i><%=PasswordUtil.REQUIREMENTS_TEXT_HTML%>
                    </i></note>
                </div>

            </div>

            <div class="emptySmall"></div>
            <%--<div style="display:block;" class="errorWide"><div class="message"> <strong><%= message %></strong></div></div>--%>
            <div>
                <div class="buttonPanel"><input type="button" class="submit" value="Submit"
                                                onclick="validateAndSubmit();"/></div>
            </div>
            <div class="emptySmall"></div>
        </div>
        <input type="hidden" id="error" value="<%=message%>"/>
        <input type="hidden" name="idFacility" value="<%=idFacility%>"/>
        <input type="hidden" name="responsePageSuccess" value="/register_user_success.jsp<%=idCoreParm%>"/>
        <input type="hidden" name="responsePageError" value="/register_user.jsp<%=idCoreParm%>"/>
    </form>

</div>


<script type="text/javascript" language="JavaScript">

    <%
    if (!isUniversityUserAuthentication) {
    %>
    document.getElementById("externalDiv").style.display = "block";
    <%
    }
    %>
</script>


</body>
</html>
