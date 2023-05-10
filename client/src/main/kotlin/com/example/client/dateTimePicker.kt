package com.example.client

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.DatePicker
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.util.StringConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DateTimePicker(val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : DatePicker() {
    var dateTimeValue = SimpleObjectProperty<LocalDateTime>(LocalDateTime.now())

    init {
        converter = object : StringConverter<LocalDate>() {
            override fun toString(value: LocalDate?): String {
                return if (dateTimeValue.get() != null) dateTimeValue.get().format(formatter) else ""
            }

            override fun fromString(value: String?): LocalDate? {
                if (value == null) {
                    dateTimeValue.set(null)
                    return null
                }

                dateTimeValue.set(LocalDateTime.parse(value, formatter))
                return dateTimeValue.get().toLocalDate()
            }
        }

        // Syncronize changes to the underlying date value back to the dateTimeValue
        valueProperty().addListener { observable, old, new ->
            if (new == null) {
                dateTimeValue.set(null)
            } else {
                if (dateTimeValue.get() == null) {
                    dateTimeValue.set(LocalDateTime.of(new, LocalTime.now()))
                } else {
                    val time = dateTimeValue.get().toLocalTime()
                    dateTimeValue.set(LocalDateTime.of(new, time))
                }
            }
        }

        // Syncronize changes to dateTimeValue back to the underlying date value
        dateTimeValue.addListener { observable, old, new ->
            valueProperty().set(new?.toLocalDate())
        }

        // Persist changes onblur+
        editor.focusedProperty().addListener { observable, old, new ->
            if (!new)
                simulateEnterPressed()
        }

    }

    private fun simulateEnterPressed() {
        editor.fireEvent(
            KeyEvent(
                editor,
                editor,
                KeyEvent.KEY_PRESSED,
                null,
                null,
                KeyCode.ENTER,
                false,
                false,
                false,
                false
            )
        )
    }
    fun dateTimeValueProperty() = dateTimeValue;
}