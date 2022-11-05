package com.huemap.backend.bin.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.huemap.backend.bin.application.BinService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BinEventHandler {
  private final BinService binService;

  @EventListener
  public void createCandidateBin(BinCreateEvent binCreateEvent) {
    binService.save(binCreateEvent);
  }
}
