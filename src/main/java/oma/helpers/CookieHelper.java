package oma.helpers;
import io.quarkus.vertx.web.RoutingExchange;
import io.vertx.core.http.Cookie;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CookieHelper {
    private RoutingExchange re;

    public CookieHelper(){
    }
    public CookieHelper(RoutingExchange currentRoutingExchange){
        this.re = currentRoutingExchange;
    }

    public static void setUserCookie(RoutingExchange re){
        re.response().addCookie(Cookie.cookie("userConnect", "rsj24lq85X25job2HXb6ZBMe3jd7UOFqVPhYm0iacPhM9eJyAEdQc736cCi0Tyfvr0PalIAhw09ryf7d2emwT2PhVHudccmiyB6T").setHttpOnly(false));
    }

    public static String getDisplayMode(RoutingExchange re){
        if(re.request().getCookie("userConnect") != null){
            String userCookie = re.request().getCookie("userConnect").getValue();
            return userCookie;
        }
        return null;
    }

    public static boolean hasCookieUser(RoutingExchange re){
        if(re.request().getCookie("userConnect") != null && re.request().getCookie("userConnect").getValue().equals("rsj24lq85X25job2HXb6ZBMe3jd7UOFqVPhYm0iacPhM9eJyAEdQc736cCi0Tyfvr0PalIAhw09ryf7d2emwT2PhVHudccmiyB6T")){
            return true;
        }
       return false;
    }
    public static void deleteCookie(RoutingExchange re) {
        re.request().getCookie("userConnect").setMaxAge(0);
    }
}
