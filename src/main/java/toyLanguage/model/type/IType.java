package toyLanguage.model.type;

import toyLanguage.model.value.IValue;

public interface IType {
    boolean equals(IType type);
    IValue getDefaultValue();
}
