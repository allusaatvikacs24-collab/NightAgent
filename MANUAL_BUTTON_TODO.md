# Manual Record Button TODO

## Plan Steps:
- [x] Update HomeScreen.kt - replace placeholder "Record Evidence" QuickActionCard with toggle buttons for start/stop
- [x] Add state for isRecording using EvidenceRecorder.isRecording (public var)
- [x] Use Button("Start Recording") / Button("Stop Recording") based on state
- [x] Call EvidenceRecorder.startRecording(LocalContext.current, LocalLifecycleOwner.current), stopRecording(context)

**Target:** HomeScreen.kt (has "Record Evidence" placeholder)
