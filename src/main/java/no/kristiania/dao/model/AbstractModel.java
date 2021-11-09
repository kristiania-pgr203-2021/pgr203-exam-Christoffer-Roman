package no.kristiania.dao.model;

public abstract class AbstractModel {

    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
