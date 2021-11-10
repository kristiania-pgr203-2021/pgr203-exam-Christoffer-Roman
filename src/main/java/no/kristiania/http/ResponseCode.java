package no.kristiania.http;

public enum ResponseCode {
    OK("200 OK"), ERROR("500 Internal Server Error"), SEE_OTHER("303 See Other"), NOT_FOUND("404 Not Found");

    private final String stringRepresentation;

    ResponseCode(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }
}
