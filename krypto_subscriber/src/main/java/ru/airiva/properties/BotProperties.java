package ru.airiva.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BotProperties {
    public BotProperties() {
    }

    @Value("${bot.username}")
    public String username;

    @Value("${bot.token}")
    public String token;

    @Value("${bot.path}")
    public String path;

    @Value("${bot.keystore}")
    public String keystore;

    @Value("${bot.keystore.password}")
    public String keystorePassword;

    @Value("${bot.external.url}")
    public String externalUrl;

    @Value("${bot.internal.url}")
    public String internalUrl;

    @Value("${bot.certificate.path}")
    public String certificatePath;

    @Value("${proxy.host}")
    public String proxyHost;

    @Value("${proxy.port}")
    public Integer proxyPort;

    @Value("${path.pem}")
    public String pathToPem;
}
