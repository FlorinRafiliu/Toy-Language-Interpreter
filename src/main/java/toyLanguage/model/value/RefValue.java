package toyLanguage.model.value;

import toyLanguage.model.type.IType;
import toyLanguage.model.type.RefType;

public class RefValue implements IValue {
    private int address;
    private IType locationType;

    public RefValue(int address, IType locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddress() {
        return this.address;
    }

    @Override
    public IType getType() {
        return new RefType(locationType);
    }

    @Override
    public String toString() {
        return "(" + address + ", " + locationType.toString() + ")";
    }

    @Override
    public boolean equals(IValue other) {
        return other.getType().equals(new RefType(locationType)) && ((RefValue) other).getAddress() == this.address;
    }
}
