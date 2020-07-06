package com.imposto.fatura.event;

import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletResponse;

public class RecursoCriadoEvent extends ApplicationEvent {
    private HttpServletResponse httpServletResponse;
    private Integer id;

    public RecursoCriadoEvent(Object source, HttpServletResponse httpServletResponse, Integer id) {
        super(source);
        this.httpServletResponse = httpServletResponse;
        this.id = id;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public Integer getId() {
        return id;
    }
}
