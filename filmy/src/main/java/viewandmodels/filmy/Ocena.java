package viewandmodels.filmy;

public class Ocena {
    private int idFilmu;
    private int ocena;

    public Ocena(int idFilmu, int ocena) {
        this.idFilmu = idFilmu;
        this.ocena = ocena;
    }

    public int getIdFilmu() {
        return idFilmu;
    }

    public void setIdFilmu(int idFilmu) {
        this.idFilmu = idFilmu;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }
}
