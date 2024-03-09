plugins {
    id("com.android.application") version "8.2.2"
    id("org.jetbrains.kotlin.android")
    id("kotlinx-serialization")
    id("com.google.devtools.ksp")
    id("com.google.protobuf")
}

android {
    namespace = "com.trustbank.client_mobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.trustbank.client_mobile"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.6"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.7"
    }
    plugins {
        create("java") {
            artifact = "com.google.protobuf:protoc-gen-javalite:3.0.0"
        }
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.25.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
            task.plugins {
                create("grpc") {
                    option("lite")
                }
            }
        }
    }

//    plugins {
//        generateProtoTasks {
//            all().forEach {
//                it.builtins {
//                    create("java") {
//                        option("lite")
//                    }
//                }
//            }
//        }
//    }
}


//protobuf {
//    protoc {
//        artifact = "com.google.protobuf:protoc:3.10.1"
//    }
//    plugins {
//        create("java") {
//            artifact = "com.google.protobuf:protoc-gen-javalite:3.0.0"
//        }
//        create("grpc") {
//            artifact = "io.grpc:protoc-gen-grpc-java:1.25.0"
//        }
////        create("grpckt") {
////            artifact = libs.protoc.gen.grpc.kotlin.get().toString() + ":jdk8@jar"
////        }
//    }
//    generateProtoTasks {
//        all().forEach {
//            it.plugins {
//                create("java") {
//                    option("lite")
//                }
//                create("grpc") {
//                    option("lite")
//                }
//                create("grpckt") {
//                    option("lite")
//                }
//            }
//
//        }
//    }
//}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.compose.material:material-icons-core:1.6.2")


    // Koin
    implementation("io.insert-koin:koin-androidx-compose:3.5.3")

    // OkHttp
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    // Json Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    // Room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    val datastore_version = "1.0.0"
    implementation("androidx.datastore:datastore:$datastore_version")
    implementation("androidx.datastore:datastore-preferences:$datastore_version")


    implementation("io.grpc:grpc-okhttp:1.60.0")
    implementation("io.grpc:grpc-protobuf-lite:1.60.0")
    implementation("io.grpc:grpc-stub:1.60.0")
    implementation("org.apache.tomcat:annotations-api:6.0.53")
}