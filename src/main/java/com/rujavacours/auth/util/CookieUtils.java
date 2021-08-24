package com.rujavacours.auth.util;

import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;



/*

Утилита для работы с куками

Напоминание: кук jwt создается на сервере и управляется только сервером (создается, удаляется) - "server-side cookie"

На клиенте этот кук нельзя считать с помощью JavaScript (т.к. стоит флаг httpOnly) - для безопасности и защиты от XSS атак.

Смотрите более подробные комментарии в методе создания кука.

Также, обязательно канал должен быть HTTPS, чтобы нельзя было дешифровать данные запросов между клиентом (браузером) и сервером


 */


@Component // добавится в Spring контейнер и будет доступен для любого Spring компонента (контроллеры, сервисы и пр.)
public class CookieUtils {

    @Value("${cookie.jwt.name}")
    private String cookieJwtName; // имя кука, который будет хранить jwt (возьмем стандартное имя)

    @Value("${cookie.jwt.max-age}")
    private int cookieAccessTokenDuration;

    @Value("${server.domain}")
    private String cookieAccessTokenDomain;


    // создает server-side cookie со значением jwt. Важно: этот кук сможет считывать только сервер, клиент не сможет с помощью JS или другого клиентского кода (сделано для безопасности)
    public HttpCookie createJwtCookie(String jwt) { // jwt - значение для кука
        return ResponseCookie

                // настройки кука
                .from(cookieJwtName, jwt) // название и значение кука
                .maxAge(cookieAccessTokenDuration) // 86400 сек = 1 сутки
                .sameSite(SameSiteCookies.STRICT.getValue()) // запрет на отправку кука, если запрос пришел со стороннего сайта (доп. защита от CSRF атак) - кук будет отправляться только если пользователь набрал URL в адресной строке
                .httpOnly(true) // кук будет доступен для считывания только на сервере (на клиенте НЕ будет доступен с помощью JavaScript - тем самым защищаемся от XSS атак)
                .secure(false) // кук будет передаваться браузером на backend только если канал будет защищен (https)
                .domain(cookieAccessTokenDomain) // для какого домена действует кук (перед отправкой запроса на backend - браузер "смотрит" на какой домен он отправляется - и если совпадает со значением из кука - тогда прикрепляет кук к запросу)
                .path("/") // кук будет доступен для всех URL

                // создание объекта
                .build();

        /* примечание: все настройки кука (domain, path и пр.) - влияют на то, будет ли браузер отправлять их при запросе.

            Браузер сверяет URL запроса (который набрали в адресной строке или любой ajax запрос с формы) с параметрами кука.
            И если есть хотя бы одно несовпадение (например domain или path) - кук отправлен не будет.

          */
    }


    // получает значение кук access_token и возвращает его значение (JWT)
    public String getCookieAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) { // если в запросе были переданы какие-либо куки

            for (Cookie cookie : cookies) { // перебор всех куков
                if (cookieJwtName.equals(cookie.getName())) { // если есть наш кук (по названию) - то берем его
                    return cookie.getValue(); // получаем значение кука (JWT)
                }
            }
        }
        return null; // значит кук не нашли - возвращаем null
    }

    public HttpCookie deleteCookie() {
        return ResponseCookie
                .from(cookieJwtName, null)
                .maxAge(0)
                .sameSite(SameSiteCookies.STRICT.getValue())
                .httpOnly(true)
                .secure(false)
                .domain(cookieAccessTokenDomain)
                .path("/")
                .build();
    }

}
