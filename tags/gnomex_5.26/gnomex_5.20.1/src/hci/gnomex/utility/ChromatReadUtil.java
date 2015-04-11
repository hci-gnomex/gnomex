package hci.gnomex.utility;


import hci.framework.model.DetailObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Arrays;

import org.biojava.bio.chromatogram.Chromatogram;
import org.biojava.bio.chromatogram.ChromatogramFactory;
import org.biojava.bio.chromatogram.UnsupportedChromatogramFormatException;
import org.biojava.bio.program.abi.ABIFParser;
import org.biojava.bio.program.abi.ABITrace;
import org.biojava.bio.seq.DNATools;


public class ChromatReadUtil extends DetailObject implements Serializable {
  
  protected File              abiFile;
  protected ABITrace          trace;
  protected Chromatogram      chrom;
  
  
    
  public ChromatReadUtil(File abiFile) {
    this.abiFile = abiFile;
    initialize();
  }
  
  public void initialize() {
    try {
      trace = new ABITrace(abiFile);

      chrom = ChromatogramFactory.create(abiFile);
    

    } catch (UnsupportedChromatogramFormatException e){
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  //Returns the signal strengths for bases G, A, T, C respectively
  public int[] getSignalStrengths() {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("S/N%", 1);

      int count = (int) r1.numberOfElements;
      int[] data = new int[count];
      int max = -1;
        byte[] shortArray = r1.offsetData;
        int i = 0;
        for ( int s=0; s<shortArray.length; s+=2) {
          data[i] = ((short)((shortArray[s]<<8)|(shortArray[s+1] & 0xff))) & 0xffff;
          max = Math.max(data[i++], max);
        }
      return data;
    }
    catch (IOException e) {
      e.printStackTrace();
      return new int[0];
    }
  }
  
  //Returns the raw data for bases G, A, T, C 
  public int[] getRawData( String base ) {
    int baseInd;
    if ( base.equals( "G" ) ) {
      baseInd = 1;
    } else if ( base.equals( "A" ) ) {
      baseInd = 2;
    } else if ( base.equals( "T" ) ) {
      baseInd = 3;
    } else if ( base.equals( "C" ) ) {
      baseInd = 4;
    } else {
      return null; 
    }
    
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("DATA", baseInd);

      int count = (int) r1.numberOfElements;
      int[] data = new int[count];
      int max = -1;
      byte[] shortArray = r1.offsetData;
      int i = 0;
      for ( int s=0; s<shortArray.length; s+=2) {
        data[i] = (short)(( (shortArray[s] & 0xff) << 8 ) | ( (shortArray[s+1] & 0xff) << 0 ) );
        max = Math.max(data[i++], max);
      }

      return data;
    }
    catch (IOException e) {
      e.printStackTrace();
      return new int[0];
    }
  }
  
  public String getRawDataString( String base ) {
    // Quality to String
    int[] data = getRawData(base);
    String rawData = "" + data[0];
    for ( int i = 0; i < data.length; i=i+2) {
      rawData += "," + data[i];
    }
    return rawData;
  }
  
  public String getInstrModel() {
    try {
      StringBuffer sb = new StringBuffer();
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("MODL", 1);
      sb.append((char) ((r1.dataRecord >>> 24) & 0xFF));
      sb.append((char) ((r1.dataRecord >>> 16) & 0xFF));
      sb.append((char) ((r1.dataRecord >>> 8) & 0xFF));
      sb.append((char) (r1.dataRecord  & 0xFF));
      return sb.toString();
    }
    catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
  
  public String getSeq() {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("PBAS", 2);
      
      int count = (int) r1.numberOfElements;
      char[] data = new char[count];
      int max = -1;

      byte[] byteArray = r1.offsetData;
      for ( int i=0; i < byteArray.length; i++) {
        data[i] = (char) ( (char) (byteArray[i]) & 0xFF);
        max = Math.max(data[i],max);
      }
      
      return new String(data);
    }
    catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
  
  public int[] getQualVals() {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("PCON", 2);

      int count = (int) r1.numberOfElements;
      int[] data = new int[count];
      int max = -1;

      byte[] byteArray = r1.offsetData;
      for ( int i=0; i < byteArray.length; i++) {
        data[i] = (char) ( (byteArray[i]) & 0xFF );
        max = Math.max(data[i],max);
      }
      
      return data;
    }
    catch (IOException e) {
      e.printStackTrace();
      return new int[0];
    }
  }
  
  public int getQ(int qVal) {
    int count = 0;
    int[] vals = getQualVals();
    
    for ( int i=0; i<vals.length; i++ ) {
      if (vals[i]>=qVal) {
        count ++;
      }
    }
    return count;
  }
  
  public double getQPercent(int qVal) {
    int length = getQualVals().length;
    int q = getQ(qVal);
    
    DecimalFormat twoDForm = new DecimalFormat("#.##");
    double q_len = (double) q/length;
    
    return Double.valueOf(twoDForm.format(q_len));
    
  }
  
  public String getLane() {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("LANE", 1);
      
      return   Long.toString(r1.dataRecord >>> 16);
    }
    catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
  
  public String getReferenceScanNumber() {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("B1Pt", 2);
      
      return   Long.toString(r1.dataRecord >>> 16);
    }
    catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
  
  public String getAnalysisEndScanNumber() {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("AEPt", 2);
      
      return   Long.toString(r1.dataRecord >>> 16);
    }
    catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
  
  public String getNumberOfLanes() {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("NLNE", 1);
      
      return   Long.toString(r1.dataRecord >>> 16);
    }
    catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
  
  public String getSpacing() {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("SPAC", 1);
      float spac = Float.intBitsToFloat((int) r1.dataRecord ); 
      
      return   String.valueOf(spac);
    }
    catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
  
  public String getStartDate() {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("RUND", 1);
      
      return   Long.toString((r1.dataRecord >>> 16) & 0xffff) + "/" + 
               Long.toString((r1.dataRecord >>> 8) & 0xff) + "/" +
               Long.toString((r1.dataRecord) & 0xff);
    }
    catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
  
  public String getStopDate() {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("RUND", 2);
      
      return   Long.toString((r1.dataRecord >>> 16) & 0xffff) + "/" + 
               Long.toString((r1.dataRecord >>> 8) & 0xff) + "/" +
               Long.toString((r1.dataRecord) & 0xff);
    }
    catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
  
  public String getStartTime() {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("RUNT", 1);
      
      return   Long.toString((r1.dataRecord >>> 24) & 0xff) + ":" + 
               Long.toString((r1.dataRecord >>> 16) & 0xff) + ":" +
               Long.toString((r1.dataRecord >>> 8) & 0xff);
    }
    catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
  
  public String getStopTime() {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("RUNT", 2);
      
      return   Long.toString((r1.dataRecord >>> 24) & 0xff) + ":" + 
               Long.toString((r1.dataRecord >>> 16) & 0xff) + ":" +
               Long.toString((r1.dataRecord >>> 8) & 0xff);
    }
    catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
  
  // Returns the comments from the abi file
  public String getComments() {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("CMNT", 1);
      byte[] byteArray = r1.offsetData;
      
      return new String(Arrays.copyOfRange(byteArray, 1, byteArray.length));
            
    }
    catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
  
  public String getMobility() {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("PDMF", 1);
      byte[] byteArray = r1.offsetData;
      
      return new String(Arrays.copyOfRange(byteArray, 1, byteArray.length));
            
    }
    catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
  
  public String getUser() {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("User", 1);
      byte[] byteArray = r1.offsetData;
      
      return new String(Arrays.copyOfRange(byteArray, 1, byteArray.length));
            
    }
    catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
  
  public String getInstrName() {
    try {
      ABIFParser abiParse = new ABIFParser(abiFile);
      ABIFParser.TaggedDataRecord r1=abiParse.getDataRecord("MCHN", 1);
      byte[] byteArray = r1.offsetData;
      
      return new String(Arrays.copyOfRange(byteArray, 1, byteArray.length));
            
    }
    catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }

  public String getBaseCalls() {
    
      int[] baseCalls = trace.getBasecalls();
      String bc = Arrays.toString( baseCalls );
      bc = bc.substring( 1, bc.length()-1 );
      bc = bc.replaceAll( " ", "" );
      return bc;
  }

  public String getATrace() {
    try {
      int[] aTrace = trace.getTrace(DNATools.a());
      String at = Arrays.toString( aTrace );
      at = at.substring( 1, at.length()-1 );
      at = at.replaceAll( " ", "" );
      return at;
    }
    catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  public String getCTrace() {
    try {
      int[] cTrace = trace.getTrace(DNATools.c());
      String ct = Arrays.toString( cTrace );
      ct = ct.substring( 1, ct.length()-1 );
      ct = ct.replaceAll( " ", "" );
      return ct;
    }
    catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  public String getGTrace() {
    try {
      int[] gTrace = trace.getTrace(DNATools.g());
      String gt = Arrays.toString( gTrace );
      gt = gt.substring( 1, gt.length()-1 );
      gt = gt.replaceAll( " ", "" );
      return gt;
    }
    catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  public String getTTrace() {
    try {
      int[] tTrace = trace.getTrace(DNATools.t());
      String tt = Arrays.toString( tTrace );
      tt = tt.substring( 1, tt.length()-1 );
      tt = tt.replaceAll( " ", "" );
      return tt;
    }
    catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }
  
  
  
  public File getAbiFile() {
    return abiFile;
  }

  
  public void setAbiFile( File abiFile ) {
    this.abiFile = abiFile;
  }

  
  
  public Chromatogram getChrom() {
    return chrom;
  }

  
  public void setChrom( Chromatogram chrom ) {
    this.chrom = chrom;
  }

  public ABITrace getTrace() {
    return trace;
  }

  
  public void setTrace( ABITrace trace ) {
    this.trace = trace;
  }
  
}
