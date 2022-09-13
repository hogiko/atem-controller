/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.conxult.tally.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author joerg
 */

@Singleton
@Getter
public class TallyControllerConfiguration {

  @ConfigProperty(name = "application.atem.hostname")
  String atemHostname;

  @ConfigProperty(name = "application.atem.port")
  int atemPort;

  @ConfigProperty(name = "application.debug")
  boolean debug = false;

  @PostConstruct
  public void loadConfiguratonFromFile() {
    for (String directory : List.of(System.getProperty("user.home", ""), ".")) {
      File propertiesFile = new File(directory + "/TallyController.properties");
      try {
        Properties properties = new Properties();
        properties.load(new FileReader(propertiesFile));
        TallyLogger.info("properties file {0} contains {1}", propertiesFile.getAbsolutePath(), properties.keySet());
        atemHostname = properties.getProperty("atem.hostname", atemHostname);
      } catch (FileNotFoundException fileNotFoundException) {
        TallyLogger.info("properties file {0} not found", propertiesFile.getAbsolutePath());
      } catch (IOException ioException) {
        TallyLogger.info("properties file {0} loading error: {1}", propertiesFile.getAbsolutePath(), ioException);
      }
    }
  }

}
