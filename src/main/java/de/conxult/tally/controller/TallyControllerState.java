/*
 * Copyright by https://conxult.de
 */
package de.conxult.tally.controller;

import de.conxult.tally.controller.domain.TallyLightInfo;
import de.conxult.tally.controller.domain.TallyLightState;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author joerg
 */
@Singleton
@Getter @Setter @Accessors(chain = true)
public class TallyControllerState {

  Map<String, TallyLightInfo>    tallyLights  = new ConcurrentHashMap<>();
  Map<String, List<PendingCall>> pendingCalls = new ConcurrentHashMap<>();
  TallyLightInfo                 none         = new TallyLightInfo("(none)");
  Map<String, Date>              stamps       = new HashMap<>();

  public TallyLightInfo getTallyLight(String name) {
    return tallyLights.computeIfAbsent(name, (n) -> new TallyLightInfo(name));
  }

  public Stream<TallyLightInfo> getTallyLights(TallyLightState state) {
    return tallyLights.values().stream().filter((tls) -> tls.getState() == state);
  }

  public TallyLightInfo getStandBy() {
    return getTallyLights(TallyLightState.STAND_BY).findFirst().orElse(none);
  }

  public TallyLightInfo getOnAir() {
    return getTallyLights(TallyLightState.ON_AIR).findFirst().orElse(none);
  }

  public void addPendingCall(String name, PendingCall pendingCall) {
    getPendingCalls(name)
      .add(pendingCall);
  }

  public void removePendingCall(String name, PendingCall pendingCall) {
    getPendingCalls(name)
      .removeIf((pc) -> pc == pendingCall);
  }

  public void triggerPendingCalls(String name) {
    TallyLightInfo tallyLightInfo = getTallyLight(name);
    for (PendingCall pendingCall : getPendingCalls(name)) {
      pendingCall.complete(tallyLightInfo);
    }
    getPendingCalls(name).clear();
  }

  List<PendingCall> getPendingCalls(String name) {
    return pendingCalls.computeIfAbsent(name, ($) -> Collections.synchronizedList(new ArrayList<>()));
  }

  static class PendingCall extends
    CompletableFuture<TallyLightInfo> {
    String name;

    public PendingCall(String name) {
      this.name = name;
    }

  }
}
