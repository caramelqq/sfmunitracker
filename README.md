SF Muni Bus Tracker app

This is an Android app written in Java that displays the routes and the GPS locations of the busses of SF Muni.

It is built using the Android architecture components (Activity, ViewModel, Repository, Room, DAO) and also uses Retrofit to pull the route and vehicle data from NextBus's rest api.  It might take a little bit for all the routes to load on the first run because the app needs to download and cache the data first.  
