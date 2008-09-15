package hci.gnomex.utility;

import java.math.BigDecimal;


public class MolarityCalculator {
  
  private static int DNA_NUCEOTIDE_MOLECULAR_WEIGHT_ng = 660;
  
  
  public static double calculateConcentrationInnM(double concentration_ngul, double averageFragmentSize) {
    
    double molarity = (concentration_ngul / (DNA_NUCEOTIDE_MOLECULAR_WEIGHT_ng * averageFragmentSize)) * 1E6;
    return molarity;
  }
  
  public static double calculateDilutionVol(double concentration, double desiredConcentration, double desiredVol) {
    double soluteVol = (desiredVol * desiredConcentration) / concentration;
    return soluteVol;
  }

}
