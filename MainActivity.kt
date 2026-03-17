

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {super.onCreate(savedInstanceState)
        setContent {
            // This starts your navigation logic
            NavGraph()
        }
    }
}