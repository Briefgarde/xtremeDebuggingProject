package ch.hearc.cafheg.business.allocations;

import java.math.BigDecimal;

public class Allocataire {

  private final NoAVS noAVS;
  private final String nom;
  private final String prenom;

  private boolean isActive;

  private boolean isSalaried;

  private BigDecimal salaire;

  public Allocataire(NoAVS noAVS, String nom, String prenom) {
    this.noAVS = noAVS;
    this.nom = nom;
    this.prenom = prenom;
  }

  public String getNom() {
    return nom;
  }

  public String getPrenom() {
    return prenom;
  }

  public NoAVS getNoAVS() {
    return noAVS;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  public boolean isSalaried() {
    return isSalaried;
  }

  public void setSalaried(boolean salaried) {
    isSalaried = salaried;
  }

  public BigDecimal getSalaire() {
    return salaire;
  }

  public void setSalaire(BigDecimal salaire) {
    this.salaire = salaire;
  }
}
