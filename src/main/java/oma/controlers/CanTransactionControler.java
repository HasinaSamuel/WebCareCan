package oma.controlers;
import io.quarkus.qute.Template;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RoutingExchange;
import io.smallrye.common.annotation.Blocking;
import oma.helpers.CookieHelper;
import oma.services.CanTransactionService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@Path("/")
public class CanTransactionControler {
    private final Template page;
    @ConfigProperty(name = "home.baseurl")
    String homeUrl;

    @ConfigProperty(name = "incremantPage")
    int incremantPageParam;
    private final Template login;
    @Inject
    CanTransactionService canTransactionService;
    @Inject
    CookieHelper cookieHelper;

    public CanTransactionControler(Template page, Template login) {
        this.page = requireNonNull(page, "page is required");
        this.login = requireNonNull(login, "page is required");
    }
    @Route(path = "home", methods = Route.HttpMethod.GET)
    public String get(RoutingExchange re) {
        Map<String, Object> data = new HashMap<>();
        data.put("isHome", true);
        data.put("homeUrl", homeUrl);

        if(cookieHelper.hasCookieUser(re)){
            return page.data("data", data).render();
        }else {
            re.response().setStatusCode(302).putHeader("location", "/").end();
            return "";
        }
    }

    @Route(path = "", methods = Route.HttpMethod.GET)
    public String login(RoutingExchange re) {
        Map<String, Object> data = new HashMap<>();
        data.put("homeUrl", homeUrl);
        return login.data("data", data).render();
    }

    @Route(path = "/logout", methods = Route.HttpMethod.GET)
    public void logOut(RoutingExchange re) {
        cookieHelper.deleteCookie(re);
        re.response().setStatusCode(302).putHeader("location", "/").end();
    }

    @Route(path = "/valide-login", methods = Route.HttpMethod.POST)
    public void valideLogin(RoutingExchange re) {
        String email = re.request().getFormAttribute("email");
        String password = re.request().getFormAttribute("password");

        Map<String, Object> data = new HashMap<>();
        data.put("homeUrl", homeUrl);

        if(email.equals("webcarecan@orange.com") && password.equals("KX1JeTzzaSHotw8Q")){
            data.put("isHome", true);
            cookieHelper.setUserCookie(re);
            re.response().setStatusCode(302).putHeader("location", "/home").end();
        }else{
            re.response().setStatusCode(302).putHeader("location", "/").end();
        }
    }

    @Blocking
    @Route(path = "/search", methods = Route.HttpMethod.POST)
    public String searchTrasaction( RoutingExchange re) {
        String cle = re.request().getFormAttribute("cle");
        Date today = new Date();
        int pageNumber = 1;
        int incremantPage = incremantPageParam;
        Map<String, Object> data = new HashMap<>();
        data.put("isHome", false);
        data.put("msisdn", cle);
        data.put("startWeek", canTransactionService.getDayStartWeek(today));
        data.put("endWeek", canTransactionService.getDayEndWeek(today));
        data.put("allTransactions", canTransactionService.getWeekTransactionByMsisdn(cle,pageNumber-1, incremantPage));
        data.put("points", canTransactionService.getPointBeneficier(cle));
        data.put("homeUrl", homeUrl);
        data.put("totalPage", (int)canTransactionService.getCountweekPage(cle, incremantPage));
        data.put("pageNumber", pageNumber);
        data.put("cle", cle);
        data.put("type", "1");
        data.put("day", "");

        if(cookieHelper.hasCookieUser(re)){
            return page.data("data", data).render();
        }else {
            re.response().setStatusCode(302).putHeader("location", "/").end();
            return "";
        }
    }

    @Blocking
    @Route(path = "par-page", methods = Route.HttpMethod.GET)
    public String searchAllPage( RoutingExchange re) throws ParseException {
        String cle = re.request().getParam("cle");
        String type = re.request().getParam("type");
        String day = re.request().getParam("day");

        int pageNumber = Integer.parseInt(re.request().getParam("pageNumber"));
        int incremantPage = incremantPageParam;
        Date today = new Date();
        Map<String, Object> data = new HashMap<>();
        data.put("isHome", false);
        data.put("homeUrl", homeUrl);
        data.put("isHome", false);
        data.put("msisdn", cle);
        data.put("startWeek", canTransactionService.getDayStartWeek(today));
        data.put("endWeek", canTransactionService.getDayEndWeek(today));
        data.put("points", canTransactionService.getPointBeneficier(cle));
        data.put("pageNumber", pageNumber);
        data.put("cle", cle);
        data.put("type", type);
        data.put("day", day);

        if(!day.equals("")){
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date dayStart = format.parse(day);
            Date dayEnd = format.parse(day);
            dayEnd.setHours(23);
            dayEnd.setMinutes(59);
            dayEnd.setSeconds(59);
            if(type.equals("2")){
                data.put("allTransactions", canTransactionService.getTransactionByMsisdnEndDay(dayStart,dayEnd,cle, pageNumber-1, incremantPage));
                data.put("totalPage", (int)canTransactionService.getCountDayPage(dayStart,dayEnd, cle, incremantPage));
            }else{
                data.put("allTransactions", canTransactionService.getTransactionByMsisdnEndDayweek(dayStart,dayEnd,cle, pageNumber-1, incremantPage));
                data.put("totalPage", (int)canTransactionService.getCountDayPageWeek(dayStart,dayEnd, cle, incremantPage));
            }
        }else{
            if(type.equals("2")){
                data.put("allTransactions", canTransactionService.getAllTransactionByMsisdn(cle, pageNumber-1, incremantPage));
                data.put("totalPage", (int)canTransactionService.getCountAllPage(cle, incremantPage));
            }else{
                data.put("allTransactions", canTransactionService.getWeekTransactionByMsisdn(cle,pageNumber-1, incremantPage));
                data.put("totalPage", (int)canTransactionService.getCountweekPage(cle, incremantPage));
            }
        }
        return page.data("data", data).render();
    }

    @Blocking
    @Route(path = "/filter", methods = Route.HttpMethod.POST)
    public String filter(RoutingExchange re) throws ParseException {

        String day = re.request().getFormAttribute("day");
        String cle = re.request().getFormAttribute("cleRecupe");
        String type = re.request().getFormAttribute("type");

        int pageNumber = 1;
        int incremantPage = incremantPageParam;
        Date today = new Date();
        Map<String, Object> data = new HashMap<>();
        data.put("isHome", false);
        data.put("msisdn", cle);
        data.put("startWeek", canTransactionService.getDayStartWeek(today));
        data.put("endWeek", canTransactionService.getDayEndWeek(today));
        data.put("points", canTransactionService.getPointBeneficier(cle));
        data.put("homeUrl", homeUrl);
        data.put("pageNumber", pageNumber);
        data.put("cle", cle);
        data.put("type", type);
        data.put("day", day);

        if(!day.equals("")){
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date dayStart = format.parse(day);
            Date dayEnd = format.parse(day);
            dayEnd.setHours(23);
            dayEnd.setMinutes(59);
            dayEnd.setSeconds(59);

            if(type.equals("1")){
                data.put("allTransactions", canTransactionService.getTransactionByMsisdnEndDayweek(dayStart,dayEnd,cle, pageNumber-1, incremantPage));
                data.put("totalPage", (int)canTransactionService.getCountDayPageWeek(dayStart,dayEnd, cle, incremantPage));
            }else{
                data.put("allTransactions", canTransactionService.getTransactionByMsisdnEndDay(dayStart,dayEnd,cle, pageNumber-1, incremantPage));
                data.put("totalPage", (int)canTransactionService.getCountDayPage(dayStart,dayEnd, cle, incremantPage));
            }

        }else {
            if(type.equals("1")){
                data.put("allTransactions", canTransactionService.getWeekTransactionByMsisdn(cle,pageNumber-1, incremantPage));
                data.put("totalPage", (int)canTransactionService.getCountweekPage(cle, incremantPage));
            }else{
                data.put("allTransactions", canTransactionService.getAllTransactionByMsisdn(cle, pageNumber-1, incremantPage));
                data.put("totalPage",(int)canTransactionService.getCountAllPage(cle, incremantPage));
            }
        }

        if(cookieHelper.hasCookieUser(re)){
            return page.data("data", data).render();
        }else {
            re.response().setStatusCode(302).putHeader("location", "/").end();
            return "";
        }
    }
}
