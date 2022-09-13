/*
 * Copyright by https://conxult.de
 */
package de.conxult.tally.controller.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author joerg
 */

@Getter @Setter @Accessors(chain = true)
public class TallyLightInfo {

  String          name;
  TallyLightState state = TallyLightState.BLACK;
  @JsonIgnore
  Date            stamp = new Date();

  public TallyLightInfo(String name) {
    this.name = name;
  }

  public TallyLightInfo setState(TallyLightState newState) {
    this.state = newState;
    stamp = new Date();
    return this;
  }

  public boolean isBlack() {
    return state == TallyLightState.BLACK;
  }

  public boolean isStandBy() {
    return state == TallyLightState.STAND_BY;
  }

  public boolean isOnAir() {
    return state == TallyLightState.ON_AIR;
  }
}
