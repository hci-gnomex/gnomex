package hci.gnomex.daemon.auto_import;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StringModder {

	public static void main(String[] args) {
		List<String> stringList = new ArrayList<String>(); 
		
		Scanner scan = new Scanner(System.in);
		String str = "";
		
		while(scan.hasNextLine()) {
			str = scan.nextLine();
			String strEnd = str.substring( str.length() - 1);
			if(strEnd.equals(",")) {
				str = str.substring(0, str.length() - 1);
			}

		}
		
		
		scan.close();
		
		
		PrintWriter pw = new PrintWriter(System.out);
		pw.println(str);
		pw.close();

	}

}
