package syft.com.syftapp_kotlin_mvvm.ui.main

import android.os.Bundle
import android.os.CountDownTimer
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import syft.com.syftapp_kotlin_mvvm.R
import syft.com.syftapp_kotlin_mvvm.models.GitResult
import syft.com.syftapp_kotlin_mvvm.models.ItemList
import syft.com.syftapp_kotlin_mvvm.models.SearchQuery
import syft.com.syftapp_kotlin_mvvm.utils.*
import syft.com.syftapp_kotlin_mvvm.viewmodels.ViewModelProviderFactory
import javax.inject.Inject


class MainActivity  : DaggerAppCompatActivity() {

      companion object {
         var searchString: String = ""
    }

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

        initRecyclerView()

        progressBar.visibility = View.GONE

        mainViewModel = ViewModelProvider(this, providerFactory).get( MainViewModel::class.java )

        observeRepos()


    }



    private fun observeRepos() {
        mainViewModel.apiData.observe(this, Observer {

           // prepareUiFromGenericData(it)
            Log.d( "filterTest", "observer 1 triggered "     )
        })

        mainViewModel.liveResult.observe(this, Observer {
            Log.d( "filterTest", "observer 2 triggered "     )
            prepareUiFromGenericData(it)
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
                    if(!isQueryExhausted)   {
                            progressBar.visibility = View.VISIBLE
                            mainViewModel.searchNextPage()
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
                newText?.let {
                    if(it.trim().length>0) {
                        progressBar.visibility = View.VISIBLE

                cntr?.cancel()

                cntr = object : CountDownTimer(waitingTime.toLong(), 500) {
                    override fun onTick(millisUntilFinished: Long) {
                        Log.d( "shaonmvvm", "seconds remaining: " + millisUntilFinished / 1000       )          }

                    override fun onFinish() {
                        Log.d("shaonmvvm", "DONE")

                                searchString = it

                                mItemListList.clear()

                                mainViewModel.searchQuery.value = SearchQuery(it,"","",1)

                            }
                        }
                    }
                }
                cntr?.start()
                return false

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

                if(searchString.trim().length >0)  {
                    mItemListList.clear()
                    progressBar.visibility = View.VISIBLE

                    mainViewModel.searchQuery.value = SearchQuery(searchString, mEdTxtVwTopics.text.toString(),    mEdTxtVwLanguage.text.toString(),1)
                }

                mAlertDialog.dismiss()

            }
        }
        mAlertDialog.show()
    }




//endregion


    fun prepareUiFromGenericData(genericData: GenericApiResponse<GitResult>)
    {
        when(genericData){
            is ApiSuccessResponse ->{
                if(mItemListList.size % 30 != 0)
                     isQueryExhausted = true

                    txtVwCount.setText("Total count is: " + genericData.body.total_count.toString())

               // mItemListList = genericData.body.items
                mItemListList.addAll(genericData.body.items)

                mAdapter.setReposInAdapter(mItemListList)
                progressBar.visibility = View.GONE
            }

            is ApiErrorResponse ->{
                Log.d("shaonmvvm" , "2. GOT ERROR RESULT: "+ genericData.errorMessage)
                progressBar.visibility = View.GONE
                 Toast.makeText(this@MainActivity,"Something went wrong",Toast.LENGTH_SHORT ).show()
            }

            is ApiEmptyResponse ->{
                Log.d("shaonmvvm" , "2. GOT EMPTY RESULT: ")
                progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity,"Something went wrong",Toast.LENGTH_SHORT ).show()

            }

        }

    }



}
