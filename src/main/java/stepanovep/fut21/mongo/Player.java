package stepanovep.fut21.mongo;

import java.time.LocalDateTime;

public class Player {

    private String resourceId;
    private String futbinId;
    private String name;
    private Integer rating;
    private String league;
    private String nation;
    private Integer pcPrice;
    private Integer xboxPrice;
    private Integer psPrice;
    private LocalDateTime priceUpdatedDt;

    public Player() {

    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getFutbinId() {
        return futbinId;
    }

    public void setFutbinId(String futbinId) {
        this.futbinId = futbinId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getPcPrice() {
        return pcPrice;
    }

    public void setPcPrice(Integer pcPrice) {
        this.pcPrice = pcPrice;
    }

    public Integer getXboxPrice() {
        return xboxPrice;
    }

    public void setXboxPrice(Integer xboxPrice) {
        this.xboxPrice = xboxPrice;
    }

    public Integer getPsPrice() {
        return psPrice;
    }

    public void setPsPrice(Integer psPrice) {
        this.psPrice = psPrice;
    }

    public LocalDateTime getPriceUpdatedDt() {
        return priceUpdatedDt;
    }

    public void setPriceUpdatedDt(LocalDateTime priceUpdatedDt) {
        this.priceUpdatedDt = priceUpdatedDt;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    @Override
    public String toString() {
        return "Player{" +
                "resourceId='" + resourceId + '\'' +
                ", futbinId='" + futbinId + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", league='" + league + '\'' +
                ", nation='" + nation + '\'' +
                ", price=" + pcPrice +
                ", priceUpdatedDt=" + priceUpdatedDt +
                '}';
    }

}
