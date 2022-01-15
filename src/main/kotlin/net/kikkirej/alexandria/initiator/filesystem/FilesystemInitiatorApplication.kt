package net.kikkirej.alexandria.initiator.filesystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class FilesystemInitiatorApplication

fun main(args: Array<String>) {
	runApplication<FilesystemInitiatorApplication>(*args)
}
