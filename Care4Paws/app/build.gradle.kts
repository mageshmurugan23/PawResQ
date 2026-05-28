plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.shrinjal.care4paws.app"
    compileSdk = 34


        packaging {
            resources {
                excludes.add("META-INF/NOTICE.md")
                excludes.add("META-INF/LICENSE.md")
                excludes.add("META-INF/NOTICE")
                excludes.add("META-INF/LICENSE")
            }
        }



    defaultConfig {
        applicationId = "com.shrinjal.care4paws.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
}

dependencies {
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    // UI
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.activity:activity:1.8.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Firebase AUTH only
    implementation(platform("com.google.firebase:firebase-bom:34.8.0"))
    implementation("com.google.firebase:firebase-auth")

    // Location
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Email (SMTP)
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")
    implementation(libs.firebase.firestore)
    implementation(libs.room.common.jvm)
    implementation(libs.room.runtime.android)

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    implementation("androidx.room:room-runtime:2.5.2")
    annotationProcessor("androidx.room:room-compiler:2.5.2")

    // OPTIONAL (but recommended)
    implementation("androidx.room:room-common:2.6.1")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
