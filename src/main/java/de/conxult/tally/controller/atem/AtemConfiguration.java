/*
 * Copyright by https://conxult.de
 */
package de.conxult.tally.controller.atem;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author joerg
 */

public class AtemConfiguration {

  static final String UNKNOWN = "Unknown";
  
  Map<String, String> inputLabels  = new HashMap<>();
  Map<String, String> outputLabels = new HashMap<>();
  
  public AtemConfiguration setInputLabel(String key, String label) {
      inputLabels.put(key, label);
      return this;
  }
      
  public String getInputLabel(String key) {
      return inputLabels.getOrDefault(key, UNKNOWN);
  }
  
  public AtemConfiguration setOutputLabel(String key, String label) {
      outputLabels.put(key, label);
      return this;
  }
      
  public String getOutputLabel(String key) {
      return outputLabels.getOrDefault(key, UNKNOWN);
  }

}
