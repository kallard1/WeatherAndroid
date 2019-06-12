package fr.area42.weatherandroid.city

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.widget.EditText
import fr.area42.weatherandroid.R

class CreateCityDialogFragment : DialogFragment() {
    interface CreateCityDialogListener {
        fun onDialogPositiveClick(cityName: String)
        fun onDialogNegativeClick()
    }

    private var listener: CreateCityDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)

        val input = EditText(context)
        with(input) {
            inputType = InputType.TYPE_CLASS_TEXT
            hint = context.getString(R.string.add_city_hint)
        }

        builder.setTitle(getString(R.string.new_city_title))
            .setView(input)
            .setPositiveButton(R.string.add_city_positive_button,
                DialogInterface.OnClickListener { _, _ ->
                    listener?.onDialogPositiveClick(input.text.toString())
                })
            .setNegativeButton(R.string.add_city_negative_button,
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                    listener?.onDialogNegativeClick()
                })

        return builder.create()
    }
}