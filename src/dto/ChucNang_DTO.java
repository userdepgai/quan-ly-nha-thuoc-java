    package dto;

    public class ChucNang_DTO {

        private String maCN;
        private String tenCN;
        private String moTa;

        public ChucNang_DTO() {}

        public ChucNang_DTO(String maCN, String tenCN, String moTa) {
            this.maCN = maCN;
            this.tenCN = tenCN;
            this.moTa = moTa;
        }

        public String getMaCN() {
            return maCN;
        }

        public void setMaCN(String maCN) {
            this.maCN = maCN;
        }

        public String getTenCN() {
            return tenCN;
        }

        public void setTenCN(String tenCN) {
            this.tenCN = tenCN;
        }

        public String getMoTa() {
            return moTa;
        }

        public void setMoTa(String moTa) {
            this.moTa = moTa;
        }
    }