package oma.models.interfaces;

import io.vertx.core.json.JsonObject;

public class Points {
    public Long point;

    public Points(Long point) {
        this.point = point;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoints(Long point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return new JsonObject()
                .put("point", point)
                .toString();
    }
}
