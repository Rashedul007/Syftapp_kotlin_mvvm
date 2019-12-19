package syft.com.syftapp_kotlin_mvvm.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ItemList (

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("language")
    @Expose
    var language: String?,

    @SerializedName("description")
    @Expose
    var description: String?,

    @SerializedName("html_url")
    @Expose
    var html_url: String?

):Parcelable

    {

        override fun toString(): String {
            return "BlogPost(id=$id, name=$name, Desc=$description)"
        }


    //region .......parcalable

        constructor(parcelIn: Parcel):this(
                parcelIn.readInt(),
                parcelIn.readString(),
                parcelIn.readString(),
                parcelIn.readString(),
                parcelIn.readString()
        )

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(id)
            dest.writeString(name)
            dest.writeString(language)
            dest.writeString(description)
            dest.writeString(html_url)
        }

        override fun describeContents(): Int {
            return 0
        }


        companion object CREATOR : Parcelable.Creator<ItemList> {
            override fun createFromParcel(parcel: Parcel): ItemList {
                return ItemList(parcel)
            }

            override fun newArray(size: Int): Array<ItemList?> {
                return arrayOfNulls(size)
            }
        }



    //endregion
    }
