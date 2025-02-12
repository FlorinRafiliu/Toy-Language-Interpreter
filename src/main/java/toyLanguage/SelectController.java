package toyLanguage;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import toyLanguage.exceptions.ExpressionException;
import toyLanguage.exceptions.TypeException;
import toyLanguage.model.adt.MyDictionary;
import toyLanguage.model.expressions.*;
import toyLanguage.model.statements.*;
import toyLanguage.model.type.BoolType;
import toyLanguage.model.type.IntType;
import toyLanguage.model.type.RefType;
import toyLanguage.model.type.StringType;
import toyLanguage.model.value.BoolValue;
import toyLanguage.model.value.IntValue;
import toyLanguage.model.value.StringValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectController {

    @FXML
    private ListView <IStatement> statementListView;
    @FXML
    private Button runButton;

    private List<IStatement> list;

    private void createStatements() {
        list = new ArrayList<>();

        //int v; v=2; Print(v);
        IStatement ex1 = new CompStatement(new VarDeclStatement("v", new IntType()),
                new CompStatement(new AssignStatement("v",new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))));
        list.add(ex1);

        //----------------------------------------------------------------------------------------------------------

        //int a;int b; a=2+3*5;b=a+1;Print(b) is represented as:
        IStatement ex2  = new CompStatement( new VarDeclStatement("a",new IntType()),
                new CompStatement(new VarDeclStatement("b",new IntType()),
                        new CompStatement(new AssignStatement("a", new ArithmeticalExpression(new ValueExpression(new IntValue(2)), ArithmeticalOperation.PLUS,new
                                ArithmeticalExpression(new ValueExpression(new IntValue(3)), ArithmeticalOperation.MULTIPLY, new ValueExpression(new IntValue(5))))),
                                new CompStatement(new AssignStatement("b",new ArithmeticalExpression(new VariableExpression("a"), ArithmeticalOperation.PLUS, new ValueExpression(new
                                        IntValue(1)))), new PrintStatement(new VariableExpression("b"))))));
        list.add(ex2);


        //----------------------------------------------------------------------------------------------------------

        //bool a; int v; a=true;(If a Then v=2 Else v=3);Print(v) is represented as
        IStatement ex3 = new CompStatement(new VarDeclStatement("a",new BoolType()),
                new CompStatement(new VarDeclStatement("v", new IntType()),
                        new CompStatement(new AssignStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompStatement(new IfStatement(new VariableExpression("a"),new AssignStatement("v",new ValueExpression(new
                                        IntValue(2))), new AssignStatement("v", new ValueExpression(new IntValue(3)))), new PrintStatement(new
                                        VariableExpression("v"))))));
        list.add(ex3);

        //----------------------------------------------------------------------------------------------------------

        //string varf;varf="test.in";openRFile(varf);int varc;readFile(varf,varc);print(varc);readFile(varf,varc);print(varc);closeRFile(varf)
        IStatement ex4 = new CompStatement(
                new VarDeclStatement("varf", new StringType()), new CompStatement(
                new AssignStatement("varf", new ValueExpression(new StringValue("test.in"))), new CompStatement(
                new OpenReadFileStatement(new VariableExpression("varf")), new CompStatement(
                new VarDeclStatement("varc", new IntType()), new CompStatement(
                new ReadFileStatement(new VariableExpression("varf"), "varc"), new CompStatement(
                new PrintStatement(new VariableExpression("varc")), new CompStatement(
                new ReadFileStatement(new VariableExpression("varf"), "varc"), new CompStatement(
                new PrintStatement(new VariableExpression("varc")), new CloseReadFileStatement(new VariableExpression("varf"))))))))));
        list.add(ex4);

        //----------------------------------------------------------------------------------------------------------

        //Ref int v;new(v,20);Ref Ref int a; new(a,v);print(v);print(a)
        IStatement ex5 = new CompStatement(
                new VarDeclStatement("v", new RefType(new IntType())), new CompStatement(
                new HeapAllocStatement("v", new ValueExpression(new IntValue(20))), new CompStatement(
                new VarDeclStatement("a", new RefType(new RefType(new IntType()))), new CompStatement(
                new HeapAllocStatement("a", new VariableExpression("v")), new CompStatement(
                new PrintStatement(new VariableExpression("v")),
                new PrintStatement(new VariableExpression("a")))))));
        list.add(ex5);

        //----------------------------------------------------------------------------------------------------------

        //Ref int v;new(v,20);Ref Ref int a; new(a,v);print(rH(v));print(rH(rH(a))+5)
        IStatement ex6 = new CompStatement(
                new VarDeclStatement("v", new RefType(new IntType())), new CompStatement(
                new HeapAllocStatement("v", new ValueExpression(new IntValue(20))), new CompStatement(
                new VarDeclStatement("a", new RefType(new RefType(new IntType()))), new CompStatement(
                new HeapAllocStatement("a", new VariableExpression("v")), new CompStatement(
                new PrintStatement(new HeapReadingExpression(new VariableExpression("v"))),
                new PrintStatement(new ArithmeticalExpression(new HeapReadingExpression(new HeapReadingExpression(new VariableExpression("a"))),
                        ArithmeticalOperation.PLUS,
                        new ValueExpression(new IntValue(5)))))))));
        list.add(ex6);

        //----------------------------------------------------------------------------------------------------------

        //Ref int v;new(v, 20);print(rH(v));wH(v, 30);print(rH(v) + 5)
        IStatement ex7 = new CompStatement(
                new VarDeclStatement("v", new RefType(new IntType())), new CompStatement(
                new HeapAllocStatement("v", new ValueExpression(new IntValue(20))), new CompStatement(
                new PrintStatement(new HeapReadingExpression(new VariableExpression("v"))), new CompStatement(
                new HeapWritingStatement("v", new ValueExpression(new IntValue(30))),
                new PrintStatement(new ArithmeticalExpression(new HeapReadingExpression(new VariableExpression("v")),
                        ArithmeticalOperation.PLUS,
                        new ValueExpression(new IntValue(5))))))));
        list.add(ex7);

        //----------------------------------------------------------------------------------------------------------

        //Ref int v;new(v, 20);Ref Ref int a;new(a, v);new(v, 30);print(rH(rH(a)))
        IStatement ex8 = new CompStatement(
                new VarDeclStatement("v", new RefType(new IntType())), new CompStatement(
                new HeapAllocStatement("v", new ValueExpression(new IntValue(20))), new CompStatement(
                new VarDeclStatement("a", new RefType(new RefType(new IntType()))), new CompStatement(
                new HeapAllocStatement("a", new VariableExpression("v")), new CompStatement(
                new HeapAllocStatement("v", new ValueExpression(new IntValue(30))),
                new PrintStatement(new HeapReadingExpression(new HeapReadingExpression(new VariableExpression("a")))))))));
        list.add(ex8);

        //----------------------------------------------------------------------------------------------------------

        //int v;v=4;while(v > 0) {print(v);v=v - 1};print(v)
        IStatement ex9 = new CompStatement(
                new VarDeclStatement("v", new IntType()), new CompStatement(
                new AssignStatement("v", new ValueExpression(new IntValue(4))), new CompStatement(
                new WhileStatement(
                        new RelationalExpression(new VariableExpression("v"), RelationalOperation.GREATER_THAN, new ValueExpression(new IntValue(0))),
                        new CompStatement(new PrintStatement(new VariableExpression("v")), new AssignStatement("v", new ArithmeticalExpression(
                                new VariableExpression("v"), ArithmeticalOperation.MINUS, new ValueExpression(new IntValue(1)))))),
                new PrintStatement(new VariableExpression("v")))));
        list.add(ex9);

        //----------------------------------------------------------------------------------------------------------

        //int v; Ref int a; v=10;new(a,22);fork(wH(a,30);v=32;print(v);print(rH(a)));print(v);print(rH(a))
        IStatement ex10 = new CompStatement(new VarDeclStatement("v", new IntType()), new CompStatement(
                new VarDeclStatement("a", new RefType(new IntType())), new CompStatement(
                new AssignStatement("v", new ValueExpression(new IntValue(10))), new CompStatement(
                new HeapAllocStatement("a", new ValueExpression(new IntValue(22))), new CompStatement(
                new ForkStatement( new CompStatement(
                        new HeapWritingStatement("a", new ValueExpression(new IntValue(30))), new CompStatement(
                        new AssignStatement("v", new ValueExpression(new IntValue(32))), new CompStatement(
                        new PrintStatement(new VariableExpression("v")),
                        new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))))))
                ), new CompStatement(
                new PrintStatement(new VariableExpression("v")),
                new PrintStatement(new HeapReadingExpression(new VariableExpression("a")))))))));
        list.add(ex10);

        //----------------------------------------------------------------------------------------------------------

        //Ref int v1; Ref int v2; Ref int v3; int cnt;
        //new(v1,2);new(v2,3);new(v3,4);newLatch(cnt,rH(v2));
        //fork(wh(v1,rh(v1)*10));print(rh(v1));countDown(cnt);
        //fork(wh(v2,rh(v2)*10));print(rh(v2));countDown(cnt);
        //fork(wh(v3,rh(v3)*10));print(rh(v3));countDown(cnt)));
        //await(cnt);
        //print(100);
        //countDown(cnt);
        //print(100)

        IStatement fork3 = new CompStatement(
                new HeapWritingStatement("v3",
                        new ArithmeticalExpression(new HeapReadingExpression(new VariableExpression("v3")), ArithmeticalOperation.MULTIPLY, new ValueExpression(new IntValue(10)))), new CompStatement(
                new PrintStatement(new HeapReadingExpression(new VariableExpression("v3"))),
                new CountDownStatement("cnt")));

        IStatement fork2 = new CompStatement(
                new HeapWritingStatement("v2",
                        new ArithmeticalExpression(new HeapReadingExpression(new VariableExpression("v2")), ArithmeticalOperation.MULTIPLY, new ValueExpression(new IntValue(10)))), new CompStatement(
                new PrintStatement(new HeapReadingExpression(new VariableExpression("v2"))), new CompStatement(
                new CountDownStatement("cnt"), new ForkStatement(fork3))));

        IStatement fork1 = new CompStatement(
                new HeapWritingStatement("v1",
                        new ArithmeticalExpression(new HeapReadingExpression(new VariableExpression("v1")), ArithmeticalOperation.MULTIPLY, new ValueExpression(new IntValue(10)))), new CompStatement(
                new PrintStatement(new HeapReadingExpression(new VariableExpression("v1"))), new CompStatement(
                new CountDownStatement("cnt"), new ForkStatement(fork2))));

        IStatement ex11 = new CompStatement(
                new VarDeclStatement("v1", new RefType(new IntType())), new CompStatement(
                new VarDeclStatement("v2", new RefType(new IntType())), new CompStatement(
                new VarDeclStatement("v3", new RefType(new IntType())), new CompStatement(
                new VarDeclStatement("cnt", new IntType()), new CompStatement(
                new HeapAllocStatement("v1", new ValueExpression(new IntValue(2))), new CompStatement(
                new HeapAllocStatement("v2", new ValueExpression(new IntValue(3))), new CompStatement(
                new HeapAllocStatement("v3", new ValueExpression(new IntValue(4))), new CompStatement(
                new NewLatchStatement("cnt", new HeapReadingExpression(new VariableExpression("v2"))), new CompStatement(
                new ForkStatement(fork1), new CompStatement(
                new AwaitStatement("cnt"), new CompStatement(
                new PrintStatement(new ValueExpression(new IntValue(100))), new CompStatement(
                new CountDownStatement("cnt"),
                new PrintStatement(new ValueExpression(new IntValue(100)))))))))))))));

        list.add(ex11);

        //----------------------------------------------------------------------------------------------------------
        // Ref int v1; int cnt; new(v1,1);createSemaphore(cnt,rH(v1));
        // fork(acquire(cnt);wh(v1,rh(v1)*10));print(rh(v1));release(cnt));
        // fork(acquire(cnt);wh(v1,rh(v1)*10));wh(v1,rh(v1)*2));print(rh(v1));release(cnt));
        // acquire(cnt);print(rh(v1)-1); release(cnt)

        IStatement fork11 = new CompStatement(
                new AcquireStatement("cnt"), new CompStatement(
                new HeapWritingStatement("v1", new ArithmeticalExpression(new HeapReadingExpression(new VariableExpression("v1")), ArithmeticalOperation.MULTIPLY, new ValueExpression(new IntValue(10)))), new CompStatement(
                new PrintStatement(new HeapReadingExpression(new VariableExpression("v1"))), new ReleaseStatement("cnt"))));
        IStatement fork22 = new CompStatement(
                new AcquireStatement("cnt"), new CompStatement(
                new HeapWritingStatement("v1", new ArithmeticalExpression(new HeapReadingExpression(new VariableExpression("v1")), ArithmeticalOperation.MULTIPLY, new ValueExpression(new IntValue(10)))), new CompStatement(
                new HeapWritingStatement("v1", new ArithmeticalExpression(new HeapReadingExpression(new VariableExpression("v1")), ArithmeticalOperation.MULTIPLY, new ValueExpression(new IntValue(2)))), new CompStatement(
                new PrintStatement(new HeapReadingExpression(new VariableExpression("v1"))), new ReleaseStatement("cnt")))));

        IStatement ex12 = new CompStatement(
                new VarDeclStatement("v1", new RefType(new IntType())), new CompStatement(
                new VarDeclStatement("cnt", new IntType()), new CompStatement(
                new HeapAllocStatement("v1", new ValueExpression(new IntValue(2))), new CompStatement(
                new CreateSemaphore("cnt", new HeapReadingExpression(new VariableExpression("v1"))), new CompStatement(
                new ForkStatement(fork11), new CompStatement(
                new ForkStatement(fork22), new CompStatement(
                new AcquireStatement("cnt"), new CompStatement(
                new PrintStatement(new ArithmeticalExpression(new HeapReadingExpression(new VariableExpression("v1")), ArithmeticalOperation.MINUS, new ValueExpression(new IntValue(1)))),
                new ReleaseStatement("cnt")))))))));

        list.add(ex12);

    }

    private void loadStatements() {
        statementListView.setItems(FXCollections.observableList(list));
    }

    @FXML
    public void initialize() {
        createStatements();
        loadStatements();
    }

    @FXML
    protected void onRunButtonClick() throws IOException {
        IStatement statement = statementListView.getSelectionModel().getSelectedItem();
        if(statement == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Select a statement!");
            errorAlert.showAndWait();
            return;
        }

        try {
            statement.typecheck(new MyDictionary<>());
        } catch (TypeException | ExpressionException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
            return;
        }

        InterpreterApplication interpreterApplication = new InterpreterApplication(statement);
        interpreterApplication.start(new Stage());
    }
}