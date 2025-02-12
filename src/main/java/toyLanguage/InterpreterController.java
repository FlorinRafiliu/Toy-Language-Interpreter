package toyLanguage;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Pair;
import toyLanguage.controller.Controller;
import toyLanguage.exceptions.ExpressionException;
import toyLanguage.model.adt.*;
import toyLanguage.model.state.PrgState;
import toyLanguage.model.statements.IStatement;
import toyLanguage.model.value.IValue;
import toyLanguage.model.value.RefValue;
import toyLanguage.model.value.StringValue;
import toyLanguage.repository.IRepository;
import toyLanguage.repository.Repository;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InterpreterController {
    private IStatement statement;
    private IRepository repository;
    private Controller controller;
    private PrgState prgState;
    private List<PrgState> prgList;
    private int currentPrgState;

    @FXML
    private TextField prgStateTextField;
    @FXML
    private Button runOneStepButton;
    @FXML
    private TextField statementTextField;
    @FXML
    private Button closeButton;

    @FXML
    private TableView<Pair<Integer, IValue>> heapTableView;
    @FXML
    private TableColumn<Pair<Integer, IValue>, String> addressColumn;
    @FXML
    private TableColumn<Pair<Integer, IValue>, String> valueHeapColumn;

    @FXML
    private ListView<String> outListView;
    @FXML
    private ListView<StringValue> fileTableListView;
    @FXML
    private ListView<String> prgStatesListView;
    @FXML
    private ListView<IStatement> execStackListView;

    @FXML
    private TableView<Pair<String, IValue>> symTableView;
    @FXML
    private TableColumn<Pair<String, IValue>, String> nameColumn;
    @FXML
    private TableColumn<Pair<String, IValue>, String> valueColumn;

    @FXML
    private TableView<Pair<Integer, Integer>> latchTableView;
    @FXML
    private TableColumn<Pair<Integer, Integer>, Integer> locationLatchColumn;
    @FXML
    private TableColumn<Pair<Integer, Integer>, Integer> valueLatchColumn;

    @FXML
    private TableView<Pair<Integer, Pair<Integer, List<Integer>>>> semaphoreTableView;
    @FXML
    private TableColumn <Pair<Integer, Pair<Integer, List<Integer>>>, String> semIndexColumn;
    @FXML
    private TableColumn <Pair<Integer, Pair<Integer, List<Integer>>>, String> semValueColumn;
    @FXML
    private TableColumn <Pair<Integer, Pair<Integer, List<Integer>>>, String> semValuesColumn;

    public void setStatement(IStatement statement) {
        this.statement = statement;
    }

    @FXML
    public void initialize() {

        nameColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getKey()));
        valueColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getValue().toString()));

        addressColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getKey().toString()));
        valueHeapColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getValue().toString()));

        locationLatchColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getKey()));
        valueLatchColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getValue()));

        semIndexColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getKey().toString()));
        semValueColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getValue().getKey().toString()));
        semValuesColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getValue().getValue().toString()));

        createProgramState();
        populate();
    }

    private void createProgramState() {
        prgState = new PrgState(statement, new MyStack<IStatement>(), new MyDictionary<String, IValue>(),
                new MyList<String>(), new MyDictionary<StringValue, BufferedReader>(), new MyHeap<>(),
                new MyLatchTable<Integer, Integer>(), new MySemaphoreTable<>());
        repository = new Repository("log.txt");
        repository.addPrgState(prgState);
        controller = new Controller(repository);
        prgList = removeCompletedPrg(repository.getPrgList());

        currentPrgState = prgState.getId();
        statementTextField.textProperty().set(statement.toString());
    }

    private void populate() {
        outListView.setItems(FXCollections.observableList(prgState.getOutputList().getAll()));
        execStackListView.setItems(FXCollections.observableList(prgState.getExecStack().getAll().reversed()));
        fileTableListView.setItems(FXCollections.observableList(prgState.getFileTable().getContent().entrySet().stream().map(e -> e.getKey()).toList()));

        symTableView.setItems(FXCollections.observableList(prgState.getSymTable().getContent().entrySet().stream().map(s -> new Pair<String, IValue>(s.getKey(), s.getValue())).toList()));
        symTableView.refresh();

        heapTableView.setItems(FXCollections.observableList(prgState.getHeap().getContent().entrySet().stream().map(s -> new Pair<Integer, IValue>(s.getKey(), s.getValue())).toList()));
        heapTableView.refresh();

        latchTableView.setItems(FXCollections.observableList(prgState.getLatchTable().getContent().entrySet().stream().map(s -> new Pair<>(s.getKey(), s.getValue())).toList()));
        latchTableView.refresh();

        semaphoreTableView.setItems(FXCollections.observableList(prgState.getSemaphoreTable().getContent().entrySet().stream().map(s -> new Pair<Integer, Pair<Integer, List<Integer>>>(s.getKey(), s.getValue())).toList()));
        semaphoreTableView.refresh();

        prgStateTextField.textProperty().set(Integer.toString(prgList.size()));
        prgStatesListView.setItems(FXCollections.observableList(prgList.stream().map(p -> Integer.toString(p.getId())).toList()));
    }

    @FXML
    protected void onMouseClickedHandler() {
        if(prgStatesListView.getSelectionModel().getSelectedItem() == null)
            return;
        prgState = prgList.get(prgStatesListView.getSelectionModel().getSelectedIndex());
        populate();
    }

    @FXML
    protected void runOneStepButtonHandler() {
        try {
            if(prgList.size() > 0) {
                prgList.getFirst().getHeap().setContent(unsafeGarbageCollector(getAddrFromSymTable(prgList),
                        prgList.getFirst().getHeap().getContent(),
                        getRefAddr(prgList, prgList.getFirst().getHeap())));
                controller.oneStepForAllPrg(prgList);
                prgList = removeCompletedPrg(repository.getPrgList());

                populate();
            }
            else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText("Program is executed completely");
                errorAlert.showAndWait();
                controller.shotDownExecutor();
                return;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public List <PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream().filter(p -> p.isNotCompleted()).collect(Collectors.toList());
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

    Map<Integer, IValue> unsafeGarbageCollector(List<Integer> symTableAddr, Map<Integer,IValue> heap, List<Integer> refAddr) {
        return heap.entrySet().stream().
                filter(e -> symTableAddr.contains(e.getKey()) || refAddr.contains(e.getKey())).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    List<Integer> getAddrFromSymTable(List <PrgState> prgStateList) {
        return prgStateList.stream().map(prgState -> prgState.getSymTable().getContent().values()).toList().stream().filter(v-> v instanceof RefValue).
                map(v-> {RefValue v1 = (RefValue) v; return v1.getAddress();})
                .collect(Collectors.toList());
    }

}
