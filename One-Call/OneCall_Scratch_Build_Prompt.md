# OneCall — Android App Build Prompt (From Scratch)

> **Ek SIM, Ek Secondary Device — Bluetooth Se Connected**
> Stack: Android · Kotlin · Bluetooth HFP · RFCOMM · Room DB

---

## ROLE & OBJECTIVE

Tu ek senior Android developer hai. Tera kaam hai **OneCall** naam ka ek Android app banana — **bilkul scratch se** — jo ek phone ka SIM call doosre phone pe Bluetooth ke through le jaaye.

Koi external server nahi. Koi internet nahi. Poora system local Bluetooth connection pe kaam karega.

---

## CONCEPT — EK LINE MEIN

Main device (jisme SIM hai) apne aap ko ek **Bluetooth headset** ban jaata hai. Secondary device us "headset" se connect ho jaata hai. Jab SIM call aaye — ringtone dono pe bajti hai, koi bhi uthaa sakta hai, audio earpiece se aata hai.

---

## TECH STACK

| Layer | Technology |
|---|---|
| Language | Kotlin |
| Min SDK | API 26 (Android 8.0) |
| Target SDK | API 34 |
| Architecture | MVVM + Repository Pattern |
| Database | Room (call history, device prefs) |
| Async | Kotlin Coroutines + Flow |
| UI | ViewBinding (no Jetpack Compose) |
| Navigation | Navigation Component (Single Activity) |
| Bluetooth Audio | Bluetooth HFP (Hands-Free Profile) |
| Custom Signaling | Bluetooth RFCOMM Socket (JSON messages) |
| DI | Manual (no Hilt/Dagger needed) |

---

## CORE CONCEPT — BLUETOOTH HFP

### Device Roles

```
Main Device  →  SIM wala phone  →  HFP Audio Gateway (AG) role
Secondary    →  Doosra phone    →  HFP Hands-Free Unit (HF) role
```

### Kyun HFP?

Android OS non-rooted apps ko SIM call ka audio capture karne NAHI deta (Android 10+).
Lekin agar secondary device ek Bluetooth headset ki tarah behave kare, toh Android OS
**khud** SIM call ka audio Bluetooth pe route kar deta hai — app ko kuch nahi karna padta.

### RFCOMM Kyu?

HFP audio OS handle karta hai. Lekin custom signals ke liye (ring notification, call accept/decline, transfer, outgoing call request) ek alag RFCOMM socket channel use hoga jisme JSON messages bheji jaayengi.

---

## PERMISSIONS (AndroidManifest.xml)

### Required — Hamesha maango

```xml
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />   <!-- API 31+ -->
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" />      <!-- API 31+ -->
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.ANSWER_PHONE_CALLS" />
<uses-permission android:name="android.permission.CALL_PHONE" />
<uses-permission android:name="android.permission.MANAGE_OWN_CALLS" />
```

### Optional — Runtime pe maango, deny pe feature disable karo

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />    <!-- BT scan, API 29/30 -->
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.READ_CALL_LOG" />
<uses-permission android:name="android.permission.WRITE_CALL_LOG" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
```

### NEVER Add (Forbidden)

```
INTERNET, ACCESS_WIFI_STATE, ACCESS_NETWORK_STATE
```
Koi Wi-Fi ya internet permission nahi — yeh fully offline app hai.

---

## PROJECT STRUCTURE

```
app/
└── src/main/
    ├── java/com/onecall/
    │   │
    │   ├── core/                        # Core Bluetooth engine
    │   │   ├── BluetoothManager.kt      # BT adapter, scan, pair, connect
    │   │   ├── BluetoothHfpController.kt # HFP AG (main) + HF (secondary) logic
    │   │   ├── RfcommSignalingService.kt # JSON signaling over RFCOMM
    │   │   ├── BluetoothCallBridge.kt   # SIM call detect → RFCOMM ring bhejo
    │   │   └── BluetoothDevicePreference.kt # SharedPrefs mein device info save
    │   │
    │   ├── service/
    │   │   └── OneCallService.kt        # Foreground service — sab alive rakho
    │   │
    │   ├── receiver/
    │   │   ├── CallReceiver.kt          # TelephonyManager broadcast
    │   │   └── BootReceiver.kt          # RECEIVE_BOOT_COMPLETED
    │   │
    │   ├── data/
    │   │   ├── db/
    │   │   │   ├── OneCallDatabase.kt   # Room DB
    │   │   │   ├── CallHistoryDao.kt
    │   │   │   └── entities/
    │   │   │       └── CallHistoryEntity.kt
    │   │   └── repository/
    │   │       └── CallHistoryRepository.kt
    │   │
    │   ├── ui/
    │   │   ├── MainActivity.kt          # Single Activity host
    │   │   ├── welcome/
    │   │   │   └── WelcomeFragment.kt   # App entry — Main ya Secondary choose karo
    │   │   ├── setup/
    │   │   │   ├── MainSetupFragment.kt # Main device: BT discoverable + pair
    │   │   │   └── SecondarySetupFragment.kt # Secondary: BT scan + connect
    │   │   ├── permissions/
    │   │   │   └── PermissionsFragment.kt
    │   │   ├── tutorial/
    │   │   │   └── TutorialFragment.kt  # 5 onboarding cards
    │   │   ├── dashboard/
    │   │   │   ├── MainDashboardFragment.kt
    │   │   │   └── SecondaryDashboardFragment.kt
    │   │   ├── call/
    │   │   │   ├── IncomingCallActivity.kt  # Full screen incoming call
    │   │   │   └── ActiveCallActivity.kt   # Active call controls
    │   │   ├── dialpad/
    │   │   │   └── DialpadFragment.kt
    │   │   ├── contacts/
    │   │   │   └── ContactsFragment.kt
    │   │   ├── history/
    │   │   │   ├── CallHistoryFragment.kt
    │   │   │   └── DeviceHistoryFragment.kt
    │   │   ├── settings/
    │   │   │   ├── SettingsFragment.kt
    │   │   │   └── DeviceSettingsFragment.kt
    │   │   └── privacy/
    │   │       └── PrivacyPolicyFragment.kt
    │   │
    │   └── model/
    │       ├── DeviceMode.kt            # enum: MAIN / SECONDARY
    │       ├── ConnectionState.kt       # enum: CONNECTED / DISCONNECTED / CONNECTING
    │       └── RfcommMessage.kt         # data class for JSON signals
    │
    └── res/
        ├── layout/                      # XML layouts
        ├── navigation/nav_graph.xml
        ├── values/strings.xml
        └── drawable/
```

---

## CORE FILES — DETAILED IMPLEMENTATION

### 1. RfcommMessage.kt — Signaling Protocol

```kotlin
// All RFCOMM messages use this structure
data class RfcommMessage(
    val type: String,
    val number: String? = null,
    val name: String? = null,
    val targetNumber: String? = null,
    val callerName: String? = null,
    val callerNumber: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

object MessageType {
    const val RING_START              = "RING_START"
    const val RING_STOP               = "RING_STOP"
    const val CALL_ACCEPTED_MAIN      = "CALL_ACCEPTED_MAIN"
    const val CALL_ACCEPTED_SECONDARY = "CALL_ACCEPTED_SECONDARY"
    const val CALL_ENDED              = "CALL_ENDED"
    const val TRANSFER_REQUEST        = "TRANSFER_REQUEST"
    const val TRANSFER_ACCEPTED       = "TRANSFER_ACCEPTED"
    const val TRANSFER_REJECTED       = "TRANSFER_REJECTED"
    const val OUTGOING_REQUEST        = "OUTGOING_REQUEST"
    const val OUTGOING_ALLOWED        = "OUTGOING_ALLOWED"
    const val OUTGOING_BLOCKED        = "OUTGOING_BLOCKED"
    const val HISTORY_SYNC            = "HISTORY_SYNC"
}
```

### 2. BluetoothManager.kt — Core BT Controller

```kotlin
class BluetoothManager(private val context: Context) {

    private val adapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    fun isBluetoothEnabled(): Boolean = adapter?.isEnabled == true

    fun startDiscovery() { adapter?.startDiscovery() }

    fun stopDiscovery() { adapter?.cancelDiscovery() }

    fun makeDiscoverable(durationSeconds: Int = 60) {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, durationSeconds)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun getBondedDevices(): Set<BluetoothDevice> =
        adapter?.bondedDevices ?: emptySet()

    fun isPaired(device: BluetoothDevice): Boolean =
        device.bondState == BluetoothDevice.BOND_BONDED
}
```

### 3. BluetoothHfpController.kt — HFP Profile Manager

```kotlin
class BluetoothHfpController(
    private val context: Context,
    private val mode: DeviceMode,  // MAIN or SECONDARY
    private val onConnected: (BluetoothDevice) -> Unit,
    private val onDisconnected: () -> Unit,
    private val onAudioConnected: () -> Unit,
    private val onAudioDisconnected: () -> Unit
) {
    private var headsetProxy: BluetoothHeadset? = null
    private var hfpClientProxy: Any? = null  // BluetoothHfpClient (hidden API)

    private val serviceListener = object : BluetoothProfile.ServiceListener {
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
            if (mode == DeviceMode.MAIN && profile == BluetoothProfile.HEADSET) {
                headsetProxy = proxy as BluetoothHeadset
            }
            // Secondary: HFP_CLIENT profile
        }
        override fun onServiceDisconnected(profile: Int) {
            headsetProxy = null
        }
    }

    fun initialize() {
        val profileType = if (mode == DeviceMode.MAIN)
            BluetoothProfile.HEADSET else 11  // HFP_CLIENT = 11
        BluetoothAdapter.getDefaultAdapter()
            ?.getProfileProxy(context, serviceListener, profileType)

        registerBroadcastReceiver()
    }

    private fun registerBroadcastReceiver() {
        val filter = IntentFilter().apply {
            addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)
            addAction(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED)
        }
        context.registerReceiver(hfpReceiver, filter)
    }

    private val hfpReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED -> {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    val state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1)
                    if (state == BluetoothProfile.STATE_CONNECTED && device != null)
                        onConnected(device)
                    else if (state == BluetoothProfile.STATE_DISCONNECTED)
                        onDisconnected()
                }
                BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED -> {
                    val state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1)
                    if (state == BluetoothHeadset.STATE_AUDIO_CONNECTED) onAudioConnected()
                    else if (state == BluetoothHeadset.STATE_AUDIO_DISCONNECTED) onAudioDisconnected()
                }
            }
        }
    }
}
```

### 4. RfcommSignalingService.kt — JSON Messaging over Bluetooth

```kotlin
class RfcommSignalingService(
    private val context: Context,
    private val mode: DeviceMode,
    private val onMessage: (RfcommMessage) -> Unit
) {
    companion object {
        val SERVICE_UUID: UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66")
    }

    private var serverSocket: BluetoothServerSocket? = null
    private var clientSocket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null
    private val gson = Gson()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun startServer() {  // Main device
        scope.launch {
            try {
                serverSocket = BluetoothAdapter.getDefaultAdapter()
                    ?.listenUsingRfcommWithServiceRecord("OneCall", SERVICE_UUID)
                val socket = serverSocket?.accept()
                clientSocket = socket
                outputStream = socket?.outputStream
                listenForMessages(socket?.inputStream)
            } catch (e: IOException) {
                retryWithBackoff { startServer() }
            }
        }
    }

    fun connectToServer(device: BluetoothDevice) {  // Secondary device
        scope.launch {
            try {
                BluetoothAdapter.getDefaultAdapter()?.cancelDiscovery()
                val socket = device.createRfcommSocketToServiceRecord(SERVICE_UUID)
                socket.connect()
                clientSocket = socket
                outputStream = socket.outputStream
                listenForMessages(socket.inputStream)
            } catch (e: IOException) {
                retryWithBackoff { connectToServer(device) }
            }
        }
    }

    fun sendMessage(message: RfcommMessage) {
        scope.launch {
            try {
                val json = gson.toJson(message) + "\n"
                outputStream?.write(json.toByteArray())
            } catch (e: IOException) {
                // Handle send failure
            }
        }
    }

    private fun listenForMessages(inputStream: InputStream?) {
        scope.launch {
            val reader = BufferedReader(InputStreamReader(inputStream))
            try {
                while (true) {
                    val line = reader.readLine() ?: break
                    val message = gson.fromJson(line, RfcommMessage::class.java)
                    withContext(Dispatchers.Main) { onMessage(message) }
                }
            } catch (e: IOException) {
                retryWithBackoff { /* reconnect */ }
            }
        }
    }

    private suspend fun retryWithBackoff(block: suspend () -> Unit) {
        var delay = 2000L
        repeat(5) {
            delay(delay)
            try { block() ; return } catch (_: Exception) {}
            delay = (delay * 2).coerceAtMost(30_000L)
        }
    }
}
```

### 5. BluetoothCallBridge.kt — SIM Call Detection

```kotlin
class BluetoothCallBridge(
    private val context: Context,
    private val rfcomm: RfcommSignalingService
) {
    private val telephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    private val callStateListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            when (state) {
                TelephonyManager.CALL_STATE_RINGING -> {
                    val name = resolveContactName(phoneNumber)
                    rfcomm.sendMessage(RfcommMessage(
                        type = MessageType.RING_START,
                        number = phoneNumber,
                        name = name
                    ))
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    rfcomm.sendMessage(RfcommMessage(type = MessageType.CALL_ACCEPTED_MAIN))
                }
                TelephonyManager.CALL_STATE_IDLE -> {
                    rfcomm.sendMessage(RfcommMessage(type = MessageType.CALL_ENDED))
                }
            }
        }
    }

    fun startListening() {
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    fun stopListening() {
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_NONE)
    }

    private fun resolveContactName(number: String?): String? {
        if (number == null) return null
        // ContactsContract lookup — return display name or null
        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
        context.contentResolver.query(uri,
            arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME), null, null, null)
            ?.use { cursor ->
                if (cursor.moveToFirst())
                    return cursor.getString(0)
            }
        return null
    }
}
```

### 6. OneCallService.kt — Foreground Service

```kotlin
class OneCallService : Service() {

    private lateinit var btManager: BluetoothManager
    private lateinit var hfpController: BluetoothHfpController
    private lateinit var rfcommService: RfcommSignalingService
    private lateinit var callBridge: BluetoothCallBridge
    private lateinit var devicePrefs: BluetoothDevicePreference

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, buildNotification("OneCall active"))

        val mode = devicePrefs.getDeviceMode()  // MAIN or SECONDARY

        rfcommService = RfcommSignalingService(this, mode) { message ->
            handleRfcommMessage(message)
        }

        hfpController = BluetoothHfpController(
            context = this, mode = mode,
            onConnected = { device -> updateNotification("Connected: ${device.name}") },
            onDisconnected = { updateNotification("Disconnected") },
            onAudioConnected = { /* HFP audio active */ },
            onAudioDisconnected = { /* Audio ended */ }
        )

        if (mode == DeviceMode.MAIN) {
            callBridge = BluetoothCallBridge(this, rfcommService)
            callBridge.startListening()
            rfcommService.startServer()
        } else {
            val pairedDevice = devicePrefs.getPairedDevice()
            pairedDevice?.let { rfcommService.connectToServer(it) }
        }

        hfpController.initialize()
        return START_STICKY
    }

    private fun handleRfcommMessage(message: RfcommMessage) {
        when (message.type) {
            MessageType.RING_START -> launchIncomingCall(message)
            MessageType.RING_STOP, MessageType.CALL_ACCEPTED_MAIN -> dismissIncomingCall()
            MessageType.CALL_ENDED -> endActiveCall()
            MessageType.TRANSFER_REQUEST -> showTransferNotification(message)
            MessageType.OUTGOING_REQUEST -> showOutgoingApprovalDialog(message)
            MessageType.HISTORY_SYNC -> saveSessionHistory(message)
        }
    }

    override fun onBind(intent: Intent?) = null
}
```

---

## ALL SCREENS — BUILD SPECIFICATION

### Screen 1: Welcome Screen

**Purpose:** App pehli baar kholne pe — User choose kare Main ya Secondary

**Layout:**
- App logo (top center)
- Tagline: *"Ek SIM, Ek Device — Bluetooth Se"*
- [Main Device Setup] — primary button (SIM wala phone)
- [Join as Secondary Device] — secondary button
- [Tutorial] — text link (bottom)

**Logic:** SharedPrefs mein `device_mode` check karo. Agar already set hai → direct Dashboard pe jao.

---

### Screen 2: Permissions Screen

**Purpose:** Zaroori permissions explain karo aur maango

**Two groups:**

**REQUIRED (app kaam nahi karega agar denied):**
- Bluetooth (basic access)
- Nearby Devices (scan + connect — Android 12+)
- Phone (incoming call detect)
- Microphone (secondary device pe audio)
- Answer Calls (accept/decline)
- Make Calls (outgoing)

**OPTIONAL (feature disable hoga agar denied):**
- Location (Bluetooth scan, Android 10/11 only) — note: *"Sirf Android 10-11 pe zaroori"*
- Contacts (caller naam dikhana)
- Call Log (history save karna)
- Notifications
- Background Activity (auto-start on boot)

**Rules:**
- Required permission denied → red warning: *"Yeh permission zaroor chahiye — app nahi chalega"*
- Optional denied → grey info: *"Yeh feature disabled rahega"*
- [Grant All] button + individual grant buttons
- Skip option (optional permissions ke liye)
- App crash NEVER hona chahiye permission deny pe

---

### Screen 3A: Main Device Setup Screen

**Purpose:** Main device ko discoverable banao, secondary se pair karo

**Layout:**
```
[Bluetooth icon] Bluetooth: ON / OFF

━━━━━━━━━━━━━━━━━━━━━━━━━
     🔵 Pair Secondary Device
━━━━━━━━━━━━━━━━━━━━━━━━━

         [Animated pulse when discoverable]
         60 seconds remaining...
         "Secondary device pe OneCall kholo
          aur 'Connect to Main Device' select karo"

───────────────────────────────────
Paired Device:
[ 📱 Rohan's Phone    ✅ Connected ]
[ Forget Device ]

───────────────────────────────────
         [Continue to Dashboard →]
```

**Logic:**
- [Pair Secondary Device] tap → `makeDiscoverable(60)` call karo
- Discoverable hone pe animated pulse + countdown timer start karo
- Paired device hai toh card dikhao — auto-reconnect try karo on load
- [Forget Device] → `removeBond()` + SharedPrefs clear

---

### Screen 3B: Secondary Device Setup Screen

**Purpose:** Secondary device main se connect karo

**Layout:**
```
[Bluetooth icon] Bluetooth: ON / OFF

━━━━━━━━━━━━━━━━━━━━━━━━━
     🔍 Scan for Main Device
━━━━━━━━━━━━━━━━━━━━━━━━━

  Nearby devices:
  ┌──────────────────────────────────┐
  │ 📱 Rahul's Pixel 8     [Connect] │
  │ 💻 Anuj's Samsung       [Connect]│
  └──────────────────────────────────┘
  Note: "Main device ka naam select karo"

  ─── OR ───

  [ Already paired hai — Auto connect ho raha hai... ]
```

**Logic:**
- Scan → `BluetoothAdapter.startDiscovery()` → nearby devices list
- OneCall app hai ki nahi → RFCOMM UUID se identify karo (jitna possible ho)
- Device select → Android standard pairing dialog
- Paired → HFP + RFCOMM connect → "Connected to [Name]" screen

---

### Screen 4: Tutorial Screen (5 Cards — Swipeable)

| Card | Title | Content |
|---|---|---|
| 1 | Welcome | OneCall kya karta hai — 2 lines |
| 2 | Main Device | "SIM wale phone pe Bluetooth ON karo. Secondary device ko pair karo." |
| 3 | Connect | "Secondary pe OneCall kholo → Scan → Pair karo" |
| 4 | Calls | "SIM call aaye → dono pe ring bajegi → koi bhi uthaa sakta hai" |
| 5 | Ready! | "Setup complete — dashboard pe jao" |

Skip button hamesha visible. Last card pe [Get Started] button.

---

### Screen 5A: Main Dashboard

```
OneCall                           🔔

SIM: Active  📶

──── Connected Device ────────────────
 📱 Rohan's Phone
 🔵 Bluetooth  ● Connected

 [Stop Calls]    [Disconnect]
──────────────────────────────────────

Bottom Nav: [ Dialpad | Contacts | History | Settings ]
```

**Device Card States:**
- Green dot = Connected
- Gray dot = Disconnected → [Reconnect] button dikhao
- [Stop Calls] = Calls is device pe na jaye but BT connected rahe
- [Disconnect] = HFP disconnect karo

---

### Screen 5B: Secondary Dashboard

```
OneCall                           🔔

Connected to: Rahul's iPhone
🔵 Bluetooth  ●●●○ signal

Bottom Nav: [ Dialpad | Contacts | History | Settings ]
```

---

### Screen 6: Incoming Call Screen (Full Screen Activity)

**Trigger:**
- Main device: `TelephonyManager.CALL_STATE_RINGING` detect hone pe
- Secondary device: RFCOMM `RING_START` message receive hone pe

**Layout:**
```
━━━━━━━━━━━━━━━━━━━━━━━━━━
   📞 Incoming Call

   Rahul Sharma
   +91 98765 43210

   Ringing on 2 devices

━━━━━━━━━━━━━━━━━━━━━━━━━━

   [🔴 DECLINE]    [🟢 ACCEPT]
━━━━━━━━━━━━━━━━━━━━━━━━━━
```

**Logic:**
- Accept on Secondary → RFCOMM `CALL_ACCEPTED_SECONDARY` → Main ring band → HFP audio active on secondary
- Accept on Main → RFCOMM `CALL_ACCEPTED_MAIN` → Secondary ring band → Earpiece mode
- Decline on any → RFCOMM `RING_STOP` → `TelecomManager.endCall()` → Both screens dismiss
- **Special Rule:** Agar secondary connected hai → main device pe IncomingCallActivity silently nahi aayegi. Sirf secondary connected nahi toh main pe full screen aayega.

**CRITICAL — AUDIO RULE:**
```kotlin
// KABHI MAT KARO
// audioManager.isSpeakerphoneOn = true  ← FORBIDDEN

// HAMESHA YEH KARO
audioManager.mode = AudioManager.MODE_IN_CALL
// HFP handle karta hai rest — OS routes audio to BT
```

---

### Screen 7: Active Call Screen

**Layout:**
```
━━━━━━━━━━━━━━━━━━━━━━━━━━
   📞 In Call

   Rahul Sharma
   +91 98765 43210

   00:02:34

━━━━━━━━━━━━━━━━━━━━━━━━━━
  [🎤 Mute]  [🔊 Speaker]  [⇄ Transfer]

  [🔴 End Call]
━━━━━━━━━━━━━━━━━━━━━━━━━━
```

**Logic:**
- End Call → RFCOMM `CALL_ENDED` + `TelecomManager.endCall()` → HISTORY_SYNC bhejo
- Transfer → Bottom sheet: secondary device naam dikhao → Select → `TRANSFER_REQUEST` bhejo
  - Accept response: HFP audio shift → Active Call screen secondary pe
  - Reject response: Toast "Transfer rejected" → Call same device pe rahe
- Mute → `AudioManager.setMicrophoneMute(true)`
- Speaker → `AudioManager.isSpeakerphoneOn = true` (sirf manually request pe, default OFF)

---

### Screen 8: Dialpad (Both Devices)

**Main device:** Standard dialpad → `CALL_PHONE` directly

**Secondary device:** Standard dialpad → RFCOMM `OUTGOING_REQUEST {number}` → Main pe approval popup

**Main device popup (secondary se outgoing request):**
```
"[Rohan's Phone] call karna chahta hai:
 +91 98765 43210 (Rahul Sharma)"

[Block]    [Allow]
```
- Allow → `OUTGOING_ALLOWED` → Main device call kare → HFP audio secondary pe
- Block → `OUTGOING_BLOCKED` → Secondary pe "Call blocked" message
- Auto-approve toggle (Settings mein): ON → popup skip karo

---

### Screen 9: Contacts

- ContactsContract se contacts load karo
- Search, tap to call
- Per-device contacts — cross-device merge NAHI karna
- Same UI dono devices pe

---

### Screen 10: Call History

**Main device:**
- Permanent history (Room DB)
- Filters: All / Missed / Incoming / Outgoing
- Caller naam, number, duration, time

**Secondary device:**
- Session-only history (memory — app restart pe reset)
- HISTORY_SYNC se populated (main device end-of-call pe bhejta hai)

---

### Screen 11: Settings

**MY DEVICE section:**
- Device nickname (edit karo)
- Device icon (Phone / Tablet / Other)

**BLUETOOTH section (Main device only):**
- Paired Device: [Name] — [Forget & Unpair]
- Auto Reconnect: ON/OFF
- HD Voice: ON/OFF (agar device support kare)

**CALLS section:**
- Auto-approve outgoing calls from secondary: ON/OFF
- Notify on outgoing call: ON/OFF

**STORAGE section:**
- Clear Call History
- Clear Device History

**ABOUT section:**
- Tutorial (open again)
- Permissions (open again)
- App Version
- Privacy note (1 line: "Koi data internet pe nahi jaata")

---

### Screen 12: Per-Device Settings (Secondary ke liye)

- Device nickname
- Device icon
- Ring on this device: ON/OFF
- Allow outgoing calls: ON/OFF
- Auto-approve outgoing: ON/OFF
- Do Not Disturb: time range
- [Stop Calls] / [Disconnect] buttons

---

## BACKGROUND SERVICE — ALWAYS ALIVE

`OneCallService` ko hamesha alive rakhna hai:

```kotlin
override fun onStartCommand(...): Int {
    // ...
    return START_STICKY  // OS restart karega agar kill ho
}
```

**Service responsibilities:**
- Bluetooth connection state monitor (BluetoothProfile.STATE_CONNECTED / DISCONNECTED)
- HFP profile alive rakho
- RFCOMM socket alive rakho — disconnect pe auto-reconnect (exponential backoff)
- `READ_PHONE_STATE` se SIM call events monitor (Main device only)
- Persistent notification: "OneCall active — [Connected / Disconnected]"

**Boot auto-start:**
```kotlin
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            context.startForegroundService(Intent(context, OneCallService::class.java))
        }
    }
}
```

---

## STRICT RULES — NON-NEGOTIABLE

### ✅ HAMESHA KARNA HAI

1. `setSpeakerphoneOn(false)` — earpiece mode hi default hai SIM call ke dauran
2. Sirf **EK** secondary device support karo — kabhi multiple nahi
3. RFCOMM socket hamesha alive — reconnect with exponential backoff
4. Graceful degradation — Bluetooth off → warning, app crash nahi
5. Permission deny → feature disable, crash nahi
6. Service `START_STICKY` — OS restart karega
7. HFP fail → user-friendly error: *"Connection failed. Unpair karke dobara try karo"*

### ❌ KABHI NAHI KARNA

1. `setSpeakerphoneOn(true)` — SIM call ke dauran FORBIDDEN (privacy violation)
2. INTERNET permission — FORBIDDEN (offline app hai)
3. Wi-Fi TCP sockets — FORBIDDEN
4. Multiple devices (2+) — FORBIDDEN
5. Main device pe forced screen popup jab secondary connected ho aur call aaye
6. Contacts cross-device merge — FORBIDDEN
7. Koi external server / API call — FORBIDDEN

---

## ROOM DATABASE SCHEMA

```kotlin
@Entity(tableName = "call_history")
data class CallHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val callerNumber: String,
    val callerName: String?,
    val callType: String,       // "INCOMING" / "OUTGOING" / "MISSED"
    val durationSeconds: Long,
    val timestamp: Long,
    val deviceSource: String,   // "MAIN" / "SECONDARY"
    val isPermanent: Boolean    // true = main device, false = session only
)
```

---

## ERROR STATES — UI HANDLING

| Error | Message | Action |
|---|---|---|
| Bluetooth OFF | "Bluetooth OFF hai — please ON karo" | [Enable Bluetooth] button |
| HFP connect fail | "Connection failed. Unpair karke dobara try karo" | [Retry] button |
| Pairing fail | "Pairing failed. Dobara try karo" | [Scan Again] button |
| Permission denied (required) | "Yeh permission zaroor chahiye" | [Grant] button |
| Permission denied (optional) | "Yeh feature disabled rahega" | Info only |
| RFCOMM disconnect | Silent reconnect try karo + notification update | Auto |
| Call audio issue | "Audio issue. Headset check karo" | Toast |

---

## BUILD CHECKLIST — END MEIN VERIFY KARO

- [ ] App builds without errors
- [ ] Main device discoverable hota hai, secondary scan kar ke pair kar sakta hai
- [ ] Paired hone ke baad auto-reconnect kaam karta hai (range mein aao → connect)
- [ ] Incoming SIM call → dono devices pe IncomingCallActivity launch hota hai
- [ ] Secondary pe accept → HFP audio active, main pe ring band
- [ ] Main pe accept → secondary ki ring band, main pe call screen
- [ ] Decline → dono screens dismiss, call reject
- [ ] Active call mein transfer kaam karta hai
- [ ] Secondary se outgoing call request → main pe approval popup
- [ ] Call end → dono screens band → history mein entry
- [ ] App band karo → service alive rahe → incoming call pe screen launch ho
- [ ] Boot ke baad service auto-start ho
- [ ] SIM call ke dauran speaker ON nahi hota (earpiece only)
- [ ] Permission deny pe app crash nahi hota

---

## FINAL NOTE

Jab poora app ready ho to likho:

```
DONE — OneCall app built from scratch. Bluetooth HFP architecture complete.
```

---

*OneCall · Local · Private · No Internet · Bluetooth Only*
