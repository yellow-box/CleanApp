pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
//        maven { "https://maven.aliyun.com/repository/google" }
//        maven { "https://maven.aliyun.com/repository/jcenter" }
        google()
        mavenCentral()
    }
}

rootProject.name = "CleanApp"
include(":app")
include(":datalib")
include(":domain")
include(":platformrelated")
include(":domain:libannotation")
include(":domain:libannotation")
