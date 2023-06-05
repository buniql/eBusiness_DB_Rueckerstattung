package com.example.dbrueckerstattung

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dbrueckerstattung.database.AppDatabase
import com.example.dbrueckerstattung.database.CustomerDao
import com.example.dbrueckerstattung.entity.Customer
import com.example.dbrueckerstattung.ui.theme.DBRueckerstattungTheme

class MainActivity : ComponentActivity() {

    private lateinit var appDb : AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        appDb = AppDatabase.getDatabase(this)

        appDb.customerDao().insert( Customer(1,1,1,"Max","Mustermann",
            "super@email.com", "p","123456","DE123"))
    }

}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Text(
            text = "Hello ${CustomerDao.getAll()} Test 1 Test!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DBRueckerstattungTheme {
        Greeting()
    }
}