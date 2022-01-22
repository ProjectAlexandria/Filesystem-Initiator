package net.kikkirej.alexandria.initiator.filesystem.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "alexandria.initiator")
class InitConfig {
    var processKey="alexandria-default"
}