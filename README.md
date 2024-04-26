**Instructions to run the project**

1. Add local.properties file in project root folder if the file is not exist after cloning
2. Add sdk.dir path in local.properties
3. Once local.properties is added, click -> Make Project in Build menu
4. Click Add configuration -> Add new -> Android App
5. Name it as 'app' instead of 'Unnamed'
6. Select Assessment.app.main in General -> Module
7. Click Apply
8. Click OK
9. Run the project
10. The project will run between Nougat (Android 7.0) to Upside Down Cake (Android 14)



**Notes**:
* No third party library is used for loading the images
* In-Memory Cache is implemented
* Disk Cache is implemented
* If in-memory cache is not available, then disk cache will fetch the images and store them into in-memory cache
* Error handling is implemented
* Project is developed in Kotlin & Compose UI
* This project is tested and working in android studio latest version android-studio-2024.1.1.4-windows