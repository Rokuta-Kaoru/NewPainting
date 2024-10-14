pluginManagement {
    repositories {
        //google {
          //  content {
            //    includeGroupByRegex("com\\.android.*")
              //  includeGroupByRegex("com\\.google.*")
                //includeGroupByRegex("androidx.*")
            //}
        //}
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.android.application") {
                useModule("com.android.tools.build:gradle:8.4.1")
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {url =uri("https://jitpack.io")}
    }
}

rootProject.name = "HelloWorld"
include(":app")
 