import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.GregorianCalendar;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Calendar extends Application{
    private BorderPane containerPane = new BorderPane();
    private GridPane monthPane = new GridPane();
    private Scene scene;
    private LocalDateTime date = LocalDateTime.now();
    // ---------------------------------------------- \\
    Label lblMonth;
    Label lblYear;
    int currentYear = date.getYear();
    int currentMonthInt = date.getMonthValue();

    YearMonth firstAndLastDay;
    String getFirstDay;
    String getLastDay;

    GregorianCalendar leapYearCheck = (GregorianCalendar) GregorianCalendar.getInstance();

    int dateInt = 0;
    Text[] daysArr = new Text[42];

    @Override //<<<<<<<<<<<<<<>>>>>>>>>>>>>>\\
    public void start(Stage primaryStage){

        scene = new Scene(containerPane, 900, 650);
        setupCalendarPane();
        primaryStage.setTitle("Calendar");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(620);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setupCalendarPane(){
        monthPane.setStyle("-fx-background-color: Blue; -fx-vgap: 1; -fx-hgap: 1; -fx-padding: 1");
        monthPane.setPadding(new Insets(10));
        containerPane.setPadding(new Insets(10));

        for(int i = 0; i < 42; i++){
            daysArr[i] = new Text("");
        }

        firstAndLastDay = YearMonth.of(currentYear, currentMonthInt); // YearMonth
        getFirstDay = firstAndLastDay.atDay(1).getDayOfWeek().name(); // String
        getLastDay = firstAndLastDay.atEndOfMonth().getDayOfWeek().name(); // String

        lblMonth = new Label(date.getMonth() + "");
        lblMonth.setFont(Font.font(19));
        lblYear = new Label(currentYear + "");
        lblYear.setFont(Font.font(19));
        HBox topLeft = new HBox(5); topLeft.setAlignment(Pos.BASELINE_LEFT);
        topLeft.getChildren().addAll(lblMonth, lblYear);

        Button btnLeftNav = new Button("<");
        Button btnToday = new Button("Today");
        Button btnRightNav = new Button(">");
        HBox topRight = new HBox(10); topLeft.setAlignment(Pos.BASELINE_RIGHT);
        topRight.getChildren().addAll(btnLeftNav, btnToday, btnRightNav);

        HBox topBox = new HBox(530);
        topBox.setPadding(new Insets(10));
        topBox.getChildren().addAll(topLeft, topRight);

        //topBox.prefWidth(980);
        topLeft.prefWidthProperty().bind(topBox.widthProperty().divide(2));
        topRight.prefWidthProperty().bind(topBox.widthProperty().divide(2));

        containerPane.setTop(topBox); // Top box
        containerPane.setCenter(monthPane); // Center box
        fillUpCalendar();

        // Adding actions
        btnLeftNav.setOnAction(e ->{
            containerPane.getChildren().remove(monthPane);
            if(currentMonthInt == 1){
                currentYear--;
                currentMonthInt = 12;
            }
            else
                currentMonthInt--;

            String monthName = new DateFormatSymbols().getMonths()[currentMonthInt - 1];
            lblMonth.setText(monthName.toUpperCase());
            lblYear.setText(currentYear + "");

            fillUpCalendar();
            containerPane.setCenter(monthPane);
        });

        btnToday.setOnAction(e ->{
            containerPane.getChildren().remove(monthPane);
            LocalDateTime dt = LocalDateTime.now();
            currentMonthInt = dt.getMonthValue();
            currentYear = dt.getYear();
            lblMonth.setText(dt.getMonth() + "");
            lblYear.setText(dt.getYear() + "");
            fillUpCalendar();
            containerPane.setCenter(monthPane);
        });

        btnRightNav.setOnAction(e ->{
            containerPane.getChildren().remove(monthPane);
            if(currentMonthInt == 12){
                currentYear++;
                currentMonthInt = 1;
            }
            else
                currentMonthInt++;

            String monthName = new DateFormatSymbols().getMonths()[currentMonthInt - 1];
            lblMonth.setText(monthName.toUpperCase());
            lblYear.setText(currentYear + "");
            fillUpCalendar();
            containerPane.setCenter(monthPane);
        });
    }

    public void fillUpCalendar(){

        firstAndLastDay = YearMonth.of(currentYear, currentMonthInt); // YearMonth
        getFirstDay = firstAndLastDay.atDay(1).getDayOfWeek().name(); // String
        getLastDay = firstAndLastDay.atEndOfMonth().getDayOfWeek().name(); // String

        Text[] days = {
            new Text("Sunday"), new Text("Monday"), new Text("Tuesday"),
            new Text("Wednesday"), new Text("Thursday"), new Text("Friday"),
            new Text("Saturday")
        };
        // Filling the name of the days, Sunday, Monday ....
        for(int i = 0, j = 0; j < 7; j++){
            StackPane stackPane = new StackPane();
            Rectangle rec = new Rectangle(127, 40);

            //rec.widthProperty().bind(monthPane.widthProperty().divide(7));
            //rec.heightProperty().bind(monthPane.heightProperty().divide(16));
            stackPane.getChildren().addAll(rec, days[j]);
            rec.setFill(Color.WHITE);
            monthPane.add(stackPane, j, i);
        }

        int dateOf = 0;
        int col = 1;
        int row = 0;
        int previous = 0;
        int nextMonthDate = 0;
        int nextCol = 0;
        int nextRow = 0;
        LocalDate newDate = LocalDate.of(currentYear, currentMonthInt, 1);
        int lengthOfMon = newDate.lengthOfMonth();
        // Filling the dates 1, 2, 3 .....
        for(col = 1; col < 7; col++){
            if(getFirstDay.equals("MONDAY") && col == 1){
                row = 1; previous = row;
            }
            else if(getFirstDay.equals("TUESDAY") && col == 1){
                row = 2; previous = row;
            }
            else if(getFirstDay.equals("WEDNESDAY") && col == 1){
                row = 3; previous = row;
            }
            else if(getFirstDay.equals("THURSDAY") && col == 1){
                row = 4; previous = row;
            }
            else if(getFirstDay.equals("FRIDAY") && col == 1){
                row = 5; previous = row;
            }
            else if(getFirstDay.equals("SATURDAY") && col == 1){
                previous = row = 6;
            }
            else row = 0;
            // Filling the dates
            for(row = row; row < 7; row++){
                if(lengthOfMon == dateOf){
                    nextMonthDate = 42 - dateOf - previous; break;
                }
                dateOf++;
                StackPane stackPane = new StackPane();
                Rectangle rec = new Rectangle(127, 93); // whole month
                //rec.widthProperty().bind(monthPane.widthProperty().divide(7));
                //rec.heightProperty().bind(monthPane.heightProperty().divide(6.5));
                Text dayText = new Text(dateOf + "");
                LocalDateTime date1 = LocalDateTime.now();
                int day = date1.getDayOfMonth();
                int mon = date1.getMonthValue();
                int yr = date1.getYear();
                if(dateOf == day && mon == currentMonthInt && yr == currentYear){
                    Circle cir = new Circle(20);
                    cir.setFill(Color.RED);
                    dayText.setFill(Color.WHITE);
                    rec.setFill(Color.WHITE);
                    stackPane.getChildren().addAll(rec, cir, dayText);
                    monthPane.add(stackPane, row, col);
                    nextRow = row; nextCol = col;
                }
                else{
                    stackPane.getChildren().addAll(rec, dayText);
                    rec.setFill(Color.WHITE);
                    monthPane.add(stackPane, row, col);
                    nextRow = row; nextCol = col;
                }
            }
        }

         //Filling dates from previous month
        int len = previous; // 4
        LocalDate ldate = LocalDate.of(currentYear, currentMonthInt, 1);
        if(currentMonthInt == 1){
            ldate = LocalDate.of(currentYear, 12, 1);
        }
        else
            ldate = LocalDate.of(currentYear, currentMonthInt-1, 1);

        int totalD = ldate.lengthOfMonth();
        totalD = totalD - previous;

        for(int j = 0; j < len; j++){
            StackPane stackPane = new StackPane();
            Rectangle rec = new Rectangle(127, 93);
            //rec.widthProperty().bind(monthPane.widthProperty().divide(7));
            //rec.heightProperty().bind(monthPane.heightProperty().divide(6.5));
            rec.setFill(Color.WHITE);
            totalD++;
            Text dayText = new Text(totalD + "");
            dayText.setFill(Color.GRAY);
            stackPane.getChildren().addAll(rec, dayText);
            monthPane.add(stackPane, j, 1);
        }

        // Filling the dates from next month 1, 2, ....
        int temp = 0; int forNextMonth = 0;

        for(int i = nextCol; i < 7; i++){
            if(i < 6) temp = nextRow+1;
            else if(nextMonthDate <= 7) temp = 7 - nextMonthDate;
            else temp = 0;

            for(int j = temp; j < 7; j++){
                forNextMonth++;
                StackPane stackPane = new StackPane();
                Rectangle rec = new Rectangle(127, 93);
                //rec.widthProperty().bind(monthPane.widthProperty().divide(7));
                //rec.heightProperty().bind(monthPane.heightProperty().divide(6.5));
                rec.setFill(Color.WHITE);
                Text dayText = new Text(forNextMonth + "");
                dayText.setFill(Color.GRAY);
                stackPane.getChildren().addAll(rec, dayText);
                monthPane.add(stackPane, j, i);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
