package net.kikkirej.alexandria.initiator.filesystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FilesystemInitiatorApplication

fun main(args: Array<String>) {
	runApplication<FilesystemInitiatorApplication>(*args)
}
