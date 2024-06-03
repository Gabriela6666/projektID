package viewandmodels.filmy;

import java.util.Date;

public class Film {
    private int id;
    private String title;
    private int productionYear;
    private int ageRestriction;
    private Date premiereDate;


    public Film(int id, String title, int productionYear) {
        this.id = id;
        this.title = title;
        this.productionYear = productionYear;
    }

    public Film(int id, String title, int productionYear, int ageRestriction, Date premiereDate) {
        this.id = id;
        this.title = title;
        this.productionYear = productionYear;
        this.ageRestriction = ageRestriction;
        this.premiereDate = premiereDate;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }
}
