# Exploration of Android Services

This project was developed as part of an Android course, with the goal of exploring Android Services and understanding their practical use. The code and experiments here are intended as a foundation for future, larger projects that may require background processing, inter-component communication, or persistent data handling.

## What does this project do?

This project demonstrates:
- **Foreground and bound services**: How to create, start, bind, and interact with Android services.
- **Data sharing between Activity and Service**: Sending data from the UI to a background service and retrieving it back.
- **Notification integration**: Running a service in the foreground with a persistent notification.
- **Service lifecycle management**: Starting, binding, unbinding, and stopping services from an Activity.

The app provides a simple UI to:
- Send data to a background service.
- Retrieve (pull) the latest data stored in the service.
- Stop (kill) the service.

**NOTE:** The notification part of the exploration was done during experiment and is not complete.

## Why is this interesting and useful?

Android Services are essential for:
- Running long-running operations in the background (e.g., downloads, music playback, data sync).
- Keeping processes alive even when the user is not interacting with the app, a TCP connection for example.
- Enabling communication between different parts of an app (or even different apps).

Understanding how to implement and manage services is crucial for building robust, responsive, and user-friendly Android applications. This project provides a minimal, clear example of these concepts, which can be adapted and extended for more complex needs.

## How does it work?

### Main Components

- [`MainActivity`](app/src/main/java/com/example/exploration_services/MainActivity.java):  
  The main UI where users can input data, send it to the service, retrieve it, and stop the service.

- [`DataService`](app/src/main/java/com/example/exploration_services/DataService.java):  
  A foreground service that stores a string of data, exposes methods to set/get this data, and shows a notification while running.

### How to use the app

1. **Start the app**:  
   The service is started and bound automatically when the app launches.

2. **Send data to the service**:  
   - Enter text in the input field.
   - Press "Send Data" to push the data to the service.

2. **At this point you can exit the App**:  
   - Go back to the Android Home.

3. **Retrieve data from the service**:  
   - Press "Pull Data" to fetch the latest data stored in the service.

4. **Stop the service**:  
   - Press "Kill the service" to stop the background service.

### How to run/build the project

1. **Clone the repository** and open it in Android Studio or VS Code with the appropriate extensions.
2. **Build the project** using Gradle:
   ```sh
   ./gradlew build
   ```
3. **Run the app** on an emulator or physical device with:
   ```sh
   ./gradlew installDebug
   ```
   Or use the IDE's "Run" button.

### Key files and structure

- [`app/src/main/java/com/example/exploration_services/MainActivity.java`](app/src/main/java/com/example/exploration_services/MainActivity.java)
- [`app/src/main/java/com/example/exploration_services/DataService.java`](app/src/main/java/com/example/exploration_services/DataService.java)
- [`app/src/main/res/layout/activity_main.xml`](app/src/main/res/layout/activity_main.xml)
- [`app/src/main/AndroidManifest.xml`](app/src/main/AndroidManifest.xml)

### Extending this project

You can use this project as a template for:
- Adding more complex data handling in the service.
- Implementing communication with multiple activities or fragments.
- Integrating with remote APIs or databases in the background.
- Experimenting with different types of services (IntentService, JobIntentService, etc.).

## References

- [Android Services documentation](https://developer.android.com/guide/components/services)
- [Foreground services](https://developer.android.com/guide/components/foreground-services)
- [Bound services](https://developer.android.com/guide/components/bound-services)

---

Feel free to adapt or extend this project for your own experiments.