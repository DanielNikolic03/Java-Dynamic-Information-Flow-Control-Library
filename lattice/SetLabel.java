package lattice;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SetLabel implements Label {
    private final Set<String> owners;

    public SetLabel(Set<String> owners) {
        if (owners == null) {
            this.owners = Collections.emptySet();
        } else {
            this.owners = Collections.unmodifiableSet(new HashSet<>(owners));
        }
    }

    public SetLabel(String... owners) {
        Set<String> tempSet = new HashSet<>();
        for (String owner : owners) {
            tempSet.add(owner);
        }
        this.owners = Collections.unmodifiableSet(tempSet);
    }

    @Override
    public boolean flowsTo(Label other) {
        if (!(other instanceof SetLabel)) {
            return false;
        }
        SetLabel otherLabel = (SetLabel) other;

        return otherLabel.owners.containsAll(this.owners);
    }

    @Override
    public Label join(Label other) {
        if (!(other instanceof SetLabel)) {
            throw new IllegalArgumentException("Cannot join SetLabel with a different label type.");
        }
        SetLabel otherLabel = (SetLabel) other;

        Set<String> combinedOwners = new HashSet<>(this.owners);
        combinedOwners.addAll(otherLabel.owners);

        return new SetLabel(combinedOwners);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SetLabel)) {
            return false;
        }
        SetLabel other = (SetLabel) obj;
        return this.owners.equals(other.owners);
    }

    @Override
    public int hashCode() {
        return owners.hashCode();
    }

    @Override
    public String toString() {
        return owners.toString();
    }

}
