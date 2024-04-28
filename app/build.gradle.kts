plugins {
    id("com.android.application")
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.example.seniorsync2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.seniorsync2"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("io.github.medyo:android-about-page:2.0.0")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.0.3")
    implementation ("com.mikhaellopez:circularimageview:4.3.1")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.work:work-runtime:2.9.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("androidx.appcompat:appcompat:1.2.0")
    implementation ("androidx.gridlayout:gridlayout:1.0.0")

    androidTestImplementation("com.android.support.test.espresso:espresso-core:2.2.2") {
        exclude(group = "com.android.support", module = "support-annotations")
    }

    implementation ("com.google.firebase:firebase-database:20.3.0")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.19")

}