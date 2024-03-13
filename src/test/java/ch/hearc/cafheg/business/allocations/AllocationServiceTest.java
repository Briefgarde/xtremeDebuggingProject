package ch.hearc.cafheg.business.allocations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AllocationServiceTest {

  private static final String PARENT_1 = "Parent1";
  private static final String PARENT_2 = "Parent2";

  private AllocationService allocationService;

  private AllocataireMapper allocataireMapper;
  private AllocationMapper allocationMapper;

  @BeforeEach
  void setUp() {
    allocataireMapper = Mockito.mock(AllocataireMapper.class);
    allocationMapper = Mockito.mock(AllocationMapper.class);

    allocationService = new AllocationService(allocataireMapper, allocationMapper);
  }
//
//  @Test
//  void findAllAllocataires_GivenEmptyAllocataires_ShouldBeEmpty() {
//    Mockito.when(allocataireMapper.findAll("Geiser")).thenReturn(Collections.emptyList());
//    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
//    assertThat(all).isEmpty();
//  }
//
//  @Test
//  void findAllAllocataires_Given2Geiser_ShouldBe2() {
//    Mockito.when(allocataireMapper.findAll("Geiser"))
//        .thenReturn(Arrays.asList(new Allocataire(new NoAVS("1000-2000"), "Geiser", "Arnaud"),
//            new Allocataire(new NoAVS("1000-2001"), "Geiser", "Aurélie")));
//    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
//    assertAll(() -> assertThat(all.size()).isEqualTo(2),
//        () -> assertThat(all.get(0).getNoAVS()).isEqualTo(new NoAVS("1000-2000")),
//        () -> assertThat(all.get(0).getNom()).isEqualTo("Geiser"),
//        () -> assertThat(all.get(0).getPrenom()).isEqualTo("Arnaud"),
//        () -> assertThat(all.get(1).getNoAVS()).isEqualTo(new NoAVS("1000-2001")),
//        () -> assertThat(all.get(1).getNom()).isEqualTo("Geiser"),
//        () -> assertThat(all.get(1).getPrenom()).isEqualTo("Aurélie"));
//  }
//
//  @Test
//  void findAllocationsActuelles() {
//    Mockito.when(allocationMapper.findAll())
//        .thenReturn(Arrays.asList(new Allocation(new Montant(new BigDecimal(1000)), Canton.NE,
//            LocalDate.now(), null), new Allocation(new Montant(new BigDecimal(2000)), Canton.FR,
//            LocalDate.now(), null)));
//    List<Allocation> all = allocationService.findAllocationsActuelles();
//    assertAll(() -> assertThat(all.size()).isEqualTo(2),
//        () -> assertThat(all.get(0).getMontant()).isEqualTo(new Montant(new BigDecimal(1000))),
//        () -> assertThat(all.get(0).getCanton()).isEqualTo(Canton.NE),
//        () -> assertThat(all.get(0).getDebut()).isEqualTo(LocalDate.now()),
//        () -> assertThat(all.get(0).getFin()).isNull(),
//        () -> assertThat(all.get(1).getMontant()).isEqualTo(new Montant(new BigDecimal(2000))),
//        () -> assertThat(all.get(1).getCanton()).isEqualTo(Canton.FR),
//        () -> assertThat(all.get(1).getDebut()).isEqualTo(LocalDate.now()),
//        () -> assertThat(all.get(1).getFin()).isNull());
//  }

  @Test
  @DisplayName("If parent 1 is active and not parent 2, should be P1")
  public void getParentDroitAllocation_GivenParent1Active_ShouldReturnParent1() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", false);

    String result = allocationService.getParentDroitAllocation(parameters);

    Assertions.assertEquals(PARENT_1, result);
  }

  @Test
  @DisplayName("If parent 1 is NOT active and parent 2 IS, should be P2")
  public void getParentDroitAllocation_GivenParent2Active_ShouldReturnParent2() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", false);
    parameters.put("parent2ActiviteLucrative", true);

    String result = allocationService.getParentDroitAllocation(parameters);

    assertEquals(PARENT_2, result);
  }

  @Test
  public void getParentDroitAllocation_GivenParentsActive_and_P1_WithAuthority_ShouldBeP1() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("parent1WithParentalAuthority", true);
    parameters.put("parent2WithParentalAuthority", false);
    String result = allocationService.getParentDroitAllocation(parameters);

    assertEquals(PARENT_1, result);
  }

  @Test
  public void getParentDroitAllocation_GivenParentsActive_and_P2_WithAuthority_ShouldBeP2() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("parent1WithParentalAuthority", false);
    parameters.put("parent2WithParentalAuthority", true);
    String result = allocationService.getParentDroitAllocation(parameters);

    assertEquals(PARENT_2, result);
  }

  @Test
  public void getParentDroitAllocation_GivenParentsSeparatedAndChildWithP1_ShouldBeP1() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("parent1WithParentalAuthority", true);
    parameters.put("parent2WithParentalAuthority", true);
    parameters.put("parentsEnsemble", false);
    parameters.put("enfantResidence", 1);
    String result = allocationService.getParentDroitAllocation(parameters);

    assertEquals(PARENT_1, result);
  }
  @Test
  public void getParentDroitAllocation_GivenParentsSeparatedAndChildWithP2_ShouldBeP2() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("parent1WithParentalAuthority", true);
    parameters.put("parent2WithParentalAuthority", true);
    parameters.put("parentsEnsemble", false);
    parameters.put("enfantResidence", 2);
    String result = allocationService.getParentDroitAllocation(parameters);

    assertEquals(PARENT_2, result);
  }

  @Test
  public void getParentDroitAllocation_GivenParentsTogetherAndP1WorkInSameCantonAsChild_ShouldBeP1() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("parent1WithParentalAuthority", true);
    parameters.put("parent2WithParentalAuthority", true);
    parameters.put("parentsEnsemble", true);
    parameters.put("P1WorkInChildCanton", true);
    parameters.put("P2WorkInChildCanton", false);
    String result = allocationService.getParentDroitAllocation(parameters);

    assertEquals(PARENT_1, result);
  }
  @Test
  public void getParentDroitAllocation_GivenParentsTogetherAndP2WorkInSameCantonAsChild_ShouldBeP2() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("parent1WithParentalAuthority", true);
    parameters.put("parent2WithParentalAuthority", true);
    parameters.put("parentsEnsemble", true);
    parameters.put("P1WorkInChildCanton", false);
    parameters.put("P2WorkInChildCanton", true);
    String result = allocationService.getParentDroitAllocation(parameters);

    assertEquals(PARENT_2, result);
  }

  @Test
  public void getParentDroitAllocation_GivenParentsTogetherAndBothWorkInCantonOfChildButP1IsRich_ShouldBeP1() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("parent1WithParentalAuthority", true);
    parameters.put("parent2WithParentalAuthority", true);
    parameters.put("parentsEnsemble", true);
    parameters.put("P1WorkInChildCanton", true);
    parameters.put("P2WorkInChildCanton", true);
    parameters.put("parent1Salaire", 10);
    parameters.put("parent2Salaire", 5);
    String result = allocationService.getParentDroitAllocation(parameters);

    assertEquals(PARENT_1, result);
  }
  @Test
  public void getParentDroitAllocation_GivenParentsTogetherAndBothWorkInCantonOfChildButP2IsRich_ShouldBeP2() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("parent1WithParentalAuthority", true);
    parameters.put("parent2WithParentalAuthority", true);
    parameters.put("parentsEnsemble", true);
    parameters.put("P1WorkInChildCanton", true);
    parameters.put("P2WorkInChildCanton", true);
    parameters.put("parent1Salaire", 10);
    parameters.put("parent2Salaire", 15);
    String result = allocationService.getParentDroitAllocation(parameters);

    assertEquals(PARENT_2, result);
  }
  @Test
  public void getParentDroitAllocation_GivenParentsTogetherAndAreSalariedAndP2isRich_ShouldBeP2() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("parent1WithParentalAuthority", true);
    parameters.put("parent2WithParentalAuthority", true);
    parameters.put("parentsEnsemble", true);
    parameters.put("P1WorkInChildCanton", false);
    parameters.put("P2WorkInChildCanton", false);
    parameters.put("P1Salaried", true);
    parameters.put("P2Salaried", true);
    parameters.put("parent1Salaire", 10);
    parameters.put("parent2Salaire", 15);
    String result = allocationService.getParentDroitAllocation(parameters);

    assertEquals(PARENT_2, result);
  }
  @Test
  public void getParentDroitAllocation_GivenParentsTogetherAndAreSalariedAndP1isRich_ShouldBeP1() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("parent1WithParentalAuthority", true);
    parameters.put("parent2WithParentalAuthority", true);
    parameters.put("parentsEnsemble", true);
    parameters.put("P1WorkInChildCanton", false);
    parameters.put("P2WorkInChildCanton", false);
    parameters.put("P1Salaried", true);
    parameters.put("P2Salaried", true);
    parameters.put("parent1Salaire", 10);
    parameters.put("parent2Salaire", 5);
    String result = allocationService.getParentDroitAllocation(parameters);

    assertEquals(PARENT_1, result);
  }

  @Test
  public void getParentDroitAllocation_GivenParentsTogetherAndP1IsSalaried_ShouldBeP1() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("parent1WithParentalAuthority", true);
    parameters.put("parent2WithParentalAuthority", true);
    parameters.put("parentsEnsemble", true);
    parameters.put("P1WorkInChildCanton", false);
    parameters.put("P2WorkInChildCanton", false);
    parameters.put("P1Salaried", true);
    parameters.put("P2Salaried", false);
    String result = allocationService.getParentDroitAllocation(parameters);

    assertEquals(PARENT_1, result);
  }
  public void getParentDroitAllocation_GivenParentsTogetherAndP2IsSalaried_ShouldBeP2() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("parent1WithParentalAuthority", true);
    parameters.put("parent2WithParentalAuthority", true);
    parameters.put("parentsEnsemble", true);
    parameters.put("P1WorkInChildCanton", false);
    parameters.put("P2WorkInChildCanton", false);
    parameters.put("P1Salaried", false);
    parameters.put("P2Salaried", true);
    String result = allocationService.getParentDroitAllocation(parameters);

    assertEquals(PARENT_2, result);
  }

  @Test
  public void getParentDroitAllocation_GivenParentsTogetherAndAreIndependantAndP1isRich_ShouldBeP1() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("parent1WithParentalAuthority", true);
    parameters.put("parent2WithParentalAuthority", true);
    parameters.put("parentsEnsemble", true);
    parameters.put("P1WorkInChildCanton", false);
    parameters.put("P2WorkInChildCanton", false);
    parameters.put("P1Salaried", false);
    parameters.put("P2Salaried", false);
    parameters.put("parent1Salaire", 10);
    parameters.put("parent2Salaire", 5);
    String result = allocationService.getParentDroitAllocation(parameters);

    assertEquals(PARENT_1, result);
  }

  @Test
  public void getParentDroitAllocation_GivenParentsTogetherAndAreIndependantAndP2isRich_ShouldBeP2() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("parent1WithParentalAuthority", true);
    parameters.put("parent2WithParentalAuthority", true);
    parameters.put("parentsEnsemble", true);
    parameters.put("P1WorkInChildCanton", false);
    parameters.put("P2WorkInChildCanton", false);
    parameters.put("P1Salaried", false);
    parameters.put("P2Salaried", false);
    parameters.put("parent1Salaire", 10);
    parameters.put("parent2Salaire", 15);
    String result = allocationService.getParentDroitAllocation(parameters);

    assertEquals(PARENT_2, result);
  }

  @Test
  @DisplayName("If both parents don't work, method should give an error.")
  public void getParentDroitAllocations_GivenNoParentHaveAL_ShouldBeError() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parent1ActiviteLucrative", false);
    parameters.put("parent2ActiviteLucrative", false);

    assertThrows(IllegalArgumentException.class,
            ()-> allocationService.getParentDroitAllocation(parameters));
  }
}