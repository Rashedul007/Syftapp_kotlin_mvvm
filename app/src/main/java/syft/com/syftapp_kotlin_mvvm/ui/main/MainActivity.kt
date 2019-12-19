package syft.com.syftapp_kotlin_mvvm.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import syft.com.syftapp_kotlin_mvvm.R
import syft.com.syftapp_kotlin_mvvm.utils.VerticalSpacingItemDecorator
import syft.com.syftapp_kotlin_mvvm.viewmodels.ViewModelProviderFactory
import javax.inject.Inject

class MainActivity  : DaggerAppCompatActivity() {

    private val LOG_TAG = "ReposMainAct"

      companion object {
         var searchString: String = ""
    }

    lateinit var mRecycleVw: RecyclerView
    lateinit var mAdapter: MainRecycleAdapter


    lateinit  var mainViewModel: MainViewModel

    @set:Inject
    var providerFactory: ViewModelProviderFactory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()

        progressBar.visibility = View.GONE

        mainViewModel = ViewModelProviders.of(this, providerFactory).get( MainViewModel::class.java )

        observeRepos()

    }


    fun getReposFromServer(filter_search :String ,filter_topics: String?, filter_language: String? ,page_number:Int =0)
    {
        Toast.makeText(this, "Topics : $filter_topics \n  Language: $filter_language",Toast.LENGTH_SHORT).show()
        mainViewModel.getReposFromServer(filter_search, filter_topics, filter_language , page_number)
    }

    private fun observeRepos() {
        mainViewModel.observeReposFromServer().observe(this, Observer { repos ->

            txtVwCount.setText("total item count is: "+repos?.total_count.toString())

        })

        mainViewModel.observeItemList().observe(this, Observer {

            if(!it.isNullOrEmpty())
                if(it.size>0) {
                    mAdapter.setReposInAdapter(it)
                    progressBar.visibility = View.GONE
                }

        })
    }


    private fun initRecyclerView() {
        mRecycleVw = findViewById(R.id.rclrVw) as RecyclerView
        val gridLayoutManager = GridLayoutManager(this, 1)
        mRecycleVw.layoutManager = gridLayoutManager
        mRecycleVw.addItemDecoration(VerticalSpacingItemDecorator(10))
        mAdapter = MainRecycleAdapter(this)
        mRecycleVw.adapter = mAdapter

        mRecycleVw.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if(!mRecycleVw.canScrollVertically(1))
                {
                   // Toast.makeText(this@MainActivity,"Need to pagination",Toast.LENGTH_SHORT ).show()
                    mainViewModel.searchNextPage()

                }
            }

        }
        )

    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        var searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        var searchView : SearchView = searchItem?.actionView as SearchView


        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if(it.trim().length>0) {
                        progressBar.visibility = View.VISIBLE

                        searchString = it

                        clearOldCalls()
                        // mainViewModel.clearRetrofitCall()
                        getReposFromServer(it,"","")

                        //  observeRepos()
                    }
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                 return false;
            }

        })


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_filter -> {
                setUpFilterDialog();
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpFilterDialog() {
        val mAlertBuilder =    AlertDialog.Builder(this)
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.dialog_filter, null)
        mAlertBuilder.setPositiveButton("ok", null)
        mAlertBuilder.setNegativeButton("cancel", null)
        mAlertBuilder.setView(promptsView)

        val mEdTxtVwTopics =    promptsView.findViewById<View>(R.id.edtDl_Topics) as EditText
        val mEdTxtVwLanguage =    promptsView.findViewById<View>(R.id.edtDl_Language) as EditText

        val mAlertDialog = mAlertBuilder.create()
        mAlertDialog.setOnShowListener {
            val btnDialog_positive =  mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btnDialog_positive.setOnClickListener {
                clearOldCalls();
                if(searchString.trim().length >0)
                    getReposFromServer( searchString,  mEdTxtVwTopics.text.toString(),    mEdTxtVwLanguage.text.toString()   )

        observeRepos()

                mAlertDialog.dismiss()
            }
        }
        mAlertDialog.show()
    }

    override fun onStop() {
        mainViewModel.clearRetrofitCall()

        super.onStop()
    }

    fun clearOldCalls()
    {
        mainViewModel.clearRetrofitCall()
        mAdapter.clearListInAdapter()
    }
}
