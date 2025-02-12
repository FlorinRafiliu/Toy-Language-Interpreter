package toyLanguage.model.value;

import toyLanguage.model.type.IType;
import toyLanguage.model.type.IntType;

public class IntValue implements IValue {
    private int value;
    public IntValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public IType getType() {
        return new IntType();
    }

    @Override
    public boolean equals(IValue other) {
        return other.getType() instanceof  IntType && this.getValue() == ((IntValue)other).getValue();
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
