package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.business.versements.Enfant;

import java.util.List;

public class Famille {
    public Allocataire parent1;
    public Allocataire parent2;
    public List<Enfant> enfantList;

    public boolean parentsTogether;

    public boolean parent1WorkInChildCanton;

    public boolean parent2WorkInChildCanton;

    public Famille(Allocataire parent1, Allocataire parent2, List<Enfant> enfantList) {
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.enfantList = enfantList;
    }

    public Famille(){}

    public Allocataire getParent1() {
        return parent1;
    }

    public void setParent1(Allocataire parent1) {
        this.parent1 = parent1;
    }

    public Allocataire getParent2() {
        return parent2;
    }

    public void setParent2(Allocataire parent2) {
        this.parent2 = parent2;
    }

    public List<Enfant> getEnfantList() {
        return enfantList;
    }

    public void setEnfantList(List<Enfant> enfantList) {
        this.enfantList = enfantList;
    }

    public boolean isParentsTogether() {
        return parentsTogether;
    }

    public void setParentsTogether(boolean parentsTogether) {
        this.parentsTogether = parentsTogether;
    }

    public boolean isParent1WorkInChildCanton() {
        return parent1WorkInChildCanton;
    }

    public void setParent1WorkInChildCanton(boolean parent1WorkInChildCanton) {
        this.parent1WorkInChildCanton = parent1WorkInChildCanton;
    }

    public boolean isParent2WorkInChildCanton() {
        return parent2WorkInChildCanton;
    }

    public void setParent2WorkInChildCanton(boolean parent2WorkInChildCanton) {
        this.parent2WorkInChildCanton = parent2WorkInChildCanton;
    }
}
