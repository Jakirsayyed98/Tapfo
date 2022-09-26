package app.tapho.ui.tcash

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import app.tapho.R
import app.tapho.databinding.ActivityTcashbackDetailBinding
import app.tapho.interfaces.SpanClickListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.help.SupportServiceActivity
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*


class TCashbackDetailActivity : BaseActivity<ActivityTcashbackDetailBinding>(), SpanClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTcashbackDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIv.setOnClickListener { onBackPressed() }
        binding.cashbackInfoIv.setOnClickListener {
            ValidationPeriodDialogFragment.newInstance().show(supportFragmentManager, "")
        }
        binding.cashbackStatusInfoIv.setOnClickListener {
            CashbackStatusInfoDialogFragment.newInstance().show(supportFragmentManager, "")
        }
        binding.infoIv.setOnClickListener {
            GeneralQuestionsDialogFragment.newInstance().show(supportFragmentManager, "")
        }

        binding.reDetail.setOnClickListener {
            startActivity(Intent(this, SupportServiceActivity::class.java))
        }

        init()
        setData()
    }

    private fun init() {
        binding.shareTv.setOnClickListener {
            checkPermission()
        }
        binding.shareIv.setOnClickListener {
            checkPermission()
        }
    }

    private fun setData() {
        Gson().fromJson(intent.getStringExtra(DATA), TCashDashboardData::class.java)?.let { data ->
            var intColor = R.color.green_dark

            when (data.status?.uppercase()) {
                "VERIFIED" -> {
                    binding.statusTv.text = getString(R.string.cashback_verified)
                    binding.cashbackMessTv.text = getString(R.string.cashback_verified_mess,
                        withSuffixAmount(data.user_commision), data.offer_name)
                    intColor = R.color.green_dark
                    binding.statusIv.setImageResource(R.drawable.ic_verified)
                }
                "VALIDATED" -> {
                    binding.statusTv.text = getString(R.string.cashback_verified)
                    binding.cashbackMessTv.text = getString(R.string.cashback_verified_mess,
                        withSuffixAmount(data.user_commision), data.offer_name)
                    intColor = R.color.green_dark
                    binding.statusIv.setImageResource(R.drawable.ic_verified)
                }
                "PENDING" -> {
                    binding.statusTv.text = getString(R.string.cashback_pending)
                    binding.cashbackMessTv.text = getString(R.string.cashback_pending_mess,
                        withSuffixAmount(data.user_commision), data.offer_name)
                    intColor = R.color.offer_coupon
                    binding.statusIv.setImageResource(R.drawable.ic_pending)
                }
                "FAILED" -> {
                    binding.statusTv.text = getString(R.string.cashback_rejected)
                    binding.cashbackMessTv.text = getString(R.string.cashback_rejected_mess,
                        withSuffixAmount(data.user_commision), data.offer_name)
                    intColor = R.color.red
                    binding.statusIv.setImageResource(R.drawable.ic_rejected)
                }
                "REJECTED" -> {
                    binding.statusTv.text = getString(R.string.cashback_rejected)
                    binding.cashbackMessTv.text = getString(R.string.cashback_rejected_mess,
                        withSuffixAmount(data.user_commision), data.offer_name)
                    intColor = R.color.red
                    binding.statusIv.setImageResource(R.drawable.ic_rejected)
                }
                else -> {
                }
            }

            statusBarColor(intColor)
            binding.reHeader.setBackgroundColor(ContextCompat.getColor(this, intColor))
            binding.liTop.setBackgroundColor(ContextCompat.getColor(this, intColor))

            binding.merchantNameTv.text = data.offer_name
            Glide.with(binding.merchantIv).load(data.image).into(binding.merchantIv)
            binding.offerTv.text = URLDecoder.decode(data.cashback, "UTF-8");
            binding.transactionId.text = data.trans_id
            binding.dateTv.text = parseDate2(data.date)
            binding.orderAmountTv.text = withSuffixAmount(data.sale_amount)
            binding.cashbackAmountTv.text = withSuffixAmount(data.user_commision)
            binding.statusTv2.text = getSpannableBold(getString(R.string.tcashback_status_,
                    data.status?.replaceFirstChar { it.uppercase() }), 18, -1, ContextCompat.getColor(this, intColor), 1.1f)
            binding.validationPeriodTv.text = getSpannableBold(getString(R.string.validation_period_,
                    getValidationPeriod(data)),
                    19,
                    -1,
                    ContextCompat.getColor(this, R.color.black), 1.1f)

            val warningMess = getString(R.string.merchant_cashback_mess)
            binding.cashbackWarningTv.text =
                setClickableSpannable(warningMess, warningMess.length - 13, warningMess.length - 1,
                    ContextCompat.getColor(this, R.color.purple_500), object : SpanClickListener {
                        override fun onSpanClick() {

                        }
                    })
        }
    }

    private fun getValidationPeriod(data: TCashDashboardData): String {
        try {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(data.date)?.let {
                return SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(Calendar.getInstance()
                    .apply {
                        time = it
                        add(Calendar.DATE, 60)
                    }.time)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    override fun onSpanClick() {

    }

    private fun checkPermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted())
                        store(getScreenShot(binding.mainLi), "status.jpg")
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?,
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun getScreenShot(view: View): Bitmap {
        val screenView = view.rootView
        screenView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(screenView.drawingCache)
        screenView.isDrawingCacheEnabled = false
        return bitmap
    }

    fun store(bm: Bitmap, fileName: String) {
        val dirPath: String = getExternalFilesDir(null)?.absolutePath + "/Screenshots"
        val dir = File(dirPath)
        if (!dir.exists())
            dir.mkdirs()
        val file = File(dirPath, fileName)
        try {
            val fOut = FileOutputStream(file)
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()
            shareImage(file)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun shareImage(file: File) {
//        val uri: Uri = Uri.fromFile(file)
        val uri = FileProvider.getUriForFile(this, "$packageName.com.vansuita.pickimage.provider", file)
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        intent.putExtra(Intent.EXTRA_TEXT, "")
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show()
        }
    }


}