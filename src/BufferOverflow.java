import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Text;

/**
 * Author: Alexander Pinkerton, Nico Gonzalez, Dylan Parris
 * Group: 9
 * BUFFER OVERFLOW SIMULATION
 */

//ajksdnaksdbkjasbdkjb
public class BufferOverflow extends Application {

    Address[] addArray;
    GraphicsContext gc;
    int addressCount = 6;
    int width = 500;
    int height = 600;
    int stepCount = 1;
    /* Stage 0 = Discovery; Stage 1 = Offset; Stage 2 = Exploit*/
    int stage = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        addArray = new Address[addressCount];
        primaryStage.setTitle("Stack Overflow Demo");
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        for (int i = 0; i < addressCount; i++) {
            addArray[i] = new Address(
                    0,
                    i * this.getHeight() / addressCount,
                    this.getWidth(),
                    this.getHeight() / addressCount
            );
            addArray[i].setC(Color.BLUE);
            addArray[i].setText("Local Stack Variable Space");
        }


        addArray[0].setText("ESP - Extended Stack Pointer");
        addArray[addressCount - 5].setText("char[10] phoneNumber");
        addArray[addressCount - 4].setText("bool loggedIn");
        addArray[addressCount - 3].setText("int counter");
        addArray[addressCount - 2].setText("ESB - Extended Base Pointer");
        addArray[addressCount - 1].setText("RETURN ADDRESS");
        drawShapes(gc);
        root.getChildren().add(canvas);
        Scene derp = new Scene(root);
        derp.setOnKeyReleased(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                switch (event.getCode()) {
                    case SPACE:
                        stepCount++;
                        step(stepCount);
                        break;
                }
            }
        });
        primaryStage.setScene(derp);
        primaryStage.show();
    }

    private void setStepCount(int i){
        stepCount = i;
    }

    private void reset(){
        clear(gc);
        addArray[0].setText("ESP - Extended Stack Pointer");
        addArray[addressCount - 5].setText("char[10] phoneNumber");
        addArray[addressCount - 4].setText("bool loggedIn");
        addArray[addressCount - 3].setText("int counter");
        addArray[addressCount - 2].setText("ESB - Extended Base Pointer");
        addArray[addressCount - 1].setText("RETURN ADDRESS");
        for( int i = 0; i < addArray.length;i++ ) {
            addArray[i].setC(Color.BLUE);
        }
        drawShapes(gc);
        setStepCount(1);
    }

    private void step(int stepCount) {
        if(stage == 0){
            if (stepCount == 2) {
                clear(gc);
                addArray[1].setText("(555) 555-");
                addArray[2].setText("5555");
                drawShapes(gc);
            } else if (stepCount == 3) {
                clear(gc);
                for (int i = 3; i < addArray.length; i++) {
                    addArray[i].setText("Segmentation Fault");
                    addArray[i].setC(Color.RED);
                }
                drawShapes(gc);
            }else {
                stage++;
                reset();
            }
        }else if (stage == 1){
            overflow();
            stage++;

        }else if (stage == 2) {
            exploit();
        }
    }


    private void overflow() {
        clear(gc);
        String overflowArray[] = {"AAAABBBBCC","CCDD","DDEE","EEFF","FFGG"};

        for (int i = 0; i < overflowArray.length; i++) {
            addArray[i + 1].setC(Color.RED);
            addArray[i + 1].setText(overflowArray[i]);
        }
        addArray[addressCount - 1].setText("RETURN ADDRESS");
        drawShapes(gc);
    }
    //opens the new windows for the exploit
    private void newWindow(Stage primaryStage){
        TableView<Customer> table = new TableView<Customer>();
        ObservableList<Customer> customers = getCustomers();
        table.setItems(customers);

        TableColumn<Customer,String> firstNameCol = new TableColumn<Customer,String>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
        TableColumn<Customer,String> lastNameCol = new TableColumn<Customer,String>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory("lastName"));
        TableColumn<Customer,String> numberCol = new TableColumn<Customer,String>("Phone Number");
        numberCol.setCellValueFactory(new PropertyValueFactory("number"));

        table.getColumns().setAll(firstNameCol, lastNameCol,numberCol);

        primaryStage.setTitle("Customer Information");
        Canvas canvas = new Canvas(0, 10);
        gc = canvas.getGraphicsContext2D();
        Scene derp = new Scene(table);
        primaryStage.setScene(derp);
        primaryStage.show();
    }

    //overflow points to the exploit() function.
//opens a new window showing the execution of code under exploit.
    private void exploit(){

        clear(gc);
        addArray[0].setText("ESP - Extended Stack Pointer");
        addArray[addressCount - 5].setText("/x90/x90/x90/x90/x90/x90/x90/x90/x90/x90");
        addArray[addressCount - 4].setText("/x90/x90/x90/x90");
        addArray[addressCount - 3].setText("/x90/x90/x90/x90");
        addArray[addressCount - 2].setText("/x90/x90/x90/x90");
        addArray[addressCount - 1].setText("get_all_customers()");
        for( int i = 0; i < addArray.length;i++ ) {
            addArray[i].setC(Color.BLUE);
        }
        //addArray[addressCount - 1].setText("RETURN ADDRESS");
        drawShapes(gc);
        Stage stage = new Stage();
        newWindow(stage);
    }



    private void clear(GraphicsContext gc) {
        gc.clearRect(0, 0, width, height);
    }

    private void drawShapes(GraphicsContext gc) {
        for (int i = 0; i < addressCount; i++) {
            addArray[i].draw(gc);
        }
    }

    public ObservableList<Customer> getCustomers() {
        ObservableList<Customer> c= FXCollections.observableArrayList();
        c.add(new Customer("Mike","Brown","330-222-1111"));
        c.add(new Customer("John","Sanches","330-222-1111"));
        c.add(new Customer("Mike","Phillips","330-222-1111"));
        c.add(new Customer("Ryan","Smith","330-222-1111"));
        c.add(new Customer("Jorge","Woods","330-222-1111"));
        c.add(new Customer("Adam","Young","330-222-1111"));
        c.add(new Customer("Alex","Frank","330-222-1111"));
        c.add(new Customer("Sam","Lopez","330-222-1111"));
        return c;
    }

    public class Customer {
        public Customer(String name,String last,String number){
            setFirstName(name);
            setLastName(last);
            setNumber(number);
        }
        private StringProperty number;
        public void setNumber(String value) { numberProperty().set(value); }
        public String getNumber() { return numberProperty().get(); }
        public StringProperty numberProperty() {
            if (number == null) number = new SimpleStringProperty(this, "###-###-####");
            return number;
        }

        private StringProperty firstName;
        public void setFirstName(String value) { firstNameProperty().set(value); }
        public String getFirstName() { return firstNameProperty().get(); }
        public StringProperty firstNameProperty() {
            if (firstName == null) firstName = new SimpleStringProperty(this, "firstName");
            return firstName;
        }

        private StringProperty lastName;
        public void setLastName(String value) { lastNameProperty().set(value); }
        public String getLastName() { return lastNameProperty().get(); }
        public StringProperty lastNameProperty() {
            if (lastName == null) lastName = new SimpleStringProperty(this, "lastName");
            return lastName;
        }
    }


    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
