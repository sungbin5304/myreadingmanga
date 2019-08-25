package com.sungbin.com.myreadingmanga.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import cn.pedant.SweetAlert.SweetAlertDialog
import com.sungbin.com.myreadingmanga.R
import com.sungbin.com.myreadingmanga.adapter.MangaPageAdapter
import com.sungbin.com.myreadingmanga.utils.HTML
import com.sungbin.com.myreadingmanga.utils.Utils
import kotlinx.android.synthetic.main.activity_manga_page_view.*

import java.util.ArrayList

class MangaPageActivity : AppCompatActivity() {

    private var items: ArrayList<String>? = null
    private var view: RecyclerView? = null
    private var adapter: MangaPageAdapter? = null
    private var act: Activity? = null
    private var name: String? = null
    private var link: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga_page_view)
        setSupportActionBar(toolbar)

        name = intent.getStringExtra("name")
        link = intent.getStringExtra("link")
        supportActionBar!!.title = name

        items = ArrayList()
        view = findViewById(R.id.manga_page)
        act = this

        LoadPageTask().execute()
    }

    @SuppressLint("StaticFieldLeak")
    private inner class LoadPageTask : AsyncTask<Void, Void, Void>() {
        var pDialog: SweetAlertDialog? = null

        override fun onPreExecute() {
            pDialog = SweetAlertDialog(act!!, SweetAlertDialog.PROGRESS_TYPE)
            pDialog!!.progressHelper.barColor = getColor(R.color.colorPrimary)
            pDialog!!.titleText = "\n\n망가 페이지 불러오는중..."
            pDialog!!.setCancelable(false)
            pDialog!!.show()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            val main = HTML.get(link)
            val entry = main.split("https://myreadingmanga.info/wp-content/plugins/lazy-load/images/1x1.trans.gif")

            for(i in 1 until entry.size){
                val cash = entry[i]
                val url = cash.split("data-lazy-src=\"")[1].split("\"")[0]
                items!!.add(url)
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            adapter = MangaPageAdapter(items, act!!)
            view!!.layoutManager = LinearLayoutManager(act!!)
            view!!.adapter = adapter
            adapter!!.notifyDataSetChanged()
            pDialog!!.cancel()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val preFav = Utils.readData(applicationContext,
                        "fav", "null")!!.contains(name!!)
        menu!!.add("즐겨찾기")
            .setIcon(if(preFav) getDrawable(R.drawable.ic_star_white_24dp) else getDrawable(R.drawable.ic_star_border_white_24dp))
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val preFav = Utils.readData(applicationContext,
            "fav", "null")!!.contains(name!!)
        val preFavString = Utils.readData(applicationContext,
            "fav", "null")!!
        if(preFav){ //즐찾 해제
            item!!.setIcon(R.drawable.ic_star_border_white_24dp)
            val newFavString = preFavString.replace("::${name!!}", "")
            Utils.saveData(applicationContext, "fav", newFavString)
        }
        else { //즐찾 설정
            item!!.setIcon(R.drawable.ic_star_white_24dp)
            val newFavString = preFavString + "::" + name!!
            Utils.saveData(applicationContext, "fav", newFavString)
            Utils.saveData(applicationContext, name!!, link!!)
        }
        return super.onOptionsItemSelected(item)
    }

}
