package lattice;

public class Labeled<T> {
    private final T value;
    private final Label label;

    public Labeled(T value, Label label) {
        this.value = value;
        this.label = label;
    }

    public T getValue() {
        return value;
    }

    public Label getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "Labeled{" + "value=" + value + ", label="+label + "}";
    }
}
