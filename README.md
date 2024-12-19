
# TalkSense

TalkSense is an Android application that listens to environmental sounds and adjusts the volume of music based on detected noise levels. This application is particularly useful for environments where background noise needs to be minimized, such as when a conversation is taking place.

## Features

- Monitors audio from the microphone.
- Detects human speech using pitch detection.
- Reduces volume when speech is detected.
- Restores volume after a short silence period.
- Runs in the background as a foreground service with notifications.
- Designed for efficient power consumption.

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/MatinDehghanian/talksense.git
   ```

2. Open the project in Android Studio.

3. Install the required dependencies.

4. Run the app on an Android device.

## Usage

The app will start running automatically and monitor the microphone for speech. When speech is detected, it will lower the volume of the music or audio playing on the device.

### Permissions

The app requires the following permissions:

- Microphone
- Internet (for downloading necessary resources)

## Contributing

Feel free to fork the repository, make changes, and create a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

- [TarsosDSP](https://github.com/JorenSix/TarsosDSP) for pitch detection functionality.
