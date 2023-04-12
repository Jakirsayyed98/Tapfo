package app.tapho.RoomDB

import androidx.room.TypeConverter
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.model.*
import app.tapho.ui.scanner.model.AllProducts.BusinessItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RoomConverters {

    @TypeConverter
    fun listtojsonAppcategory(value:ArrayList<AppCategory>?) =Gson().toJson(value)

    @TypeConverter
    fun formString(listOfString: String?):ArrayList<AppCategory>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<AppCategory?>?>() {}.type)
    }

    @TypeConverter
    fun formAnyString(listOfString: String?):ArrayList<Any>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<Any?>?>() {}.type)
    }

    @TypeConverter
    fun fromArrayAnyList(listdata:ArrayList<Any>):String{
        return Gson().toJson(listdata)
    }


    @TypeConverter
    fun formBanner1String(listOfString: String?):ArrayList<BannerList>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<BannerList?>?>() {}.type)
    }

    @TypeConverter
    fun fromArrayBanner1List(listdata:ArrayList<BannerList>):String{
        return Gson().toJson(listdata)
    }

    @TypeConverter
    fun formBanner2String(listOfString: String?):ArrayList<BannerList2>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<BannerList2?>?>() {}.type)
    }

    @TypeConverter
    fun fromArrayBanner2List(listdata:ArrayList<BannerList2>):String{
        return Gson().toJson(listdata)
    }


    @TypeConverter
    fun formBanner3String(listOfString: String?):ArrayList<BannerList3>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<BannerList3?>?>() {}.type)
    }

    @TypeConverter
    fun fromArrayBanner3List(listdata:ArrayList<BannerList3>):String{
        return Gson().toJson(listdata)
    }

    @TypeConverter
    fun formBanner4String(listOfString: String?):ArrayList<BannerList4>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<BannerList4?>?>() {}.type)
    }

    @TypeConverter
    fun fromArrayBanner4List(listdata:ArrayList<BannerList4>):String{
        return Gson().toJson(listdata)
    }

    @TypeConverter
    fun formBanner5String(listOfString: String?):ArrayList<BannerList5>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<BannerList5?>?>() {}.type)
    }

    @TypeConverter
    fun fromArrayBanner5List(listdata:ArrayList<BannerList5>):String{
        return Gson().toJson(listdata)
    }

    @TypeConverter
    fun formBanner6String(listOfString: String?):ArrayList<BannerList6>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<BannerList6?>?>() {}.type)
    }


    @TypeConverter
    fun fromArrayBanner6List(listdata:ArrayList<BannerList6>):String{
        return Gson().toJson(listdata)
    }

    @TypeConverter
    fun formBanner7String(listOfString: String?):ArrayList<BannerList7>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<BannerList7?>?>() {}.type)
    }


    @TypeConverter
    fun fromArrayBanner7List(listdata:ArrayList<BannerList7>):String{
        return Gson().toJson(listdata)
    }


    @TypeConverter
    fun formBanner8String(listOfString: String?):ArrayList<BannerList8>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<BannerList8?>?>() {}.type)
    }


    @TypeConverter
    fun fromArrayBanner8List(listdata:ArrayList<BannerList8>):String{
        return Gson().toJson(listdata)
    }



    @TypeConverter
    fun formBanner9String(listOfString: String?):ArrayList<BannerList9>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<BannerList9?>?>() {}.type)
    }


    @TypeConverter
    fun fromArrayBanner9List(listdata:ArrayList<BannerList9>):String{
        return Gson().toJson(listdata)
    }

    @TypeConverter
    fun formBanner10String(listOfString: String?):ArrayList<BannerList10>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<BannerList10?>?>() {}.type)
    }


    @TypeConverter
    fun fromArrayBanner10List(listdata:ArrayList<BannerList10>):String{
        return Gson().toJson(listdata)
    }


    @TypeConverter
    fun formcashback_merchantString(listOfString: String?):ArrayList<CashbackMerchant>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<CashbackMerchant?>?>() {}.type)
    }


    @TypeConverter
    fun fromArraycashback_merchantList(listdata:ArrayList<CashbackMerchant>):String{
        return Gson().toJson(listdata)
    }


    @TypeConverter
    fun formcHeadlineString(listOfString: String?):ArrayList<Headline>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<Headline?>?>() {}.type)
    }


    @TypeConverter
    fun fromArrayHeadlineList(listdata:ArrayList<Headline>):String{
        return Gson().toJson(listdata)
    }

    @TypeConverter
    fun formcNewCashbackString(listOfString: String?):ArrayList<NewCashback>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<NewCashback?>?>() {}.type)
    }


    @TypeConverter
    fun fromArrayNewCashbackList(listdata:ArrayList<NewCashback>):String{
        return Gson().toJson(listdata)
    }

    @TypeConverter
    fun formcDataString(listOfString: String?):ArrayList<Data>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<Data?>?>() {}.type)
    }


    @TypeConverter
    fun fromArrayDataList(listdata:ArrayList<Data>):String{
        return Gson().toJson(listdata)
    }

 @TypeConverter
    fun formcPopularString(listOfString: String?):ArrayList<Popular>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<Popular?>?>() {}.type)
    }


    @TypeConverter
    fun fromArrayPopularList(listdata:ArrayList<Popular>):String{
        return Gson().toJson(listdata)
    }

    @TypeConverter
    fun formcprofileString(listOfString: String?):ArrayList<app.tapho.ui.model.UserDetails.Data>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<app.tapho.ui.model.UserDetails.Data?>?>() {}.type)
    }


    @TypeConverter
    fun fromArrayprofileList(listdata:ArrayList<app.tapho.ui.model.UserDetails.Data>):String{
        return Gson().toJson(listdata)
    }

    @TypeConverter
    fun formcPromotedAppString(listOfString: String?):ArrayList<PromotedApp>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<PromotedApp?>?>() {}.type)
    }


    @TypeConverter
    fun fromArrayPromotedAppList(listdata:ArrayList<PromotedApp>):String{
        return Gson().toJson(listdata)
    }


    @TypeConverter
    fun formcSuperCategoryString(listOfString: String?):ArrayList<SuperCategory>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<SuperCategory?>?>() {}.type)
    }


    @TypeConverter
    fun fromArraySuperCategoryList(listdata:ArrayList<SuperCategory>):String{
        return Gson().toJson(listdata)
    }


    @TypeConverter
    fun formcServiceString(listOfString: String?):ArrayList<Service>?{
        return Gson().fromJson(listOfString, object : TypeToken<List<Service?>?>() {}.type)
    }


    @TypeConverter
    fun fromArrayServiceList(listdata:ArrayList<Service>):String{
        return Gson().toJson(listdata)
    }

  @TypeConverter
    fun formcBusinessItemString(listOfString: String?):BusinessItem{
        return Gson().fromJson(listOfString, object : TypeToken<BusinessItem?>() {}.type)
    }


    @TypeConverter
    fun fromArrayBusinessItemList(listdata:BusinessItem):String{
        return Gson().toJson(listdata)
    }









}