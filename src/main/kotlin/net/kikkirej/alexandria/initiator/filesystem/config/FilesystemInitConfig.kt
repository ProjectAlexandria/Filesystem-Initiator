package net.kikkirej.alexandria.initiator.filesystem.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "alexandria.initiator.filesystem")
class FileSystemInitConfig {
    var sources: List<FilesystemSourceConfig> = listOf(FilesystemSourceConfig(1, "default", "/projects"))
    var cron: String = "5/* * * * * *"
}

class FilesystemSourceConfig(var id: Long, var name: String, var path: String) {
    constructor() : this(0, "", "")
}
