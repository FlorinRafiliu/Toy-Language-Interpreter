package toyLanguage.model.type;

import toyLanguage.model.value.IValue;
import toyLanguage.model.value.StringValue;

public class StringType implements IType {
    @Override
    public boolean equals(IType type) {
        return type instanceof StringType;
    }

    @Override
    public IValue getDefaultValue() {
        return new StringValue("");
    }
    @Override
    public String toString() {
        return "string";
    }

}
