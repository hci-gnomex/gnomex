package hci.gnomex.daemon.auto_import;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class PathMaker {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String inFile = "";
		String fileName = "";
		int offset = 3;



		if (args.length == 2) {
			inFile = args[0];
			fileName = args[1];

		} else {
			System.out.println("Please provide the input file name and where you want save output file(s)");
			System.exit(1);
		}

		try {
			// printing

			Set<String> fileSet = new TreeSet<String>();

			List filePaths = pathBuilder(inFile);
			PrintWriter writer = null;


			writer = new PrintWriter(fileName);
			for (int i = 0; i < filePaths.size(); i++) {

				List<String> p = (List<String>) filePaths.get(i);
				for (int j = 0; j < p.size(); j++) {
					if (p.size() - 1 == j && i == filePaths.size()  - 1) {
						writer.print(p.get(j));
					} else {
						writer.println(p.get(j));
					}
				}
			}
			writer.close();



			/*for (int i = 0; i < filePaths.size(); i++) {

				List<String> p = (List<String>) filePaths.get(i);

				String dirName = p.get(0).split("/")[0];
				String properDirName = dirName.replace(" ", "_");
				writer =  new PrintWriter(outPath + properDirName + "_Paths.out");

				for (int j = 0; j < p.size(); j++) {
					if (p.size() - 1 == j) {
						writer.print(p.get(j));
					} else {
						writer.println(p.get(j));
					}
				}
				writer.close();
			}*/


		} catch (IOException e) {
			System.out.println(e.getStackTrace());

		}catch(Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public static List<String> pathBuilder(String fileName) throws IOException {
		PeekableScanner scan = new PeekableScanner(new File(fileName));
		List<String> paths = new ArrayList<String>();
		List multiPathsOFFiles = null;

		Stack<DirectoryNode> pBuilder = new Stack<DirectoryNode>();
		int rootCount = 0;
		//String line = bf.readLine();
		String rootPath = scan.next();
		multiPathsOFFiles= pathBuilder(scan, paths, pBuilder, rootCount, rootPath);
		scan.close();
		return multiPathsOFFiles;
	}

	private static List pathBuilder(PeekableScanner scan,
									List<String> paths, Stack<DirectoryNode> builder, int parentCount,String rootPath)
			throws IOException {
		int offset = 3; // first node is 3 chars offset back to 0;
		List<ArrayList<String>> pathsOfFiles = new ArrayList<ArrayList<String>>();


		if(!hasLeaf(formatFileName(scan.peek(),offset))) {
			String firstLine = scan.next();
			pushNode(firstLine, builder,offset);
		}

		while(scan.hasNext()) {
			while (scan.hasNext()) {
				String currentLine = scan.next();

				char[] currentFileName = currentLine.toCharArray();
				List lineInfo = consumeLine(currentFileName);
				String dirName = (String) lineInfo.get(0);
				int count = ((int) lineInfo.get(1)) - offset;

				int nextCount = getNextCount(scan) - offset;

				//System.out.println(currentLine + " " + count);

				if(count == 0 && ! hasLeaf(dirName)) {
					builder.clear();
					pushNode(currentLine,builder,offset);
					continue;
					//break;
				}



				if(hasLeaf(dirName)){
					paths.add(rootPath + makePath(builder,dirName));

				}
				else if(builder.peek().getDepth() < count ){
					builder.push(new DirectoryNode(count,dirName));
				}


				if(count >= nextCount ){

					if(nextCount <= 0) {
						break;
					}


					//int numToPop = (count - nextCount) / 4;

					DirectoryNode n = builder.peek();

					while(n.getDepth() >= nextCount) {
						builder.pop();
						n = builder.peek();
					}


				}


			}
			//System.out.println("End of While Loop");
			pathsOfFiles.add(new ArrayList<String>(paths));
			paths.clear();

		}


		return pathsOfFiles;

	}

	private static String makePath(Stack<DirectoryNode> builder, String file) {
		StringBuilder strBuild = new StringBuilder();
		for(DirectoryNode dir : builder){
			strBuild.append(dir.getName());
			strBuild.append("/");
		}
		strBuild.append(file);
		return strBuild.toString();

	}

	private static List consumeLine(char[] lineCharArray) {
		List lineInfo = new ArrayList();
		StringBuilder strBuild = new StringBuilder();
		int spaceCount = 0;
		boolean countSpaces = true;


		for (int j = 0; j < lineCharArray.length; j++) {
			int asciiChar = (int) lineCharArray[j];
			char cd = (char) asciiChar;

			if ((asciiChar > 126 || asciiChar == 32) && countSpaces) {
				spaceCount++;
				continue;
			}
			else {
				countSpaces = false;
				char c = (char) asciiChar;
				strBuild.append(Character.toString(c));
			}
		}
		spaceCount--;
		lineInfo.add(strBuild.toString());
		lineInfo.add(spaceCount);

		return lineInfo;

	}
	public static boolean hasLeaf(String dirName){
		if (dirName.split("\\.").length > 1){
			return true;
		}else{
			return false;
		}


	}

	private static int getNextCount(PeekableScanner scan) {
		String nextLine = scan.peek();
		if(nextLine == null) {
			return -1;
		}else {
			List nextLineInfo = consumeLine(nextLine.toCharArray());
			int nextCount = (int) nextLineInfo.get(1);
			return nextCount;
		}
	}

	private static String formatFileName(String currentLine, int offset){
		char[] currentFileName = currentLine.toCharArray();
		List lineInfo = consumeLine(currentFileName);

		String dirName = (String) lineInfo.get(0);
		return dirName;
	}

	public static void pushNode(String currentLine,Stack<DirectoryNode> builder,int offset){
		char[] currentFileName = currentLine.toCharArray();
		List lineInfo = consumeLine(currentFileName);
		String dirName = (String) lineInfo.get(0);
		int count = (int) lineInfo.get(1);
		builder.push(new DirectoryNode(count - offset ,dirName));
	}



}
