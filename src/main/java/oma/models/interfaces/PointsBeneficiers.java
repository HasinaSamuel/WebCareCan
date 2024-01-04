package oma.models.interfaces;

import io.vertx.core.json.JsonObject;

public class PointsBeneficiers {
    public Long totalPoint;
    public Long weekPoint;
    public PointsBeneficiers() {
    }

    public PointsBeneficiers(Long totalPoint, Long weekPoint) {
        this.totalPoint = totalPoint;
        this.weekPoint = weekPoint;
    }

    public Long getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Long totalPoint) {
        this.totalPoint = totalPoint;
    }

    public Long getWeekPoint() {
        return weekPoint;
    }

    public void setWeekPoint(Long weekPoint) {
        this.weekPoint = weekPoint;
    }

    @Override
    public String toString() {
        return new JsonObject()
                .put("totalPoint", totalPoint)
                .put("weekPoint", weekPoint)
                .toString();
    }
}
