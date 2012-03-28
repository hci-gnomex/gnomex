package hci.gnomex.utility;


import hci.framework.model.DetailObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.biojava.bio.chromatogram.Chromatogram;
import org.biojava.bio.chromatogram.ChromatogramFactory;
import org.biojava.bio.chromatogram.UnsupportedChromatogramFormatException;
import org.biojava.bio.program.abi.ABIFParser;
import org.biojava.bio.program.abi.ABITrace;


public class ChromatTrimUtil extends DetailObject implements Serializable {
  
  protected File              abiFile;
  protected ABITrace          trace;
  protected Chromatogram      chrom;
  protected int[]             qualArray;
  
  
  // Trim ends until there are more than (reqPercent)% good bases
  // in a (windowLength) bases window
  // A base must have a QV higher than (acceptableQV) to be considered good
  protected Integer           acceptableQV;
  protected Integer           reqPercent;
  protected Integer           windowLength;
  
  
  
  public ChromatTrimUtil(File abiFile) {
    this.abiFile = abiFile;
    initialize();
  }
  
  public void initialize() {
    try {
      trace = new ABITrace(abiFile);

      chrom = ChromatogramFactory.create(abiFile);
    
      qualArray = getQualArray();


    } catch (UnsupportedChromatogramFormatException e){
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  // Find largest trim interval with maximum qual score where
  // qual score for an interval is the sum of the (quality scores - acceptable qual val)
  // Returns the start and stop indices, respectively, in an array
  public int[] getTrimInterval() {
    
    int[] vals = getQualArray();
    
    int maxSumSoFar = -2147483648;
    int curSum = 0;
    int a = 0;
    int b = 0;
    int s = 0;
    int i = 0;
    
    int len = vals.length;
    
    // Get base quality values by subtracting off the quality threshold
    for ( i = 0; i < len; i++ ) {
      vals[i] = vals[i] - acceptableQV;
    }
    
    // Find the sub-array with the max sum of base quality values
    for( i = 0; i < len; i++ ) {
        curSum += vals[i];
        if ( curSum > maxSumSoFar ) {
            maxSumSoFar = curSum;
            a = s;
            b = i;
        }
        if( curSum < 0 ) {
            curSum = 0;
            s = i + 1;
        }
    }
    
    int[] startAndStop = new int[2];
    startAndStop[0] = a;
    startAndStop[1] = b;
    return startAndStop;
  }
  
  public int getStartTrimIndex() {
    for (int pos = 0; pos <= (qualArray.length - windowLength); pos ++ ) {
      int[] cutArray = new int[windowLength]; 
      System.arraycopy(qualArray, pos, cutArray, 0, windowLength);
      
      // Trim until the percent of bases in the window with an acceptable value is greater
      // than the acceptable percentage
      double qperc = getreqPercent(cutArray, acceptableQV);
      double thresh = ( (double) reqPercent)/100;
      if ( qperc >= thresh ) {
        return pos;
      }
      
    }
    return -1;
  }
  
  public int getStopTrimIndex() {
    for (int pos = qualArray.length - windowLength; pos >= 0; pos -- ) {
      int[] cutArray = new int[windowLength]; 
      System.arraycopy(qualArray, pos, cutArray, 0, windowLength);
      
      // Trim until the percent of bases in the window with an acceptable value is greater
      // than the acceptable percentage
      double qperc = getreqPercent(cutArray, acceptableQV);
      double thresh = ( (double) reqPercent)/100;
      if ( qperc >= thresh ) {
        return pos;
      }
      
    }
    return -1;
  }

  public int getTrimLength() {
    if ( getStartTrimIndex() == -1 || getStopTrimIndex() == -1 ) {
      return -1;
    }
    return getStopTrimIndex() - getStartTrimIndex() + 1;
  }
  
  // Get an integer array of the quality values for the trace using ABI file parser
  private int[] getQualArray() {
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
  
  // Get the total number of quality scores from an array that are above a certain value 
  private int getQ(int[] vals, int qVal) {
    int count = 0;
    
    for ( int i=0; i<vals.length; i++ ) {
      if (vals[i]>=qVal) {
        count ++;
      }
    }
    return count;
  }
  
  // Get the percentage of quality scores from an array that are above a certain value
  private double getreqPercent(int[] vals, int qVal) {
    int length = vals.length;
    int q = getQ(vals, qVal);
    
    return (double) q/length;
  }
  
  // Get the average quality value for an array
  private double getAverageQ(int[] vals) {
    int sum = 0;
    for ( int i=0; i<vals.length; i++) {
      sum += vals[i];
    }
    return sum/(double) vals.length;
  }
  
  public Integer getAcceptableQV()
  {
    return acceptableQV;
  }

  public void setAcceptableQV(Integer acceptableQV)
  {
    this.acceptableQV = acceptableQV;
  }

  public Integer getReqPercent()
  {
    return reqPercent;
  }

  public void setReqPercent(Integer reqPercent)
  {
    this.reqPercent = reqPercent;
  }

  public Integer getWindowLength()
  {
    return windowLength;
  }

  public void setWindowLength(Integer windowLength)
  {
    this.windowLength = windowLength;
  }
  
}
