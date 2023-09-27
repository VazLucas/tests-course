package com.example.demo.domain;

import static com.example.demo.common.PlanetConstant.INVALID_PLANET;
import static com.example.demo.common.PlanetConstant.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.doThrow;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

@ExtendWith(MockitoExtension.class)
// for large applications, SpringBootTest will consume more resources, for unit
// tests use mockito as above
// @SpringBootTest(classes = PlanetService.class)
public class PlanetServiceTest {

  @InjectMocks
  // @Autowired -> SpringBootTest
  private PlanetService planetService;

  @Mock
  // @MockBean -> SpringBootTest
  private PlanetRepository planetRepository;

  // operação_estado_retorno
  @Test
  public void createPlanet_ValidData_ReturnPlanet() {
    //setup
    when(planetRepository.save(PLANET)).thenReturn(PLANET);
    // sut = system under test

    //act
    Planet sutPlanet = planetService.create(PLANET);
    //assert
    assertThat(sutPlanet).isEqualTo(PLANET);
  }

  @Test
  public void createPlanet_InvalidData_ThrowsException ()
  {
    when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

    assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    
  }

  @Test
  public void getPlanet_ExistingId_returnPlanet() {
    when (planetRepository.findById(anyLong())).thenReturn(Optional.of(PLANET));

    Optional<Planet> sutPlannet = planetService.get(1L);

    assertThat(sutPlannet).isNotEmpty();
    
    assertThat(sutPlannet.get()).isEqualTo(PLANET);
  }

  @Test
  public void getPlanet_NotExistingId_ReturnsEmpty() {
    when (planetRepository.findById(1L)).thenReturn(Optional.empty());

    Optional<Planet> sutPlannet = planetService.get(1L);

    assertThat(sutPlannet).isEmpty();
  }

  @Test
  public void getPlanet_ExistingName_ReturnsPlanet() {
    when (planetRepository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

    Optional<Planet> sutPlannet = planetService.getByName(PLANET.getName());

    assertThat(sutPlannet).isNotEmpty();
    assertThat(sutPlannet.get()).isEqualTo(PLANET);
  }

  @Test
  public void getPlanet_NotExistingName_ReturnsEmpty() {
    final String name = "Unexisting Name";

    when(planetRepository.findByName(name)).thenReturn(Optional.empty());

    Optional<Planet> sutPlannet = planetService.getByName(name);

    assertThat(sutPlannet).isEmpty();
  }

  @Test
  public void listPlanet_ReturnsAllPlanets() {
    List<Planet> planets = new ArrayList<>() {
      {
        add(PLANET);
      }
    };
    Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimate(), PLANET.getTerrain()));
    when(planetRepository.findAll(query)).thenReturn(planets);

    List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());

    assertThat(sut).isNotEmpty();
    assertThat(sut).hasSize(1);
    assertThat(sut.get(0)).isEqualTo(PLANET);
  }

  @Test
  public void listPlanet_ReturnsNoPlanets() {
    

    when(planetRepository.findAll(any())).thenReturn(Collections.emptyList());

    List<Planet> sutPlanets = planetService.list(PLANET.getTerrain(), PLANET.getClimate());

    assertThat(sutPlanets).isEmpty();
  }

  @Test
  public void removePlanet_WithExistingId_doesNotThrowAnyException() {
    assertThatCode(() -> planetService.remove(1L)).doesNotThrowAnyException();
  }

  @Test
  public void removePlanet_WithUnexistingId_ThrowsException() {
    doThrow(new RuntimeException()).when(planetRepository).deleteById(99L);

    assertThatThrownBy(() -> planetService.remove(99L)).isInstanceOf(RuntimeException.class);
  }
}
