package app.tapho.ui.localbizzUI.BusinessFlow

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.tapho.R
import app.tapho.databinding.FragmentAddBusinessProfileDetailBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.utils.ImageResizer
import app.tapho.utils.RealPathUtil
import app.tapho.utils.toast
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class AddBusinessProfileDetailFragment : BaseFragment<FragmentAddBusinessProfileDetailBinding>(),
    IPickResult {
    private var image: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBusinessProfileDetailBinding.inflate(inflater,container,false)
        _binding!!.toolbar.tvTitle.text="Listing Business"
        statusBarTextWhite()
        statusBarColor(R.color.white)
        clickevents()


        return _binding!!.root
    }

    private fun buttonSelected() {


        if (!getSharedPreference().getString("businessDescription").isNullOrEmpty()){
            _binding!!.btnVerify.isSelected = true
        }
        if (!getSharedPreference().getString("secondary_number").isNullOrEmpty()){
            _binding!!.btnVerify.isSelected = true
        }
        if (!getSharedPreference().getString("businessEmail").isNullOrEmpty()){
            _binding!!.btnVerify.isSelected = true
        }
        if (!getSharedPreference().getString("business_whatsapp_number").isNullOrEmpty()){
            _binding!!.btnVerify.isSelected = true
        }
        if (!getSharedPreference().getString("ESTB_YEAR").isNullOrEmpty()){
            _binding!!.btnVerify.isSelected = true
        }
        if (!getSharedPreference().getString("profile_image").isNullOrEmpty()){
            _binding!!.btnVerify.isSelected = true
        }
        if (!getSharedPreference().getString("SaveProfile_banner").isNullOrEmpty()){
            _binding!!.btnVerify.isSelected = true
        }
    }

    override fun onStart() {
        super.onStart()
        setAllTextData()
        buttonSelected()

    }

    override fun onResume() {
        super.onResume()
        setAllTextData()
        buttonSelected()
    }
    private fun setAllTextData() {
        _binding!!.apply {
            businessDescription.text =if (getSharedPreference().getString("businessDescription").isNullOrEmpty()) "Tell About your Business" else getSharedPreference().getString("businessDescription")
            contactnumber.text =if (getSharedPreference().getString("secondary_number").isNullOrEmpty()) "Enter your business Conatcs" else getSharedPreference().getString("secondary_number").toString()+","+getSharedPreference().getString("optional_number").toString()
            addEmail.text =if (getSharedPreference().getString("businessEmail").isNullOrEmpty()) "Enter your business email id" else getSharedPreference().getString("businessEmail")
            businessWhatsappNumber.text =if (getSharedPreference().getString("business_whatsapp_number").isNullOrEmpty()) "Enter your Business WhatsApp Number" else getSharedPreference().getString("business_whatsapp_number")
            businessWebsiteUrl.text =if (getSharedPreference().getString("businesswebsite").isNullOrEmpty()) "Enter your business website Url" else getSharedPreference().getString("businesswebsite")



            Glide.with(requireContext()).load(getSharedPreference().getString("profile_image")).circleCrop().centerCrop().into(_binding!!.iconProfile)
            val profile_banner = getSharedPreference().getString("SaveProfile_banner")
            if(!profile_banner.isNullOrEmpty()){
                Glide.with(requireContext()).load(profile_banner).centerCrop().into(_binding!!.shopImage)
//                _binding!!.shopImageAdd.visibility = View.GONE
            }

//            if (getSharedPreference().getString("business_whatsapp_number").isNullOrEmpty().not()){
//               _binding!!.radioComplete.isChecked = true
//            }else{
//                _binding!!.radioComplete.isChecked = false
//            }
        }
    }

    private fun clickevents() {
        _binding!!.shopImageAdd.setOnClickListener {
            PickImageDialog.build(PickSetup(), this).show(childFragmentManager)
        }
        _binding!!.iconProfile.setOnClickListener {
            PickImageDialog.build(PickSetup(), object : IPickResult{
                override fun onPickResult(r: PickResult?) {
                    if (r?.error == null) {
//            getImageView().setImageBitmap(r.getBitmap());
                        image = RealPathUtil.getRealPath(context, r?.uri)
                        setImage1(image)
                    } else {
                        context?.toast(r.error.message)
                    }
                }

            }).show(childFragmentManager)
        }
        _binding!!.apply {

            toolbar.backIv.setOnClickListener {
                activity?. onBackPressedDispatcher?.onBackPressed()
            }
            btnVerify.setOnClickListener {
                if (getSharedPreference().getString("business_whatsapp_number").isNullOrEmpty()){
                    Snackbar.make(requireView()," Please fill all the information ", Toast.LENGTH_SHORT).show()
                }else{
                    LocalbizContainerActivity.openContainer(requireContext(),"AddBusinessAddressFragment")
                }
            }

            businessPhoneNumber.setOnClickListener{
                LocalbizContainerActivity.openContainer(requireContext(),"AddBusinessPhone_Number","0")
            }
            businessDiscription.setOnClickListener {
                if (getSharedPreference().getString("profile_image").isNullOrEmpty()){
                    Snackbar.make(requireView(),"You have to upload both image", Toast.LENGTH_SHORT).show()
                }else{
                    LocalbizContainerActivity.openContainer(requireContext(), "businessDiscription","0")
                }


            }
            businessPhoneNumber.setOnClickListener {
                if (getSharedPreference().getString("profile_image").isNullOrEmpty()){
                    Snackbar.make(requireView(),"You have to upload both image", Toast.LENGTH_SHORT).show()
                }else {
                    LocalbizContainerActivity.openContainer(requireContext(), "businessPhoneNumber")
                }
            }
             businessWebsite.setOnClickListener {
                LocalbizContainerActivity.openContainer(requireContext(), "businessWebSite")
            }
            businessEmail.setOnClickListener {
                if (getSharedPreference().getString("secondary_number").isNullOrEmpty()){
                    Snackbar.make(requireView(),"Please enter Contact number first", Toast.LENGTH_SHORT).show()
                }else {
                    LocalbizContainerActivity.openContainer(requireContext(), "businessEmail","0")
                }

            }
            businessWhatsappNumber.setOnClickListener {
                if (getSharedPreference().getString("secondary_number").isNullOrEmpty()){
                    Snackbar.make(requireView(),"Please enter Contact number first", Toast.LENGTH_SHORT).show()
                }else {
                    LocalbizContainerActivity.openContainer(requireContext(), "businessWhatsappNumber","0")
                }

            }


        }


    }

    private fun setImage1(image: String?) {
        getSharedPreference().saveString("profile_image",image!!)
        Glide.with(requireContext()).load(image).circleCrop().centerCrop().into(_binding!!.iconProfile)
    }

    private fun getBitmapreduce(reduceBitmap: Bitmap?): File {
       // val file :File = File(Environment.getExternalStorageDirectory() + File.separator + Calendar.getInstance().time)

        val file = File(Environment.getExternalStorageDirectory().toString() + File.separator + Calendar.getInstance().time)

        val OutputStream = ByteArrayOutputStream()
        reduceBitmap!!.compress(Bitmap.CompressFormat.JPEG,0,OutputStream)

        val bitmapdata: ByteArray = OutputStream.toByteArray()

        try {
            val fileOutputStream : FileOutputStream = FileOutputStream(file)
            file.createNewFile()
            fileOutputStream.write(bitmapdata)
            fileOutputStream.flush()
            fileOutputStream.close()
            return file
        }catch (e: Exception){
            e.printStackTrace()
        }
        return file
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddBusinessProfileDetailFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onPickResult(r: PickResult?) {
        if (r?.error == null) {
//            getImageView().setImageBitmap(r.getBitmap());
            image = RealPathUtil.getRealPath(context, r?.uri)
            setImage(image)
        } else {
            context?.toast(r.error.message)
        }
    }

    private fun setImage(image: String?) {
//        val reducesize : Bitmap = BitmapFactory.decodeFile(image)
//        val imageResizer = ImageResizer()
//        val reduceBitmap :Bitmap? =imageResizer.reduceBitmapSize(reducesize,240000)
//        val reducedImage : File =  getBitmapreduce(reduceBitmap)
//
//        getSharedPreference().saveString("SaveProfile_banner",reducedImage.toString())
//        Glide.with(requireContext()).load(reducedImage).circleCrop().centerCrop().into(_binding!!.shopImage)

        getSharedPreference().saveString("SaveProfile_banner",image!!)
        Glide.with(requireContext()).load(image).centerCrop().into(_binding!!.shopImage)
//        _binding!!.shopImageAdd.visibility = View.GONE
    }

}