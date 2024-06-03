package viewandmodels.filmy;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/filmy";
    private static final String USER = "gabriela";
    private static final String PASSWORD = "";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<UserBadge> getTopThreeUsersWithBadges() throws SQLException {
        List<UserBadge> userBadges = new ArrayList<>();
        String query = "SELECT * FROM przyznaj_odznake()";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int userId = rs.getInt("id_uzytkownika");
                String badge = rs.getString("odznaka");
                userBadges.add(new UserBadge(userId, badge));
            }
        }
        return userBadges;
    }

    public static List<FilmyPracownicy> getFilmyPracownicy(String filmTitle, String actorName) throws SQLException {
        List<FilmyPracownicy> filmyPracownicy = new ArrayList<>();
        String query = "SELECT fp.id_filmu, fp.id_pracownika, fp.id_funkcji " +
                "FROM public.filmy_pracownicy fp " +
                "JOIN public.filmy f ON fp.id_filmu = f.id_filmu " +
                "JOIN public.uzytkownicy u ON fp.id_pracownika = u.id_uzytkownika " +
                "WHERE f.tytul ILIKE ? OR (u.imie || ' ' || u.nazwisko) ILIKE ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + filmTitle + "%");
            pstmt.setString(2, "%" + actorName + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int idFilmu = rs.getInt("id_filmu");
                    int idPracownika = rs.getInt("id_pracownika");
                    int idFunkcji = rs.getInt("id_funkcji");
                    filmyPracownicy.add(new FilmyPracownicy(idFilmu, idPracownika, idFunkcji));
                }
            }
        }
        return filmyPracownicy;
    }


    public static List<Film> getFilmsInCountry(String country) throws SQLException {
        List<Film> filmsInCountry = new ArrayList<>();
        String query = "SELECT f.id_filmu, f.tytul, f.rok_produkcji, fp.ograniczenie_wiekowe, fp.data_premiery " +
                "FROM public.filmy f " +
                "JOIN public.filmy_panstwa fp ON f.id_filmu = fp.id_filmu " +
                "JOIN public.panstwa p ON fp.id_panstwa = p.id_panstwa " +
                "WHERE p.nazwa_panstwa = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, country);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id_filmu");
                    String title = rs.getString("tytul");
                    int productionYear = rs.getInt("rok_produkcji");
                    int ageRestriction = rs.getInt("ograniczenie_wiekowe");
                    Date premiereDate = rs.getDate("data_premiery");
                    filmsInCountry.add(new Film(id, title, productionYear, ageRestriction, premiereDate));
                }
            }
        }
        return filmsInCountry;
    }

    public static String getUserBadge(int userId) throws SQLException {
        String query = "SELECT odznaka FROM public.uzytkownicy WHERE id_uzytkownika = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("odznaka");
                }
            }
        }
        return "No Badge";
    }

    public static void addRating(int userId, int filmPracownikId, double rating) throws SQLException {
        String query = "INSERT INTO public.oceny_uzytkownikow (id_uzytkownika, id_filmy_pracownicy, ocena, data_oceny) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, filmPracownikId);
            pstmt.setDouble(3, rating);
            pstmt.setDate(4, Date.valueOf(LocalDate.now()));
            pstmt.executeUpdate();
        }
    }

    // Pobieranie listy filmów
    public static List<Film> getFilms() throws SQLException {
        List<Film> films = new ArrayList<>();
        String query = "SELECT id_filmu, tytul, rok_produkcji FROM public.filmy ORDER BY id_filmu";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id_filmu");
                String title = rs.getString("tytul");
                int productionYear = rs.getInt("rok_produkcji");
                films.add(new Film(id, title, productionYear));
            }
        }
        return films;
    }
    // Pobieranie top 50 filmów
    public static List<Film> getTop50Films() throws SQLException {
        List<Film> top50Films = new ArrayList<>();
        String query = "SELECT id_filmu, tytul, rok_produkcji FROM public.filmy ORDER BY id_filmu LIMIT 50";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id_filmu");
                String title = rs.getString("tytul");
                int productionYear = rs.getInt("rok_produkcji");
                top50Films.add(new Film(id, title, productionYear));
            }
        }
        return top50Films;
    }

    // Pobieranie top 50 aktorów
    public static List<Actor> getTop50Actors() throws SQLException {
        List<Actor> top50Actors = new ArrayList<>();
        String query = "SELECT id, imie, nazwisko FROM najsławniejsi_aktorzy ORDER BY liczba_nagrod DESC, srednia_ocena DESC LIMIT 50";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("imie");
                String lastName = rs.getString("nazwisko");
                top50Actors.add(new Actor(id, firstName, lastName));
            }
        }
        return top50Actors;
    }



    // Pobieranie listy gatunków
    public static List<String> getGenres() throws SQLException {
        List<String> genres = new ArrayList<>();
        String query = "SELECT nazwa_gatunku FROM public.gatunki ORDER BY id_gatunku";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String genre = rs.getString("nazwa_gatunku");
                genres.add(genre);
            }
        }
        return genres;
    }

    // Pobieranie listy pracowników
    public static List<Pracownik> getPracownicy() throws SQLException {
        List<Pracownik> pracownicy = new ArrayList<>();
        String query = "SELECT id_pracownika, data_urodzenia, data_smierci FROM public.pracownicy ORDER BY id_pracownika";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id_pracownika");
                LocalDate dataUrodzenia = rs.getDate("data_urodzenia").toLocalDate();
                LocalDate dataSmierci = rs.getDate("data_smierci") != null ? rs.getDate("data_smierci").toLocalDate() : null;
                pracownicy.add(new Pracownik(id, dataUrodzenia, dataSmierci));
            }
        }
        return pracownicy;
    }

    // Pobieranie listy nagród filmowych
    public static List<NagrodaFilm> getNagrodyFilmy() throws SQLException {
        List<NagrodaFilm> nagrodyFilmy = new ArrayList<>();
        String query = "SELECT id_nagrody, id_filmu, nazwa_nagrody, kto FROM public.nagrody_filmy ORDER BY id_nagrody";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id_nagrody");
                int idFilmu = rs.getInt("id_filmu");
                String nazwa = rs.getString("nazwa_nagrody");
                int kto = rs.getInt("kto");
                nagrodyFilmy.add(new NagrodaFilm(id, idFilmu, nazwa, kto));
            }
        }
        return nagrodyFilmy;
    }

    // Pobieranie listy krajów
    public static List<Panstwo> getPanstwa() throws SQLException {
        List<Panstwo> panstwa = new ArrayList<>();
        String query = "SELECT id_panstwa, nazwa_panstwa FROM public.panstwa ORDER BY id_panstwa";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id_panstwa");
                String nazwa = rs.getString("nazwa_panstwa");
                panstwa.add(new Panstwo(id, nazwa));
            }
        }
        return panstwa;
    }

    // Pobieranie listy ocen
    public static List<Ocena> getOceny() throws SQLException {
        List<Ocena> oceny = new ArrayList<>();
        String query = "SELECT id_filmu, ocena_filmu FROM public.oceny ORDER BY id_filmu, ocena_filmu";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int idFilmu = rs.getInt("id_filmu");
                int ocena = rs.getInt("ocena_filmu");
                oceny.add(new Ocena(idFilmu, ocena));
            }
        }
        return oceny;
    }

    public static List<FilmyPracownicy> getUserProfile(String userName, String userBadge) throws SQLException {
        List<FilmyPracownicy> filmyPracownicy = new ArrayList<>();
        String query = "SELECT fp.id_filmu, fp.id_pracownika, fp.id_funkcji " +
                "FROM public.filmy_pracownicy fp " +
                "JOIN public.uzytkownicy u ON fp.id_pracownika = u.id_uzytkownika " +
                "WHERE u.nazwa LIKE ? OR u.odznaka LIKE ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + userName + "%");
            pstmt.setString(2, "%" + userBadge + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int idFilmu = rs.getInt("id_filmu");
                    int idPracownika = rs.getInt("id_pracownika");
                    int idFunkcji = rs.getInt("id_funkcji");
                    filmyPracownicy.add(new FilmyPracownicy(idFilmu, idPracownika, idFunkcji));
                }
            }
        }
        return filmyPracownicy;
    }

    // Metody do usuwania danych
    public static void deleteFilm(int id) throws SQLException {
        String query = "DELETE FROM public.filmy WHERE id_filmu = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public static void deletePracownik(int id) throws SQLException {
        String query = "DELETE FROM public.pracownicy WHERE id_pracownika = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}


