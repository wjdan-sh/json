package com.example.jsonprctice

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var details: EURO? = null
    lateinit var userinp:EditText
    lateinit var convrt:Button
    lateinit var spinner:Spinner

    var selected: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         userinp = findViewById(R.id.userinput)
         convrt = findViewById(R.id.btn)
         spinner = findViewById(R.id.spr)

        val cur = arrayListOf("inr", "usd", "aud", "sar", "cny", "jpy")

        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, cur
            )
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    selected = position
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                   Toast.makeText(applicationContext, "Please select something", Toast.LENGTH_SHORT).show()
                }
            }
        }

        convrt.setOnClickListener {

            var sel = userinp.text.toString()
            if (sel.isNotEmpty()) {
                var currency: Double = sel.toDouble()

                getCurrency(onResult = {
                    details = it

                    when (selected) {

                        0 -> disp(calc(details?.eur?.inr?.toDouble(), currency));
                        1 -> disp(calc(details?.eur?.usd?.toDouble(), currency));
                        2 -> disp(calc(details?.eur?.aud?.toDouble(), currency));
                        3 -> disp(calc(details?.eur?.sar?.toDouble(), currency));
                        4 -> disp(calc(details?.eur?.cny?.toDouble(), currency));
                        5 -> disp(calc(details?.eur?.jpy?.toDouble(), currency));
                    }
                })
            }else{
                Toast.makeText(applicationContext, "Please enter something", Toast.LENGTH_SHORT).show()
            }
            userinp.text.clear()
            userinp.clearFocus()
        }


    }
    private fun disp(calc: Double) {

        val Result = findViewById<TextView>(R.id.result)

        Result.text = String.format("%.3f", calc)
    }

    private fun calc(i: Double?, sel: Double): Double {
        var s = 0.0
        if (i !== null) {
            s = (i * sel)
        }
        return s
    }

    private fun getCurrency(onResult: (EURO?) -> Unit) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        if (apiInterface != null) {
            apiInterface.getCurrency()?.enqueue(object : Callback<EURO> {
                override fun onResponse(
                    call: Call<EURO>,
                    response: Response<EURO>
                ) {
                    onResult(response.body())

                }

                override fun onFailure(call: Call<EURO>, t: Throwable) {
                    onResult(null)
                    Toast.makeText(applicationContext, "Something went wrong!" + t.message, Toast.LENGTH_SHORT).show();
                }

            })
        }
    }
}