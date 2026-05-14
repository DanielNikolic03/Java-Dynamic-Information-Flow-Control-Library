package lattice;

public final class Authority {
    private final Label efficacy;

    public Authority(Label efficacy) {
        this.efficacy = efficacy;
    }

    public Label getEfficacy() {
        return efficacy;
    }

    public Authority attenuate(Label requestedEfficacy) {
        if (!requestedEfficacy.flowsTo(this.efficacy)) {
            throw new IllegalArgumentException("Attenuation failed: Cannot increase authority. " + requestedEfficacy + " is no bounded by current efficacy " + this.efficacy);
        }
        return new Authority(requestedEfficacy);
    }
}
