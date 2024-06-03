package viewandmodels.filmy;

public class FilmyPracownicy {
    private int idFilmu;
    private int idPracownika;
    private int idFunkcji;

    public FilmyPracownicy(int idFilmu, int idPracownika, int idFunkcji) {
        this.idFilmu = idFilmu;
        this.idPracownika = idPracownika;
        this.idFunkcji = idFunkcji;
    }

    public int getIdFilmu() {
        return idFilmu;
    }

    public void setIdFilmu(int idFilmu) {
        this.idFilmu = idFilmu;
    }

    public int getIdPracownika() {
        return idPracownika;
    }

    public void setIdPracownika(int idPracownika) {
        this.idPracownika = idPracownika;
    }

    public int getIdFunkcji() {
        return idFunkcji;
    }

    public void setIdFunkcji(int idFunkcji) {
        this.idFunkcji = idFunkcji;
    }
}
