package com.example.researcher

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.app.SearchManager;
import android.content.pm.ActivityInfo
import android.os.Build
import android.telecom.Call
import android.text.method.ScrollingMovementMethod
import android.widget.SearchView.OnQueryTextListener;
import android.view.View
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import okhttp3.*



class MainActivity : AppCompatActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    }


    private fun parse(urname:String) {
        val okHttpClient: OkHttpClient = OkHttpClient()

        var f = 0
        var f2 = 0
        var j = 0
        val k = "\u0022"
        val k1 = "\u002c"
        var k2 :Char
        var l = 0
        var pe = ""
        var zn = ""
        var str01 = ""
        var str02 = ""
        var uName = ""
        var uID = ""
        val size = 100
        val arr = Array(size, { Array(4, {""}) })


        val URL1 = URL("https://api.github.com/users/"+urname+"/repos")


        val request: Request = Request.Builder().url(URL1).build()
        okHttpClient.newCall(request).enqueue(object: Callback {




            override fun onFailure(call: okhttp3.Call, e: IOException?) {
            }

            override fun onResponse(call: okhttp3.Call, response: Response?) {
                val line = response?.body()?.string()
                if (line != null) {
                    for (i in 0..line.length - 1) {
                        if (j < size) {
                            if (f == 1 && line.get(i) != k.get(0)) {
                                pe = pe + line.get(i)
                            }

                            if (f == 2) {
                                if (pe != "name" && pe != "description" && pe != "language" && pe != "login") {
                                    f = 0
                                    pe = ""
                                    zn = ""
                                }
                            }
                            if (f == 3 && line.get(i) != k.get(0)) {
                                zn = zn + line.get(i)
                            }
                            if (f == 4) {
                                if (pe == "name") {
                                    arr[j][0] = zn
                                    if (arr[j][0] == "full_name") {
                                        arr[j][0] = "null"
                                    }
                                }
                                if (pe == "description") {
                                    arr[j][1] = zn
                                    if (arr[j][1] == "fork") {
                                        arr[j][1] = "null"
                                    }
                                }
                                if (pe == "language") {
                                    arr[j][2] = zn
                                    if (arr[j][2] == "has_issues") {
                                        arr[j][2] = "null"
                                    }
                                    j = j + 1
                                }
                                if (pe == "login") {
                                    arr[j][3] = zn
                                    str01 = line.get(i + 2).toString() + line.get(i + 3).toString()
                                    if (str01 == "id" && f2 == 0) {
                                        uName = zn
                                        l = 6
                                        do {
                                            str02 = str02 + line.get(i + l)
                                            l += 1
                                            k2 = line.get(i + l)
                                            if (k2 == k1.get(0)) {
                                                l = 0
                                            }
                                        } while (l > 1)
                                        uID = str02
                                        f2 = 1

                                    }
                                }

                                f = 0
                                pe = ""
                                zn = ""
                            }

                            if (line.get(i) == k.get(0)) {
                                f = f + 1

                            }

                        }

                        }
                        runOnUiThread {
                            var textView3 = findViewById<TextView>(R.id.textView3)
                            var textView2 = findViewById<TextView>(R.id.textView2)
                            var textView1 = findViewById<TextView>(R.id.textView1)
                            var prText=""

                           for (i in 0 .. j-1) {
                            prText= prText + "Проект: "+ arr[i][0]  + "\nОписание: "+ arr[i][1] + "\nЯзык: "+ arr[i][2]  + "\nАвтор: "+ arr[i][3] + "\n\n"
                           }
                            if (uID == ""){
                                textView1.setText("Не найдено :(")
                                textView2.setText(" ")
                                textView3.setText(" ")
                            }else{
                                textView3.setMovementMethod(ScrollingMovementMethod())
                                textView1.setText("Имя: $uName")
                                textView2.setText("id: $uID")
                                textView3.setText("$prText")}

                        }
                }
            }



        })
    }

   @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()

                searchView.setQuery("", false)



                searchItem.collapseActionView()
                Toast.makeText(this@MainActivity, "Ищем: $query", Toast.LENGTH_LONG).show()
                //if(user.contains(query)){
                //adapter.filter.filter(query)
                //Toast.makeText(applicationContext, "$query найдено", Toast.LENGTH_LONG).show()
                //}

                //else{
                //Toast.makeText(applicationContext, "$query не найдено", Toast.LENGTH_LONG).show()
                //}
                searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
                parse("$query")





                    return true
                }


            override fun onQueryTextChange(query: String?): Boolean {
                // adapter.filter.filter(query)
                return false
            }

        })
        return true



    }
}






