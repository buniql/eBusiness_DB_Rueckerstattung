package com.example.dbrueckerstattung

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dbrueckerstattung.ui.theme.DBRueckerstattungTheme
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLogin()
    }

    private fun loadLogin() {
        setContentView(R.layout.login)

        var buttonToDashboard: Button = findViewById(R.id.button_to_dashboard)

        var userText: EditText = findViewById(R.id.login_name_input)

        var pwText: EditText = findViewById(R.id.login_password_input)

        buttonToDashboard.setOnClickListener {
            if(!userText.text.toString().equals("E-Mail") && pwText.text.toString().equals("p")) {
                loadDashboard()
            }
        }

        var buttonToImprintLogin: TextView = findViewById(R.id.textView_to_imprintlogin)

        buttonToImprintLogin.setOnClickListener {
            loadImprintLogin()
        }

        var buttonToRegister1: TextView = findViewById(R.id.textView_to_register1)

        buttonToRegister1.setOnClickListener {
            loadRegister1()
        }

        var buttonToResetPW: TextView = findViewById(R.id.textView_to_forgotpw)

        buttonToResetPW.setOnClickListener {
            loadForgotPW()
        }
    }

    private fun loadImprintLogin() {
        setContentView(R.layout.imprintlogin)
        var buttonToLogin: Button = findViewById(R.id.button_to_login)

        buttonToLogin.setOnClickListener {
            loadLogin()
        }
    }

    private fun loadRegister1() {
        setContentView(R.layout.register_1)
        var buttonToRegister2: Button = findViewById(R.id.button_to_register2)

        buttonToRegister2.setOnClickListener {
            loadRegister2()
        }
    }

    private fun loadRegister2() {
        setContentView(R.layout.register_2)
        var checkBoxToAGB: CheckBox = findViewById(R.id.AGB)
        var agbText: TextView = findViewById(R.id.agb_text)
        var buttonToRegister3: Button = findViewById(R.id.button_to_register3)

        agbText.setOnClickListener {
            loadAGB()
        }

        buttonToRegister3.setOnClickListener {
            if(checkBoxToAGB.isChecked){
                loadRegister3()
            }
        }
    }

    private fun loadAGB() {
        setContentView(R.layout.agb)
        var buttonToLogin: Button = findViewById(R.id.button_to_register2)

        buttonToLogin.setOnClickListener {
            loadRegister2()
        }
    }

    private fun loadRegister3() {
        setContentView(R.layout.register_3)
        var buttonToLogin: Button = findViewById(R.id.button_to_login)

        buttonToLogin.setOnClickListener {
            loadLogin()
        }
    }

    private fun loadForgotPW() {
        setContentView(R.layout.forgotpw)
        var buttonToResetNotify: Button = findViewById(R.id.button_to_resetnotify)

        buttonToResetNotify.setOnClickListener {
            loadResetNotify()
        }
    }

    private fun loadResetNotify() {
        setContentView(R.layout.resetnotify)
        var buttonToLogin: Button = findViewById(R.id.button_to_login)

        buttonToLogin.setOnClickListener {
            loadLogin()
        }
    }

    private fun loadDashboard() {

        setContentView(R.layout.dashboard)
        val refunded_sum_textView = findViewById<TextView>(R.id.refunded_sum)
        val status_textView = findViewById<TextView>(R.id.statuseinträge)
        val statistik_textView = findViewById<TextView>(R.id.statistiken_text)
        val minput = InputStreamReader(assets.open("db.csv"))
        val reader = BufferedReader(minput)
        val csvParser = CSVParser.parse(
            reader,
            CSVFormat.DEFAULT
        )
        val list= mutableListOf<daten>()

        csvParser.forEach { record ->
            val id = record.get(0)
            val verspeatung = record.get(1)
            val betrag = record.get(2).toDoubleOrNull()

            if (id != null && verspeatung != null && betrag != null) {
                val daten = daten(id, verspeatung, betrag)
                list.add(daten)
            }
        }

        val refundedSum = list.sumOf { it.betrag }
        refunded_sum_textView.text = "${refundedSum}€"

        val lastThreeEntries = list.takeLast(3)

        lastThreeEntries.forEach { entry ->
            println("ID: ${entry.id}, Verspätung: ${entry.verspeatung}, Betrag: ${entry.betrag}")
        }
        lastThreeEntries.forEach {
            status_textView.append(
                "Auftrag ${it.id} wird bearbeitet mit dem Betrag: ${it.betrag} \n\n"
            )
        }
        val jaCount = list.count { it.verspeatung == "Ja" }
        val prozentJa = jaCount.toDouble() / list.size * 100
        statistik_textView.text = "${prozentJa.toInt()}% der Züge im letzten Monat waren nicht pünktlich"



        var claimButton: Button = findViewById(R.id.botton_to_claim)
        var settingsButton: Button = findViewById(R.id.button_to_settings)

        claimButton.setOnClickListener {
            loadClaim()
        }

        settingsButton.setOnClickListener {
            loadSettings()
        }
    }

    private fun loadClaim() {
        setContentView(R.layout.claim)

        var dashboardButton: Button = findViewById(R.id.button_to_dashboard)
        var settingsButton: Button = findViewById(R.id.button_to_settings)

        var photoButton: Button = findViewById(R.id.button_to_scan)
        var dbNavButton: Button = findViewById(R.id.button_to_claim_successful)

        dashboardButton.setOnClickListener {
            loadDashboard()
        }
        settingsButton.setOnClickListener {
            loadSettings()
        }
        photoButton.setOnClickListener {
            loadScan()
        }
        dbNavButton.setOnClickListener {
            loadClaimSuccessful()
        }
    }

    private fun loadScan() {
        setContentView(R.layout.scan)

        var takePictureButton: ImageView = findViewById(R.id.cameraButton)

        takePictureButton.setOnClickListener {
            loadClaimSuccessful()
        }
    }

    private fun loadClaimSuccessful() {
        setContentView(R.layout.claimsuccessful)

        var backToDashboardButton: Button = findViewById(R.id.button_to_dashboard)

        backToDashboardButton.setOnClickListener {
            loadDashboard()
        }
    }

    private fun loadSettings() {



        setContentView(R.layout.settings)

        var claimButton: Button = findViewById(R.id.botton_to_claim)
        var dashboardButton: Button = findViewById(R.id.button_to_dashboard)
        var submitButton: Button = findViewById(R.id.button_to_submit)

        dashboardButton.setOnClickListener {
            loadDashboard()
        }

        claimButton.setOnClickListener {
            loadClaim()
        }

        submitButton.setOnClickListener {
            loadSettings()
        }
    }
}

data class KundenDaten(
    val ID: Int,
    val Verspaetung: String,
    val Rueckerstattungsbetrag: Double,
)

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DBRueckerstattungTheme {
        Greeting("Android")
    }
}