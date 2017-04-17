# PopularMovies

Android App, which displays movie data, based on sorting criteria. It utilizes the [The Movie DB API](https://www.themoviedb.org/documentation/api) to query data using Retrofit as a REST-Client.
The user can favorite movies, which are then stored with a Content-Provider backed by a SQLite Database.

## Download
As of April 17th 2016, the app is available on the [Play Store for download](https://play.google.com/store/apps/details?id=de.aaronoe.cinematic)

## Video
![In-App Experience](https://github.com/aaronoe/screenshots/blob/master/Cinematic_long.gif?raw=true "In-App Experience")

## Purpose

This project is part of [Udacity's and Google's Associate Android Developer Fast-Track Program](https://www.udacity.com/course/associate-android-developer-fast-track--nd818)
The purpose is to design this app from the ground up, bringing it from an idea to a functional state. However it's not close to a production ready state yet.

## External Libraries

- Retrofit2 as a REST-Client
- GSON for converting the JSON-Response to Java Objects
- Picasso for image loading
- Butterknife for binding views

## License

This project utilizes the [MIT License](https://github.com/aaronoe/space_launch_manifest/blob/master/LICENSE.md "Project License")
