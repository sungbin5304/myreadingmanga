@file:Suppress("DEPRECATION")

package com.sungbin.com.myreadingmanga.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.app.SearchManager
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import com.sungbin.com.myreadingmanga.R
import com.sungbin.com.myreadingmanga.adapter.MangaListAdapter
import com.sungbin.com.myreadingmanga.dto.MangaListItem
import com.sungbin.com.myreadingmanga.utils.HTML
import com.sungbin.com.myreadingmanga.utils.Utils
import java.util.ArrayList
import com.sungbin.com.myreadingmanga.handler.BackPressCloseHandler
import cn.pedant.SweetAlert.SweetAlertDialog
import android.os.AsyncTask
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import com.shashank.sony.fancytoastlib.FancyToast

import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import com.sungbin.com.myreadingmanga.adapter.MangaFavAdapter
import com.sungbin.com.myreadingmanga.dto.MangaFavItem
import kotlinx.android.synthetic.main.activity_manga_list.*
import android.support.v7.widget.DividerItemDecoration


class MangaListActivity : AppCompatActivity() {

    private var items: ArrayList<MangaListItem>? = null
    private var backPressCloseHandler: BackPressCloseHandler? = null
    private var view: RecyclerView? = null
    private var adapter: MangaListAdapter? = null
    private var act: Activity? = null
    private var search: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga_list)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_star_white_24dp)

        backPressCloseHandler = BackPressCloseHandler(this)

        items = ArrayList()
        view = findViewById(R.id.manga_list)
        act = this

        LoadListTask().execute()
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView.maxWidth = Integer.MAX_VALUE
            searchView.queryHint = getString(com.sungbin.com.myreadingmanga.R.string.input_search_keyword)

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(str: String?): Boolean {
                    search = str.toString()
                    val main = HTML.get("https://myreadingmanga.info/search/?search=$search")

                    if(main.contains("No results found")){
                        Utils.toast(act!!,
                            getString(com.sungbin.com.myreadingmanga.R.string.search_result_gone),
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING)
                    }
                    else LoadSearchTask().execute()

                    return false
                }

                override fun onQueryTextChange(str: String?): Boolean {
                    return false
                }

            })
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.setIconifiedByDefault(true)

        }
        return true
    }

    override fun onBackPressed() {
        backPressCloseHandler!!.onBackPressed()
    }

    @SuppressLint("StaticFieldLeak")
    private inner class LoadSearchTask : AsyncTask<Void, Void, Void>() {
        var pDialog:SweetAlertDialog? = null

        override fun onPreExecute() {
            pDialog = SweetAlertDialog(act!!, SweetAlertDialog.PROGRESS_TYPE)
            pDialog!!.progressHelper.barColor = getColor(R.color.colorPrimary)
            pDialog!!.titleText = "\n\n검색된 망가 목록 불러오는중..."
            pDialog!!.setCancelable(false)
            pDialog!!.show()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            val main = HTML.get("https://myreadingmanga.info/search/?search=$search")

            val entry = main.split("https://myreadingmanga.info/wp-content/plugins/wpsolr-pro/wpsolr/core/images/gif-load.gif")[1]
                .split("p_title\">")
            items!!.clear()

            for (i in 1 until entry.size) {
                val cash = entry[i]
                val name = cash.split("\">")[1].split("</a>")[0]
                val link = cash.split("<a href=\"")[1].split("\">")[0]
                val thumb = cash.split("wdm_result_list_thumb\" src=\"")[1].split("\">")[0]
                val data = MangaListItem(link, name, thumb)
                items!!.add(data)
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            adapter = MangaListAdapter(items, act!!)
            view!!.adapter = adapter
            view!!.layoutManager = LinearLayoutManager(act!!)
            adapter!!.notifyDataSetChanged()
            if(pDialog!!.isShowing) pDialog!!.cancel()
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class LoadListTask : AsyncTask<Void, Void, Void>() {
        var pDialog:SweetAlertDialog? = null

        override fun onPreExecute() {
            pDialog = SweetAlertDialog(act!!, SweetAlertDialog.PROGRESS_TYPE)
            pDialog!!.progressHelper.barColor = getColor(R.color.colorPrimary)
            pDialog!!.titleText = "\n\n망가 목록 불러오는중..."
            pDialog!!.setCancelable(false)
            pDialog!!.show()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            val main = HTML.get("https://myreadingmanga.info/")
            val entry = main.split("entry-title-link")

            for(i in 1 until entry.size - 1){
                val cash = entry[i]
                val name = cash.split("\">")[1].split("</a>")[0]
                val link = cash.split("bookmark\" href=\"")[1].split("\">")[0]
                val thumb = cash.split("entry-image-link")[1].split("src=\"")[1].split("\"")[0]
                val data = MangaListItem(link, name, thumb)
                items!!.add(data)
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            adapter = MangaListAdapter(items, act!!)
            view!!.adapter = adapter
            view!!.layoutManager = LinearLayoutManager(act!!)
            adapter!!.notifyDataSetChanged()
            pDialog!!.cancel()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val dialog = AlertDialog.Builder(this@MangaListActivity)
        dialog.setTitle(getString(R.string.list_manga_fav))

        val view = RecyclerView(this@MangaListActivity)
        val favItems: ArrayList<MangaFavItem> = ArrayList()
        val favList = Utils.readData(applicationContext, "fav", "null")!!.split("::")

        for(i in 1 until favList.size){
            val name = favList[i]
            val link = Utils.readData(applicationContext, name, "null")!!
            val data = MangaFavItem(link, name)
            favItems.add(data)
        }

        val favAdapter = MangaFavAdapter(favItems, act!!)
        view.adapter = favAdapter
        view.layoutManager = LinearLayoutManager(act!!)
        favAdapter.notifyDataSetChanged()

        val dividerItemDecoration = DividerItemDecoration(applicationContext,
            LinearLayoutManager(this@MangaListActivity).orientation)
        view.addItemDecoration(dividerItemDecoration)

        val container = FrameLayout(this@MangaListActivity)
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )

        params.leftMargin = resources.getDimensionPixelSize(R.dimen.activity_margin)
        params.rightMargin = resources.getDimensionPixelSize(R.dimen.activity_margin)
        params.topMargin = resources.getDimensionPixelSize(R.dimen.activity_margin)
        params.bottomMargin = resources.getDimensionPixelSize(R.dimen.activity_margin)

        view.layoutParams = params
        container.addView(view)

        dialog.setView(container)
        dialog.show()

        return super.onOptionsItemSelected(item)
    }

}
