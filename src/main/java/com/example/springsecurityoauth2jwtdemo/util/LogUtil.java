package com.example.springsecurityoauth2jwtdemo.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class LogUtil {
    protected static Logger auditLogger = LoggerFactory.getLogger("audit");

    /**
     * Audit Log(감사 로그)를 출력하는 메서드
     *
     * @param action  Http 요청을 전달한 클라이언트가 행동한 내용
     * @param actor   Http 요청을 전달한 클라이언트. (예: 이메일, 이름, 유저아이디 등)
     * @param request Http 요청을 전달한 클라이언트가 요청한 모든 Http 리소스를 출력하기 위한 서블릿 객체
     */
    public static void writeAuditLog(String action, String actor, HttpServletRequest request) {
        try {
            auditLogger.info(new ObjectMapper().writeValueAsString(new AuditLog(action, actor, request)));
        } catch (JsonProcessingException e) {
            System.out.println(String.format(">>> Audit Log Json Processing Exception : %s", e.getMessage()));
            e.printStackTrace();
        }
    }
}


@Getter
@Setter
class LogBase {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.S Z", timezone = "Asia/Seoul")
    protected Date time = new Date(System.currentTimeMillis());
    protected String table;
}

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
class AuditLog extends LogBase {
    private String action; // 요청자가 요청한 행동
    private String actor; // 요청자 정보 (google id 혹은 이름 등)
    private String ip; // 요청자의 ip address
    private String path; // 요청자가 요청한 uri 경로
    private String method; // 요청자가 요청한 http method
    private Map<String, String> headers; // 요청자가 요청시 보낸 http header
    private Map<String, String> params; // 요청자가 요청시 보낸 http parameters
    private String body; // 요청자가 요청시 보낸 http body

    public AuditLog(String action, String actor, HttpServletRequest request) {
        this.table = "audit_log";
        this.action = action;
        this.actor = actor;
        if (request != null) {
            this.ip = !StringUtils.isEmpty(request.getRemoteHost()) ? request.getRemoteHost() : null;
            this.path = !StringUtils.isEmpty(request.getServletPath()) ? request.getServletPath() : null;
            this.method = !StringUtils.isEmpty(request.getMethod()) ? request.getMethod() : null;
            this.headers = !StringUtils.isEmpty(getHeaderMap(request)) ? getHeaderMap(request) : null;
            this.params = !StringUtils.isEmpty(getParamMap(request)) ? getParamMap(request) : null;
//            this.body =
        }
    }

    protected Map<String, String> getHeaderMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    protected Map<String, String> getParamMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String key = (String) paramNames.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
        }
        return map;
    }
}