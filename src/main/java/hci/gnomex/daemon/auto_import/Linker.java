
package hci.gnomex.daemon.auto_import;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Linker {
	public static void main(String[] args)  {

		String inIDsFileName = "";
		String inCreds ="";
		String outFileName = "";
		String importType = "";
		List<String> slInfo = new ArrayList<String>();


		
		if(args.length == 4) {
			inIDsFileName = args[0];
			inCreds=args[1];
			outFileName = args[2];
			importType = args[3].toLowerCase();
		}else {
			System.out.println("Please provide the sql script, the output file name, and the import type.");
			System.exit(1);
		}

		Connection conn = null;
		ResultSet rs = null;
		CallableStatement cstmt = null;
		Query q = null;



		try {
			// not a hibernate Query
			q = new Query(inCreds);
			String storedProc = determineProc(importType);
			conn = q.getConnection();
			
			String idString = readIDs(inIDsFileName);
			

			// need to obtain store procedure in db(a sql script stored in our sql server db)
			cstmt = conn.prepareCall(storedProc);
			cstmt.setString (1,idString);

			
		

			
			int rowsAffected = 0;
			boolean isResults = cstmt.execute();

			while(isResults  || rowsAffected != -1) {
				if(isResults) {
					rs = cstmt.getResultSet();
					break;
				}else {
					rowsAffected = cstmt.getUpdateCount();
				}
				isResults = cstmt.getMoreResults();
			}

			while(rs.next()) {
				
				
				if(importType.equals("avatar")) {
					saveResult(rs,slInfo, 11); 
					
				}else if(importType.equals("foundation")) {
					saveResult(rs,slInfo, 10); 
					
				}
				
			}

			writeToFile(outFileName, slInfo);

		}catch(SQLException ex) {
			ex.printStackTrace();

		}catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			try { cstmt.close(); } catch (Exception e) { /* ignored */ }
			if(q != null){
				q.closeConnection();
			}
		}

	}

	private static String determineProc(String importType) throws Exception {

		if(importType.equals( "avatar")) {
			return "{call gnomex.dbo.sp_GetAvatarInfo(?)}";
			
		}else if(importType.equals("foundation")) {
			return "{call gnomex.dbo.sp_GetFoundationInfo(?)}";
		}
		else {
			throw new Exception("Please provide an importType of avatar or foundation");
		}
		
	}

	public static String readIDs(String fileName){
		BufferedReader bf = null;
		String idStr = "";

		try {
			bf = new BufferedReader(new FileReader(fileName));
			idStr = bf.readLine();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		finally {
			try {
				bf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return idStr;
	}

	public static void writeToFile(String fileName, List<String> dataToWrite) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName);
			for (int i = 0; i < dataToWrite.size(); i++) {
				writer.println(dataToWrite.get(i));
			}


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally {
			writer.close();
		}

	}
	
	
	public static void saveResult(ResultSet rs, List<String> slInfo, int itemCount) throws SQLException {
		StringBuilder strBuild = new StringBuilder();
		
		for(int i = 1; i <= itemCount; i++ ) {
			if(i < itemCount) {
				strBuild.append(rs.getString(i));
				strBuild.append("\t");
			}else {
				strBuild.append(rs.getString(i));
			}
			
		}
		System.out.println(strBuild.toString());
		slInfo.add(strBuild.toString());
		strBuild = new StringBuilder();
		
		
	}


}






