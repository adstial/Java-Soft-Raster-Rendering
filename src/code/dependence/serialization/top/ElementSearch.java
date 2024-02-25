package code.dependence.serialization.top;

public interface ElementSearch<E, C> {
    E getElement(C c);
    E[] getElements(C c);
}
