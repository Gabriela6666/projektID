package viewandmodels.filmy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Random;

public class DataGenerator {

    public static void generateFilms(int count) {
        String query = "INSERT INTO public.filmy (tytul, rok_produkcji) VALUES (?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < count; i++) {
                pstmt.setString(1, "Film " + i);
                pstmt.setInt(2, 2000 + new Random().nextInt(22));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void generatePracownicy(int count) {
        String query = "INSERT INTO public.pracownicy (data_urodzenia, data_smierci) VALUES (?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < count; i++) {
                LocalDate dataUrodzenia = LocalDate.of(1970 + new Random().nextInt(30), new Random().nextInt(12) + 1, new Random().nextInt(28) + 1);
                LocalDate dataSmierci = new Random().nextBoolean() ? null : LocalDate.of(2000 + new Random().nextInt(20), new Random().nextInt(12) + 1, new Random().nextInt(28) + 1);
                pstmt.setDate(1, java.sql.Date.valueOf(dataUrodzenia));
                if (dataSmierci != null) {
                    pstmt.setDate(2, java.sql.Date.valueOf(dataSmierci));
                } else {
                    pstmt.setNull(2, Types.DATE);
                }
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void generateGatunki(int count) {
        String query = "INSERT INTO public.gatunki (nazwa_gatunku) VALUES (?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < count; i++) {
                pstmt.setString(1, "Gatunek " + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void generateJezyki(int count) {
        String query = "INSERT INTO public.jezyki (nazwa) VALUES (?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < count; i++) {
                pstmt.setString(1, "Jezyk " + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void generatePanstwa(int count) {
        String query = "INSERT INTO public.panstwa (nazwa_panstwa) VALUES (?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < count; i++) {
                pstmt.setString(1, "Panstwo " + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void generateNagrodyFilmy(int count) {
        String query = "INSERT INTO public.nagrody_filmy (id_filmu, nazwa_nagrody, kto) VALUES (?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < count; i++) {
                pstmt.setInt(1, new Random().nextInt(500) + 1);
                pstmt.setString(2, "Nagroda " + i);
                pstmt.setInt(3, new Random().nextInt(500) + 1);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void generateKategorie(int count) {
        String query = "INSERT INTO public.kategorie (nazwa_kategorii) VALUES (?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < count; i++) {
                pstmt.setString(1, "Kategoria " + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void generateMuzyka(int count) {
        String query = "INSERT INTO public.muzyka (tytul_piosenki) VALUES (?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < count; i++) {
                pstmt.setString(1, "Piosenka " + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void generateOceny(int count) {
        String query = "INSERT INTO public.oceny (id_filmu, ocena_filmu) VALUES (?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < count; i++) {
                pstmt.setInt(1, new Random().nextInt(500) + 1);
                pstmt.setInt(2, new Random().nextInt(10) + 1);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void generateUzytkownicy(int count) {
        String query = "INSERT INTO public.uzytkownicy (nazwa, haslo) VALUES (?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < count; i++) {
                pstmt.setString(1, "Uzytkownik " + i);
                pstmt.setString(2, "haslo" + i);  // Pamiętaj, aby w produkcji używać szyfrowania hasła
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
