package toyLanguage.model.value;

import toyLanguage.model.type.BoolType;
import toyLanguage.model.type.IType;

public class BoolValue implements IValue {
    private boolean value;

    public BoolValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public IType getType() {
        return new BoolType();
    }

    @Override
    public boolean equals(IValue other) {
        return other.getType() instanceof BoolType && ((BoolValue) other).getValue() == this.getValue();
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
