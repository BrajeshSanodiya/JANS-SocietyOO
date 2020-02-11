package com.jans.tracking

import android.os.Bundle
import java.util.*

object TrackingUtils {

    fun mapToBundle(map: Map<String, Any?>) = Bundle(map.size).apply {

        for ((key, value) in map) {
            when (value) {
                null -> putString(key, null) // Any nullable type will suffice.

                // Scalars
                is Boolean -> putBoolean(key, value)
                is Double -> putDouble(key, value)
                is Float -> putFloat(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                is CharSequence -> putCharSequence(key, value)
                is Date -> putString(key, value.toString())

                // Reference arrays
                is Array<*> -> {
                    val componentType = value::class.java.componentType!!
                    @Suppress("UNCHECKED_CAST") // Checked by reflection.
                    when {
                        String::class.java.isAssignableFrom(componentType) -> {
                            putStringArray(key, value as Array<String>)
                        }
                        else -> {
                            val valueType = componentType.canonicalName
                            throw IllegalArgumentException(
                                "Illegal value array type $valueType for key \"$key\""
                            )
                        }
                    }
                }

                else -> {
                    val valueType = value.javaClass.canonicalName
                    throw IllegalArgumentException("Illegal value type $valueType for key \"$key\"")
                }
            }
        }
    }

    fun convertMapAnyToMapString(map: Map<String, Any?>) = mutableMapOf<String, String>().apply {
        for ((key, value) in map) {
            put(key, value.toString())
        }
    }
}