package no.kristiania.dao.model;

public class Questionnarie extends AbstractModel {

    // TODO: possibly remove class

    private final String name;

    public String getName() {
        return name;
    }

    public Questionnarie(String name) {
        this.name = name;
    }
}
