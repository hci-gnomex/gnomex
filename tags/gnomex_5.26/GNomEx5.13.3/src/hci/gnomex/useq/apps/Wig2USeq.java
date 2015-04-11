package hci.gnomex.useq.apps;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import hci.gnomex.useq.*;
import hci.gnomex.useq.data.*;

public class Wig2USeq {

	private File[] files;
	private float skipValue = Float.MIN_VALUE;
	private float negativeSkipValue;
	private ArrayList<File> files2Zip = new ArrayList<File>();
	private int rowChunkSize = 100000;
	private File saveDirectory;
	private String versionedGenome = null;
	private int graphStyle = 1;
	private String color = null;
	private String description = null;
	private File workingWigFile = null;
	private Pattern number = Pattern.compile("^\\d");
	private Pattern space = Pattern.compile("\\s");
	private Pattern equal = Pattern.compile("=");

	public Wig2USeq(String[] args) {
		//check for args 
		processArgs(args);

		//for each file
		try{
			System.out.println("\nParsing...");
			for (int x=0; x< files.length; x++){
				workingWigFile = files[x];
				System.out.println("\t"+workingWigFile);

				//what kind of wig file?
				String type =  parseWigFileType();
				if (type == null) USeqUtilities.printExit("\nCould not parse the file type from this file, aborting. -> "+workingWigFile+"\n\t" +
				"Looking for a line containing 'type=bedGraph' or starting with 'fixedStep' or 'variableStep'.");

				//make save directory
				saveDirectory = USeqUtilities.makeDirectory(workingWigFile, ".TempDelMe");

				//parse file and write to save directory
				if (type.equals("variableStep")) parseVariableStepWigFile();
				else if (type.equals("fixedStep")) {
					graphStyle =1;
					parseFixedStepWigFile();
				}
				else if (type.equals("bedGraph")) {
					graphStyle =1;
					parseBedGraphFile();
				}
				else USeqUtilities.printExit("Not implemented "+type);

				//write the read me
				writeReadMeTxt();

				//zip compress directory
				System.out.println("\tZipping...");
				zipIt();

			}
		} catch (Exception e){
			System.out.println("\nProblem parsing "+ workingWigFile.getName()+"! Skipping.\n");
			e.printStackTrace();
		}
	}

	private void zipIt(){
		String zipName = USeqUtilities.removeExtension( saveDirectory.getName()) +USeqUtilities.USEQ_EXTENSION_WITH_PERIOD;
		File zipFile = new File (workingWigFile.getParentFile(), zipName);
		File[] files = new File[files2Zip.size()];
		files2Zip.toArray(files);
		USeqUtilities.zip(files, zipFile);
		USeqUtilities.deleteDirectory(saveDirectory);
		files2Zip.clear();
	}

	private void writeReadMeTxt(){
		try {
			ArchiveInfo ai = new ArchiveInfo(versionedGenome, null);
			//set data type, graph or region
			ai.setDataType(ArchiveInfo.DATA_TYPE_VALUE_GRAPH);
			ai.setInitialGraphStyle(Text2USeq.GRAPH_STYLES[graphStyle]);
			//set text file source
			ai.setOriginatingDataSource(workingWigFile.toString());
			//set color
			if (color != null) ai.setInitialColor(color);
			//set description?
			if (description != null) ai.setDescription(description);
			//write
			File readme = ai.writeReadMeFile(saveDirectory);
			files2Zip.add(0,readme);
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	/**Looks 'fixedStep' or 'variableStep' or 'bedGraph', if none found returns null*/
	private String parseWigFileType() throws IOException{
		BufferedReader in = USeqUtilities.fetchBufferedReader(workingWigFile);
		String line;
		String type = null;
		int counter = 0;
		while ((line=in.readLine())!=null){
			line = line.trim();
			if (line.length()==0) continue;
			//bedGraph?
			if (line.contains("type=bedGraph")){
				type = "bedGraph";
				break;
			}
			if (line.startsWith("fixedStep")) {
				type = "fixedStep";
				break;
			}
			if (line.startsWith("variableStep")) {
				type = "variableStep";
				break;
			}
			counter++;
			if (counter > 1000) return null;
		}
		in.close();
		return type;
	}

	/** Converts a bedGraph to a stairstep/ heatmap graph and saves in useq format.
		track type=bedGraph
		chr1	64	69	1
		chr1	69	71	2
		chr1	71	73	3
		chr1	94	97	5
		chr1	97	157	6
	 * Note, the BedGraph ucsc spec uses interbase coordinates.*/
	public void parseBedGraphFile() throws IOException{
		BufferedReader in = USeqUtilities.fetchBufferedReader(workingWigFile);
		String line;
		Pattern space = Pattern.compile("\\s+");
		ArrayList <PositionScore> al = new ArrayList <PositionScore>();
		String currentChromosome = "";
		String[] tokens = null;
		HashSet<String> chroms = new HashSet<String>();
		int start;
		int stop;
		float score = 0;
		//advance until hitting data
		while ((line=in.readLine())!=null){
			line = line.trim();
			if (line.length()==0) continue;
			if (line.contains("type=bedGraph")){
				break;
			}
		}
		//read first data line
		while ((line=in.readLine())!=null){
			//chr1	94	97	5
			tokens = space.split(line);
			if (tokens.length!=4) continue;
			else {
				score = Float.parseFloat(tokens[3]);
				//check score?
				if (skipValue != Float.MIN_VALUE){
					if (score > skipValue || score < negativeSkipValue ) break;
				}
				else break;
			}
		}
		//parse data line
		currentChromosome = tokens[0];
		chroms.add(currentChromosome);
		start = Integer.parseInt(tokens[1]);
		stop = Integer.parseInt(tokens[2]);

		//set zero value?
		if (score != 0){
			int pos = start-1;
			if (pos <0) pos=0;
			al.add(new PositionScore(pos, 0));
		}
		//add block
		al.add(new PositionScore(start, score));
		al.add(new PositionScore(stop -1, score));

		//read remaining data watching for gaps
		while ((line=in.readLine())!=null){
			//chr1	94	97	5
			tokens = space.split(line);
			if (tokens.length!=4) continue;
			//check chrom, if different then close and write chrom to file
			if (tokens[0].equals(currentChromosome) == false){
				//check if it exists
				if (chroms.contains(tokens[0])) throw new IOException("This file is not sorted by chromosome! "+tokens[0]+" has been parsed before! Aborting");
				else chroms.add(tokens[0]);
				//write it out
				System.out.println("\t\t"+currentChromosome+"\t"+al.size());
				PositionScore[] ps = new PositionScore[al.size()];
				al.toArray(ps);
				SliceInfo sliceInfo = new SliceInfo(currentChromosome, ".",0,0,0,null);
				PositionScoreData data = new PositionScoreData(ps, sliceInfo);
				PositionScoreData.updateSliceInfo(ps, sliceInfo);
				data.sliceWritePositionScoreData(rowChunkSize, saveDirectory, files2Zip);
				//reset
				currentChromosome = tokens[0];
				al.clear();
			}
			//parse values
			score = Float.parseFloat(tokens[3]);
			//check score?
			if (skipValue != Float.MIN_VALUE){
				if (score <= skipValue && score >= negativeSkipValue ) continue;
			}
			int newStart = Integer.parseInt(tokens[1]); 
			//check if there is a gap
			if (newStart != stop){
				//zero old stop
				al.add(new PositionScore(stop, 0));
				//zero new start
				int newStartMinOne = newStart-1;
				if (newStartMinOne != stop){
					al.add(new PositionScore(newStartMinOne, 0));
				}
			}
			start = newStart;
			stop = Integer.parseInt(tokens[2]);
			//add block
			al.add(new PositionScore(start, score));
			al.add(new PositionScore(stop-1, score));
		}
		//zero end
		al.add(new PositionScore(stop, 0));

		//write out last
		System.out.println("\t\t"+currentChromosome+"\t"+al.size());
		PositionScore[] ps = new PositionScore[al.size()];
		al.toArray(ps);
		SliceInfo sliceInfo = new SliceInfo(currentChromosome, ".",0,0,0,null);
		PositionScoreData data = new PositionScoreData(ps, sliceInfo);
		PositionScoreData.updateSliceInfo(ps, sliceInfo);
		data.sliceWritePositionScoreData(rowChunkSize, saveDirectory, files2Zip);
		al = null;

	}

	/**Parses a variableStep wig file, skips any track type data. Span is assumed to be 1.
	 * Should look something like:
	 * 	variableStep chrom=chr19 span=1
	 * 	59304701 10.0
	 * 	59304901 12.5
	 * 	59305401 15.0
	 * 	59305601 17.5
	 * Subtracts 1 from each position to convert from 1-relative coordinates specified from UCSC Wig fromat
	 * to interbase coordinates. 
	 * Skips lines with designated skipValue*/
	public void parseVariableStepWigFile() throws Exception{
		//load file
		String line;
		String[] tokens = null;
		String chromosome = null;
		ArrayList<PositionScore> ps = new ArrayList<PositionScore>();
		BufferedReader in = USeqUtilities.fetchBufferedReader(workingWigFile);
		Matcher mat;
		//for each line
		while ((line=in.readLine())!=null){
			line = line.trim();
			if (line.length()==0) continue;
			//start with a number?
			mat = number.matcher(line);
			if (mat.find()){
				tokens = space.split(line);
				if (tokens.length !=2) throw new Exception("Problem with parsing position:value from "+workingWigFile+" line -> "+line);
				float value = Float.parseFloat(tokens[1]);
				if (value != Float.MIN_VALUE) ps.add(new PositionScore(Integer.parseInt(tokens[0])-1, value));
			}
			//variableStep
			else if (line.startsWith("variableStep")){
				//parse chrom
				tokens = space.split(line);
				tokens = equal.split(tokens[1]);
				if (tokens.length !=2) throw new Exception ("Problem parsing chromosome from"+workingWigFile+" line -> "+line); 
				//first one or old
				if (chromosome != null) {
					System.out.println("\t\t"+chromosome+"\t"+ps.size());
					PositionScore[] psArray = new PositionScore[ps.size()];
					ps.toArray(psArray);
					ps.clear();
					SliceInfo sliceInfo = new SliceInfo(chromosome, ".",0,0,0,null);
					PositionScoreData data = new PositionScoreData(psArray, sliceInfo);
					PositionScoreData.updateSliceInfo(psArray, sliceInfo);
					data.sliceWritePositionScoreData(rowChunkSize, saveDirectory, files2Zip);
				}
				chromosome = tokens[1];
			}
		}

		if (chromosome == null) throw new Exception ("No 'variableStep chrom=...' line found in "+workingWigFile);
		//save last chromosome
		System.out.println("\t\t"+chromosome+"\t"+ps.size());
		PositionScore[] psArray = new PositionScore[ps.size()];
		ps.toArray(psArray);
		SliceInfo sliceInfo = new SliceInfo(chromosome, ".",0,0,0,null);
		PositionScoreData data = new PositionScoreData(psArray, sliceInfo);
		PositionScoreData.updateSliceInfo(psArray, sliceInfo);
		data.sliceWritePositionScoreData(rowChunkSize, saveDirectory, files2Zip);
		ps = null;
		psArray = null;
		in.close();
	}

	/**Parses a fixedStep wig file, skips any track type data. 
	 * Should look something like:
	 * 	fixedStep chrom=chrY start=668 step=1
	 *	0.012
	 *	0.021
	 *	0.028
	 *	0.033
	 *	0.036
	 * Subtracts 1 from each position to convert from 1-relative coordinates specified from UCSC Wig format
	 * to interbase coordinates. */
	public void parseFixedStepWigFile() throws Exception{
		ArrayList<PositionScore> ps = new ArrayList<PositionScore>();
		//load file
		String line;
		String[] tokens = null;
		String chromosome = null;
		BufferedReader in = USeqUtilities.fetchBufferedReader(workingWigFile);
		Matcher mat;
		HashSet<String> chroms = new HashSet<String>();

		int startPosition = 0;
		int stepSize = 0;

		//for each line
		while ((line=in.readLine())!=null){
			line = line.trim();
			//empty?
			if (line.length() == 0) continue;
			//start with a number?
			mat = number.matcher(line);
			if (mat.find()){
				float value = Float.parseFloat(line);
				//skip it?
				if (value != skipValue) {
					//set to very low value so it will be displayed in stair step graph
					if (value == 0) value = 0.0000000001f;
					ps.add(new PositionScore(startPosition, value));
				}
				//increment position
				startPosition+= stepSize;
			}
			//fixedStep?
			else if (line.startsWith("fixedStep")){
				//zero prior?
				int sizePS = ps.size();
				if (sizePS!=0){
					//get last
					int lastPosition = ps.get(sizePS-1).getPosition();
					//set zero
					ps.add(new PositionScore(lastPosition+1, 0.0f));
				}
				//split line and check 'fixedStep chrom=chrY start=668 step=1'
				tokens = space.split(line);
				if (tokens.length !=4) throw new Exception("Problem with parsing fixedStep line from "+workingWigFile+" line -> "+line);
				//parse chrom
				String[] chromTokens = equal.split(tokens[1]);
				if (chromTokens.length !=2) throw new Exception ("Problem parsing chromosome from"+workingWigFile+" line -> "+line); 
				//first one or old
				if (chromosome == null) chromosome = chromTokens[1];
				if (chromosome != null) {
					//different chromosome?
					if (chromosome.equals(chromTokens[1]) == false){
						//close old and start new
						System.out.println("\t\t"+chromosome+"\t"+ps.size());
						PositionScore[] psArray = new PositionScore[ps.size()];
						ps.toArray(psArray);
						ps.clear();
						psArray = stripDuplicateValues(psArray);
						SliceInfo sliceInfo = new SliceInfo(chromosome, ".",0,0,0,null);
						PositionScoreData data = new PositionScoreData(psArray, sliceInfo);
						PositionScoreData.updateSliceInfo(psArray, sliceInfo);
						data.sliceWritePositionScoreData(rowChunkSize, saveDirectory, files2Zip);
						if (chroms.contains(chromosome) == false) chroms.add(chromosome);
						else USeqUtilities.printExit("\nWig file is not sorted by chromosome! Aborting.\n");
						chromosome = chromTokens[1];
					}
				}
				//set start
				String[] startTokens = equal.split(tokens[2]);
				if (startTokens.length !=2) throw new Exception ("Problem parsing start position from"+workingWigFile+" line -> "+line);
				startPosition = Integer.parseInt(startTokens[1]) -1;
				//set step
				String[] stepTokens = equal.split(tokens[3]);
				if (stepTokens.length !=2) throw new Exception ("Problem parsing start position from"+workingWigFile+" line -> "+line);
				stepSize = Integer.parseInt(stepTokens[1]);
				//set zero position
				int pos = startPosition-1;
				//set zero?
				if (pos > 0) ps.add(new PositionScore(pos, 0.0f));
			}
		}
		if (chromosome == null) throw new Exception ("No 'fixedStep chrom=...' line found in "+workingWigFile);

		//zero last position
		int sizePS = ps.size();
		if (sizePS!=0){
			//get last
			int lastPosition = ps.get(sizePS-1).getPosition();
			//set zero
			ps.add(new PositionScore(lastPosition+1, 0.0f));
		}

		//save last chromosome
		System.out.println("\t\t"+chromosome+"\t"+ps.size());
		PositionScore[] psArray = new PositionScore[ps.size()];
		ps.toArray(psArray);
		ps.clear();
		psArray = stripDuplicateValues(psArray);
		SliceInfo sliceInfo = new SliceInfo(chromosome, ".",0,0,0,null);
		PositionScoreData data = new PositionScoreData(psArray, sliceInfo);
		PositionScoreData.updateSliceInfo(psArray, sliceInfo);
		data.sliceWritePositionScoreData(rowChunkSize, saveDirectory, files2Zip);
		ps = null;
		data = null;
		psArray = null;
		in.close();
	}

	public static PositionScore[] stripDuplicateValues(PositionScore[] ps){
		ArrayList<PositionScore> al = new ArrayList<PositionScore>();
		float value = Float.MIN_VALUE;
		int lastSetIndex = -1;
		for (int i=0; i<ps.length; i++){
			float testValue = ps[i].getScore();
			//different value? Then save.
			if (testValue != value) {
				al.add(ps[i]);
				value = testValue;
				lastSetIndex = i;
			}
			//same value? Then look ahead until end reached or value changes
			else {
				for (int j = i+1; j<ps.length; j++){
					float nextValue = ps[j].getScore();
					//different value then add current ps
					if (nextValue != testValue) {
						al.add(ps[i]);
						lastSetIndex = i;
						break;
					}
					//otherwise its the same so do nothing
					else i++;
				}
			}
		}
		//add last value?
		if (lastSetIndex != (ps.length-1)) al.add(ps[ps.length-1]);
		//return
		ps = new PositionScore[al.size()];
		al.toArray(ps);
		return ps;
	}


	public static void main(String[] args) {
		if (args.length==0){
			printDocs();
			System.exit(0);
		}	
		new Wig2USeq(args);
	}		

	/**This method will process each argument and assign new varibles*/
	public void processArgs(String[] args){
		Pattern pat = Pattern.compile("-[a-z]");
		File file = null;
		for (int i = 0; i<args.length; i++){
			String lcArg = args[i].toLowerCase();
			Matcher mat = pat.matcher(lcArg);
			if (mat.matches()){
				char test = args[i].charAt(1);
				try{
					switch (test){
					case 'f': file = new File(args[i+1]); i++; break;
					case 's': skipValue = Float.parseFloat(args[++i]); break;
					case 'i': rowChunkSize = Integer.parseInt(args[++i]); break;
					case 'v': versionedGenome = args[++i]; break;
					case 'd': description = args[++i]; break;
					case 'h': color = args[++i]; break;
					case 'r': graphStyle = Integer.parseInt(args[++i]); break;
					default: USeqUtilities.printExit("\nError: unknown option! " + mat.group());
					}
				}
				catch (Exception e){
					USeqUtilities.printExit("\nSorry, something doesn't look right with this parameter: -"+test+"\n");
				}
			}
		}

		//parse files and genome version
		if (file == null) USeqUtilities.printExit("\nError: cannot find your xxx.wig file(s)?");
		File[][] tot = new File[6][];
		tot[0] = USeqUtilities.extractFiles(file,".wig");
		tot[1] = USeqUtilities.extractFiles(file,".wig.zip");
		tot[2] = USeqUtilities.extractFiles(file,".wig.gz");
		tot[3] = USeqUtilities.extractFiles(file,".bedGraph4");
		tot[4] = USeqUtilities.extractFiles(file,".bedGraph4.zip");
		tot[5] = USeqUtilities.extractFiles(file,".bedGraph4.gz");

		files = USeqUtilities.collapseFileArray(tot);
		if (files == null || files.length == 0) USeqUtilities.printExit("\nError: cannot find your xxx.wig/bedGraph4 file(s)?");
		if (versionedGenome == null) USeqUtilities.printExit("\nError: you must supply a genome version. Goto http://genome.ucsc.edu/cgi-" +
		"bin/hgGateway load your organism to find the associated genome version.\n");

		//set negative skip value?
		if (skipValue != Float.MIN_VALUE) negativeSkipValue = skipValue * -1;
	}	

	public static void printDocs(){ 
		StringBuilder sb = new StringBuilder();
		for (int i=0; i< Text2USeq.GRAPH_STYLES.length; i++){
			sb.append("      "+i+"\t"+Text2USeq.GRAPH_STYLES[i]+"\n");
		}
		System.out.println("\n" +
				"**************************************************************************************\n" +
				"**                                Wig 2 USeq: Dec 2009                              **\n" +
				"**************************************************************************************\n" +
				"Converts variable step, fixed step, and bedGraph xxx.wig/bedGraph4(.zip/.gz OK) files\n" +
				"into stair step/ heat map useq archives. Span parameters are not supported.\n\n" +

				"-f The full path directory/file text for your xxx.wig(.gz/.zip OK) file(s).\n" +
				"-v Genome version (e.g. H_sapiens_Mar_2006), get from UCSC Browser,\n" +
				"      http://genome.ucsc.edu/FAQ/FAQreleases\n"+ 
				"-s Skip wig lines with designated value/score.\n"+
				"-i Index size for slicing split chromosome data (e.g. # rows per file), defaults to\n" +
				"      100000.\n"+
				"-r Initial graph style, defaults to 1\n"+ sb+
				"-h Initial graph color, hexadecimal (e.g. #6633FF), enclose in quotations!\n"+
				"-d Description, enclose in quotations! \n"+

				"\nExample: java -Xmx1G -jar path2/Apps/Wig2USeq -f /WigFiles/ -v H_sapiens_Feb_2009\n\n" +

		"**************************************************************************************\n");		
	}

}
