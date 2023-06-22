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
import androidx.core.widget.doAfterTextChanged
import com.example.dbrueckerstattung.ui.theme.DBRueckerstattungTheme
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLogin()
    }

    private fun loadLogin() {
        //DB Initialisierung
//        var appDb = AppDatabase.getDatabase(this)
//
//        appDb.customerDao().insert( Customer(1,1,1,"Max","Mustermann",
//            "super@email.com", "p","123456","DE123")
//        )
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
            val von = record.get(1)
            val nach = record.get(2)
            val datum = record.get(3)
            val status = record.get(4)
            val verspeatung = record.get(5)
            val betrag = record.get(6).toDoubleOrNull()

            if (id != null && verspeatung != null && betrag != null) {
                val daten = daten(id, von, nach, datum, status, verspeatung, betrag)
                list.add(daten)
            }
        }

        val refundedSum = list.sumOf { it.betrag }
        refunded_sum_textView.text = "${String.format("%.2f", refundedSum)}€"

        val lastEntries = list.takeLast(3)

        lastEntries.forEach {
            status_textView.append(
                "Auftrag ${it.id} wird bearbeitet mit dem Betrag: ${it.betrag} \n\n"
            )
        }
        val jaCount = list.count { it.verspeatung == "Ja" }
        val prozentJa = jaCount.toDouble() / list.size * 100
        statistik_textView.text = "${prozentJa.toInt()}% der Züge im letzten Monat waren nicht pünktlich"



        var claimButton: Button = findViewById(R.id.botton_to_claim)
        var settingsButton: Button = findViewById(R.id.button_to_settings)
        var refundlistButton: Button = findViewById(R.id.button_to_detailList)

        claimButton.setOnClickListener {
            loadClaim()
        }

        settingsButton.setOnClickListener {
            loadSettings()
        }

        refundlistButton.setOnClickListener{
            loadDetailedList(list)
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

    private fun loadDetailedList(list:List<daten>){
        setContentView(R.layout.refundlist)

        var dashboardButton: Button = findViewById(R.id.button_to_dashboard)

        var searchBox: EditText = findViewById(R.id.refundlist_search)
        var sortByDateButton: Button = findViewById(R.id.sort_by_date)
        var sortBySumButton: Button = findViewById(R.id.sort_by_sum)
        var doneCheckbox: CheckBox = findViewById(R.id.checkbox_done)
        var refund_textView = findViewById<TextView>(R.id.refunds)

        var currentSearchString: String = ""
        var currentCheckBoxState: Boolean = false

        var sortedList: List<daten>

        list.forEach {
            refund_textView.append(
                "Auftrag ${it.id}\n Von: ${it.von} Nach: ${it.nach} \n Datum: ${it.datum} \n" +
                        " Status: ${it.status}\n Betrag: ${it.betrag}€ \n\n"
            )
        }

        searchBox.doAfterTextChanged { text ->
            currentSearchString = text.toString();
            refund_textView.setText("");
            list.forEach {
                if (it.von.contains(text.toString(), ignoreCase = true) || it.nach.contains(text.toString(), ignoreCase = true))
                    refund_textView.append(
                        "Auftrag ${it.id}\n Von: ${it.von} Nach: ${it.nach} \n Datum: ${it.datum} \n" +
                                " Status: ${it.status}\n Betrag: ${it.betrag}€ \n\n"
                    )
            }
        }

        sortByDateButton.setOnClickListener {
            refund_textView.setText("");

            if(sortByDateButton.text.equals("Datum ASC")) {
                sortedList = list.sortedWith(compareByDescending<daten> { it.datum.substring(6, 10) }
                    .thenBy { it.datum.substring(3, 5) }
                    .thenBy { it.datum.substring(0, 2) }
                )
                sortedList.forEach {
                    if (it.von.contains(currentSearchString, ignoreCase = true) || it.nach.contains(currentSearchString, ignoreCase = true))
                        refund_textView.append(
                            "Auftrag ${it.id}\n Von: ${it.von} Nach: ${it.nach} \n Datum: ${it.datum} \n" +
                                    " Status: ${it.status}\n Betrag: ${it.betrag}€ \n\n"
                        )
                }
                sortByDateButton.setText("Datum DSC")
            } else {
                sortedList = list.sortedWith(compareBy<daten> { it.datum.substring(6, 10) }
                    .thenBy { it.datum.substring(3, 5) }
                    .thenBy { it.datum.substring(0, 2) }
                )
                sortedList.forEach {
                    if (it.von.contains(currentSearchString, ignoreCase = true) || it.nach.contains(currentSearchString, ignoreCase = true))
                        refund_textView.append(
                            "Auftrag ${it.id}\n Von: ${it.von} Nach: ${it.nach} \n Datum: ${it.datum} \n" +
                                    " Status: ${it.status}\n Betrag: ${it.betrag}€ \n\n"
                        )
                }
                sortByDateButton.setText("Datum ASC")
            }
        }

        sortBySumButton.setOnClickListener {
            refund_textView.setText("");

            if(sortBySumButton.text.equals("Betrag ASC")) {
                sortedList = list.sortedBy {it.betrag}
                sortedList.forEach {
                    if (it.von.contains(currentSearchString, ignoreCase = true) || it.nach.contains(currentSearchString, ignoreCase = true))
                        refund_textView.append(
                            "Auftrag ${it.id}\n Von: ${it.von} Nach: ${it.nach} \n Datum: ${it.datum} \n" +
                                    " Status: ${it.status}\n Betrag: ${it.betrag}€ \n\n"
                        )
                }
                sortBySumButton.setText("Betrag DSC")
            } else {
                sortedList = list.sortedByDescending  {it.betrag}
                sortedList.forEach {
                    if (it.von.contains(currentSearchString, ignoreCase = true) || it.nach.contains(currentSearchString, ignoreCase = true))
                        refund_textView.append(
                            "Auftrag ${it.id}\n Von: ${it.von} Nach: ${it.nach} \n Datum: ${it.datum} \n" +
                                    " Status: ${it.status}\n Betrag: ${it.betrag}€ \n\n"
                        )
                }
                sortBySumButton.setText("Betrag ASC")
            }
        }

        doneCheckbox.setOnClickListener {
            currentCheckBoxState = doneCheckbox.isChecked;
            refund_textView.setText("");

            if(doneCheckbox.isChecked) {
                list.forEach {
                    if (it.von.contains(currentSearchString, ignoreCase = true) || it.nach.contains(currentSearchString, ignoreCase = true && it.status.equals("Abgeschlossen")))
                        refund_textView.append(
                            "Auftrag ${it.id}\n Von: ${it.von} Nach: ${it.nach} \n Datum: ${it.datum} \n" +
                                    " Status: ${it.status}\n Betrag: ${it.betrag}€ \n\n"
                        )
                }
            } else {
                list.forEach {
                    if (it.von.contains(currentSearchString, ignoreCase = true) || it.nach.contains(currentSearchString, ignoreCase = true))
                    refund_textView.append(
                        "Auftrag ${it.id}\n Von: ${it.von} Nach: ${it.nach} \n Datum: ${it.datum} \n" +
                                " Status: ${it.status}\n Betrag: ${it.betrag}€ \n\n"
                    )
                }
            }

        }

        dashboardButton.setOnClickListener {
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