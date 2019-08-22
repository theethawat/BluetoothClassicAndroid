# Bluetooth Android

Practical Android Connect Bluetooth Start at bluetooth Classic (Using Kotlin and MVVM Architecture), Only note something not good enought for reference .

### Environment Check

- Enable Databinding in build.gradle (App Modlue)
- Make DatabindingUtil varialble in your activity like
  `private lateinit var binding:ActivityMainBinding`

## Initial Setting

- Grant Bluetooth Permission in AndroidManifest.xml

        <uses-permission android:name="android.permission.BLUETOOTH"/>
        <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>

- Set Bluetooth Adapter

  `val mybluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()`

- Set Time to Make Our Main Device (Phone) Discoverable

  `val discoverableIntent:Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply { putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,150) }`

- Make User Grant Permission Bluetooth for us and Make sure user turn on Bluetooth Service (Can Be in onCreate)

        if (mybluetoothAdapter?.isEnabled == false) {
            val userEnableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE) startActivityForResult(userEnableBtIntent, 12) }

## Bluetooth Classic

- Checking for Paired Device

  `val pairedDevice: Set<BluetoothDevice>? = mybluetoothAdapter?.bondedDevices`

  And you can checking whatever you want like add `foreach` loop and Log check or displaying in the User Interface

## Project Researcher

- Theethawat Savastham
- Prince of Songkla University, Hatyai Thailand
