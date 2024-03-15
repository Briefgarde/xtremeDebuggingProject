package ch.hearc.cafheg.infrastructure.api;

import static ch.hearc.cafheg.infrastructure.persistance.Database.inTransaction;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.Allocation;
import ch.hearc.cafheg.business.allocations.AllocationService;
import ch.hearc.cafheg.business.allocations.Canton;
import ch.hearc.cafheg.business.allocations.Famille;
import ch.hearc.cafheg.business.allocations.NoAVS;
import ch.hearc.cafheg.business.versements.VersementService;
import ch.hearc.cafheg.infrastructure.pdf.PDFExporter;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import ch.hearc.cafheg.infrastructure.persistance.EnfantMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
public class RESTController {

  private final AllocationService allocationService;
  private final VersementService versementService;

  public RESTController() {
    this.allocationService = new AllocationService(new AllocataireMapper(), new AllocationMapper());
    this.versementService = new VersementService(new VersementMapper(), new AllocataireMapper(),
        new PDFExporter(new EnfantMapper()));
  }

  /*
  // Headers de la requête HTTP doit contenir "Content-Type: application/json"
  // BODY de la requête HTTP à transmettre afin de tester le endpoint
  {
      "enfantResidence" : "Neuchâtel",
      "parent1Residence" : "Neuchâtel",
      "parent2Residence" : "Bienne",
      "parent1ActiviteLucrative" : true,
      "parent2ActiviteLucrative" : true,
      "parent1Salaire" : 2500,
      "parent2Salaire" : 3000
  }

  new body of the request with updated parameters
  {
    "parent1" : {
      "noAVS" : "756.1234.5678.97",
      "nom" : "Doe",
      "prenom" : "John",
      "isActive" : true,
      "isSalaried" : true,
      "parentalAuthority" : true,
      "salaire" : 2500,
      "lifeCanton" : "Neuchâtel"
    },
    "parent2" : {
      "noAVS" : "756.1234.5678.98",
      "nom" : "Doe",
      "prenom" : "Jane",
      "isActive" : true,
      "isSalaried" : true,
      "parentalAuthority" : true,
      "salaire" : 3000,
      "lifeCanton" : "Bienne"
    },
    "enfant" : {
      "noAVS" : "756.1234.5678.99",
      "nom" : "Doe",
      "prenom" : "Jack",
      "canton" : "Neuchâtel"
    }
  }
   */
  @PostMapping("/droits/quel-parent")
  public String getParentDroitAllocation(@RequestBody Map<String, Object> params) {
    Famille famille = new Famille(params);
    return inTransaction(() -> allocationService.getParentDroitAllocation(famille));
  }

  @GetMapping("/allocataires")
  public List<Allocataire> allocataires(
      @RequestParam(value = "startsWith", required = false) String start) {
    return inTransaction(() -> allocationService.findAllAllocataires(start));
  }

  @GetMapping("/allocations")
  public List<Allocation> allocations() {
    return inTransaction(allocationService::findAllocationsActuelles);
  }

  @GetMapping("/allocations/{year}/somme")
  public BigDecimal sommeAs(@PathVariable("year") int year) {
    return inTransaction(() -> versementService.findSommeAllocationParAnnee(year).getValue());
  }

  @GetMapping("/allocations-naissances/{year}/somme")
  public BigDecimal sommeAns(@PathVariable("year") int year) {
    return inTransaction(
        () -> versementService.findSommeAllocationNaissanceParAnnee(year).getValue());
  }

  @GetMapping(value = "/allocataires/{allocataireId}/allocations", produces = MediaType.APPLICATION_PDF_VALUE)
  public byte[] pdfAllocations(@PathVariable("allocataireId") int allocataireId) {
    return inTransaction(() -> versementService.exportPDFAllocataire(allocataireId));
  }

  @GetMapping(value = "/allocataires/{allocataireId}/versements", produces = MediaType.APPLICATION_PDF_VALUE)
  public byte[] pdfVersements(@PathVariable("allocataireId") int allocataireId) {
    return inTransaction(() -> versementService.exportPDFVersements(allocataireId));
  }
}
