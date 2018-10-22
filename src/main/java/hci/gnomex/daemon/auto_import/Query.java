package hci.gnomex.daemon.auto_import;

import java.io.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Query {
	
	private String username;
	private String password;
	private String connectionStr;
	private String className;
	private Connection conn;
	
	
	
	
	
	
	
	Query(String creds){
		
			readInCreds(creds);
			initalizeConnection();

		
	}
	
	
	
	public static void main(String[] args)  {
		Query q = new Query("C:\\Users\\u0566434\\Desktop\\ORIEN\\Java\\AvatarWrangler\\");
	}
	
	
	
	
	private void readInCreds(String Creds)   {
		String fileName = Creds;
		FileReader fr = null;
		BufferedReader bf = null;
		
		try {
			
			fr = new FileReader(fileName);
			bf = new BufferedReader(fr);
			String line = "";
			
			while((line = bf.readLine()) != null) {
				String[] credArray = line.split(" ");
				if(credArray[0].equals("username")) {
					this.username = credArray[1];
				}else if(credArray[0].equals("password")) {
					this.password = credArray[1];
				}
				else if(credArray[0].equals("connectionStr")) {
					this.connectionStr = credArray[1];
				}else if(credArray[0].equals("className")) {
					this.className = credArray[1];
				}else {
					throw new Exception("Missing Credentials");
				}
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(fr != null) {
					fr.close();
				}
				if(bf != null) {
					bf.close();
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}


	public void initalizeConnection() {

			try{
				Class.forName(className);
				conn = DriverManager.getConnection(connectionStr,username,password);

			}catch (SQLException e){
				if(conn != null){
					try { conn.close(); } catch (Exception ex) { /* ignored */ }
				}
				e.printStackTrace();
				System.exit(1);
			}
			catch(ClassNotFoundException e){
				if(conn != null){
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
				if(personId.equals(pe)) {
					idAnalysis = rs.getInt("idAnalysis");
					break;
				}
				
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try { stmt.close(); } catch (Exception e) { /* ignored */ }
			
		}
		
		if(idAnalysis != null) {
		    return ""+ idAnalysis;
		}
		
		return null;
	}


	public List getXMLFileStatus (String fileName){
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
				xmlStatusRow.add( rs.getString("srcFileName"));
				xmlStatusRow.add(rs.getTimestamp("srcFileModDtTm"));
				xmlStatusRow.add(rs.getString("processedYN"));
				xmlStatus.add(xmlStatusRow);
			}


		}catch(SQLException e){
			return xmlStatus;

		}
		finally {

			try { prepStmt.close(); } catch (Exception e) { /* ignored */ }

		}
		return xmlStatus;

	}
	
	
	public boolean isExistingExperiment(String mrn) {
		String sqlStmt = "SELECT rq.idRequest, rq.number" + 
				"FROM Request rq" + 
				" WHERE rq.idRequest IN (SELECT pe.idRequest  FROM PropertyEntry as pe" + 
				" JOIN Property as p  ON p.idProperty = pe.idProperty" + 
				" WHERE pe.valueString =?)";
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {

			try { prepStmt.close(); } catch (Exception e) { /* ignored */ }

		}
		if(count > 0) {
			return true;
		}
		return false;
		
	}



	public Integer getAnalysisID(String name, String folderName) {
		String query = "SELECT a.idAnalysis "
						+ " FROM Analysis a "
						+ " JOIN AnalysisGroupItem ai ON ai.idAnalysis = a.idAnalysis "
						+ " JOIN AnalysisGroup ag ON ag.idAnalysisGroup = ai.idAnalysisGroup"
						+ " WHERE a.name = ? AND ag.name = ?";
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
			if(analysisDupicates.size() > 1){
				String strDups = "";
				for(Integer dup : analysisDupicates ){ // if dups never expect to be that many so string cat is fine
					strDups += dup + ", ";
				}


				throw new Exception("There are duplicate analyses: " + strDups + " in Analysis Group " + folderName);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e) {
			System.out.println(e.getMessage());
			e.getStackTrace();

		}finally{

			try { pStmnt.close(); } catch (Exception e) { /* ignored */ }
			//this.closeConnection();

		}


		return analysisID;
	}
	
	public Connection getConnection(){
		return this.conn;
	}

	public void closeConnection(){ // call after every set of sql queries
		if(conn != null){
			try { conn.close(); } catch (Exception e) { /* ignored */ }
		}
	}

	public List<String> getImportedIDReport(List<String> sampleIDList, Integer idAnalysisGroup) {
		StringBuilder strIDList = new StringBuilder();
		strIDList.append("(");
		for( int i =0; i <  sampleIDList.size(); i++ ){
			if(i < sampleIDList.size() - 1){
				strIDList.append("\'");
				strIDList.append(sampleIDList.get(i));
				strIDList.append("\', ");
			}else{
				strIDList.append(")");
			}

		}


		String query = "SELECT r.idRequest, a.idAnalysis, s.name, a.name FROM Sample s \n" +
						"JOIN Request r ON s.idRequest = r.idRequest \n" +
						"JOIN Analysis a ON a.name = r.name \n" +
					    "JOIN AnalysisGroupItem agi ON agi.idAnalysis = a.idAnalysis AND agi.idAnalysisGroup = " + idAnalysisGroup + "\n"+
				        "WHERE " + strIDList.toString() + "\n";


		System.out.println("The report query: " + query);
		List<String> importedReport = new ArrayList();
		/*String query = "SELECT r.idRequest, a.idAnalysis, s.name, r.name " +
				"FROM Request r " +
				"JOIN Analysis a ON a.name = r.name " +
				"JOIN Sample s ON s.idRequest = r.idRequest " +
				"WHERE s.name=? ";*/
		Statement stat = null;
		try{

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

		}catch(SQLException e){
			e.printStackTrace();
		} finally {
			if(stat != null){
				try {stat.close();
				}catch(SQLException e){
					e.printStackTrace();
					System.exit(1);
				}
			}

		}

		return importedReport;

	}
}
