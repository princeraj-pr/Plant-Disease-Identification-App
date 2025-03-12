# AI-Powered Plant Disease Detection App

## 📌 Project Overview
This project aims to develop an AI-driven Android application that identifies plant diseases using image recognition. The app provides instant diagnoses, treatment recommendations, weather-based alerts, and expert consultations to enhance agricultural productivity.

## 🔍 Key Features
- **AI-Powered Image Recognition**: Uses a deep learning model (EfficientNet-B3 CNN) for disease classification.
- **Instant Diagnosis**: Detects diseases in plants from uploaded images.
- **Treatment Recommendations**: Suggests tailored treatments from a disease database.
- **Real-Time Alerts**: Provides weather-based disease prevention tips.
- **Community & Expert Support**: Connects users with agricultural experts.

## 🛠 Tech Stack
- **Machine Learning**: Python, TensorFlow Lite, EfficientNet-B3 CNN
- **Android Development**: Kotlin, XML
- **Monitoring & Training**: Custom Callback (LR_ASK)
- **UI & Animation**: Material Design Components, Lottie Animation
- **Camera & Image Processing**: CameraX API
- **Networking**: Retrofit with Gson Converter
- **Location Services**: Google Play Services Location API

## 🔑 Permissions Used

The app requires the following permissions to function properly:

- **Storage Access**
  - `android.permission.READ_MEDIA_IMAGES` (For reading media files on Android 13+)
  - `android.permission.READ_EXTERNAL_STORAGE` (For reading media on older Android versions)
  - `android.permission.WRITE_EXTERNAL_STORAGE` *(maxSdkVersion="32")* (For writing media on older Android versions)

- **Camera Access**
  - `android.permission.CAMERA` (For capturing images)

- **Location Services**
  - `android.permission.ACCESS_COARSE_LOCATION` (For approximate location data)
  - `android.permission.ACCESS_FINE_LOCATION` (For precise location data)

- **Internet Access**
  - `android.permission.INTERNET` (For connecting to online services and databases)
 
## 🔧 Requirements

- **Android Version**: 5.0 (Lollipop) and above  
- **Minimum API Level**: 21

## 📺 Demo
https://github.com/user-attachments/assets/fe07f050-3290-4c08-92c3-03825a4eb8df
