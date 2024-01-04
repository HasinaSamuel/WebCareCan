package oma.services;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import oma.models.entity.CanTransaction;
import oma.models.interfaces.Points;
import oma.models.interfaces.PointsBeneficiers;
import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class CanTransactionService {
    public Date getDayStartWeek(Date today){
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int dayOfWeekNum = cal.get(Calendar.DAY_OF_WEEK);
        Date dayResponse = new Date();
        if(dayOfWeekNum == 7){
            dayResponse = today;
        } else {
            dayResponse = new Date(today.getTime() - (1000 * 60 * 60 * 24)*dayOfWeekNum);
        }
        dayResponse.setHours(00);
        dayResponse.setMinutes(00);
        dayResponse.setSeconds(00);
        return dayResponse;
    }

    public Date getDayEndWeek(Date today){
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int dayOfWeekNum = cal.get(Calendar.DAY_OF_WEEK);
        Date dayResponse = new Date();
        if(dayOfWeekNum == 6){
            dayResponse = today;
        } else if(dayOfWeekNum == 7) {
            dayResponse = new Date(today.getTime() + (1000 * 60 * 60 * 24)*6);
        }else{
            int nbrDate = 6 - dayOfWeekNum;
            dayResponse = new Date(today.getTime() + (1000 * 60 * 60 * 24)*nbrDate);
        }
        dayResponse.setHours(23);
        dayResponse.setMinutes(59);
        dayResponse.setSeconds(59);

        return dayResponse;
    }

    public long getCountAllPage(String cle, int increment){
        long totalCount = CanTransaction.find("beneficiaireMsisdn = ?1", cle).count();
        int i = (int) (totalCount / increment);
        int y = (int) (totalCount % increment);
        if(y != 0){
            i = i+1;
        }
        return i;
    }

    public long getCountweekPage(String cle, int increment){
        Date today = new Date();
        Date start = getDayStartWeek(today);
        Date end = getDayEndWeek(today);
        long totalCount = CanTransaction.find("insertionDate >= ?1 AND insertionDate <= ?2 AND beneficiaireMsisdn = ?3", start, end, cle).count();
        int i = (int) (totalCount / increment);
        int y = (int) (totalCount % increment);
        if(y != 0){
            i = i+1;
        }
        return i;
    }

    public long getCountDayPage(Date start,Date end, String cle, int increment){
        long totalCount = CanTransaction.find("insertionDate >= ?1 AND insertionDate <= ?2 AND beneficiaireMsisdn = ?3", start, end, cle).count();
        int i = (int) (totalCount / increment);
        int y = (int) (totalCount % increment);
        if(y != 0){
            i = i+1;
        }
        return i;
    }

    public List<CanTransaction> getAllTransactionByMsisdn(String cle, int page, int increment){
        List<CanTransaction> lignes = null;
        try {
            lignes = CanTransaction.find("beneficiaireMsisdn = ?1", cle).page(Page.of(page, increment)).list();
        }
        catch (Exception e){
            System.out.println("erreur: "+e.getMessage());
        }
        return lignes;
    }

    public List<CanTransaction> getWeekTransactionByMsisdn(String cle, int page, int increment){
        Date today = new Date();
        Date start = getDayStartWeek(today);
        Date end = getDayEndWeek(today);
        List<CanTransaction> lignes = null;
        try {
            lignes = CanTransaction.find("insertionDate >= ?1 AND insertionDate <= ?2 AND beneficiaireMsisdn = ?3", start, end, cle).page(Page.of(page, increment)).list();
        }
        catch (Exception e){
            System.out.println("erreur: "+e.getMessage());
        }
        return lignes;
    }

    public List<CanTransaction> getTransactionByMsisdnEndDay(Date start, Date end, String cle, int page, int increment){
        List<CanTransaction> lignes = null;
        try {
            lignes = CanTransaction.find("insertionDate >= ?1 AND insertionDate <= ?2 AND beneficiaireMsisdn = ?3", start, end, cle).page(Page.of(page, increment)).list();
        }
        catch (Exception e){
            System.out.println("erreur: "+e.getMessage());
        }
        return lignes;
    }

    public List<CanTransaction> getTransactionByMsisdnEndDayweek(Date start, Date end, String cle, int page, int increment){
        Date today = new Date();
        Date startweek = getDayStartWeek(today);
        Date endweek = getDayEndWeek(today);
        List<CanTransaction> lignes = null;
        try {
            lignes = CanTransaction.find("insertionDate >= ?1 AND insertionDate >= ?2 AND  insertionDate <= ?3  AND insertionDate <= ?4 AND  beneficiaireMsisdn = ?5", start,startweek, end,endweek, cle).page(Page.of(page, increment)).list();
        }
        catch (Exception e){
            System.out.println("erreur: "+e.getMessage());
        }
        return lignes;
    }

    public long getCountDayPageWeek(Date start,Date end, String cle, int increment){
        Date today = new Date();
        Date startweek = getDayStartWeek(today);
        Date endweek = getDayEndWeek(today);
        long totalCount = CanTransaction.find("insertionDate >= ?1 AND insertionDate >= ?2 AND insertionDate <= ?3 AND insertionDate <= ?4 AND beneficiaireMsisdn = ?5", start,startweek, end,endweek, cle).count();
        int i = (int) (totalCount / increment);
        int y = (int) (totalCount % increment);
        if(y != 0){
            i = i+1;
        }
        return i;
    }

    public PointsBeneficiers getPointBeneficier(String cle){
        PointsBeneficiers pb = new PointsBeneficiers();
        PanacheQuery<Points> pointHebdo = null;
        PanacheQuery<Points> pointAll = null;
        Date today = new Date();
        Date start = getDayStartWeek(today);
        Date end = getDayEndWeek(today);
        try {
            pointHebdo = CanTransaction.find("SELECT  SUM(u.points) as point   FROM CanTransaction u WHERE u.insertionDate >= ?1 AND u.insertionDate <= ?2 AND u.beneficiaireMsisdn = ?3", start, end, cle).project(Points.class);
            pointHebdo.stream().forEach(x ->{
                pb.setWeekPoint(x.point);
            });

            pointAll = CanTransaction.find("SELECT  SUM(u.points) as point   FROM CanTransaction u WHERE u.beneficiaireMsisdn = ?1", cle).project(Points.class);
            pointAll.stream().forEach(x ->{
                pb.setTotalPoint(x.point);
            });
        }catch (Exception e){
            System.out.println("erreur: "+e.getMessage());
        }
        return pb;
    }
}
