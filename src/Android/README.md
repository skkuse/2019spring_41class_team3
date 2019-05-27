# Frontend (Android app)

The frontend app is built by an Android Studio project.

## Open Source
- TBA

### Debugging

1. Install the server to local machine (refer to [instructions for installing the server](../backend/README.md))
2. Open this folder as an Android Studio project
3. Set the build variant to *debug*
4. Reverse forward server port from deployment target to local server by running `adb reverse tcp:3000 tcp:3000` 
5. Build and run application

### Production

1. Open this folder as an Android Studio project
2. Set the build variant to *release*
3. Build and run application