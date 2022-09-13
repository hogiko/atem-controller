/*
 * Copyright by https://conxult.de
 */
package de.conxult.tally.controller.atem;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author joerg
 */

@Getter @Setter @Accessors(chain = true)
public class AtemConfiguration {

  Map<String, String> inputLabels  = new HashMap<>();
  Map<String, String> outputLabels = new HashMap<>();

}
