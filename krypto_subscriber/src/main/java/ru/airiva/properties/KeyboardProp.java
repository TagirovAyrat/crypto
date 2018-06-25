package ru.airiva.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeyboardProp {
    @Value("${START}")
    private String start;
    @Value("${CANCEL}")
    private String cancel;
    @Value("${FAQ}")
    private String faq;
    @Value("${ABOUT}")
    private String about;
    @Value("${PARTNERS}")
    private String partners;
    @Value("${PACKAGES}")
    private String packages;
    @Value("${PROFILE}")
    private String profile;
    @Value("${UNPAIDORDERS}")
    private String unpaidorders;
    @Value("${EXCHANGE}")
    private String exchange;
    @Value("${SUPPORT}")
    private String support;

}
