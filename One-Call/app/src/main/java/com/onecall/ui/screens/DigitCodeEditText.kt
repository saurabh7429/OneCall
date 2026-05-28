package com.onecall.ui.screens

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.Gravity
import androidx.appcompat.widget.AppCompatEditText

class DigitCodeEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle,
) : AppCompatEditText(context, attrs, defStyleAttr) {

    var onDigitEntered: ((String) -> Unit)? = null
    var onBackspaceAtEmpty: (() -> Unit)? = null
    var onContentChanged: (() -> Unit)? = null

    private var suppressCallback = false

    init {
        inputType = InputType.TYPE_CLASS_NUMBER
        filters = arrayOf(InputFilter.LengthFilter(1))
        gravity = Gravity.CENTER
        isSingleLine = true
        textAlignment = TEXT_ALIGNMENT_CENTER
        importantForAutofill = IMPORTANT_FOR_AUTOFILL_NO

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                if (suppressCallback) {
                    return
                }

                val currentText = s?.toString().orEmpty()
                val cleanedText = currentText.filter(Char::isDigit).take(1)
                if (cleanedText != currentText) {
                    suppressCallback = true
                    setText(cleanedText)
                    setSelection(text?.length ?: 0)
                    suppressCallback = false
                    return
                }

                if (cleanedText.isNotEmpty()) {
                    onDigitEntered?.invoke(cleanedText)
                }

                onContentChanged?.invoke()
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (text.isNullOrEmpty()) {
                onBackspaceAtEmpty?.invoke()
            } else {
                suppressCallback = true
                setText("")
                suppressCallback = false
                onContentChanged?.invoke()
                onBackspaceAtEmpty?.invoke()
            }
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateInputConnection(outAttrs: android.view.inputmethod.EditorInfo): android.view.inputmethod.InputConnection? {
        val baseConnection = super.onCreateInputConnection(outAttrs) ?: return null
        return object : android.view.inputmethod.InputConnectionWrapper(baseConnection, true) {
            override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
                if (beforeLength == 1 && afterLength == 0) {
                    if (this@DigitCodeEditText.text.isNullOrEmpty()) {
                        onBackspaceAtEmpty?.invoke()
                    } else {
                        suppressCallback = true
                        setText("")
                        suppressCallback = false
                        onContentChanged?.invoke()
                        onBackspaceAtEmpty?.invoke()
                    }
                    return true
                }
                return super.deleteSurroundingText(beforeLength, afterLength)
            }
        }
    }
}