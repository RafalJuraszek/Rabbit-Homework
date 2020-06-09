package zad.middleware;

@FunctionalInterface
public interface Handler<T> {

    void handle(T msg);
}
