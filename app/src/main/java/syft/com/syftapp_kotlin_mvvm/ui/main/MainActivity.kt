package syft.com.syftapp_kotlin_mvvm.ui.main

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html.fromHtml
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import syft.com.syftapp_kotlin_mvvm.R
import syft.com.syftapp_kotlin_mvvm.models.GitResult
import syft.com.syftapp_kotlin_mvvm.models.ItemList
import syft.com.syftapp_kotlin_mvvm.models.SearchQuery
import syft.com.syftapp_kotlin_mvvm.utils.ApiEmptyResponse
import syft.com.syftapp_kotlin_mvvm.utils.ApiErrorResponse
import syft.com.syftapp_kotlin_mvvm.utils.ApiSuccessResponse
import syft.com.syftapp_kotlin_mvvm.utils.GenericApiResponse
import syft.com.syftapp_kotlin_mvvm.viewmodels.ViewModelProviderFactory
import javax.inject.Inject


class MainActivity  : DaggerAppCompatActivity() {

      companion object {
         var searchString: String = ""
    }

    lateinit var toolBar:Toolbar

    lateinit var mRecycleVw: RecyclerView
    lateinit var mAdapter: MainRecycleAdapter


    lateinit  var mainViewModel: MainViewModel

    @set:Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val waitingTime = 2000
    private var cntr: CountDownTimer? = null

    private  var mItemListList: MutableList<ItemList> = ArrayList()

    private var isQueryExhausted:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolBar =  findViewById(R.id.main_toolbar) as Toolbar
        setSupportActionBar(toolBar)

        initRecyclerView()

        progressBar.visibility = View.GONE

        mainViewModel = ViewModelProvider(this, providerFactory).get( MainViewModel::class.java )

    }


    override fun onResume() {
        super.onResume()

        observeRepos()
    }



    private fun observeRepos() {
        mainViewModel.apiData.observe(this, Observer {

           // prepareUiFromGenericData(it)

        })

        mainViewModel.liveResult.observe(this, Observer {

            prepareUiFromGenericData(it)
        })
    }




    private fun initRecyclerView() {
        mRecycleVw = findViewById(R.id.rclrVw) as RecyclerView
        val gridLayoutManager = GridLayoutManager(this, 1)
        mRecycleVw.layoutManager = gridLayoutManager
        //mRecycleVw.addItemDecoration(VerticalSpacingItemDecorator(2))
        mAdapter = MainRecycleAdapter(this)
        mRecycleVw.adapter = mAdapter

        mRecycleVw.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if(!mRecycleVw.canScrollVertically(1)) {
                    if (!mItemListList.isNullOrEmpty() || mItemListList.size != 0) {
                        if (!isQueryExhausted) {
                            progressBar.visibility = View.VISIBLE
                            mainViewModel.searchNextPage()
                        }

                    }
                }
            }

        }
        )

    }




//region ....optionsMenu

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        var searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        var searchView : SearchView = searchItem?.actionView as SearchView


        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {typedStr ->
                    searchString = typedStr
                      cntr?.cancel()

                    if(!searchString.trim().isNullOrEmpty() || !searchString.trim().equals("") ) {
                        progressBar.visibility = View.VISIBLE


                        cntr = object : CountDownTimer(waitingTime.toLong(), 500) {

                                override fun onTick(millisUntilFinished: Long) {
                                    //Log.d( "err_chk", "timer: seconds remaining: " + millisUntilFinished / 1000       )
                                     }

                                override fun onFinish() {
                                    mItemListList.clear()

                                    mainViewModel.searchQuery.value = SearchQuery(searchString, "", "", 1)
                                }
                        }

                        cntr?.start()
                    }
                    else
                        progressBar.visibility = View.GONE

                }

                return false
            }

        })


        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_filter -> {
                if(searchString.trim().isNullOrEmpty() || searchString.trim().equals("") )
                    dialog_msg("Please enter something in search box first")
                else
                setUpFilterDialog()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpFilterDialog() {
        val mAlertBuilder =    AlertDialog.Builder(this, R.style.AlertDialogTheme)
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.dialog_filter, null)
/*        mAlertBuilder.setPositiveButton("ok", null)
        mAlertBuilder.setNegativeButton("cancel", null)*/
        mAlertBuilder.setView(promptsView)

        val mEdTxtVwTopics =    promptsView.findViewById<View>(R.id.edtDl_Topics) as EditText
        val mEdTxtVwLanguage =    promptsView.findViewById<View>(R.id.edtDl_Language) as EditText

        val mbtnOk =    promptsView.findViewById<View>(R.id.dialog_ok) as Button
        val mbtnCancel =    promptsView.findViewById<View>(R.id.dialog_cancel) as Button

        val mAlertDialog = mAlertBuilder.create()


        mAlertDialog.setOnShowListener {
            mbtnOk.setOnClickListener { if(!searchString.trim().isNullOrEmpty()){
                mItemListList.clear()
                progressBar.visibility = View.VISIBLE

                mainViewModel.searchQuery.value = SearchQuery(searchString, mEdTxtVwTopics.text.toString(),    mEdTxtVwLanguage.text.toString(),1)
            }

                mAlertDialog.dismiss()
            }

            mbtnCancel.setOnClickListener{
                mAlertDialog.dismiss()
            }
        }

        mAlertDialog.show()
    }

    private fun dialog_msg(msg:String) {
        AlertDialog.Builder(this@MainActivity)
            .setMessage(fromHtml("<font color='#c544c5'>$msg</font>"))
            .setCancelable(false)
            .setPositiveButton("ok", object : DialogInterface.OnClickListener {
                override fun onClick(
                    dialog: DialogInterface?,
                    which: Int
                ) {
                }
            }).show()
    }




//endregion


    fun prepareUiFromGenericData(genericData: GenericApiResponse<GitResult>)
    {
        when(genericData){
            is ApiSuccessResponse ->{
                if(mItemListList.size % 30 != 0 )
                     isQueryExhausted = true

                    txtVwCount.setText("Search result: " + genericData.body.total_count.toString())

                mItemListList.addAll(genericData.body.items)

                mAdapter.setReposInAdapter(mItemListList)
                progressBar.visibility = View.GONE
            }

            is ApiErrorResponse ->{
               // Log.d("shaonmvvm" , "2. GOT ERROR RESULT: "+ genericData.errorMessage)
                progressBar.visibility = View.GONE
                 Toast.makeText(this@MainActivity,"Something went wrong",Toast.LENGTH_SHORT ).show()
            }

            is ApiEmptyResponse ->{
                // Log.d("err_chk" , "2. GOT EMPTY RESULT: ")
                progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity,"Something went wrong",Toast.LENGTH_SHORT ).show()


            }

        }

    }



}
