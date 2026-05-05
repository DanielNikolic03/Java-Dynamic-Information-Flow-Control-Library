package lattice;

public enum LevelLabel implements Label {
    LOW, HIGH;

    @Override
    public boolean flowsTo(Label other) {
        if (this == HIGH) {
            return other == HIGH;
        }
        // Low can always flow from LOW and HIGH
        return true;
    }

    @Override
    public Label join(Label other) {
        if (this == HIGH || other == HIGH) {
            return HIGH;
        }
        return LOW;
    }
}