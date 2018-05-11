package hci.gnomex.daemon.auto_import;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class PeekableScanner {
	private Scanner scan1;
	private Scanner scan2;
	private String next;

	public PeekableScanner( File source ) throws FileNotFoundException
	{
		scan1 = new Scanner(source);
		scan2 = new Scanner(source);
		next = scan2.nextLine();
	}

	public boolean hasNext()
	{
		return scan1.hasNextLine();
	}

	public String next()
	{
		next = (scan2.hasNextLine() ? scan2.nextLine() : null);
		return scan1.nextLine();
	}

	public String peek()
	{
		return next;
	}
	public void close(){
		this.scan1.close();
		this.scan2.close();
	}
	

}
