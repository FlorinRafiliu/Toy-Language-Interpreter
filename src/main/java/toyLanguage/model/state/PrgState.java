package toyLanguage.model.state;

import javafx.util.Pair;
import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.StackException;
import toyLanguage.exceptions.StatementException;
import toyLanguage.model.adt.*;
import toyLanguage.model.statements.IStatement;
import toyLanguage.model.value.IValue;
import toyLanguage.model.value.StringValue;

import java.io.BufferedReader;
import java.util.List;

public class PrgState {
    private MyIStack<IStatement> execStack;
    private MyIDictionary<String,IValue> symTable;
    private MyIList<String> outputList;
    private IStatement originalStatement;
    private MyIDictionary<StringValue, BufferedReader> fileTable;
    private MyIHeap<Integer, IValue> heap;
    private MyILatchTable<Integer, Integer> latchTable;
    private MyISemaphoreTable<Integer, Pair<Integer, List<Integer>>> semaphoreTable;
    private static int id = 0;
    private int myId;


    public PrgState(IStatement initState, MyIStack<IStatement> execStack, MyIDictionary<String,IValue> symTable,
                    MyIList<String> outputList, MyIDictionary<StringValue, BufferedReader> fileTable,
                    MyIHeap<Integer, IValue> heap, MyILatchTable<Integer, Integer> latchTable, MyISemaphoreTable <Integer, Pair<Integer, List<Integer>>> semaphoreTable) {
        this.execStack = execStack;
        this.symTable = symTable;
        this.outputList = outputList;
        this.fileTable = fileTable;
        this.heap = heap;
        this.latchTable = latchTable;
        this.semaphoreTable = semaphoreTable;

        this.myId = requestId();
        this.originalStatement = initState.deepCopy();
        execStack.push(initState);
    }

    private synchronized int requestId() {
        this.id = this.id + 1;
        return id;
    }

    public int getId() {
        return  myId;
    }

    public MyIStack<IStatement> getExecStack() {
        return execStack;
    }

    public MyIDictionary<String,IValue> getSymTable() {
        return symTable;
    }

    public MyIList<String> getOutputList() {
        return outputList;
    }

    public MyIDictionary<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public MyIHeap<Integer, IValue> getHeap() {
        return heap;
    }

    public MyILatchTable<Integer, Integer> getLatchTable() { return latchTable; }

    public MyISemaphoreTable<Integer, Pair <Integer, List <Integer>>> getSemaphoreTable() { return semaphoreTable; }

    public Boolean isNotCompleted() {
        return !execStack.isEmpty();
    }

    public PrgState oneStep() throws StackException, StatementException, ExpressionException{
        if(execStack.isEmpty())
            throw new StackException("prgState is empty!");
        IStatement crtStatement = execStack.pop();
        return crtStatement.execute(this);
    }

    public String toStringFile() {
        String res = "File Table:\n";
        for(StringValue s : fileTable.keys()) {
            res += s.getValue() + "\n";
        }
        return res;
    }

    @Override
    public String toString() {
        return myId + "\n" + execStack.toString() + symTable.toString() + outputList.toString()  + toStringFile() + heap.toString() + latchTable.toString();
    }
}
