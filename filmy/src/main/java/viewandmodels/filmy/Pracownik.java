package viewandmodels.filmy;

import java.time.LocalDate;

public class Pracownik {
    private int id;
    private LocalDate dataUrodzenia;
    private LocalDate dataSmierci;

    public Pracownik(int id, LocalDate dataUrodzenia, LocalDate dataSmierci) {
        this.id = id;
        this.dataUrodzenia = dataUrodzenia;
        this.dataSmierci = dataSmierci;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDataUrodzenia() {
        return dataUrodzenia;
    }

    public void setDataUrodzenia(LocalDate dataUrodzenia) {
        this.dataUrodzenia = dataUrodzenia;
    }

    public LocalDate getDataSmierci() {
        return dataSmierci;
    }

    public void setDataSmierci(LocalDate dataSmierci) {
        this.dataSmierci = dataSmierci;
    }
}
