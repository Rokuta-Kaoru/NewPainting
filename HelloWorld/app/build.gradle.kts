plugins {
    id ("com.android.application")
    id ("kotlin-android")
    id ("com.chaquo.python") version "15.0.1"
    id("com.google.gms.google-services")
}


android {
    namespace = "com.example.helloworld"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.helloworld"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ndk{
            abiFilters += listOf("armeabi-v7a","arm64-v8a", "x86","x86_64")
        }

        buildConfigField("String", "PYTHON_EXECUTABLE", "\"C:\\\\Users\\\\kichi\\\\AppData\\\\Local\\\\Programs\\\\Python\\\\python.exe\"")


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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0" // これを最新のバージョンに更新
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

chaquopy{
    defaultConfig{
        buildPython("\"C:\\Users\\kichi\\AppData\\Local\\Programs\\Python\\pythonw.exe\"")

        pip{
            install("numpy")
            install("pandas")
            install("Pillow")
            install("opencv-python")
        }
    }
}

dependencies {
    // テストおよびデバッグ用依存関係
    testImplementation("junit:junit:4.13.2") // JUnitの追加
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.1") // 最新バージョン
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.1") // 最新バージョン
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.1") // 最新バージョン

    // AndroidX関連の依存関係
    implementation("androidx.activity:activity-compose:1.7.2") // これは最新のバージョンです
    implementation("androidx.compose.ui:ui:1.5.1") // 最新バージョン
    implementation("androidx.compose.ui:ui-graphics:1.5.1") // 最新バージョン
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.1") // 最新バージョン
    implementation("androidx.compose.material3:material3:1.3.0") // 最新バージョン
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1") // 最新バージョンを確認
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // 最新バージョンを使用
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.5.0")

    // Firebase BoMを使用してFirebase依存関係を管理
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-storage-ktx") // Firebase Storage
    implementation ("com.google.firebase:firebase-database-ktx:20.3.0")
    implementation ("com.google.firebase:firebase-storage-ktx:20.2.0")


    // Picasso（画像ライブラリ）
    implementation("com.squareup.picasso:picasso:2.8") // Picasso
}


