package io.timmers.volumedown

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class SettingsActivity : Activity() {
    private lateinit var mLog: Logger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        mLog = Logger(this, SettingsActivity::class.java.simpleName)
        mLog.v("Received intent")
        val intent = Intent(this, VolumeControlService::class.java)
        startService(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        // menuInflater.inflate(R.menu.settings, menu)
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else*/ return super.onOptionsItemSelected(item)
    }
}