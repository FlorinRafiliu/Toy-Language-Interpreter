package toyLanguage.repository;

import toyLanguage.exceptions.RepoException;
import toyLanguage.model.state.PrgState;

import java.util.List;

public interface IRepository {
    PrgState getCrtPrg();
    void addPrgState(PrgState state);
    void logPrgState(PrgState prg) throws RepoException;
    List <PrgState> getPrgList();
    void setPrgList(List<PrgState> newData);
}
