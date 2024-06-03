package viewandmodels.filmy;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Main extends Application {

    private TableView<Film> filmTable;
    private ObservableList<Film> filmData;
    private TableView<Pracownik> pracownikTable;
    private ObservableList<Pracownik> pracownikData;
    private TableView<String> gatunkiTable;
    private ObservableList<String> gatunkiData;
    private TableView<NagrodaFilm> nagrodyTable;
    private ObservableList<NagrodaFilm> nagrodyData;
    private TableView<Panstwo> panstwaTable;
    private ObservableList<Panstwo> panstwaData;
    private TableView<Ocena> ocenyTable;
    private ObservableList<Ocena> ocenyData;
    private ObservableList<Actor> actorData;
    private TableView<Actor> actorTable;
    private ObservableList<FilmyPracownicy> filmyPracownicyData;
    private TableView<FilmyPracownicy> filmyPracownicyTable;
    private Label userNameLabel;
    private Label userBadgeLabel;

    private int userId = 1;

    @Override
    public void start(Stage primaryStage) {
        // Login window should be displayed first
        LoginController loginApp = new LoginController();
        loginApp.setMainApp(this);
        loginApp.start(new Stage());
    }

    public void showMainWindow(Stage primaryStage) {
        primaryStage.setTitle("JavaFX PostgreSQL Example");

        // Create tabs for different data types
        TabPane tabPane = new TabPane();

        // Tab for Films
        Tab filmTab = new Tab("Films");
        VBox filmBox = new VBox();
        filmBox.getChildren().addAll(createFilmForm(), createFilmTable());
        filmTab.setContent(filmBox);

        // Tab for Pracownicy
        Tab pracownikTab = new Tab("Pracownicy");
        VBox pracownikBox = new VBox();
        pracownikBox.getChildren().addAll(createPracownikForm(), createPracownikTable());
        pracownikTab.setContent(pracownikBox);

        // Tab for Gatunki
        Tab gatunkiTab = new Tab("Gatunki");
        VBox gatunkiBox = new VBox();
        gatunkiBox.getChildren().addAll(createGatunkiTable(), listGatunkiButton());
        gatunkiTab.setContent(gatunkiBox);

        // Tab for Nagrody
        Tab nagrodyTab = new Tab("Nagrody Filmy");
        VBox nagrodyBox = new VBox();
        nagrodyBox.getChildren().addAll(createNagrodyTable(), listNagrodyButton());
        nagrodyTab.setContent(nagrodyBox);

        // Tab for Panstwa
        Tab panstwaTab = new Tab("Panstwa");
        VBox panstwaBox = new VBox();
        panstwaBox.getChildren().addAll(createPanstwaTable(), listPanstwaButton());
        panstwaTab.setContent(panstwaBox);

        // Tab for Oceny
        Tab ocenyTab = new Tab("Oceny");
        VBox ocenyBox = new VBox();
        ocenyBox.getChildren().addAll(createOcenyTable(), listOcenyButton());
        ocenyTab.setContent(ocenyBox);

        // Tab for Tools (to generate data)
        Tab toolsTab = new Tab("Tools");
        VBox toolsBox = new VBox();
        toolsBox.getChildren().addAll(createGenerateDataButton());
        toolsTab.setContent(toolsBox);

        // Tab for Top 50 Films
        Tab top50FilmsTab = new Tab("Top 50 Films");
        VBox top50FilmsBox = new VBox();
        top50FilmsBox.getChildren().addAll(createTop50FilmsTable(), listTop50FilmsButton());
        top50FilmsTab.setContent(top50FilmsBox);

        // Tab for Top 50 Actors
        Tab top50ActorsTab = new Tab("Top 50 Actors");
        VBox top50ActorsBox = new VBox();
        top50ActorsBox.getChildren().addAll(createTop50ActorsTable(), listTop50ActorsButton());
        top50ActorsTab.setContent(top50ActorsBox);

        // Tab for Films in Country
        Tab filmsInCountryTab = new Tab("Films in Country");
        VBox filmsInCountryBox = new VBox();
        filmsInCountryBox.getChildren().addAll(searchFilmsInCountryButton(), createTopFilmsInCountryTable());
        filmsInCountryTab.setContent(filmsInCountryBox);

        // Tab for User Profile
        Tab userProfileTab = new Tab("User Profile");
        VBox userProfileBox = new VBox();
        userProfileBox.getChildren().addAll(createUserProfile(), createFilmyPracownicyTable());
        userProfileTab.setContent(userProfileBox);

        // Add all tabs to the TabPane
        tabPane.getTabs().addAll(filmTab, pracownikTab, gatunkiTab, nagrodyTab, panstwaTab, ocenyTab, top50FilmsTab, top50ActorsTab, filmsInCountryTab, userProfileTab);

        // Set the Scene
        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        listUserProfile();
    }

    private VBox createUserProfile() {
        VBox vbox = new VBox();

        userNameLabel = new Label("User Name: " + getUserName());
        userBadgeLabel = new Label("Badge: " + getUserBadge());

        vbox.getChildren().addAll(userNameLabel, userBadgeLabel);
        return vbox;
    }

    private TableView<FilmyPracownicy> createFilmyPracownicyTable() {
        filmyPracownicyTable = new TableView<>();

        TableColumn<FilmyPracownicy, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<FilmyPracownicy, String> filmTitleColumn = new TableColumn<>("Film Title");
        filmTitleColumn.setCellValueFactory(new PropertyValueFactory<>("filmTitle"));

        TableColumn<FilmyPracownicy, String> actorNameColumn = new TableColumn<>("Actor Name");
        actorNameColumn.setCellValueFactory(new PropertyValueFactory<>("actorName"));

        filmyPracownicyTable.getColumns().addAll(idColumn, filmTitleColumn, actorNameColumn);

        filmyPracownicyData = FXCollections.observableArrayList();
        filmyPracownicyTable.setItems(filmyPracownicyData);

        return filmyPracownicyTable;
    }

    private HBox createFilmyPracownicySearchBox(TableView<FilmyPracownicy> table) {
        TextField filmTitleField = new TextField();
        filmTitleField.setPromptText("Film Title");

        TextField actorNameField = new TextField();
        actorNameField.setPromptText("Actor Name");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> {
            String filmTitle = filmTitleField.getText();
            String actorName = actorNameField.getText();
            try {
                List<FilmyPracownicy> results = Database.getFilmyPracownicy(filmTitle, actorName);
                filmyPracownicyData.clear();
                filmyPracownicyData.addAll(results);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        HBox searchBox = new HBox(filmTitleField, actorNameField, searchButton);
        searchBox.setSpacing(10);
        return searchBox;
    }

    private VBox createAddRatingBox() {
        VBox vbox = new VBox();

        Label label = new Label("Add Rating:");
        TextField filmPracownikIdField = new TextField();
        filmPracownikIdField.setPromptText("Film-Pracownik ID");

        TextField ratingField = new TextField();
        ratingField.setPromptText("Rating");

        Button addButton = new Button("Add Rating");
        addButton.setOnAction(e -> {
            String filmPracownikIdText = filmPracownikIdField.getText();
            String ratingText = ratingField.getText();
            try {
                int filmPracownikId = Integer.parseInt(filmPracownikIdText);
                double rating = Double.parseDouble(ratingText);
                Database.addRating(userId, filmPracownikId, rating);
                showAlert("Success", "Rating added successfully.");
            } catch (NumberFormatException | SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to add rating.");
            }
        });

        vbox.getChildren().addAll(label, filmPracownikIdField, ratingField, addButton);
        return vbox;
    }

    private void listUserProfile() {
        try {
            List<FilmyPracownicy> filmyPracownicy = Database.getFilmyPracownicy("", "");
            filmyPracownicyData.clear();
            if (filmyPracownicy.isEmpty()) {
                System.out.println("No records found");
            } else {
                filmyPracownicyData.addAll(filmyPracownicy);
                System.out.println(filmyPracownicy.size() + " records loaded");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addRating(int filmPracownikId, double rating) {
        try {
            Database.addRating(userId, filmPracownikId, rating);
            showAlert("Success", "Rating added successfully");
            listUserProfile();
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to add rating");
        }
    }

    private int getUserId() {
        // Mock user ID, replace with actual user ID from login
        return userId;
    }

    private String getUserName() {
        // Mock user name, replace with actual user name from login
        return "John Doe";
    }

    private String getUserBadge() {
        try {
            return Database.getUserBadge(getUserId());
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "No Badge";
        }
    }


    private TableView<Film> createTop50FilmsTable() {
        TableView<Film> top50FilmsTable = new TableView<>();
        TableColumn<Film, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Film, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Film, Integer> productionYearColumn = new TableColumn<>("Production Year");
        productionYearColumn.setCellValueFactory(new PropertyValueFactory<>("productionYear"));

        top50FilmsTable.getColumns().addAll(idColumn, titleColumn, productionYearColumn);

        ObservableList<Film> top50FilmsData = FXCollections.observableArrayList();
        try {
            List<Film> top50Films = Database.getTop50Films(); // Metoda do pobierania top 50 filmów z bazy danych
            top50FilmsData.addAll(top50Films);
        } catch (SQLException e) {
            e.printStackTrace();
            // Obsługa błędu pobierania danych z bazy danych
        }

        top50FilmsTable.setItems(top50FilmsData);

        return top50FilmsTable;
    }

    private VBox searchFilmsInCountryButton() {
        Button searchButton = new Button("Search");
        TextField countryTextField = new TextField();

        TableView<Film> table = createTopFilmsInCountryTable(); // Tworzenie tabeli w metodzie

        searchButton.setOnAction(event -> {
            String country = countryTextField.getText();
            if (!country.isEmpty()) {
                ObservableList<Film> filmsInCountryData = FXCollections.observableArrayList();
                try {
                    List<Film> filmsInCountry = Database.getFilmsInCountry(country);
                    filmsInCountryData.addAll(filmsInCountry);
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle database error
                }
                table.setItems(filmsInCountryData);
            }
        });

        HBox searchBox = new HBox(countryTextField, searchButton);
        searchBox.setSpacing(10);

        VBox searchLayout = new VBox(searchBox, table);
        searchLayout.setSpacing(10);

        return searchLayout;
    }

    private TableView<Film> createTopFilmsInCountryTable() {
        TableView<Film> topFilmsInCountryTable = new TableView<>();
        // Dodaj kolumny
        TableColumn<Film, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Film, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Film, Integer> productionYearColumn = new TableColumn<>("Production Year");
        productionYearColumn.setCellValueFactory(new PropertyValueFactory<>("productionYear"));

        TableColumn<Film, Integer> ageRestrictionColumn = new TableColumn<>("Age Restriction");
        ageRestrictionColumn.setCellValueFactory(new PropertyValueFactory<>("ageRestriction"));

        TableColumn<Film, Date> premiereDateColumn = new TableColumn<>("Premiere Date");
        premiereDateColumn.setCellValueFactory(new PropertyValueFactory<>("premiereDate"));

        topFilmsInCountryTable.getColumns().addAll(idColumn, titleColumn, productionYearColumn, ageRestrictionColumn, premiereDateColumn);

        return topFilmsInCountryTable;
    }

    private Button listTop50FilmsButton() {
        Button listTop50FilmsButton = new Button("List Top 50 Films");
        listTop50FilmsButton.setOnAction(event -> {
            try {
                List<Film> top50Films = Database.getTop50Films(); // Metoda do pobierania top 50 filmów z bazy danych
                filmData.clear();
                filmData.addAll(top50Films);
            } catch (SQLException e) {
                e.printStackTrace();
                // Obsługa błędu pobierania danych z bazy danych
            }
        });
        return listTop50FilmsButton;
    }

    private TableView<Actor> createTop50ActorsTable() {
        actorTable = new TableView<>();

        TableColumn<Actor, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Actor, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Actor, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Actor, Integer> awardsColumn = new TableColumn<>("Number of Awards");
        awardsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfAwards"));

        TableColumn<Actor, Double> ratingColumn = new TableColumn<>("Average Rating");
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("averageRating"));

        actorTable.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, awardsColumn, ratingColumn);

        actorData = FXCollections.observableArrayList();
        actorTable.setItems(actorData);


        return actorTable;
    }

    private Button listTop50ActorsButton() {
        Button listTop50ActorsButton = new Button("List Top 50 Actors");
        listTop50ActorsButton.setOnAction(event -> {
            try {
                List<Actor> top50Actors = Database.getTop50Actors(); // Metoda do pobierania top 50 aktorów z bazy danych
                actorData.clear();
                actorData.addAll(top50Actors);
            } catch (SQLException e) {
                e.printStackTrace();
                // Obsługa błędu pobierania danych z bazy danych
            }
        });
        return listTop50ActorsButton;
    }

    private VBox createFilmForm() {
        VBox vbox = new VBox();

        Label label = new Label("Add, Update or Delete Film:");
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField productionYearField = new TextField();
        productionYearField.setPromptText("Production Year");

        TextField idField = new TextField();
        idField.setPromptText("Film ID (for update/delete)");

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String title = titleField.getText();
            String productionYearText = productionYearField.getText();
            if (title == null || title.isEmpty()) {
                showAlert("Validation Error", "Title field is empty.");
            } else if (productionYearText == null || productionYearText.isEmpty()) {
                showAlert("Validation Error", "Production Year field is empty.");
            } else {
                try {
                    int productionYear = Integer.parseInt(productionYearText);
                    addFilm(title, productionYear);
                } catch (NumberFormatException ex) {
                    showAlert("Validation Error", "Production Year must be a number.");
                }
            }
        });

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            String idText = idField.getText();
            String title = titleField.getText();
            String productionYearText = productionYearField.getText();
            if (idText == null || idText.isEmpty()) {
                showAlert("Validation Error", "ID field is empty.");
            } else {
                try {
                    int id = Integer.parseInt(idText);
                    int productionYear = Integer.parseInt(productionYearText);
                    updateFilm(id, title, productionYear);
                } catch (NumberFormatException ex) {
                    showAlert("Validation Error", "ID and Production Year must be numbers.");
                }
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            String idText = idField.getText();
            if (idText == null || idText.isEmpty()) {
                showAlert("Validation Error", "ID field is empty.");
            } else {
                try {
                    int id = Integer.parseInt(idText);
                    deleteFilm(id);
                } catch (NumberFormatException ex) {
                    showAlert("Validation Error", "ID must be a number.");
                }
            }
        });

        Button listFilmsButton = new Button("List Films");
        listFilmsButton.setOnAction(e -> listFilms());

        vbox.getChildren().addAll(label, idField, titleField, productionYearField, addButton, updateButton, deleteButton, listFilmsButton);
        return vbox;
    }

    private TableView<Film> createFilmTable() {
        filmTable = new TableView<>();
        TableColumn<Film, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Film, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Film, Integer> productionYearColumn = new TableColumn<>("Production Year");
        productionYearColumn.setCellValueFactory(new PropertyValueFactory<>("productionYear"));

        filmTable.getColumns().add(idColumn);
        filmTable.getColumns().add(titleColumn);
        filmTable.getColumns().add(productionYearColumn);

        filmData = FXCollections.observableArrayList();
        filmTable.setItems(filmData);

        return filmTable;
    }

    private VBox createPracownikForm() {
        VBox vbox = new VBox();

        Label label = new Label("Add, Update or Delete Pracownik:");
        DatePicker dataUrodzeniaPicker = new DatePicker();
        dataUrodzeniaPicker.setPromptText("Data Urodzenia");

        DatePicker dataSmierciPicker = new DatePicker();
        dataSmierciPicker.setPromptText("Data Smierci");

        TextField idField = new TextField();
        idField.setPromptText("Pracownik ID (for update/delete)");

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            LocalDate dataUrodzenia = dataUrodzeniaPicker.getValue();
            LocalDate dataSmierci = dataSmierciPicker.getValue();
            if (dataUrodzenia == null) {
                showAlert("Validation Error", "Data Urodzenia field is empty.");
            } else {
                addPracownik(dataUrodzenia, dataSmierci);
            }
        });

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            String idText = idField.getText();
            LocalDate dataUrodzenia = dataUrodzeniaPicker.getValue();
            LocalDate dataSmierci = dataSmierciPicker.getValue();
            if (idText == null || idText.isEmpty()) {
                showAlert("Validation Error", "ID field is empty.");
            } else {
                try {
                    int id = Integer.parseInt(idText);
                    updatePracownik(id, dataUrodzenia, dataSmierci);
                } catch (NumberFormatException ex) {
                    showAlert("Validation Error", "ID must be a number.");
                }
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            String idText = idField.getText();
            if (idText == null || idText.isEmpty()) {
                showAlert("Validation Error", "ID field is empty.");
            } else {
                try {
                    int id = Integer.parseInt(idText);
                    deletePracownik(id);
                } catch (NumberFormatException ex) {
                    showAlert("Validation Error", "ID must be a number.");
                }
            }
        });

        Button listPracownicyButton = new Button("List Pracownicy");
        listPracownicyButton.setOnAction(e -> listPracownicy());

        vbox.getChildren().addAll(label, idField, dataUrodzeniaPicker, dataSmierciPicker, addButton, updateButton, deleteButton, listPracownicyButton);
        return vbox;
    }

    private TableView<Pracownik> createPracownikTable() {
        pracownikTable = new TableView<>();
        TableColumn<Pracownik, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Pracownik, LocalDate> dataUrodzeniaColumn = new TableColumn<>("Data Urodzenia");
        dataUrodzeniaColumn.setCellValueFactory(new PropertyValueFactory<>("dataUrodzenia"));

        TableColumn<Pracownik, LocalDate> dataSmierciColumn = new TableColumn<>("Data Smierci");
        dataSmierciColumn.setCellValueFactory(new PropertyValueFactory<>("dataSmierci"));

        pracownikTable.getColumns().add(idColumn);
        pracownikTable.getColumns().add(dataUrodzeniaColumn);
        pracownikTable.getColumns().add(dataSmierciColumn);

        pracownikData = FXCollections.observableArrayList();
        pracownikTable.setItems(pracownikData);

        return pracownikTable;
    }

    private TableView<String> createGatunkiTable() {
        gatunkiTable = new TableView<>();
        TableColumn<String, String> gatunekColumn = new TableColumn<>("Gatunek");
        gatunekColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue()));

        gatunkiTable.getColumns().add(gatunekColumn);

        gatunkiData = FXCollections.observableArrayList();
        gatunkiTable.setItems(gatunkiData);

        return gatunkiTable;
    }

    private Button listGatunkiButton() {
        Button button = new Button("List Gatunki");
        button.setOnAction(e -> listGatunki());
        return button;
    }

    private void listGatunki() {
        try {
            List<String> gatunki = Database.getGenres();
            gatunkiData.clear();
            if (gatunki.isEmpty()) {
                System.out.println("No genres found");
            } else {
                gatunkiData.addAll(gatunki);
                System.out.println(gatunki.size() + " genres loaded");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private TableView<NagrodaFilm> createNagrodyTable() {
        nagrodyTable = new TableView<>();
        TableColumn<NagrodaFilm, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<NagrodaFilm, Integer> idFilmuColumn = new TableColumn<>("ID Filmu");
        idFilmuColumn.setCellValueFactory(new PropertyValueFactory<>("idFilmu"));

        TableColumn<NagrodaFilm, String> nazwaColumn = new TableColumn<>("Nazwa");
        nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));

        TableColumn<NagrodaFilm, Integer> ktoColumn = new TableColumn<>("Kto");
        ktoColumn.setCellValueFactory(new PropertyValueFactory<>("kto"));

        nagrodyTable.getColumns().add(idColumn);
        nagrodyTable.getColumns().add(idFilmuColumn);
        nagrodyTable.getColumns().add(nazwaColumn);
        nagrodyTable.getColumns().add(ktoColumn);

        nagrodyData = FXCollections.observableArrayList();
        nagrodyTable.setItems(nagrodyData);

        return nagrodyTable;
    }

    private Button listNagrodyButton() {
        Button button = new Button("List Nagrody");
        button.setOnAction(e -> listNagrody());
        return button;
    }

    private void listNagrody() {
        try {
            List<NagrodaFilm> nagrody = Database.getNagrodyFilmy();
            nagrodyData.clear();
            if (nagrody.isEmpty()) {
                System.out.println("No awards found");
            } else {
                nagrodyData.addAll(nagrody);
                System.out.println(nagrody.size() + " awards loaded");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private TableView<Panstwo> createPanstwaTable() {
        panstwaTable = new TableView<>();
        TableColumn<Panstwo, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Panstwo, String> nazwaColumn = new TableColumn<>("Nazwa");
        nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));

        panstwaTable.getColumns().add(idColumn);
        panstwaTable.getColumns().add(nazwaColumn);

        panstwaData = FXCollections.observableArrayList();
        panstwaTable.setItems(panstwaData);

        return panstwaTable;
    }

    private Button listPanstwaButton() {
        Button button = new Button("List Panstwa");
        button.setOnAction(e -> listPanstwa());
        return button;
    }

    private void listPanstwa() {
        try {
            List<Panstwo> panstwa = Database.getPanstwa();
            panstwaData.clear();
            if (panstwa.isEmpty()) {
                System.out.println("No countries found");
            } else {
                panstwaData.addAll(panstwa);
                System.out.println(panstwa.size() + " countries loaded");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private TableView<Ocena> createOcenyTable() {
        ocenyTable = new TableView<>();

        TableColumn<Ocena, Integer> idFilmuColumn = new TableColumn<>("ID Filmu");
        idFilmuColumn.setCellValueFactory(new PropertyValueFactory<>("idFilmu"));

        TableColumn<Ocena, Integer> ocenaColumn = new TableColumn<>("Ocena");
        ocenaColumn.setCellValueFactory(new PropertyValueFactory<>("ocena"));

        ocenyTable.getColumns().add(idFilmuColumn);
        ocenyTable.getColumns().add(ocenaColumn);

        ocenyData = FXCollections.observableArrayList();
        ocenyTable.setItems(ocenyData);

        return ocenyTable;
    }

    private Button listOcenyButton() {
        Button button = new Button("List Oceny");
        button.setOnAction(e -> listOceny());
        return button;
    }

    private void listOceny() {
        try {
            List<Ocena> oceny = Database.getOceny();
            ocenyData.clear();
            if (oceny.isEmpty()) {
                System.out.println("No ratings found");
            } else {
                ocenyData.addAll(oceny);
                System.out.println(oceny.size() + " ratings loaded");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private VBox createGenerateDataButton() {
        VBox vbox = new VBox();

        Button generateDataButton = new Button("Generate Data");
        generateDataButton.setOnAction(e -> generateData());

        vbox.getChildren().addAll(generateDataButton);
        return vbox;
    }

    private void generateData() {
        DataGenerator.generateFilms(500);
        DataGenerator.generatePracownicy(500);
        DataGenerator.generateGatunki(50);
        DataGenerator.generateJezyki(50);
        DataGenerator.generatePanstwa(50);
        DataGenerator.generateNagrodyFilmy(100);
        DataGenerator.generateKategorie(20);
        DataGenerator.generateMuzyka(100);
        DataGenerator.generateOceny(100);
        DataGenerator.generateUzytkownicy(100);
        showAlert("Data Generation", "Dane zostały wygenerowane.");
    }

    private void addFilm(String title, int productionYear) {
        String query = "INSERT INTO public.filmy (tytul, rok_produkcji) VALUES (?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, title);
            pstmt.setInt(2, productionYear);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Film added successfully");
                listFilms();  // Refresh the table after adding a new entry
            } else {
                System.out.println("Failed to add film");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateFilm(int id, String title, int productionYear) {
        String query = "UPDATE public.filmy SET tytul = ?, rok_produkcji = ? WHERE id_filmu = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, title);
            pstmt.setInt(2, productionYear);
            pstmt.setInt(3, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Film updated successfully");
                listFilms();  // Refresh the table after updating an entry
            } else {
                System.out.println("Failed to update film");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteFilm(int id) {
        try {
            Database.deleteFilm(id);
            listFilms();  // Refresh the table after deleting an entry
            System.out.println("Film deleted successfully");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void listFilms() {
        try {
            List<Film> films = Database.getFilms();
            filmData.clear();
            if (films.isEmpty()) {
                System.out.println("No films found");
            } else {
                filmData.addAll(films);
                System.out.println(films.size() + " films loaded");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addPracownik(LocalDate dataUrodzenia, LocalDate dataSmierci) {
        String query = "INSERT INTO public.pracownicy (data_urodzenia, data_smierci) VALUES (?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(dataUrodzenia));
            if (dataSmierci != null) {
                pstmt.setDate(2, java.sql.Date.valueOf(dataSmierci));
            } else {
                pstmt.setNull(2, Types.DATE);
            }
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Pracownik added successfully");
                listPracownicy();  // Refresh the table after adding a new entry
            } else {
                System.out.println("Failed to add pracownik");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updatePracownik(int id, LocalDate dataUrodzenia, LocalDate dataSmierci) {
        String query = "UPDATE public.pracownicy SET data_urodzenia = ?, data_smierci = ? WHERE id_pracownika = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(dataUrodzenia));
            if (dataSmierci != null) {
                pstmt.setDate(2, java.sql.Date.valueOf(dataSmierci));
            } else {
                pstmt.setNull(2, Types.DATE);
            }
            pstmt.setInt(3, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Pracownik updated successfully");
                listPracownicy();  // Refresh the table after updating an entry
            } else {
                System.out.println("Failed to update pracownik");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deletePracownik(int id) {
        try {
            Database.deletePracownik(id);
            listPracownicy();  // Refresh the table after deleting an entry
            System.out.println("Pracownik deleted successfully");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void listPracownicy() {
        try {
            List<Pracownik> pracownicy = Database.getPracownicy();
            pracownikData.clear();
            if (pracownicy.isEmpty()) {
                System.out.println("No pracownicy found");
            } else {
                pracownikData.addAll(pracownicy);
                System.out.println(pracownicy.size() + " pracownicy loaded");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
