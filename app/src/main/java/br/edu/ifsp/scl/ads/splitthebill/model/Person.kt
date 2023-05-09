package br.edu.ifsp.scl.ads.splitthebill.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Person (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    var name: String,
    var totalPaid: Double,
    var itemBought: String
) : Parcelable

