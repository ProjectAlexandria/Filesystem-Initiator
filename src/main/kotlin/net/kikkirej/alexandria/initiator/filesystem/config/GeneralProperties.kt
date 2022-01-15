package net.kikkirej.alexandria.initiator.filesystem.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("alexandria")
class GeneralProperties (var sharedfolder: String = "/alexandriadata")