# Syft Git Search App

![alt text](https://github.com/shaon007/Syftapp_kotlin_mvvm/blob/master/app/src/main/res/drawable/screenshot_syft.png)

The app generates search results in the form of a list as the user types in text into the search field.
There is an additional filter function next to the search box, this opens a dialog with the options to filter results by topic and language categories.

A total count of the search result is shown below the search field.

The results are sorted by the order of their star rating from high to low. The results display git name, language followed by a truncated description. On click, they open the git repository in web view. 


## Technologies implemented

*	Kotlin - This is my second implementation in Kotlin. I am still in the process of learning the language as I want to cross over to Kotlin entirely for Android development.
*	MVVM pattern
*	Dagger 2
*	Retrofit
*	Infinite scrolling mechanism
*	Unit test, mockito


## Development brief

In order to search git repositories whenever user types anything in searchbox, I needed to make api call every time a character is entered and cancel the previous api calls made already.

First attempt I used RxJava,  but that made a sperate API call for each of the characters entered in the search field and showed random results due to being observed as live data, this accumulated as a list of results at the end with several search results.

Using switchMap with LiveData as a parameter instead has been more useful, this method effectively cancels an API call by only returning  the result for the most recent submitted API call.

I introduced a two second delay before each API request. The timing starts from the last character typed. This allows a person to enter multiple characters in between and retrieve one set of result that is much more relevant. 

I wanted to filter from just the search results but couldn’t find the fields to filter on. So I had to make new api calls to filter by topic and language. If I could have stored the result in local database then it would have been faster to run the filter.

I have spent roughly 2 days to develop this. It took me a bit of time to write the  test codes as I haven’t had the need to write them in the past.


## Testing

As this is the search app, I focused my tests on making sure search entries don’t return errors on mistakes like whitespaces and calls correct methods for api calls and results. I have also used a generic class for handling any error or empty results.

I have used unit testing with Mockito. Here is a breakdown of the tests I have written so far:

*	If no character or whitespace is entered in search then the api call result isn’t observed
*	If no character or whitespace is entered in search then the method for api call isn’t called
*	If success result is turned from api call then the result can be observed with LiveData
*	If any text is entered in  search box then correct method is being called 
*	If somehow empty search query is submitted for api calls then it throws exception



## Built With

Using the Github API

https://developer.github.com/v3/search/#search-repositories



## Author

* **Rashedul Hasan**  

