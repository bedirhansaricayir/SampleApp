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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "EterationCase"
include(":app")
include(":core:model")
include(":core:common")
include(":core:network")
include(":core:database")
include(":core:data")
include(":core:domain")
include(":core:base")
include(":feature:home")
include(":core:navigation")
include(":feature:detail")
include(":feature:cart")
include(":feature:favorite")
