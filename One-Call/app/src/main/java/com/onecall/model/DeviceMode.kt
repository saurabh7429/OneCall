package com.onecall.model

enum class DeviceMode {
    MAIN,
    SECONDARY;

    companion object {
        fun fromString(value: String?): DeviceMode? = values().find { it.name == value }
    }
}
