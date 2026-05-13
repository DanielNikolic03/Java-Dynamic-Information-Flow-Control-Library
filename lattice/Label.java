package lattice;

public interface Label {
    // Check if data flow from this label to oher label is allowed
    boolean flowsTo(Label other);

    //Combine this label with another label.
    Label join(Label other);
    
    //Generate greatest lower bound, used for network attunuation of data and authority.
    Label meet(Label other);
}