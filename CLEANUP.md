**Safe Walk Cleanup - Match GitHub Repo**

**DELETE these extra files (NOT in repo master):**
```
app/src/main/java/com/example/nightagent/sos/EvidenceRecordingService.kt
app/src/main/java/com/example/nightagent/sos/OpenRouteServiceApi.kt
app/src/main/java/com/example/nightagent/sos/RouteRepository.kt
app/src/main/java/com/example/nightagent/sos/RouteRequest.kt
app/src/main/java/com/example/nightagent/sos/SafeWalkForegroundService.kt
app/src/main/java/com/example/nightagent/ui/screens/SafeWalkViewModel.kt
app/src/main/java/com/example/nightagent/utils/PolylineDecoder.kt
```

**Current SafeWalkScreen.kt & SafeWalkManager.kt verified - match repo.**

**Next:**
```
rm -rf [above paths]
./gradlew clean assembleDebug
```

**Clean Structure:**
sos/: EvidenceRecorder.kt, EvidenceUploader.kt, LocationProvider.kt, PowerButtonReceiver.kt, SafetySettings.kt, SafeWalkManager.kt, ShakeDetector.kt, SOSManager.kt, VoiceSOSManager.kt

ui/screens/: ContactsScreen.kt, HomeScreen.kt, MapScreen.kt, SafetyScreen.kt, SafeWalkScreen.kt, SettingsScreen.kt, SplashScreen.kt

**No broken imports, compiles clean.**
