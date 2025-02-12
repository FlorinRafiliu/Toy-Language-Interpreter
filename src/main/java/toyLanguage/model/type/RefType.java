package toyLanguage.model.type;

import toyLanguage.model.value.IValue;
import toyLanguage.model.value.RefValue;

public class RefType implements IType {
    private IType inner;

    public RefType(IType inner) {
        this.inner = inner;
    }

    public IType getInner() {
        return this.inner;
    }

    @Override
    public boolean equals(IType another) {
        if(another instanceof RefType)
            return inner.equals(((RefType) another).getInner());
        else
            return false;
    }

    @Override
    public String toString() {
        return "Ref " + inner.toString();
    }

    @Override
    public IValue getDefaultValue() {
        return new RefValue(0, inner);
    }
}
