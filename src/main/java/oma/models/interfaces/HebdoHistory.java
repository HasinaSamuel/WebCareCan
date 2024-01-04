package oma.models.interfaces;

import io.vertx.core.json.JsonObject;

public class HebdoHistory {
    public String beneficiaireMsisdn;
    public Long point;

    public HebdoHistory(String beneficiaireMsisdn, Long point) {
        this.beneficiaireMsisdn = beneficiaireMsisdn;
        this.point = point;
    }

    public String getBeneficiaireMsisdn() {
        return beneficiaireMsisdn;
    }

    public void setBeneficiaireMsisdn(String beneficiaireMsisdn) {
        this.beneficiaireMsisdn = beneficiaireMsisdn;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoints(Long point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return new JsonObject().put("beneficiaireMsisdn", beneficiaireMsisdn)
                .put("point", point)
                .toString();
    }
}
