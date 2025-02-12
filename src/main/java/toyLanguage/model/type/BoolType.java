package toyLanguage.model.type;

import toyLanguage.model.value.BoolValue;
import toyLanguage.model.value.IValue;

public class BoolType implements IType {

    @Override
    public boolean equals(IType t) {
        return t instanceof BoolType;
    }

    @Override
    public IValue getDefaultValue() {
        return new BoolValue(false);
    }
    @Override
    public String toString() {
        return "bool";
    }
}
