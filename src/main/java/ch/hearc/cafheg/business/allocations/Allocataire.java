package ch.hearc.cafheg.business.allocations;

import java.math.BigDecimal;

public class Allocataire {

  private final NoAVS noAVS;
  private final String nom;
  private final String prenom;

  private boolean isActive;

  private boolean isSalaried;

  private boolean parentalAuthority;

  private BigDecimal salaire;

  private Canton lifeCanton;

  public Allocataire(NoAVS noAVS, String nom, String prenom) {
    this.noAVS = noAVS;
    this.nom = nom;
    this.prenom = prenom;
  }

  public Allocataire(NoAVS noAVS, String nom, String prenom, boolean isActive, boolean isSalaried, boolean parentalAuthority, BigDecimal salaire, Canton canton) {
    this.noAVS = noAVS;
    this.nom = nom;
    this.prenom = prenom;
    this.isActive = isActive;
    this.isSalaried = isSalaried;
    this.parentalAuthority = parentalAuthority;
    this.salaire = salaire;
    this.lifeCanton = canton;
  }

  public boolean getParentalAuthority() {
    return parentalAuthority;
  }

  public void setParentalAuthority(boolean parentalAuthority) {
    this.parentalAuthority = parentalAuthority;
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

  public Canton getLifeCanton() {
    return lifeCanton;
  }

  public void setLifeCanton(Canton lifeCanton) {
    this.lifeCanton = lifeCanton;
  }
}
