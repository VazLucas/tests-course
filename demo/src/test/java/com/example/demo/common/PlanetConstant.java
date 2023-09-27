package com.example.demo.common;

import com.example.demo.domain.Planet;

public class PlanetConstant {
  public static final Planet PLANET = new Planet("mars", "tropical", "mud");
  public static final Planet INVALID_PLANET = new Planet("", "", "");
}
