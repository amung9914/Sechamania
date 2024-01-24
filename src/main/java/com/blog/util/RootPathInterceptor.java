package com.blog.util;

import com.blog.entity.Referer;
import com.blog.repository.RefererRepository;
import com.blog.service.RefererService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RootPathInterceptor implements HandlerInterceptor {

    private final RefererService refererService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String hostHeader = request.getHeader("Host");
        boolean isInternalRequest = hostHeader != null && hostHeader.contains("domainAddress");

        if(requestURI.equals("/")&& !isRedirect(response)){
            String path = request.getHeader("Referer");
            if(path==null){
                path = "직접 유입";
            }
            refererService.save(new Referer(path));
        }
        // 내 사이트에서 오는게 아님
        /*if(requestURI.equals("/")&& !isRedirect(response)&&!isInternalRequest){
            refererService.save(new Referer(request.getHeader("Referer")));
        }*/
        return true;
    }

    private boolean isRedirect(HttpServletResponse response){
        return response.getStatus() == HttpServletResponse.SC_FOUND; //302제외
    }
}
