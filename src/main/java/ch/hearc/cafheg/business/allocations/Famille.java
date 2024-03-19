package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.business.versements.Enfant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class Famille {

    static Logger logger = LoggerFactory.getLogger(Famille.class);
    public Allocataire parent1;
    public Allocataire parent2;
    public Enfant enfant;

    public boolean parentsTogether;

    public boolean parent1WorkInChildCanton;

    public boolean parent2WorkInChildCanton;

    //json constructor
    public Famille(Map<String, Object> params){
        logger.debug("Creating family from {}", params);
        this.parent1 = new Allocataire((Map<String, Object>) params.get("parent1"));

        this.parent2 = new Allocataire((Map<String, Object>) params.get("parent2"));

        this.enfant = new Enfant((Map<String, Object>) params.get("enfant"));
        this.parentsTogether = (boolean) params.getOrDefault("parentsTogether", true);
        this.parent1WorkInChildCanton = (boolean) params.getOrDefault("parent1WorkInChildCanton", true);
        this.parent2WorkInChildCanton = (boolean) params.getOrDefault("parent2WorkInChildCanton", true);
        logger.debug("Created family : {}", this);
    }

    public Famille(Allocataire parent1, Allocataire parent2, Enfant enfant) {
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.enfant = enfant;
    }

    public Famille(Allocataire parent1, Allocataire parent2, Enfant enfant, boolean parentsTogether, boolean parent1WorkInChildCanton, boolean parent2WorkInChildCanton) {
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.enfant = enfant;
        this.parentsTogether = parentsTogether;
        this.parent1WorkInChildCanton = parent1WorkInChildCanton;
        this.parent2WorkInChildCanton = parent2WorkInChildCanton;
    }



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

    public Enfant getEnfant() {
        return enfant;
    }

    public void setEnfant(Enfant enfant) {
        this.enfant = enfant;
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
