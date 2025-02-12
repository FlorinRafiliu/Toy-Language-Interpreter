package toyLanguage.model.value;

import toyLanguage.model.type.IType;

public interface IValue {
    IType getType();
    boolean equals(IValue other);
}
