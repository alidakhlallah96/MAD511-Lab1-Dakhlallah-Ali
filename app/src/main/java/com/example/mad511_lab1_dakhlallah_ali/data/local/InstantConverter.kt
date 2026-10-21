package com.example.mad511_lab1_dakhlallah_ali.data.local

import androidx.room3.ColumnTypeConverter
import java.time.Instant

class InstantConverter {
    @ColumnTypeConverter fun fromInstant(v: Instant): Long = v.toEpochMilli()
    @ColumnTypeConverter fun toInstant(v: Long): Instant = Instant.ofEpochMilli(v)
}