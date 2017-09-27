package jp.ijufumi.sample.twitter.interceptor;

import jp.ijufumi.sample.twitter.service.CampaignService;
import jp.ijufumi.sample.twitter.service.TwitterService;
import org.slf4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

public class TwitterConnectionInterceptor extends HandlerInterceptorAdapter {
    final TwitterService twitterService;
    final CampaignService campaignService;
    final Logger logger;

    public TwitterConnectionInterceptor(
            TwitterService twitterService,
            CampaignService campaignService,
            Logger logger
    ) {
        this.twitterService = twitterService;
        this.campaignService = campaignService;
        this.logger = logger;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestUri = request.getRequestURI();
        if (requestUri.startsWith("/connect/twitter") || requestUri.startsWith("/error")) {
            return true;
        }

        // Twitterのコネクションが取れない場合はSpring Social Twitterの機能を呼び出す
        if (!twitterService.isConnected()) {
            response.addCookie(new Cookie("callbackUrl", requestUri));
            response.sendRedirect("/connect/twitter");
            return false;
        }

        Optional<Cookie> cookieOpt =
                Arrays.stream(request.getCookies()).filter(x -> x.getName().equals("callbackUrl")).findFirst();

        if (cookieOpt.isPresent()) {
            Cookie cookie = cookieOpt.get();
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            response.sendRedirect(cookie.getValue());
            return false;
        }

        return true;
    }
}
