package no.kristiania.model;

public class Questionnarie extends AbstractModel {

    private final String name;

    public String getName() {
        return name;
    }

    public Questionnarie(String name) {
        this.name = name;
    }
}
