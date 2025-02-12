package toyLanguage.controller;

import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.RepoException;
import toyLanguage.exceptions.StackException;
import toyLanguage.exceptions.StatementException;
import toyLanguage.model.adt.MyIDictionary;
import toyLanguage.model.adt.MyIHeap;
import toyLanguage.model.state.PrgState;
import toyLanguage.model.value.IValue;
import toyLanguage.model.value.RefValue;
import toyLanguage.repository.IRepository;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    private IRepository repository;
    private ExecutorService executor;

    public Controller(IRepository repository) {
        this.repository = repository;
        executor = Executors.newFixedThreadPool(2);
    }
    public void shotDownExecutor() {
        executor.shutdownNow();
    }

    public void addProgram(PrgState state) {
        repository.addPrgState(state);
    }

    public List <PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream().filter(p -> p.isNotCompleted()).collect(Collectors.toList());
    }

    public void oneStepForAllPrg(List<PrgState> prgList) throws RepoException, InterruptedException{
        prgList.forEach(prg -> {
            try {
                repository.logPrgState(prg);
            } catch (RepoException e) {
                throw new RuntimeException(e);
            }
        });
        List<Callable<PrgState>> callList = prgList.stream().
                map((PrgState p) -> (Callable<PrgState>)(() -> {return p.oneStep();})).collect(Collectors.toList());

        List<PrgState> newPrgList = executor.invokeAll(callList).stream().map(future -> {
            try {
                return future.get();
            } catch(Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }).filter(p -> p != null).collect(Collectors.toList());
        prgList.addAll(newPrgList);
        prgList.forEach(prg -> {
            try {
                repository.logPrgState(prg);
            } catch (RepoException e) {
                throw new RuntimeException(e);
            }
        });
        repository.setPrgList(prgList);
    }

    public void allStep() throws InterruptedException, RepoException, StatementException, ExpressionException{
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletedPrg(repository.getPrgList());
        while(prgList.size() > 0) {
            prgList.getFirst().getHeap().setContent(unsafeGarbageCollector(getAddrFromSymTable(prgList),
                    prgList.getFirst().getHeap().getContent(),
                    getRefAddr(prgList, prgList.getFirst().getHeap())));
            oneStepForAllPrg(prgList);
            prgList = removeCompletedPrg(repository.getPrgList());
        }
        executor.shutdownNow();
        repository.setPrgList(prgList);
    }

    /*
    public void allStep() throws StackException, StatementException, ExpressionException, RepoException {
        PrgState prg = repository.getCrtPrg();
        repository.logPrgState(prg);
        displayState();
        while(!prg.getExecStack().isEmpty()) {
            prg.oneStep();
            repository.logPrgState(prg);
            prg.getHeap().setContent(unsafeGarbageCollector(getAddrFromSymTable(prg.getSymTable().getContent().values()),
                                                                                prg.getHeap().getContent(),
                                                                                getRefAddr(prg.getSymTable(), prg.getHeap())));
            repository.logPrgState(prg);
            displayState();
        }
    }
    */

    public void displayState() throws StackException, StatementException, ExpressionException {
        PrgState prg = repository.getCrtPrg();
        System.out.println(prg.toString());
    }

    List <Integer> getRefAddr(List <PrgState> prgStateList, MyIHeap<Integer, IValue> heap) throws ExpressionException {
        List<Integer> ans = new ArrayList<>();

        for (PrgState prgState : prgStateList) {
            MyIDictionary <String, IValue> symTable = prgState.getSymTable();
            for (IValue v : symTable.getContent().values()) {
                if (v instanceof RefValue) {
                    int addr = ((RefValue) v).getAddress();
                    ans.add(addr);
                    while (addr != 0 && heap.get(addr) instanceof RefValue) {
                        int addr1 = ((RefValue) heap.get(addr)).getAddress();
                        ans.add(addr1);
                        addr = addr1;
                    }
                    if (addr != 0)
                        ans.add(addr);
                }
            }
        }

        return ans;
    }

    Map <Integer, IValue> unsafeGarbageCollector(List<Integer> symTableAddr, Map<Integer,IValue> heap, List<Integer> refAddr) {
        return heap.entrySet().stream().
                filter(e -> symTableAddr.contains(e.getKey()) || refAddr.contains(e.getKey())).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    List<Integer> getAddrFromSymTable(List <PrgState> prgStateList) {
        return prgStateList.stream().map(prgState -> prgState.getSymTable().getContent().values()).toList().stream().filter(v-> v instanceof RefValue).
                map(v-> {RefValue v1 = (RefValue) v; return v1.getAddress();})
                .collect(Collectors.toList());
    }

    List<Integer> getAddrFromSymTable(Collection <IValue> symTableValues) {
        return symTableValues.stream().filter(v-> v instanceof RefValue).
                map(v-> {RefValue v1 = (RefValue) v; return v1.getAddress();})
                .collect(Collectors.toList());
    }


}
