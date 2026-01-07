import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

class CommonPomConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.afterEvaluate {
            val publishing = project.extensions.findByType(PublishingExtension::class.java)
            publishing?.publications?.withType(MavenPublication::class.java)?.configureEach {
                pom {
                    url.set("https://github.com/Rallista/maplibre-compose-playground")
                    inceptionYear.set("2023")
                    
                    licenses {
                        license {
                            name.set("MPL-2.0")
                            url.set("https://www.mozilla.org/en-US/MPL/2.0/")
                        }
                    }
                    
                    developers {
                        developer {
                            name.set("Jacob Fielding")
                            organization.set("Rallista")
                            organizationUrl.set("https://rallista.app")
                        }
                        developer {
                            name.set("Ian Wagner")
                            organization.set("Stadia Maps")
                            organizationUrl.set("https://stadiamaps.com/")
                        }
                    }
                    
                    contributors {
                        contributor {
                            name.set("Ramani Maps")
                            organizationUrl.set("https://github.com/ramani-maps/ramani-maps")
                        }
                    }
                    
                    scm {
                        connection.set("scm:git:https://github.com/Rallista/maplibre-compose-playground.git")
                        developerConnection.set("scm:git:ssh://github.com/Rallista/maplibre-compose-playground.git")
                        url.set("https://github.com/Rallista/maplibre-compose-playground")
                    }
                    
                    withXml {
                        val rootNode = asNode()
                        val repositoriesNode = rootNode.appendNode("repositories")
                        val repositoryNode = repositoriesNode.appendNode("repository")
                        repositoryNode.appendNode("name", "Google")
                        repositoryNode.appendNode("id", "google")
                        repositoryNode.appendNode("url", "https://maven.google.com/")
                    }
                }
            }
        }
    }
}
