package com.fishedee.jpa_boost.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix="spring.jpa-boost")
public class JPABoostProperties {
    private boolean enable;
}
