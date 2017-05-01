#!/usr/bin/env bash
echo "Building..."
./gradlew assembleDebug --configure-on-demand --daemon
for SERIAL in $(adb devices | grep -v List | cut -f 1);
    do
        DEVICE=$(adb -s $SERIAL shell getprop ro.product.model)
        echo "Installing On $DEVICE"
        adb -s $SERIAL install -r app/build/outputs/apk/app-debug.apk
        echo "Launching On $DEVICE"
        adb -s $SERIAL shell am start -n "com.github.abhrp.pixabaysearchdemo/com.github.abhrp.pixabaysearchdemo.activity.PixabaySearchActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
    done