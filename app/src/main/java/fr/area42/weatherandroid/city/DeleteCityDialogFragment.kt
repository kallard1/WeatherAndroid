package fr.area42.weatherandroid.city

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import fr.area42.weatherandroid.R

class DeleteCityDialogFragment : DialogFragment() {
    interface DeleteCityDialogListener {
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
    }

    companion object {
        val EXTRA_CITY_NAME = "fr.area42.weatherandroid.EXTRA_CITY_NAME"

        fun newInstance(cityName: String) : DeleteCityDialogFragment {
            val fragment = DeleteCityDialogFragment()
            fragment.arguments = Bundle().apply {
                putString(EXTRA_CITY_NAME, cityName)
            }

            return fragment
        }
    }

    private lateinit var cityName: String

    var listener: DeleteCityDialogListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cityName = arguments!!.getString(EXTRA_CITY_NAME)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)

        val negativeButton = builder.setTitle(getString(R.string.delete_city_title, cityName))
            .setPositiveButton(getString(R.string.common_yes)) { _, _ -> listener?.onDialogPositiveClick() }
            .setNegativeButton(getString(R.string.common_no)) { _, _ -> listener?.onDialogNegativeClick() }

        return builder.create()
    }
}