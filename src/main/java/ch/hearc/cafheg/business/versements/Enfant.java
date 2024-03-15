package ch.hearc.cafheg.business.versements;

import java.util.Map;

import ch.hearc.cafheg.business.allocations.Canton;
import ch.hearc.cafheg.business.allocations.NoAVS;

public class Enfant {

  private final NoAVS noAVS;
  private final String nom;
  private final String prenom;

  private Canton canton;
  public Enfant(NoAVS noAVS, String nom, String prenom) {
    this.noAVS = noAVS;
    this.nom = nom;
    this.prenom = prenom;
  }

  //json contructor
  public Enfant(Map<String, Object> params){
    // we extract every parameters of the map and set the values of the object
    this.noAVS = params.get("noAVS") != null ? new NoAVS((String) params.get("noAVS")) : null;
    this.nom = (String) params.get("nom");
    this.prenom = (String) params.get("prenom");
    this.canton = params.get("canton") != null ? Canton.fromValue((String) params.get("canton")) : null;
  }

  public Enfant(NoAVS noAVS, String nom, String prenom, Canton canton) {
    this.noAVS = noAVS;
    this.nom = nom;
    this.prenom = prenom;
    this.canton = canton;
  }

  public Canton getCanton() {
    return canton;
  }

  public void setCanton(Canton canton) {
    this.canton = canton;
  }

  public NoAVS getNoAVS() {
    return noAVS;
  }

  public String getNom() {
    return nom;
  }

  public String getPrenom() {
    return prenom;
  }
}
