package net.kikkirej.alexandria.initiator.filesystem

import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity(name = "source")
class Source(@Id var id: Long,
             var name: String,
             var type:String = "Filesystem")

@Entity(name = "project")
class Project(@Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
              @ManyToOne var source: Source,
              var url:String?,
              @Column(name = "external-identifier") var externalIdentifier: String,
              @OneToMany(cascade = [CascadeType.ALL]) var metadata: Set<ProjectMetadata>)

@Entity(name = "project_metadata")
class ProjectMetadata(@Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
                      var key:String,
                      var type:String,
                      var value:String)

@Entity(name = "version")
class Version(@Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
              var default: Boolean = true, //in case of Filesystem there is only one "default"-version
              var name:String,
              @ManyToOne var project: Project,
              @ManyToOne var latest_analysis: Analysis? = null,
              @OneToMany(cascade = [CascadeType.ALL]) var metadata: Set<VersionMetadata>)

@Entity(name = "version_metadata")
class VersionMetadata(@Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
                      var key:String,
                      var type:String,
                      var value:String?)

@Entity(name = "analysis")
class Analysis(@Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
               @ManyToOne var version: Version,
               var creationTime: Timestamp)