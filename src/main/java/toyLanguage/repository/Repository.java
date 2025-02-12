package toyLanguage.repository;

import toyLanguage.exceptions.RepoException;
import toyLanguage.model.state.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private List<PrgState> data;
    private String logFilePrg;
    private int currentPrgState;

    public Repository(String logFilePrg) {
        data = new ArrayList<PrgState>();
        this.logFilePrg = logFilePrg;
        currentPrgState = 0;

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter (new FileWriter(this.logFilePrg, false)));
            pw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPrgState(PrgState state) {
        data.add(currentPrgState, state);
    }

    @Override
    public void logPrgState(PrgState prg) throws RepoException {
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter (new FileWriter(logFilePrg, true)));
            pw.print(prg.toString());
            pw.print("\n");
            pw.close();
        } catch (IOException e) {
            throw new RepoException(e.getMessage());
        }
    }

    @Override
    public List<PrgState> getPrgList() {
        return data;
    }

    @Override
    public void setPrgList(List<PrgState> newData) {
        this.data = newData;
    }

    public PrgState getCrtPrg() {
        return data.get(currentPrgState);
    }


}
