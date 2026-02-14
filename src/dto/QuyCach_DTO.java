package dto;

public class QuyCach_DTO {
    private String maQC;
    private int slTrongHop;
    private int slHopTrongThung;
    private int slspThung;

    public QuyCach_DTO() {
    }

    public QuyCach_DTO(String maQC, int slTrongHop, int slHopTrongThung, int slspThung) {
        this.maQC = maQC;
        this.slTrongHop = slTrongHop;
        this.slHopTrongThung = slHopTrongThung;
        this.slspThung = slspThung;
    }

    public String getMaQC() {
        return maQC;
    }

    public void setMaQC(String maQC) {
        this.maQC = maQC;
    }

    public int getSlTrongHop() {
        return slTrongHop;
    }

    public void setSlTrongHop(int slTrongHop) {
        this.slTrongHop = slTrongHop;
    }

    public int getSlHopTrongThung() {
        return slHopTrongThung;
    }

    public void setSlHopTrongThung(int slHopTrongThung) {
        this.slHopTrongThung = slHopTrongThung;
    }

    public int getSlspThung() {
        return slspThung;
    }

    public void setSlspThung(int slspThung) {
        this.slspThung = slspThung;
    }
}