package toyLanguage.model.value;

import toyLanguage.model.type.IType;
import toyLanguage.model.type.StringType;

public class StringValue implements IValue {
    private String value;

    public StringValue(String value) {
        this.value = value;
    }
    @Override
    public IType getType() {
        return new StringType();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(IValue other) {
        return other.getType() instanceof StringType && this.getValue() == ((StringValue)other).getValue();
    }
    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
