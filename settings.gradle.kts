import java.net.URI

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    // Можно оставить эту настройку
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Добавь gradlePluginPortal() если его нет, но обычно это не главное
        gradlePluginPortal()
        maven { url = URI.create("https://jitpack.io") }
    }
}

rootProject.name = "RakhimovaKP"
include(":app")
 