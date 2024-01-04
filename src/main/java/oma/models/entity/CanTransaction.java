package oma.models.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.vertx.core.json.JsonObject;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
@Entity
@Table(name = "can_transaction")
public class CanTransaction extends PanacheEntityBase {
    @Id
    private Long id;
    @Column(name = "refill_id")
    String refillId;
    @Column(name = "bundle_name")
    String bundleName;
    @Column(name = "beneficiaire_msisdn")
    String beneficiaireMsisdn;
    @Column(name = "sender_msisdn")
    String senderMsisdn;
    @Column(name = "for_other")
    Long forOther;
    @Column(name = "points")
    Long points;
    @Column(name = "total_points")
    String totalPoints;
    @Column(name = "origin")
    String origin;
    @Column(name = "insertion_date")
    Date insertionDate;

    public CanTransaction() {
    }
    public CanTransaction(String refillId, String bundleName, String beneficiaireMsisdn, String senderMsisdn, Long forOther, Long points, String totalPoints, String origin, Date insertionDate) {
        this.refillId = refillId;
        this.bundleName = bundleName;
        this.beneficiaireMsisdn = beneficiaireMsisdn;
        this.senderMsisdn = senderMsisdn;
        this.forOther = forOther;
        this.points = points;
        this.totalPoints = totalPoints;
        this.origin = origin;
        this.insertionDate = insertionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefillId() {
        return refillId;
    }

    public void setRefillId(String refillId) {
        this.refillId = refillId;
    }

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public String getBeneficiaireMsisdn() {
        return beneficiaireMsisdn;
    }

    public void setBeneficiaireMsisdn(String beneficiaireMsisdn) {
        this.beneficiaireMsisdn = beneficiaireMsisdn;
    }

    public String getSenderMsisdn() {
        return senderMsisdn;
    }

    public void setSenderMsisdn(String senderMsisdn) {
        this.senderMsisdn = senderMsisdn;
    }

    public Long getForOther() {
        return forOther;
    }

    public void setForOther(Long forOther) {
        this.forOther = forOther;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public String getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    @Override
    public String toString() {
        return new JsonObject().put("refillId", refillId)
                .put("bundleName", bundleName)
                .put("beneficiaireMsisdn", beneficiaireMsisdn)
                .put("senderMsisdn", senderMsisdn)
                .put("forOther", forOther)
                .put("points", points)
                .put("totalPoints", totalPoints)
                .put("origin", origin)
                .put("insertionDate", new SimpleDateFormat("dd MMMM yyyy")
                .format(insertionDate))
                .toString();
    }
}
