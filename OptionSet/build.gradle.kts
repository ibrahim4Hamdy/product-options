plugins {
    id("com.android.library")
    id("maven-publish")

}
group = "com.github.ibrahim4Hamdy"
version = "0.0.2-alpha"
android {
    namespace = "com.bemo.product.option"
    compileSdk = 34

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))      // << --- ADD This
    }
}
//===============================

java {
    sourceCompatibility = JavaVersion.VERSION_17            // << --- ADD This
    targetCompatibility = JavaVersion.VERSION_17
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.github.ibrahim4Hamdy"
            artifactId = "product-options"
            version = "0.0.2-alpha"
            afterEvaluate {
                from(components["release"])
//                artifact("sourcesJar")
//                artifact("javadocJar")
//                tasks.register("prepareReleaseJar", Jar::class.java) {
//                    from(android.sourceSets["main"].java.srcDirs)
//                    archiveClassifier.set("classes")
//                }
            }

        }

    }

}
dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")

}