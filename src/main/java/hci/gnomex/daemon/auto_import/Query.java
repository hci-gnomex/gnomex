package hci.gnomex.daemon.auto_import;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Query {
    private String username;
    private String password;
    private String connectionStr;
    private String className;
    private Connection conn;

    Query(String creds) {
        readInCreds(creds);
        initalizeConnection();
    }

    public static void main(String[] args) {
        new Query("C:\\Users\\u0566434\\Desktop\\ORIEN\\Java\\AvatarWrangler\\");
    }

    private void readInCreds(String Creds) {
        String fileName = Creds;
        FileReader fr = null;
        BufferedReader bf = null;

        try {
            fr = new FileReader(fileName);
            bf = new BufferedReader(fr);
            String line = "";

            while((line = bf.readLine()) != null) {
                String[] credArray = line.split(" ");
                if (credArray[0].equals("username")) {
                    this.username = credArray[1];
                } else if (credArray[0].equals("password")) {
                    this.password = credArray[1];
                } else if (credArray[0].equals("connectionStr")) {
                    this.connectionStr = credArray[1];
                } else {
                    if (!credArray[0].equals("className")) {
                        throw new Exception("Missing Credentials");
                    }

                    this.className = credArray[1];
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }

                if (bf != null) {
                    bf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void initalizeConnection() {
        try {
            Class.forName(className);
            conn = DriverManager.getConnection(connectionStr, username,password);
        } catch (SQLException e) {
            if (conn != null) {
				try { conn.close(); } catch (Exception ex) { /* ignored */ }
            }
            e.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException e) {
            if (conn != null) {
				try { conn.close(); } catch (Exception ex) { /* ignored */ }
            }

            e.printStackTrace();
            System.exit(1);
        }

    }

    public String getIdAnalysisFromPropertyEntry(String pe) {
        String query = "SELECT a.idAnalysis,a.name FROM Analysis a JOIN PropertyEntry pe ON pe.valueString = a.name";
        Integer idAnalysis = null;
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                String personId = rs.getString("name");
                if (personId.equals(pe)) {
                    idAnalysis = rs.getInt("idAnalysis");
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { stmt.close(); } catch (Exception e) { }
        }

        return idAnalysis != null ? "" + idAnalysis : null;
    }

    public List getXMLFileStatus(String fileName) {
        List xmlStatusRow = new ArrayList();
		List<List> xmlStatus = new ArrayList<List>();
		StringBuilder strBuild = new StringBuilder();


		String sqlStmt = "SELECT srcFileName, srcFileModDtTm, processedYN "
				+ "FROM MolecularProfiling.dbo.XmlFileData xData " +
				"WHERE xData.srcFileName =? "
				+ "ORDER BY xData.srcFileName";


        PreparedStatement prepStmt = null;
        try {
			prepStmt = conn.prepareStatement(sqlStmt);
            prepStmt.setString(1, fileName);
            ResultSet rs = prepStmt.executeQuery();

            while(rs.next()) {
                xmlStatusRow.add(rs.getString("srcFileName"));
                xmlStatusRow.add(rs.getTimestamp("srcFileModDtTm"));
                xmlStatusRow.add(rs.getString("processedYN"));
                xmlStatus.add(xmlStatusRow);
            }


		}catch(SQLException e){
            return xmlStatus;
        }
        finally {
            try { prepStmt.close();} catch (Exception e) { }

        }

        return xmlStatus;
    }

    public boolean isExistingExperiment(String mrn) {
		String sqlStmt = "SELECT rq.idRequest, rq.number " +
				" FROM Request rq" +
				" WHERE rq.idRequest IN (SELECT pe.idRequest  FROM PropertyEntry as pe" +
				" JOIN Property as p  ON p.idProperty = pe.idProperty" +
				" WHERE pe.valueString = ?)";

        PreparedStatement prepStmt = null;
        int count = 0;

        try {
			prepStmt = conn.prepareStatement(sqlStmt);
            prepStmt.setString(1, mrn);


			ResultSet rs = prepStmt.executeQuery();

			while(rs.next()) {
                rs.getString("idRequest");
                rs.getString("number");
				count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { prepStmt.close();} catch (Exception e) { }

        }

        return count > 0;
    }

    public Integer getAnalysisID(String name, String folderName) {
        String query = "SELECT a.idAnalysis " +
                " FROM Analysis a " +
                " JOIN AnalysisGroupItem ai ON ai.idAnalysis = a.idAnalysis " +
                " JOIN AnalysisGroup ag ON ag.idAnalysisGroup = ai.idAnalysisGroup" +
                " WHERE a.name = ? AND ag.name = ?";
        PreparedStatement pStmnt = null;
        Integer analysisID = -1;
		List<Integer> analysisDupicates = new ArrayList<Integer>();

        try {
			pStmnt = conn.prepareStatement(query);
            pStmnt.setString(1, name);
            pStmnt.setString(2, folderName);
            ResultSet rs = pStmnt.executeQuery();

            while(rs.next()) {
                System.out.println("Analysis:  " + rs.getInt("idAnalysis") + " is already created");
                analysisID = rs.getInt("idAnalysis");
                analysisDupicates.add(analysisID);
            }

            if (analysisDupicates.size() > 1) {
                String strDups = "";
				for(Integer dup : analysisDupicates ){ // if dups never expect to be that many so string cat is fine
					strDups += dup + ", ";
				}


                throw new Exception("There are duplicate analyses: " + strDups + " in Analysis Group " + folderName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
        } finally {
            try { pStmnt.close(); } catch (Exception var20) { }

        }

        return analysisID;
    }

    public Connection getConnection() {
        return this.conn;
    }

	public void closeConnection(){ // call after every set of sql queries
		if(conn != null){
			try { conn.close(); } catch (Exception e) { /* ignored */ }
            }
        }

    public List<String> getImportedIDReport(List<String> sampleIDList, Integer idAnalysisGroup) {
        List<String> importedReport = new ArrayList();
        String strIDList = buildINstr(sampleIDList);
        if (sampleIDList.size() > 0) {
            String sampleName = "";

			if (idAnalysisGroup == 11) { //avatar project
                sampleName = "SL";
			}else if (idAnalysisGroup == 14) {// foundation project{
				sampleName = "RF"; //TRF, CRF, QRF
            }

            String query = "SELECT r.idRequest, a.idAnalysis, s.name, a.name FROM Sample s \n" +
                    "JOIN Request r ON s.idRequest = r.idRequest \n" +
                    "JOIN Analysis a ON a.name = r.name \n" +
                    "JOIN AnalysisGroupItem agi ON agi.idAnalysis = a.idAnalysis AND agi.idAnalysisGroup = " + idAnalysisGroup + "\n" +
                    "WHERE s.name IN " + strIDList + " AND s.name LIKE \'%" + sampleName + "%\'";
            System.out.println("The report query: " + query);
            Statement stat = null;

            try {
                StringBuilder strBuild = new StringBuilder();
				stat = conn.createStatement();
				ResultSet rs = stat.executeQuery(query);
				while(rs.next()){
                    strBuild.append(rs.getInt(1));
                    strBuild.append("\t");
                    strBuild.append(rs.getInt(2));
                    strBuild.append("\t");
                    strBuild.append(rs.getString(3));
                    strBuild.append("\t");
                    strBuild.append(rs.getString(4));
                    strBuild.append("\n");
                    importedReport.add(strBuild.toString());
					strBuild = new StringBuilder();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (stat != null) {
                    try { stat.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }

            }
        }

        return importedReport;
    }

    public boolean hasLinkAnalysisExperiment(int analysisID, int requestID) {
        boolean hasLink = false;
        StringBuilder strBuild = new StringBuilder();
        strBuild.append("SELECT * ");
        strBuild.append("FROM AnalysisExperimentItem aei ");
        strBuild.append("WHERE aei.idRequest = ? AND aei.idAnalysis = ? ");
        PreparedStatement pStmnt = null;
        int count = 0;

        try {
			pStmnt = conn.prepareStatement(strBuild.toString());
            pStmnt.setInt(1, requestID);
            pStmnt.setInt(2, analysisID);
            ResultSet rs = pStmnt.executeQuery();
            if (rs.next()) {
                System.out.print("Has a link ");
                System.out.println("On idAnalysisExperimentItem: " + rs.getInt("idAnalysisExperimentItem"));
                ++count;
            }

            if (count > 0) {
                hasLink = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
        } finally {
            try { pStmnt.close(); } catch (Exception e) { }
        }
        return hasLink;
    }

    public Map<Integer, List<Integer>> getCollaboratorsForIRB(List<String> irbs, List<Integer> analysisIDs) {
        Statement statement = null;
        PreparedStatement pStatement = null;
        ArrayList<Integer> collabIDsInLab = new ArrayList();
        HashMap collabsToAddForAnalysis = new HashMap();

        try {
            Iterator var7 = irbs.iterator();

            String collabToAnalysisQuery = "SELECT * from Lab l JOIN LabUser lu ON lu.idLab = l.idLab WHERE l.lastName LIKE ?";

            for(String irb: irbs) {

                pStatement = conn.prepareStatement(collabToAnalysisQuery);
                pStatement.setString(1, "%" + irb + "%");
                ResultSet rs = pStatement.executeQuery();

                int labID = -1;
                while(rs.next()) {

                    collabIDsInLab.add(rs.getInt("idAppUser"));
                    Integer currentLabID = rs.getInt("idLab");
                    if (currentLabID != labID && labID != -1) {
                        throw new Exception("More than one IRB found when only expecting only one for " + irb);
                    }
                    labID = currentLabID;
                }
                labID = -1;
                pStatement.clearParameters();
            }



            for(Integer aID : analysisIDs ) {
                collabToAnalysisQuery = "SELECT * from AnalysisCollaborator ac WHERE ac.idAnalysis = ? AND ac.idAppUser = ?";
                collabsToAddForAnalysis.put(aID, new ArrayList());
                System.out.println("-------------------------------------------------------------------");


                for(Integer collabID : collabIDsInLab) {

                    boolean existingRelation = false;
                    pStatement = conn.prepareStatement(collabToAnalysisQuery);
                    pStatement.setInt(1, aID);
                    pStatement.setInt(2, collabID);
                    System.out.print("SELECT * from AnalysisCollaborator ac \nWhere ac.idAnalysis = " + aID + " AND ac.idAppUser = " + collabID);
                    ResultSet rs = pStatement.executeQuery();
                    if (rs.next()) {
                        existingRelation = true;
                    }

                    if (!existingRelation) {
                        ((List)collabsToAddForAnalysis.get(aID)).add(collabID);
                        System.out.println("   actual collabs added " + collabID);
                    }
                }

                System.out.println("-------------------------------------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());

            try { pStatement.close(); } catch (SQLException se) {
                System.out.println("can't close prepared statement");
            }

            System.exit(1);
        } finally {
            try { pStatement.close(); } catch (SQLException se) {
                System.out.println("can't close prepared statement");
            }

        }

        return collabsToAddForAnalysis;
    }

    String buildINstr(List<String> ids) {
        StringBuilder strIDList = new StringBuilder();
        strIDList.append("(");

        for(int i = 0; i < ids.size(); ++i) {
            if (i < ids.size() - 1) {
                strIDList.append("'");
                strIDList.append((String)ids.get(i));
                strIDList.append("', ");
            } else {
                strIDList.append("'");
                strIDList.append((String)ids.get(i));
                strIDList.append("'");
            }
        }

        strIDList.append(")");
        return strIDList.toString();
    }

    public void assignPermissions(List<Integer> collabsToAddToAnalysis, Integer analysisID) {
        String assignPermissionsQuery = "INSERT INTO AnalysisCollaborator VALUES (?,?,?,?)";

        try {
            PreparedStatement ps = this.conn.prepareStatement(assignPermissionsQuery);
            Throwable var5 = null;

            try {
                this.conn.setAutoCommit(false);
                Iterator var6 = collabsToAddToAnalysis.iterator();

                while(var6.hasNext()) {
                    Integer collabID = (Integer)var6.next();
                    ps.setInt(1, analysisID);
                    ps.setInt(2, collabID);
                    ps.setString(3, "Y");
                    ps.setString(4, "Y");
                    ps.addBatch();
                }

                ps.executeBatch();
                this.conn.commit();
            } catch (Throwable var18) {
                var5 = var18;
                throw var18;
            } finally {
                if (ps != null) {
                    if (var5 != null) {
                        try {
                            ps.close();
                        } catch (Throwable var17) {
                            var5.addSuppressed(var17);
                        }
                    } else {
                        ps.close();
                    }
                }

            }
        } catch (SQLException var20) {
            try {
                this.conn.rollback();
            } catch (SQLException var16) {
                var16.printStackTrace();
            }
        }

    }

    public List<Integer> executeAnalysisWithCriteriaQuery(String query, List<String> attributeIDs) {
        List<Integer> idAnalysisList = new ArrayList();
        PreparedStatement pstmnt = null;

        try {
            pstmnt = this.conn.prepareStatement(query);
            Iterator var5 = attributeIDs.iterator();

            while(var5.hasNext()) {
                String id = (String)var5.next();
                pstmnt.setString(1, "%" + id + "%");
                ResultSet rs = pstmnt.executeQuery(query);

                while(rs.next()) {
                    idAnalysisList.add(rs.getInt("idAnalysis"));
                }

                pstmnt.clearParameters();
            }
        } catch (SQLException var8) {
        }

        return idAnalysisList;
    }
}
