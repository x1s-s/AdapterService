package by.x1ss.adapterservice.model.request;

public enum PersonType {
    PHYSICAL,
    JURIDICAL;

    public String getType() {
        return this.name().toLowerCase();
    }
}
