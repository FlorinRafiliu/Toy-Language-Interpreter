package toyLanguage.model.type;

import toyLanguage.model.value.IValue;
import toyLanguage.model.value.IntValue;

public class IntType implements IType {

    @Override
    public boolean equals(IType t) {
        return t instanceof IntType;
    }
    @Override
    public String toString() {
        return "int";
    }

    @Override
    public IValue getDefaultValue() {
        return new IntValue(0);
    }
}
