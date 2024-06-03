package viewandmodels.filmy;

public class NagrodaFilm {
    private int id;
    private int idFilmu;
    private String nazwa;
    private int kto;

    public NagrodaFilm(int id, int idFilmu, String nazwa, int kto) {
        this.id = id;
        this.idFilmu = idFilmu;
        this.nazwa = nazwa;
        this.kto = kto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFilmu() {
        return idFilmu;
    }

    public void setIdFilmu(int idFilmu) {
        this.idFilmu = idFilmu;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public int getKto() {
        return kto;
    }

    public void setKto(int kto) {
        this.kto = kto;
    }
}
