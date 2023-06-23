package com.example.dbrueckerstattung

import android.os.Bundle
import android.util.Log
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
import com.example.dbrueckerstattung.database.AppDatabase
import com.example.dbrueckerstattung.entity.Daten
import com.example.dbrueckerstattung.entity.UserSingleton
import com.example.dbrueckerstattung.tools.Tools
import com.example.dbrueckerstattung.ui.theme.DBRueckerstattungTheme
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : ComponentActivity() {

    private var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getInstance(applicationContext)
        db?.datenDao()?.clearData()

        //Daten werden aus der CSV-Datei ausgelesen
        val minput = InputStreamReader(assets.open("db.csv"))
        val reader = BufferedReader(minput)
        val csvParser = CSVParser.parse(
            reader,
            CSVFormat.DEFAULT
        )

        var count: Int = 0;
        //Datenbank Migration mithilfe CSV
        csvParser.forEach { record ->
            val von = record.get(1)
            val nach = record.get(2)
            val datum = record.get(3)
            val status = record.get(4)
            val verspeatung = record.get(5)
            val betrag = record.get(6).toDoubleOrNull()

            if (verspeatung != null && betrag != null) {
                val daten = Daten(count, von, nach, datum, status, verspeatung, betrag)
                //Daten werden in die DB geschrieben
                db?.datenDao()?.insertDaten(daten)
            }
            count++;
        }
        loadLogin()
    }

    //Login View wird geladen
    private fun loadLogin() {
        setContentView(R.layout.login)
        //Deklarierung der Variablen, die zum Anzeigen der Daten benötigt werden (mit Verlinkung)
        var buttonToDashboard: Button = findViewById(R.id.button_to_dashboard)
        var userText: EditText = findViewById(R.id.login_name_input)
        var pwText: EditText = findViewById(R.id.login_password_input)
        var buttonToImprintLogin: TextView = findViewById(R.id.textView_to_imprintlogin)
        var buttonToRegister1: TextView = findViewById(R.id.textView_to_register1)
        var buttonToResetPW: TextView = findViewById(R.id.textView_to_forgotpw)

        //Validierung ob der Login korrekt ist
        buttonToDashboard.setOnClickListener {
            if(userText.text.toString().equals(UserSingleton.user.email) && pwText.text.toString().equals(UserSingleton.user.password)) {
                loadDashboard()
            } else {
                Tools.exceptionToast(getApplicationContext(), "Benutzername oder Passwort falsch");
            }
        }

        // Button zum Impressum
        buttonToImprintLogin.setOnClickListener {
            loadImprintLogin()
        }
        //Button zum Registrieren-Vorgang
        buttonToRegister1.setOnClickListener {
            loadRegister1()
        }
        //Button zum 'Passwort-Vergessen' Vorgang
        buttonToResetPW.setOnClickListener {
            loadForgotPW()
        }
    }

    //Impressum View wird geladen
    private fun loadImprintLogin() {
        setContentView(R.layout.imprintlogin)
        var buttonToLogin: Button = findViewById(R.id.button_to_login)

        buttonToLogin.setOnClickListener {
            loadLogin()
        }
    }

    //Funktion für die E-Mail und Passwort Registrierung
    private fun loadRegister1() {
        setContentView(R.layout.register_1)
        //Deklarierung der Variablen, die zum Anzeigen der Daten benötigt werden (mit Verlinkung)
        var email: EditText = findViewById(R.id.login_email_input)
        var pw: EditText = findViewById(R.id.login_password_input1)
        var pwConfirm: EditText = findViewById(R.id.login_password_input2)

        var buttonToRegister2: Button = findViewById(R.id.button_to_register2)

        //Validierung Email gültig und Passowrt Gültig?
        buttonToRegister2.setOnClickListener {
            if(email.text.toString().contains("@")) {
                if(pw.text.toString().equals(pwConfirm.text.toString())) {
                    if(pw.text.toString().length >= 12) {
                        UserSingleton.setUserEmail(email.text.toString())
                        UserSingleton.setUserPassword(pw.text.toString())
                        loadRegister2()
                    } else {
                        Tools.exceptionToast(getApplicationContext(), "Passwort benötigt mindestens 12 Zeichen")
                    }
                } else {
                    Tools.exceptionToast(getApplicationContext(), "Passwörter stimmen nicht überein");
                }
            } else {
                Tools.exceptionToast(getApplicationContext(), "Gib eine valide Email an");
            }

        }
    }

    // Registrierung für Name und IBAN
    private fun loadRegister2() {
        setContentView(R.layout.register_2)
        //Deklarierung der Variablen, die zum Anzeigen der Daten benötigt werden (mit Verlinkung)
        var firstname: EditText = findViewById(R.id.login_firstname_input)
        var lastname: EditText = findViewById(R.id.login_lastname_input)
        var iban: EditText = findViewById(R.id.login_iban_input)

        var checkBoxToAGB: CheckBox = findViewById(R.id.AGB)
        var agbText: TextView = findViewById(R.id.agb_text)
        var buttonToRegister3: Button = findViewById(R.id.button_to_register3)

        agbText.setOnClickListener {
            loadAGB()
        }

        // Validierung ob eine gültige IBAN eingegeben wurde sowie die AGB´s gelesen wurden
        buttonToRegister3.setOnClickListener {
            if(checkBoxToAGB.isChecked) {
                // Überprüfung der IBAN mit einem regulären Ausdruck
                if(iban.text.toString().matches("^[DE]{2}([0-9a-zA-Z]{20})\$".toRegex()) || !firstname.text.toString().equals("") || !lastname.text.toString().equals("")) {
                    UserSingleton.setUserSurname(firstname.text.toString())
                    UserSingleton.setUserLastname(lastname.text.toString())
                    UserSingleton.setUserIban(iban.text.toString())
                    loadRegister3()
                } else {
                    Tools.exceptionToast(getApplicationContext(), "Eingaben nicht korrekt");
                }
            } else {
                Tools.exceptionToast(getApplicationContext(), "Stimme unseren AGB's zu, um fortzufahren");
            }
        }
    }
    //AGB Aufruf
    private fun loadAGB() {
        setContentView(R.layout.agb)
        var buttonToLogin: Button = findViewById(R.id.button_to_register2)

        buttonToLogin.setOnClickListener {
            loadRegister2()
        }
    }
    //Abschlus Registier-Vorgang
    private fun loadRegister3() {
        setContentView(R.layout.register_3)
        var buttonToLogin: Button = findViewById(R.id.button_to_login)

        buttonToLogin.setOnClickListener {
            loadLogin()
        }
    }
    //Passwort-Vergessen-Funktion
    private fun loadForgotPW() {
        setContentView(R.layout.forgotpw)
        var buttonToResetNotify: Button = findViewById(R.id.button_to_resetnotify)

        buttonToResetNotify.setOnClickListener {
            loadResetNotify()
        }
    }
    //Abschluss Passwort-Vergessen
    private fun loadResetNotify() {
        setContentView(R.layout.resetnotify)
        var buttonToLogin: Button = findViewById(R.id.button_to_login)

        buttonToLogin.setOnClickListener {
            loadLogin()
        }
    }

    //Dashboard View wird geladen
    private fun loadDashboard() {

        //Verweis auf die XML-Datei dashboard.xml
        setContentView(R.layout.dashboard)

        //Deklarierung der Variablen, die zum Anzeigen der Daten benötigt werden (mit Verlinkung)
        val refunded_sum_textView = findViewById<TextView>(R.id.refunded_sum)
        val status_textView = findViewById<TextView>(R.id.statuseinträge)
        val statistik_textView = findViewById<TextView>(R.id.statistiken_text)

        //Datenbankzugriff für alle Rückerstattungen
        val list: List<Daten> = db?.datenDao()?.loadAllDaten() ?: emptyList()

        //Aufsummierung der Beiträge, die rückerstattet werden
        val refundedSum = list.sumOf { it.betrag }
        refunded_sum_textView.text = "${String.format("%.2f", refundedSum)}€"

        //Die letzten 3 Einträge der Liste werden in der Variablen lastEntries gespeichert
        val lastEntries = list.takeLast(3)

        //Die letzten 3 Einträge werden auf dem Dashboard ausgegeben
        lastEntries.forEach {
            status_textView.append(
                "Auftrag ${it.id} wird bearbeitet mit dem Betrag: ${it.betrag} \n\n"
            )
        }

        //Berechnung der prozentualen Anzahl an Verspätungen
        val jaCount = list.count { it.verspeatung == "Ja" }
        val prozentJa = jaCount.toDouble() / list.size * 100
        statistik_textView.text = "${prozentJa.toInt()}% der Züge im letzten Monat waren nicht pünktlich"


        //Deklarierung der Variablen für die einzelnen Buttons (mit Verlinkung zu dashboard.xml)
        var claimButton: Button = findViewById(R.id.botton_to_claim)
        var settingsButton: Button = findViewById(R.id.button_to_settings)
        var refundlistButton: Button = findViewById(R.id.button_to_detailList)

        //Festlegen, was beim klicken auf den Button "claimButton" passiert
        claimButton.setOnClickListener {
            loadClaim()
        }

        //Festlegen, was beim klicken auf den Button "settingsButton" passiert
        settingsButton.setOnClickListener {
            loadSettings()
        }

        //Festlegen, was beim klicken auf den Button "refundlistButton" passiert
        refundlistButton.setOnClickListener{
            loadDetailedList(list)
        }
    }

    //Beantragen View wird geladen
    private fun loadClaim() {
        setContentView(R.layout.claim)
        //Deklarierung der Variablen, die zum Anzeigen der Daten benötigt werden (mit Verlinkung)
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

    //Scan View mit der Mock-Kamera wird geladen
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

    //Detalierte Liste View wird geladen
    private fun loadDetailedList(list:List<Daten>){
        setContentView(R.layout.refundlist)
        //Deklarierung der Variablen, die zum Anzeigen der Daten benötigt werden (mit Verlinkung)
        var dashboardButton: Button = findViewById(R.id.button_to_dashboard)

        var searchBox: EditText = findViewById(R.id.refundlist_search)
        var sortByDateButton: Button = findViewById(R.id.sort_by_date)
        var sortBySumButton: Button = findViewById(R.id.sort_by_sum)
        var doneCheckbox: CheckBox = findViewById(R.id.checkbox_done)
        var refund_textView = findViewById<TextView>(R.id.refunds)

        var currentSearchString: String = ""
        var currentCheckBoxState: Boolean = false

        var sortedList: List<Daten>

        //Alle Daten werden geladen und angezeigt ohne Filterung
        list.forEach {
            refund_textView.append(
                "Auftrag ${it.id}\n Von: ${it.von} Nach: ${it.nach} \n Datum: ${it.datum} \n" +
                        " Status: ${it.status}\n Betrag: ${it.betrag}€ \n\n"
            )
        }

        //Filterung nach Eingabe
        searchBox.doAfterTextChanged { text ->
            currentSearchString = text.toString();
            refund_textView.setText("");
            list.forEach {
                if(!currentCheckBoxState || it.status.equals("Abgeschlossen"))
                    if (it.von.contains(text.toString(), ignoreCase = true) || it.nach.contains(text.toString(), ignoreCase = true))
                        refund_textView.append(
                            "Auftrag ${it.id}\n Von: ${it.von} Nach: ${it.nach} \n Datum: ${it.datum} \n" +
                                    " Status: ${it.status}\n Betrag: ${it.betrag}€ \n\n"
                        )
            }
        }

        //Sortierung nach Datum
        sortByDateButton.setOnClickListener {
            refund_textView.setText("");
            //Aufsteigend
            if(sortByDateButton.text.equals("Datum ASC")) {
                sortedList = list.sortedWith(compareByDescending<Daten> { it.datum.substring(6, 10) }
                    .thenBy { it.datum.substring(3, 5) }
                    .thenBy { it.datum.substring(0, 2) }
                )
                sortedList.forEach {
                    if(!currentCheckBoxState || it.status.equals("Abgeschlossen"))
                        if (it.von.contains(currentSearchString, ignoreCase = true) || it.nach.contains(currentSearchString, ignoreCase = true))
                            refund_textView.append(
                                "Auftrag ${it.id}\n Von: ${it.von} Nach: ${it.nach} \n Datum: ${it.datum} \n" +
                                        " Status: ${it.status}\n Betrag: ${it.betrag}€ \n\n"
                            )
                }
                sortByDateButton.setText("Datum DSC")
            } else {
                //Absteigend
                sortedList = list.sortedWith(compareBy<Daten> { it.datum.substring(6, 10) }
                    .thenBy { it.datum.substring(3, 5) }
                    .thenBy { it.datum.substring(0, 2) }
                )
                sortedList.forEach {
                    if(!currentCheckBoxState || it.status.equals("Abgeschlossen"))
                        if (it.von.contains(currentSearchString, ignoreCase = true) || it.nach.contains(currentSearchString, ignoreCase = true))
                            refund_textView.append(
                                "Auftrag ${it.id}\n Von: ${it.von} Nach: ${it.nach} \n Datum: ${it.datum} \n" +
                                        " Status: ${it.status}\n Betrag: ${it.betrag}€ \n\n"
                            )
                }
                sortByDateButton.setText("Datum ASC")
            }
        }

        //Sortierung nach Betrag
        sortBySumButton.setOnClickListener {
            refund_textView.setText("");
            //Aufsteigend
            if(sortBySumButton.text.equals("Betrag ASC")) {
                sortedList = list.sortedBy {it.betrag}
                sortedList.forEach {
                    if(!currentCheckBoxState || it.status.equals("Abgeschlossen"))
                        if (it.von.contains(currentSearchString, ignoreCase = true) || it.nach.contains(currentSearchString, ignoreCase = true))
                            refund_textView.append(
                                "Auftrag ${it.id}\n Von: ${it.von} Nach: ${it.nach} \n Datum: ${it.datum} \n" +
                                        " Status: ${it.status}\n Betrag: ${it.betrag}€ \n\n"
                            )
                }
                sortBySumButton.setText("Betrag DSC")
            } else {
                //Absteigend
                sortedList = list.sortedByDescending  {it.betrag}
                sortedList.forEach {
                    if(!currentCheckBoxState || it.status.equals("Abgeschlossen"))
                        if (it.von.contains(currentSearchString, ignoreCase = true) || it.nach.contains(currentSearchString, ignoreCase = true))
                            refund_textView.append(
                                "Auftrag ${it.id}\n Von: ${it.von} Nach: ${it.nach} \n Datum: ${it.datum} \n" +
                                        " Status: ${it.status}\n Betrag: ${it.betrag}€ \n\n"
                            )
                }
                sortBySumButton.setText("Betrag ASC")
            }
        }

        // Filterung nach abgeschlossene Rückerstattungen
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
                    if (it.von.contains(currentSearchString, ignoreCase = true) || it.nach.contains(currentSearchString, ignoreCase = true)) {
                        refund_textView.append(
                            "Auftrag ${it.id}\n Von: ${it.von} Nach: ${it.nach} \n Datum: ${it.datum} \n" +
                                    " Status: ${it.status}\n Betrag: ${it.betrag}€ \n\n"
                        )
                    }
                }
            }

        }

        dashboardButton.setOnClickListener {
            loadDashboard()
        }
    }
    private fun loadSettings() {
        setContentView(R.layout.settings)
        //Deklarierung der Variablen, die zum Anzeigen der Daten benötigt werden (mit Verlinkung)
        var firstname: EditText = findViewById(R.id.surname_input)
        var lastname: EditText = findViewById(R.id.lastname_input)
        var iban: EditText = findViewById(R.id.IBAN_input)

        //Setzten der Daten des registrierten Benutzer
        firstname.setText(UserSingleton.user.surname)
        lastname.setText(UserSingleton.user.lastname)
        iban.setText(UserSingleton.user.iban)

        var claimButton: Button = findViewById(R.id.botton_to_claim)
        var dashboardButton: Button = findViewById(R.id.button_to_dashboard)
        var submitButton: Button = findViewById(R.id.button_to_submit)
        var imprint: TextView = findViewById(R.id.button_to_imprint)

        var userTextVorname: EditText = findViewById(R.id.surname_input)
        var userTextNachname: EditText = findViewById(R.id.lastname_input)
        var ibanText: EditText = findViewById(R.id.IBAN_input)

        imprint.setOnClickListener {
            loadImprintSettings()
        }

        dashboardButton.setOnClickListener {
            loadDashboard()
        }

        claimButton.setOnClickListener {
            loadClaim()
        }

        //Änderungen der Nutzerdaten
        submitButton.setOnClickListener {
            if(ibanText.text.toString().matches("^[DE]{2}([0-9a-zA-Z]{20})\$".toRegex()) || !userTextVorname.text.toString().equals("") || !userTextNachname.text.toString().equals("")) {
                UserSingleton.setUserSurname(firstname.text.toString())
                UserSingleton.setUserLastname(lastname.text.toString())
                UserSingleton.setUserIban(iban.text.toString())
                loadRegister3()
            } else {
                Tools.exceptionToast(getApplicationContext(), "Eingaben nicht korrekt");
            }
            loadSettings()
        }
    }

    private fun loadImprintSettings() {
        setContentView(R.layout.imprint)
        var buttonToLogin: Button = findViewById(R.id.button_to_settings)

        buttonToLogin.setOnClickListener {
            loadSettings()
        }
    }
}
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